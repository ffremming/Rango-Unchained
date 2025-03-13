package io.github.RangoUnchained.Model.Components;

public class StatComponent implements Component {

    private int timesPopped = 0;

    private int timesHitGround = 0;

    public int getTimesPopped() {
        return timesPopped;
    }

    public void setTimesPopped(int timesPopped) {
        this.timesPopped = timesPopped;
    }

    public int getTimesHitGround() {
        return timesHitGround;
    }

    public void setTimesHitGround(int timesHitGround) {
        this.timesHitGround = timesHitGround;
    }
}
