package com.ladinc.clappybird.core.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.clappybird.core.AudioThread;
import com.ladinc.clappybird.core.ClappyBird;
import com.ladinc.clappybird.core.collision.CollisionHelper;
import com.ladinc.clappybird.core.objects.Bird;
import com.ladinc.clappybird.core.objects.Pipe;

public class GameScreen implements Screen 
{
	private static final String ASSETS_DIR = "../../clappybird/assets/";

	private ClappyBird game;
	
	private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private Texture backgroundTexture;
    
    private World world;
    
    //Used for sprites etc
	private int screenWidth;
    private int screenHeight;
    
    //Used for Box2D
    private float worldWidth;
    private float worldHeight;
    private static int PIXELS_PER_METER = 10;
    
    public static Vector2 center;
    
    private static Bird bird;
    
    private Box2DDebugRenderer debugRenderer;
    
    private AudioThread audioThread;

	private Texture birdTexture;
    private Sprite birdSprite ;
    
    private Texture btmPipeTexture;
    private Sprite btmPipeSprite ;
    
    private Texture topPipeTexture;
    private Sprite topPipeSprite ;
    
    private static List<Pipe> listPipes = new ArrayList<Pipe>();
    
    private float timer;

	private boolean drawPipes;
    
	public GameScreen(ClappyBird gs)
	{
		this.game = gs;
		
    	this.screenWidth = 480;
    	this.screenHeight = 800;
    	
    	this.worldHeight = this.screenHeight / PIXELS_PER_METER;
    	this.worldWidth = this.screenWidth / PIXELS_PER_METER;
    	
    	backgroundTexture = new Texture(Gdx.files.internal("../../clappybird/assets/background.png"));
    	spriteBatch = new SpriteBatch();    

    	center = new Vector2(worldWidth / 2, worldHeight / 2);
    	
    	this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
        debugRenderer = new Box2DDebugRenderer();
        
	}
	
	public static void updateSprite(Sprite sprite, SpriteBatch spriteBatch, int PIXELS_PER_METER, Body body)
    {
        if(sprite != null && spriteBatch != null && body != null)
        {
                setSpritePosition(sprite, PIXELS_PER_METER, body);

                sprite.draw(spriteBatch);
        }
    }
    
    public static void setSpritePosition(Sprite sprite, int PIXELS_PER_METER, Body body)
    {
        
        sprite.setPosition(PIXELS_PER_METER * body.getPosition().x - sprite.getWidth()/2,
                        PIXELS_PER_METER * body.getPosition().y  - sprite.getHeight()/2);
        sprite.setRotation((MathUtils.radiansToDegrees * body.getAngle()));
    }
	
	@Override
	public void render(float delta) {

		timer = timer + delta;
		
		//used for intro sequence, no pipes for 5 secs
		if(timer > 5)drawPipes = true;
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); 
		camera.update();
		
        world.step(1.0f/60.0f, 10, 10);
        
        birdTexture = new Texture(Gdx.files.internal(ASSETS_DIR+"birdMid.png"));
		birdSprite = new Sprite(birdTexture);
		
		btmPipeTexture = new Texture(Gdx.files.internal(ASSETS_DIR+"pipeUp.png"));
		btmPipeSprite = new Sprite(btmPipeTexture);
		
		topPipeTexture = new Texture(Gdx.files.internal(ASSETS_DIR+"pipeDown.png"));
		topPipeSprite = new Sprite(topPipeTexture);
		
		spriteBatch.begin();
        
        //set up background image
        spriteBatch.draw(backgroundTexture, 0, 0);
        	
        drawPipesAtIntervals();

        //bird seems to operate in a different coordinate system to sprite, need to alter by a factor of PIXELS_PER_METER
        updateSprite(birdSprite, spriteBatch, PIXELS_PER_METER, bird.body);
             
        //draw pipe image over pipe objects
		for(Pipe p : listPipes)
		{
			updateSprite(btmPipeSprite, spriteBatch, PIXELS_PER_METER, p.btmPipe);
			updateSprite(topPipeSprite, spriteBatch, PIXELS_PER_METER, p.topPipe);
		}
		 
		spriteBatch.end();

		for(Pipe p : listPipes)
		{
			//p.body.getPosition().x = p.body.getPosition().x - 1;
			p.btmPipe.setLinearVelocity(new Vector2(-10, 0));
			p.topPipe.setLinearVelocity(new Vector2(-10, 0));
		}
		
        debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
	}

	private void drawPipesAtIntervals() {
		if(drawPipes && timer>=2){
        	timer = 0f;
        	
        	Random rand = new Random();
        	
        	// Want a random position for the gap to be places, between 2 and 10. 
        	// Each unit represents 5 units on the scale of the screen
        	int low = 4; //minimum position for gap
        	int high = 7; // max position for gap
        	int randomNum = rand.nextInt(high - low) + low;
        	listPipes.add(new Pipe(world, randomNum));
		}
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() 
	{
		world = new World(new Vector2(0f, -80.0f), true);
		
		bird = new Bird(world, center);
		
		//draw ground line
		setUpGround();
		
		world.setContactListener(new CollisionHelper());
		
		audioThread = new AudioThread(bird);
        Thread t = new Thread(audioThread);
        t.start();
		
	}

	private void setUpGround() {
		// Create our body definition
	    BodyDef groundBodyDef = new BodyDef();
	    groundBodyDef.type = BodyType.StaticBody;

		// Create our line
		// Create a body from the definition and add it to the world
        Body groundBody = world.createBody(groundBodyDef);
        EdgeShape ground = new EdgeShape();
        ground.set(0, 0, screenWidth, 0);
        groundBody.createFixture(ground, 0.0f);
        ground.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public static Bird getBird(){
		return bird;
	}

}
