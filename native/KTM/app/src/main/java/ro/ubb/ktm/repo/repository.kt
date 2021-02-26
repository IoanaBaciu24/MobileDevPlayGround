package ro.ubb.ktm.repo

import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo

interface Repository{

    fun getAllAlbums(): MutableList<Album>;
    fun addAlbum(album: Album);
}