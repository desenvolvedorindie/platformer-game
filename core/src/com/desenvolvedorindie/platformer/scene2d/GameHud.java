package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.entity.system.PlayerControllerSystem;
import com.desenvolvedorindie.platformer.scene2d.utils.DirectionalInputListener;

import java.util.Arrays;
import java.util.Collections;

public class GameHud extends Actor {

    //public final Touchpad directional;

    public final ImageButton up, down, left, right, b;

    public PlayerControllerSystem playerControllerSystem;

    private ScaleToAction leftScaleToAction, rightScaleToAction, upScaleToAction, downScaleToAction;

    private Actor current;

    public GameHud(Stage stage, Skin skin) {
        float duration = 1f;
        float scale = 1.5f;
        Vector2 center = new Vector2(60, 60);

        upScaleToAction = new ScaleToAction();
        upScaleToAction.setDuration(duration);
        upScaleToAction.setInterpolation(Interpolation.linear);
        upScaleToAction.setScale(scale);

        up = new ImageButton(skin.getDrawable("controller_up"));
        up.setPosition(center.x - 22, center.y + 11);
        up.setOrigin(Align.center);
        up.setTransform(true);
        up.addListener(new DirectionalInputListener(upScaleToAction) {
            @Override
            public boolean pressed() {
                current = up;
                sort();
                return true;
            }

            @Override
            public void cancel() {
                up.setScale(1f);
            }
        });
        stage.addActor(up);

        downScaleToAction = new ScaleToAction();
        downScaleToAction.setDuration(duration);
        downScaleToAction.setInterpolation(Interpolation.linear);
        downScaleToAction.setScale(scale);

        down = new ImageButton(skin.getDrawable("controller_down"));
        down.setPosition(center.x - 22, center.y - 54);
        down.setOrigin(Align.center);
        down.setTransform(true);
        down.addListener(new DirectionalInputListener(downScaleToAction) {
            @Override
            public boolean pressed() {
                current = down;
                sort();
                return true;
            }

            @Override
            public void cancel() {
                down.setScale(1f);
            }
        });
        stage.addActor(down);

        leftScaleToAction = new ScaleToAction();
        leftScaleToAction.setDuration(duration);
        leftScaleToAction.setInterpolation(Interpolation.linear);
        leftScaleToAction.setScale(scale);

        left = new ImageButton(skin.getDrawable("controller_left"));
        left.setPosition(center.x - 55, center.y - 21);
        left.setOrigin(Align.center);
        left.setTransform(true);
        left.addListener(new DirectionalInputListener(leftScaleToAction) {
            @Override
            public boolean pressed() {
                current = left;
                sort();
                return playerControllerSystem.moveLeft = true;
            }

            @Override
            public void cancel() {
                left.setScale(1f);
                playerControllerSystem.moveLeft = false;
            }
        });
        stage.addActor(left);

        rightScaleToAction = new ScaleToAction();
        rightScaleToAction.setDuration(duration);
        rightScaleToAction.setInterpolation(Interpolation.linear);
        rightScaleToAction.setScale(scale);

        right = new ImageButton(skin.getDrawable("controller_right"));
        right.setPosition(center.x + 11, center.y - 22);
        right.setOrigin(Align.center);
        right.setTransform(true);
        right.addListener(new DirectionalInputListener(rightScaleToAction) {
            @Override
            public boolean pressed() {
                current = right;
                sort();
                return playerControllerSystem.moveRight = true;
            }

            @Override
            public void cancel() {
                right.setScale(1f);
                playerControllerSystem.moveRight = false;
            }
        });
        stage.addActor(right);

        b = new ImageButton(skin.getDrawable("controller_b"));
        b.setPosition(PlatformerGame.UI_WIDTH - 4 - b.getWidth(), 4);
        b.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return playerControllerSystem.jump = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playerControllerSystem.jump = false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (x < 0 || y < 0 || x > event.getListenerActor().getWidth() || y > event.getListenerActor().getHeight()) {  // means outside the button
                    playerControllerSystem.jump = false;
                }
            }

        });
        stage.addActor(b);
    }

    public void sort(){
        current.setZIndex(current.getStage().getActors().size);

        int z = current.getStage().getActors().size;

        for(Actor actor: current.getStage().getActors()) {
            if(actor != current) {
                actor.setZIndex(--z);
            }
        }
    }

}
