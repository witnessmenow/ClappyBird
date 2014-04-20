package com.ladinc.clappybird.core.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
import com.google.android.gms.ads.a;
import com.ladinc.clappybird.android.ActionResolver;
import com.ladinc.clappybird.core.AudioThread;
import com.ladinc.clappybird.core.ClappyBird;
import com.ladinc.clappybird.core.collision.CollisionHelper;
import com.ladinc.clappybird.core.objects.Bird;
import com.ladinc.clappybird.core.objects.Bird.WingPosition;
import com.ladinc.clappybird.core.objects.Pipe;

public class GameScreen implements Screen 
{
	private static final String DEMO = "DEMO";

	private static final String GROUND = "GROUND";

	private static final String TOP_PIPE = "TOP_PIPE";

	private static final String BTM_PIPE = "BTM_PIPE";

	private static final String BIRD_UP = "BIRD_UP";

	private static final String BIRD_MID = "BIRD_MID";

	private static final String BIRD_DOWN = "BIRD_DOWN";

	private static final String BACKGROUND = "BACKGROUND";
	
	private OrthographicCamera camera;
    private static SpriteBatch spriteBatch;

    private Texture backgroundTexture;
    
    public static World world;
    
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

	private Texture birdMidTexture;
    private Sprite birdSprite ;
    
    private Texture btmPipeTexture;
    private Sprite btmPipeSprite;
    
    private Texture scoreTexture1;
    private Texture scoreTexture2;
    private Texture scoreTexture3;
    
    private Texture groundTexture;
    
    private Texture topPipeTexture;
    private Sprite topPipeSprite ;
    
    public static List<Pipe> listPipes = new ArrayList<Pipe>();
    
    private float timer;

	private boolean drawPipes;
    
	public static List<Pipe> scoresList = new ArrayList<Pipe>();

	private Texture demoTexture;

	private Texture birdUpTexture;

	private Texture birdDownTexture;

	private Texture scoreBoardTexture;

	private Texture highScrTextr1;

	private Texture highScrTextr2;

	private Texture highScrTextr3;

	private Texture gameOverTexture;

	private Sound scoreSound;

	public static boolean gameOver = false;

	public static int score;
	
	public static boolean demoOver = false;
	
	public static Map<String, Texture> textureMap = new HashMap<String, Texture>();

	public static int highScr;

	public static boolean reset = false;
	
	public static Sound flapSound;
	
	private ActionResolver actionResolver;
	
	public GameScreen(ClappyBird gs)
	{		
    	this.screenWidth = 480;
    	this.screenHeight = 800;
    	
    	this.worldHeight = this.screenHeight / PIXELS_PER_METER;
    	this.worldWidth = this.screenWidth / PIXELS_PER_METER;
    	
    	spriteBatch = new SpriteBatch();    

    	center = new Vector2(worldWidth / 2, worldHeight / 2);
    	
    	this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
        //debugRenderer = new Box2DDebugRenderer();
        
        setUpTextureMap();
        flapSound = Gdx.audio.newSound(Gdx.files.internal("flap.mp3"));
	}
	
	private void setUpTextureMap() {
		birdMidTexture = new Texture(Gdx.files.internal("birdMid.png"));
		birdUpTexture = new Texture(Gdx.files.internal("birdUp.png"));
		birdDownTexture = new Texture(Gdx.files.internal("birdDown.png"));
		
		btmPipeTexture = new Texture(Gdx.files.internal("pipeUp.png"));
		topPipeTexture = new Texture(Gdx.files.internal("pipeDown.png"));
		
		groundTexture = new Texture(Gdx.files.internal("ground.png"));
		
		backgroundTexture = new Texture(Gdx.files.internal("background.png"));
		
		//demo to the user, tap icon and floating bird displayed
		demoTexture = new Texture(Gdx.files.internal("demo.png"));
		
		textureMap.put(BIRD_DOWN, birdDownTexture);
		textureMap.put(BIRD_MID, birdMidTexture);
		textureMap.put(BIRD_UP, birdUpTexture);
		
		textureMap.put(BTM_PIPE, btmPipeTexture);
		textureMap.put(TOP_PIPE, topPipeTexture);
		
		textureMap.put(GROUND, groundTexture);
		
		textureMap.put(BACKGROUND, backgroundTexture);
		
		textureMap.put(DEMO, demoTexture);
		
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
        
        if(bird.body == body && !gameOver && demoOver){
        	if(body.getLinearVelocity().y > 0){
        		sprite.setRotation((45f));
        	}
        	else{
        		//get the square of the velocity in the y-direction to work out the rotation of the bird
        		float rotation = 45 - (2f*(Math.abs(body.getLinearVelocity().y)));
        		if(rotation<-90)rotation = -90;
        		
        		sprite.setRotation(rotation);
        	}
        }
    	else{
        	sprite.setRotation((MathUtils.radiansToDegrees * body.getAngle()));
        }
    }
	
	@Override
	public void render(float delta) {
		if(reset){
			removePipesOnGameReset();
			bird = null;
			reset = false;
			world = null;
			show();
		}
		bird.checkForJump();
	
		//In demo mode, the bird floats and there is an image indicating to the user to tap the screen
		if(!demoOver)floatyDemoBird();
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); 
		camera.update();	
		
		spriteBatch.setProjectionMatrix(camera.combined);
		
        world.step(1.0f/60.0f, 10, 10);  
        
        updateTimerValueAndSetDrawPipes(delta);
        
        drawTextures();
        setPipesMoving();

		//remove bottom and top pipes if they have moved off the screen
		removeUnseenPipes();
        
        
        //debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));
	}

	private void removePipesOnGameReset() {
		for(int i = 0; i < listPipes.size(); i++){
		Pipe pipe = listPipes.get(i);
		
		pipe.btmPipe.setLinearVelocity(new Vector2(0, 0));
		pipe.topPipe.setLinearVelocity(new Vector2(0, 0));
		
		GameScreen.world.destroyBody(pipe.btmPipe);
		pipe.btmPipe = null;
		GameScreen.world.destroyBody(pipe.topPipe);
		pipe.topPipe = null;

		pipe = null;
	}
		
	listPipes.clear();
	
	for(int j = 0; j<scoresList.size(); j++){
		Pipe pipe = scoresList.get(j);
		
		if(pipe.btmPipe!=null){
			pipe.btmPipe.setLinearVelocity(new Vector2(0, 0));
			GameScreen.world.destroyBody(pipe.btmPipe);
			pipe.btmPipe = null;
		}
		if(pipe.topPipe!=null){
			pipe.topPipe.setLinearVelocity(new Vector2(0, 0));
			GameScreen.world.destroyBody(pipe.topPipe);
			pipe.topPipe = null;
		}
		
		pipe = null;
	}
	scoresList.clear();
	
		
	}

	private void showScoreAndHighScore() {
		gameOverTexture = new Texture(Gdx.files.internal("gameOver.png"));
		spriteBatch.draw(gameOverTexture, 150, 500);
		
		
		scoreBoardTexture = new Texture(Gdx.files.internal("scoreScreen.png"));
		spriteBatch.draw(scoreBoardTexture, 150, 350);
		
		String highScoreStr = ""+highScr;
		char[] highScoreArr = highScoreStr.toCharArray();
		
		if(highScr<10){
			highScrTextr1 = new Texture(Gdx.files.internal(highScoreArr[0]+".png"));
			highScrTextr2 = new Texture(Gdx.files.internal("blank"+".png"));
			highScrTextr3 = new Texture(Gdx.files.internal("blank"+".png"));
		}
		else if(highScr<100){
			highScrTextr1 = new Texture(Gdx.files.internal(highScoreArr[0]+".png"));
			highScrTextr2 = new Texture(Gdx.files.internal(highScoreArr[1]+".png"));
			highScrTextr3 = new Texture(Gdx.files.internal("blank"+".png"));
		}
		else if(highScr < 1000){
			highScrTextr1 = new Texture(Gdx.files.internal(highScoreArr[0]+".png"));
			highScrTextr2 = new Texture(Gdx.files.internal(highScoreArr[1]+".png"));
			highScrTextr3 = new Texture(Gdx.files.internal(highScoreArr[2]+".png"));
		}
		
		spriteBatch.draw(highScrTextr1, 315, 370);
		spriteBatch.draw(highScrTextr2, 315 + highScrTextr1.getWidth()+ 5, 370);
		spriteBatch.draw(highScrTextr3, 315 + highScrTextr1.getWidth()*2 + 10, 370);
		
		spriteBatch.draw(scoreTexture1, 315, 415);
		spriteBatch.draw(scoreTexture2, 330, 415);
		spriteBatch.draw(scoreTexture3, 340, 415);
		
		
		drawMedal();
	}

	private void drawMedal() {
		Texture medalTexture = null;
		boolean isAndroid = checkDeviceIsAndroid();
		
		if(isAndroid){
			actionResolver.submitScoreGPGS(score);
			actionResolver.unlockAchievementGPGS(score);
		}
		if(score<10){
			medalTexture = new Texture(Gdx.files.internal("blank"+".png"));
		}
		if(score>=10 && score<20){
			medalTexture = new Texture(Gdx.files.internal("bronzeMedal"+".png"));
		}
		else if(score>=20 && score<30){
			medalTexture = new Texture(Gdx.files.internal("silverMedal"+".png"));
		}
		else if(score>=30 && score<40){
			medalTexture = new Texture(Gdx.files.internal("goldMedal"+".png"));
		}
		else if(score>=40){
			medalTexture = new Texture(Gdx.files.internal("platMedal"+".png"));
		}
		spriteBatch.draw(medalTexture, 175, 380);
	}

	private boolean checkDeviceIsAndroid() {
		boolean response = false;
		switch(Gdx.app.getType()) {
		   case Android:
			   response = true;
		       // android specific code
		   case Desktop:
		       // desktop specific code
		   case WebGL:
		       /// HTML5 specific code
		}
		return response;
	}

	private void floatyDemoBird() {
        
		if(bird.getPos().y < (center.y-1)){
			bird.body.applyForce(bird.body.getWorldVector(new Vector2(0.0f, 7000.0f)), bird.body.getWorldCenter(), true );
		}		
	}

	private void updateTimerValueAndSetDrawPipes(float delta) {
		timer = timer + delta;
		if(!demoOver && timer > 0.25){
			timer = 0;
		}
		
        //used for intro sequence, no pipes for 5 secs
        if(timer > 5)drawPipes = true;
	}

	private void setPipesMoving() {
		if(!gameOver){
			for(Pipe p : listPipes){
				if(p.btmPipe!= null && p.btmPipe.getPosition()!=null){
				p.btmPipe.setLinearVelocity(new Vector2(-15, 0));
				p.topPipe.setLinearVelocity(new Vector2(-15, 0));
			}}
		}
	}

	private void drawTextures() {
		
		//every 0.5 seconds change the wing position
		if((timer%0.25)<=0.03 && !gameOver){
			//state machine for moving the birds wings
			if(bird.getWingPosition() == WingPosition.MIDDLEDOWN){
				birdSprite = new Sprite(textureMap.get(BIRD_DOWN));
				bird.setWingPosition(WingPosition.DOWN);
			}
			else if(bird.getWingPosition() == WingPosition.DOWN){
				birdSprite = new Sprite(textureMap.get(BIRD_MID));
				bird.setWingPosition(WingPosition.MIDDLEUP);
			}
			else if(bird.getWingPosition() == WingPosition.MIDDLEUP){
				birdSprite = new Sprite(textureMap.get(BIRD_UP));
				bird.setWingPosition(WingPosition.UP);
			}
			else if(bird.getWingPosition() == WingPosition.UP){
				birdSprite = new Sprite(textureMap.get(BIRD_MID));
				bird.setWingPosition(WingPosition.MIDDLEDOWN);
			}
		}
		
		//birdSprite = new Sprite(birdDownTexture);
		btmPipeSprite = new Sprite(textureMap.get(BTM_PIPE));
		topPipeSprite = new Sprite(textureMap.get(TOP_PIPE));
		
		spriteBatch.begin();
        
        //set up background image
        spriteBatch.draw(textureMap.get(BACKGROUND), 0, 0);
        	
        
        //bird seems to operate in a different coordinate system to sprite, need to alter by a factor of PIXELS_PER_METER
        updateSprite(birdSprite, spriteBatch, PIXELS_PER_METER, bird.body);
             
        if(!gameOver && demoOver)drawPipesAtIntervals();
        
        //draw pipe image over pipe objects
		for(Pipe p : listPipes)
		{
			updateSprite(btmPipeSprite, spriteBatch, PIXELS_PER_METER, p.btmPipe);
			updateSprite(topPipeSprite, spriteBatch, PIXELS_PER_METER, p.topPipe);
		}
		 
		//add sprite for the ground
		spriteBatch.draw(textureMap.get(GROUND), 0, 0);
		
		if(!demoOver)spriteBatch.draw(textureMap.get(DEMO), 250, 320);
		
		cleanUpPipes();
		
		calculateAndDisplayScore();
				
		if(gameOver){
			showScoreAndHighScore();
			showRetryBtn();
		}
		
		spriteBatch.end();
	}

	private void cleanUpPipes() {
		for(int i = 0; i<listPipes.size(); i++){
			if(listPipes.get(i).btmPipe == null){
				listPipes.remove(i);
				scoresList.remove(i);
			}
		}
		
	}

	private void showRetryBtn() {
		// TODO Auto-generated method stub
		
	}

	private void removeUnseenPipes() {
		for(int i =0; i < listPipes.size(); i++){
			Pipe pipe = listPipes.get(i);
			
			if(pipe.getBtmPos().x<-10){
				world.destroyBody(pipe.btmPipe);
				pipe.btmPipe = null;
				world.destroyBody(pipe.topPipe);
				pipe.topPipe = null;
				
				listPipes.remove(pipe);
				scoresList.remove(pipe);
				pipe = null;
		}}
	}

	private void calculateAndDisplayScore() {
		for(Pipe p : listPipes){
			if(p.btmPipe.getPosition()!=null && !scoresList.contains(p))
			{	
				if(p.getBtmPos().x < center.x - 5){
					scoreSound = Gdx.audio.newSound(Gdx.files.internal("score.mp3"));
					playSound(scoreSound);
					scoresList.add(p);
					//keep a unique list of the pipes that have passed the centre x point.
					//Only add them to the list if they have newly passed this point
					//The user's score is the size of the list then
					score = score + 1;
				}
			}
		}
		
		String scoreStr = String.valueOf(score);
		char[] scoreArr = scoreStr.toCharArray();
		
		if(score < 10){
			scoreTexture1 = new Texture(Gdx.files.internal(scoreArr[0]+".png"));
			scoreTexture2 = new Texture(Gdx.files.internal("blank"+".png"));
			scoreTexture3 = new Texture(Gdx.files.internal("blank"+".png"));
		}
		else if(score < 100){
			scoreTexture1 = new Texture(Gdx.files.internal(scoreArr[0]+".png"));
			scoreTexture2 = new Texture(Gdx.files.internal(scoreArr[1]+".png"));
			scoreTexture3 = new Texture(Gdx.files.internal("blank"+".png"));
		}
		else if(score < 1000){
			scoreTexture1 = new Texture(Gdx.files.internal(scoreArr[0]+".png"));
			scoreTexture2 = new Texture(Gdx.files.internal(scoreArr[1]+".png"));
			scoreTexture3 = new Texture(Gdx.files.internal(scoreArr[2]+".png"));
		}
		
		// draw the user's score on the screen
		spriteBatch.draw(scoreTexture1, 250, 700);
		spriteBatch.draw(scoreTexture2, 265, 700);
		spriteBatch.draw(scoreTexture3, 280, 700);
	}

	private void drawPipesAtIntervals() {
		if(drawPipes && timer>=3.5){
        	timer = 0f;
        	
        	Random rand = new Random();
        	
        	// Want a random position for the gap to be places, between 2 and 10. 
        	// Each unit represents 5 units on the scale of the screen
        	int low = 3; //minimum position for gap
        	int high = 8; // max position for gap
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
		Vector2 birdPos = new Vector2(center.x - 5, center.y);
		bird = new Bird(world, birdPos);
		bird.setWingPosition(WingPosition.MIDDLEDOWN);
		
		//draw ground line
		setUpGround();
		setUpCeiling();
		
		world.setContactListener(new CollisionHelper());
		
		//audioThread = new AudioThread(bird);
        //Thread t = new Thread(audioThread);
        //t.start();
		
	}

	private void setUpCeiling() {
		// Create our body definition
	    BodyDef ceilingBodyDef = new BodyDef();
	    ceilingBodyDef.type = BodyType.StaticBody;
	    
		// Create our line
		// Create a body from the definition and add it to the world
        Body ceilingBody = world.createBody(ceilingBodyDef);
        EdgeShape ceiling = new EdgeShape();
        ceiling.set(0, 100, screenWidth, 100);
        ceilingBody.createFixture(ceiling, 0.0f);
        ceiling.dispose();
		
	}

	private void setUpGround() {
		// Create our body definition
	    BodyDef groundBodyDef = new BodyDef();
	    groundBodyDef.type = BodyType.StaticBody;
	    
		// Create our line
		// Create a body from the definition and add it to the world
        Body groundBody = world.createBody(groundBodyDef);
        EdgeShape ground = new EdgeShape();
        ground.set(0, 11, screenWidth, 11);
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
	
	public static synchronized void playSound(Sound sound){
		sound.play();
	}

}
