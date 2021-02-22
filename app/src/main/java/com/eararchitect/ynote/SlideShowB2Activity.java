package com.eararchitect.ynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlideShowB2Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView thumb;
    private int noteId;
    public static final String EXTRA_ID = "ID";
    private String recordFile;
    private B2NotesViewModel b2NotesViewModel;
    private TextView className, title;
    private ArrayList<B2Note> b2Notes = new ArrayList<>();
    private Chronometer duration;


    //Playing Audio
    private ImageButton playBtn, stopBtn, mute;
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;
    private MediaPlayer mediaPlayer = null;
    private boolean isStopped = false;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private boolean isMuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_b2);
        b2NotesViewModel = new ViewModelProvider(SlideShowB2Activity.this).get(B2NotesViewModel.class);
        b2NotesViewModel.getAllB2Notes().observe(this, new Observer<List<B2Note>>() {
            @Override
            public void onChanged(List<B2Note> b2Notes) {
                b2Notes.addAll(b2Notes);


            }
        });
        Intent intent = getIntent();
        //Setting up the toolbar
        toolbar = findViewById(R.id.slide_show_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SlideShow");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        className = findViewById(R.id.class_textView);
        title = findViewById(R.id.title_textView);
        duration = findViewById(R.id.duration_view);
        thumb = findViewById(R.id.thumb);
        mute = findViewById(R.id.mute_button);


        if (intent.hasExtra(EXTRA_ID)) {
            noteId = intent.getIntExtra(EXTRA_ID, 1);
            b2NotesViewModel.getB2Note(noteId).observe(this, new Observer<B2Note>() {
                @Override
                public void onChanged(B2Note bNote) {

                    className.setText(bNote.getClassName());
                    title.setText(bNote.getTitle());
                    recordFile = bNote.getRecordedFile();
                    Picasso.get()
                            .load(bNote.getImageUri())
                            .into(thumb);
                    playAudio();
                }
            });

        }


        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    if (isMuted) {
                        unMuteAudio();
                    } else {
                        muteAudio();
                    }
                } else {
                    //Do nothing
                }

            }
        });


        playerSeekbar = findViewById(R.id.player_seekbar);
        //Initialising the play button
        playBtn = findViewById(R.id.player_play_btn);
        stopBtn = findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStopped) {

                } else if (isPaused) {
                    stopAudio();
                } else if (isPlaying) {
                    stopAudio();
                }
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStopped) {
                    playAudio();
                    //Do nothing

                } else if (recordFile == null) {
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
    }

    private void showNextNote() {
        if (noteId < b2Notes.size()) {
            noteId = noteId + 1;
            b2NotesViewModel.getB2Note(noteId).observe(SlideShowB2Activity.this, new Observer<B2Note>() {
                @Override
                public void onChanged(B2Note b2Note) {
                    className.setText(b2Note.getClassName());
                    title.setText(b2Note.getTitle());
                    recordFile = b2Note.getRecordedFile();
                    Picasso.get()
                            .load(b2Note.getImageUri())
                            .into(thumb);
                    playAudio();

                }
            });
        } else {
            Intent exit = new Intent(SlideShowB2Activity.this, B3NotesActivity.class);
            startActivity(exit);
            finish();
        }
    }

    // function to pause the audio
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
        isStopped = true;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void muteAudio() {
        mediaPlayer.setVolume(0, 0);
        mute.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mute_audio, null));
        isMuted = true;
    }

    private void unMuteAudio() {
        mediaPlayer.setVolume(1, 1);
        mute.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.unmute_button, null));
        isMuted = false;
    }

    private void playAudio() {
        //Initialising mediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            //Starting media Player
            mediaPlayer.setDataSource(recordFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.player_pause_btn, null));


        //Play the audio
        isPlaying = true;
        isStopped = false;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Stop audio after playback is complete
                stopAudio();
                showNextNote();


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
}