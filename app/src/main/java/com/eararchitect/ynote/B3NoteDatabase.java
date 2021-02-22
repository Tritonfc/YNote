package com.eararchitect.ynote;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = B3Note.class, version = 1, exportSchema = false)
public abstract class B3NoteDatabase extends RoomDatabase {


    private static B3NoteDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;


    //Initialization of ExecutorService to handle background tasks Asynchronously
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract B3NoteDao b3NoteDao();

    //Synchronized function for checking if an instance of the database has already been created before creating another
    public static synchronized B3NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), B3NoteDatabase.class, "b3Note_table").fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }
}
