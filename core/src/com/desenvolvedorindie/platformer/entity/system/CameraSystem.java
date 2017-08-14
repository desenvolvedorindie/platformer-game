package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.desenvolvedorindie.gdxcamera.constraint.*;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

public class CameraSystem extends BaseSystem {

    private static final float[] ZOOM_LEVELS = new float[]{
            6 / (float) Block.TILE_SIZE,
            1f,
            2,
            3,
    };
    private final Vector3 playerPositionV3 = new Vector3();
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<TransformComponent> mTransform;
    private World gameWorld;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private CameraZoom cameraZoom;
    private CameraFollowConstraint cameraFollow;
    private CameraConstraintBoundingBox cameraConstraintBoundBox;
    private CameraConstraint cameraConstraint;

    public CameraSystem(World world, Camera camera) {
        this.gameWorld = world;
        this.camera = camera;

        shapeRenderer = new ShapeRenderer();

        cameraZoom = new CameraZoom(ZOOM_LEVELS, 1f);

        cameraFollow = new CameraFollowConstraint(playerPositionV3, 1f);

        cameraConstraintBoundBox = new CameraConstraintBoundingBox(new BoundingBox(new Vector3(World.mapToWorld(2), World.mapToWorld(2), 0), new Vector3(World.mapToWorld(world.getWidth() - 2), World.mapToWorld(world.getHeight() - 2), 0)));

        cameraConstraint = new CameraConstraintMultiplexer(cameraZoom, cameraFollow, cameraConstraintBoundBox);
    }

    @Override
    protected void processSystem() {
        CollidableComponent cColidable = mCollidable.get(gameWorld.getPlayer());
        TransformComponent cTransform = mTransform.get(gameWorld.getPlayer());

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cameraZoom.zoomOut();
            } else {
                cameraZoom.zoomIn();

            }
        }

        float delta = world.getDelta();

        Rectangle rectangle = cColidable.collisionBox;

        playerPositionV3.set(cTransform.position.x + rectangle.width / 2, cTransform.position.y + rectangle.height, 0);
        cameraConstraint.update(camera, delta);

        if (PlatformerGame.DEBUG) {
            cameraConstraint.debug(camera, shapeRenderer, delta);
        }
    }

}
