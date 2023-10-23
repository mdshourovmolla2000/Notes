package com.shourov.notes.repository

import com.shourov.notes.database.AppDao
import com.shourov.notes.database.tables.NoteTable

class SearchRepository(private val dao: AppDao) {
    suspend fun searchNote(title: String): List<NoteTable> = dao.searchNote(title)

    suspend fun deleteNote(note: NoteTable) = dao.deleteNote(note)
}