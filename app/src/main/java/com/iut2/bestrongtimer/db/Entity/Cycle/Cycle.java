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

    @ColumnInfo(name = "pos")
    private int pos;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "repetition")
    private int repetition;

    @ColumnInfo(name = "activity_time")
    private long activityTime;

    @ColumnInfo(name = "recovery_time")
    private long recoveryTime;

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

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
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

    public long getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(long activityTime) {
        this.activityTime = activityTime;
    }

    public long getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(long recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    @Override
    public String toString() {
        return "Cycle{" + "\n\t" +
                "id=" + id + "\n\t" +
                ", sequenceId=" + sequenceId + "\n\t" +
                ", pos=" + pos + "\n\t" +
                ", name='" + name + '\'' + "\n\t" +
                ", repetition=" + repetition + "\n\t" +
                ", activityTime=" + activityTime + "\n\t" +
                ", recoveryTime=" + recoveryTime + "\n" +
                '}';
    }
}
