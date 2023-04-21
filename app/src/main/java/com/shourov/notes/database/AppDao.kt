package com.shourov.notes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shourov.notes.database.tables.NoteTable

@Dao
interface AppDao {

    @Query("SELECT * FROM note_table")
    fun getNotes(): LiveData<List<NoteTable>>

    @Insert
    suspend fun insertNote(note: NoteTable)

    @Delete
    suspend fun deleteNote(note: NoteTable)

    @Update
    suspend fun updateNote(note: NoteTable)

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :noteTitle || '%'")
    suspend fun searchNote(noteTitle: String): List<NoteTable>
}