package ro.ubb.exam_template.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubb.exam_template.domain.Entite

@Dao
interface DAO
{
    @get: Query("select * from entities")
    val entities: LiveData<List<Entite>>

    @get: Query("select count(*) from entities")
    val numberOfEntities: Int

    @Insert
    fun addEntity(entite: Entite): Long

    @Insert
    fun addEntities(entites: List<Entite>): List<Long>

    @Delete
    fun deleteEntity(entite: Entite)

    @Query("delete from entities where id = :id")
    fun deleteById(id: Int)

    @Query("delete from entities")
    fun deleteAll()

    @Update
    fun updateEntity(v: Entite)
}