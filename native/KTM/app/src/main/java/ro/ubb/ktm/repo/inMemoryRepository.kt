package ro.ubb.ktm.repo

import ro.ubb.ktm.R
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo







//class InMemoryRepository() : Repository{
//
//    private var instance: InMemoryRepository? = null
//
//    override fun addAlbum(album: Album) {
//        albums.add(album)
//    }
//
//    fun getInstance(): InMemoryRepository? {
//        if (instance == null) {
//            synchronized(InMemoryRepository::class.java) {
//                if (instance == null) {
//                    instance = ro.ubb.ktm.repo.InMemoryRepository()
//                }
//            }
//        }
//        return instance
//    }
//    private fun InMemoryRepository() {
//
//        createDummyData()
//
//    }
//    private val albums = mutableListOf<Album>();
////
////    init {
////        createDummyData()
////    }
//    override fun getAllAlbums(): MutableList<Album> {
//
//        return albums;
//    }
//
//
//    private fun createDummyData()
//    {
//        var p1 = Photo("s1", "c1", R.drawable.img1);
//        var p2 = Photo("s2", "c2", R.drawable.img2);
//        var p3 = Photo("s3", "c3", R.drawable.img3);
//        var p4 = Photo("s4", "c4", R.drawable.img4);
//
//        var l1 = mutableListOf<Photo>();
//        l1.add(p1);
//        l1.add(p2);
//
//        var l2 = mutableListOf<Photo>();
//        l2.add(p3);
//        l2.add(p4);
//
//        var a1 = Album("ALO PRONTO 1", "d1", l1);
//        var a2 = Album("ALO PRONTO 2", "d2", l2);
//
//        albums.add(a1);
//        albums.add(a2);
//    }
//}


object InMemoryRepository: Repository{

    private val albums = mutableListOf<Album>();

    init {
//        createDummyData()
    }
    override fun getAllAlbums(): MutableList<Album> {
        return albums;
    }

    override fun addAlbum(album: Album) {
        albums.add(album)
    }


    fun deleteAlbum(album: Album) {

        var idx = -1
        for (i in 0..albums.size)
        {
            if(albums[i].name == album.name && albums[i].description == album.description)
            {
                idx = i
                break
            }
        }
        if(idx!=-1)
        {
            albums.removeAt(idx)
        }
    }

    fun replAlbum(album: Album)
    {
        for (i in 0..albums.size)
        {
            if (albums[i].name == album.name)
            {
                albums[i] = album
                break
            }
        }
    }

    fun getAlbumByName(name: String): Album
    {
        for (i in 0..albums.size)
        {
            if (albums[i].name == name)
            {
                return albums[i]
            }
        }

        return Album(0,"","")
    }
}