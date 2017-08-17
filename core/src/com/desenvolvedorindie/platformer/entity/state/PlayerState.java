package com.desenvolvedorindie.platformer.entity.state;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.desenvolvedorindie.platformer.entity.component.*;

public enum PlayerState implements State<Entity> {
    Idle {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            CollidableComponent cCollidable = mCollidable.get(entity);
            StateComponent cState = mState.get(entity);
            RigidBodyComponent cRigidBody = mRigidBody.get(entity);

            if (cCollidable.onGround) {
                if (cRigidBody.velocity.x != 0) {
                    cState.state.changeState(Walk);
                }
            } else {
                cState.state.changeState(Jump);
            }
        }

        @Override
        public void enter(Entity entity) {
            super.enter(entity);

            SpriterAnimationComponent cSpriterAnimation = mSpriterAnimation.get(entity);

            cSpriterAnimation.spriterAnimator.play("idle");
        }
    },
    Walk {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            CollidableComponent cCollidable = mCollidable.get(entity);
            StateComponent cState = mState.get(entity);
            RigidBodyComponent cRigidBody = mRigidBody.get(entity);

            if (cCollidable.onGround) {
                if (cRigidBody.velocity.x == 0) {
                    cState.state.changeState(Idle);
                }
            } else {
                cState.state.changeState(Jump);
            }
        }

        @Override
        public void enter(Entity entity) {
            super.enter(entity);

            SpriterAnimationComponent cSpriterAnimation = mSpriterAnimation.get(entity);
            cSpriterAnimation.spriterAnimator.play("walk");
        }
    },
    Jump {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            RigidBodyComponent cRigidBody = mRigidBody.get(entity);
            SpriterAnimationComponent cSpriterAnimation = mSpriterAnimation.get(entity);
            CollidableComponent cCollidable = mCollidable.get(entity);
            StateComponent cState = mState.get(entity);
            PlayerComponent cPlayer = mPlayer.get(entity);

            if (cRigidBody.velocity.y < 0) {
                if (!cPlayer.alreadyFalling) {
                    cSpriterAnimation.spriterAnimator.play("fall_start");
                    cPlayer.alreadyFalling = true;
                }
            }

            if (cCollidable.onGround) {
                if (cRigidBody.velocity.x == 0) {
                    cState.state.changeState(Idle);
                } else {
                    cState.state.changeState(Walk);
                }
            }
        }

        @Override
        public void exit(Entity entity) {
            super.exit(entity);

            PlayerComponent cPlayer = mPlayer.get(entity);

            cPlayer.alreadyFalling = false;
        }

        @Override
        public void enter(Entity entity) {
            super.enter(entity);

            RigidBodyComponent cRigidBody = mRigidBody.get(entity);
            SpriterAnimationComponent cSpriterAnimation = mSpriterAnimation.get(entity);

            if (cRigidBody.velocity.y > 0) {
                cSpriterAnimation.spriterAnimator.play("jump_start");
            } else if (cRigidBody.velocity.y < 0) {
                cSpriterAnimation.spriterAnimator.play("fall_start");
            }
        }
    };

    protected ComponentMapper<CollidableComponent> mCollidable;

    protected ComponentMapper<RigidBodyComponent> mRigidBody;

    protected ComponentMapper<TransformComponent> mTransform;

    protected ComponentMapper<SpriterAnimationComponent> mSpriterAnimation;

    protected ComponentMapper<PlayerComponent> mPlayer;

    protected ComponentMapper<StateComponent> mState;

    private boolean alreadyInit;

    protected void init(Entity entity) {
        if (!alreadyInit) {
            entity.getWorld().inject(this);
            alreadyInit = true;
        }
    }

    @Override
    public void enter(Entity entity) {
        init(entity);
    }

    @Override
    public void exit(Entity entity) {

    }

    @Override
    public void update(Entity entity) {
        init(entity);

        RigidBodyComponent cRigidBodyComponent = mRigidBody.get(entity);
        TransformComponent cTransform = mTransform.get(entity);

        if (cRigidBodyComponent.velocity.x > 0) {
            cTransform.scaleX = Math.abs(cTransform.scaleX);
        } else if (cRigidBodyComponent.velocity.x < 0) {
            cTransform.scaleX = -Math.abs(cTransform.scaleX);
        }
    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }

}