package com.ladinc.clappybird.android;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class ClappyBirdAndroid extends Game {

	public GameScreenAndroid gameScreen;
	

	@Override
	public void create () 
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Gdx.app.setLogLevel(Application.LOG_INFO);
		
		createScreens();
		
		setScreen(gameScreen);
	}
	
	private void createScreens()
	{
		this.gameScreen = new GameScreenAndroid(this);

	}
}
