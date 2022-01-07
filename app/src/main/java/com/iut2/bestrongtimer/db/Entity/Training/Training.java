package com.iut2.bestrongtimer.db.Entity.Training;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.iut2.bestrongtimer.db.TimestampConverter;

import java.util.Date;

@Entity
public class Training implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "creation_date", defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters({TimestampConverter.class})
    public Date creationDate;

    @ColumnInfo(name = "setup_time")
    private long setupTime;

    public Training(String name, String description, long setupTime) {
        this.name = name;
        this.creationDate = new Date();
        this.description = description;
        this.setupTime = setupTime;
    }

    protected Training(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        setupTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(setupTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Training> CREATOR = new Creator<Training>() {
        @Override
        public Training createFromParcel(Parcel in) {
            return new Training(in);
        }

        @Override
        public Training[] newArray(int size) {
            return new Training[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public long getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(long setupTime) {
        this.setupTime = setupTime;
    }

    @Override
    public String toString() {
        return "Training{" + "\n\t" +
                "id=" + id + "\n\t" +
                ", name='" + name + '\'' + "\n\t" +
                ", description='" + description + '\'' + "\n\t" +
                ", creationDate=" + creationDate + "\n\t" +
                ", setupTime=" + setupTime + "\n\t" +
                '}';
    }
}
