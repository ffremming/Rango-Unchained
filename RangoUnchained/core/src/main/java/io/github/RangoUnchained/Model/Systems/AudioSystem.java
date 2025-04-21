package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.RangoUnchained.Model.Components.AudioComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Entities.BallEntity;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Entities.FloorEntity;
import io.github.RangoUnchained.Model.Entities.ObstacleEntity;
import io.github.RangoUnchained.Model.Entities.PlayerEntity;
import io.github.RangoUnchained.Model.Entities.ProjectileEntity;
import io.github.RangoUnchained.Model.Factories.AudioLoader;

public class AudioSystem implements Systems {

    private final ComponentFilter filter = new ComponentFilter();
    private final Random random = new Random();
    private final AudioLoader audioLoader;


    public AudioSystem(ContactSystem centralContactListener){
        filter.require(AudioComponent.class);
        audioLoader = AudioLoader.getInstance();

        centralContactListener.subscribe(
            BallEntity.class, FloorEntity.class,
            collisionEvent -> playSound(AudioComponent.ActionType.BOUNCE, 0.1f),
            null);
        centralContactListener.subscribe(
            BallEntity.class, ObstacleEntity.class,
            collisionEvent -> playSound(AudioComponent.ActionType.BOUNCE, 0.1f),
            null);
        centralContactListener.subscribe(
            BallEntity.class, ProjectileEntity.class,
            collisionEvent -> playSound(AudioComponent.ActionType.POP, 10f),
            null);
    }

    private void playSound (AudioComponent.ActionType soundKey, float volume){
        List<Sound> sounds = audioLoader.getSounds(soundKey);
        if (sounds != null && !sounds.isEmpty()) {
            Sound sound = sounds.get(random.nextInt(sounds.size()));
            sound.play(volume);
            Gdx.app.log("AudioSystem", " " + sound);
        }
    }

    private void delaySound (AtomicBoolean action, float delay){
        action.set(true);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                action.set(false);
            }
        }, delay);
    }

    @Override
    public void updateEntity(Entity entity, float delta) {
        AudioComponent audio = (AudioComponent) entity.getComponent(AudioComponent.class);
        InputComponent input = (InputComponent) entity.getComponent(InputComponent.class);

//        AudioComponent.ActionType soundKey;
        if( entity instanceof PlayerEntity){
            if((input.isRight() || input.isLeft()) && !audio.hasWalkingAudio.get()){
                playSound(AudioComponent.ActionType.MOVE, 1.3f);
                delaySound(audio.hasWalkingAudio, 0.5f);
            }
            if (input.isShoot() && !audio.hasShootingAudio.get()){
                playSound(AudioComponent.ActionType.SHOOT, 0.3f);
                delaySound(audio.hasShootingAudio, 1.2f);
            }

        }

    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }

}
