package ro.ubb.ktm.model

import java.io.Serializable

class Photo(
    var id: Int,
    var albumId: Int,
    var comment: String,
    var path_to_picture: String,
    var isOnlyLocal: Int = -1,
    var albumOnlyLocal: Boolean = false
             ): Serializable{}