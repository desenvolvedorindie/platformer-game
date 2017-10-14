package com.desenvolvedorindie.platformer.entity.effects;

public abstract class StatusEffect implements IStatusEffect {

    private float duration;

    public StatusEffect(float duration) {
        this.duration = duration;
    }

    @Override
    public void update(float delta) {
        duration -= delta;
    }

    @Override
    public boolean hasFinished() {
        return duration < 0;
    }
}
