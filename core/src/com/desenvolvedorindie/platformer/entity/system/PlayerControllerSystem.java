package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.entity.component.*;

public class PlayerControllerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<Box2dRigidBodyComponent> mBox2dRigidBody;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<JumpComponent> mJump;

    private ComponentMapper<CollidableComponent> mCollidable;

    private boolean moveRight;

    private boolean moveLeft;

    private boolean jump;

    public PlayerControllerSystem() {
        super(Aspect.all(PlayerComponent.class, Box2dRigidBodyComponent.class, JumpComponent.class));

        Gdx.input.setInputProcessor(new InputMultiplexer(new GameInputAdapter()));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent cPlayer = mPlayer.get(entityId);
        Box2dRigidBodyComponent cBox2DRigidBody = mBox2dRigidBody.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        JumpComponent cJump = mJump.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        Vector2 vel = cBox2DRigidBody.body.getLinearVelocity();
        Vector2 pos = cBox2DRigidBody.body.getPosition();

        if (cPlayer.canWalk) {
            if (moveRight == moveLeft) {
                cRigidBody.velocity.x = 0;
            } else if (moveLeft) {
                cRigidBody.velocity.x = -cPlayer.walkSpeed;
            } else if (moveRight) {
                cRigidBody.velocity.x = cPlayer.walkSpeed;
            }
        }


        if (cJump.canJump && jump) {
            if (mCollidable.has(entityId)) {
                boolean wallJump = (cCollidable.onLeftWall || cCollidable.onRightWall) && cRigidBody.velocity.y < 0;

                if (cCollidable.onGround || wallJump) {
                    cRigidBody.velocity.y = cJump.jumpSpeed;
                }
            } else {
                cRigidBody.velocity.y = cJump.jumpSpeed;
            }
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
