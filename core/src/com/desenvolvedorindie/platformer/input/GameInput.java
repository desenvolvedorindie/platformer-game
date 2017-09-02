package com.desenvolvedorindie.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameInput {

    public static final int ANY_BUTTON = -1;

    private final int MAX_BUTTONS = 5;

    private boolean[] buttonsCurrentState = new boolean[MAX_BUTTONS], buttonsLastState = new boolean[MAX_BUTTONS];

    private boolean buttonJustPressed;

    public GameInput() {
        for (int i = 0; i < MAX_BUTTONS; i++) {
            buttonsCurrentState[i] = false;
            buttonsLastState[i] = false;
        }
    }

    public void process() {
        buttonJustPressed = false;
        for (int button = 0; button < MAX_BUTTONS; button++) {
            buttonsLastState[button] = buttonsCurrentState[button];
            buttonsCurrentState[button] = Gdx.input.isButtonPressed(button);
            buttonJustPressed |= buttonsCurrentState[button];
        }
    }

    public boolean isButtonStillReleased(int key) {
        return !buttonsCurrentState[key] && !buttonsLastState[key];
    }

    public boolean isButtonJustPressed(int key) {
        if (key == Input.Keys.ANY_KEY) {
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

}
