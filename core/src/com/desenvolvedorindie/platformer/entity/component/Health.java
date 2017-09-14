package com.desenvolvedorindie.platformer.entity.component;

import com.artemis.annotations.PooledWeaver;

@PooledWeaver
public class Health {

    public int hp;
    public int hpMax;

    public Health() {

    }

    public Health(int startHealth) {
        setStartHealth(startHealth);
    }

    public void setStartHealth(int amount) {
        this.hp = amount;
        this.hpMax = amount;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public void reset() {
        this.hp = this.hpMax;
    }

}
