package com.eararchitect.ynote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class B2NotesViewModel extends AndroidViewModel {
    private B2NoteRepository b2NoteRepository;
    private LiveData<List<B2Note>> allB2Notes;

    public B2NotesViewModel(@NonNull Application application) {
        super(application);

        b2NoteRepository = new B2NoteRepository(application);
        allB2Notes = b2NoteRepository.getAllB2Notes();
    }

    public void insert(B2Note b2Note) {
        b2NoteRepository.insert(b2Note);
    }

    public void update(B2Note b2Note) {
        b2NoteRepository.update(b2Note);
    }

    public void delete(B2Note b2Note) {
        b2NoteRepository.delete(b2Note);
    }

    public void deleteAllB2Notes() {
        b2NoteRepository.deleteAllB2Notes();
    }

    public LiveData<List<B2Note>> getAllB2Notes() {
        return allB2Notes;
    }

    public LiveData<List<B2Note>> getAllB2NotesFromClass(String className) {
        allB2Notes = b2NoteRepository.getAllB2NotesFromClass(className);
        return allB2Notes;
    }

    public LiveData<List<B2Note>> getAllB2NotesFromSearch(String title) {
        return b2NoteRepository.getAllB2NotesFromSearch(title);
    }

    public LiveData<B2Note> getB2Note(int id) {
        return b2NoteRepository.getB2Note(id);
    }
}
