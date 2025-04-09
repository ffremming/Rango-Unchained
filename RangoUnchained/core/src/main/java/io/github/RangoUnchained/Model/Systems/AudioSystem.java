package io.github.RangoUnchained.Model.Systems;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


import java.util.List;
import java.util.Random;

import io.github.RangoUnchained.Model.Components.AudioComponent;
import io.github.RangoUnchained.Model.Entities.Entity;

public class AudioSystem implements System {

    private ComponentFilter filter = new ComponentFilter();

    public AudioSystem(){
        filter
            .require(AudioComponent.class);
    }



    @Override
    public void updateEntity(Entity entity, float delta) {
        AudioComponent audio = (AudioComponent) entity.getComponent(AudioComponent.class);
        AudioComponent.ActionType soundKey;

        while ((soundKey = audio.audioQueue.poll()) != null) {
            Gdx.app.log("Audio", ""+entity + "" + soundKey);
            List<Sound> sounds = audio.audioMap.get(soundKey);

            if (sounds != null && !sounds.isEmpty()) {
                sounds.get(new Random().nextInt(sounds.size())).play();
            }
        }
    }

    @Override
    public boolean filter(Entity entity) {
        return (filter.matches(entity));
    }
}
