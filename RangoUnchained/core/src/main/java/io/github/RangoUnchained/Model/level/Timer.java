package io.github.RangoUnchained.Model.level;

public class Timer {

    private double time; // Time in seconds
    private boolean running;

    public Timer() {
        this.time = 0;
        this.running = false;
        start();
    }

    // Start the timer
    public void start() {
        this.running = true;
    }

    // Stop the timer
    public void stop() {
        this.running = false;
    }

    // Reset the timer
    public void reset() {
        this.time = 0;
        this.running = false;
    }

    // Update the timer (call this method every frame or at regular intervals)
    public void update(double deltaTime) {
        if (running) {
            this.time += deltaTime;
        }
    }

    // Get the current time
    public double getTime() {
        return this.time;
    }
}
