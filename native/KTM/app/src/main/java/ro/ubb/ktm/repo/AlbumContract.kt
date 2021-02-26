package ro.ubb.ktm.repo

import android.provider.BaseColumns

object AlbumContract
{
    const val DATABASE_NAME = "KTM"
    const val DATABASE_VERSION = 6

    object AbumEntry: BaseColumns{
        const val ALBUM_TABLE_NAME = "album"
        const val COL_ALBUM_NAME = "albumName"
        const val COL_ALBUM_DESCRIPTION = "description"
        const val COL_ALBUM_ID = "aid"
        const val COL_IS_LOCAL = "isLocal"
    }

    object PhotoEntry: BaseColumns{
        const val PHOTO_TABLE_NAME = "photo"
        const val COL_PICTURE_ID = "pid"
        const val COL_PICTURE_COMMENT = "pictureComment"
        const val COL_PICTURE_PATH = "path"
        const val COL_IS_LOCAL = "isLocal"

    }
}