package io.github.RangoUnchained.Model.Systems;

import java.util.ArrayList;
import java.util.Arrays;


import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Components.TutorialComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Tutorial.KillEnemyStrategy;
import io.github.RangoUnchained.Model.Tutorial.KillSpawnStrategy;
import io.github.RangoUnchained.Model.Tutorial.LevelCountDown;
import io.github.RangoUnchained.Model.Tutorial.MoveLeftStrategy;
import io.github.RangoUnchained.Model.Tutorial.MoveRightStrategy;
import io.github.RangoUnchained.Model.Tutorial.NoteStrategy;
import io.github.RangoUnchained.Model.Tutorial.ShootStrategy;
import io.github.RangoUnchained.Model.Tutorial.TutorialManager;

/**
 * ECS system responsible for updating the tutorial.
 * It leverages the TutorialManager to drive tutorial progression via the Strategy Pattern.
 */
public class TutorialSystem implements System {

    private final ComponentFilter filter = new ComponentFilter();
    private final TutorialManager tutorialManager;

    /**
     * Constructs the TutorialSystem and initializes the TutorialManager with the tutorial steps.
     */
    public TutorialSystem(){
        // Require both TutorialComponent and InputComponent for entities processed by this system.
        filter.require(TutorialComponent.class);
        filter.require(InputComponent.class);
        
        // Initialize the TutorialManager with a sequence of tutorial strategies.
        tutorialManager = new TutorialManager(Arrays.asList(
            new MoveLeftStrategy(),
            new MoveRightStrategy(),
            new ShootStrategy(),
           
            new KillEnemyStrategy("Ball: Armedillo Medium",1),
            new KillSpawnStrategy(),
            new NoteStrategy(2,"if enemies hit you, you lose a heart"),
            new KillEnemyStrategy("Ball: Armedillo Big",7),
            new NoteStrategy(2,"if you lose all hearts, its game over"),
            new LevelCountDown()
        ));
    }
    
    /**
     * Updates the tutorial logic for a given entity.
     *
     * @param entity    the entity containing the TutorialComponent and InputComponent
     */
    public void updateEntity(Entity entity) {
        TutorialComponent tutorial = (TutorialComponent)entity.getComponent(TutorialComponent.class);
        InputComponent input = (InputComponent)entity.getComponent(InputComponent.class);

        updateContext(input);
        // get the current context of a step.
        
        
        // Delegate the update to the TutorialManager.
        float delta =0.0166f;
        tutorialManager.update((delta));
        
        // Update the TutorialComponent's message for display.
        tutorial.message = tutorialManager.getCurrentMessage();
    }
    
    @Override
    public boolean filter(Entity entity) {
        return filter.matches(entity);
    }

    private void updateContext(InputComponent input){
        tutorialManager.getCurrentContext().shoot = input.isShoot()|| tutorialManager.getCurrentContext().shoot;
        tutorialManager.getCurrentContext().left = input.isLeft()|| tutorialManager.getCurrentContext().left;
        tutorialManager.getCurrentContext().right = input.isRight()|| tutorialManager.getCurrentContext().right;
    }

    public void flagBallKilled() {
        tutorialManager.getCurrentContext().amountBallsKilled ++;
    }

    public String getTutorialMessage(){
        return tutorialManager.getCurrentMessage();
    }

    public void flagPlayerHit() {
        tutorialManager.getCurrentContext().takenDamage = true;
    }
}
