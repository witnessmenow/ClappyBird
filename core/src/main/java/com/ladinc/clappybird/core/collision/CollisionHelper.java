package com.ladinc.clappybird.core.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladinc.clappybird.core.objects.Pipe;
import com.ladinc.clappybird.core.screen.GameScreen;

public class CollisionHelper implements ContactListener{

	private static final String HIGHSCORE = "highscore";
	private Sound hitSound;

	public CollisionHelper()
	{
	}

	//Bird has hit either the ground or a pipe, dont car which
	@Override
	public void beginContact(Contact contact) 
	{		
		//boolean used to disable pipe and bird movement and show the game over screen
		if(GameScreen.demoOver && !GameScreen.gameOver){
			GameScreen.gameOver = true;  
			hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
			GameScreen.playSound(hitSound);
			
			//stop the pipes from moving on game over
			for(Pipe p : GameScreen.listPipes){
				if(p.btmPipe!=null && p.btmPipe.getPosition()!=null){
					p.btmPipe.setLinearVelocity(new Vector2(0, 0));
					p.topPipe.setLinearVelocity(new Vector2(0, 0));
				}
			}
			
			//if the bird hits a pipe we want it fall vertically down, not bounce off the pipe backwards
			GameScreen.getBird().body.setLinearVelocity(new Vector2(0, -10));
			
			saveScore();
		}
	}

	private void saveScore() {
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		Integer currHighScore = prefs.getInteger(HIGHSCORE);
		
		if(currHighScore == null){
			currHighScore = 0;
			prefs.putInteger(HIGHSCORE, currHighScore);
		}
		
		
		if(GameScreen.score>currHighScore){
			//overwrite the previous highscore if score is higher than value in the file
			GameScreen.highScr = GameScreen.score;
			prefs.putInteger(HIGHSCORE, GameScreen.score);
			prefs.flush();
		}
		else{
			//do not overwrite the highscore on file. The current higscore is still valid
			GameScreen.highScr = currHighScore;
		}
	}

	@Override
	public void endContact(Contact contact) 
	{   
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
