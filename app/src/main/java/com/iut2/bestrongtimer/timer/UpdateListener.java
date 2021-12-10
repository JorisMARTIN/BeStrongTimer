package com.iut2.bestrongtimer.timer;

public interface UpdateListener {

    void onUpdate(int minuteLeft, int secondsLeft);
    void onFinish();
}
