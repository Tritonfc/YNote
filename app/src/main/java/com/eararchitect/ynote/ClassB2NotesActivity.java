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

public class ClassB2NotesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private B2NotesViewModel b2NotesViewModel;
    private RecyclerView b2Recycler;
    public static final String EXTRA_CLASS = "className";
    final B2NoteAdapter b2NotesAdapter = new B2NoteAdapter();
    private String className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_b2_notes);
        b2NotesViewModel = new ViewModelProvider(ClassB2NotesActivity.this).get(B2NotesViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_CLASS)) {
            className = intent.getStringExtra(EXTRA_CLASS);

        }

        toolbar = findViewById(R.id.b2_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("B2 Notes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.b2_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addB2 = new Intent(ClassB2NotesActivity.this, AddB2Note.class);
                finish();
                startActivity(addB2);
            }
        });


        b2Recycler = findViewById(R.id.b2_recycler);
        b2Recycler.setLayoutManager(new LinearLayoutManager(ClassB2NotesActivity.this));
        b2Recycler.setHasFixedSize(true);


        b2Recycler.setAdapter(b2NotesAdapter);


        b2NotesViewModel.getAllB2NotesFromClass(className).observe(this, new Observer<List<B2Note>>() {
            @Override
            public void onChanged(List<B2Note> b2Notes) {
                b2NotesAdapter.setNotes(b2Notes);

            }
        });
        b2NotesAdapter.setOnItemClickListener(new B2NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B2Note b2Note) {
                Intent viewClass = new Intent(ClassB2NotesActivity.this, B2NoteViewer.class);
                viewClass.putExtra(B2NoteViewer.EXTRA_ID,b2Note.getId());
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
                    b2NotesViewModel.getAllB2Notes().observe(ClassB2NotesActivity.this, new Observer<List<B2Note>>() {
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


    //Function to get text from searchView and show suggestions from room database
    private void getNotesFromDb(String searchText) {
        searchText = "%" + searchText + "%";
        b2NotesViewModel.getAllB2NotesFromSearch(searchText).observe(ClassB2NotesActivity.this, new Observer<List<B2Note>>() {
            @Override
            public void onChanged(List<B2Note> b2Notes) {
                b2NotesAdapter.setNotes(b2Notes);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}