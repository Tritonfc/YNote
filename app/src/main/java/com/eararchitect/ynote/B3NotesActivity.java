package com.eararchitect.ynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class B3NotesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    final B3NoteAdapter b3NotesAdapter = new B3NoteAdapter();
    private B3NotesViewModel b3NotesViewModel;
    private ImageView slide;
    private RecyclerView b3Recycler;
    private int noteId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b3_notes);


        toolbar = findViewById(R.id.b3_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("B3 Notes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        slide = findViewById(R.id.play_side);
        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewClass = new Intent(B3NotesActivity.this, SlideShowB3Activity.class);
                viewClass.putExtra(SlideShowB3Activity.EXTRA_ID, noteId);
                startActivity(viewClass);

            }
        });

        floatingActionButton = findViewById(R.id.b3_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addB3 = new Intent(B3NotesActivity.this, AddB3Note.class);
                finish();
                startActivity(addB3);
            }
        });


        b3Recycler = findViewById(R.id.b3_recycler);
        b3Recycler.setLayoutManager(new LinearLayoutManager(B3NotesActivity.this));
        b3Recycler.setHasFixedSize(true);


        b3Recycler.setAdapter(b3NotesAdapter);

        b3NotesViewModel = new ViewModelProvider(B3NotesActivity.this).get(B3NotesViewModel.class);
        b3NotesViewModel.getAllB3Notes().observe(this, new Observer<List<B3Note>>() {
            @Override
            public void onChanged(List<B3Note> b3Notes) {
                b3NotesAdapter.setNotes(b3Notes);
                if(b3Notes.isEmpty()){
                    slide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(B3NotesActivity.this,"No notes to show in slideShow",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        b3NotesAdapter.setOnItemClickListener(new B3NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B3Note b3Note) {
                Intent viewClass = new Intent(B3NotesActivity.this, B3NoteViewer.class);
                viewClass.putExtra(B3NoteViewer.EXTRA_ID, b3Note.getId());
                startActivity(viewClass);
            }
        });
        //On-Long listener to show option to delete or export document to google drive
        b3NotesAdapter.setLongOnItemClickListener(new B3NoteAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClick(B3Note b3Note, ImageView b3Image) {
                PopupMenu popupMenu = new PopupMenu(B3NotesActivity.this, b3Image);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.notes_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                b3NotesViewModel.delete(b3Note);
                                return true;
                            case R.id.export:
                                return true;

                        }
                        return false;
                    }
                });
            }
        });

        searchView = findViewById(R.id.search_bar);
        //Query listener for searchView to show suggestions for searches
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getNotesFromDb(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    b3NotesViewModel.getAllB3Notes().observe(B3NotesActivity.this, new Observer<List<B3Note>>() {
                        @Override
                        public void onChanged(List<B3Note> b3Notes) {
                            b3NotesAdapter.setNotes(b3Notes);


                        }
                    });
                } else {
                    getNotesFromDb(s);
                }

                return true;
            }
        });
    }


    //Function to get text from searchView and show suggestions from room database
    private void getNotesFromDb(String searchText) {
        searchText = "%" + searchText + "%";
        b3NotesViewModel.getAllB3NotesFromSearch(searchText).observe(B3NotesActivity.this, new Observer<List<B3Note>>() {
            @Override
            public void onChanged(List<B3Note> b3Notes) {
                b3NotesAdapter.setNotes(b3Notes);

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
}