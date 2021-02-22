package com.eararchitect.ynote;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = B2Note.class, version = 1, exportSchema = false)
public abstract class B2NoteDatabase extends RoomDatabase {


    private static B2NoteDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;


    //Initialization of ExecutorService to handle background tasks Asynchronously
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract B2NoteDao b2NoteDao();

    //Synchronized function for checking if an instance of the database has already been created before creating another
    public static synchronized B2NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), B2NoteDatabase.class, "b2Note_table").fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }
}
