package com.desenvolvedorindie.platformer.entity;

import com.artemis.ComponentMapper;
import com.artemis.EntityEdit;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;

public class EntitiesFactory {

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<CollidableComponent> mCollidable;

    private ComponentMapper<JumpComponent> mJump;

    public int createPlayer(World world, float x, float y) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);

        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new Sprite(new Texture("player/player.png"));

        PlayerComponent cPlayer = mPlayer.create(entity);

        RigidBodyComponent cRigidBody = mRigidBody.create(entity);

        CollidableComponent cCollidable = mCollidable.create(entity);

        JumpComponent cJump = mJump.create(entity);

        return entity;
    }

}
