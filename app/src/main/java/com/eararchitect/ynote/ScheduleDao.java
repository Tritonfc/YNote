package com.eararchitect.ynote;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert
    void insert(Schedule schedule);


    @Update
    void update(Schedule schedule);


    @Delete
    void delete(Schedule schedule);

    //Query for deleting all the items from the class database
    @Query("DELETE FROM schedule_table WHERE date = :date")
    void deleteAllSchedulesFromDate(String date);

    //Query for selecting tasks for a particular date
    @Query("SELECT * FROM schedule_table WHERE date = :date")
    LiveData<List<Schedule>> getScheduleForDate(String date);
}
