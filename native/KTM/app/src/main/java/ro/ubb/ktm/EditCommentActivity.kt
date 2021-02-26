package ro.ubb.ktm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.edit_album_description_page.*
import kotlinx.android.synthetic.main.edit_comment_page.*
import kotlinx.android.synthetic.main.photo_options.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.service.EntitiesService
import ro.ubb.ktm.service.Service
import ro.ubb.ktm.service.ServiceImpl
import ro.ubb.ktm.service.ServiceImplDBVersion

class EditCommentActivity : AppCompatActivity(){

//    private lateinit var album : Album
    private lateinit var photo: Photo
    private var service = EntitiesService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.edit_comment_page)
//        this.album = intent.getSerializableExtra("album") as Album
        this.photo = intent.getSerializableExtra("photo") as Photo
        photo_comment_box.setText(photo.comment)
        service.dbInit(this)
        edit_photo_comment_button.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to edit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    GlobalScope.launch(Dispatchers.IO) {
                        photo.comment = photo_comment_box.text.toString()
                        service.updatePhoto(photo)
                        val intent = Intent()
                        intent.putExtra("photo", photo);
//                        intent.putExtra("album", album)
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