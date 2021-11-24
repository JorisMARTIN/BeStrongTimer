package com.iut2.bestrongtimer.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseClient {

    private static DatabaseClient instance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "BeStrongTimer").build();

        ////////// REMPLIR LA BD à la première création à l'aide de l'objet roomDatabaseCallback
        // Ajout de la méthode addCallback permettant de populate (remplir) la base de données à sa création
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "BeStrongTimer").addCallback(roomDatabaseCallback).build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    // Objet permettant de populate (remplir) la base de données à sa création
    RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {

        // Called when the database is created for the first time.
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            db.execSQL("INSERT INTO training (name, description, setup_time, global_sequence_time, difficulty_average) VALUES(\"Test\", \"Test description\", 5000, 820000, 3.0);");

            // Sequence 1
            db.execSQL("INSERT INTO sequence (training_id, name, description, difficulty, repetition, recovery_time, global_cycle_time) VALUES (1, \"Test sequence 1\", \"Test Description sequence 1\", 2, 10, 60000, 150000);");
                // Cycles
                db.execSQL("INSERT INTO cycle (sequence_id, name, repetition, activity_time, recovery_time) VALUES (1, \"Pompes x 5\", 5, 10000, 5000);");
                db.execSQL("INSERT INTO cycle (sequence_id, name, repetition, activity_time, recovery_time) VALUES (1, \"Abdos x 5\", 5, 10000, 5000);");

            // Sequence 2
            db.execSQL("INSERT INTO sequence (training_id, name, description, difficulty, repetition, recovery_time, global_cycle_time) VALUES (1, \"Test sequence 2\", \"Test Description sequence 2\", 4, 5, 5000, 75000);");
                // Cycles
                db.execSQL("INSERT INTO cycle (sequence_id, name, repetition, activity_time, recovery_time) VALUES (2, \"Pompes x 5\", 5, 10000, 5000);");
        }
    };

}
