package com.eararchitect.ynote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Data entry Object Class for defining operations we can perform on the class_table

@Dao
public interface NewClassDao {
    @Insert
    void insert(NewClass newClass);

    @Update
    void update(NewClass newClass);


    @Delete
    void delete(NewClass newClass);


    //Query for deleting all the items from the class Database
    @Query("DELETE FROM class_table")
    void deleteAllClasses();

    //Query for selecting all the items from the class Database
    @Query("SELECT * FROM class_table ORDER BY id DESC")
    LiveData<List<NewClass>> getAllClasses();

    //Query for selecting all classes from a search term
    @Query("SELECT * FROM class_table WHERE class_name LIKE :className")
    LiveData<List<NewClass>> getAllClassesFromSearch(String className);


    //Query to update the number of b1 notes in a class
    @Query("UPDATE class_table SET b1_notes = b1_notes+1 WHERE id = :id")
    void updateB1(int id);


    //Query to update the number of b2 notes in a class
    @Query("UPDATE class_table SET b2_notes = b2_notes+1 WHERE id = :id")
    void updateB2(int id);

    //Query to update the number of b3 notes in a class
    @Query("UPDATE class_table SET b3_notes = b3_notes+1 WHERE id = :id")
    void updateB3(int id);
}
