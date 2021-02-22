package com.eararchitect.ynote;

import android.app.Application;


import androidx.lifecycle.LiveData;

import java.util.List;

public class NewClassRepository {
    private NewClassDao newClassDao;
    private LiveData<List<NewClass>> allClasses;

    public NewClassRepository(Application application) {
        NewClassDatabase database = NewClassDatabase.getInstance(application);
        newClassDao = database.newClassDao();
        allClasses = newClassDao.getAllClasses();
    }

    public void insert(final NewClass newClass) {
        NewClassDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newClassDao.insert(newClass);

            }
        });

    }

    public void update(final NewClass newClass) {
        NewClassDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newClassDao.update(newClass);

            }
        });

    }

    public void delete(final NewClass newClass) {
        NewClassDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newClassDao.delete(newClass);

            }
        });
    }

    public void deleteAllClasses() {
        NewClassDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newClassDao.deleteAllClasses();

            }
        });
    }
    public void updateB1(final int id){
        NewClassDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newClassDao.updateB1(id);
            }
        });
    }

    public void updateB2(final int id){
        NewClassDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newClassDao.updateB2(id);
            }
        });
    }
    public void updateB3(final int id){
        NewClassDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newClassDao.updateB3(id);
            }
        });
    }

    public LiveData<List<NewClass>> getAllClasses() {
        return allClasses;
    }


    public LiveData<List<NewClass>> getAllClassesFromSearch(String className) {
        return newClassDao.getAllClassesFromSearch(className);

    }



}
