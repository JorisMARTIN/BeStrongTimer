package com.iut2.bestrongtimer.db.Entity.Sequence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sequence {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "training_id")
    private int trainingId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "difficulty")
    private int difficulty;

    @ColumnInfo(name = "repetition")
    private int repetition;

    @ColumnInfo(name = "recovery_time")
    private int recoveryTime;

    @ColumnInfo(name = "global_cycle_time")
    private int globalCycleTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public int getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(int recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public int getGlobalCycleTime() {
        return globalCycleTime;
    }

    public void setGlobalCycleTime(int globalCycleTime) {
        this.globalCycleTime = globalCycleTime;
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "id=" + id +
                ", trainingId=" + trainingId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", difficulty=" + difficulty +
                ", repetition=" + repetition +
                ", recoveryTime=" + recoveryTime +
                ", globalCycleTime=" + globalCycleTime +
                '}';
    }
}
