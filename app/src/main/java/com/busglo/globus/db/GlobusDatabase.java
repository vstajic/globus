package com.busglo.globus.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.busglo.globus.domain.user.User;
import com.busglo.globus.domain.user.UserDao;

import static com.busglo.globus.db.GlobusDatabase.DB_VERSION;

@Database(entities = User.class, version = DB_VERSION)
public abstract class GlobusDatabase extends RoomDatabase {
    public static final String DB_NAME = "Globus";
    public static final int DB_VERSION = 1;

    public abstract UserDao userDao();

}
