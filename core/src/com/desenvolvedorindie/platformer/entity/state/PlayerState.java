package com.desenvolvedorindie.platformer.entity.state;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.desenvolvedorindie.platformer.entity.component.*;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.ScaleComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent;

public enum PlayerState implements State<Entity> {
    Idle {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent cCollidable = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent.class);
            StateComponent cState = entity.getComponent(StateComponent.class);
            com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent cRigidBody = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent.class);

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

            com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent cSpriterAnimation = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent.class);

            if (cSpriterAnimation != null)
                cSpriterAnimation.spriterAnimator.play("idle");
        }
    },
    Walk {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent cCollidable = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent.class);
            StateComponent cState = entity.getComponent(StateComponent.class);
            com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent cRigidBody = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent.class);

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

            com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent cSpriterAnimation = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent.class);

            if (cSpriterAnimation != null)
                cSpriterAnimation.spriterAnimator.play("walk");
        }
    },
    Jump {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent cRigidBody = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent.class);
            com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent cSpriterAnimation = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent.class);
            com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent cCollidable = entity.getComponent(CollidableComponent.class);
            StateComponent cState = entity.getComponent(StateComponent.class);
            PlayerComponent cPlayer = entity.getComponent(PlayerComponent.class);

            if (cRigidBody.velocity.y < 0) {
                if (!cPlayer.alreadyFalling) {
                    if (cSpriterAnimation != null)
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

            com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent cRigidBody = entity.getComponent(com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent.class);
            com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent cSpriterAnimation = entity.getComponent(SpriterAnimationComponent.class);

            if (cRigidBody.velocity.y > 0) {
                if (cSpriterAnimation != null)
                    cSpriterAnimation.spriterAnimator.play("jump_start");
            } else if (cRigidBody.velocity.y < 0) {
                if (cSpriterAnimation != null)
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
        com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent cRigidBodyComponent = entity.getComponent(RigidBodyComponent.class);
        ScaleComponent cScale = entity.getComponent(ScaleComponent.class);

        if (cRigidBodyComponent.velocity.x > 0) {
            cScale.scaleX = Math.abs(cScale.scaleX);
        } else if (cRigidBodyComponent.velocity.x < 0) {
            cScale.scaleX = -Math.abs(cScale.scaleX);
        }
    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }

}