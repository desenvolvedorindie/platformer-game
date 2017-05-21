package com.desenvolvedorindie.platformer.entity;

import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.SpriteComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;

public class EntitiesFactory {

    public static Entity createPlayer(World world, float x, float y) {
        Entity entity = world.createEntity();

        EntityEdit entityEdit = entity.edit();

        TransformComponent cTransform = new TransformComponent();
        cTransform.position.set(x, y);

        entityEdit.add(cTransform);

        SpriteComponent cSprite = new SpriteComponent();
        cSprite.sprite = new Sprite(new Texture("player/player.png"));

        entityEdit.add(cSprite);

        PlayerComponent cPlayer = new PlayerComponent();

        entityEdit.add(cPlayer);

        return entity;
    }

}
