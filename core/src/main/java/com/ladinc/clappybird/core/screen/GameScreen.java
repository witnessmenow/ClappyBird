package com.ladinc.clappybird.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.clappybird.core.AudioThread;
import com.ladinc.clappybird.core.ClappyBird;
import com.ladinc.clappybird.core.objects.Bird;

public class GameScreen implements Screen 
{
	private ClappyBird game;
	
	private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

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
    
    private Box2DDebugRenderer debugRenderer;
    
    private AudioThread audioThread;
    
	public GameScreen(ClappyBird gs)
	{
		this.game = gs;
		
    	this.screenWidth = 480;
    	this.screenHeight = 800;
    	
    	this.worldHeight = this.screenHeight / PIXELS_PER_METER;
    	this.worldWidth = this.screenWidth / PIXELS_PER_METER;
    	
    	this.center = new Vector2(worldWidth / 2, worldHeight / 2);
    	
    	this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
        debugRenderer = new Box2DDebugRenderer();
        
        audioThread = new AudioThread();
        Thread t = new Thread(audioThread);
        t.start();
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); 
		camera.update();
		
		//this.bird.processMovement();
		
		//world.step(Gdx.app.getGraphics().getDeltaTime(), 10, 10);
        //world.clearForces();
        world.step(1.0f/60.0f, 10, 10);
        //world.clearForces();
        
        debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
		
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
