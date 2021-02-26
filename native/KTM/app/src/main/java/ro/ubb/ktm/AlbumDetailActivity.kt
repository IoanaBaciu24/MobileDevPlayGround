package ro.ubb.ktm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.album_detail.*
import ro.ubb.ktm.model.Album
import java.util.jar.Manifest
import kotlin.reflect.typeOf
import android.provider.MediaStore
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.service.*


class AlbumDetailActivity: AppCompatActivity(), PhotoAdapter.OnEventListener{
   private lateinit var album : Album
    private  var service = EntitiesService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.album = intent.getSerializableExtra("album") as Album
        service.dbInit(this)
        setContentView(R.layout.album_detail)

        album_title_labebe.text = (album as Album).name
        album_description_tview.setText(album.description)

        GlobalScope.launch (Dispatchers.Main){

            val photos = service.getPicsOfAlbum(album.id)
            val viewAdapter =
                PhotoAdapter(
                    this@AlbumDetailActivity,
                    photos
                )
            picture_list.adapter = viewAdapter
            add_picture_button.setOnClickListener {

                pickImageFromGallery()
            }


        }




        album_options_button.setOnClickListener{

            if(service.isOnline()) {

//                val intent = Intent(this@AlbumDetailActivity, AlbumOptionsActivity::class.java)
//                intent.putExtra("album", album)
//
//                startActivityForResult(intent, 5);
//                finish()

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                        GlobalScope.launch (Dispatchers.IO){
                            service.deleteAlbum(album.id)
                            EntitiesService.albums.removeIf{ a -> a.id == album.id}
                            println("AICI 1")
                            val intent = Intent()
                            intent.putExtra("albumId", album.id)
                            setResult(Activity.RESULT_OK, intent)
                            println("AICI 2")
                            finish();
                        }
//                    startActivity(Intent(this, MainActivity::class.java))

                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()



//            service.deleteAlbum(album)
            }
            else
            {
                Toast
                    .makeText(this@AlbumDetailActivity,
                        "Device is offline. The feature that you want to access is unavailable",
                        Toast.LENGTH_LONG)
                    .show()
            }
        }


        edit_description_button_new.setOnClickListener()
        {
            if (service.isOnline())
            {
                val intent = Intent(this, EditAlbumDescActivity::class.java)
                intent.putExtra("album",album)

                startActivityForResult(intent, 6);
            }
            else
            {
                Toast
                    .makeText(this@AlbumDetailActivity,
                        "Device is offline. The feature that you want to access is unavailable",
                        Toast.LENGTH_LONG)
                    .show()
            }
        }



    }


    override fun onEventClickListener(position: Int) {
        val int = Intent(this@AlbumDetailActivity, PhotoDetailActivity::class.java)
//        println(EntitiesService.albums)

        int.putExtra("photo", EntitiesService.photos[position])
        println("AM AJUNS AICI NO")
        println(EntitiesService.photos[position])
        startActivityForResult(int, 11)
    }
    private fun pickImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        println("ENTERERD ON ACTIVITY RESULT: " + requestCode)

        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            var ceva = data?.data

            if (ceva != null){
                var altceva = convertMediaUriToPath(ceva)
                var list = altceva.split("/")
                var name = list[list.size-1]
                println("HERE COMES A NAME")
                println(name)
                var albumId = album.isOnlyLocal
                var albumOnlyLocal= false
                if(albumId == -1)
                {
                    albumId = album.id
                    albumOnlyLocal = true
                }

                var photo = Photo(-1, albumId, "", altceva, albumOnlyLocal = albumOnlyLocal);
                //  todo add the picture to thedatabase
                GlobalScope.launch (Dispatchers.Main){

                    photo = service.addPhoto(photo)!!
                    EntitiesService.photos.add(0, photo);

                    picture_list.adapter?.notifyItemInserted(0)
                }

//                picture_list.adapter?.notifyDataSetChanged()

        }


    }
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            println("REQ CODE 5 DBG");
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish();
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 6)
        {
            println("ENTERED ON ACTIV RES CODE 6")
             album = data?.getSerializableExtra("album") as Album;

            album_description_tview.setText(album.description);

        }

        var a = data?.getSerializableExtra("album")
        if(a!=null)
        {
            var b = a as Album
            album_description_tview.setText(b.description)
        }

        if (resultCode == Activity.RESULT_OK )
        {
            picture_list?.adapter?.notifyDataSetChanged();



        }


    }

    override fun onResume() {
        super.onResume()
        picture_list.adapter?.notifyDataSetChanged()
    }

    fun convertMediaUriToPath(uri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        cursor.close()
        return path
    }

}