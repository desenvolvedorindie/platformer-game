package com.desenvolvedorindie.platformer.world;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.entity.component.Box2dRigidBodyComponent;

import net.dermetfan.gdx.physics.box2d.ContactAdapter;

public class WorldContactListener extends ContactAdapter {

    @Override
    public void beginContact(Contact contact) {
        Object aUserData = contact.getFixtureA().getBody().getUserData();
        Object bUserData = contact.getFixtureB().getBody().getUserData();

        ContactListener contactListener;

        if (aUserData instanceof Entity) {
            contactListener = ((Entity) aUserData).getComponent(Box2dRigidBodyComponent.class).contactListener;

            if (contactListener != null) {
                contactListener.beginContact(contact);
            }
        } else if (aUserData instanceof Block) {
            contactListener = ((Block) aUserData).getContactListener();

            if (contactListener != null) {
                contactListener.beginContact(contact);
            }
        }

        if (bUserData instanceof Entity) {
            contactListener = ((Entity) bUserData).getComponent(Box2dRigidBodyComponent.class).contactListener;

            if (contactListener != null) {
                contactListener.beginContact(contact);
            }
        } else if (bUserData instanceof Block) {
            contactListener = ((Block) bUserData).getContactListener();

            if (contactListener != null) {
                contactListener.beginContact(contact);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Object aUserData = contact.getFixtureA().getBody().getUserData();
        Object bUserData = contact.getFixtureB().getBody().getUserData();

        ContactListener contactListener;

        if (aUserData instanceof Entity) {
            contactListener = ((Entity) aUserData).getComponent(Box2dRigidBodyComponent.class).contactListener;

            if (contactListener != null) {
                contactListener.endContact(contact);
            }
        } else if (aUserData instanceof Block) {
            contactListener = ((Block) aUserData).getContactListener();

            if (contactListener != null) {
                contactListener.endContact(contact);
            }
        }

        if (bUserData instanceof Entity) {
            contactListener = ((Entity) bUserData).getComponent(Box2dRigidBodyComponent.class).contactListener;

            if (contactListener != null) {
                contactListener.endContact(contact);
            }
        } else if (bUserData instanceof Block) {
            contactListener = ((Block) bUserData).getContactListener();

            if (contactListener != null) {
                contactListener.endContact(contact);
            }
        }
    }
}
