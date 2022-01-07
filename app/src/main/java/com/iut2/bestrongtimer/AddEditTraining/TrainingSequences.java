package com.iut2.bestrongtimer.AddEditTraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iut2.bestrongtimer.R;
import com.iut2.bestrongtimer.db.DatabaseClient;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;
import com.iut2.bestrongtimer.db.Entity.Training.Training;
import com.iut2.bestrongtimer.db.Entity.TrainingSequence;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrainingSequences extends AppCompatActivity implements View.OnClickListener {

    public static final String TRAINING_ID = "training_id";

    DatabaseClient db;

    // VIEW
    LinearLayout sequencesContainer;
    Button addSequence, submit;

    // DATA
    long trainingId;
    ArrayList<Sequence> sequences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_sequences);

        db = DatabaseClient.getInstance(getApplicationContext());

        trainingId = getIntent().getLongExtra(TRAINING_ID, -1);

        sequencesContainer = findViewById(R.id.training_sequences_layout);

        addSequence = findViewById(R.id.training_sequences_add_sequence);
        submit = findViewById(R.id.training_sequences_submit_button);

        addSequence.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        collectSequences();
        super.onResume();
    }

    private void collectSequences() {

        sequencesContainer.removeAllViews();

        class CollectSequences extends AsyncTask<Void, Void, TrainingSequence> {

            @Override
            protected TrainingSequence doInBackground(Void... voids) {
                TrainingSequence sequences = db.getAppDatabase().trainingDAO().getSequencesFromTrainingId((int)trainingId);
                return sequences;
            }

            @Override
            protected void onPostExecute(TrainingSequence trainingSequences) {
                super.onPostExecute(trainingSequences);

                sequences = new ArrayList<>(trainingSequences.sequences);
                initDisplay();
            }
        }

        CollectSequences collectSequences = new CollectSequences();
        collectSequences.execute();
    }

    private void initDisplay() {

        for (Sequence sequence : sequences) {

            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.component_sequence, null);

            // Inflater views
            TextView title = (TextView) layout.findViewById(R.id.component_sequence_title);
            TextView description = (TextView) layout.findViewById(R.id.component_sequence_description);
            TextView repetion = (TextView) layout.findViewById(R.id.component_sequence_repetition);
            TextView level = (TextView) layout.findViewById(R.id.component_sequence_globalLevel);

            // Apply sequence data
            title.setText(sequence.getName());
            description.setText(sequence.getDescription());

            repetion.setText("x " + sequence.getRepetition());

            level.setText(sequence.getDifficulty() + " / 5");
            // Add event
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AddEditSequence.class);
                    intent.putExtra(AddEditSequence.TRAINING_ID, trainingId);
                    intent.putExtra(AddEditSequence.SEQUENCE, sequence);
                    intent.putExtra(AddEditSequence.SEQUENCE_LENGTH, sequences.size());
                    startActivity(intent);
                }
            });

            sequencesContainer.addView(layout);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == addSequence) {
            Intent intent = new Intent(getApplicationContext(), AddEditSequence.class);
            intent.putExtra(AddEditSequence.TRAINING_ID, trainingId);
            intent.putExtra(AddEditSequence.SEQUENCE_LENGTH, sequences.size());
            startActivity(intent);
        } else if (v == submit) {
            finish();
        }
    }
}