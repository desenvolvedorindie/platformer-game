package com.desenvolvedorindie.platformer.entity;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.entity.component.Box2dRigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.SpriterAnimationComponent;
import com.desenvolvedorindie.platformer.entity.component.StateComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.entity.state.PlayerState;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.resource.spriter.PlayerSpriterAnimationListener;
import com.desenvolvedorindie.platformer.world.Physical;

import net.spookygames.gdx.spriter.SpriterAnimator;

public class EntitiesFactory {

    private ComponentMapper<Box2dRigidBodyComponent> mBox2dRigidBody;

    private ComponentMapper<CollidableComponent> mCollidableComponent;

    private ComponentMapper<JumpComponent> mJump;

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<RigidBodyComponent> mRigiBody;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<SpriterAnimationComponent> mSpriterAnimationComponent;

    private ComponentMapper<StateComponent> mState;

    private ComponentMapper<TransformComponent> mTransform;

    public int createPlayer(World world, com.badlogic.gdx.physics.box2d.World box2d, float x, float y) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);

        Texture texture = Assets.manager.get(Assets.player);

        int width = texture.getWidth();
        int height = texture.getHeight();

        /*
        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new Sprite(texture);
        */
        SpriterAnimationComponent cSpriterAnimation = mSpriterAnimationComponent.create(entity);
        cSpriterAnimation.spriterAnimator = new SpriterAnimator(Assets.manager.get(Assets.grayGuy).entities.first());
        cSpriterAnimation.spriterAnimator.play("idle");

        cTransform.scaleX = cTransform.scaleY = 0.17f;

        PlayerComponent cPlayer = mPlayer.create(entity);

        StateComponent cState = mState.create(entity);
        cState.state = new DefaultStateMachine<Entity, PlayerState>(world.getEntity(entity), PlayerState.Idle);

        JumpComponent cJump = mJump.create(entity);

        final RigidBodyComponent cRigidBody = mRigiBody.create(entity);

        CollidableComponent cCollidable = mCollidableComponent.create(entity);
        cCollidable.collisionBox.setPosition(new Vector2(x, y));
        cCollidable.collisionBox.setSize(width, height);

        Box2dRigidBodyComponent cBox2dRigidBody = mBox2dRigidBody.create(entity);

        Physical.RigidBodyCreation rigidBodyCreation = Physical.createRigidBodyForEntity(world.getEntity(entity), box2d, x, y, width, height);

        cBox2dRigidBody.body = rigidBodyCreation.body;
        cBox2dRigidBody.fixture = rigidBodyCreation.fixtureFooter;
        cBox2dRigidBody.fixtureGround = rigidBodyCreation.fixtureGround;
        cBox2dRigidBody.fixtureCeiling = rigidBodyCreation.fixtureCeiling;
        cBox2dRigidBody.fixtureLeft = rigidBodyCreation.fixtureLeft;
        cBox2dRigidBody.fixtureRight = rigidBodyCreation.fixtureRight;

        cSpriterAnimation.spriterAnimator.addAnimationListener(new PlayerSpriterAnimationListener());

        return entity;
    }

}
