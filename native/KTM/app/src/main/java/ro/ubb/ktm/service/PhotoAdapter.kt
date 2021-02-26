package ro.ubb.ktm.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.ubb.ktm.AlbumDetailActivity
import ro.ubb.ktm.PhotoDetailActivity
import ro.ubb.ktm.R
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo

class PhotoAdapter internal constructor(private val onEventListener: OnEventListener, public val photos: MutableList<Photo>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>()
{

    interface OnEventListener{
        fun  onEventClickListener(position: Int);
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.picture_item, parent, false)
//        return ViewHolder(view)
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.picture_item, parent, false)
        return ViewHolder(view, onEventListener)    }


    override fun getItemCount(): Int {
//        return service.getPhotosOfAlbum(album.id).size
        return photos.size;
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = service.getPhotosOfAlbum(album.id)[position]
//        var bitMap = BitmapFactory.decodeFile(item.path_to_picture)
//        holder.photoItem.setImageBitmap(bitMap)
//
//        holder.photoItem.setOnClickListener{
//            val intent = Intent(this.context, PhotoDetailActivity::class.java)
//            intent.putExtra("album",album)
//            intent.putExtra("photo", item)
//            context?.startActivity(intent)
//        }
//    }

//    private var album: Album
//    private var inflater: LayoutInflater
//    private var itemClickListener: PhotoAdapter.ItemClickListener? = null
//    private val context: Context?
//    private val service: ServiceImplDBVersion

//    init {
//
//        this.album = album
//        inflater = LayoutInflater.from(context);
//        this.context = context;
//        this.service = service
//    }
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
//    fun setClickListener(itemClickListener: PhotoAdapter.ItemClickListener?) {
//        this.itemClickListener = itemClickListener
//    }
//    inner class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(itemView),
//        View.OnClickListener{
//        var photoItem: ImageView = itemView.findViewById(R.id.photo_item)
//
//        override fun onClick(v: View?) {
//            if (itemClickListener != null)
//            {
//                itemClickListener!!.onItemClick(v, adapterPosition)}
//
//        }
//
//
//    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = photos[position]
//        holder.albumName.text = item?.name ?: println(item.name).toString()
//        holder.coverPhoto.setImageResource(R.drawable.ic_photo_black_24dp)
        var bitMap = BitmapFactory.decodeFile(item.path_to_picture)
        holder.photoItem.setImageBitmap(bitMap)


    }

    inner class ViewHolder (itemView: View, onEventListener: OnEventListener): RecyclerView.ViewHolder(itemView) {
//        var albumName: TextView = itemView.findViewById(R.id.album_name)
//        var coverPhoto: ImageView = itemView.findViewById(R.id.cover_photo)
        var photoItem: ImageView = itemView.findViewById(R.id.photo_item)
        val onEventListener: OnEventListener = onEventListener

        init {
            itemView.setOnClickListener{
                onEventListener.onEventClickListener(adapterPosition)
            }
        }


    }
}