package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;

public class PlayerControllerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<JumpComponent> mJump;

    private ComponentMapper<CollidableComponent> mCollidable;

    private boolean moveRight;

    private boolean moveLeft;

    private boolean jump;

    public PlayerControllerSystem() {
        super(Aspect.all(PlayerComponent.class, RigidBodyComponent.class, JumpComponent.class, CollidableComponent.class));

        Gdx.input.setInputProcessor(new InputMultiplexer(new GameInputAdapter()));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent cPlayer = mPlayer.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        JumpComponent cJump = mJump.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        if (cPlayer.canWalk) {
            if (moveRight == moveLeft) {
                cRigidBody.velocity.x = 0;
            } else if (moveLeft) {
                cRigidBody.velocity.x = -cPlayer.walkSpeed;
            } else if (moveRight) {
                cRigidBody.velocity.x = cPlayer.walkSpeed;
            }
        }

        if(cJump.canJump && cCollidable.onGround && jump) {
            cRigidBody.velocity.y = cJump.jumpSpeed;
        }
    }

    private class GameInputAdapter extends InputAdapter {

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
    }
}
