package com.shourov.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shourov.notes.database.tables.NoteTable

@Database(entities = [NoteTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao() : AppDao

    companion object{
        @Volatile
        private var databaseInstance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (databaseInstance == null) {
                synchronized(this){
                    databaseInstance = Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()
                }
            }
            return databaseInstance!!
        }
    }
}