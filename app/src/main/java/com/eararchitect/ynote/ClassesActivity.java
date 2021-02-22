package com.eararchitect.ynote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ClassesActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private SearchView searchView;
    final ClassesAdapter adapts = new ClassesAdapter();
    private FloatingActionButton floatingActionButton;
    private ClassViewModel classViewModel;
    private int b1 = 0;
    private int b2 = 0;
    private int b3 = 0;

    private EditText classEdit;
    private RecyclerView classesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);


        toolbar = findViewById(R.id.classes_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Classes");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        classesRecycler = findViewById(R.id.classes_recycler);
        classesRecycler.setLayoutManager(new LinearLayoutManager(ClassesActivity.this));
        classesRecycler.setHasFixedSize(true);


        classesRecycler.setAdapter(adapts);

        classViewModel = new ViewModelProvider(ClassesActivity.this).get(ClassViewModel.class);
        //Get all classes from room database and display it in recyclerView
        classViewModel.getAllClasses().observe(this, new Observer<List<NewClass>>() {
            @Override
            public void onChanged(List<NewClass> newClasses) {
                adapts.setClasses(newClasses);

            }
        });

        adapts.setOnItemClickListener(new ClassesAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NewClass newClass) {
                Intent viewClass = new Intent(ClassesActivity.this, ViewClassActivity.class);
                viewClass.putExtra(ViewClassActivity.EXTRA_TITLE, newClass.getClassName());
                viewClass.putExtra(ViewClassActivity.EXTRA_ID, newClass.getId());
                viewClass.putExtra(ViewClassActivity.EXTRA_B1, newClass.getB1Notes());
                viewClass.putExtra(ViewClassActivity.EXTRA_B2, newClass.getB2Notes());
                viewClass.putExtra(ViewClassActivity.EXTRA_B3, newClass.getB3Notes());
                startActivity(viewClass);
            }
        });


        floatingActionButton = findViewById(R.id.classes_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(ClassesActivity.this);
                View promptView = li.inflate(R.layout.create_class_prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClassesActivity.this, R.style.MyDialogTheme);
                alertDialogBuilder.setView(promptView);
                classEdit = promptView.findViewById(R.id.create_class);
                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveClass();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                //Initialising Alert Dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
                    classViewModel.getAllClasses().observe(ClassesActivity.this, new Observer<List<NewClass>>() {
                        @Override
                        public void onChanged(List<NewClass> newClasses) {
                            adapts.setClasses(newClasses);


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

    //Function to save a new Class to roomDatabase
    private void saveClass() {
        String classTitle = classEdit.getText().toString().trim();

        if (classTitle.trim().isEmpty()) {
            Toast.makeText(ClassesActivity.this, "Please enter a class name", Toast.LENGTH_SHORT).show();
            return;
        } else {
            NewClass newClass = new NewClass(classTitle, b1, b2, b3);
            classViewModel.insert(newClass);
            Toast.makeText(ClassesActivity.this, "New Class Saved", Toast.LENGTH_SHORT).show();
        }


    }



    //Function to get text from searchView and show suggestions from room database
    private void getNotesFromDb(String searchText) {
        searchText = "%" + searchText + "%";
        classViewModel.getAllClassesFromSearch(searchText).observe(ClassesActivity.this, new Observer<List<NewClass>>() {
            @Override
            public void onChanged(List<NewClass> newClasses) {
                adapts.setClasses(newClasses);

            }
        });
    }


}
