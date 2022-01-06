package com.iut2.bestrongtimer.LiveTraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iut2.bestrongtimer.R;
import com.iut2.bestrongtimer.db.Entity.Training.Training;

public class LiveTrainingEndActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TRAINING = "training";

    // VIEWS
    Button restartButton, backButton;

    // Intent Data
    Training training;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_training_end);

        training = getIntent().getParcelableExtra(TRAINING);

        restartButton = findViewById(R.id.activity_training_end_restart_button);
        backButton = findViewById(R.id.activity_training_end_back_button);

        restartButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == restartButton) {
            Intent intent = new Intent(getApplicationContext(), LiveTrainingActivity.class);
            intent.putExtra(LiveTrainingActivity.TRAINING, training);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Retour au menu", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}