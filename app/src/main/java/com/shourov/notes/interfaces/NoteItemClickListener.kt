package com.shourov.notes.interfaces

import com.shourov.notes.database.tables.NoteTable

interface NoteItemClickListener {
    fun onNoteItemClick(currentItem: NoteTable)
}