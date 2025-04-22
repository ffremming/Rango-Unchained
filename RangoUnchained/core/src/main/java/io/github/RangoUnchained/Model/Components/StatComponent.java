package io.github.RangoUnchained.Model.Components;

/**
 * Component for tracking pop stats to an entity.
 */
public class StatComponent implements Component {

    private int timesPopped = 0;

    public int getTimesPopped() {
        return timesPopped;
    }

    public void setTimesPopped(int timesPopped) {
        this.timesPopped = timesPopped;
    }
}
