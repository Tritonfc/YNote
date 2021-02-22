package com.eararchitect.ynote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleClassActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String EXTRA_DATE = "Date";
    private TextView startTime, endTime;
    private ScheduleViewModel scheduleViewModel;
    private TimePicker timePicker;
    private Spinner spinner;
    private String date;
    private String className;
    private String startT, endT;

    private Button done;
    private ClassViewModel classViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_class);

        Intent intent = getIntent();

        //Get selected date from calendar in intent
        if (intent.hasExtra(EXTRA_DATE)) {
            date = intent.getStringExtra(EXTRA_DATE);
        }


        classViewModel = new ViewModelProvider(ScheduleClassActivity.this).get(ClassViewModel.class);
        scheduleViewModel = new ViewModelProvider(ScheduleClassActivity.this).get(ScheduleViewModel.class);

        spinner = findViewById(R.id.spin);
        setUpSpinner();

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleClass();
            }
        });

        toolbar = findViewById(R.id.schedule_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schedule a class");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(ScheduleClassActivity.this);
                View promptView = li.inflate(R.layout.time_picker_prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduleClassActivity.this, R.style.MyDialogTheme);

                alertDialogBuilder.setView(promptView);
                //Set up time picker to get time picked
                timePicker = promptView.findViewById(R.id.timePicker);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int hourOfDay = timePicker.getHour();
                                int minute = timePicker.getMinute();

                                endTime.setText(hourOfDay + ":" + minute);
                                endT = (hourOfDay + ":" + minute);


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(ScheduleClassActivity.this);
                View promptView = li.inflate(R.layout.time_picker_prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduleClassActivity.this, R.style.MyDialogTheme);

                alertDialogBuilder.setView(promptView);
                timePicker = promptView.findViewById(R.id.timePicker);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int hourOfDay = timePicker.getHour();
                                int minute = timePicker.getMinute();

                                startTime.setText(hourOfDay + ":" + minute);
                                startT = (hourOfDay + ":" + minute);


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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

   //Set up spinner to include already created classes
    private void setUpSpinner() {

        final List<NewClass> item = new ArrayList<>();

        final SpinnerAdapter adapt = new SpinnerAdapter(getApplicationContext(), item);
        spinner.setAdapter(adapt);


        classViewModel.getAllClasses().observe(this, new Observer<List<NewClass>>() {
            @Override
            public void onChanged(final List<NewClass> newClasses) {

                adapt.setClasses(newClasses);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        className = newClasses.get(i).getClassName();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });


    }

    private void scheduleClass() {

        Schedule schedule = new Schedule(className, startT, endT, date);
        scheduleViewModel.insert(schedule);
        Toast.makeText(ScheduleClassActivity.this,"New Class Scheduled",Toast.LENGTH_SHORT).show();
        Intent calendar = new Intent(ScheduleClassActivity.this,CalendarActivity.class);
        finish();
        startActivity(calendar);


    }

}