package com.desenvolvedorindie.platformer;

import com.badlogic.gdx.Game;
import com.desenvolvedorindie.platformer.screen.GameScreen;

public class PlatformerGame extends Game {

	private static PlatformerGame instance;

	public static final boolean DEBUG = true;

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
