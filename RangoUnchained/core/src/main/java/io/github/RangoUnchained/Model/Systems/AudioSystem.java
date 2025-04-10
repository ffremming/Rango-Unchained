package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.List;
import java.util.Random;

import io.github.RangoUnchained.Model.Components.AudioComponent;
import io.github.RangoUnchained.Model.Components.InputComponent;
import io.github.RangoUnchained.Model.Entities.Entity;
import io.github.RangoUnchained.Model.Factories.AudioLoader;

public class AudioSystem implements System {

    private final ComponentFilter filter = new ComponentFilter();
    private AudioLoader audioLoader;

    public AudioSystem(){

        filter.require(AudioComponent.class);
        audioLoader = AudioLoader.getInstance();
    }

    private void playSound (List<Sound> sounds){
        if (sounds != null && !sounds.isEmpty()) {
            sounds.get(new Random().nextInt(sounds.size())).play();
        }
    }

    @Override
    public void updateEntity(Entity entity, float delta) {
        AudioComponent audio = (AudioComponent) entity.getComponent(AudioComponent.class);
        AudioComponent.ActionType soundKey;

        while ((soundKey = audio.audioQueue.poll()) != null) {
            Gdx.app.log("Audio", " " +entity + " " + soundKey);
            List<Sound> sounds = audioLoader.getSounds(soundKey);
            playSound(sounds);
        }

    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
