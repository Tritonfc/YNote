package com.eararchitect.ynote;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = Schedule.class, version = 1,exportSchema = false)
public abstract class ScheduleDatabase extends RoomDatabase {

    private static ScheduleDatabase instance;
    private static final int NUMBER_OF_THREADS =4;


    //Initialization of ExecutorService to handle background tasks Asynchronously
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract ScheduleDao scheduleDao();


    //Synchronized function for checking if an instance of the database has already been created before creating another
    public static synchronized ScheduleDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ScheduleDatabase.class, "schedule_database").fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }
}
