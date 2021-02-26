package ro.ubb.ktm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.album_detail.*
import kotlinx.android.synthetic.main.album_options.*
import kotlinx.android.synthetic.main.create_album_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.service.EntitiesService
import ro.ubb.ktm.service.EntitiesService.Companion.albums
import ro.ubb.ktm.service.Service
import ro.ubb.ktm.service.ServiceImpl
import ro.ubb.ktm.service.ServiceImplDBVersion


class AlbumOptionsActivity :  AppCompatActivity(){
    private lateinit var album : Album
    private  var service = EntitiesService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_options)
        this.album = intent.getSerializableExtra("album") as Album
        service.dbInit(this)
        delete_album_button.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    GlobalScope.launch (Dispatchers.IO){
                        service.deleteAlbum(album.id)
                        albums.removeIf{a -> a.id == album.id}
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

        }

        edit_description_button.setOnClickListener{

            val intent = Intent(this, EditAlbumDescActivity::class.java)
            intent.putExtra("album",album)

            startActivityForResult(intent, 6);

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK && requestCode == 6)
        {
            println("AM AJUNS LA ON ACTIV RESULT IN ALBUM OPTIONS")
            val data1 = data?.getSerializableExtra("album") as Album
            intent.putExtra("album", data1)
//            album_description_tview.setText(data1.description)
            finish()
        }
    }
}