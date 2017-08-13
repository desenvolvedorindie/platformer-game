package com.desenvolvedorindie.platformer.world;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.desenvolvedorindie.platformer.block.Block;

public class Physical {

    public static final float PPM = Block.TILE_SIZE * 1.5f;

    public static RigidBodyCreation createRigidBodyForEntity(Entity entity, com.badlogic.gdx.physics.box2d.World box2d, float x, float y, int width, int height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.allowSleep = false;
        bodyDef.fixedRotation = true;
        bodyDef.bullet = true;
        bodyDef.active = true;
        bodyDef.gravityScale = 1;

        Body body = box2d.createBody(bodyDef);

        body.setUserData(entity);

        CircleShape circle = new CircleShape();
        circle.setPosition(new Vector2(0, (height / 2 - width / 2) / PPM));
        circle.setRadius(width / 2 / PPM);

        FixtureDef fixtureDefHeader = new FixtureDef();
        fixtureDefHeader.shape = circle;
        fixtureDefHeader.friction = 0f;
        fixtureDefHeader.restitution = 0f;
        fixtureDefHeader.density = 0;

        Fixture fixture = body.createFixture(fixtureDefHeader);

        fixture.setUserData("header");

        circle.dispose();

        circle = new CircleShape();
        circle.setPosition(new Vector2(0, -(height / 2 - width / 2) / PPM));
        circle.setRadius(width / 2 / PPM);

        FixtureDef fixtureDefFooter = new FixtureDef();
        fixtureDefFooter.shape = circle;
        fixtureDefFooter.friction = 0f;
        fixtureDefFooter.restitution = 0f;
        fixtureDefFooter.density = 0;

        Fixture fixtureFooter = body.createFixture(fixtureDefFooter);

        fixtureFooter.setUserData("footer");

        circle.dispose();

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox((width - 2) / 2 / PPM, 2 / PPM, new Vector2(0, -height / 2 / PPM), 0);

        FixtureDef fixtureDefGround = new FixtureDef();
        fixtureDefGround.shape = rectangle;
        fixtureDefGround.isSensor = true;

        Fixture fixtureGround = body.createFixture(fixtureDefGround);
        fixtureGround.setUserData("ground");

        rectangle.dispose();

        rectangle = new PolygonShape();
        rectangle.setAsBox((width - 2) / 2 / PPM, 2 / PPM, new Vector2(0, height / 2 / PPM), 0);

        FixtureDef fixtureDefCeiling = new FixtureDef();
        fixtureDefCeiling.shape = rectangle;
        fixtureDefCeiling.isSensor = true;

        Fixture fixtureCeiling = body.createFixture(fixtureDefCeiling);
        fixtureCeiling.setUserData("ceiling");

        rectangle.dispose();

        rectangle = new PolygonShape();
        rectangle.setAsBox(2 / PPM, (height - 2) / 2 / PPM, new Vector2(-width / 2 / PPM, 0), 0);

        FixtureDef fixtureDefLeft = new FixtureDef();
        fixtureDefLeft.shape = rectangle;
        fixtureDefLeft.isSensor = true;

        Fixture fixtureLeft = body.createFixture(fixtureDefLeft);
        fixtureLeft.setUserData("left");

        rectangle.dispose();

        rectangle = new PolygonShape();
        rectangle.setAsBox(2 / PPM, (height - 2) / 2 / PPM, new Vector2(width / 2 / PPM, 0), 0);

        FixtureDef fixtureDefRight = new FixtureDef();
        fixtureDefRight.shape = rectangle;
        fixtureDefRight.isSensor = true;

        Fixture fixtureRight = body.createFixture(fixtureDefRight);
        fixtureRight.setUserData("right");

        rectangle.dispose();

        RigidBodyCreation rigidBodyCreation = new RigidBodyCreation();
        rigidBodyCreation.body = body;
        rigidBodyCreation.fixture = fixture;
        rigidBodyCreation.fixtureFooter = fixtureFooter;
        rigidBodyCreation.fixtureGround = fixtureGround;
        rigidBodyCreation.fixtureCeiling = fixtureCeiling;
        rigidBodyCreation.fixtureLeft = fixtureLeft;
        rigidBodyCreation.fixtureRight = fixtureRight;

        return rigidBodyCreation;
    }

    public static class RigidBodyCreation {
        public Body body;
        public Fixture fixture;
        public Fixture fixtureFooter;
        public Fixture fixtureGround;
        public Fixture fixtureCeiling;
        public Fixture fixtureLeft;
        public Fixture fixtureRight;

    }
}
