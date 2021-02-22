package com.eararchitect.ynote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;


//I apologise for the amount of IF statements used in the code below


public class AddB1Note extends AppCompatActivity {
    //UI ELEMENTS
    private Toolbar toolbar;
    private Spinner spin;
    private ClassViewModel classViewModel;
    private B1NotesViewModel b1NotesViewModel;
    private Button save;

    //Audio Record Elements
    private ImageButton record_button;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;
    private String recordFile;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer = null;
    private String recordPath;

    private ImageButton playBtn;
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    //Duration Picker
    private NumberPicker hourPicker, minutePicker;
    private Chronometer timer;
    private int hours, minutes;
    private Button setDuration;


    //ImageView and picker Elements

    private ArrayList<ImageView> imageViews;
    private ImageView pick;
    private static final int GALLERY_REQUEST = 9;
    private static final int CAMERA_REQUEST = 11;
    private LinearLayout imagePick;
    public static final int REQUEST_GET_SINGLE_FILE = 3;
    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 2;
    int position = -1;

    //Edittexts
    private EditText descriptionEdit;
    private EditText titleEdit;
    private EditText cautionEdit;
    private EditText otherNotesEdit;
    private EditText correctionEdit;

    //Saving note parameters
    private String title;
    private String description;
    private String caution;
    private String otherNotes;
    private String correction;
    private ArrayList<String> photos;
    private ArrayList<Bitmap> pickedImages;
    private String totalTime;
    private String recordedFile = "";
    private String className;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_b1_note);
        //Setting up the toolbar
        toolbar = findViewById(R.id.add_b1_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add B1 Note");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        classViewModel = new ViewModelProvider(AddB1Note.this).get(ClassViewModel.class);


        //Initialisation of arrayList to hold photos picked from gallery
        photos = new ArrayList<>();
        pickedImages = new ArrayList<>();

        playerSeekbar = findViewById(R.id.player_seekbar);
        //Initialising the play button
        playBtn = findViewById(R.id.player_play_btn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    //Do nothing

                } else if (recordedFile == null) {
                    //Do nothing
                } else {
                    if (isPlaying) {
                        //Pause Audio since audio is playing
                        pauseAudio();
                    } else if (isPaused) {
                        //Resume audio since audio is paused
                        resumeAudio();
                    } else {
                        //Play Audio
                        playAudio();
                    }
                }
            }
        });
        //Setting up spinner to display users classes
        spin = findViewById(R.id.spin);
        setUpSpinner();


        //Implementing the seekBar
        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            //Pause audio while user uses seekBar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            //Resume audio from point user stops using seekBar
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });

        //Initialising Edittexts
        titleEdit = findViewById(R.id.title_edit);
        descriptionEdit = findViewById(R.id.description_edit);
        cautionEdit = findViewById(R.id.caution_edit);
        correctionEdit = findViewById(R.id.correction_edit);
        otherNotesEdit = findViewById(R.id.other_edit);


        //Initialising arrayLists to save ImageView AND ImageUri
        photos = new ArrayList<>();
        imageViews = new ArrayList<>();

        //setting up chronometer
        timer = findViewById(R.id.record_timer);


        imagePick = findViewById(R.id.pick_images);
        //setting up ImageView for picking images
        pick = findViewById(R.id.add_pic);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddB1Note.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddB1Note.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORAGE);


                } else {
                    getImageFromGallery();
                }
            }
        });

        //Setting up Number Picker For duration
        hourPicker = findViewById(R.id.duration_edit_1);

        //Initialising button to set Duration
        setDuration = findViewById(R.id.set);
        setDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTotalTime();
                Toast.makeText(AddB1Note.this, "The total duration has been set", Toast.LENGTH_SHORT).show();

            }
        });
        //Setting up the number Picker
        minutePicker = findViewById(R.id.duration_edit_2);
        hourPicker.setMaxValue(99);
        hourPicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(1);
        b1NotesViewModel = new ViewModelProvider(AddB1Note.this).get(B1NotesViewModel.class);

        save = findViewById(R.id.done);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveB1Note();
            }
        });


        //Onclick listener to start recording audio
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                //Set the format of the chronomter to HOURS-MINUTES-SECONDS
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                chronometer.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s));


                String currentTime = timer.getText().toString();
                //Checking if the duration limit has been reached
                if (currentTime.equals(totalTime)) {
                    stopRecording();
                    isRecording = false;
                    record_button.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.record_btn_stopped, null));
                }
            }
        });

        //setting up the record Button
        record_button = findViewById(R.id.record_btn);
        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if an audio file is playing already
                if (isPlaying) {
                    //Do Nothing


                    //Checking if an audio file has already been recorded before
                } else if (!recordedFile.isEmpty()) {
                    LayoutInflater li = LayoutInflater.from(AddB1Note.this);
                    View promptView = li.inflate(R.layout.record_prompt, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddB1Note.this, R.style.MyDialogTheme);
                    alertDialogBuilder.setView(promptView);
                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    recordedFile = "";
                                    startRecording();

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

                } else {
                    //Checking if the mediaRecorder is currently recording audio
                    if (isRecording) {
                        //Stop Recording
                        stopRecording();
                        record_button.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.record_btn_stopped, null));
                        isRecording = false;


                    } else {
                        if (checkPermissions()) {
                            //Start Recording
                            startRecording();


                        }
                    }

                }
            }
        });


    }

    //Function to pause the audio
    private void pauseAudio() {
        mediaPlayer.pause();
        playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.player_play_btn, null));
        isPlaying = false;
        isPaused = true;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    //Function to resume audio if paused
    private void resumeAudio() {
        mediaPlayer.start();
        playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.player_pause_btn, null));
        isPlaying = true;
        isPaused = false;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    private void stopAudio() {
        //Stop The Audio
        playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.player_play_btn, null));

        isPlaying = false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    //Function to start playing recorded audio
    private void playAudio() {
        //Initialising mediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            //Starting media Player
            mediaPlayer.setDataSource(recordedFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.player_pause_btn, null));


        //Play the audio
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Stop audio after playback is complete
                stopAudio();

            }
        });
        //Get length of audio and set the seekbar accordingly
        playerSeekbar.setMax(mediaPlayer.getDuration());
        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    //Function to update position of seekBar as audio Plays
    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }


    //Stop playing audio when activity has been stopped
    @Override
    public void onStop() {
        super.onStop();
        if (isPlaying) {
            stopAudio();
        }
    }

    //Method to Check if user has granted permission to use the phone recorder
    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(AddB1Note.this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(AddB1Note.this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery();
                } else {

                }
                return;
            }
        }
    }


    //Function to show dialog for choosing gallery or camera
    private void showImageOptionDialog() {
        final String[] options = getResources().getStringArray(R.array.image_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddB1Note.this);
        builder.setTitle(R.string.alert_dialog_title)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                getImageFromGallery();
                                break;
                            case 1:
                                capturePictureFromCamera();
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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

    //Method to start the recording process
    private void startRecording() {
        if (totalTime == null) {
            Toast.makeText(this, "Please set a duration before recording", Toast.LENGTH_SHORT).show();
        } else {
            //Start timer from 0
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            mediaRecorder = new MediaRecorder();
            //Get app external directory path
            recordPath = AddB1Note.this.getExternalFilesDir("/").getAbsolutePath();
            //Get current date and time
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault());
            Date now = new Date();
            //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file
            recordFile = "Recording_" + formatter.format(now) + ".3gp";
            //Set audio source to phone mic
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // set audio format
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //set audio encoder
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(recordPath + "/" + recordFile);


            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Start Recording
            mediaRecorder.start();
            isRecording = true;
            record_button.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.record_btn_recording, null));
        }
    }

    //Method to stop the recording process
    private void stopRecording() {
        //Stop media recorder and set it to null for further use to record new audio
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        recordedFile = recordPath + "/" + recordFile;

        timer.stop();
    }

    //Function to start up camera
    private void capturePictureFromCamera() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    //Function to open image Gallery
    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }


    //Function to calculate total time in milliseconds set by user in number Picker
    private void setTotalTime() {
        hours = hourPicker.getValue();
        minutes = minutePicker.getValue();
        //Set total duration from hours and minutes input
        totalTime = String.format(Locale.getDefault(), "%02d:%02d:00", hours, minutes);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {

            //Get uri and save it for future use
            Uri selectedImage = data.getData();
            getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //Add the uri String format to the arrayList
            photos.add(selectedImage.toString());
            //Creating an imageView at runtime
            final CircularImageView circularImageView = new CircularImageView(getApplicationContext());
            final float scale = this.getResources().getDisplayMetrics().density;
            int dpASPixels = (int) (70 * scale + 0.5f);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpASPixels, dpASPixels);
            circularImageView.setLayoutParams(params);
            imagePick.addView(circularImageView);
            Picasso.get().load(selectedImage).into(circularImageView);
            imageViews.add(circularImageView);

            //On-long click listener to delete selected images
            circularImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(AddB1Note.this, circularImageView);

                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.delete_popup, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.delete:
                                    //Get position of imageView and delete it
                                    position = imageViews.indexOf(circularImageView);
                                    if (position > -1) {
                                        imageViews.remove(position);
                                        photos.remove(position);
                                        circularImageView.setVisibility(View.GONE);
                                        pick.setVisibility(View.VISIBLE);


                                    }

                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                    popupMenu.show();

                    return true;
                }
            });


            if (imageViews.size() == 4) {
                pick.setVisibility(View.GONE);

            }

                //If you want to add picture from camera functionality add it here
        } else if (requestCode == CAMERA_REQUEST && requestCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        }
    }


    //Function to set up spinner with already created classes from the room database
    private void setUpSpinner() {

        final List<NewClass> item = new ArrayList<>();

        final SpinnerAdapter adapt = new SpinnerAdapter(getApplicationContext(), item);
        spin.setAdapter(adapt);


        classViewModel.getAllClasses().observe(this, new Observer<List<NewClass>>() {
            @Override
            public void onChanged(final List<NewClass> newClasses) {

                adapt.setClasses(newClasses);
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        className = newClasses.get(i).getClassName();
                        id = newClasses.get(i).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });


    }

    //Function to save a B1 Note
    private void saveB1Note() {
        title = titleEdit.getText().toString().trim();
        description = descriptionEdit.getText().toString().trim();
        caution = cautionEdit.getText().toString().trim();
        otherNotes = otherNotesEdit.getText().toString().trim();
        correction = correctionEdit.getText().toString().trim();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a note title", Toast.LENGTH_SHORT).show();
        } else if (description.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
        } else if (className.trim().isEmpty()) {
            Toast.makeText(this, "Please choose a class", Toast.LENGTH_SHORT).show();
        } else if (recordedFile.isEmpty()) {
            Toast.makeText(this, "Please record an audio", Toast.LENGTH_SHORT).show();

        } else if (totalTime.equals(null)) {
            Toast.makeText(this, "Please set a total duration", Toast.LENGTH_SHORT).show();


        } else if (photos.isEmpty()) {
            Toast.makeText(this, "Please add at least one photo", Toast.LENGTH_SHORT).show();
        } else {
            B1Note note = new B1Note(className, title, totalTime, photos, description, correction, caution, otherNotes, recordedFile);
            b1NotesViewModel.insert(note);
            addB1Note();
            Intent b1Activity = new Intent(AddB1Note.this, B1NotesActivity.class);
            startActivity(b1Activity);
            finish();
            Toast.makeText(this, "New B1 Note Saved", Toast.LENGTH_SHORT).show();

        }


    }


    //Function to update the number of B1 note in the chosen class
    private void addB1Note() {
        classViewModel.updateB1(id);
    }
}