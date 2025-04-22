package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.Gdx;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.ContactStrategies.ContactStrategy;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;

public class ScoreManager implements ContactStrategy{

    private double score = 0;
    private final GameLevel level;
    public ScoreManager(GameLevel level){
        setContactStrategies();
        this.level = level;
    }

    public void setScore(int score){this.score = score;}

    public void addScore(double score){this.score += score;}

    public int getScore(){return (int) Math.round(score);}

    private void handleBallProjectileCollision(CollisionEvent contact){
        addScore((1 / level.getTimer().getTime()) * 100);
    }

    @Override
    public void setContactStrategies() {
        ContactSystem centralContactListener = LevelController.getInstance().getSystem(ContactSystem.class);
        centralContactListener.subscribe(
        BallEntity.class, ProjectileEntity.class,
        this::handleBallProjectileCollision, // For beginContact
        null);
    }
}
