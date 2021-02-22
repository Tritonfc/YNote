package com.eararchitect.ynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class B1NoteViewer extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String EXTRA_ID = "ID";
    private B1NotesViewModel b1NotesViewModel;
    private ViewPager thumb;
    private int noteId;
    private String recordFile;
    private TextView className, description, correction, caution, others, duration;


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
        setContentView(R.layout.activity_b1_note_viewer);

        className = findViewById(R.id.class_textView);
        description = findViewById(R.id.description_text);
        duration = findViewById(R.id.duration_view);
        correction = findViewById(R.id.correction_text);
        caution = findViewById(R.id.caution_text);
        others = findViewById(R.id.other_text);
        thumb = findViewById(R.id.thumb);
        mute = findViewById(R.id.mute_button);


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


        b1NotesViewModel = new ViewModelProvider(B1NoteViewer.this).get(B1NotesViewModel.class);

        Intent intent = getIntent();

        toolbar = findViewById(R.id.view_b1_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get id of note from intent to get item from room database
        if (intent.hasExtra(EXTRA_ID)) {
            noteId = intent.getIntExtra(EXTRA_ID, 1);
            b1NotesViewModel.getB1Note(noteId).observe(this, new Observer<B1Note>() {
                @Override
                public void onChanged(B1Note b1Note) {
                    getSupportActionBar().setTitle(b1Note.getClassName());
                    className.setText(b1Note.getTitle());
                    duration.setText(b1Note.getDuration());
                    recordFile = b1Note.getRecordedFile();
                    description.setText(b1Note.getDescription());
                    correction.setText(b1Note.getCorrection());
                    caution.setText(b1Note.getCaution());
                    others.setText(b1Note.getOtherNotes());
                    //ViewPager to show multiple images in a slide format
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(B1NoteViewer.this,b1Note.getImageUris());
                    thumb.setAdapter(viewPagerAdapter);
                }
            });

        }
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

    //Method for back button on ToolBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        isStopped = true;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }


    //Function to mute audio
    private void muteAudio() {
        mediaPlayer.setVolume(0, 0);
        mute.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.mute_audio, null));
        isMuted = true;
    }


    //Function to unmute audio
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