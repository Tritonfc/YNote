package com.eararchitect.ynote;


//Entity Class for creating a B1 Note Table


import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

@Entity(tableName = "b1Note_table")
public class B1Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "class_name")
    private String className;
    private String title;
    private String duration;
    @ColumnInfo(name = "image_uris")
    @TypeConverters({Converter.class})
    private ArrayList<String> imageUris;
    private String description;
    private String correction;
    private String caution;
    @ColumnInfo(name = "other_notes")
    private String otherNotes;
    @ColumnInfo(name = "recorded_file")
    private String recordedFile;

    public B1Note(String className, String title, String duration, ArrayList<String> imageUris, String description, String correction, String caution, String otherNotes, String recordedFile) {
        this.className = className;
        this.title = title;
        this.duration = duration;
        this.imageUris = imageUris;
        this.description = description;
        this.correction = correction;
        this.caution = caution;
        this.otherNotes = otherNotes;
        this.recordedFile = recordedFile;
    }

    public String getClassName() {
        return className;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getDuration() {
        return duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getImageUris() {
        return imageUris;
    }

    public String getDescription() {
        return description;
    }

    public String getCorrection() {
        return correction;
    }

    public String getCaution() {
        return caution;
    }

    public String getOtherNotes() {
        return otherNotes;
    }

    public String getRecordedFile() {
        return recordedFile;
    }
}
