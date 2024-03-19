package com.example.traveldiary.listeners;

import com.example.traveldiary.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
