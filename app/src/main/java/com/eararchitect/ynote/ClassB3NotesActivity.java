package com.eararchitect.ynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ClassB3NotesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private B3NotesViewModel b3NotesViewModel;
    private RecyclerView b3Recycler;
    final B3NoteAdapter b3NotesAdapter = new B3NoteAdapter();
    public static final String EXTRA_CLASS = "className";
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_b3_notes);
        b3NotesViewModel = new ViewModelProvider(ClassB3NotesActivity.this).get(B3NotesViewModel.class);


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_CLASS)) {
            className = intent.getStringExtra(EXTRA_CLASS);

        }


        toolbar = findViewById(R.id.b3_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("B3 Notes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.b3_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addB3 = new Intent(ClassB3NotesActivity.this, AddB3Note.class);
                finish();
                startActivity(addB3);
            }
        });


        b3Recycler = findViewById(R.id.b3_recycler);
        b3Recycler.setLayoutManager(new LinearLayoutManager(ClassB3NotesActivity.this));
        b3Recycler.setHasFixedSize(true);


        b3Recycler.setAdapter(b3NotesAdapter);


        b3NotesViewModel.getAllB3NotesFromClass(className).observe(this, new Observer<List<B3Note>>() {
            @Override
            public void onChanged(List<B3Note> b3Notes) {
                b3NotesAdapter.setNotes(b3Notes);

            }
        });
        b3NotesAdapter.setOnItemClickListener(new B3NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B3Note b3Note) {
                Intent viewClass = new Intent(ClassB3NotesActivity.this, B3NoteViewer.class);
                viewClass.putExtra(B3NoteViewer.EXTRA_ID, b3Note.getId());
                startActivity(viewClass);
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
                    b3NotesViewModel.getAllB3Notes().observe(ClassB3NotesActivity.this, new Observer<List<B3Note>>() {
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
        b3NotesViewModel.getAllB3NotesFromSearch(searchText).observe(ClassB3NotesActivity.this, new Observer<List<B3Note>>() {
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