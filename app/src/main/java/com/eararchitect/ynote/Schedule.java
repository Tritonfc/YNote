package com.eararchitect.ynote;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule_table")
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "class_name")
    private String className;

    @ColumnInfo(name = "start_time")
    private String startTime;

    private String date;

    @ColumnInfo(name = "end_time")
    private String endTime;

    public Schedule(String className,String startTime, String endTime, String date){
        this.className = className;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDate() {
        return date;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setId(int id) {
        this.id = id;
    }
}
