package com.aboesmail.omar.pharma.Database.UserDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocalUser localUser);

    @Update
    void update(LocalUser localUser);

    @Delete
    void logeout(LocalUser localUser);

    @Query("SELECT * FROM  user_table")
    LiveData<LocalUser> find();

    @Query("SELECT * FROM user_table WHERE id = (:id)")
    LocalUser getByID(String id);

}
