package com.rudrik.noteapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "folder")
public class Folder implements Serializable {

    public static List<Folder> myFolders = new ArrayList<>();

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "folder_name")
    public String fName;

    public Folder(int id, String fName) {
        this.id = id;
        this.fName = fName;
    }

    @Ignore
    public Folder(String fName) {
        this.fName = fName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", fName='" + fName + '\'' +
                '}';
    }
}
