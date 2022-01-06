package com.iut2.bestrongtimer.AddEditTraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.iut2.bestrongtimer.R;
import com.iut2.bestrongtimer.db.DatabaseClient;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;

public class AddEditSequence extends AppCompatActivity implements View.OnClickListener {

    public static final String TRAINING_ID = "training_id";
    public static final String SEQUENCE = "sequence";
    public static final String SEQUENCE_LENGTH = "sequence_length";

    // VIEW
    TextView title, name, description, repetitionLabel, recoveryLabel;
    RatingBar difficulty;

    Button lessRepetition, plusRepetition, lessRecovey, plusRecovery, cycleButton, delete, submit;

    // DATA
    long trainingId;
    Sequence sequence;
    int sequences_length;

    DatabaseClient db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_sequence);

        db = DatabaseClient.getInstance(getApplicationContext());

        title = findViewById(R.id.activity_add_edit_sequence_title);
        name = findViewById(R.id.activity_add_edit_sequence_name_input);
        description = findViewById(R.id.activity_add_edit_sequence_description_input);
        repetitionLabel = findViewById(R.id.activity_add_edit_sequence_repetition_label);
        recoveryLabel = findViewById(R.id.activity_add_edit_sequence_recovery_label);
        difficulty = findViewById(R.id.activity_add_edit_sequence_rating);

        lessRepetition = findViewById(R.id.activity_add_edit_sequence_repetition_less_button);
        plusRepetition = findViewById(R.id.activity_add_edit_sequence_repetition_plus_button);

        lessRecovey = findViewById(R.id.activity_add_edit_sequence_recovery_less_button);
        plusRecovery = findViewById(R.id.activity_add_edit_sequence_recovery_plus_button);

        cycleButton = findViewById(R.id.activity_add_edit_sequence_cycle_button);
        submit = findViewById(R.id.activity_add_edit_sequence_submit_button);

        lessRepetition.setOnClickListener(this);
        plusRepetition.setOnClickListener(this);

        lessRecovey.setOnClickListener(this);
        plusRecovery.setOnClickListener(this);

        cycleButton.setOnClickListener(this);
        submit.setOnClickListener(this);

        delete = findViewById(R.id.activity_add_edit_sequence_delete_button);
        delete.setOnClickListener(this);

        trainingId = getIntent().getLongExtra(TRAINING_ID, 0);
        sequence = getIntent().getParcelableExtra(SEQUENCE);
        sequences_length = getIntent().getIntExtra(SEQUENCE_LENGTH, 0);

        if (sequence != null) {
            title.setText(R.string.edit_sequence);
            name.setText(sequence.getName());
            description.setText(sequence.getDescription());

            repetitionLabel.setText(String.valueOf(sequence.getRepetition()));
            difficulty.setRating(sequence.getDifficulty());
            recoveryLabel.setText(String.valueOf(sequence.getRecoveryTime() / 1000));
        } else {
            delete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == lessRepetition || v == plusRepetition) {
            int repetition = Integer.parseInt(repetitionLabel.getText().toString());
            if (v == lessRepetition && repetition > 1) repetitionLabel.setText(String.valueOf(repetition - 1));
            else if (v == plusRepetition) repetitionLabel.setText(String.valueOf(repetition + 1));

            return;
        }

        if (v == lessRecovey || v == plusRecovery) {
            int recoveryTime = Integer.parseInt(recoveryLabel.getText().toString());
            if (v == lessRecovey && recoveryTime > 0) recoveryLabel.setText(String.valueOf(recoveryTime - 1));
            else if (v == plusRecovery) recoveryLabel.setText(String.valueOf(recoveryTime + 1));

            return;
        }

        if (v == cycleButton || v == submit) {
            if (sequence == null) createSequence(v == submit);
            else updateSequence(v == submit);
        }

        if (v == delete) {
            delete();
        }
    }

    /**
     *
     * @param editCyclesOrCloseAfterUpdate true = close, false = edit cycles
     */
    private void createSequence(boolean editCyclesOrCloseAfterUpdate) {

        sequence = new Sequence(trainingId, sequences_length + 1, name.getText().toString(), description.getText().toString(), (int) difficulty.getRating(), Integer.parseInt(repetitionLabel.getText().toString()), Long.parseLong(recoveryLabel.getText().toString()) * 1000, 0);

        class InsertSequence extends AsyncTask<Void, Void, Long> {

            @Override
            protected Long doInBackground(Void... voids) {
                long newId = db.getAppDatabase().sequenceDAO().insert(sequence);
                return newId;
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                sequence.setId(id);

                if (editCyclesOrCloseAfterUpdate) close();
                else editCycles(sequence.getId());
            }
        }

        InsertSequence insertSequence = new InsertSequence();
        insertSequence.execute();
    }

    /**
     *
     * @param editCycleOrCloseAfterUpdate true = close, false = edit cycles
     */
    private void updateSequence (boolean editCycleOrCloseAfterUpdate) {

        sequence.setName(name.getText().toString());
        sequence.setDescription(description.getText().toString());
        sequence.setDifficulty((int) difficulty.getRating());
        sequence.setRepetition(Integer.parseInt(repetitionLabel.getText().toString()));
        sequence.setRecoveryTime(Long.parseLong(recoveryLabel.getText().toString()) * 1000);


        class UpdateSequence extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.getAppDatabase().sequenceDAO().update(sequence);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);

                if (editCycleOrCloseAfterUpdate) close();
                else editCycles(sequence.getId());
            }
        }

        UpdateSequence updateSequence = new UpdateSequence();
        updateSequence.execute();
    }

    private void delete() {

        class DeleteSequence extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.getAppDatabase().sequenceDAO().delete(sequence);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                close();
            }
        }

        DeleteSequence deleteSequence = new DeleteSequence();
        deleteSequence.execute();
    }

    private void editCycles (long sequenceId) {
        Intent intent = new Intent(getApplicationContext(), SequenceCycles.class);
        intent.putExtra(SequenceCycles.SEQUENCE_ID, sequenceId);

        startActivity(intent);
    }

    private void close() {
        finish();
    }
}