package io.github.RangoUnchained.Model.Tutorial;

public class NoteStrategy implements TutorialStepStrategy{

    String note;
    int duration;
    float counter = 0;
    public NoteStrategy(int duration, String note){
        this.duration = duration;
        this.note = note;
    }

    @Override
    public void onEnter() {
    }

    @Override
    public void update(TutorialStepStrategyContext context, float deltaTime) {
        counter += deltaTime;
    }

    @Override
    public boolean isComplete(TutorialStepStrategyContext context) {
        return counter>duration;
    }

    @Override
    public String getMessage() {
        return note;
    }
    
}
