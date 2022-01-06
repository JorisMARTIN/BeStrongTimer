package com.iut2.bestrongtimer.AddEditTraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iut2.bestrongtimer.MainActivity;
import com.iut2.bestrongtimer.R;
import com.iut2.bestrongtimer.db.DatabaseClient;
import com.iut2.bestrongtimer.db.Entity.Training.Training;


public class AddEditTraining extends AppCompatActivity implements View.OnClickListener {

    public static final String TRAINING_ID = "training_id";

    // VIEW
    TextView title, setupTimeLabel;
    EditText nameInput, descriptionInput;

    Button lessSetupTime, plusSetupTime, sequenceButton, submit, delete;

    // Database
    DatabaseClient db;

    // Data
    long trainingId;
    Training training;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_training);

        title = findViewById(R.id.activity_add_edit_training_title);
        nameInput = findViewById(R.id.activity_add_edit_training_name_input);
        descriptionInput = findViewById(R.id.activity_add_edit_training_description_input);
        setupTimeLabel = findViewById(R.id.activity_add_edit_training_setup_time_label);
        lessSetupTime = findViewById(R.id.activity_add_edit_training_setup_time_less_button);
        plusSetupTime = findViewById(R.id.activity_add_edit_training_setup_time_plus_button);

        lessSetupTime.setOnClickListener(this);
        plusSetupTime.setOnClickListener(this);

        sequenceButton = findViewById(R.id.activity_add_edit_training_sequence_button);
        submit = findViewById(R.id.activity_add_edit_training_submit_button);

        sequenceButton.setOnClickListener(this);
        submit.setOnClickListener(this);

        delete = findViewById(R.id.activity_add_edit_training_delete_button);
        delete.setOnClickListener(this);

        db = DatabaseClient.getInstance(getApplicationContext());

        trainingId = getIntent().getLongExtra(TRAINING_ID, -1);

        if (trainingId != -1) getTraining();
        else delete.setVisibility(View.INVISIBLE);
    }

    private void getTraining() {

        class CollectTraining extends AsyncTask<Void, Void, Training> {

            @Override
            protected Training doInBackground(Void... voids) {
                Training training = db.getAppDatabase().trainingDAO().getTrainingById(trainingId);
                return training;
            }

            @Override
            protected void onPostExecute(Training tr) {
                super.onPostExecute(tr);

                training = tr;

                title.setText(R.string.edit_training);
                nameInput.setText(training.getName());
                descriptionInput.setText(training.getDescription());
                setupTimeLabel.setText(String.valueOf(training.getSetupTime() / 1000));
            }
        }

        CollectTraining collectTraining = new CollectTraining();
        collectTraining.execute();
    }

    @Override
    public void onClick(View v) {

        // Setup time
        if (v == lessSetupTime || v == plusSetupTime) {
            int setupTime = Integer.parseInt(setupTimeLabel.getText().toString());
            if (v == lessSetupTime && setupTime > 0) setupTimeLabel.setText(String.valueOf(setupTime - 1));
            else if (v == plusSetupTime) setupTimeLabel.setText(String.valueOf(setupTime + 1));

            return;
        }


        if (v == sequenceButton || v == submit) {
            if (training == null) createTraining(v == submit);
            // We need to update the training
            else updateTraining(v == submit);
        }

        if (v == delete) {
            delete();
        }
    }

    /**
     *
     * @param editSequenceOrCloseAfterUpdate true = close, false = edit sequences
     */
    private void createTraining(boolean editSequenceOrCloseAfterUpdate) {

        training = new Training(nameInput.getText().toString(), descriptionInput.getText().toString(), Long.parseLong(setupTimeLabel.getText().toString()) * 1000, 0, 0);

        class InsertTraining extends AsyncTask<Void, Void, Long> {

            @Override
            protected Long doInBackground(Void... voids) {
                long newId = db.getAppDatabase().trainingDAO().insert(training);
                return newId;
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                training.setId(id);
                if (editSequenceOrCloseAfterUpdate) close();
                else editSequences(id);
            }
        }

        InsertTraining insertTraining = new InsertTraining();
        insertTraining.execute();
    }

    /**
     *
     * @param editSequenceOrCloseAfterUpdate true = close, false = edit sequences
     */
    private void updateTraining (boolean editSequenceOrCloseAfterUpdate) {

        training.setName(nameInput.getText().toString());
        training.setDescription(descriptionInput.getText().toString());
        training.setSetupTime(Long.parseLong(setupTimeLabel.getText().toString()) * 1000);


        class UpdateTraining extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.getAppDatabase().trainingDAO().update(training);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);

                if (editSequenceOrCloseAfterUpdate) close();
                else editSequences(training.getId());
            }
        }

        UpdateTraining updateTraining = new UpdateTraining();
        updateTraining.execute();
    }

    private void delete() {

        class DeleteTraining extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.getAppDatabase().trainingDAO().delete(training);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                close();
            }
        }

        DeleteTraining deleteTraining = new DeleteTraining();
        deleteTraining.execute();
    }

    private void editSequences(long id) {
        Intent intent = new Intent(getApplicationContext(), TrainingSequences.class);
        intent.putExtra(AddEditSequence.TRAINING_ID, id);

        startActivity(intent);
    }

    private void close() {
        finish();
    }
}