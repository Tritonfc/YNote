package com.eararchitect.ynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewClassActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView b1Count, b2Count, b3Count;
    private CardView cardView, cardView2, cardView3;

    public static final String EXTRA_TITLE = "className";
    public static final String EXTRA_B1 = "B1";
    public static final String EXTRA_B2 = "B2";
    public static final String EXTRA_B3 = "B3";
    private int b1, b2, b3;
    public static final String EXTRA_ID = "ID";
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        Intent intent = getIntent();


        cardView = findViewById(R.id.cardView);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);
        toolbar = findViewById(R.id.view_class_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (intent.hasExtra(EXTRA_TITLE)
        ) {
            getSupportActionBar().setTitle(intent.getStringExtra(EXTRA_TITLE));
            className = intent.getStringExtra(EXTRA_TITLE);
        }

        b1Count = findViewById(R.id.b1_count);
        b2Count = findViewById(R.id.b2_count);
        b3Count = findViewById(R.id.b3_count);

        //Getting the values of the note count of the class and displaying them
        b1 = getIntent().getIntExtra(EXTRA_B1, 1);

        b2 = getIntent().getIntExtra(EXTRA_B2, 1);
        b3 = getIntent().getIntExtra(EXTRA_B3, 1);
        b1Count.setText(String.valueOf(b1));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b1 = new Intent(ViewClassActivity.this, ClassB1NotesActivity.class);
                b1.putExtra(ClassB1NotesActivity.EXTRA_CLASS, className);
                startActivity(b1);
            }
        });
        b2Count.setText(String.valueOf(b2));
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b2 = new Intent(ViewClassActivity.this, ClassB2NotesActivity.class);
                b2.putExtra(ClassB2NotesActivity.EXTRA_CLASS, className);
                startActivity(b2);

            }
        });
        b3Count.setText(String.valueOf(b3));
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b3 = new Intent(ViewClassActivity.this, ClassB3NotesActivity.class);
                b3.putExtra(ClassB3NotesActivity.EXTRA_CLASS, className);
                startActivity(b3);

            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}