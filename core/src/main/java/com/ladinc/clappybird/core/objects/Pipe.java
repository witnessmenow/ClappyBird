package com.ladinc.clappybird.core.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.clappybird.core.screen.GameScreen;

public class Pipe {
	
	public static float WIDTH = 4;
	public static float HEIGHT = 34;
	
	public Body btmPipe;
	public Body topPipe;
	
	private final static float xPos = GameScreen.center.x+30;
	
	public Pipe(World world, int gapPosition)
	{		
		Vector2 pipePosition = new Vector2();
		
		// x-position for the pipes is set to be just outside the edge of the screen
		pipePosition.x = xPos;
		
		//initialize body 
		setUpBottomPipe(world, pipePosition, gapPosition);
		setUpTopPipe(world, pipePosition, gapPosition);
	}

	private void setUpTopPipe(World world, Vector2 pipePosition, int gapPos) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		
		pipePosition.y = (9+gapPos)*5;
		bodyDef.position.set(pipePosition);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		this.topPipe = world.createBody(bodyDef);
	    
	    //initialize shape
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(Pipe.WIDTH / 2, Pipe.HEIGHT / 2);
		fixtureDef.shape=boxShape;
		fixtureDef.density = 10f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution=0.4f; //positively bouncy!
	    this.topPipe.createFixture(fixtureDef);
	    
	    boxShape.dispose();
		
	}

	private void setUpBottomPipe(World world, Vector2 pipePosition, int gapPos) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		
		pipePosition.y = gapPos*5;
		bodyDef.position.set(pipePosition);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		this.btmPipe = world.createBody(bodyDef);
	    
	    //initialize shape
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(Pipe.WIDTH / 2, Pipe.HEIGHT / 2);
		fixtureDef.shape=boxShape;
		fixtureDef.density = 10f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution=0.4f; //positively bouncy!
	    this.btmPipe.createFixture(fixtureDef);
	    
	    boxShape.dispose();
	}
	
	public Vector2 getPos(){
		return this.btmPipe.getPosition();
	}
}
