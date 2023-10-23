package com.shourov.notes.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String
)