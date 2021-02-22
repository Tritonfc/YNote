package com.eararchitect.ynote;


import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

//Entity Class for creating a B2 Note Table

@Entity(tableName = "b2Note_table")
public class B2Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "class_name")
    private String className;
    private String title;
    private String duration;

    @ColumnInfo(name = "image_uri")

    private String imageUri;

    @ColumnInfo(name = "recorded_file")
    private String recordedFile;

    private String definition1;
    private String duration1;

    private String definition2;
    private String duration2;

    private String definition3;
    private String duration3;

    private String definition4;
    private String duration4;

    public B2Note(String className, String title, String duration, String imageUri, String recordedFile, String definition1, String duration1, String definition2, String duration2, String definition3, String duration3, String definition4, String duration4) {
        this.className = className;

        this.duration = duration;
        this.title = title;
        this.imageUri = imageUri;
        this.recordedFile = recordedFile;
        this.definition1 = definition1;
        this.duration1 = duration1;
        this.definition2 = definition2;
        this.duration2 = duration2;
        this.definition3 = definition3;
        this.duration3 = duration3;
        this.definition4 = definition4;
        this.duration4 = duration4;
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

    public String getRecordedFile() {
        return recordedFile;
    }

    public String getDefinition1() {
        return definition1;
    }

    public String getDuration1() {
        return duration1;
    }

    public String getDefinition2() {
        return definition2;
    }

    public String getDuration2() {
        return duration2;
    }

    public String getDefinition3() {
        return definition3;
    }

    public String getDuration3() {
        return duration3;
    }

    public String getDefinition4() {
        return definition4;
    }

    public String getDuration4() {
        return duration4;
    }
}
