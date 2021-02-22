package com.eararchitect.ynote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class B3NotesViewModel extends AndroidViewModel {
    private B3NoteRepository b3NoteRepository;
    private LiveData<List<B3Note>> allB3Notes;
    public B3NotesViewModel(@NonNull Application application) {
        super(application);

        b3NoteRepository = new B3NoteRepository(application);
        allB3Notes = b3NoteRepository.getAllB3Notes();
    }

    public void insert(B3Note b3Note){
        b3NoteRepository.insert(b3Note);
    }
    public void update(B3Note b3Note){
        b3NoteRepository.update(b3Note);
    }
    public void delete(B3Note b3Note){
        b3NoteRepository.delete(b3Note);
    }
    public void deleteAllB3Notes(){
        b3NoteRepository.deleteAllB3Notes();
    }

    public LiveData<List<B3Note>> getAllB3Notes(){
        return allB3Notes;
    }
    public LiveData<List<B3Note>> getAllB3NotesFromClass(String className){
        allB3Notes = b3NoteRepository.getAllB3NotesFromClass(className);
        return allB3Notes;
    }

    public LiveData<List<B3Note>> getAllB3NotesFromSearch(String title) {
        return b3NoteRepository.getAllB3NotesFromSearch(title);
    }

    public LiveData<B3Note> getB3Note(int id){
        return b3NoteRepository.getB3Note(id);
    }
}
