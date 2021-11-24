package com.iut2.bestrongtimer.db.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;

import java.util.List;

public class SequenceCycle {

    @Embedded public Sequence sequence;
    @Relation(
            parentColumn = "id",
            entityColumn = "sequence_id"
    )
    public List<Cycle> cycles;

}
