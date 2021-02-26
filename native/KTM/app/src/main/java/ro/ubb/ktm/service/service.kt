package ro.ubb.ktm.service

import ro.ubb.ktm.model.Album

interface Service {

    fun getAllAlbums(): MutableList<Album>;
    fun addAlbum(album: Album);
    fun deleteAlbum(album: Album);
    fun replAlbum(album: Album)
    fun getAlbumByName(name: String): Album
}