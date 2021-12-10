package com.iut2.bestrongtimer.timer;

import android.os.CountDownTimer;

public class Timer {

    private UpdateListener listener;

    private long initialTime, currentTime;
    private CountDownTimer timer;

    public Timer(UpdateListener listener, long initTime) {
        this.listener = listener;
        this.initialTime = initTime;
        this.currentTime = initTime;
    }

    public void start() {
        if (timer != null) return;

        timer = new CountDownTimer(this.currentTime, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTime = millisUntilFinished;
                update();
            }

            @Override
            public void onFinish() {
                currentTime = 0;

                finish();
            }
        }.start();
    }

    public void pause() {
        if (timer == null) return;

        stop();

        update();
    }

    // Remettre à le compteur à la valeur initiale
    public void reset() {

        if (timer != null) {

            // Arreter le timer
            stop();

            currentTime = initialTime;
        }

        // Mise à jour
        update();

    }

    // Arrete l'objet CountDownTimer et l'efface
    private void stop() {
        timer.cancel();
        timer = null;
    }

    public int getMinutes() {
        return (int) (currentTime / 1000)/60;
    }

    public int getSecondes() {
        int secs = (int) (currentTime / 1000);
        return secs % 60;
    }

    public int getMillisecondes() {
        return (int) (currentTime % 1000);
    }

    private void update() {
        listener.onUpdate(this.getMinutes(), this.getSecondes());
    }
    private void finish() {
        listener.onFinish();
    }
}
