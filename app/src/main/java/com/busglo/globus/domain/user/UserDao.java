package com.busglo.globus.domain.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users ORDER BY datetime(date_created) DESC")
    Maybe<List<User>> findAllUsers();

    @Query("SELECT * FROM users WHERE id = :userId")
    Flowable<User> findUserById(int userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User... users);

    @Update
    void updateUser(User... users);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM users")
    void deleteAllUsers();

}
