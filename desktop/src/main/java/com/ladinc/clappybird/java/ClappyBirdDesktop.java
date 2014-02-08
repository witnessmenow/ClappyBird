package com.ladinc.clappybird.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.ladinc.clappybird.core.ClappyBird;

public class ClappyBirdDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new ClappyBird(), config);
	}
}
