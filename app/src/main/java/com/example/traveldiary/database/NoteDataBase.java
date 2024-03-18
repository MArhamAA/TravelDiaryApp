package com.example.traveldiary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.traveldiary.dao.NoteDao;
import com.example.traveldiary.entities.Note;

@Database(entities = {Note.class}, version = 2, exportSchema = false)
public abstract class NoteDataBase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static NoteDataBase notesDatabase;

    public static synchronized NoteDataBase getDatabase(Context context) {
        if (notesDatabase == null) {
            notesDatabase = Room.databaseBuilder(
                            context.getApplicationContext(),
                            NoteDataBase.class,
                            "notes_db"
                    ).addMigrations(MIGRATION_1_2)
                    .build();
        }
        return notesDatabase;
    }

    // Define your migration from version 1 to version 2
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Execute SQL statements to perform migration, e.g., adding a new column
            database.execSQL("ALTER TABLE notes ADD COLUMN user_email TEXT");
        }
    };
}
