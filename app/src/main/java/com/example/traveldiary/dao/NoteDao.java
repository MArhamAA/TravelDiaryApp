package com.example.traveldiary.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.traveldiary.entities.Note;

import java.util.List;
@Dao
public interface NoteDao {

    /** @noinspection AndroidUnresolvedRoomSqlReference*/
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE user_email = :userEmail ORDER BY id DESC")
    List<Note> getNotesByEmail(String userEmail);

    @Query("SELECT * FROM notes WHERE pinned = :pin AND user_email = :userEmail ORDER BY id DESC")
    List<Note> getPinnedNotes(boolean pin, String userEmail);

    @Query("SELECT * FROM notes WHERE shared = :share AND user_email = :userEmail ORDER BY id DESC")
    List<Note> getMySharedNotes(boolean share, String userEmail);

    @Query("SELECT * FROM notes WHERE shared = :share AND user_email != :userEmail ORDER BY id DESC")
    List<Note> getOtherSharedNotes(boolean share, String userEmail);

    @Query("UPDATE notes SET pinned = :pin WHERE id = :id")
    void pin(int id, boolean pin);

    @Query("UPDATE notes SET shared = :share WHERE id = :id")
    void share(int id, boolean share);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

}
