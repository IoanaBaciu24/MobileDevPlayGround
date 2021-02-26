package ro.ubb.ktm.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.drm.DrmManagerClient
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ro.ubb.ktm.AlbumDetailActivity
import ro.ubb.ktm.R
import ro.ubb.ktm.model.Album

class AlbumAdapter (private val onEventListener: OnEventListener, public val albums: MutableList<Album>): RecyclerView.Adapter<AlbumAdapter.ViewHolder>(){
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = albums[position]
        holder.albumName.text = item?.name ?: println(item.name).toString()
        holder.coverPhoto.setImageResource(R.drawable.ic_photo_black_24dp)


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_item, parent, false)
        return ViewHolder(view, onEventListener)    }

    override fun getItemCount(): Int  {

        return albums.size
    }





    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    inner class ViewHolder (itemView: View, onEventListener: OnEventListener): RecyclerView.ViewHolder(itemView) {
        var albumName: TextView = itemView.findViewById(R.id.album_name)
        var coverPhoto: ImageView = itemView.findViewById(R.id.cover_photo)
        val onEventListener: OnEventListener = onEventListener

        init {
            itemView.setOnClickListener{
                onEventListener.onEventClickListener(adapterPosition)
            }
        }


    }

    interface OnEventListener{
        fun  onEventClickListener(position: Int);
    }
}