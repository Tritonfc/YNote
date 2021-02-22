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

public class B2NotesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    final B2NoteAdapter b2NotesAdapter = new B2NoteAdapter();
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    private ImageView slide;
    private int noteId = 1;
    private B2NotesViewModel b2NotesViewModel;
    private RecyclerView b2Recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b2_notes);

        toolbar = findViewById(R.id.b2_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("B2 Notes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        slide = findViewById(R.id.slide);

        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewClass = new Intent(B2NotesActivity.this, SlideShowB2Activity.class);
                viewClass.putExtra(SlideShowB2Activity.EXTRA_ID, noteId);
                startActivity(viewClass);
            }
        });

        floatingActionButton = findViewById(R.id.b2_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addB2 = new Intent(B2NotesActivity.this, AddB2Note.class);
                finish();
                startActivity(addB2);
            }
        });


        b2Recycler = findViewById(R.id.b2_recycler);
        b2Recycler.setLayoutManager(new LinearLayoutManager(B2NotesActivity.this));
        b2Recycler.setHasFixedSize(true);


        b2Recycler.setAdapter(b2NotesAdapter);

        b2NotesViewModel = new ViewModelProvider(B2NotesActivity.this).get(B2NotesViewModel.class);
        b2NotesViewModel.getAllB2Notes().observe(this, new Observer<List<B2Note>>() {
            @Override
            public void onChanged(List<B2Note> b2Notes) {
                b2NotesAdapter.setNotes(b2Notes);
                if(b2Notes.isEmpty()){
                    slide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(B2NotesActivity.this,"No notes to show in slideShow",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
        b2NotesAdapter.setOnItemClickListener(new B2NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B2Note b2Note) {
                Intent viewClass = new Intent(B2NotesActivity.this, B2NoteViewer.class);
                viewClass.putExtra(B2NoteViewer.EXTRA_ID, b2Note.getId());
                startActivity(viewClass);
            }
        });
        b2NotesAdapter.setLongOnItemClickListener(new B2NoteAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClick(B2Note b2Note, ImageView b2Image) {
                PopupMenu popupMenu = new PopupMenu(B2NotesActivity.this, b2Image);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.notes_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                b2NotesViewModel.delete(b2Note);
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getNotesFromDb(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    b2NotesViewModel.getAllB2Notes().observe(B2NotesActivity.this, new Observer<List<B2Note>>() {
                        @Override
                        public void onChanged(List<B2Note> b2Notes) {
                            b2NotesAdapter.setNotes(b2Notes);


                        }
                    });
                } else {
                    getNotesFromDb(s);
                }

                return true;
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
        b2NotesViewModel.getAllB2NotesFromSearch(searchText).observe(B2NotesActivity.this, new Observer<List<B2Note>>() {
            @Override
            public void onChanged(List<B2Note> b2Notes) {
                b2NotesAdapter.setNotes(b2Notes);

            }
        });
    }
}