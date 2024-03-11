package com.example.traveldiary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.traveldiary.dao.NoteDao;
import com.example.traveldiary.entities.Note;

@Database(entities = Note.class, version=1, exportSchema = false)
public abstract class NoteDataBase extends RoomDatabase {

    private static NoteDataBase notesDatabase;

    public static synchronized NoteDataBase getDatabase(Context context) {
        if (notesDatabase == null) {
            notesDatabase = Room.databaseBuilder(
                    context,
                    NoteDataBase.class,
                    "notes_db"
            ).build();
        }
        return notesDatabase;
    }

    public abstract NoteDao noteDao();
}
