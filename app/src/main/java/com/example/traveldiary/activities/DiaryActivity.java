package com.example.traveldiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.traveldiary.R;
import com.example.traveldiary.database.NoteDataBase;
import com.example.traveldiary.entities.Note;

import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    ImageView addNoteBtn;
    RecyclerView recyclerView;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        addNoteBtn = findViewById(R.id.addNote);
        recyclerView = findViewById(R.id.notesRecyclerView);

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), DiaryDetailsActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });

        getNotes();
    }

    private void getNotes() {
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NoteDataBase
                        .getDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                Log.d("CUR: ",notes.toString());
            }
        }

        new GetNotesTask().execute();
    }

}