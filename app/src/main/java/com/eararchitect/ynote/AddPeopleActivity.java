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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPeopleActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference searchRef = db.collection("Users");
    private Query q;
    private SearchAdapter adapter;
    private ListView lists;
    private String userUid;
    private Toolbar toolbar;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Check if a user is signed in and get the user id;
        if (user != null) {

            userUid = user.getUid();
        }


        searchBar = findViewById(R.id.search_bar);
        lists = findViewById(R.id.add_people_recycler);



        //Text changed listener to show suggestions for search
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == 0) {
                    lists.setVisibility(View.GONE);

                    setAdapter(q);
                } else {
                    lists.setVisibility(View.VISIBLE);
                    q = searchRef.orderBy("userName").startAt(s.toString().trim()).endAt(s.toString().trim() + "\uf8ff");

                    setAdapter(q);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        toolbar = findViewById(R.id.add_people_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add People");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Function to load suggestions from search bar into list
    private void setAdapter(Query q1) {
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<SearchModel> searched = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        //Add every item from result to list
                        SearchModel model = documentSnapshot.toObject(SearchModel.class);
                        searched.add(model);
                    }

                    adapter = new SearchAdapter(AddPeopleActivity.this, searched);
                    lists.setAdapter(adapter);

                    lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int item = position;
                            SearchModel model = (SearchModel) lists.getItemAtPosition(item);
                            final String userId = model.getUserId();

                            //Check is user is trying to add himself
                            if(userId.equals(userUid)){
                                Toast.makeText(AddPeopleActivity.this,"You cant add Yourself",Toast.LENGTH_SHORT).show();

                            }else {
                                LayoutInflater li = LayoutInflater.from(AddPeopleActivity.this);
                                View promptView = li.inflate(R.layout.add_prompt, null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddPeopleActivity.this, R.style.MyDialogTheme);
                                alertDialogBuilder.setView(promptView);
                                alertDialogBuilder.setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                addPerson(userId);

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                //Initialising Alert Dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        }
                    });

                }

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


    //Function to add person to the list of users added people
    private void addPerson(String id){
        DocumentReference person = db.collection("Users").document(id);
        person.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            searchRef.document(userUid).collection("Added People").document(id).set(snapshot.getData())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent added = new Intent(AddPeopleActivity.this,PeopleActivity.class);
                                            startActivity(added);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }

                    }
                });
    }
}