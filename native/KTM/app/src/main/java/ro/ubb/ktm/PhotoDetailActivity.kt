package ro.ubb.ktm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.album_detail.*
import kotlinx.android.synthetic.main.edit_comment_page.*
import kotlinx.android.synthetic.main.photo_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.service.EntitiesService
import ro.ubb.ktm.service.ServiceImplDBVersion

class PhotoDetailActivity : AppCompatActivity(){

//    private lateinit var album : Album
    private lateinit var photo: Photo
    private  var service = EntitiesService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.photo_detail)
        service.dbInit(this)
//        this.album = intent.getSerializableExtra("album") as Album
        this.photo = intent.getSerializableExtra("photo") as Photo
        var bitmap= BitmapFactory.decodeFile(photo.path_to_picture)

        picture_detail.setImageBitmap(bitmap)
        picture_comment.setText(photo.comment)
//        picture_detail_title.setText(photo.name)
        this.picture_detail_options_button.setOnClickListener{

            if(service.isOnline()){

//            val intent = Intent(this, PhotoOptionsActivity::class.java)
////            intent.putExtra("album",album)
//            intent.putExtra("photo", photo)
//            startActivityForResult(intent, 7)


                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                        GlobalScope.launch (Dispatchers.IO){
                            service.deletePhoto(photo.id)
                            EntitiesService.photos.removeIf{ a -> a.id == photo.id}

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
            else
            {
                Toast
                    .makeText(this,
                        "Device is offline. The feature that you want to access are unavailable",
                        Toast.LENGTH_LONG)
                    .show()
            }
        }


        edit_comment_button_new.setOnClickListener()
        {
            if(service.isOnline())
            {
                val intent = Intent(this, EditCommentActivity::class.java)
//            intent.putExtra("album",album)
                intent.putExtra("photo", photo)
                startActivityForResult(intent, 8)
            }
            else{
                Toast
                    .makeText(this,
                        "Device is offline. The feature that you want to access are unavailable",
                        Toast.LENGTH_LONG)
                    .show()
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("INTRAT IN ON ACTIV REZ")
        println(requestCode)
        println(resultCode)
        println(Activity.RESULT_OK)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 8)
        {
            println("REQ CODE 7 DBG");
//            val intent = Intent()
            val photo1 = data?.getSerializableExtra("photo") as Photo
            setResult(Activity.RESULT_OK, intent)
            picture_comment.setText(photo1.comment)
//            finish();
        }
//        var service = ServiceImplDBVersion(this)
//        photo = service.getPictureById(photo.id, album.id)
//
//        picture_comment.setText(photo.comment)
    }

}