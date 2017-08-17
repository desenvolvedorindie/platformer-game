package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

public class DebugSystem extends BaseSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private final Vector3 screenCoordinate = new Vector3();

    private World gameWorld;

    private Camera camera;

    public DebugSystem(World world, Camera camera) {
        gameWorld = world;
        this.camera = camera;
    }

    @Override
    protected void processSystem() {
        TransformComponent cTransform = mTransform.get(gameWorld.getPlayer());
        RigidBodyComponent cRigidBody = mRigidBody.get(gameWorld.getPlayer());

        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                if (gameWorld.getEntityTrackerWindow() != null) {
                    gameWorld.getEntityTrackerWindow().setVisible(!gameWorld.getEntityTrackerWindow().isVisible());
                }
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched()) {
            screenCoordinate.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(screenCoordinate);

            cTransform.position.set(screenCoordinate.x, screenCoordinate.y);
            cRigidBody.velocity.set(Vector2.Zero);
        }
    }
}
