package com.eararchitect.ynote;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity Class for creating a class in the classes Room database

@Entity(tableName = "class_table")
public class NewClass {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "class_name")
    private String className;

    @ColumnInfo(name = "b1_notes")
    private int b1Notes;

    @ColumnInfo(name = "b2_notes")
    private int b2Notes;

    @ColumnInfo(name = "b3_notes")
    private int b3Notes;

    public NewClass(String className, int b1Notes, int b2Notes, int b3Notes) {
        this.className = className;
        this.b1Notes = b1Notes;
        this.b2Notes = b2Notes;
        this.b3Notes = b3Notes;
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

    public int getB1Notes() {
        return b1Notes;
    }

    public int getB2Notes() {
        return b2Notes;
    }

    public int getB3Notes() {
        return b3Notes;
    }
}
