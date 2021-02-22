package com.eararchitect.ynote;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

//Repository to hold and give database access to local app and servers

public class B1NoteRepository {

    private B1NoteDao b1NoteDao;
    private LiveData<List<B1Note>> allB1Notes;


    public B1NoteRepository(Application application) {
        B1NoteDatabase database = B1NoteDatabase.getInstance(application);
        b1NoteDao = database.b1NoteDao();
        allB1Notes = b1NoteDao.getAllB1Notes();

    }




    public void insert(final B1Note b1Note) {
        //Use database Executor to run operations on the background thread
        B1NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b1NoteDao.insert(b1Note);

            }
        });

    }

    public void update(final B1Note b1Note) {
        B1NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b1NoteDao.update(b1Note);

            }
        });

    }

    public void delete(final B1Note b1Note) {
        B1NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b1NoteDao.delete(b1Note);

            }
        });

    }

    public void deleteAllB1Notes() {
        B1NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b1NoteDao.deleteAllB1Notes();
            }
        });
    }

    public LiveData<List<B1Note>> getAllB1Notes() {
        return allB1Notes;
    }

    public LiveData<List<B1Note>> getAllB1NotesFromClass(String className) {
        return b1NoteDao.getAllB1NotesFromClass(className);

    }

    public LiveData<List<B1Note>> getAllB1NotesFromSearch(String title) {
        return b1NoteDao.getAllB1NotesFromSearch(title);

    }

    public LiveData<B1Note> getB1Note(final int id) {
        return b1NoteDao.getB1Note(id);
    }


}
