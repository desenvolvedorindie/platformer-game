package com.desenvolvedorindie.platformer.entity.state;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.desenvolvedorindie.platformer.entity.component.*;

public enum PlayerState implements State<Entity> {
    Idle {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            CollidableComponent cCollidable = entity.getComponent(CollidableComponent.class);
            StateComponent cState = entity.getComponent(StateComponent.class);
            RigidBodyComponent cRigidBody = entity.getComponent(RigidBodyComponent.class);

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

            SpriterAnimationComponent cSpriterAnimation = entity.getComponent(SpriterAnimationComponent.class);

            cSpriterAnimation.spriterAnimator.play("idle");
        }
    },
    Walk {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            CollidableComponent cCollidable = entity.getComponent(CollidableComponent.class);
            StateComponent cState = entity.getComponent(StateComponent.class);
            RigidBodyComponent cRigidBody = entity.getComponent(RigidBodyComponent.class);

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

            SpriterAnimationComponent cSpriterAnimation = entity.getComponent(SpriterAnimationComponent.class);
            cSpriterAnimation.spriterAnimator.play("walk");
        }
    },
    Jump {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            RigidBodyComponent cRigidBody = entity.getComponent(RigidBodyComponent.class);
            SpriterAnimationComponent cSpriterAnimation = entity.getComponent(SpriterAnimationComponent.class);
            CollidableComponent cCollidable = entity.getComponent(CollidableComponent.class);
            StateComponent cState = entity.getComponent(StateComponent.class);
            PlayerComponent cPlayer = entity.getComponent(PlayerComponent.class);

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

            PlayerComponent cPlayer = entity.getComponent(PlayerComponent.class);

            cPlayer.alreadyFalling = false;
        }

        @Override
        public void enter(Entity entity) {
            super.enter(entity);

            RigidBodyComponent cRigidBody = entity.getComponent(RigidBodyComponent.class);
            SpriterAnimationComponent cSpriterAnimation = entity.getComponent(SpriterAnimationComponent.class);

            if (cRigidBody.velocity.y > 0) {
                cSpriterAnimation.spriterAnimator.play("jump_start");
            } else if (cRigidBody.velocity.y < 0) {
                cSpriterAnimation.spriterAnimator.play("fall_start");
            }
        }
    };

    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void exit(Entity entity) {

    }

    @Override
    public void update(Entity entity) {
        RigidBodyComponent cRigidBodyComponent = entity.getComponent(RigidBodyComponent.class);
        TransformComponent cTransform = entity.getComponent(TransformComponent.class);

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