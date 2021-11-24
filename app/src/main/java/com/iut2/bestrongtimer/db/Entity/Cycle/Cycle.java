package com.iut2.bestrongtimer.db.Entity.Cycle;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cycle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "sequence_id")
    private int sequenceId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "repetition")
    private int repetition;

    @ColumnInfo(name = "activity_time")
    private int activityTime;

    @ColumnInfo(name = "recovery_time")
    private int recoveryTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public int getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(int activityTime) {
        this.activityTime = activityTime;
    }

    public int getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(int recoveryTime) {
        this.recoveryTime = recoveryTime;
    }
}
