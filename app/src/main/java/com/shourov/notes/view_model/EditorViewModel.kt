package com.shourov.notes.view_model

import androidx.lifecycle.ViewModel
import com.shourov.notes.database.tables.NoteTable
import com.shourov.notes.repository.EditorRepository

class EditorViewModel(private val repository: EditorRepository) : ViewModel() {

    suspend fun insertNote(note: NoteTable) = repository.insertNote(note)

    suspend fun updateNote(note: NoteTable) = repository.updateNote(note)
}