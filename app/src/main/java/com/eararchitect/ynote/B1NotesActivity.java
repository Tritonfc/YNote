package com.eararchitect.ynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class B1NotesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView searchBar;
    private FloatingActionButton floatingActionButton;
    private B1NotesViewModel b1NotesViewModel;
    private RecyclerView b1Recycler;
    private ImageView slide;
    private int noteId = 1;
    final B1NotesAdapter b1NotesAdapter = new B1NotesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b1_notes);

        slide = findViewById(R.id.slide_btn);
        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewClass = new Intent(B1NotesActivity.this, SlideShowB1Activity.class);
                viewClass.putExtra(SlideShowB1Activity.EXTRA_ID, noteId);
                startActivity(viewClass);
            }
        });
        b1Recycler = findViewById(R.id.b1_recycler);
        b1Recycler.setLayoutManager(new LinearLayoutManager(B1NotesActivity.this));
        b1Recycler.setHasFixedSize(true);


        b1Recycler.setAdapter(b1NotesAdapter);

        b1NotesViewModel = new ViewModelProvider(B1NotesActivity.this).get(B1NotesViewModel.class);
        b1NotesViewModel.getAllB1Notes().observe(this, new Observer<List<B1Note>>() {
            @Override
            public void onChanged(List<B1Note> b1Notes) {
                //Set all notes from room database to recyclerview
                b1NotesAdapter.setNotes(b1Notes);
                if(b1Notes.isEmpty()){
                    slide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(B1NotesActivity.this,"No notes to show in slideShow",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
        b1NotesAdapter.setOnItemClickListener(new B1NotesAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B1Note b1Note) {
                Intent viewClass = new Intent(B1NotesActivity.this, B1NoteViewer.class);
                viewClass.putExtra(B1NoteViewer.EXTRA_ID, b1Note.getId());
                startActivity(viewClass);
            }
        });

        //On-Long listener to show option to delete or export document to google drive
        b1NotesAdapter.setLongOnItemClickListener(new B1NotesAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClick(B1Note b1Note, ImageView b1Image) {
                PopupMenu popupMenu = new PopupMenu(B1NotesActivity.this, b1Image);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.notes_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                b1NotesViewModel.delete(b1Note);
                                return true;
                            case R.id.export:
                                return true;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        searchBar = findViewById(R.id.search_bar);
        searchBar.setSubmitButtonEnabled(true);

        //Query listener for searchView to show suggestions for searches
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNotesFromDb(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() == 0) {
                    b1NotesViewModel.getAllB1Notes().observe(B1NotesActivity.this, new Observer<List<B1Note>>() {
                        @Override
                        public void onChanged(List<B1Note> b1Notes) {
                            b1NotesAdapter.setNotes(b1Notes);


                        }
                    });
                } else {
                    getNotesFromDb(newText);
                }

                return true;
            }
        });

        toolbar = findViewById(R.id.b1_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("B1 Notes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.b1_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addB1 = new Intent(B1NotesActivity.this, AddB1Note.class);
                finish();
                startActivity(addB1);

            }
        });
    }

    //Method for back button on ToolBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //Function to get text from searchView and show suggestions from room database
    private void getNotesFromDb(String searchText) {
        searchText = "%" + searchText + "%";
        b1NotesViewModel.getAllB1NotesFromSearch(searchText).observe(B1NotesActivity.this, new Observer<List<B1Note>>() {
            @Override
            public void onChanged(List<B1Note> b1Notes) {
                b1NotesAdapter.setNotes(b1Notes);

            }
        });
    }

}