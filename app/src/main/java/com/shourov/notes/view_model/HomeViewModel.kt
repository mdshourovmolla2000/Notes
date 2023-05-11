package com.shourov.notes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shourov.notes.database.tables.NoteTable
import com.shourov.notes.repository.HomeRepository

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    fun getNotes(): LiveData<List<NoteTable>> = repository.getNotes()

    suspend fun deleteNote(note: NoteTable) = repository.deleteNote(note)

}