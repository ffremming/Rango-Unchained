package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.Gdx;

import io.github.RangoUnchained.Controllers.LevelController;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Systems.ContactSystem;
import io.github.RangoUnchained.Model.Systems.ContactSystem.CollisionEvent;
import io.github.RangoUnchained.Model.contactStrategies.ContactStrategy;

public class ScoreManager implements ContactStrategy{

    int score = 0;
    public ScoreManager(){
        setContactStrategies();
    }

    public void setScore(int score){this.score = score;}

    public void addScore(int score){this.score += score;}

    public int getScore(){return score;}
    
    private void handleBallProjectileCollision(CollisionEvent contact){
        //TODO do we want different scores for different balls?
        Gdx.app.log("contact",""+score);
        addScore(1);
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
