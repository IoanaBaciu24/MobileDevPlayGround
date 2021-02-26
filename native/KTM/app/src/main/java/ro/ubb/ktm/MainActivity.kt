package ro.ubb.ktm

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.service.*
import ro.ubb.ktm.service.EntitiesService.Companion.albums
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity(), AlbumAdapter.OnEventListener {

    private val service = EntitiesService();
    private var offline = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        service.dbInit(this)

        GlobalScope.launch(Dispatchers.Main) {
            val albums = service.getAlbums()
            val viewAdapter =
                AlbumAdapter(
                    this@MainActivity,
                    albums
                )
            album_list.adapter = viewAdapter
            val viewManager = LinearLayoutManager(this@MainActivity)
            create_album.setOnClickListener{

                startActivityForResult(Intent(this@MainActivity, CreateAlbumActivity::class.java), 15);

            }


        }

        // check if device is online

        val networkConnection = NetService(applicationContext);
        networkConnection.observe(this, Observer {isConnected ->
            if(isConnected){
                if(offline) {
                    val serv = EntitiesService()
                    serv.dbInit(this)
                    serv.performServerUpdates()
                    Toast
                        .makeText(
                            this,
                            "Device is back online",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }
            else{
                offline = true;
                Toast
                    .makeText(this,
                        "Device is offline. Some features will not be available",
                        Toast.LENGTH_LONG)
                    .show()

            }
        })




    }

    override fun onEventClickListener(position: Int) {
        val int = Intent(this@MainActivity, AlbumDetailActivity::class.java)
        println(albums)

        int.putExtra("album", albums[position])

        startActivityForResult(int, 5)
    }

//    override fun onResume() {
////        val viewAdapter =
////            AlbumAdapter(
////                this,
////                service
////            )
//        super.onResume()
////        album_list.adapter = viewAdapter
////        album_list.adapter?.notifyDataSetChanged();
//
//
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==5)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                println("REQ CODE 6");

                album_list.adapter?.notifyDataSetChanged();
            }
        }
        if(requestCode == 15)
        {
            if(resultCode== Activity.RESULT_OK)
            {

//                album_list.adapter?.notifyDataSetChanged()
                albums.add(0, data?.getSerializableExtra("album")as Album);

                album_list.adapter?.notifyItemInserted(0)
            }
        }

    }


}
