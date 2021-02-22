package com.eararchitect.ynote;


import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
//Entity Class for creating a B2 Note Table

@Entity(tableName = "b3Note_table")
public class B3Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "class_name")
    private String className;
    private String title;
    private String duration;

    @ColumnInfo(name = "image_uri")
    private String imageUri;
    private String description;
    @ColumnInfo(name = "recorded_file")
    private String recordedFile;

    public B3Note(String className,String title,String duration, String imageUri,String description,String recordedFile){
        this.className = className;
        this.description = description;
        this.duration = duration;
        this.title = title;
        this.imageUri = imageUri;
        this.recordedFile = recordedFile;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getDescription() {
        return description;
    }

    public String getRecordedFile() {
        return recordedFile;
    }
}
