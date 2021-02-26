package ro.ubb.ktm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.album_detail.*
import kotlinx.android.synthetic.main.album_options.*
import kotlinx.android.synthetic.main.photo_options.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.service.EntitiesService
import ro.ubb.ktm.service.EntitiesService.Companion.photos
import ro.ubb.ktm.service.Service
import ro.ubb.ktm.service.ServiceImpl
import ro.ubb.ktm.service.ServiceImplDBVersion

class PhotoOptionsActivity : AppCompatActivity()
{
//    private lateinit var album : Album
    private lateinit var photo: Photo
    private  var service = EntitiesService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.photo_options)
//        this.album = intent.getSerializableExtra("album") as Album
        this.photo = intent.getSerializableExtra("photo") as Photo
        service.dbInit(this)
        delete_photo_button.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    GlobalScope.launch (Dispatchers.IO){
                        service.deletePhoto(photo.id)
                        photos.removeIf{a -> a.id == photo.id}

                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

        }

        edit_comment_button.setOnClickListener{

            val intent = Intent(this, EditCommentActivity::class.java)
//            intent.putExtra("album",album)
            intent.putExtra("photo", photo)
            startActivityForResult(intent, 8)

        }
    }



}