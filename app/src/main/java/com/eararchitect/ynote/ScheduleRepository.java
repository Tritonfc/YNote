package com.eararchitect.ynote;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;

import java.util.List;

public class ScheduleRepository {


    private ScheduleDao scheduleDao;

    public ScheduleRepository(Application application) {
        ScheduleDatabase database = ScheduleDatabase.getInstance(application);
        scheduleDao = database.scheduleDao();

    }


    public void insert(final Schedule schedule) {
        ScheduleDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                scheduleDao.insert(schedule);

            }
        });

    }

    public void update(final Schedule schedule) {
        ScheduleDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                scheduleDao.update(schedule);

            }
        });

    }

    public void delete(final Schedule schedule) {
        ScheduleDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                scheduleDao.delete(schedule);

            }
        });
    }



    public void deleteAllSchedulesFromDate(final String date) {
        ScheduleDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                scheduleDao.deleteAllSchedulesFromDate(date);
            }
        });
    }

    public LiveData<List<Schedule>> getScheduleForDate(String date) {
        return scheduleDao.getScheduleForDate(date);
    }
}
