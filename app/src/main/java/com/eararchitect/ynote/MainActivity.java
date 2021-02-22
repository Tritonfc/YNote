package com.eararchitect.ynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.services.drive.Drive;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private ImageView calendar, settings;
    private static final String TAG = "An error occurred";
    private static final String KEY_USER = "userName";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_PHOTO = "userPic";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    public static final int RC_SIGN_IN = 2;
    private String clientId = "981431721096-cmbufes3rioj71eumif1hr8sr7cdeku6.apps.googleusercontent.com";
    private Button signIn;
    private RecyclerView homePageRecycler;
    private CircularImageView people, announcement, account;
    private FloatingActionButton floatingActionButton;
    private DriveServiceHelper mDriveServiceHelper;
    private List<HomePageModel> items;
    private ClassViewModel classViewModel;

    private int b1 = 0;
    private int b2 = 0;
    private int b3 = 0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText classEdit, search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setting up the Toolbar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);


        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.settings_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.premium:
                                if (mAuth.getCurrentUser()!=null) {
                                    Intent premium = new Intent(MainActivity.this,PremiumNotesActivity.class);
                                    startActivity(premium);

                                } else {
                                    Toast.makeText(MainActivity.this, "Please login to gain access to premium notes", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case R.id.export:
                                Intent export = new Intent(MainActivity.this, B1NotesActivity.class);
                                startActivity(export);

                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        search = findViewById(R.id.search_bar);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    final String set = search.getText().toString().trim();
                    Intent startSearch = new Intent(MainActivity.this, MainActivitySearch.class);
                    startSearch.putExtra("Searched", set);
                    startSearch.putExtra(MainActivitySearch.EXTRA_TEXT, set);
                    startActivity(startSearch);

                    return true;
                }
                return false;
            }
        });

        calendar = findViewById(R.id.calendar);
        mAuth = FirebaseAuth.getInstance();

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calendarView = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(calendarView);
            }
        });
        account = findViewById(R.id.account);
        classViewModel = new ViewModelProvider(MainActivity.this).get(ClassViewModel.class);
        if (user != null) {
            Picasso.get()
                    .load(user.getPhotoUrl())
                    .into(account);

        }


        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    showLogOutDialog(user);

                } else {
                    showLoginDialog();

                }
            }
        });

        //Setting up the People Button
        people = findViewById(R.id.people);
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if user is signed in
                if (mAuth.getCurrentUser() != null) {
                    Intent people = new Intent(MainActivity.this, PeopleActivity.class);
                    startActivity(people);
                } else {

                }
            }
        });
        announcement = findViewById(R.id.announce);
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {

                    Intent announce = new Intent(MainActivity.this, AnnouncementsActivity.class);
                    startActivity(announce);

                } else {


                }
            }
        });

        //Setting up Floating Action Button
        floatingActionButton = findViewById(R.id.home_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating PopupMenu for floating Action Button
                //Menu used for the popup can be found in the menu directory in resource folder
                final PopupMenu popupMenu = new PopupMenu(MainActivity.this, floatingActionButton);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.floating_button_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.new_class:
                                //Creates Alert Dialog for Creating a new class
                                //The dialog Theme used for the alert dialog can be found and modified in the styles folder
                                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                                View promptView = li.inflate(R.layout.create_class_prompt, null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
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


                                return true;

                            case R.id.b1_note:
                                Intent addB1 = new Intent(MainActivity.this, AddB1Note.class);
                                startActivity(addB1);
                                return true;

                            case R.id.b2_note:
                                Intent addB2 = new Intent(MainActivity.this, AddB2Note.class);
                                startActivity(addB2);
                                return true;

                            case R.id.b3_note:
                                Intent addB3 = new Intent(MainActivity.this, AddB3Note.class);
                                startActivity(addB3);
                                return true;

                            default:
                                return false;

                        }
                    }
                });
                popupMenu.show();

            }
        });


        //Setting up the recyclerView for Homepage
        homePageRecycler = findViewById(R.id.home_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        homePageRecycler.setLayoutManager(gridLayoutManager);
        //Initialising the Method To setup HomePage Menu
        setUpHomePageMenu();


    }

    //Initiate the google signIN
    private void SignIn() {
        Intent signIn = googleSignInClient.getSignInIntent();
        startActivityForResult(signIn, RC_SIGN_IN);
    }

    //Method to SetUp the HomePage Elements
    private void setUpHomePageMenu() {
        items = new ArrayList<>();
        items.add(new HomePageModel("Your Classes", R.drawable.class_image));
        items.add(new HomePageModel("B1 Notes", R.drawable.bnotes));
        items.add(new HomePageModel("B2 Notes", R.drawable.bnotes));
        items.add(new HomePageModel("B3 Notes", R.drawable.bnotes));



        HomePageAdapter homePageAdapter = new HomePageAdapter(items);
        homePageRecycler.setAdapter(homePageAdapter);

        //OnClick listener for selecting menu items based on their position in the list
        homePageAdapter.setOnItemClickListener(new HomePageAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, ImageView v) {
                if (position == 0) {
                    Intent classes = new Intent(MainActivity.this, ClassesActivity.class);
                    startActivity(classes);

                } else if (position == 1) {
                    Intent b1 = new Intent(MainActivity.this, B1NotesActivity.class);
                    startActivity(b1);

                } else if (position == 2) {
                    Intent b2 = new Intent(MainActivity.this, B2NotesActivity.class);
                    startActivity(b2);

                } else if (position == 3) {
                    Intent b3 = new Intent(MainActivity.this, B3NotesActivity.class);
                    startActivity(b3);


                }

            }
        });


    }

    //Function to save new Class in database

    private void saveClass() {
        String classTitle = classEdit.getText().toString().trim();

        if (classTitle.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a class name", Toast.LENGTH_SHORT).show();
            return;
        } else {
            NewClass newClass = new NewClass(classTitle, b1, b2, b3);
            classViewModel.insert(newClass);
            Intent classActivity = new Intent(MainActivity.this, ClassesActivity.class);
            startActivity(classActivity);
            Toast.makeText(this, "New Class Saved", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                // ...
            }
        } else {

        }
    }


    //Function to authenticate user into firebase with google and save details in firestore
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                String userPhoto = String.valueOf(user.getPhotoUrl());
                                userRef.document(uid).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    Picasso.get()
                                                            .load(user.getPhotoUrl())
                                                            .into(account);
                                                    account.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            showLogOutDialog(user);
                                                        }
                                                    });


                                                } else {
                                                    Map<String, Object> person = new HashMap<>();
                                                    person.put(KEY_USER, user.getDisplayName());
                                                    person.put(KEY_PHOTO, userPhoto);
                                                    person.put(KEY_USER_ID, user.getUid());
                                                    userRef.document(uid).set(person)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Picasso.get()
                                                                            .load(user.getPhotoUrl())
                                                                            .into(account);


                                                                }


                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });


                                                }
                                            }
                                        });
                            }
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }

                        // ...
                    }
                });
    }


    //Function to show log out page after user has signed in
    private void showLogOutDialog(final FirebaseUser firebaseUser) {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptView = li.inflate(R.layout.logged_in, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        CircularImageView userPic = promptView.findViewById(R.id.userPic);
        TextView userName = promptView.findViewById(R.id.userName);
        alertDialogBuilder.setView(promptView);
        Picasso.get()
                .load(firebaseUser.getPhotoUrl())
                .into(userPic);

        userName.setText(firebaseUser.getDisplayName());
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (firebaseUser != null) {
                            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseAuth.getInstance().signOut();
                                    Picasso.get()
                                            .load(R.drawable.account)
                                            .into(account);
                                    account.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            showLoginDialog();
                                        }
                                    });


                                }
                            });

                        }
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        //Initialising Alert Dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void showLoginDialog() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptView = li.inflate(R.layout.login_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        alertDialogBuilder.setView(promptView);
        signIn = promptView.findViewById(R.id.google_sign);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();

            }
        });
        alertDialogBuilder.setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        //Initialising Alert Dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}