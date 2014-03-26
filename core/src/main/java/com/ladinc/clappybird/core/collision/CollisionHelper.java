package com.ladinc.clappybird.core.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladinc.clappybird.core.objects.Pipe;
import com.ladinc.clappybird.core.screen.GameScreen;

public class CollisionHelper implements ContactListener{

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
		
			//stop the pipes from moving on game over
			for(Pipe p : GameScreen.listPipes){
				p.btmPipe.setLinearVelocity(new Vector2(0, 0));
				p.topPipe.setLinearVelocity(new Vector2(0, 0));
	
			}
			
			//if the bird hits a pipe we want it fall vertically down, not bounce off the pipe backwards
			GameScreen.getBird().body.setLinearVelocity(new Vector2(0, -10));
			
			saveScore();
		}
	}

	private void saveScore() {
		FileHandle file = Gdx.files.local("highscore.txt");
		
		String currHigscore = file.readString();
		GameScreen.highScr = Integer.parseInt(currHigscore);
		
		if(GameScreen.score>GameScreen.highScr){
			//overwrite the previous highscore if score is higher than value in the file
			file.writeString(""+GameScreen.score, false);
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
