package com.eararchitect.ynote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


//Dao for defining queries on the b1 note table

@Dao
public interface B1NoteDao {

    @Insert
    void insert(B1Note b1Note);



    @Update
    void update(B1Note b1Note);


    @Delete
    void delete(B1Note b1Note);

    //Query for deleting all the items from the class database
    @Query("DELETE FROM b1Note_table")
    void deleteAllB1Notes();

    //Query for selecting all the items from the class Database
    @Query("SELECT * FROM b1Note_table ORDER BY id DESC")
    LiveData<List<B1Note>> getAllB1Notes();

    //Query for selecting the b1 notes for a particular class
    @Query("SELECT * FROM b1Note_table WHERE class_name = :className")
    LiveData<List<B1Note>> getAllB1NotesFromClass(String className);

    //Query for selecting a b1 note with particular id
    @Query("SELECT * FROM b1Note_table WHERE id = :id")
    LiveData<B1Note> getB1Note(int id);

    //Query for getting b1 notes from a search term
    @Query("SELECT * FROM b1Note_table WHERE title LIKE :title")
    LiveData<List<B1Note>> getAllB1NotesFromSearch(String title);
}
