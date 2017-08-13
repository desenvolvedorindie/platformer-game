package com.desenvolvedorindie.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class FPSLogger extends com.badlogic.gdx.graphics.FPSLogger {

    private long startTime;

    private String titleName;

    private boolean debugInConsole, debugInTitle;

    public FPSLogger(String titleName, boolean debugInConsole, boolean debugInTitle) {
        this.startTime = TimeUtils.nanoTime();
        this.titleName = titleName;
        this.debugInConsole = debugInConsole;
        this.debugInTitle = debugInTitle;
    }

    /**
     * Logs the current frames per second to the debugInConsole.
     */
    public void log() {
        if (TimeUtils.nanoTime() - startTime > 1000000000) /* 1,000,000,000ns == one second */ {
            if (debugInConsole)
                super.log();
            if (debugInTitle)
                Gdx.graphics.setTitle(this.titleName + " - " + Gdx.graphics.getFramesPerSecond() + "fps");
            startTime = TimeUtils.nanoTime();
        }
    }

}
