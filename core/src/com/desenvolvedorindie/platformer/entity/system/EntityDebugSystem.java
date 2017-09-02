package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;

public class EntityDebugSystem extends BaseSystem {

    private final Vector3 screenCoordinate = new Vector3();
    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private Camera camera;

    private int entity;

    public EntityDebugSystem(Camera camera, int entity) {
        this.camera = camera;
        this.entity = entity;
    }

    @Override
    protected void processSystem() {
        PlayerComponent cPlayer = mPlayer.get(this.entity);
        TransformComponent cTransform = mTransform.get(this.entity);
        RigidBodyComponent cRigidBody = mRigidBody.get(this.entity);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched()) {
            screenCoordinate.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(screenCoordinate);

            cTransform.position.set(screenCoordinate.x, screenCoordinate.y);
            cRigidBody.velocity.set(Vector2.Zero);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            cPlayer.godMode = !cPlayer.godMode;
        }
    }
}
