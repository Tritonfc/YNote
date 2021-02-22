package com.eararchitect.ynote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel {

    private ScheduleRepository scheduleRepository;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);

        scheduleRepository = new ScheduleRepository(application);
    }


    public void insert(Schedule schedule) {
        scheduleRepository.insert(schedule);
    }

    public void update(Schedule schedule) {
        scheduleRepository.update(schedule);
    }

    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }

    public void deleteAllSchedulesFromDate(String date) {
        scheduleRepository.deleteAllSchedulesFromDate(date);
    }

    public LiveData<List<Schedule>> getAllSchedulesForDate(String date) {
        return scheduleRepository.getScheduleForDate(date);
    }


}
