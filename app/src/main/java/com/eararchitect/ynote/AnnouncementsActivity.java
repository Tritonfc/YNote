package com.eararchitect.ynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnouncementsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY_USER = "userName";
    private static final String KEY_ANNOUNCEMENTS = "Announcements";
    private static final String KEY_PHOTO = "userPic";
    private static final String KEY_ANNOUNCEMENT = "announcement";
    private CollectionReference userRef = db.collection("Users");
    private String currentUser;
    private String announcement;
    private RecyclerView announceRecycler;
    private AnnouncementAdapter announcementAdapter;

    private EditText announce_edit;
    private String userName, userPhoto;

    //ArryList to get all the users added people at runtime
    private ArrayList<String> peopleIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser = user.getUid();
            userName = user.getDisplayName();
            userPhoto = String.valueOf(user.getPhotoUrl());

            userRef.document(currentUser).collection("Added People").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                //Load all the ids added people from firebase to the arrayList
                                peopleIds.add(documentSnapshot.getId());
                            }
                        }
                    });
        }

        announceRecycler = findViewById(R.id.announcement_recycler);
        setUpRecycler();


        toolbar = findViewById(R.id.announce_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Announcements");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.announcement_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(AnnouncementsActivity.this);
                View promptView = li.inflate(R.layout.announcement_prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AnnouncementsActivity.this, R.style.MyDialogTheme);
                announce_edit = promptView.findViewById(R.id.announce_edit);

                alertDialogBuilder.setView(promptView);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                announcement = announce_edit.getText().toString().trim();
                                if (announcement.trim().isEmpty()) {
                                    Toast.makeText(AnnouncementsActivity.this, "Field Cannot be empty", Toast.LENGTH_SHORT).show();

                                } else {
                                    //Save announcement into user document
                                    Map<String, Object> announce = new HashMap<>();
                                    announce.put(KEY_ANNOUNCEMENT, announcement);
                                    announce.put(KEY_PHOTO, userPhoto);
                                    announce.put(KEY_USER, userName);
                                    userRef.document(currentUser).collection(KEY_ANNOUNCEMENTS).add(announce)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    //Get all the users added people and add the announcement to their document
                                                    for (int i = 0; i < peopleIds.size(); i++) {
                                                        userRef.document(peopleIds.get(i)).collection(KEY_ANNOUNCEMENTS).add(announce);
                                                    }

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                //Initialising Alert Dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }


    //Set up recylcer view with announcements from firebase with firestore recycler library
    private void setUpRecycler() {
        Query query = userRef.document(currentUser).collection(KEY_ANNOUNCEMENTS);

        FirestoreRecyclerOptions<Announcement> options = new FirestoreRecyclerOptions.Builder<Announcement>()
                .setQuery(query, Announcement.class)
                .build();

        announcementAdapter = new AnnouncementAdapter(options);
        announceRecycler.setHasFixedSize(true);
        announceRecycler.setLayoutManager(new LinearLayoutManager(AnnouncementsActivity.this));
        announceRecycler.setAdapter(announcementAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        announcementAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        announcementAdapter.stopListening();

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