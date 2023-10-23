package com.shourov.notes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shourov.notes.database.tables.NoteTable
import com.shourov.notes.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
    private val _searchResultLiveData = MutableLiveData<List<NoteTable>>()
    val searchResultLiveData : LiveData<List<NoteTable>> get() = _searchResultLiveData

    fun searchNote(title: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.searchNote(title)
            withContext(Dispatchers.Main) {
                _searchResultLiveData.postValue(result)
            }
        }
    }

    suspend fun deleteNote(note: NoteTable) = repository.deleteNote(note)
}