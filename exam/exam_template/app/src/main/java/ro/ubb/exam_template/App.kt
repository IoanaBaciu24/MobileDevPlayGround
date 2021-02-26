package ro.ubb.exam_template

import android.app.Application
import androidx.room.Room
import ro.ubb.exam_template.db.DB

class App : Application() {
    lateinit var db: DB
    //    var db = Room.databaseBuilder(applicationContext, BookDB::class.java, "database-name").build()
    override fun onCreate() {
        super.onCreate()
        println("APLICATION CONTEXT" + applicationContext)
        db = Room.databaseBuilder(applicationContext,
            DB::class.java, "database-name").build()
    }
}