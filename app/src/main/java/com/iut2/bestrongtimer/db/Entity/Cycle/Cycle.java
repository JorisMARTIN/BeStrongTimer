package com.iut2.bestrongtimer.db.Entity.Cycle;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cycle implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "sequence_id")
    private long sequenceId;

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

    public Cycle(long sequenceId, int pos, String name, int repetition, long activityTime, long recoveryTime) {
        this.sequenceId = sequenceId;
        this.pos = pos;
        this.name = name;
        this.repetition = repetition;
        this.activityTime = activityTime;
        this.recoveryTime = recoveryTime;
    }

    protected Cycle(Parcel in) {
        id = in.readLong();
        sequenceId = in.readLong();
        pos = in.readInt();
        name = in.readString();
        repetition = in.readInt();
        activityTime = in.readLong();
        recoveryTime = in.readLong();
    }

    public static final Creator<Cycle> CREATOR = new Creator<Cycle>() {
        @Override
        public Cycle createFromParcel(Parcel in) {
            return new Cycle(in);
        }

        @Override
        public Cycle[] newArray(int size) {
            return new Cycle[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(long sequenceId) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(sequenceId);
        dest.writeInt(pos);
        dest.writeString(name);
        dest.writeInt(repetition);
        dest.writeLong(activityTime);
        dest.writeLong(recoveryTime);
    }
}
