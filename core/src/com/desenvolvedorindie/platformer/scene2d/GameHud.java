package com.desenvolvedorindie.platformer.scene2d;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.desenvolvedorindie.platformer.PlatformerGame;
import com.desenvolvedorindie.platformer.input.InputSequence;
import com.desenvolvedorindie.platformer.inventory.Inventory;
import com.desenvolvedorindie.platformer.scene2d.utils.DirectionalInputListener;

public class GameHud extends WidgetGroup {

    public static final int ACTIONBAR_SLOTS = 9;

    private final int MAX_BUTTONS = 6;
    boolean buttonJustPressed;
    private ImageButton up, down, left, right, b;
    private boolean[] buttonsState = new boolean[MAX_BUTTONS], buttonsCurrentState = new boolean[MAX_BUTTONS], buttonsLastState = new boolean[MAX_BUTTONS];
    private ScaleToAction leftScaleToAction, rightScaleToAction, upScaleToAction, downScaleToAction, bScaleToAction;
    private Image actionbar;

    private final Skin skin;

    private boolean includeMobile;

    private ButtonGroup<SlotActor> invetoryBar = new ButtonGroup<SlotActor>();

    DragAndDrop dragAndDrop = new DragAndDrop();

    private InventoryActor inventoryActor;

    private Actor current;

    private GameHudListener hudListener;

    public GameHud(Skin skin, boolean includeMobile, float width, float height) {
        this.skin = skin;
        this.includeMobile = includeMobile;

        invetoryBar.setMaxCheckCount(1);
        invetoryBar.setMaxCheckCount(1);

        dragAndDrop.setDragActorPosition(8, -8);

        actionbar = new Image(skin, "actionbar");
        actionbar.setPosition(width / 2 - actionbar.getWidth() / 2,height - actionbar.getHeight());
        addActor(actionbar);

        inventoryActor = new InventoryActor(new Inventory(), dragAndDrop, skin);
        inventoryActor.setPosition(width / 2 - inventoryActor.getWidth() / 2 + 1, height - inventoryActor.getHeight() / 2 - 18);
        addActor(inventoryActor);

        //invetoryBar.uncheckAll();
        //invetoryBar.getButtons().get(0).setChecked(true);

        if (includeMobile) {
            float duration = 1f;
            float scale = 1.5f;
            Vector2 center = new Vector2(60, 60);

            upScaleToAction = new ScaleToAction();
            upScaleToAction.setDuration(duration);
            upScaleToAction.setInterpolation(Interpolation.linear);
            upScaleToAction.setScale(scale);

            up = new ImageButton(skin, "up");
            up.setPosition(center.x - 22, center.y + 11);
            up.setOrigin(Align.center);
            up.setTransform(true);
            up.addListener(new DirectionalInputListener(upScaleToAction) {
                @Override
                public boolean pressed() {
                    buttonsState[Buttons.UP] = true;
                    current = up;
                    sort();
                    return hudListener != null && hudListener.buttonDown(Buttons.UP);
                }

                @Override
                public void cancel() {
                    buttonsState[Buttons.UP] = false;
                    up.setScale(1f);
                    if (hudListener != null) {
                        hudListener.buttonUp(Buttons.UP);
                    }
                }
            });
            addActor(up);

            downScaleToAction = new ScaleToAction();
            downScaleToAction.setDuration(duration);
            downScaleToAction.setInterpolation(Interpolation.linear);
            downScaleToAction.setScale(scale);

            down = new ImageButton(skin, "down");
            down.setPosition(center.x - 22, center.y - 54);
            down.setOrigin(Align.center);
            down.setTransform(true);
            down.addListener(new DirectionalInputListener(downScaleToAction) {
                @Override
                public boolean pressed() {
                    current = down;
                    sort();
                    buttonsState[Buttons.DOWN] = true;
                    return hudListener != null && hudListener.buttonDown(Buttons.DOWN);
                }

                @Override
                public void cancel() {
                    down.setScale(1f);
                    buttonsState[Buttons.DOWN] = false;
                    if (hudListener != null) {
                        hudListener.buttonUp(Buttons.DOWN);
                    }
                }
            });
            addActor(down);

            leftScaleToAction = new ScaleToAction();
            leftScaleToAction.setDuration(duration);
            leftScaleToAction.setInterpolation(Interpolation.linear);
            leftScaleToAction.setScale(scale);

            left = new ImageButton(skin, "left");
            left.setPosition(center.x - 55, center.y - 21);
            left.setOrigin(Align.center);
            left.setTransform(true);
            left.addListener(new DirectionalInputListener(leftScaleToAction) {
                @Override
                public boolean pressed() {
                    current = left;
                    sort();
                    buttonsState[Buttons.LEFT] = true;
                    return hudListener != null && hudListener.buttonDown(Buttons.LEFT);
                }

                @Override
                public void cancel() {
                    left.setScale(1f);
                    buttonsState[Buttons.LEFT] = false;
                    if (hudListener != null) {
                        hudListener.buttonUp(Buttons.LEFT);
                    }
                }
            });
            addActor(left);

            rightScaleToAction = new ScaleToAction();
            rightScaleToAction.setDuration(duration);
            rightScaleToAction.setInterpolation(Interpolation.linear);
            rightScaleToAction.setScale(scale);

            right = new ImageButton(skin, "right");
            right.setPosition(center.x + 11, center.y - 22);
            right.setOrigin(Align.center);
            right.setTransform(true);
            right.addListener(new DirectionalInputListener(rightScaleToAction) {
                @Override
                public boolean pressed() {
                    current = right;
                    sort();
                    buttonsState[Buttons.RIGHT] = true;
                    return hudListener != null && hudListener.buttonDown(Buttons.RIGHT);
                }

                @Override
                public void cancel() {
                    right.setScale(1f);
                    buttonsState[Buttons.RIGHT] = false;
                    if (hudListener != null) {
                        hudListener.buttonUp(Buttons.RIGHT);
                    }
                }
            });
            addActor(right);

            bScaleToAction = new ScaleToAction();
            bScaleToAction.setDuration(duration);
            bScaleToAction.setInterpolation(Interpolation.linear);
            bScaleToAction.setScale(scale);

            b = new ImageButton(skin, "b");
            b.setPosition(PlatformerGame.UI_WIDTH - 4 - b.getWidth(), 4);
            b.setOrigin(Align.center);
            b.setTransform(true);
            b.addListener(new DirectionalInputListener(bScaleToAction) {
                @Override
                public boolean pressed() {
                    current = b;
                    sort();
                    buttonsState[Buttons.B] = true;
                    return hudListener != null && hudListener.buttonDown(Buttons.B);
                }

                @Override
                public void cancel() {
                    b.setScale(1f);
                    buttonsState[Buttons.B] = false;
                    if (hudListener != null) {
                        hudListener.buttonUp(Buttons.B);
                    }
                }
            });
            addActor(b);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        buttonJustPressed = false;
        for (int button = 0; button < MAX_BUTTONS; button++) {
            buttonsLastState[button] = buttonsState[button];
            buttonsCurrentState[button] = isButtonPressed(button);
            buttonJustPressed |= buttonsCurrentState[button];
        }
    }

    private void sort() {
        current.setZIndex(current.getStage().getActors().size);

        int z = current.getStage().getActors().size;

        for (Actor actor : current.getStage().getActors()) {
            if (actor != current) {
                actor.setZIndex(--z);
            }
        }
    }

    public GameHudListener getHudListener() {
        return hudListener;
    }

    public void setHudListener(GameHudListener hudListener) {
        this.hudListener = hudListener;
    }

    public boolean isButtonPressed(int button) {
        return buttonsState[button];
    }

    public boolean isButtonStillReleased(int key) {
        return !buttonsCurrentState[key] && !buttonsLastState[key];
    }

    public boolean isButtonJustPressed(int key) {
        if (key == Buttons.ANY_BUTTON) {
            return buttonJustPressed;
        }

        return buttonsCurrentState[key] && !buttonsLastState[key];
    }

    public boolean isButtonJustReleased(int keycode) {
        return !buttonsCurrentState[keycode] && buttonsLastState[keycode];
    }

    public boolean isButtonStillPressed(int keycode) {
        return buttonsCurrentState[keycode] && buttonsLastState[keycode];
    }

    public interface GameHudListener {

        boolean buttonDown(int buttoncode);

        boolean buttonUp(int buttoncode);

    }

    static public class Buttons {
        public static final int ANY_BUTTON = -1;
        public static final int UP = 0;
        public static final int DOWN = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
        public static final int A = 4;
        public static final int B = 5;
    }

    public class GameHudInputButton implements InputSequence.IInputButton {

        private int button;

        public GameHudInputButton(int button) {
            this.button = button;
        }

        @Override
        public boolean pressed() {
            return isButtonJustPressed(button);
        }

        @Override
        public boolean anyPressed() {
            return isButtonJustPressed(Buttons.ANY_BUTTON);
        }

    }
}
