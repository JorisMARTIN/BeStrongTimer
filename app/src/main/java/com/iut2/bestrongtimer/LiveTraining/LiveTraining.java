package com.iut2.bestrongtimer.LiveTraining;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;

import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;
import com.iut2.bestrongtimer.db.Entity.SequenceCycle;
import com.iut2.bestrongtimer.timer.Timer;
import com.iut2.bestrongtimer.timer.UpdateListener;

import java.util.List;

public class LiveTraining implements Parcelable {

    private Timer timer;
    private List<SequenceCycle> sequenceCycles;

    private Sequence currentSequence;
    private Cycle currentCycle;
    private int currentSequenceRepetitionLeft, currentCycleRepetitionLeft;
    private LiveTrainingListener listener;
    private LiveTrainingState state;
    private boolean isPaused;
    private Vibrator v;

    public LiveTraining(List<SequenceCycle> sequenceCycle, LiveTrainingListener listener, Vibrator v) {

        this.currentSequence = sequenceCycle.get(0).sequence;
        this.currentCycle = sequenceCycle.get(0).getSortedCycles().get(0);

        this.currentSequenceRepetitionLeft = this.currentSequence.getRepetition() - 1;
        this.currentCycleRepetitionLeft = this.currentCycle.getRepetition();

        this.sequenceCycles = sequenceCycle;
        this.listener = listener;

        this.listener.onSequenceChange(this.currentSequence, this.sequenceCycles.size());
        this.listener.onCycleChange(this.currentCycle, this.sequenceCycles.get(this.currentSequence.getPos() - 1).getSortedCycles().size());

        this.isPaused = false;

        this.v = v;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getMinutesLeft() {
        return this.timer.getMinutes();
    }

    public int getSecondsLeft() {
        return this.timer.getSecondes();
    }

    public void setListener(LiveTrainingListener listener) {
        this.listener = listener;
    }

    public List<SequenceCycle> getSequenceCycles() {
        return sequenceCycles;
    }

    public Sequence getCurrentSequence() {
        return currentSequence;
    }

    public Cycle getCurrentCycle() {
        return currentCycle;
    }

    public int getCurrentSequenceRepetitionLeft() {
        return currentSequenceRepetitionLeft;
    }

    public int getCurrentCycleRepetitionLeft() {
        return currentCycleRepetitionLeft;
    }

    public LiveTrainingState getState() {
        return state;
    }

    protected LiveTraining(Parcel in) {
        currentSequence = in.readParcelable(Sequence.class.getClassLoader());
        currentCycle = in.readParcelable(Cycle.class.getClassLoader());
        currentSequenceRepetitionLeft = in.readInt();
        currentCycleRepetitionLeft = in.readInt();
        isPaused = in.readByte() != 0;
    }

    public static final Creator<LiveTraining> CREATOR = new Creator<LiveTraining>() {
        @Override
        public LiveTraining createFromParcel(Parcel in) {
            return new LiveTraining(in);
        }

        @Override
        public LiveTraining[] newArray(int size) {
            return new LiveTraining[size];
        }
    };

    public void startTraining(long preparationTime) {
        this.state = LiveTrainingState.SETUP;

        timer = new Timer(new UpdateListener() {
            @Override
            public void onUpdate(int minuteLeft, int secondsLeft) {
                listener.onTimeChange(minuteLeft, secondsLeft);
            }

            @Override
            public void onFinish() {
                listener.onActivity();
                state = LiveTrainingState.ACTIVITY;
                startTimer(currentCycle.getActivityTime());
            }
        }, preparationTime, v);
        timer.start();

    }

    /**
     * Called a the end of each timer
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
            this.currentCycle = this.sequenceCycles.get(this.currentSequence.getPos() - 1).getSortedCycles().get(this.currentCycle.getPos());
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

    private void startTimer(long timerTime) {
        timer = new Timer(new UpdateListener() {
            @Override
            public void onUpdate(int minuteLeft, int secondsLeft) {
                listener.onTimeChange(minuteLeft, secondsLeft);
            }

            @Override
            public void onFinish() {
                timer.reset();
                updateState();
            }
        }, timerTime, v);

        timer.start();
    }

    public void pauseRestart() {
        if (this.isPaused) {
            this.timer.start();
            this.listener.onTimerRestart();
        } else {
            this.timer.pause();
            this.listener.onTimerPause();
        }

        this.isPaused = !this.isPaused;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(currentSequence, flags);
        dest.writeParcelable(currentCycle, flags);
        dest.writeInt(currentSequenceRepetitionLeft);
        dest.writeInt(currentCycleRepetitionLeft);
        dest.writeByte((byte) (isPaused ? 1 : 0));
    }
}
