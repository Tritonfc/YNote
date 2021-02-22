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
public interface B3NoteDao {
    @Insert
    void insert(B3Note b3Note);


    @Update
    void update(B3Note b3Note);


    @Delete
    void delete(B3Note b3Note);

    //Query for deleting all the items from the class database
    @Query("DELETE FROM b3Note_table")
    void deleteAllB3Notes();

    //Query for selecting all the items from the class Database
    @Query("SELECT * FROM b3Note_table ORDER BY id DESC")
    LiveData<List<B3Note>> getAllB3Notes();

    //Query for selecting the b3 notes for a particular class
    @Query("SELECT * FROM b3Note_table WHERE class_name = :className")
    LiveData<List<B3Note>> getAllB3NotesFromClass(String className);
    //Query for selecting a b3 note with particular id
    @Query("SELECT * FROM b3Note_table WHERE id = :id")
    LiveData<B3Note> getB3Note(int id);
    //Query for getting b3 notes from a search term
    @Query("SELECT * FROM b3Note_table WHERE title LIKE :title")
    LiveData<List<B3Note>> getAllB3NotesFromSearch(String title);
}
