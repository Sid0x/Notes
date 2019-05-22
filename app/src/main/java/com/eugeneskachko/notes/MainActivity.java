package com.eugeneskachko.notes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

import com.eugeneskachko.notes.dataBase.DataBaseClient;
import com.eugeneskachko.notes.dataBase.Note;
import com.eugeneskachko.notes.dataBase.NoteDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabNewNote;
    private RecyclerView recyclerView;
    private SearchView search;
    private int sortBy = R.id.sortByNew_old;
    private String searchQuery = null;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(sortBy).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByA_z:
                setSortBy(R.id.sortByA_z);
                item.setChecked(true);
                getNotes();
                return true;
            case R.id.sortByZ_a:
                setSortBy(R.id.sortByZ_a);
                item.setChecked(true);
                getNotes();
                return true;
            case R.id.sortByNew_old:
                setSortBy(R.id.sortByNew_old);
                item.setChecked(true);
                getNotes();
                return true;
            case R.id.sortByOld_new:
                setSortBy(R.id.sortByOld_new);
                item.setChecked(true);
                getNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createListenerForFBNewNote();
        createListenerForSearch();

        recyclerView = findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void getNotes() {
        class getNotes extends AsyncTask<String, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(String... queries) {
                List<Note> noteList;
                NoteDao noteDao = DataBaseClient
                        .getInstance(getApplicationContext())
                        .getAppDataBase()
                        .noteDao();

                if (getSearchQuery() == null || getSearchQuery().isEmpty()) {
                    switch (getSortBy()) {
                        case R.id.sortByA_z:
                            noteList = noteDao.getAllSortByA_z();
                            break;
                        case R.id.sortByZ_a:
                            noteList = noteDao.getAllSortByZ_a();
                            break;
                        case R.id.sortByNew_old:
                            noteList = noteDao.getAllSortByNew_old();
                            break;
                        case R.id.sortByOld_new:
                            noteList = noteDao.getAllSortByOld_new();
                            break;
                        default:
                            noteList = noteDao.getAll();
                            break;
                    }
                } else {
                    switch (getSortBy()) {
                        case R.id.sortByA_z:
                            noteList = noteDao.getQuerySortByA_z(getSearchQuery());
                            break;
                        case R.id.sortByZ_a:
                            noteList = noteDao.getQuerySortByZ_a(getSearchQuery());
                            break;
                        case R.id.sortByNew_old:
                            noteList = noteDao.getQuerySortByNew_old(getSearchQuery());
                            break;
                        case R.id.sortByOld_new:
                            noteList = noteDao.getQuerySortByOld_new(getSearchQuery());
                            break;
                        default:
                            noteList = noteDao.getQuery(getSearchQuery());
                            break;
                    }
                }
                return noteList;
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                NotesAdapter adapter = new NotesAdapter(MainActivity.this, notes);
                recyclerView.setAdapter(adapter);
            }
        }

        getNotes getNotes = new getNotes();
        getNotes.execute();
    }

    private void createListenerForSearch(){
        search = findViewById(R.id.svQuery);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                setSearchQuery("%" + query + "%");
                getNotes();
                return false;
            }
        });
    }

    private void createListenerForFBNewNote(){
        fabNewNote = findViewById(R.id.fbNewNote);
        fabNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
