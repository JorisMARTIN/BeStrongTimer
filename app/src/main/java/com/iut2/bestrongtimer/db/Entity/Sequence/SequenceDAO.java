package com.iut2.bestrongtimer.db.Entity.Sequence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.iut2.bestrongtimer.db.Entity.SequenceCycle;

import java.util.List;

@Dao
public interface SequenceDAO {

    @Query("SELECT * FROM sequence")
    List<Sequence> getAll();

    @Query("SELECT COUNT(*) FROM sequence")
    int count();

    @Query("SELECT * FROM sequence WHERE id = (:id) LIMIT 1")
    Sequence getSequenceById(int id);

    @Insert
    void insert(Sequence sequence);

    @Delete
    void delete(Sequence sequence);

    @Update
    void update(Sequence sequence);

    @Transaction
    @Query("SELECT * FROM sequence")
    List<SequenceCycle> getCycles();

}
