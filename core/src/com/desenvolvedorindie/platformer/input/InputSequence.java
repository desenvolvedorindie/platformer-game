package com.desenvolvedorindie.platformer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.desenvolvedorindie.platformer.PlatformerGame;

public class InputSequence {

    private IInputButton[] sequence;

    private float time, duration;

    private int index;

    public InputSequence(float duration, IInputButton[] sequence) {
        this.duration = duration;
        this.sequence = sequence;
    }


    public boolean process() {
        boolean result = false;

        if (index > 0) {
            time += Gdx.graphics.getDeltaTime();
        }

        if (sequence[index].pressed()) {
            if (++index == size()) {
                result = true;
                reset();
            }
        } else if (sequence[index].anyPressed()) {
            reset();
        }

        if (time > duration) {
            reset();
        }

        return result;
    }

    public void reset() {
        time = 0;
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public int size() {
        return sequence.length;
    }

    public enum Type {
        KEY,
        BUTTON,
    }

    public interface IInputButton {
        boolean pressed();

        boolean anyPressed();
    }

    public static class MultipleInputButton implements IInputButton {

        public IInputButton[] inputButtons;

        public MultipleInputButton(IInputButton[] inputButtons) {
            this.inputButtons = inputButtons;
        }

        @Override
        public boolean pressed() {
            for (IInputButton inputButton : inputButtons) {
                if (inputButton.pressed()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean anyPressed() {
            for (IInputButton inputButton : inputButtons) {
                if (inputButton.anyPressed()) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class InputButton implements IInputButton {

        public Type type;

        public int button;

        public InputButton(Type type, int button) {
            this.type = type;
            this.button = button;
        }

        @Override
        public boolean pressed() {
            if (type == Type.KEY) {
                if (Gdx.input.isKeyJustPressed(button)) {
                    return true;
                }
            } else if (PlatformerGame.input.isButtonJustPressed(button)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean anyPressed() {
            return Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || PlatformerGame.input.isButtonJustPressed(GameInput.ANY_BUTTON);
        }
    }

}
