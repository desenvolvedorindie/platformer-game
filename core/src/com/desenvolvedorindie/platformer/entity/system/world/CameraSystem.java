package com.desenvolvedorindie.platformer.entity.system.world;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.desenvolvedorindie.gdxcamera.constraint.*;
import com.desenvolvedorindie.platformer.block.Block;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.utils.Debuggable;
import com.desenvolvedorindie.platformer.world.World;

public class CameraSystem extends BaseSystem implements Debuggable {

    private static final float[] ZOOM_LEVELS = new float[]{
            6 / (float) Block.TILE_SIZE,
            1f,
            2,
            3,
    };
    private final Vector3 playerPositionV3 = new Vector3();

    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<PositionComponent> mTransform;

    private World gameWorld;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private CameraZoom cameraZoom;
    private CameraFollowConstraint cameraFollow;
    private CameraConstraintBoundingBox cameraConstraintBoundBox;
    private CameraConstraint cameraConstraint;
    private boolean debug;

    public CameraSystem(World world, Camera camera, ShapeRenderer shapeRenderer) {
        this.gameWorld = world;
        this.camera = camera;

        cameraZoom = new CameraZoom(ZOOM_LEVELS, 1f);

        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            cameraZoom.zoomIn();
        }

        cameraFollow = new CameraFollowConstraint(playerPositionV3, 1f);

        cameraConstraintBoundBox = new CameraConstraintBoundingBox(new BoundingBox(new Vector3(World.mapToWorld(2), World.mapToWorld(2), 0), new Vector3(World.mapToWorld(world.getWidth() - 2), World.mapToWorld(world.getHeight() - 2), 0)));

        cameraConstraint = new CameraConstraintMultiplexer(cameraZoom, cameraFollow, cameraConstraintBoundBox);
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void processSystem() {
        PositionComponent cPosition = mTransform.get(gameWorld.getPlayer());
        CollidableComponent cColidable = mCollidable.get(gameWorld.getPlayer());

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                cameraZoom.zoomOut();
            } else {
                cameraZoom.zoomIn();
            }
        }

        float delta = world.getDelta();

        Rectangle rectangle = cColidable.collisionBox;

        playerPositionV3.set(cPosition.position.x + rectangle.width / 2, cPosition.position.y + rectangle.height, 0);
        cameraConstraint.update(camera, delta);

        if (isDebug()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            cameraConstraint.debug(camera, shapeRenderer, delta);
            shapeRenderer.end();
        }
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
