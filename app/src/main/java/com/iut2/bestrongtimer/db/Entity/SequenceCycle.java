package com.iut2.bestrongtimer.db.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SequenceCycle {

    @Embedded public Sequence sequence;
    @Relation(
            parentColumn = "id",
            entityColumn = "sequence_id"
    )
    public List<Cycle> cycles;

    public List<Cycle> getSortedCycles() {
        List<Cycle> cycles = this.cycles;

        Collections.sort(cycles, new Comparator<Cycle>() {
            @Override
            public int compare(Cycle c1, Cycle c2) {
                return c1.getPos() - c2.getPos();
            }
        });

        return cycles;
    }

    @Override
    public String toString() {
        return "SequenceCycle{" + "\n\t" +
                "sequence=" + sequence + "\n\t" +
                ", cycles=" + cycles + "\n" +
                '}';
    }
}
