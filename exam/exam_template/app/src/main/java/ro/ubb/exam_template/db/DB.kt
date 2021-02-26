package ro.ubb.exam_template.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import ro.ubb.exam_template.domain.Entite
import ro.ubb.exam_template.domain.LocalEntity


@Database(entities = [Entite::class, LocalEntity::class], version = 1, exportSchema = false)
abstract class DB: RoomDatabase()
{
    abstract val entDAO: DAO
    abstract val localDAO: LocalDAO
}