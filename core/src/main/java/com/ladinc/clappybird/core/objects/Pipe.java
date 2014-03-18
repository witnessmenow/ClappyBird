package com.ladinc.clappybird.core.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.ladinc.clappybird.core.collision.CollisionHelper;

public class Pipe {
	
	public static float WIDTH = 4;
	public static float HEIGHT = 34;
	
	public Body body;
	
	public Pipe(World world, Vector2 position)
	{		
		//initialize body 
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(position);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		this.body = world.createBody(bodyDef);
	    
	    //initialize shape
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(Pipe.WIDTH / 2, Pipe.HEIGHT / 2);
		fixtureDef.shape=boxShape;
		fixtureDef.density = 10f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution=0.4f; //positively bouncy!
	    this.body.createFixture(fixtureDef);
	    
	    this.body.setUserData(new CollisionHelper());
	    
	    boxShape.dispose();
	}
	
	public Vector2 getPos(){
		return this.body.getPosition();
	}
}
