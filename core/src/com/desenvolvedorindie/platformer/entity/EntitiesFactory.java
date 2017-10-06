package com.desenvolvedorindie.platformer.entity;

import com.artemis.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.StateComponent;
import com.desenvolvedorindie.platformer.entity.component.base.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.base.TransformComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.render.GFXComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.render.SpriterAnimationComponent;
import com.desenvolvedorindie.platformer.entity.state.PlayerState;
import com.desenvolvedorindie.platformer.entity.system.render.SpriterAnimationRenderSystem;
import com.desenvolvedorindie.platformer.graphics.fx.Outline;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.resource.spriter.PlayerSpriterAnimationListener;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class EntitiesFactory {

    private World world;

    private Archetype playerArchetype;

    private ComponentMapper<CollidableComponent> mCollidableComponent;

    private ComponentMapper<JumpComponent> mJump;

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<RigidBodyComponent> mRigiBody;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<SpriterAnimationComponent> mSpriterAnimationComponent;

    private ComponentMapper<StateComponent> mState;

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<GFXComponent> mGFX;

    public EntitiesFactory(World world) {
        this.world = world;

        playerArchetype = new ArchetypeBuilder()
                .add(TransformComponent.class)
                .add(SpriterAnimationComponent.class)
                .add(StateComponent.class)
                .add(PlayerComponent.class)
                .add(JumpComponent.class)
                .add(RigidBodyComponent.class)
                .add(CollidableComponent.class)
                .add(GFXComponent.class)
                .build(world);
    }

    public int createPlayer(float x, float y) {
        int entity = world.create(playerArchetype);

        TransformComponent cTransform = mTransform.get(entity);
        mTransform.get(entity).position.set(x, y);

        int width = 14;
        int height = 14;

        SpriterAnimationComponent cSpriterAnimation = mSpriterAnimationComponent.get(entity);
        cSpriterAnimation.spriterAnimator = new SpriterAnimator(Assets.manager.get(Assets.grayGuy).entities.first());
        cSpriterAnimation.spriterAnimator.play("idle");
        cSpriterAnimation.spriterAnimator.addAnimationListener(new PlayerSpriterAnimationListener());

        cTransform.scaleX = cTransform.scaleY = 0.17f;

        PlayerComponent cPlayer = mPlayer.get(entity);

        StateComponent cState = mState.get(entity);
        cState.state = new DefaultStateMachine<Entity, PlayerState>(world.getEntity(entity), PlayerState.Idle);

        JumpComponent cJump = mJump.get(entity);

        final RigidBodyComponent cRigidBody = mRigiBody.get(entity);

        CollidableComponent cCollidable = mCollidableComponent.get(entity);
        cCollidable.collisionBox.setPosition(new Vector2(x, y));
        cCollidable.collisionBox.setSize(width, height);

        GFXComponent cGFX = mGFX.get(entity);


        return entity;
    }

}
