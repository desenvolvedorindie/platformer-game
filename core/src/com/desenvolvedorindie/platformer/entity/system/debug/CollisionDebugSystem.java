package com.desenvolvedorindie.platformer.entity.system.debug;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.entity.component.basic.PositionComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;
import com.desenvolvedorindie.platformer.world.World;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;

public class CollisionDebugSystem extends IteratingSystem {

    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<CollidableComponent> mCollidable;

    private World gameWorld;

    private Camera camera;

    private ShapeRenderer shapeRenderer;

    Vector2 minTemp = new Vector2(), maxTemp = new Vector2(), sizeTemp = new Vector2(), centerTemp = new Vector2();

    public CollisionDebugSystem(World world, Camera camera, ShapeRenderer shapeRenderer) {
        super(Aspect.all(
                RigidBodyComponent.class,
                CollidableComponent.class)
        );
        this.gameWorld = world;
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void begin() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(Line);

        shapeRenderer.setColor(Color.YELLOW);

        for (int x = 0; x < gameWorld.getWidth(); x++) {
            for (int y = 0; y < gameWorld.getHeight(); y++) {
                Rectangle rectangle = gameWorld.getTileRectangle(x, y);

                if (rectangle != null) {
                    shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
            }
        }
    }

    @Override
    protected void process(int entityId) {
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        Vector2 min = cCollidable.collisionBox.getPosition(minTemp);
        Vector2 max = cCollidable.collisionBox.getSize(maxTemp).add(min);
        Vector2 size = cCollidable.collisionBox.getSize(sizeTemp);
        Vector2 center = cCollidable.collisionBox.getCenter(centerTemp);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(min.x, min.y, size.x, size.y);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.line(center.x, center.y, center.x + cRigidBody.velocity.x, center.y + cRigidBody.velocity.y);

        shapeRenderer.setColor(Color.RED);

        if (cCollidable.onGround) {
            shapeRenderer.line(min.x, min.y, max.x, min.y);
        }

        if (cCollidable.onCeiling) {
            shapeRenderer.line(min.x, max.y, max.x, max.y);
        }

        if (cCollidable.onLeftWall) {
            shapeRenderer.line(min.x, min.y, min.x, max.y);
        }

        if (cCollidable.onRightWall) {
            shapeRenderer.line(max.x, min.y, max.x, max.y);
        }
    }

    @Override
    protected void end() {
        shapeRenderer.end();
    }

    @Override
    protected void dispose() {
        shapeRenderer.dispose();
    }
}
