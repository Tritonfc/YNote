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

public class ClassB1NotesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private SearchView searchBar;
    private B1NotesViewModel b1NotesViewModel;
    final B1NotesAdapter b1NotesAdapter = new B1NotesAdapter();
    private RecyclerView b1Recycler;
    private String className;
    public static final String EXTRA_CLASS = "className";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_b1_notes);
        b1Recycler = findViewById(R.id.b1_recycler);
        b1Recycler.setLayoutManager(new LinearLayoutManager(ClassB1NotesActivity.this));
        b1Recycler.setHasFixedSize(true);

        b1NotesViewModel = new ViewModelProvider(ClassB1NotesActivity.this).get(B1NotesViewModel.class);
        Intent intent = getIntent();


        //Get className From intent
        if (intent.hasExtra(EXTRA_CLASS)) {
            className = intent.getStringExtra(EXTRA_CLASS);

        }


        b1Recycler.setAdapter(b1NotesAdapter);


        b1NotesViewModel.getAllB1NotesFromClass(className).observe(this, new Observer<List<B1Note>>() {
            @Override
            public void onChanged(List<B1Note> b1Notes) {
                b1NotesAdapter.setNotes(b1Notes);
            }
        });
        b1NotesAdapter.setOnItemClickListener(new B1NotesAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B1Note b1Note) {
                Intent viewClass = new Intent(ClassB1NotesActivity.this, B1NoteViewer.class);
                viewClass.putExtra(B1NoteViewer.EXTRA_ID, b1Note.getId());
                startActivity(viewClass);
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
                Intent addB1 = new Intent(ClassB1NotesActivity.this, AddB1Note.class);
                finish();
                startActivity(addB1);

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
                    b1NotesViewModel.getAllB1Notes().observe(ClassB1NotesActivity.this, new Observer<List<B1Note>>() {
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
        b1NotesViewModel.getAllB1NotesFromSearch(searchText).observe(ClassB1NotesActivity.this, new Observer<List<B1Note>>() {
            @Override
            public void onChanged(List<B1Note> b1Notes) {
                b1NotesAdapter.setNotes(b1Notes);

            }
        });
    }
}