package com.iut2.bestrongtimer.db.Entity.Training;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.iut2.bestrongtimer.db.Entity.TrainingSequence;

import java.util.List;

@Dao
public interface TrainingDAO {

    @Query("SELECT * FROM training")
    List<Training> getAll();

    @Query("SELECT COUNT(*) FROM training")
    int count();

    @Query("SELECT * FROM training WHERE id = (:id) LIMIT 1")
    Training getTrainingById(long id);

    @Insert
    long insert(Training training);

    @Delete
    void delete(Training training);

    @Update
    void update(Training training);

    @Transaction
    @Query("SELECT * FROM training")
    List<TrainingSequence> getSequences();

    @Transaction
    @Query("SELECT * FROM training WHERE id = (:id)")
    TrainingSequence getSequencesFromTrainingId(int id);
}
