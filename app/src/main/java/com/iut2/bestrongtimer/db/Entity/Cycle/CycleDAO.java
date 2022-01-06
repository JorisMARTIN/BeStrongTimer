package com.iut2.bestrongtimer.db.Entity.Cycle;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CycleDAO {

    @Query("SELECT * FROM cycle")
    List<Cycle> getAll();

    @Query("SELECT COUNT(*) FROM cycle")
    int count();

    @Query("SELECT * FROM cycle WHERE id = (:id) LIMIT 1")
    Cycle getSequenceById(int id);

    @Insert
    long insert(Cycle cycle);

    @Delete
    void delete(Cycle cycle);

    @Update
    void update(Cycle cycle);
}
