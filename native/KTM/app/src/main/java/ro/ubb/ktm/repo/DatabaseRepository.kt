package ro.ubb.ktm.repo

import android.content.Context
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.COL_ALBUM_DESCRIPTION
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.COL_ALBUM_ID
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.COL_ALBUM_NAME
import ro.ubb.ktm.repo.AlbumContract.PhotoEntry.COL_PICTURE_COMMENT
import ro.ubb.ktm.repo.AlbumContract.PhotoEntry.COL_PICTURE_ID
import ro.ubb.ktm.repo.AlbumContract.PhotoEntry.COL_PICTURE_PATH

class DatabaseRepository internal constructor(context: Context)
{
    val handler = DataBaseHandler(context);
     fun getAllAlbums(): MutableList<Album> {
         var list = mutableListOf<Album>()
         var cursor = handler.queryAllAlbums();
         if (cursor.moveToFirst()) {
             do {
                 val id = cursor.getInt(cursor.getColumnIndex(COL_ALBUM_ID))
                 val name = cursor.getString(cursor.getColumnIndex(COL_ALBUM_NAME))
                 val description = cursor.getString(cursor.getColumnIndex(COL_ALBUM_DESCRIPTION))
                 list.add(Album(id, name, description))
             } while (cursor.moveToNext())
         }

         return list;
    }

     fun addAlbum(album: Album): Album {
         val insertAlbum = handler.insertAlbum(album);
         val all = getAllAlbums()
         for(a in all)
         {
             println(a.id.toString() + " " + a.name)
         }
         return insertAlbum
    }

    fun getPicturesFromAlbum(albumId: Int): MutableList<Photo>
    {
        var list = mutableListOf<Photo>()
        var cursor = handler.queryPhotosOfAlbum(albumId);
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COL_PICTURE_ID))
                val path = cursor.getString(cursor.getColumnIndex(COL_PICTURE_PATH))
                val comment = cursor.getString(cursor.getColumnIndex(COL_PICTURE_COMMENT))
                val albumId = cursor.getInt(cursor.getColumnIndex(COL_ALBUM_ID))
                list.add(Photo(id, albumId,comment, path))
            } while (cursor.moveToNext())
        }

        return list;
    }


    fun getPicturesLocal(): MutableList<Photo>
    {
        var list = mutableListOf<Photo>()
        var cursor = handler.queryOnlyLocalPics();
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COL_PICTURE_ID))
                val path = cursor.getString(cursor.getColumnIndex(COL_PICTURE_PATH))
                val comment = cursor.getString(cursor.getColumnIndex(COL_PICTURE_COMMENT))
                val albumId = cursor.getInt(cursor.getColumnIndex(COL_ALBUM_ID))
                list.add(Photo(id, albumId,comment, path))
            } while (cursor.moveToNext())
        }

        return list;
    }



    fun setAllAlbumsAsNonLocal()
    {
        handler.updateLocalsAsNonLocals()
    }


    fun setAllPicsAsNonLocal()
    {
//        handler.()
    }


    fun getAllLocalAlbums(): MutableList<Album>
    {
        var list = mutableListOf<Album>()

        var cursor = handler.queryOnlyLocals();
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COL_ALBUM_ID))
                val name = cursor.getString(cursor.getColumnIndex(COL_ALBUM_NAME))
                val description = cursor.getString(cursor.getColumnIndex(COL_ALBUM_DESCRIPTION))
                list.add(Album(id, name, description))
            } while (cursor.moveToNext())
        }

        return list;
    }

    fun deleteAlbum(album: Album)
    {
        println(album.id)
        println(album.name)
        handler.deleteAlbumLocal(album.id)
    }

    fun updateAlbum(album: Album) {
        handler.updateAlbum(album)
    }

    fun addPicture(pic: Photo): Photo
    {
        return handler.insertPictureToAlbum(pic)
    }


    fun deletePicture(picId: Int)
    {
        handler.deleteAlbumLocal(picId)
    }

    fun updatePicture(pic: Photo)
    {
        handler.updatePicture(pic)
    }



}