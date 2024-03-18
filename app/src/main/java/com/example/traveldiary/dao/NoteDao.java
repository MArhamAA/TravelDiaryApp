package com.example.traveldiary.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.traveldiary.entities.Note;

import java.util.List;
@Dao
public interface NoteDao {

    /** @noinspection AndroidUnresolvedRoomSqlReference*/
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE user_email = :userEmail ORDER BY id DESC")
    List<Note> getNotesByEmail(String userEmail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

}
