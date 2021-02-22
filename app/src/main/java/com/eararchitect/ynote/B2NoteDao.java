package com.eararchitect.ynote;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
//Dao for defining queries on the b2 note table
@Dao
public interface B2NoteDao {
    @Insert
    void insert(B2Note b2Note);


    @Update
    void update(B2Note b2Note);


    @Delete
    void delete(B2Note b2Note);

    //Query for deleting all the items from the class database
    @Query("DELETE FROM b2Note_table")
    void deleteAllB2Notes();

    //Query for selecting all the items from the class Database
    @Query("SELECT * FROM b2Note_table ORDER BY id DESC")
    LiveData<List<B2Note>> getAllB2Notes();

    //Query for selecting the b2 notes for a particular class
    @Query("SELECT * FROM b2Note_table WHERE class_name = :className")
    LiveData<List<B2Note>> getAllB2NotesFromClass(String className);
    //Query for selecting a b2 note with particular id
    @Query("SELECT * FROM b2Note_table WHERE id = :id")
    LiveData<B2Note> getB2Note(int id);
    //Query for getting b2 notes from a search term
    @Query("SELECT * FROM b2Note_table WHERE title LIKE :title")
    LiveData<List<B2Note>> getAllB2NotesFromSearch(String title);
}
