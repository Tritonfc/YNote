package com.eararchitect.ynote;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = NewClass.class, version = 1,exportSchema = false)
public abstract class NewClassDatabase extends RoomDatabase {

    private static NewClassDatabase instance;
    private static final int NUMBER_OF_THREADS =4;


     //Initialization of ExecutorService to handle background tasks Asynchronously
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract NewClassDao newClassDao();


     //Synchronized function for checking if an instance of the database has already been created before creating another
    public static synchronized NewClassDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NewClassDatabase.class, "class_database").fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }
}
