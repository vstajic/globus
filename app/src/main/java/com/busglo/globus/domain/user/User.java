package com.busglo.globus.domain.user;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "biography")
    private String bio;

    @ColumnInfo(name = "company")
    private String company;

    @ColumnInfo(name = "job_title")
    private String title;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "date_created")
    private long timestamp;

    public User(String uuid, String name, String bio, String company, String title, String avatar) {
        this.uuid = uuid;
        this.name = name;
        this.bio = bio;
        this.company = company;
        this.title = title;
        this.avatar = avatar;
        this.timestamp = new Date().getTime();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}


