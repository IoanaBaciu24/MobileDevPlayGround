package ro.ubb.ktm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.edit_album_description_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.service.EntitiesService
import ro.ubb.ktm.service.Service
import ro.ubb.ktm.service.ServiceImpl
import ro.ubb.ktm.service.ServiceImplDBVersion

class EditAlbumDescActivity : AppCompatActivity(){

    private lateinit var album : Album
    private var service = EntitiesService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.edit_album_description_page)
        this.album = intent.getSerializableExtra("album") as Album
        service.dbInit(this)
        album_description_box.setText(album.description)

        edit_album_description_button.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to edit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
//                    service.deleteAlbum(album)
                    println(album_description_box.text)
                    GlobalScope.launch(Dispatchers.IO) {
                        album.description = album_description_box.text.toString()
                        println(album.description)
//                    service.replAlbum(album)
                        service.updateAlbum(album)
                        val intent = Intent()
                        intent.putExtra("album", album);
                        setResult(Activity.RESULT_OK, intent)
                        finish();

                    }

                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }
}