package io.github.RangoUnchained.Model.level;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;

public class ScoreManager implements ContactListener{

    int score = 0;
    public ScoreManager(){
        //LevelController.getInstance().getWorld().setContactListener(this);
    }

    public void setScore(int score){this.score = score;}

    public void addScore(int score){this.score += score;}

    public int getScore(){return score;}

    @Override
    public void endContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        if (dataA == null || dataB == null) return;
        if (!(dataA instanceof Entity) || !(dataB instanceof Entity)) return;

        Entity entityA = (Entity) dataA;
        Entity entityB = (Entity) dataB;


        // Check ball-player collision
        if (entityA instanceof BallEntity && entityB instanceof ProjectileEntity) {
            handleBallProjectileCollision((BallEntity) entityA);
        } else if (entityA instanceof ProjectileEntity && entityB instanceof BallEntity) {
            handleBallProjectileCollision((BallEntity) entityB);
        }
    }
    
    private void handleBallProjectileCollision(BallEntity ball){
        //TODO do we want different scores for different balls?
        addScore(1);
    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
