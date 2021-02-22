package com.eararchitect.ynote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClassViewModel extends AndroidViewModel {
    private NewClassRepository newClassRepository;
    private LiveData<List<NewClass>> allClasses;

    public ClassViewModel(@NonNull Application application) {
        super(application);
        newClassRepository = new NewClassRepository(application);
        allClasses = newClassRepository.getAllClasses();
    }

    public void insert(NewClass newClass) {
        newClassRepository.insert(newClass);
    }

    public void update(NewClass newClass) {
        newClassRepository.update(newClass);
    }

    public void updateB1(int id) {
        newClassRepository.updateB1(id);
    }

    public void updateB2(int id) {
        newClassRepository.updateB2(id);
    }

    public void updateB3(int id) {
        newClassRepository.updateB3(id);
    }

    public void delete(NewClass newClass) {
        newClassRepository.delete(newClass);
    }

    public void deleteAllClasses() {
        newClassRepository.deleteAllClasses();
    }

    public LiveData<List<NewClass>> getAllClasses() {
        return allClasses;
    }

    public LiveData<List<NewClass>> getAllClassesFromSearch(String className) {
        return newClassRepository.getAllClassesFromSearch(className);

    }

}
