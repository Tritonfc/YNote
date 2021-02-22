package com.eararchitect.ynote;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Remember to change version of database if any functionality in dao is modified
@Database(entities = B1Note.class, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class B1NoteDatabase extends RoomDatabase {

    private static B1NoteDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;


    //Initialization of ExecutorService to handle background tasks Asynchronously
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract B1NoteDao b1NoteDao();

    //Synchronized function for checking if an instance of the database has already been created before creating another
    public static synchronized B1NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), B1NoteDatabase.class, "b1Note_table").fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }

}
