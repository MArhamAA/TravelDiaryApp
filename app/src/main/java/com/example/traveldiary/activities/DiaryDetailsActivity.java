package com.example.traveldiary.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveldiary.dao.NoteDao;
import com.example.traveldiary.database.NoteDataBase;
import com.example.traveldiary.entities.Note;
import com.example.traveldiary.R;
import com.example.traveldiary.entities.Utility;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiaryDetailsActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;

    private String selectedNoteColor;

    private NoteDataBase dataBase;

    private int diary_id;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    private AlertDialog dialogDeleteNote;

    private Note alreadyAvailableNote;

    private boolean pinStatus = false, shareStatus = false;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener((v) -> { onBackPressed(); });

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        diary_id = 0;

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        selectedNoteColor = "#333333";

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            diary_id = alreadyAvailableNote.getId();
            setViewOrUpdateNote();
        }

        initMiscellaneous();
        setSubtitleIndicatorColor();

        updateImagesWithDiaryId(String.valueOf(diary_id));

    }

    private void setViewOrUpdateNote() {
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(alreadyAvailableNote.getDateTime());

        pinStatus = alreadyAvailableNote.isPinned();
        shareStatus = alreadyAvailableNote.isShared();

        final LinearLayout layoutMisc = findViewById(R.id.layoutMisc);
        TextView pinText = layoutMisc.findViewById(R.id.layoutPinText);

        if (pinStatus) {
            pinText.setText("Unpin this diary");
        } else {
            pinText.setText("Pin this diary");
        }

        TextView shareText = layoutMisc.findViewById(R.id.layoutShareText);

        if (shareStatus == false) {
            shareText.setText("Share this diary");
        } else {
            shareText.setText("Stop sharing this diary");
        }

    }

    private void saveNote() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Utility.showToast(this, "Diary title can't be empty");
            return;
        }

        final Note note = new Note();
        note.setUserEmail(user.getEmail());
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubtitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setColor(selectedNoteColor);

        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NoteDataBase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();

        diary_id = note.getId();

        Utility.showToast(this, "OK");
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMisc = findViewById(R.id.layoutMisc);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMisc);

        TextView pinText = layoutMisc.findViewById(R.id.layoutPinText);

        layoutMisc.findViewById(R.id.textMisc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMisc.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMisc.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMisc.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMisc.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMisc.findViewById(R.id.imageColor5);

        layoutMisc.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMisc.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3E";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMisc.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMisc.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A5AFC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_baseline_done_24);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMisc.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_baseline_done_24);
                setSubtitleIndicatorColor();
            }
        });

        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvailableNote.getColor()) {
                case "#FDBE3E":
                    layoutMisc.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842":
                    layoutMisc.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3A5AFC":
                    layoutMisc.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000":
                    layoutMisc.findViewById(R.id.viewColor5).performClick();
                    break;
            }
        }

        layoutMisc.findViewById(R.id.layoutAddImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                selectImage();
            }
        });

        if (alreadyAvailableNote != null) {
            layoutMisc.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMisc.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteNoteDialog();
                }
            });

            layoutMisc.findViewById(R.id.layoutPin).setVisibility(View.VISIBLE);
            layoutMisc.findViewById(R.id.layoutPin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    pinNote();
                }
            });

            layoutMisc.findViewById(R.id.layoutShare).setVisibility(View.VISIBLE);
            layoutMisc.findViewById(R.id.layoutShare).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    shareNote();
                }
            });

        }

    }

    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DiaryDetailsActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NoteDataBase.getDatabase(getApplicationContext()).noteDao()
                                    .deleteNote(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();

                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });

        }

        dialogDeleteNote.show();

    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    private void selectImage() {
        Intent intent = new Intent(this, ImagesUploadActivity.class);
//        Toast.makeText(this, "Diary ID: " + diary_id, Toast.LENGTH_SHORT).show();
        intent.putExtra("diary_id", String.valueOf(diary_id));
        startActivity(intent);
    }

    private void pinNote() {
        pinStatus = alreadyAvailableNote.isPinned();
        Note updatedNote = alreadyAvailableNote;
//        Toast.makeText(this, "HERE "+alreadyAvailableNote.getId(), Toast.LENGTH_SHORT).show();
        if (!pinStatus) {
            // Pin the note
            updatedNote.setPinned(true);
            Toast.makeText(this, "Pinned", Toast.LENGTH_SHORT).show();
        } else {
            // Unpin the note
            updatedNote.setPinned(false);
            Toast.makeText(this, "Unpinned", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("StaticFieldLeak")
        class UpdateNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NoteDataBase.getDatabase(getApplicationContext()).noteDao().updateNote(updatedNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new UpdateNoteTask().execute();
    }

    private void shareNote() {
        shareStatus = alreadyAvailableNote.isShared();
        Note updatedNote = alreadyAvailableNote;
//        Toast.makeText(this, "HERE "+alreadyAvailableNote.getId(), Toast.LENGTH_SHORT).show();
        if (!shareStatus) {
            // Pin the note
            updatedNote.setShared(true);
            Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
        } else {
            // Unpin the note
            updatedNote.setShared(false);
            Toast.makeText(this, "Sharing stopped", Toast.LENGTH_SHORT).show();
        }

        @SuppressLint("StaticFieldLeak")
        class UpdateNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NoteDataBase.getDatabase(getApplicationContext()).noteDao().updateNote(updatedNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new UpdateNoteTask().execute();
    }

    private void updateImagesWithDiaryId(String newDiaryId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference imagesCollection = firestore.collection("images");

        // Query images with diary_id = "0"
        imagesCollection.whereEqualTo("diary_id", "0")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Update the diary_id field with the newDiaryId
                        DocumentReference imageRef = imagesCollection.document(document.getId());
                        imageRef.update("diary_id", newDiaryId)
                                .addOnSuccessListener(aVoid -> {
                                    // Handle success
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }


}