package com.ladinc.clappybird.html;

import com.ladinc.clappybird.core.ClappyBird;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class ClappyBirdHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new ClappyBird();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
