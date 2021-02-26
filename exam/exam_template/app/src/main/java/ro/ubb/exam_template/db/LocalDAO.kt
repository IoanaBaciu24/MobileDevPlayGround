package ro.ubb.exam_template.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubb.exam_template.domain.LocalEntity

@Dao
interface LocalDAO
{
    @get: Query("select * from localEntities")
    val entities: LiveData<List<LocalEntity>>

    @get: Query("select count(*) from localEntities")
    val numberOfEntities: Int

    @Insert
    fun addEntity(entite: LocalEntity): Long

    @Insert
    fun addLocalEntities(entites: List<LocalEntity>): List<Long>

    @Delete
    fun deleteEntity(entite: LocalEntity)

    @Query("delete from localEntities where id = :id")
    fun deleteById(id: Int)

    @Query("delete from localEntities")
    fun deleteAll()

    @Update
    fun updateEntity(v: LocalEntity)
}