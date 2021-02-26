package ro.ubb.ktm.model

import java.io.Serializable

class Album(

    var id: Int,
    var name: String,
    var description: String,
    var isOnlyLocal: Int = -1

): Serializable{

}

//
//class Album(var name: String, var description: String): Serializable{
//
//    companion object Factory{
//        var photos = mutableListOf<Photo>()
//
//        fun addPic(photo: Photo){
//            photos.add(photo)
//        }
//    }
//}
