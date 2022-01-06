package com.iut2.bestrongtimer.LiveTraining;

import com.iut2.bestrongtimer.db.Entity.Cycle.Cycle;
import com.iut2.bestrongtimer.db.Entity.Sequence.Sequence;

public interface LiveTrainingListener {

    void onTimeChange(int minutes, int seconds);

    void onCycleRestart(int currentCycleRepetitionLeft);
    void onCycleChange(Cycle currentCycle, int cycleLength);

    void onSequenceRestart(int currentSequenceRepetitionLeft);
    void onSequenceChange(Sequence currentSequence, int sequenceLength);

    void onTrainingEnd();

    void onCycleRecovery();

    void onSequenceRecovery();

    void onActivity();

    void onTimerPause();
    void onTimerRestart();
}
