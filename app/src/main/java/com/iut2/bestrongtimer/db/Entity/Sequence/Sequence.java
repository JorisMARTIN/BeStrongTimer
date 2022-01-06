package com.iut2.bestrongtimer.db.Entity.Sequence;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sequence implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "training_id")
    private long trainingId;

    @ColumnInfo(name = "pos")
    private int pos;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "difficulty")
    private int difficulty;

    @ColumnInfo(name = "repetition")
    private int repetition;

    @ColumnInfo(name = "recovery_time")
    private long recoveryTime;

    @ColumnInfo(name = "global_cycle_time")
    private long globalCycleTime;

    public Sequence(long trainingId, int pos, String name, String description, int difficulty, int repetition, long recoveryTime, long globalCycleTime) {
        this.trainingId = trainingId;
        this.pos = pos;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.repetition = repetition;
        this.recoveryTime = recoveryTime;
        this.globalCycleTime = globalCycleTime;
    }

    protected Sequence(Parcel in) {
        id = in.readLong();
        trainingId = in.readLong();
        pos = in.readInt();
        name = in.readString();
        description = in.readString();
        difficulty = in.readInt();
        repetition = in.readInt();
        recoveryTime = in.readLong();
        globalCycleTime = in.readLong();
    }

    public static final Creator<Sequence> CREATOR = new Creator<Sequence>() {
        @Override
        public Sequence createFromParcel(Parcel in) {
            return new Sequence(in);
        }

        @Override
        public Sequence[] newArray(int size) {
            return new Sequence[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
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

    public long getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(long recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public long getGlobalCycleTime() {
        return globalCycleTime;
    }

    public void setGlobalCycleTime(long globalCycleTime) {
        this.globalCycleTime = globalCycleTime;
    }

    @Override
    public String toString() {
        return "Sequence{" + "\n\t" +
                "id=" + id + "\n\t" +
                ", trainingId=" + trainingId + "\n\t" +
                ", pos=" + pos + "\n\t" +
                ", name='" + name + '\'' + "\n\t" +
                ", description='" + description + '\'' + "\n\t" +
                ", difficulty=" + difficulty + "\n\t" +
                ", repetition=" + repetition + "\n\t" +
                ", recoveryTime=" + recoveryTime + "\n\t" +
                ", globalCycleTime=" + globalCycleTime + "\n" +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(trainingId);
        dest.writeInt(pos);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(difficulty);
        dest.writeInt(repetition);
        dest.writeLong(recoveryTime);
        dest.writeLong(globalCycleTime);
    }
}
