package com.iut2.bestrongtimer.AddEditTraining;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iut2.bestrongtimer.R;
import com.iut2.bestrongtimer.db.DatabaseClient;
import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;

public class AddEditCycle extends AppCompatActivity implements View.OnClickListener {

    public static final String SEQUENCE_ID = "sequence_id";
    public static final String CYCLE = "cycle";
    public static final String CYCLE_LENGTH = "cycle_length";

    DatabaseClient db;

    // VIEWS
    TextView title, activityTimeLabel, recoveryTimeLabel, repetitionLabel;
    EditText nameInput;
    Button lessActivityTimeButton, plusActivityTimeButton, lessRecoveryTimeButton, plusRecoveryTimeButton, lessRepetitionButton, plusRepetitionButton, submitButton, deleteButton;

    // DATA
    long sequenceId;
    int cycleLength;
    Cycle cycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_cycle);

        db = DatabaseClient.getInstance(getApplicationContext());

        title = findViewById(R.id.activity_add_edit_cycle_title);
        activityTimeLabel = findViewById(R.id.activity_add_edit_cycle_activity_time_label);
        recoveryTimeLabel = findViewById(R.id.activity_add_edit_cycle_recovery_label);
        repetitionLabel = findViewById(R.id.activity_add_edit_cycle_repetition_label);

        nameInput = findViewById(R.id.activity_add_edit_cycle_name_input);

        lessActivityTimeButton = findViewById(R.id.activity_add_edit_cycle_activity_time_less_button);
        plusActivityTimeButton = findViewById(R.id.activity_add_edit_cycle_activity_time_plus_button);

        lessRecoveryTimeButton = findViewById(R.id.activity_add_edit_cycle_recovery_less_button);
        plusRecoveryTimeButton = findViewById(R.id.activity_add_edit_cycle_recovery_plus_button);

        lessRepetitionButton = findViewById(R.id.activity_add_edit_cycle_repetition_less_button);
        plusRepetitionButton = findViewById(R.id.activity_add_edit_cycle_repetition_plus_button);

        lessActivityTimeButton.setOnClickListener(this);
        plusActivityTimeButton.setOnClickListener(this);

        lessRecoveryTimeButton.setOnClickListener(this);
        plusRecoveryTimeButton.setOnClickListener(this);

        lessRepetitionButton.setOnClickListener(this);
        plusRepetitionButton.setOnClickListener(this);

        submitButton = findViewById(R.id.activity_add_edit_cycle_submit_button);
        submitButton.setOnClickListener(this);

        deleteButton = findViewById(R.id.activity_add_edit_cycle_delete_button);
        deleteButton.setOnClickListener(this);

        sequenceId = getIntent().getLongExtra(SEQUENCE_ID, -1);
        cycleLength = getIntent().getIntExtra(CYCLE_LENGTH, 0);
        cycle = getIntent().getParcelableExtra(CYCLE);

        if (cycle != null) {

            title.setText(R.string.edit_cycle);

            nameInput.setText(cycle.getName());
            activityTimeLabel.setText(String.valueOf(cycle.getActivityTime() / 1000));
            recoveryTimeLabel.setText(String.valueOf(cycle.getRecoveryTime() / 1000));
            repetitionLabel.setText(String.valueOf(cycle.getRepetition()));
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == lessActivityTimeButton || v == plusActivityTimeButton) {
            int activityTime = Integer.parseInt(activityTimeLabel.getText().toString());
            if (v == lessActivityTimeButton && activityTime > 1) activityTimeLabel.setText(String.valueOf(activityTime - 1));
            else if (v == plusActivityTimeButton) activityTimeLabel.setText(String.valueOf(activityTime + 1));

            return;
        }

        if (v == lessRecoveryTimeButton || v == plusRecoveryTimeButton) {
            int recoveryTime = Integer.parseInt(recoveryTimeLabel.getText().toString());
            if (v == lessRecoveryTimeButton && recoveryTime > 0) recoveryTimeLabel.setText(String.valueOf(recoveryTime - 1));
            else if (v == plusRecoveryTimeButton) recoveryTimeLabel.setText(String.valueOf(recoveryTime + 1));

            return;
        }

        if (v == lessRepetitionButton || v == plusRepetitionButton) {
            int repetition = Integer.parseInt(repetitionLabel.getText().toString());
            if (v == lessRepetitionButton && repetition > 1) repetitionLabel.setText(String.valueOf(repetition - 1));
            else if (v == plusRepetitionButton) repetitionLabel.setText(String.valueOf(repetition + 1));

            return;
        }

        if (v == submitButton) {
            if (cycle == null) createCycle();
            else updateCycle();
        }

        if (v == deleteButton) {
            deleteCycle();
        }
    }

    private void createCycle() {

        cycle = new Cycle(sequenceId, cycleLength + 1, nameInput.getText().toString(), Integer.parseInt(repetitionLabel.getText().toString()), Long.parseLong(activityTimeLabel.getText().toString()) * 1000, Long.parseLong(recoveryTimeLabel.getText().toString()) * 1000);

        class InsertCycle extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.getAppDatabase().cycleDAO().insert(cycle);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                close();
            }
        }

        InsertCycle insertCycle = new InsertCycle();
        insertCycle.execute();
    }

    private void updateCycle() {
        cycle.setName(nameInput.getText().toString());
        cycle.setActivityTime(Long.parseLong(activityTimeLabel.getText().toString()) * 1000);
        cycle.setRecoveryTime(Long.parseLong(recoveryTimeLabel.getText().toString()) * 1000);
        cycle.setRepetition(Integer.parseInt(repetitionLabel.getText().toString()));

        class UpdateCycle extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.getAppDatabase().cycleDAO().update(cycle);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                close();
            }
        }

        UpdateCycle updateCycle = new UpdateCycle();
        updateCycle.execute();

    }

    private void deleteCycle() {

        class DeleteCycle extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.getAppDatabase().cycleDAO().delete(cycle);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                close();
            }
        }

        DeleteCycle deleteCycle = new DeleteCycle();
        deleteCycle.execute();
    }

    private void close() {
        finish();
    }
}