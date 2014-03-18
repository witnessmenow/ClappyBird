package com.ladinc.clappybird.core.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionHelper implements ContactListener{

	public CollisionHelper()
	{
	}

	//Bird has hit either the ground or a pipe, dont car which
	@Override
	public void beginContact(Contact contact) 
	{
		//Fixture fixtureA = contact.getFixtureA();
		//Fixture fixtureB = contact.getFixtureB();
        
		System.out.println("collision detected!");
		
		//Display game over screen with score
		showGameOver();      	
	}

	private void showGameOver() {
		// TODO Auto-generated method stub
		
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
