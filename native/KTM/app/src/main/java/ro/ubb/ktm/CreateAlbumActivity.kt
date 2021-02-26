package ro.ubb.ktm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_album_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.service.*

class CreateAlbumActivity : AppCompatActivity(){

//    private val service = ServiceImplDBVersion(this);
    private val service = EntitiesService()
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_album_page)
        println("AM AJUNS YAS")
        service.dbInit(this)
        submit_button_create.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                var al = addToService()
                println("ALBUM AICI NI")
                println(al)
                val intent = Intent()
                intent.putExtra("album", al)
                setResult(Activity.RESULT_OK, intent)
                finish();
            }
        }
    }


    suspend fun addToService(): Album {




            var nameEditText = findViewById(R.id.name_edit_text) as EditText
            var descrEditText = findViewById(R.id.description_edit_text) as EditText

            var album = Album(-1, nameEditText.text.toString(), descrEditText.text.toString())
            val addAlbum = service.addAlbum(album);
            album.id = addAlbum?.id?.or(0) ?: 0
            println(nameEditText.text)
            println(descrEditText.text)
            println(album.id)
            return album



    }
}