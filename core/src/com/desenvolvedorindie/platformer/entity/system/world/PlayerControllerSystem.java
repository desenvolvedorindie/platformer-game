package com.desenvolvedorindie.platformer.entity.system.world;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.desenvolvedorindie.platformer.entity.command.Command;
import com.desenvolvedorindie.platformer.entity.command.JumpCommand;
import com.desenvolvedorindie.platformer.entity.command.StopWalkCommand;
import com.desenvolvedorindie.platformer.entity.command.WalkCommand;
import com.desenvolvedorindie.platformer.entity.component.PlayerComponent;
import com.desenvolvedorindie.platformer.entity.component.WalkerComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.CollidableComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.JumpComponent;
import com.desenvolvedorindie.platformer.entity.component.physic.RigidBodyComponent;
import com.desenvolvedorindie.platformer.input.InputSequence;
import com.desenvolvedorindie.platformer.scene2d.GameHud;

public class PlayerControllerSystem extends IteratingSystem {

    public InputSequence especial, especial2;
    public boolean moveRight;
    public boolean moveLeft;
    public boolean jump;
    private Command jumpCommand, stopCommand, walkLeftCommand, walkRightCommand;
    private int leftKey = Input.Keys.A, rightKey = Input.Keys.D, downKey = Input.Keys.S, upKey = Input.Keys.W, jumpKey = Input.Keys.SPACE;
    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<JumpComponent> mJump;
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<WalkerComponent> mWalker;
    private PlayerInputAdapter playerInputAdapter;

    public PlayerControllerSystem() {
        super(Aspect.all(
                PlayerComponent.class,
                JumpComponent.class
        ));

        Controllers.addListener(new PlayerControllerListener());

        playerInputAdapter = new PlayerInputAdapter();

        jumpCommand = new JumpCommand();
        stopCommand = new StopWalkCommand();
        walkLeftCommand = new WalkCommand(-1);
        walkRightCommand = new WalkCommand(1);

        especial = new InputSequence(0.5f, new InputSequence.IInputButton[]{
                new InputSequence.MultipleInputButton(new InputSequence.IInputButton[]{
                        new InputSequence.InputButton(InputSequence.Type.KEY, downKey),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.DOWN),
                }),
                new InputSequence.MultipleInputButton(new InputSequence.IInputButton[]{
                        new InputSequence.InputButton(InputSequence.Type.KEY, leftKey),
                        new InputSequence.InputButton(InputSequence.Type.KEY, rightKey),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.LEFT),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.RIGHT),
                }),
                new InputSequence.MultipleInputButton(new InputSequence.IInputButton[]{
                        new InputSequence.InputButton(InputSequence.Type.BUTTON, Input.Buttons.LEFT),
                        //gameHud.new GameHudInputButton(GameHud.Buttons.B),
                }),
        });

        especial2 = new InputSequence(0.5f, new InputSequence.IInputButton[]{
                new InputSequence.InputButton(InputSequence.Type.KEY, upKey),
                new InputSequence.InputButton(InputSequence.Type.KEY, downKey),
                new InputSequence.MultipleInputButton(new InputSequence.IInputButton[]{
                        new InputSequence.InputButton(InputSequence.Type.KEY, leftKey),
                        new InputSequence.InputButton(InputSequence.Type.KEY, rightKey),
                }),
        });
    }

    @Override
    protected void process(int entityId) {
        Entity entity = world.getEntity(entityId);

        PlayerComponent cPlayer = mPlayer.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        JumpComponent cJump = mJump.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        WalkerComponent cWalker = mWalker.get(entityId);

        if (especial.process()) {
            Gdx.app.log("Sequence", "Especial");
        }

        if (especial2.process()) {
            Gdx.app.log("Sequence", "Especial 2");
        }

        if (cWalker.canWalk) {
            if (moveRight == moveLeft) {
                stopCommand.execute(entity);
            } else if (moveLeft) {
                walkLeftCommand.execute(entity);
            } else if (moveRight) {
                walkRightCommand.execute(entity);
            }
        }

        if (!jump && cRigidBody.velocity.y > 0.0f) {
            cRigidBody.velocity.y = Math.min(cRigidBody.velocity.y, cJump.minJumpSpeed);
        }

        if (jump) {
            jumpCommand.execute(entity);
        }
    }

    public PlayerInputAdapter getPlayerInputAdapter() {
        return playerInputAdapter;
    }

    private class PlayerControllerListener implements ControllerListener {

        @Override
        public void connected(Controller controller) {

        }

        @Override
        public void disconnected(Controller controller) {

        }

        @Override
        public boolean buttonDown(Controller controller, int buttonCode) {
            Gdx.app.log(controller.getName(), String.valueOf(buttonCode));
            if (buttonCode == 96) {
                jump = true;
            }
            return true;
        }

        @Override
        public boolean buttonUp(Controller controller, int buttonCode) {
            if (buttonCode == 96) {
                jump = false;
            }
            return true;
        }

        @Override
        public boolean axisMoved(Controller controller, int axisCode, float value) {
            Gdx.app.log(controller.getName() + "-axis:" + axisCode, String.valueOf(value));
            if (axisCode == 6) {
                if (value == 1) {
                    moveRight = true;
                } else if (value == -1) {
                    moveLeft = true;
                } else {
                    moveRight = false;
                    moveLeft = false;
                }
            }
            return true;
        }

        @Override
        public boolean povMoved(Controller controller, int povCode, PovDirection value) {
            Gdx.app.log(controller.getName() + "-pov:" + povCode, String.valueOf(value));

            return true;
        }

        @Override
        public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
            return false;
        }

        @Override
        public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
            return false;
        }

        @Override
        public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
            return false;
        }
    }

    private class PlayerInputAdapter extends InputAdapter implements GameHud.GameHudListener {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == rightKey) {
                moveRight = true;
            } else if (keycode == leftKey) {
                moveLeft = true;
            } else if (keycode == jumpKey) {
                jump = true;
            }
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == rightKey) {
                moveRight = false;
            } else if (keycode == leftKey) {
                moveLeft = false;
            } else if (keycode == jumpKey) {
                jump = false;
            }
            return true;
        }

        @Override
        public boolean buttonDown(int buttoncode) {
            switch (buttoncode) {
                case GameHud.Buttons.RIGHT:
                    moveRight = true;
                    break;
                case GameHud.Buttons.LEFT:
                    moveLeft = true;
                    break;
                case GameHud.Buttons.B:
                    jump = true;
                    break;
            }
            return true;
        }

        @Override
        public boolean buttonUp(int buttoncode) {
            switch (buttoncode) {
                case GameHud.Buttons.RIGHT:
                    moveRight = false;
                    break;
                case GameHud.Buttons.LEFT:
                    moveLeft = false;
                    break;
                case GameHud.Buttons.B:
                    jump = false;
                    break;
            }
            return true;
        }
    }

}
