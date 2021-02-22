package com.eararchitect.ynote;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;


//Repository to hold and give database access to local app and servers

public class B2NoteRepository {


    private B2NoteDao b2NoteDao;
    private LiveData<List<B2Note>> allB2Notes;

    public B2NoteRepository(Application application) {
        B2NoteDatabase database = B2NoteDatabase.getInstance(application);
        b2NoteDao = database.b2NoteDao();
        allB2Notes = b2NoteDao.getAllB2Notes();

    }

    public void insert(final B2Note b2Note) {
        B2NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b2NoteDao.insert(b2Note);

            }
        });

    }

    public void update(final B2Note b2Note) {
        B2NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b2NoteDao.update(b2Note);

            }
        });

    }

    public void delete(final B2Note b2Note) {
        B2NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b2NoteDao.delete(b2Note);

            }
        });

    }


    public void deleteAllB2Notes() {
        B2NoteDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                b2NoteDao.deleteAllB2Notes();
            }
        });
    }

    public LiveData<List<B2Note>> getAllB2Notes() {
        return allB2Notes;
    }

    public LiveData<List<B2Note>> getAllB2NotesFromClass(String className) {
        allB2Notes = b2NoteDao.getAllB2NotesFromClass(className);
        return allB2Notes;
    }

    public LiveData<B2Note> getB2Note(final int id) {
        return b2NoteDao.getB2Note(id);
    }

    public LiveData<List<B2Note>> getAllB2NotesFromSearch(String title) {
      return b2NoteDao.getAllB2NotesFromSearch(title);

    }
}
