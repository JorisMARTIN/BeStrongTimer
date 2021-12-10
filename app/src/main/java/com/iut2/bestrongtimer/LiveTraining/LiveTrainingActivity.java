package com.iut2.bestrongtimer.LiveTraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iut2.bestrongtimer.LiveTraining.LiveTraining;
import com.iut2.bestrongtimer.LiveTraining.LiveTrainingListener;
import com.iut2.bestrongtimer.R;
import com.iut2.bestrongtimer.db.DatabaseClient;
import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;
import com.iut2.bestrongtimer.db.Entity.SequenceCycle;
import com.iut2.bestrongtimer.db.Entity.Training.Training;
import com.iut2.bestrongtimer.timer.Timer;
import com.iut2.bestrongtimer.timer.UpdateListener;

import java.util.List;

public class LiveTrainingActivity extends AppCompatActivity implements LiveTrainingListener {

    private static final String TRAINING = "training";

    // DATABASE
    DatabaseClient db;

    // CONSTRAINT LAYOUT (used to change background color)
    ConstraintLayout layout;

    ImageView stateIcon;

    // TEXT FIELDS
    TextView
            trainingTitle,

            currentSequenceIndex,
            currentSequenceTitle,
            currentSequenceDesc,
            currentSequenceRepetitionLeft,

            currentCycleIndex,
            currentCycleTitle,
            currentCycleActivityTime,
            currentCycleRecoveryTime,
            currentCycleRepetitionLeft,

            stateLabel,
            timeLeftLabel;

    Button pauseResumeButton;

    // Training data
    Training training;
    List<SequenceCycle> sequencesCycles;


    // Model
    LiveTraining liveTraining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_training);

        // Get db
        db = DatabaseClient.getInstance(getApplicationContext());

        // Setup views
        layout = findViewById(R.id.activity_live_layout);
        stateIcon = findViewById(R.id.activity_live_state_icon);

        trainingTitle = findViewById(R.id.activity_live_title);

        currentSequenceIndex = findViewById(R.id.activity_live_current_sequence_index);
        currentSequenceTitle = findViewById(R.id.activity_live_current_sequence_title);
        currentSequenceDesc = findViewById(R.id.activity_live_current_sequence_desc);
        currentSequenceRepetitionLeft = findViewById(R.id.activity_live_current_sequence_repetition_left);

        currentCycleIndex = findViewById(R.id.activity_live_current_cycle_index);
        currentCycleTitle = findViewById(R.id.activity_live_current_cycle_title);
        currentCycleActivityTime = findViewById(R.id.activity_live_current_cycle_activity_time);
        currentCycleRecoveryTime = findViewById(R.id.activity_live_current_cycle_recovery_time);
        currentCycleRepetitionLeft = findViewById(R.id.activity_live_current_cycle_repetition_left);

        stateLabel = findViewById(R.id.activity_live_state_label);
        timeLeftLabel = findViewById(R.id.activity_live_timer_label);

        pauseResumeButton = findViewById(R.id.activity_live_bottom_button);

        training = getIntent().getParcelableExtra(TRAINING);

        trainingTitle.setText(training.getName());

        // Get Sequence & cycle associated to the training
        getSequences();
    }

    private void getSequences() {

        class CollectSequencesCycles extends AsyncTask<Void, Void, List<SequenceCycle>> {

            @Override
            protected List<SequenceCycle> doInBackground(Void... voids) {
                List<SequenceCycle> trainingSequencesCycles = db.getAppDatabase().sequenceDAO().getSequencesCycleFromTrainingId(training.getId());
                return trainingSequencesCycles;
            }

            @Override
            protected void onPostExecute(List<SequenceCycle> trainingSequencesCycles) {
                super.onPostExecute(trainingSequencesCycles);
                sequencesCycles = trainingSequencesCycles;

                initTraining();
            }
        }

        CollectSequencesCycles c = new CollectSequencesCycles();
        c.execute();
    }

    private void initTraining() {

        liveTraining = new LiveTraining(this.sequencesCycles, this);

        pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO pause restart
            }
        });

        this.stateLabel.setText("Préparation...");

       Timer timer = new Timer(new UpdateListener() {
            @Override
            public void onUpdate(int minutesLeft, int secondsLeft) {
               //LiveTrainingActivity.this.onTimeChange(minutesLeft, secondsLeft);
               onTimeChange(minutesLeft, secondsLeft);
            }

            @Override
            public void onFinish() {
                liveTraining.startTraining();
            }
        }, 10000);
       timer.start();
    }

    // Live training listeners

    @Override
    public void onTimeChange(int minutes, int seconds) {

        String label = "";

        if (minutes > 0) {
            label += minutes + 1 + " min, ";
        }

        label += seconds + 1 + " s";

        this.timeLeftLabel.setText(label);
    }

    @Override
    public void onCycleRestart(int currentCycleRepetitionLeft) {
        this.currentCycleRepetitionLeft.setText("x " + currentCycleRepetitionLeft);
    }

    @Override
    public void onCycleChange(Cycle currentCycle, int cycleLength) {
        this.currentCycleIndex.setText("Cycle " + currentCycle.getPos() + " / " + cycleLength);
        this.currentCycleTitle.setText(currentCycle.getName());
        this.currentCycleActivityTime.setText(currentCycle.getActivityTime() / 1000 + " s");
        this.currentCycleRecoveryTime.setText(currentCycle.getRecoveryTime() / 1000 + " s");
        this.currentCycleRepetitionLeft.setText("x " + currentCycle.getRepetition());
    }

    @Override
    public void onSequenceRestart(int currentSequenceRepetitionLeft) {
        this.currentSequenceRepetitionLeft.setText("x " + currentSequenceRepetitionLeft);
    }

    @Override
    public void onSequenceChange(Sequence currentSequence, int sequenceSize) {
        this.currentSequenceIndex.setText("Sequence " + currentSequence.getPos() + " / " + sequenceSize);
        this.currentSequenceTitle.setText(currentSequence.getName());
        this.currentSequenceDesc.setText(currentSequence.getDescription());
        this.currentSequenceRepetitionLeft.setText("x " + currentSequence.getRepetition());
    }

    @Override
    public void onTrainingEnd() {

    }

    @Override
    public void onCycleRecovery() {
        this.stateLabel.setText("Souffle un peu ...");
        this.layout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGreen));
        this.stateIcon.setBackgroundResource(R.drawable.ic_sleep);
    }

    @Override
    public void onSequenceRecovery() {
        this.stateLabel.setText("Tu as le droit de souffler un peu plus");
        this.layout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGreen));
        this.stateIcon.setBackgroundResource(R.drawable.ic_sleep);
    }

    @Override
    public void onActivity() {
        this.stateLabel.setText("GO !");
        this.layout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightRed));
        this.stateIcon.setBackgroundResource(R.drawable.ic_activity);
    }
}