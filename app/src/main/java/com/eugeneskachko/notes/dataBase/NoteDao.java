package com.eugeneskachko.notes.dataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("SELECT * FROM note ORDER BY noteTitle COLLATE NOCASE ASC")
    List<Note> getAllSortByA_z();

    @Query("SELECT * FROM note ORDER BY noteTitle COLLATE NOCASE DESC")
    List<Note> getAllSortByZ_a();

    @Query("SELECT * FROM note ORDER BY unixTimestamp DESC")
    List<Note> getAllSortByNew_old();

    @Query("SELECT * FROM note ORDER BY unixTimestamp ASC")
    List<Note> getAllSortByOld_new();

    @Query("SELECT * FROM note WHERE noteTitle LIKE :like or noteDescription LIKE :like")
    List<Note> getQuery(String like);

    @Query("SELECT * FROM note WHERE noteTitle LIKE :like or noteDescription LIKE :like ORDER BY noteTitle COLLATE NOCASE ASC")
    List<Note> getQuerySortByA_z(String like);

    @Query("SELECT * FROM note WHERE noteTitle LIKE :like or noteDescription LIKE :like ORDER BY noteTitle COLLATE NOCASE DESC")
    List<Note> getQuerySortByZ_a(String like);

    @Query("SELECT * FROM note WHERE noteTitle LIKE :like or noteDescription LIKE :like ORDER BY unixTimestamp DESC")
    List<Note> getQuerySortByNew_old(String like);

    @Query("SELECT * FROM note WHERE noteTitle LIKE :like or noteDescription LIKE :like ORDER BY unixTimestamp ASC")
    List<Note> getQuerySortByOld_new(String like);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
