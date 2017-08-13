package com.desenvolvedorindie.platformer.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;
import static com.desenvolvedorindie.platformer.world.Physical.PPM;

public class Block {

    public static final int TILE_SIZE = 16;
    public static final int TILE_HALF_SIZE = TILE_SIZE / 2;

    public final Texture texture;

    public Block(Texture texture) {
        this.texture = texture;
    }

    public boolean isSolid() {
        return true;
    }

    public Body createBody(World box2d, int x, int y) {
        if (isSolid()) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = StaticBody;
            bodyDef.position.set((x * TILE_SIZE + TILE_HALF_SIZE)/* / PPM*/, (y * TILE_SIZE + TILE_HALF_SIZE)/* / PPM*/);
            bodyDef.allowSleep = true;
            bodyDef.awake = false;
            bodyDef.active = true;

            Body body = box2d.createBody(bodyDef);

            body.setUserData(this);

            PolygonShape rectangle = new PolygonShape();
            rectangle.setAsBox(TILE_HALF_SIZE/* / PPM*/, TILE_HALF_SIZE/* / PPM*/);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = rectangle;
            fixtureDef.friction = 0f;
            fixtureDef.restitution = 0f;
            fixtureDef.density = 0;
            fixtureDef.isSensor = false;

            Fixture fixture = body.createFixture(fixtureDef);

            fixture.setUserData("tile: " + x + ", " + y);

            rectangle.dispose();

            return body;
        } else {
            return null;
        }
    }

    public ContactListener getContactListener() {
        return null;
    }

}
