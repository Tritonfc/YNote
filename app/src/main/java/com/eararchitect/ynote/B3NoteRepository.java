package com.eararchitect.ynote;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
//Repository to hold and give database access to local app and servers
public class B3NoteRepository {

    private B3NoteDao b3NoteDao;
    private LiveData<List<B3Note>> allB3Notes;

    public B3NoteRepository(Application application) {
        B3NoteDatabase database = B3NoteDatabase.getInstance(application);
        b3NoteDao = database.b3NoteDao();
        allB3Notes = b3NoteDao.getAllB3Notes();

    }

    public void insert(final B3Note b3Note) {
        //Use database Executor to run operations on the background thread
        B3NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b3NoteDao.insert(b3Note);

            }
        });

    }
    public void update(final B3Note b3Note) {
        B3NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b3NoteDao.update(b3Note);

            }
        });

    }
    public void delete(final B3Note b3Note) {
        B3NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b3NoteDao.delete(b3Note);

            }
        });

    }


    public void deleteAllB3Notes() {
        B3NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b3NoteDao.deleteAllB3Notes();
            }
        });
    }
    public LiveData<List<B3Note>> getAllB3Notes() {
        return allB3Notes;
    }

    public LiveData<List<B3Note>> getAllB3NotesFromClass(String className) {
        allB3Notes = b3NoteDao.getAllB3NotesFromClass(className);
        return allB3Notes;
    }
    public LiveData<B3Note> getB3Note(final int id) {
        return b3NoteDao.getB3Note(id);
    }

    public LiveData<List<B3Note>> getAllB3NotesFromSearch(String title) {
        return b3NoteDao.getAllB3NotesFromSearch(title);

    }

}
