package ro.ubb.ktm.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.io.jvm.javaio.toInputStream
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.repo.DatabaseRepository
import java.util.concurrent.locks.ReentrantLock
import okhttp3.ConnectionSpec
import org.json.simple.parser.JSONParser
import ro.ubb.ktm.model.Photo
import java.io.InputStreamReader
import java.nio.ByteBuffer
import kotlin.streams.toList


class EntitiesService
{
    private var dbRepo: DatabaseRepository? = null
    private var hasConnection: Boolean = true;

    private var context: Context? = null;


    companion object{
        var  albums: MutableList<Album> = mutableListOf()
        var photos: MutableList<Photo> = mutableListOf()
        var BASE_URL = "https://75cf6343a09a.ngrok.io";
        private val httpClient = HttpClient(CIO)

        val lock = ReentrantLock();
    }

    fun dbInit(context: Context){
        this.context = context;
//        var eventDao = LocalDatabase.getDatabase(context).userEventDao();
//        this.eventRepository = UserEventRepository(eventDao);
        this.dbRepo = DatabaseRepository(context)
        this.hasConnection = true;
    }


    fun isOnline(): Boolean{
        if(this.context == null)
            return false;
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        cm.apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }


    suspend fun getAlbums(): MutableList<Album>{
        albums = getAlbumsAsync().await()
        if(albums.size>0)
        {
            println("AICI FII ANTENA")
            println(albums[0].id)
        }
        else
        {
            println("AOLEU NU E NIMIC IN BAZA DE DATE")
        }
        return albums;
    }


    suspend fun getPicsOfAlbum(id: Int): MutableList<Photo>{
        photos = getPhotosAsync(id).await()
        if(photos.size>0)
        {
            println("AICI FII ANTENA LA POZE")
            println(photos[0].id)
        }
        else
        {
            println("AOLEU NU E NIMIC IN BAZA DE DATE")
        }
        return photos;
    }

    fun jsonToAlbum(obj: org.json.JSONObject): Album {
        println(obj["id"])
        val id = obj["id"] as Int
//        val id2 = Math.toIntExact(id)
        return Album(
            id,
            obj["name"] as String,
            obj["description"] as String
        )
    }


    fun jsonToPhoto(obj: org.json.JSONObject): Photo {
        println(obj["id"])
        val id = obj["id"] as Int
//        val id2 = Math.toIntExact(id)
        return Photo(
            id,
            obj["albumId"] as Int,
            obj["comment"] as String,
            obj["path"] as String
        )
    }

    fun performServerUpdates(){
        GlobalScope.launch (Dispatchers.IO){
            lock.lock()
            val locals =
                dbRepo!!.getAllLocalAlbums()
            for(album in locals){
                val a = addAlbum2(album)
//                al.server = id
//                eventRepository!!.updateEvent(event)
                album.isOnlyLocal = a!!.id
                dbRepo!!.updateAlbum(album)
            }
            val local_pics = dbRepo!!.getPicturesLocal()
            println("AICI VAD CARE E FAZA CU POZELE LOCALE")

            for(pics in local_pics){
//                al.server = id
//                eventRepository!!.updateEvent(event)
                println("Albumul cum e in db " + pics.id.toString() + " " + pics.albumId  + " " + pics.albumOnlyLocal + " " + pics.isOnlyLocal)
                if(pics.albumOnlyLocal)
                {
                    var l = mutableListOf<Album>()
                    var all = dbRepo!!.getAllAlbums().stream().filter{a->a.id == pics.albumId}.toList()
                    pics.albumId = all[0].isOnlyLocal
                }
                val addPhotoServer = addPhotoServer(pics)
                println("Albumul de pe server: " + addPhotoServer!!.id.toString() + " " + addPhotoServer.albumId  + " " + addPhotoServer.albumOnlyLocal + " " + addPhotoServer.isOnlyLocal)

                pics.isOnlyLocal = addPhotoServer.id

                pics.albumOnlyLocal = false
                println("Albumul chiar cand ii fac update: " + pics.id.toString() + " " + pics.albumId  + " " + pics.albumOnlyLocal + " " + pics.isOnlyLocal)
                dbRepo!!.updatePicture(pics)

            }
//            dbRepo!!.setAllAlbumsAsNonLocal()
            if(lock.isHeldByCurrentThread)
                lock.unlock();
        }
    }



    private suspend fun getAlbumsAsync(): Deferred<MutableList<Album>> {
        return GlobalScope.async(Dispatchers.IO){
            if(isOnline()){
                return@async getAlbumsFromServer()
            }
            if(!hasConnection){
                return@async mutableListOf<Album>();
            }
            var dbData = dbRepo!!.getAllAlbums();
//            var albums = mutableListOf<Album>()
//            for(album: Album in dbData)
//                albums.add(toModel(album))
            return@async dbData;
        }
    }



    private suspend fun getPhotosAsync(id: Int): Deferred<MutableList<Photo>> {
        return GlobalScope.async(Dispatchers.IO){
            if(isOnline()){
                return@async getPhotosServer(id)
            }
            if(!hasConnection){
                return@async mutableListOf<Photo>();
            }
            var dbData = dbRepo!!.getPicturesFromAlbum(id);
//            var albums = mutableListOf<Album>()
//            for(album: Album in dbData)
//                albums.add(toModel(album))
            return@async dbData;
        }
    }

    private suspend fun getAlbumsFromServer(): MutableList<Album>{
        val url = "${BASE_URL}/albums"
        val request = Request.Builder()
            .url(url)
//            .get()
            .build()
        println("---------------------------------------------------------")
        println(request.body)
        println(request.headers)
        println(request.url)
        val httpClient = OkHttpClient()
        val connectionSpecs = mutableListOf<ConnectionSpec>()
        connectionSpecs.add(ConnectionSpec.COMPATIBLE_TLS)
//        httpClient.set
        httpClient.newCall(request)
            .execute()
            .use {response ->
                if(!response.isSuccessful)
                    return mutableListOf()

                val jsonArrayList = JSONArray(response.body!!.string())
                var elements = mutableListOf<Album>()
                for(index in 0 until jsonArrayList.length()){
                    var jsonObject = jsonArrayList[index] as JSONObject
                    var album = jsonToAlbum(jsonObject)
//                    var id: Long = eventRepository!!.getEventLocalIDByServerId(event.serverIdentifier)
//                    event.evId = id
                    elements.add(album)
                }
                return elements
            }
    }


    private suspend fun getPhotosServer(id: Int): MutableList<Photo>{
        val url = "${BASE_URL}/photos/${id}"
        val request = Request.Builder()
            .url(url)
//            .get()
            .build()
        println("---------------------------------------------------------")
        println(request.body)
        println(request.headers)
        println(request.url)
        val httpClient = OkHttpClient()
        val connectionSpecs = mutableListOf<ConnectionSpec>()
        connectionSpecs.add(ConnectionSpec.COMPATIBLE_TLS)
//        httpClient.set
        httpClient.newCall(request)
            .execute()
            .use {response ->
                if(!response.isSuccessful)
                    return mutableListOf()

                val jsonArrayList = JSONArray(response.body!!.string())
                var elements = mutableListOf<Photo>()
                for(index in 0 until jsonArrayList.length()){
                    var jsonObject = jsonArrayList[index] as JSONObject
                    var photo = jsonToPhoto(jsonObject)
//                    var id: Long = eventRepository!!.getEventLocalIDByServerId(event.serverIdentifier)
//                    event.evId = id
                    elements.add(photo)
                }
                return elements
            }
    }



    suspend fun addAlbum(album: Album): Album? {
        val a = addAlbumAsync(album).await()
        println(a)
        return  a
    }

    suspend fun addPhoto(photo: Photo): Photo? {
        val a = addPhotoAsync(photo).await()
        println(a?.path_to_picture)
        return  a
    }

    private fun addAlbumAsync(album: Album): Deferred<Album?> {
        return GlobalScope.async(Dispatchers.IO){
//            album.evId = 0;
//            event.serverIdentifier = -1;
            if(isOnline()){
                val a = addAlbum2(album)
                println(a)
//                dbRepo!!.addAlbum(album);
                return@async a;
//                event.serverIdentifier = serverId
            }

            val ev = dbRepo!!.addAlbum(album);
//            return@async ev;
            return@async ev
        }
    }

    private fun addPhotoAsync(photo: Photo): Deferred<Photo?> {
        return GlobalScope.async(Dispatchers.IO){
            //            album.evId = 0;
//            event.serverIdentifier = -1;
            if(isOnline()){
                val a = addPhotoServer(photo)
                println(a)
                return@async a;
//                event.serverIdentifier = serverId
            }

            val ev = dbRepo!!.addPicture(photo);
//            return@async ev;
            return@async ev
        }
    }

    private fun addAlbumServer(album: Album) {
        val url = "${BASE_URL}/albums/post";
        val serializedEvent = Gson().toJson(album);
        println(serializedEvent)
        val requestBody: RequestBody = FormBody.Builder()
            .add("name", album.name)
            .add("description", album.description)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
        println(requestBody.toString())
        println(request.body)
        val client = OkHttpClient()
        client.newCall(request)
            .execute()
            .use { response ->
                if (response.isSuccessful) {
//                    val res = JSONObject(response.body!!.string());
//                    return res.getInt("album")
                }
//                return -1
            }
    }
    private fun albumToJson(album: Album): String {
        val json = org.json.simple.JSONObject()
        json["id"] = album.id
        json["name"] = album.name
        json["description"] = album.description

        return json.toJSONString()
    }

    private fun photoToJson(photo: Photo): String {
        val json = org.json.simple.JSONObject()
        json["id"] = photo.id
        json["albumId"] = photo.albumId
        json["comment"] = photo.comment
        json["path"] = photo.path_to_picture
        return json.toJSONString()
    }

    fun addAlbum2(album: Album) = runBlocking {
        println(album.name)
        println(album.description)
        println(albumToJson(album))
        if(!hasConnection) return@runBlocking null
        val response = withContext(Dispatchers.IO) {
            httpClient.post<HttpResponse>("${BASE_URL}/albums/post") {
                body = albumToJson(album)
            }
        }
        var gson = Gson()
//        println(response.content.readUTF8Line(10000))
        val obj = gson.fromJson<Album>(response.content.readUTF8Line(10000), Album::class.java)
//        val obj = parser.parse(InputStreamReader(response.content.toInputStream())) as org.json.JSONObject
        println(obj)
        return@runBlocking obj
    }

    fun addPhotoServer(photo: Photo) = runBlocking {
//        println(album.name)
//        println(album.description)
        println(photoToJson(photo))
        if(!hasConnection) return@runBlocking null
        val response = withContext(Dispatchers.IO) {
            httpClient.post<HttpResponse>("${BASE_URL}/photos") {
                body = photoToJson(photo)
            }
        }
        var gson = Gson()
//        println(response.content.readUTF8Line(10000))
        val obj = gson.fromJson<Photo>(response.content.readUTF8Line(10000), Photo::class.java)
//        val obj = parser.parse(InputStreamReader(response.content.toInputStream())) as org.json.JSONObject
//        println(obj.id)
//        println(obj.path_to_picture)
//        photo.id = obj.id
        return@runBlocking obj
    }


     suspend fun deleteAlbum(albumId: Int)
     {
         if(!isOnline())
         {
             Toast
                 .makeText(this.context,
                     "Device is offline. You can't really delete anything",
                     Toast.LENGTH_LONG)
                 .show()
         }
         else
         {
             deleteAlbumServer(albumId)
             dbRepo?.deleteAlbum(Album(albumId, "","", isOnlyLocal = albumId))
         }
     }



    fun deleteAlbumServer(albumId: Int)
    {
        val url = "${BASE_URL}/albums/delete/${albumId}"
        val request = Request.Builder().url(url).get().build()
        val httpClient = OkHttpClient()
        httpClient.newCall(request).execute().use { response ->

            println(response.body)
        }


    }


    suspend fun deletePhoto(photoId: Int)
    {
        if(!isOnline())
        {
            Toast
                .makeText(this.context,
                    "Device is offline. You can't really delete anything",
                    Toast.LENGTH_LONG)
                .show()
        }
        else
        {
            deletePhotoServer(photoId)
            dbRepo?.deletePicture(photoId)
        }
    }

    fun deletePhotoServer(photoId: Int)
    {
        val url = "${BASE_URL}/photos/delete/${photoId}"
        val request = Request.Builder().url(url).get().build()
        val httpClient = OkHttpClient()
        httpClient.newCall(request).execute()


    }


    suspend fun updateAlbum(album: Album)
    {
        if(!isOnline())
        {
            Toast
                .makeText(this.context,
                    "Device is offline. You can't really delete anything",
                    Toast.LENGTH_LONG)
                .show()
        }
        else{
            updateAlbumServer(album)
            dbRepo!!.updateAlbum(album)
        }
    }


    suspend fun updatePhoto(photo: Photo)
    {
        if(!isOnline())
        {
            Toast
                .makeText(this.context,
                    "Device is offline. You can't really delete anything",
                    Toast.LENGTH_LONG)
                .show()
        }
        else{
            updatePhotoServer(photo)
            dbRepo!!.updatePicture(photo)
        }
    }



    fun updateAlbumServer(album: Album) = runBlocking{
//        println(album.name)
//        println(album.description)
//        println(albumToJson(album))
        if(!hasConnection) return@runBlocking null
        val response = withContext(Dispatchers.IO) {
            httpClient.post<HttpResponse>("${BASE_URL}/albums/update/") {
                body = albumToJson(album)
            }
        }
        var gson = Gson()
//        println(response.content.readUTF8Line(10000))
        val obj = gson.fromJson<Album>(response.content.readUTF8Line(10000), Album::class.java)
//        val obj = parser.parse(InputStreamReader(response.content.toInputStream())) as org.json.JSONObject
        println(obj)
        return@runBlocking obj
    }


    fun updatePhotoServer(photo: Photo) = runBlocking{
        //        println(album.name)
//        println(album.description)
//        println(albumToJson(album))
        if(!hasConnection) return@runBlocking null
        val response = withContext(Dispatchers.IO) {
            httpClient.post<HttpResponse>("${BASE_URL}/photos/update/") {
                body = photoToJson(photo)
            }
        }
        var gson = Gson()
//        println(response.content.readUTF8Line(10000))
        val obj = gson.fromJson<Photo>(response.content.readUTF8Line(10000), Photo::class.java)
//        val obj = parser.parse(InputStreamReader(response.content.toInputStream())) as org.json.JSONObject
        println(obj)
        return@runBlocking obj
    }


}