package com.iut2.bestrongtimer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Cycle.CycleDAO;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;
import com.iut2.bestrongtimer.db.Entity.Sequence.SequenceDAO;
import com.iut2.bestrongtimer.db.Entity.Training.Training;
import com.iut2.bestrongtimer.db.Entity.Training.TrainingDAO;

@Database(entities = {Training.class, Sequence.class, Cycle.class}, version = 1, exportSchema = false)
@TypeConverters({TimestampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TrainingDAO trainingDAO();
    public abstract SequenceDAO sequenceDAO();
    public abstract CycleDAO cycleDAO();
}
