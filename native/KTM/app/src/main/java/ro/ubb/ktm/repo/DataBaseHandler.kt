package ro.ubb.ktm.repo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ro.ubb.ktm.model.Album
import ro.ubb.ktm.model.Photo
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.ALBUM_TABLE_NAME
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.COL_ALBUM_DESCRIPTION
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.COL_ALBUM_ID
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.COL_ALBUM_NAME
import ro.ubb.ktm.repo.AlbumContract.AbumEntry.COL_IS_LOCAL
import ro.ubb.ktm.repo.AlbumContract.DATABASE_NAME
import ro.ubb.ktm.repo.AlbumContract.DATABASE_VERSION
import ro.ubb.ktm.repo.AlbumContract.PhotoEntry.COL_PICTURE_COMMENT
import ro.ubb.ktm.repo.AlbumContract.PhotoEntry.COL_PICTURE_ID
import ro.ubb.ktm.repo.AlbumContract.PhotoEntry.COL_PICTURE_PATH
import ro.ubb.ktm.repo.AlbumContract.PhotoEntry.PHOTO_TABLE_NAME


class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION)
{
    override fun onCreate(db: SQLiteDatabase?) {
        val create_albums = "CREATE TABLE "+ ALBUM_TABLE_NAME + " (" + COL_ALBUM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ALBUM_NAME + " VARCHAR(50), " +
                COL_IS_LOCAL + " INTEGER, " +
        COL_ALBUM_DESCRIPTION + " VARCHAR(500))"

        var create_photo = "CREATE TABLE " + PHOTO_TABLE_NAME + " (" +
                COL_PICTURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_PICTURE_COMMENT + " VARCHAR(500), " +
                COL_PICTURE_PATH + " VARCHAR(500), " +
                COL_ALBUM_ID + " INTEGER, "+
                COL_IS_LOCAL + " INTEGER, " +
                "FOREIGN KEY(" + COL_ALBUM_ID + ") REFERENCES " + ALBUM_TABLE_NAME + "(" + COL_ALBUM_ID + ") ON DELETE CASCADE)";

        println(create_albums)
        println(create_photo)
        db?.execSQL(create_albums);
        db?.execSQL(create_photo);

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("drop table ${PHOTO_TABLE_NAME}")
        db?.execSQL("drop table ${ALBUM_TABLE_NAME}")
        onCreate(db)
    }

    fun insertAlbum(album: Album): Album
    {
        val db = this.writableDatabase
        var cv = ContentValues();

        cv.put(COL_ALBUM_NAME, album.name);
        cv.put(COL_ALBUM_DESCRIPTION, album.description)
        cv.put(COL_IS_LOCAL, album.isOnlyLocal)

        val result = db.insert(ALBUM_TABLE_NAME, null, cv);

        println(result);
        return getLastInsertedAlbum()
    }


    fun getLastInsertedAlbum(): Album
    {
        val db = this.writableDatabase
        val result = db.rawQuery("select * from ${ALBUM_TABLE_NAME} where ${COL_ALBUM_ID} = (select max(${COL_ALBUM_ID}) from ${ALBUM_TABLE_NAME})", null)
        var album = Album(-1, "", "")
        if (result.moveToFirst()) {
                album.id = result.getInt(result.getColumnIndex(COL_ALBUM_ID))
                album.name = result.getString(result.getColumnIndex(COL_ALBUM_NAME))
                album.description = result.getString(result.getColumnIndex(COL_ALBUM_DESCRIPTION))

        }
        return album
    }

    fun getLastInsertedPhoto(): Photo
    {
        val db = this.writableDatabase
        val result = db.rawQuery("select * from ${PHOTO_TABLE_NAME} where ${COL_PICTURE_ID} = (select max(${COL_PICTURE_ID}) from ${PHOTO_TABLE_NAME})", null)
        var photo = Photo(-1, -1, "", "")
        if (result.moveToFirst()) {
            photo.id = result.getInt(result.getColumnIndex(COL_PICTURE_ID))
            photo.albumId = result.getInt(result.getColumnIndex(COL_ALBUM_ID))
            photo.comment = result.getString(result.getColumnIndex(COL_PICTURE_COMMENT))
            photo.path_to_picture = result.getString(result.getColumnIndex(COL_PICTURE_PATH))

        }
        return photo
    }

    fun insertPictureToAlbum(pic: Photo) : Photo
    {
        val db = this.writableDatabase
        val cv = ContentValues();
        cv.put(COL_PICTURE_COMMENT, pic.comment);
        cv.put(COL_PICTURE_PATH, pic.path_to_picture)
        cv.put(COL_ALBUM_ID, pic.albumId)
        cv.put(COL_IS_LOCAL, pic.isOnlyLocal)

        val result = db.insert(PHOTO_TABLE_NAME, null, cv);
        println(result);
        return getLastInsertedPhoto()
    }

    fun queryAllAlbums(): Cursor
    {
        val db = this.writableDatabase
        return db.rawQuery("select * from ${ALBUM_TABLE_NAME}", null)
    }

    fun queryOnlyLocals(): Cursor
    {
        val db = this.writableDatabase
        return db.rawQuery("select * from ${ALBUM_TABLE_NAME} where ${COL_IS_LOCAL} = -1", null)
    }

    fun queryOnlyLocalPics(): Cursor
    {
        val db = this.writableDatabase
        return db.rawQuery("select * from ${PHOTO_TABLE_NAME} where ${COL_IS_LOCAL} = -1", null)
    }


    fun queryPhotosOfAlbum(albumId: Int): Cursor
    {
        val db = this.writableDatabase
        return db.rawQuery("select * from ${PHOTO_TABLE_NAME} where " + COL_ALBUM_ID + "= " + albumId.toString(), null )

    }


    fun deleteAlbum(albumId: Int)
    {
        val db = this.writableDatabase
        var a = arrayOf(albumId.toString())

        val delete = db.delete(ALBUM_TABLE_NAME, COL_ALBUM_ID + "=?", a)
        println(delete)
        println("DELETED ALBUM")
    }

    fun deleteAlbumLocal(albumId: Int)
    {
        val db = this.writableDatabase
        var a = arrayOf(albumId.toString())

        val delete = db.delete(ALBUM_TABLE_NAME, COL_IS_LOCAL + "=?", a)
        println(delete)
        println("DELETED ALBUM")
    }

    fun updateAlbum(album: Album) {

        val db = this.writableDatabase
        var a = arrayOf(album.id.toString())
        val values = ContentValues()
        values.put(COL_ALBUM_DESCRIPTION, album.description)
//        values.put(COL_ALBUM_NAME, album.name)
        values.put(COL_IS_LOCAL, album.isOnlyLocal)
        val update = db.update(ALBUM_TABLE_NAME, values, COL_ALBUM_ID + "=?", a)
        println(update)
    }
    fun updateLocalsAsNonLocals()
    {
        val values = ContentValues()
        values.put(COL_IS_LOCAL, 0)
        val db = this.writableDatabase
        val update = db.update(ALBUM_TABLE_NAME, values, COL_IS_LOCAL + "=?", arrayOf(1.toString()))
        val update2 = db.update(PHOTO_TABLE_NAME, values, "$COL_IS_LOCAL=?", arrayOf(1.toString()))

        println(update)
    }
    fun deletePicture(picId: Int)
    {
        val db = this.writableDatabase
        var a = arrayOf(picId.toString())

        val delete = db.delete(PHOTO_TABLE_NAME, COL_PICTURE_ID + "=?", a)
        println(delete)
    }

    fun deletePictureLocal(picId: Int)
    {
        val db = this.writableDatabase
        var a = arrayOf(picId.toString())

        val delete = db.delete(PHOTO_TABLE_NAME, COL_IS_LOCAL + "=?", a)
        println(delete)
    }

    fun updatePicture(pic: Photo)
    {
        val db = this.writableDatabase
        var a = arrayOf(pic.id.toString())
        val values = ContentValues()
        values.put(COL_PICTURE_PATH, pic.path_to_picture)
        values.put(COL_PICTURE_COMMENT, pic.comment)
        values.put(COL_ALBUM_ID, pic.albumId)
        values.put(COL_IS_LOCAL, pic.isOnlyLocal)
        val update = db.update(PHOTO_TABLE_NAME, values, COL_PICTURE_ID + "=?", a)
        println(update)

    }
}




