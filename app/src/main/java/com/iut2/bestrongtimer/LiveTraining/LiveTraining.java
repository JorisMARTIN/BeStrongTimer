package com.iut2.bestrongtimer.LiveTraining;

import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;
import com.iut2.bestrongtimer.db.Entity.SequenceCycle;
import com.iut2.bestrongtimer.timer.Timer;
import com.iut2.bestrongtimer.timer.UpdateListener;

import java.util.List;

public class LiveTraining {

    private Timer timer;
    private List<SequenceCycle> sequenceCycles;

    private Sequence currentSequence;
    private Cycle currentCycle;
    private int currentSequenceRepetitionLeft, currentCycleRepetitionLeft;
    private LiveTrainingListener listener;
    private LiveTrainingState state;

    public LiveTraining(List<SequenceCycle> sequenceCycle, LiveTrainingListener listener) {

        this.currentSequence = sequenceCycle.get(0).sequence;
        this.currentCycle = sequenceCycle.get(0).getSortedCycles().get(0);

        this.currentSequenceRepetitionLeft = this.currentSequence.getRepetition();
        this.currentCycleRepetitionLeft = this.currentCycle.getRepetition();

        this.sequenceCycles = sequenceCycle;
        this.listener = listener;

        this.listener.onSequenceChange(this.currentSequence, this.sequenceCycles.size());
        this.listener.onCycleChange(this.currentCycle, this.sequenceCycles.get(this.currentSequence.getPos() - 1).getSortedCycles().size());

    }

    public void startTraining() {
        this.state = LiveTrainingState.ACTIVITY;
        this.listener.onActivity();
        startTimer(this.currentCycle.getActivityTime());
    }

    /**
     * Called a the end of each timer
     *
     *
     */
    public void updateState() {


        // Check if the current timer is a cycle activity
        if (this.state == LiveTrainingState.ACTIVITY) {
            // Set state to cycle recovery and start timer;
            this.state = LiveTrainingState.RECOVERY;
            this.startTimer(this.currentCycle.getRecoveryTime());
            this.listener.onCycleRecovery();

            return;
        }

        // Check if the current Cycle isn't finished
        if (this.currentCycleRepetitionLeft > 0) {
            // Launch next repetition
            this.state = LiveTrainingState.ACTIVITY;
            this.currentCycleRepetitionLeft--;
            this.listener.onCycleRestart(this.currentCycleRepetitionLeft);

            this.listener.onActivity();
            this.startTimer(this.currentCycle.getActivityTime());
            return;
        }

        // The current cycle is finished
        // Check if the current cycle isn't the last of the current sequence
        if (this.currentCycle.getPos() < this.sequenceCycles.get(this.currentSequence.getPos() - 1).cycles.size()) {
            // Then launch next cycle
            this.state = LiveTrainingState.ACTIVITY;
            // Switch to next next cycle ( pos start from 1 to ... and not from 0 to ..., then .get(elem.getPos()) == .get(xx.indexOf(elem) + 1))
            this.currentCycle = this.sequenceCycles.get(this.currentSequence.getPos()).getSortedCycles().get(this.currentCycle.getPos());
            // Reset repetition left
            this.currentCycleRepetitionLeft = this.currentCycle.getRepetition();

            this.listener.onCycleChange(this.currentCycle, this.sequenceCycles.get(this.currentSequence.getPos() - 1).cycles.size());
            this.listener.onActivity();

            startTimer(this.currentCycle.getActivityTime());
            return;
        }

        // The current cycle was the last of the sequence.
        // If the last timer was a Cycle Recovery
        if (this.state == LiveTrainingState.RECOVERY) {
            // Start Sequence recovery
            this.state = LiveTrainingState.SEQUENCE_RECOVERY;
            this.listener.onSequenceRecovery();
            startTimer(this.currentSequence.getRecoveryTime());
            return;
        }

        // Check if the sequence isn't finished
        if (this.currentSequenceRepetitionLeft > 0) {
            // Then restart the current sequence
            this.currentSequenceRepetitionLeft--;
            this.listener.onSequenceRestart(this.currentSequenceRepetitionLeft);

            // Update current cycle and this repetion
            this.currentCycle = this.sequenceCycles.get(this.currentSequence.getPos() - 1).getSortedCycles().get(0);
            this.currentCycleRepetitionLeft = this.currentCycle.getRepetition();
            this.listener.onCycleChange(this.currentCycle, this.sequenceCycles.get(this.currentSequence.getPos() - 1).cycles.size());

            this.listener.onActivity();
            this.startTimer(this.currentCycle.getActivityTime());
            return;
        }

        // The current sequence is finished
        // Check if there is a next sequence after the current
        if (this.currentSequence.getPos() < this.sequenceCycles.size()) {
            // Then launch the next sequence and set repetion left
            this.currentSequence = this.sequenceCycles.get(this.currentSequence.getPos()).sequence;
            this.currentSequenceRepetitionLeft = this.currentSequence.getRepetition();

            this.listener.onSequenceChange(this.currentSequence, this.sequenceCycles.size());

            // Update current cycle and this repetition
            this.currentCycle = this.sequenceCycles.get(this.currentSequence.getPos() - 1).getSortedCycles().get(0);
            this.currentCycleRepetitionLeft = this.currentCycle.getRepetition();

            this.listener.onCycleChange(this.currentCycle, this.sequenceCycles.get(this.currentSequence.getPos() - 1).cycles.size());

            this.listener.onActivity();
            this.startTimer(this.currentCycle.getActivityTime());
            return;
        }

        // The current sequence was the last of the training
        // Then the training is end;

        listener.onTrainingEnd();

    }

    public void startTimer(long timerTime) {
        timer = new Timer(new UpdateListener() {
            @Override
            public void onUpdate(int minuteLeft, int secondsLeft) {
                listener.onTimeChange(minuteLeft, secondsLeft);
            }

            @Override
            public void onFinish() {
                updateState();
            }
        }, timerTime);

        timer.start();
    }

}
