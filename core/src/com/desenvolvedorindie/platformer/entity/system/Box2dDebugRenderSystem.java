package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dDebugRenderSystem extends BaseSystem {

    private Box2DDebugRenderer debugRenderer;

    private World world;

    private Camera camera;

    public Box2dDebugRenderSystem(World world, Camera camera) {
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);
        this.world = world;
        this.camera = camera;
    }

    @Override
    protected void processSystem() {
        debugRenderer.render(world, camera.combined);
    }

    @Override
    protected void dispose() {
        debugRenderer.dispose();
    }
}
