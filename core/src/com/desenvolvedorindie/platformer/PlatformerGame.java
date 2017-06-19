package com.desenvolvedorindie.platformer;

import com.badlogic.gdx.Game;
import com.desenvolvedorindie.platformer.screen.GameScreen;

public class PlatformerGame extends Game {

	private static PlatformerGame instance;

	public static final boolean DEBUG = true;

	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;

	private PlatformerGame() {
	}

	public static PlatformerGame getInstance() {
		if (instance == null) {
			instance = new PlatformerGame();
		}
		return instance;
	}

	@Override
	public void create() {
		this.setScreen(new GameScreen());
	}

}
