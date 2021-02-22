package com.eararchitect.ynote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;



public class B1NotesViewModel extends AndroidViewModel {
    private B1NoteRepository b1NoteRepository;
    private LiveData<List<B1Note>> allB1Notes;
    public B1NotesViewModel(@NonNull Application application) {
        super(application);

        b1NoteRepository = new B1NoteRepository(application);
        allB1Notes = b1NoteRepository.getAllB1Notes();
    }
    public void insert(B1Note b1Note){
        b1NoteRepository.insert(b1Note);
    }
    public void update(B1Note b1Note){
        b1NoteRepository.update(b1Note);
    }
    public void delete(B1Note b1Note){
        b1NoteRepository.delete(b1Note);
    }
    public void deleteAllB1Notes(){
        b1NoteRepository.deleteAllB1Notes();
    }
    public LiveData<List<B1Note>> getAllB1Notes(){
        return allB1Notes;
    }

    public LiveData<List<B1Note>> getAllB1NotesFromClass(String className){
        allB1Notes = b1NoteRepository.getAllB1NotesFromClass(className);
        return allB1Notes;
    }

    public LiveData<List<B1Note>> getAllB1NotesFromSearch(String title){
       return b1NoteRepository.getAllB1NotesFromSearch(title);

    }

    public LiveData<B1Note> getB1Note(int id){
        return b1NoteRepository.getB1Note(id);
    }


}
