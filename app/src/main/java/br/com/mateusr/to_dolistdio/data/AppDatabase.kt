package br.com.mateusr.to_dolistdio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.mateusr.to_dolistdio.data.daos.TaskDao
import br.com.mateusr.to_dolistdio.data.model.Task

@Database (entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDao

    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "taskApp_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}