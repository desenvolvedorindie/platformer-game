package com.desenvolvedorindie.platformer.entity;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.desenvolvedorindie.platformer.entity.component.*;
import com.desenvolvedorindie.platformer.resource.Assets;
import com.desenvolvedorindie.platformer.resource.spriter.PlayerSpriterAnimationListener;
import net.spookygames.gdx.spriter.SpriterAnimator;

public class EntitiesFactory {

    private ComponentMapper<CollidableComponent> mCollidableComponent;

    private ComponentMapper<JumpComponent> mJump;

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<RigidBodyComponent> mRigiBody;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<SpriterAnimationComponent> mSpriterAnimationComponent;

    private ComponentMapper<StateComponent> mState;

    private ComponentMapper<TransformComponent> mTransform;

    public int createPlayer(World world, float x, float y) {
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

        /*
        StateComponent cState = mState.create(entity);
        cState.state = new DefaultStateMachine<Entity, PlayerState>(world.getEntity(entity), PlayerState.Idle);
        */

        JumpComponent cJump = mJump.create(entity);

        final RigidBodyComponent cRigidBody = mRigiBody.create(entity);

        /*
        CollidableComponent cCollidable = mCollidableComponent.create(entity);
        cCollidable.collisionBox.setPosition(new Vector2(x, y));
        cCollidable.collisionBox.setSize(width, height);
        */

        cSpriterAnimation.spriterAnimator.addAnimationListener(new PlayerSpriterAnimationListener());

        return entity;
    }

}
