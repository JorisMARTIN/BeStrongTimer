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
import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;
import com.iut2.bestrongtimer.db.Entity.SequenceCycle;
import com.iut2.bestrongtimer.db.Entity.TrainingSequence;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SequenceCycles extends AppCompatActivity implements View.OnClickListener {

    public static final String SEQUENCE_ID = "sequence_id";

    DatabaseClient db;

    // VIEW
    LinearLayout cyclesContainer;
    Button addCycle, submit, delete;

    // DATA
    long sequenceId;
    ArrayList<Cycle> cycles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_cycles);

        db = DatabaseClient.getInstance(getApplicationContext());

        cyclesContainer = findViewById(R.id.sequence_cycles_layout);
        addCycle = findViewById(R.id.sequence_cycles_add_cycle);
        submit = findViewById(R.id.sequence_cycles_submit_button);

        addCycle.setOnClickListener(this);
        submit.setOnClickListener(this);

        sequenceId = getIntent().getLongExtra(SEQUENCE_ID, 0);
    }

    @Override
    protected void onResume() {
        collectCycles();
        super.onResume();
    }

    private void collectCycles() {

        cyclesContainer.removeAllViews();

        class CollectCycles extends AsyncTask<Void, Void, SequenceCycle> {

            @Override
            protected SequenceCycle doInBackground(Void... voids) {
                SequenceCycle cycles = db.getAppDatabase().sequenceDAO().getCyclesFromSequenceId((int)sequenceId);
                return cycles;
            }

            @Override
            protected void onPostExecute(SequenceCycle sequenceCycle) {
                super.onPostExecute(sequenceCycle);

                cycles = new ArrayList<>(sequenceCycle.getSortedCycles());
                initDisplay();
            }
        }

        CollectCycles collectCycles = new CollectCycles();
        collectCycles.execute();
    }

    private void initDisplay() {
        for (Cycle cycle : cycles) {

            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.component_cycle, null);

            // Inflater views
            TextView title = (TextView) layout.findViewById(R.id.component_cycle_title);
            TextView activityTime = (TextView) layout.findViewById(R.id.component_cycle_activity_time);
            TextView recoveryTime = (TextView) layout.findViewById(R.id.component_cycle_recovery_time);
            TextView repetition = (TextView) layout.findViewById(R.id.component_cycle_repetition);

            // Apply sequence data
            title.setText(cycle.getName());
            activityTime.setText(String.valueOf(cycle.getActivityTime() / 1000) + " s");
            recoveryTime.setText(String.valueOf(cycle.getRecoveryTime() / 1000) + " s");

            repetition.setText(" x " + cycle.getRepetition());

            // Add event
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AddEditCycle.class);
                    intent.putExtra(AddEditCycle.SEQUENCE_ID, sequenceId);
                    intent.putExtra(AddEditCycle.CYCLE, cycle);
                    intent.putExtra(AddEditCycle.CYCLE_LENGTH, cycles.size());
                    startActivity(intent);
                }
            });

            cyclesContainer.addView(layout);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == addCycle) {
            Intent intent = new Intent(getApplicationContext(), AddEditCycle.class);
            intent.putExtra(AddEditCycle.SEQUENCE_ID, sequenceId);
            intent.putExtra(AddEditCycle.CYCLE_LENGTH, cycles.size());
            startActivity(intent);
        } else if (v == submit) {
            finish();
        }
    }
}