package com.iut2.bestrongtimer.db.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;
import com.iut2.bestrongtimer.db.Entity.Training.Training;

import java.util.List;

public class TrainingSequence {
    @Embedded public Training training;
    @Relation(
            parentColumn = "id",
            entityColumn = "training_id"
    )
    public List<Sequence> sequences;

    @Override
    public String toString() {
        return "TrainingSequence{" + "\n\t" +
                "training=" + training + "\n\t" +
                ", sequences=" + sequences + "\n" +
                '}';
    }
}
