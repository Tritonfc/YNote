package com.eararchitect.ynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PeopleActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference peopleRef = db.collection("Users");
    private Toolbar toolbar;
    private String id;
    private FloatingActionButton floatingActionButton;
    private RecyclerView peopleRecycler;
    private PeopleAdapter peopleAdapter;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        toolbar = findViewById(R.id.people_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("People");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        peopleRecycler = findViewById(R.id.people_recycler);

        floatingActionButton = findViewById(R.id.people_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPeople = new Intent(PeopleActivity.this, AddPeopleActivity.class);
                startActivity(addPeople);
            }
        });

        setUpRecyclerView();
    }


    //Function to setUp people from firebase to
    private void setUpRecyclerView() {
        Query query = peopleRef.document(currentUser).collection("Added People");

        FirestoreRecyclerOptions<SearchModel> options = new FirestoreRecyclerOptions.Builder<SearchModel>()
                .setQuery(query, SearchModel.class)
                .build();

        peopleAdapter = new PeopleAdapter(options);
        peopleRecycler.setHasFixedSize(true);
        peopleRecycler.setLayoutManager(new LinearLayoutManager(PeopleActivity.this));
        peopleRecycler.setAdapter(peopleAdapter);


        peopleAdapter.setOnItemClickListener(new PeopleAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                if (documentSnapshot.exists()) {
                    id = documentSnapshot.getId();
                    LayoutInflater li = LayoutInflater.from(PeopleActivity.this);
                    View promptView = li.inflate(R.layout.remove_prompt, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PeopleActivity.this, R.style.MyDialogTheme);
                    alertDialogBuilder.setView(promptView);
                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    removePerson(id);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();

                                }
                            });

                    //Initialising Alert Dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            }


        });


    }


    @Override
    public void onStart() {
        super.onStart();
        peopleAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        peopleAdapter.stopListening();

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

    private void removePerson(String id) {
        peopleRef.document(currentUser).collection("Added People").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PeopleActivity.this,"That person has been removed",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}