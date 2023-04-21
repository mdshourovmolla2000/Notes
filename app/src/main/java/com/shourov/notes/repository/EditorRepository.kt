package com.shourov.notes.repository

import com.shourov.notes.database.AppDao
import com.shourov.notes.database.tables.NoteTable

class EditorRepository(private val dao: AppDao) {

    suspend fun insertNote(note: NoteTable) = dao.insertNote(note)

    suspend fun updateNote(note: NoteTable) = dao.updateNote(note)
}