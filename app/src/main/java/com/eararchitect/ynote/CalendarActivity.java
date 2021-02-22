package com.eararchitect.ynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private TextView dateView;
    private CalendarView calendarView;
    private FloatingActionButton floatingActionButton;
    private ScheduleViewModel scheduleViewModel;
    private RecyclerView scheduleRecycler;
    private String date;
    private Calendar calendar;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        scheduleRecycler = findViewById(R.id.schedule_recycler);
        scheduleRecycler.setLayoutManager(new LinearLayoutManager(CalendarActivity.this));
        scheduleRecycler.setHasFixedSize(true);



        final ScheduleAdapter adapts = new ScheduleAdapter();
        scheduleRecycler.setAdapter(adapts);

        scheduleViewModel = new ViewModelProvider(CalendarActivity.this).get(ScheduleViewModel.class);

        dateView = findViewById(R.id.date_view);
        calendarView = findViewById(R.id.calendar);
        floatingActionButton = findViewById(R.id.calendar_floating);


        calendar = Calendar.getInstance();
        //Get current date and display it in textView
        sdf = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
        date = sdf.format(calendar.getTime());
        dateView.setText(date);
        scheduleViewModel.getAllSchedulesForDate(date).observe(CalendarActivity.this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                //Get schedules for that particular date and display in textView
                adapts.setSchedules(schedules);
            }
            });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startTask = new Intent(CalendarActivity.this, ScheduleClassActivity.class);
                startTask.putExtra(ScheduleClassActivity.EXTRA_DATE, date);
                startActivity(startTask);

            }
        });



        //Get date selected in calendar and display the schedules
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "-" + (month + 1) + "-" + year;
                dateView.setText(date);

                scheduleViewModel.getAllSchedulesForDate(date).observe(CalendarActivity.this, new Observer<List<Schedule>>() {
                    @Override
                    public void onChanged(List<Schedule> schedules) {
                        adapts.setSchedules(schedules);

                    }
                });


            }
        });
    }
}