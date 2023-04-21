package com.shourov.notes.repository

import androidx.lifecycle.LiveData
import com.shourov.notes.database.AppDao
import com.shourov.notes.database.tables.NoteTable

class HomeRepository(private val dao: AppDao) {
    fun getNotes(): LiveData<List<NoteTable>> = dao.getNotes()

    suspend fun deleteNote(note: NoteTable) = dao.deleteNote(note)
}