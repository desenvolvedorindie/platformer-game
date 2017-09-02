package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.TimeUtils;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.input.InputSequence;
import com.desenvolvedorindie.platformer.scene2d.GameHud;

public class PlayerControllerSystem extends IteratingSystem {

    public InputSequence especial;
    public boolean moveRight;
    public boolean moveLeft;
    public boolean jump;
    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<JumpComponent> mJump;
    private ComponentMapper<CollidableComponent> mCollidable;
    private long secondToLastPressSpace;
    private long lastPressSpace;

    private PlayerInputAdapter playerInputAdapter;

    public PlayerControllerSystem(GameHud gameHud) {
        super(Aspect.all(PlayerComponent.class, JumpComponent.class));

        playerInputAdapter = new PlayerInputAdapter();

        especial = new InputSequence(1, new InputSequence.IInputButton[]{
                new InputSequence.MultipleInputButton(new InputSequence.IInputButton[]{
                        new InputSequence.InputButton(InputSequence.Type.KEY, Input.Keys.S),
                        new InputSequence.InputButton(InputSequence.Type.KEY, Input.Keys.DOWN),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.DOWN),
                }),
                new InputSequence.MultipleInputButton(new InputSequence.IInputButton[]{
                        new InputSequence.InputButton(InputSequence.Type.KEY, Input.Keys.A),
                        new InputSequence.InputButton(InputSequence.Type.KEY, Input.Keys.D),
                        new InputSequence.InputButton(InputSequence.Type.KEY, Input.Keys.LEFT),
                        new InputSequence.InputButton(InputSequence.Type.KEY, Input.Keys.RIGHT),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.LEFT),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.RIGHT),
                }),
                new InputSequence.MultipleInputButton(new InputSequence.IInputButton[]{
                        new InputSequence.InputButton(InputSequence.Type.BUTTON, Input.Buttons.LEFT),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.B),
                }),
        });
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent cPlayer = mPlayer.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        JumpComponent cJump = mJump.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        if (especial.process()) {
            Gdx.app.log("Especial", "Especial");
        }

        if (cPlayer.canWalk) {
            if (moveRight == moveLeft) {
                cRigidBody.velocity.x = 0;
            } else if (moveLeft) {
                cRigidBody.velocity.x = -cPlayer.walkSpeed;
            } else if (moveRight) {
                cRigidBody.velocity.x = cPlayer.walkSpeed;
            }
        }

        if (!jump && cRigidBody.velocity.y > 0.0f) {
            cRigidBody.velocity.y = Math.min(cRigidBody.velocity.y, cJump.minJumpSpeed);
        }

        if (cJump.canJump && jump) {
            if (mCollidable.has(entityId)) {
                boolean wallJump = (cCollidable.onLeftWall || cCollidable.onRightWall) && cRigidBody.velocity.y < 0 && cJump.wallJump;

                if (cCollidable.onGround || wallJump) {
                    cRigidBody.velocity.y = cJump.jumpSpeed;
                }
            } else {
                cRigidBody.velocity.y = cJump.jumpSpeed;
            }
        }
    }

    public PlayerInputAdapter getPlayerInputAdapter() {
        return playerInputAdapter;
    }

    private class PlayerInputAdapter extends InputAdapter implements GameHud.GameHudListener {

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.RIGHT:
                case Input.Keys.D:
                    moveRight = true;
                    break;
                case Input.Keys.LEFT:
                case Input.Keys.A:
                    moveLeft = true;
                    break;
                case Input.Keys.SPACE:
                    secondToLastPressSpace = lastPressSpace;
                    lastPressSpace = TimeUtils.millis();
                    jump = true;
                    break;
            }

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Input.Keys.RIGHT:
                case Input.Keys.D:
                    moveRight = false;
                    break;
                case Input.Keys.LEFT:
                case Input.Keys.A:
                    moveLeft = false;
                    break;
                case Input.Keys.SPACE:
                    jump = false;
                    break;
            }
            return true;
        }

        @Override
        public boolean buttonDown(int buttoncode) {
            switch (buttoncode) {
                case GameHud.Buttons.RIGHT:
                    moveRight = true;
                    break;
                case GameHud.Buttons.LEFT:
                    moveLeft = true;
                    break;
                case GameHud.Buttons.B:
                    jump = true;
                    break;
            }
            return true;
        }

        @Override
        public boolean buttonUp(int buttoncode) {
            switch (buttoncode) {
                case GameHud.Buttons.RIGHT:
                    moveRight = false;
                    break;
                case GameHud.Buttons.LEFT:
                    moveLeft = false;
                    break;
                case GameHud.Buttons.B:
                    jump = false;
                    break;
            }
            return true;
        }
    }

}
