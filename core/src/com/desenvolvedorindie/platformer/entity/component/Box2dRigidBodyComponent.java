package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.desenvolvedorindie.platformer.block.Block;

import net.dermetfan.gdx.physics.box2d.ContactAdapter;

public class Box2dRigidBodyComponent extends Component {

    public static final ContactListener DEFAULT_CONTACT_LISTENER = new ContactAdapter() {

        @Override
        public void beginContact(Contact contact) {
            Object aUserData = contact.getFixtureA().getBody().getUserData();
            Object bUserData = contact.getFixtureB().getBody().getUserData();

            if (aUserData instanceof Entity) {
                Entity entity = (Entity) aUserData;

                if (bUserData instanceof Block) {
                    if(contact.getFixtureA().getUserData().equals("ground")) {
                        entity.getComponent(Box2dRigidBodyComponent.class).onGround = true;
                    }
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
            Object aUserData = contact.getFixtureA().getBody().getUserData();
            Object bUserData = contact.getFixtureB().getBody().getUserData();

            if (aUserData instanceof Entity) {
                Entity entity = (Entity) aUserData;

                if (bUserData instanceof Block) {
                    if(contact.getFixtureA().getUserData().equals("ground")) {
                        entity.getComponent(Box2dRigidBodyComponent.class).onGround = false;
                    }
                }
            }
        }

    };

    public Body body;

    public Fixture fixture;

    public Fixture fixtureGround;

    public Fixture fixtureCeiling;

    public Fixture fixtureLeft;

    public Fixture fixtureRight;

    public ContactListener contactListener = DEFAULT_CONTACT_LISTENER;

    public boolean onGround;
}
