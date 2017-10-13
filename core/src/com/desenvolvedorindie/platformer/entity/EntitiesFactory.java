package com.desenvolvedorindie.platformer.entity;

import com.artemis.*;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.StateComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.*;
import com.desenvolvedorindie.platformer.entity.component.physic.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.render.GFXComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent;
import com.desenvolvedorindie.platformer.entity.state.PlayerState;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.resource.spriter.PlayerSpriterAnimationListener;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class EntitiesFactory {

    private World world;

    private Archetype playerArchetype, playerOtherArchetype;

    private ComponentMapper<CollidableComponent> mCollidableComponent;

    private ComponentMapper<JumpComponent> mJump;

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<RigidBodyComponent> mRigiBody;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<SpriterAnimationComponent> mSpriterAnimationComponent;

    private ComponentMapper<StateComponent> mState;

    private ComponentMapper<PositionComponent> mPosition;

    private ComponentMapper<OriginComponent> mOrigin;

    private ComponentMapper<ScaleComponent> mScale;

    private ComponentMapper<RotationComponent> mRotation;

    private ComponentMapper<TintComponent> mTint;

    private ComponentMapper<GFXComponent> mGFX;

    public EntitiesFactory(World world) {
        this.world = world;

        playerArchetype = new ArchetypeBuilder()
                .add(PositionComponent.class)
                .add(OriginComponent.class)
                .add(ScaleComponent.class)
                .add(RotationComponent.class)
                .add(TintComponent.class)
                .add(SpriterAnimationComponent.class)
                .add(StateComponent.class)
                .add(PlayerComponent.class)
                .add(JumpComponent.class)
                .add(RigidBodyComponent.class)
                .add(CollidableComponent.class)
                .build(world);

        playerOtherArchetype = new ArchetypeBuilder()
                .add(PositionComponent.class)
                .add(SpriterAnimationComponent.class)
                .build(world);
    }

    public int createPlayer(float x, float y) {
        int entity = world.create(playerArchetype);

        PositionComponent cTransform = mPosition.get(entity);
        mPosition.get(entity).position.set(x, y);

        int width = 14;
        int height = 14;

        SpriterAnimationComponent cSpriterAnimation = mSpriterAnimationComponent.get(entity);
        cSpriterAnimation.spriterAnimator = new SpriterAnimator(Assets.manager.get(Assets.grayGuy).entities.first());
        cSpriterAnimation.spriterAnimator.play("idle");
        cSpriterAnimation.spriterAnimator.addAnimationListener(new PlayerSpriterAnimationListener());

        ScaleComponent cScale = mScale.get(entity);

        cScale.scaleX = cScale.scaleY = 0.17f;

        PlayerComponent cPlayer = mPlayer.get(entity);

        StateComponent cState = mState.get(entity);
        cState.state = new DefaultStateMachine<Entity, PlayerState>(world.getEntity(entity), PlayerState.Idle);

        JumpComponent cJump = mJump.get(entity);

        final RigidBodyComponent cRigidBody = mRigiBody.get(entity);

        CollidableComponent cCollidable = mCollidableComponent.get(entity);
        cCollidable.collisionBox.setPosition(new Vector2(x, y));
        cCollidable.collisionBox.setSize(width, height);

        return entity;
    }

    public int createOtherPlayer(float x, float y) {
        int entity = world.create(playerOtherArchetype);

        PositionComponent cTransform = mPosition.get(entity);
        mPosition.get(entity).position.set(x, y);

        ScaleComponent cScale = mScale.get(entity);

        cScale.scaleX = cScale.scaleY = 0.17f;

        SpriterAnimationComponent cSpriterAnimation = mSpriterAnimationComponent.get(entity);
        cSpriterAnimation.spriterAnimator = new SpriterAnimator(Assets.manager.get(Assets.grayGuy).entities.first());
        cSpriterAnimation.spriterAnimator.play("idle");
        cSpriterAnimation.spriterAnimator.addAnimationListener(new PlayerSpriterAnimationListener());

        return entity;
    }

}
