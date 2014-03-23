package com.ladinc.clappybird.core.objects;

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
	
	public Bird(World world, Vector2 position)
	{		
		//initialize body 
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
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
			if(GameScreen.demoOver && !GameScreen.gameOver){
				this.body.applyForce(this.body.getWorldVector(new Vector2(0.0f, 110000.0f)), this.body.getWorldCenter(), true );
			}
			GameScreen.demoOver = true;
		}
		
	}
	
	public Vector2 getPos(){
		return this.body.getPosition();
	}
}
