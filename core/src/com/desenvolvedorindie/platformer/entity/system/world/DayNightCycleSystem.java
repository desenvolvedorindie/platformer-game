package com.desenvolvedorindie.platformer.entity.system.world;

import com.artemis.BaseSystem;

public class DayNightCycleSystem extends BaseSystem {

    private static final float DURATION = 12 * 60;

    private static final int SEGMENTS = 6;

    private static final float SEGMENT_DURATION = DURATION / SEGMENTS;

    private int day;

    private float time;

    private int lastSegment;

    @Override
    protected void processSystem() {
        float delta = world.delta;

        time += delta;

        if (time > DURATION) {
            time -= DURATION;
        }

        if (lastSegment != getCurrentSegment()) {
            if (lastSegment == SEGMENTS) {
                day++;
            }

            lastSegment = getCurrentSegment();
        }
    }


    public int getCurrentSegment() {
        return (int) (time / SEGMENT_DURATION);
    }

    public int getDay() {
        return day;
    }


}
