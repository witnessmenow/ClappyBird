package com.ladinc.clappybird.core;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ladinc.clappybird.core.screen.GameScreen;

public class ClappyBird extends Game {

	public GameScreen gameScreen;
	

	@Override
	public void create () 
	{
		//Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.setLogLevel(Application.LOG_INFO);
		
		createScreens();
		
		setScreen(gameScreen);
	}
	
	private void createScreens()
	{
		this.gameScreen = new GameScreen(this);

	}
}
