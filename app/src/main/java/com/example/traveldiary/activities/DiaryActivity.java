package com.example.traveldiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.traveldiary.entities.DiaryAdapter;
import com.example.traveldiary.R;
import com.example.traveldiary.entities.*;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class DiaryActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    ImageView addNoteBtn;
    RecyclerView recyclerView;
    DiaryAdapter diaryAdapter;

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
    }

//    void setupRecyclerView() {
//        Query query  = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
//        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
//                .setQuery(query,Note.class).build();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        diaryAdapter = new DiaryAdapter(options,this);
//        recyclerView.setAdapter(diaryAdapter);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        diaryAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        diaryAdapter.stopListening();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        diaryAdapter.notifyDataSetChanged();
//    }
}