package ro.ubb.ktm.service

import ro.ubb.ktm.model.Album
import ro.ubb.ktm.repo.InMemoryRepository

class ServiceImpl: Service {


    override fun getAlbumByName(name: String): Album {
        return InMemoryRepository.getAlbumByName(name)
    }

    override fun replAlbum(album: Album) {
        InMemoryRepository.replAlbum(album)
    }

    override fun deleteAlbum(album: Album) {
        InMemoryRepository.deleteAlbum(album)
    }

    override fun addAlbum(album: Album) {
        InMemoryRepository.addAlbum(album)
        println(InMemoryRepository.getAllAlbums())
    }

    override fun getAllAlbums(): MutableList<Album> {
        return InMemoryRepository.getAllAlbums();
    }

//    val repository = InMemoryRepository();


}