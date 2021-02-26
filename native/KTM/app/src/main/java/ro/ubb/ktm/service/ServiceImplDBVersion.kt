package ro.ubb.ktm.service

import android.content.Context
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.repo.DatabaseRepository

class ServiceImplDBVersion internal constructor(context: Context){

    private val databaseRepository = DatabaseRepository(context);

    fun getAllAlbums(): MutableList<Album>
    {
        return databaseRepository.getAllAlbums();
    }

    fun getPhotosOfAlbum(albumId: Int): MutableList<Photo> {
        return databaseRepository.getPicturesFromAlbum(albumId);
    }
    fun addAlbum(album: Album)
    {
        databaseRepository.addAlbum(album)
    }

    fun deleteAlbum(album: Album) {

        databaseRepository.deleteAlbum(album)
    }

    fun editAlbum(album: Album)
    {
        databaseRepository.updateAlbum(album)
    }


    fun getPicsFromAlbum(albumId: Int): MutableList<Photo>
    {
        return databaseRepository.getPicturesFromAlbum(albumId)
    }

    fun addPhoto(photo: Photo)
    {
        databaseRepository.addPicture(photo)
    }

    fun deletePhoto(photo: Photo)
    {
        databaseRepository.deletePicture(photo.id)
    }

    fun editPhoto(photo: Photo)
    {
        databaseRepository.updatePicture(photo)
    }


    fun getPictureById(photoId: Int, albumId: Int): Photo
    {
        var l = databaseRepository.getPicturesFromAlbum(albumId)
        var i = 0
        for (photo in l)
        {
            if(photo.id == photoId)
                return photo
        }

        return Photo(-1,-1,"", "")
    }
}