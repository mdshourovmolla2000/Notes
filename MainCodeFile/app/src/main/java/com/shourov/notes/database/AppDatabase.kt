package com.shourov.notes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shourov.notes.database.tables.NoteTable

@Database(entities = [NoteTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao() : AppDao
}