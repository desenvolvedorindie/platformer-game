package com.desenvolvedorindie.platformer.entity.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.entity.component.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.RigidBodyComponent;
import com.desenvolvedorindie.platformer.entity.component.TransformComponent;
import com.desenvolvedorindie.platformer.world.World;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;

public class CollisionDebugSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<CollidableComponent> mCollidable;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private World gameWorld;

    private Camera camera;

    private ShapeRenderer shapeRenderer;

    public CollisionDebugSystem(World world, Camera camera, ShapeRenderer shapeRenderer) {
        super(Aspect.all(TransformComponent.class, RigidBodyComponent.class, CollidableComponent.class));
        this.gameWorld = world;
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void begin() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(Line);
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        Vector2 min = cCollidable.collisionBox.getPosition(new Vector2());
        Vector2 max = cCollidable.collisionBox.getSize(new Vector2()).add(min);
        Vector2 size = cCollidable.collisionBox.getSize(new Vector2());
        Vector2 center = cCollidable.collisionBox.getCenter(new Vector2());

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
        shapeRenderer.setColor(Color.YELLOW);

        for (int x = 0; x < gameWorld.getWidth(); x++) {
            for (int y = 0; y < gameWorld.getHeight(); y++) {
                Rectangle rectangle = gameWorld.getTileRectangle(x, y);

                if (rectangle != null) {
                    shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
            }
        }

        shapeRenderer.end();
    }

    @Override
    protected void dispose() {
        shapeRenderer.dispose();
    }
}