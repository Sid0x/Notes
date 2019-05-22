package com.eugeneskachko.notes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.eugeneskachko.notes.dataBase.DataBaseClient;
import com.eugeneskachko.notes.dataBase.Note;

import java.util.Date;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText etNoteTitle, etNoteDescription;
    private boolean updateChecker = false;
    public Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);

        createNote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_details, menu);
        return true;
    }

    private void createNote(){
        note = (Note) getIntent().getSerializableExtra("note");

        if (note != null) {
            setUpdateChecker(true);
            loadNote(note);
        } else {
            note = new Note();
        }
    }

    private void loadNote(Note note) {
        etNoteTitle.setText(note.getNoteTitle());
        etNoteDescription.setText(note.getNoteDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveNote();
                return true;
            case R.id.Delete:
                deleteNote();
                return true;
            case R.id.Share:
                shareNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareNote(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, etNoteTitle.getText().toString().trim() + "\n\n" + etNoteDescription.getText().toString().trim());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void saveNote() {
        final String noteTitle = etNoteTitle.getText().toString().trim();
        final String noteDescription = etNoteDescription.getText().toString().trim();

        if (noteTitle.isEmpty()) {
            etNoteTitle.setError("Title required");
            etNoteTitle.requestFocus();
            return;
        }

        if (noteDescription.isEmpty()) {
            etNoteDescription.setError("Description required");
            etNoteDescription.requestFocus();
            return;
        }

        class SaveNote extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Date date = new Date();
                long unixTimestamp = date.getTime();

                note.setNoteTitle(noteTitle);
                note.setNoteDescription(noteDescription);
                note.setUnixTimestamp(unixTimestamp);

                if (isUpdateChecker()) {
                    DataBaseClient.getInstance(getApplicationContext()).getAppDataBase()
                            .noteDao()
                            .update(note);
                } else {
                    DataBaseClient.getInstance(getApplicationContext()).getAppDataBase()
                            .noteDao()
                            .insert(note);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                if (isUpdateChecker())
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        }

        SaveNote saveNote = new SaveNote();
        saveNote.execute();
    }

    private void deleteNote() {
        class DeleteNote extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DataBaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .noteDao()
                        .delete(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(NoteDetailsActivity.this, MainActivity.class));
            }
        }

        DeleteNote dt = new DeleteNote();
        dt.execute();

    }

    public boolean isUpdateChecker() {
        return updateChecker;
    }

    public void setUpdateChecker(boolean updateChecker) {
        this.updateChecker = updateChecker;
    }

}
