package ro.ubb.exam_template.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entities")
data class Entite(@field:PrimaryKey(autoGenerate = true)var id: Int,  var name: String?, var group: String?, var details: String?, var status: String, var students: Int, var type: String)



@Entity(tableName = "localEntities")
data class LocalEntity(@field:PrimaryKey(autoGenerate = true)var id: Int,  var name: String?, var group: String?, var details: String?, var status: String, var students: Int, var type: String)
