package com.ladinc.clappybird.core.screen;

import java.util.ArrayList;
import java.util.List;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.clappybird.core.AudioThread;
import com.ladinc.clappybird.core.ClappyBird;
import com.ladinc.clappybird.core.objects.Bird;
import com.ladinc.clappybird.core.objects.BoxProp;
import com.ladinc.clappybird.core.objects.Pipe;

public class GameScreen implements Screen 
{
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
    
    private Vector2 center;
    
    private static Bird bird;
    
    private Pipe pipeLow;
    
    private Box2DDebugRenderer debugRenderer;
    
    private AudioThread audioThread;

	private Texture birdTexture;
    private Sprite birdSprite ;
    
    private Texture pipeTexture;
    private Sprite pipeSprite ;
    
    private static boolean drawPipes = false;
    
    private static List<Pipe> listPipes = new ArrayList<Pipe>();
    
	public GameScreen(ClappyBird gs)
	{
		this.game = gs;
		
    	this.screenWidth = 480;
    	this.screenHeight = 800;
    	
    	this.worldHeight = this.screenHeight / PIXELS_PER_METER;
    	this.worldWidth = this.screenWidth / PIXELS_PER_METER;
    	
    	backgroundTexture = new Texture(Gdx.files.internal("../../clappybird/assets/background.png"));
    	spriteBatch = new SpriteBatch();    

    	this.center = new Vector2(worldWidth / 2, worldHeight / 2);
    	
    	this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
        debugRenderer = new Box2DDebugRenderer();
        
        drawPipe();
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

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); 
		camera.update();
		
        world.step(1.0f/60.0f, 10, 10);
        
        birdTexture = new Texture(Gdx.files.internal("../../clappybird/assets/birdMid.png"));
		birdSprite = new Sprite(birdTexture);
		
		pipeTexture = new Texture(Gdx.files.internal("../../clappybird/assets/pipeUp.png"));
		pipeSprite = new Sprite(pipeTexture);
		
		spriteBatch.begin();
        
        //set up background image
        spriteBatch.draw(backgroundTexture, 0, 0);
        
        //bird seems to operate in a different coordinate system to sprite, need to alter by a factor of PIXELS_PER_METER
        updateSprite(birdSprite, spriteBatch, PIXELS_PER_METER, bird.body);
        
        //draw pipe image over pipe objects
		for(Pipe p : listPipes)
		{
			System.out.println(listPipes.size());
			updateSprite(pipeSprite, spriteBatch, PIXELS_PER_METER, p.body);
		}
		  
		  spriteBatch.end();

        debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
	}

	private void drawPipe() {
		//set up a timer to have the 'intro' part to the level, which sets a flag which indicates the free ride 
        //part of the start of the level has finished
        Timer introTimer = new Timer(true);
        introTimer.schedule(
            new TimerTask() {
              public void run() { 
            	  // draw a pipe every second after a delay of 5 seconds.
            	  // A new pipe will be drawn every second at the edge of the screen. Pipes
            	  // will move with consistent velocity in the -x direction
            	  Pipe p = new Pipe(world, new Vector2(bird.getPos().x*PIXELS_PER_METER - 100, 100));         
            	  listPipes.add(p);
            	  System.out.println("new pipe added");
              }
            }, 5000, 10000);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() 
	{
		world = new World(new Vector2(0f, -80.0f), true);
		
		bird = new Bird(world, this.center);
		
		//set a box around the screen
		new BoxProp(world, this.worldWidth, 1f, new Vector2(this.center.x, 0));
		new BoxProp(world, this.worldWidth, 1f, new Vector2(this.center.x, this.worldHeight));
		
		audioThread = new AudioThread(bird);
        Thread t = new Thread(audioThread);
        t.start();
		
	}

	private void setBird(Bird bird) {
		GameScreen.bird = bird;
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
