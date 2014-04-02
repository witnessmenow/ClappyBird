package com.ladinc.clappybird.core.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.clappybird.core.screen.GameScreen;

public class Bird {
	
	public static float WIDTH = 2;
	public static float HEIGHT = 2;
	
	public Body body;
	private WingPosition wingPosition;
	
	public Bird(World world, Vector2 position)
	{		
		//initialize body 
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = false;
		this.body = world.createBody(bodyDef);
	    
	    //initialize shape
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(Bird.WIDTH / 2, Bird.HEIGHT / 2);
		fixtureDef.shape=boxShape;
		fixtureDef.density = 15f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution=0.4f; //positively bouncy!
	    this.body.createFixture(fixtureDef);
	    
	    boxShape.dispose();
	}
	
	public void checkForJump()
	{
			//need to convert short[] to byte[] for isClap
		if(Gdx.input.justTouched()){
			if(!GameScreen.gameOver){
				GameScreen.demoOver = true;
			}

			if(GameScreen.demoOver && !GameScreen.gameOver){
				this.body.applyForce(this.body.getWorldVector(new Vector2(0.0f, 190000.0f)), this.body.getWorldCenter() , true );
			}
			else if(GameScreen.demoOver && GameScreen.gameOver){
				resetWorld();
			}
		}
		
	}

	private void resetWorld() {
		GameScreen.gameOver = false;
		GameScreen.demoOver = false;	

		GameScreen.score = 0;

		this.body.setTransform(GameScreen.center.x-5, GameScreen.center.y, 0);
		this.body.setLinearVelocity(new Vector2(0, 0));
		GameScreen.reset = true;
	}
	
	public static enum WingPosition{UP, MIDDLEDOWN, MIDDLEUP, DOWN};
	
	public void setWingPosition(WingPosition pos){
		this.wingPosition = pos;
	}
	
	public WingPosition getWingPosition(){
		return this.wingPosition;
	}
	
	public Vector2 getPos(){
		return this.body.getPosition();
	}
}
