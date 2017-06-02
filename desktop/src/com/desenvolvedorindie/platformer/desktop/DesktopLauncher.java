package com.desenvolvedorindie.platformer.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.desenvolvedorindie.platformer.PlatformerGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 720);
        config.setResizable(false);
        config.useVsync(true);
        new Lwjgl3Application(PlatformerGame.getInstance(), config);
    }
}
