package com.shourov.notes.application

import android.app.Application
import androidx.room.Room
import com.shourov.notes.database.AppDatabase

class BaseApplication: Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "com.shourov.notes"
        ).build()
    }
}