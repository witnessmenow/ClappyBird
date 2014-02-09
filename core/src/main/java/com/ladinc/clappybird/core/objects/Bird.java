package com.ladinc.clappybird.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.musicg.api.ClapApi;

public class Bird {
	
	public static float WIDTH = 2;
	public static float HEIGHT = 2;
	
	public Body body;
	
	private ClapApi clapApi;
	
	public Bird(World world, Vector2 position, ClapApi clapApi)
	{
		this.clapApi = clapApi;
		
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
		fixtureDef.density = 10f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution=0.4f; //positively bouncy!
	    this.body.createFixture(fixtureDef);
	    
	    //this.body.setUserData(new CollisionInfo("Wall", CollisionObjectType.wall));
	    
	    boxShape.dispose();
	}
	
	public void processMovement(short[] sampleData)
	{
		byte[] byteArr = this.convertShortToByteArr(sampleData);
		
		//need to convert short[] to byte[] for isClap
		if(this.clapApi.isClap(byteArr))
		{			
			this.body.applyForce(this.body.getWorldVector(new Vector2(0.0f, 100000.0f)), this.body.getWorldCenter(), true );
		}
	}

	private byte[] convertShortToByteArr(short[] sampleData) {
		// TODO Auto-generated method stub
		return null;
	}

}
