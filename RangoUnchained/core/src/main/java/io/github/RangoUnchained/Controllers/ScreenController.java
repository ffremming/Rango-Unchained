
package io.github.RangoUnchained.Controllers;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class ScreenController {

    private static ScreenController screenController;
    private Stack<ScreenAdapter> screens;
    private ScreenController() {
        screens = new Stack<>();
    }

    public static ScreenController getInstance(){
        if (screenController == null) {
            screenController = new ScreenController();
        }
        return screenController;
    }

    public void push (ScreenAdapter screen){
        if (!screens.isEmpty()) {
            screens.push(screen);
        }
    }
    public void pop(){
        if (!screens.isEmpty()) {
            screens.pop();
        }
    }
    public void set(ScreenAdapter screen){
        screens.pop();
        screens.push(screen);
    }

    public void update(SpriteBatch batch){
//        screens.peek().update(batch);
    }
    public void render(float dt){
        if (!screens.isEmpty()) {
            screens.peek().render(dt);
        }
    }

    public void dispose(){
        if (!screens.empty()){
            screens.pop().dispose();
        }
    }
}

