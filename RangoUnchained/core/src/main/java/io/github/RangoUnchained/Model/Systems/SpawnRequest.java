package io.github.RangoUnchained.Model.Systems;

import com.badlogic.gdx.math.Vector2;

class SpawnRequest {
    public float x, y, radius;
    public String spritePath;
    public int timesPopped;
    public Vector2 velocity;
    public SpawnRequest(float x, float y, float radius, String spritePath, int timesPopped,
                        Vector2 velocity) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.spritePath = spritePath;
        this.timesPopped = timesPopped;
        this.velocity = velocity;
    }
}