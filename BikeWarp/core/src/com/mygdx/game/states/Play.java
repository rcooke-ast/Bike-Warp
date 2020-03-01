/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Images by Craziwolf

package com.mygdx.game.states;

import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.B2DVars;

import static com.mygdx.game.handlers.B2DVars.PPM;

import com.mygdx.game.handlers.GameContactListener;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameInputProcessor;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.handlers.ObjectVars;
import com.mygdx.game.handlers.ReplayVars;
import com.mygdx.game.utilities.PolygonOperations;
import com.gushikustudios.rube.PolySpatial;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.SimpleImage;
import com.gushikustudios.rube.SimpleSpatial;
import com.gushikustudios.rube.loader.RubeSceneAsyncLoader;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.gushikustudios.rube.loader.serializers.utils.RubeDecor;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;
import com.gushikustudios.rube.loader.serializers.utils.RubeVertexArray;

/**
 *
 * @author rcooke
 */

/*
 * Things to do in order to get the Bike.json file to load:
 * call each wheel joint a name with the key: "name" : "leftwheel"  (or "rightwheel") 
 */
public class Play extends GameState {

	private boolean debug = false; // Change to false to not render object outlines
    private World mWorld;
    private RubeScene mScene;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam;
    private GameContactListener cl;
    private String editorString = null;
    private int levelID, mode;
    private boolean isReplay = false;
    private int mVelocityIter = 8;
    private int mPositionIter = 3;

    private Array<SimpleSpatial> mSpatials; // used for rendering rube images
    private Array<SimpleImage> mDecors; // used for rendering decorations
    private Array<PolySpatial> mPolySpatials;
    private Map<String, Texture> mTextureMap;
    private Map<Texture, TextureRegion> mTextureRegionMap;
    private Body gameInfo;
    private Array<float[]> switchGate;
    private Array<Body> remBodies;
    private Array<Body> kinematicBodies;
    private Array<Vector2[]> kinematicPath;
    private int[] kinematicDirection;
    private int[] kinematicIndex;
    private float[] kinematicSpeed;
    private float[] kinematicLength;
    private float[] kinematicLengthLeft;
    private Array<Integer> remBodiesIdx;
    private Array<float[]> transportImages;
    private Array<float[]> doorImages;
    private Array<Body> fallingJoints;
    private Array<Float> fallingJointsFallTime;
    private Array<Float> fallingJointsTime;
    private float[] doorArr = new float[5];

    private static final Vector2 mTmp = new Vector2(); // shared by all objects
    private static final Vector2 mTmp3 = new Vector2(); // shared during polygon creation
    private SpriteBatch mBatch;
    private PolygonSpriteBatch mPolyBatch;
    private AssetManager mAssetManager;
    
    private WheelJoint leftWheel, rightWheel;
    private RopeJoint leftRope, rightRope;
    private WheelJointDef leftWheelL, rightWheelL, leftWheelR, rightWheelR;
    private RopeJointDef leftRopeL, rightRopeL, leftRopeR, rightRopeR;
    private Body bikeBodyLW;
    private Body bikeBodyRW;
    private Body bikeBodyH;
    private Body bikeBodyC;
    private Body switchGateBody;
    private float bikeDirc = 1.0f;
	private float dircGrav;
    private float bikeScale = 1.0f;
    private float bikeScaleLev = 0.05f;
    private float gravityScale = -1.0f;
    private Vector2 gravityPrev = new Vector2();
    private float motorTorque = 0.0f;
    private float playerTorque = 0.0f;
    private float applyTorque = -1.0f;
    private int applyNitrous = 0;
    private float playerJump = 0.0f;
    private float applyJump = -1.0f;
    private float canTransport = -1.0f;
    private float spinTime = 0.6f; // Time (in s) before spin can recommence
    private float jumpTime = 1.2f; // Time (in s) before jump can recommence
    private float transportTime = 5.0f; // Time (in s) before transporters are activated
    private float fallTime = 5.0f; // Time (in s) before a falling platform will fall after being touched
    private float nitrousLevel = 0.0f; // Current level of nitrous
    private float finAngle = 0.0f, finishRad = 0.0f;
    private Vector2 startPosition, finishPosition;
    private float startDirection;
    private float startAngle;
    private Texture texture;
    private Sprite sky, mountains, trees, finishFG, openDoor, switchGL, switchRL, metalBar;
    private Sprite bikeColour, bikeOverlay, suspensionRear, suspensionFront;
    private BitmapFont timer, timerWR, timerPB;
    private int timerStart, timerCurrent, timerTotal, timerMSecs, timerSecs, timerMins;
    private String worldRecord, personalRecord;
    private float timerWidth, timerHeight, timerWRWidth, timerWRHeight, jcntrWidth, jcntrHeight;
    private int collectKeyRed=0, collectKeyGreen=0, collectKeyBlue=0, collectNitrous=0;
    //private int[] animateJewel;
    private float SCRWIDTH;
    private BitmapFont keyRedCntr, keyGreenCntr, keyBlueCntr, jewelCntr, nitrousCntr;
    private int collectJewel;
    private boolean collectDiamond;
    private int nullvar;
    private boolean forcequit, lrIsDown, paintBackdrop;
    
    // Some items to be displayed on the HUD
    //private Sprite panelShadeA, panelShadeB;//, panelShadeC;
    //private NinePatchDrawable panelContainer;
    private Sprite keyRed, keyBlue, keyGreen, jewelSprite;
    private Sprite nitrous, nitrousTube, nitrousFluid;
    //private TextureRegion[] jewelSprites;

    private static final String [][] LEVEL_FILE_LIST =
    	   {
    	      {
    	         "data/levelname.json",
    	         "data/bikeright.json"
    	      }//,
//    	      {
    	         //"data/boundary.json"//,
    	    	 //"data/bike.json"
//    	      }
    	   };

    private enum GAME_STATE
    {
    	STARTING,
    	LOADING,
    	LOADED,
    	RUNNING
    };
    
    private GAME_STATE mState;
    private GAME_STATE mPrevState;
    private GAME_STATE mNextState;

    private boolean mUseAssetManager;
    private int mRubeFileList;
    private int mRubeFileIndex;
    
    public Play(GameStateManager gsm, String editorScene, int levID, int modeValue) {
        super(gsm);
        editorString = editorScene;
        levelID = levID;
        mode = modeValue;
        // Check if it's a replay
        ReplayVars.Reset();
        // NEED TO DELETE THESE TWO LINES
        mode = 4;
        ReplayVars.LoadReplay("replay.rpl");
        ///////////////////////////////////
        if ((mode == 3) | (mode==4)) {
        	isReplay = false;
        	GameInputProcessor.Disable(true);
        } else isReplay = false;
        create();
    }
    
    public void create() {
    	forcequit = false;
        // Set the contact listener
        cl = new GameContactListener();

        // Set up box2d camera
        float SCTOSCRH = ((float) Gdx.graphics.getWidth()*Gdx.graphics.getDesktopDisplayMode().height)/((float) Gdx.graphics.getDesktopDisplayMode().width);
        float SCTOSCRW = ((float) Gdx.graphics.getHeight()*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Gdx.graphics.getWidth()/PPM, SCTOSCRH/PPM);
        //b2dCam.setToOrtho(false, BikeGame.V_WIDTH/PPM, BikeGame.V_HEIGHT/PPM);
        b2dCam.position.set(0, 0, 0);
        //b2dCam.zoom = B2DVars.SCRWIDTH/(BikeGame.V_WIDTH/PPM);
        b2dCam.zoom = B2DVars.SCRWIDTH/(Gdx.graphics.getWidth()/PPM);
        b2dCam.update();

        //hudCam.setToOrtho(false);

        hudCam.setToOrtho(false, SCTOSCRW, Gdx.graphics.getHeight());
        SCRWIDTH = SCTOSCRW/BikeGame.SCALE;
        hudCam.position.set(SCRWIDTH/2,BikeGame.V_HEIGHT/2,0);
        //hudCam.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
        hudCam.zoom = 1.0f/(BikeGame.SCALE);
        hudCam.update();

        b2dr = new Box2DDebugRenderer();

        mBatch = new SpriteBatch();
        mPolyBatch = new PolygonSpriteBatch();
        
        mTextureMap = new HashMap<String, Texture>();
        mTextureRegionMap = new HashMap<Texture, TextureRegion>();

        mState = mNextState = GAME_STATE.STARTING;

        // Get the records
        if ((mode == 1) | (mode == 3)) {
            worldRecord = GameVars.getTimeString(GameVars.worldTimesTrain.get(levelID)[0]);
            personalRecord = GameVars.getTimeString(GameVars.plyrTimesTrain.get(GameVars.currentPlayer).get(levelID)[0]);
        } else if ((mode == 2) | (mode==4)) {
            worldRecord = GameVars.getTimeString(GameVars.worldTimes.get(levelID)[0]);
            personalRecord = GameVars.getTimeString(GameVars.plyrTimes.get(GameVars.currentPlayer).get(levelID)[0]);
        } else {
        	worldRecord = GameVars.getTimeString(-1);
            personalRecord = GameVars.getTimeString(-1);
        }
        collectDiamond = false;
        
        // Create new wheel and rope joint definitions
        leftWheelL = new WheelJointDef();
        rightWheelL = new WheelJointDef();
        leftWheelR = new WheelJointDef();
        rightWheelR = new WheelJointDef();
        leftRopeL = new RopeJointDef();
        rightRopeL = new RopeJointDef();
        leftRopeR = new RopeJointDef();
        rightRopeR = new RopeJointDef();

        // Load the suspension textures
        bikeColour = new Sprite(BikeGameTextures.LoadTexture("bike_white",0));
        bikeOverlay = new Sprite(BikeGameTextures.LoadTexture("bike_overlay",0));
        suspensionRear = new Sprite(BikeGameTextures.LoadTexture("rear_suspension",0));
        suspensionFront = new Sprite(BikeGameTextures.LoadTexture("front_suspension",0));

        // Load the finish ball textures
        finishFG = new Sprite(BikeGameTextures.LoadTexture("finish_whirl",0));

        // Load the mountain texture
        mountains = new Sprite(BikeGameTextures.LoadTexture("mountains",2));
        trees = new Sprite(BikeGameTextures.LoadTexture("trees",2));

        // Load the opening door, switch box, and metal bar textures
        openDoor = new Sprite(BikeGameTextures.LoadTexture("gate",0));
        switchGL = new Sprite(BikeGameTextures.LoadTexture("switch_greenL",0));
        switchRL = new Sprite(BikeGameTextures.LoadTexture("switch_redL",0));
        metalBar = new Sprite(BikeGameTextures.LoadTexture("metal_pole_1x16",0));

        // Load the transport overlay
        //Texture transTexture = new Texture(Gdx.files.internal("data/images/transport_overlay.png"));
        //transTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        //transportOverlay = new Sprite(transTexture);
        //transportScrollTimer = 0.0f;

        // Load the sparkling jewel array
        //Texture tex = new Texture(Gdx.files.internal("data/images/gem_gold.png"));
        //tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//jewelSprites = new TextureRegion[4];
		//for(int i = 0; i < jewelSprites.length; i++) {
		//	jewelSprites[i] = new TextureRegion(tex, i * 128, 0, 128, 128);
		//}

        // Load the items to be displayed on the HUD
        keyRed = new Sprite(BikeGameTextures.LoadTexture("key_red",0));
        keyBlue = new Sprite(BikeGameTextures.LoadTexture("key_blue",0));
        keyGreen = new Sprite(BikeGameTextures.LoadTexture("key_green",0));
        jewelSprite = new Sprite(BikeGameTextures.LoadTexture("gem_gold",0));
        nitrous = new Sprite(BikeGameTextures.LoadTexture("nitrous",0));
        nitrousTube = new Sprite(BikeGameTextures.LoadTexture("nitrous_tube",0));
        nitrousFluid = new Sprite(BikeGameTextures.LoadTexture("nitrous_fluid",0));

        // Timer for this run
        timer = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        timer.setColor(0, 0, 0, 1);
        timer.setScale(0.35f);
        timerWidth = timer.getBounds("00:00:000").width;
        timerHeight = timer.getBounds("00:00:000").height;
        // World Record Timer
        timerWR = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        timerWR.setColor(0.8f, 0.725f, 0, 1);
        timerWR.setScale(0.2f);
        // Personal Best Timer
        timerPB = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        timerPB.setColor(0.5f, 0.5f, 0.5f, 1);
        timerPB.setScale(0.2f);
        timerWRWidth = timerWR.getBounds("WR  00:00:000").width;
        timerWRHeight = timerWR.getBounds("PB  00:00:000").height;
        keyRedCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        keyRedCntr.setScale(0.25f);
        keyRedCntr.setColor(1, 0, 0, 1);
        keyGreenCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        keyGreenCntr.setScale(0.25f);
        keyGreenCntr.setColor(0.2f, 1, 0.2f, 1);
        keyBlueCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        keyBlueCntr.setScale(0.25f);
        keyBlueCntr.setColor(0, 0.7f, 1, 1);
        jewelCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        jewelCntr.setScale(0.25f);
        jewelCntr.setColor(0.85f, 0.85f, 0, 1);
        nitrousCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        nitrousCntr.setScale(0.25f);
        nitrousCntr.setColor(0.2f, 0.2f, 1, 1);
        jcntrWidth = jewelCntr.getBounds("00").width;
        jcntrHeight = jewelCntr.getBounds("00").height;
        
        // Initiate some arrays
        switchGate = new Array<float[]>();
        kinematicBodies = new Array<Body>();
        kinematicPath = new Array<Vector2[]>();
        fallingJoints = new Array<Body>();
        fallingJointsFallTime = new Array<Float>();
        fallingJointsTime = new Array<Float>();

    	playerJump = 100.0f;
    	lrIsDown = false;
    }

    public void handleReplays(float dt) {
//    	int repTimer = (int) (TimeUtils.millis() - 0.5f*dt) - timerStart; // -0.5dt to account for rounding
    	int repTimer = (int) (TimeUtils.millis()) - timerStart; // -0.5dt to account for rounding
    	// Check the key presses
    	ReplayVars.Check(repTimer);
    }
    
    public void handleInput() {
    	int repTimer = (int) (TimeUtils.millis()) - timerStart;
    	// ESC is pressed
        if (GameInput.isPressed(GameInput.KEY_ESC)) {
        	forcequit = true;
        	ReplayVars.replayKeyPressTime.add(repTimer);
        	ReplayVars.replayKeyPress.add(GameInput.KEY_ESC);
        }
        // Accelerate
        if (GameInput.isDown(GameInput.KEY_ACCEL)) {
        	motorTorque = 10.0f;
        	// Replay actions
        	if ((ReplayVars.accelDown == false) & (!isReplay)) {
        		ReplayVars.replayAccel.add(1);
        		ReplayVars.replayAccelTime.add(repTimer);
        		ReplayVars.accelDown = true;
        	}
        }
        else {
        	motorTorque = 0.0f;
        	// Replay actions
        	if ((ReplayVars.accelDown == true) & (!isReplay)) {
        		ReplayVars.replayAccel.add(0);
        		ReplayVars.replayAccelTime.add(repTimer);
        		ReplayVars.accelDown = false;
        	}
        }
        // Brake
        if (GameInput.isDown(GameInput.KEY_BRAKE)) {
        	if (bikeDirc == 1.0f) {
        		bikeBodyLW.setAngularVelocity(0.0f);
        		bikeBodyLW.setFixedRotation(true);
        		bikeBodyRW.setAngularVelocity(0.9f*bikeBodyRW.getAngularVelocity());
        	} else {
        		bikeBodyRW.setAngularVelocity(0.0f);
        		bikeBodyRW.setFixedRotation(true);
        		bikeBodyLW.setAngularVelocity(0.9f*bikeBodyLW.getAngularVelocity());
        	}
        	// Replay actions
        	if ((ReplayVars.brakeDown == false) & (!isReplay)) {
        		ReplayVars.replayBrake.add(1);
        		ReplayVars.replayBrakeTime.add(repTimer);
        		ReplayVars.brakeDown = true;
        	}
        } else {
    		bikeBodyLW.setFixedRotation(false);        	
    		bikeBodyRW.setFixedRotation(false);        	
        	// Replay actions
        	if ((ReplayVars.brakeDown == true) & (!isReplay)) {
        		ReplayVars.replayBrake.add(0);
        		ReplayVars.replayBrakeTime.add(repTimer);
        		ReplayVars.brakeDown = false;
        	}
        }
        // Change Direction
        if (GameInput.isPressed(GameInput.KEY_CHDIR)) {
        	switchBikeDirection();
        	if  (!isReplay) {
            	ReplayVars.replayKeyPressTime.add(repTimer);
            	ReplayVars.replayKeyPress.add(GameInput.KEY_CHDIR);
        	} else ReplayVars.UpdateKeyPress();
        }
        //playerTorque = 0.0f;
        float torqueVal = 180.0f;
        if (GameInput.isDown(GameInput.KEY_SPINL)) {
        	lrIsDown = true;
        	if ((applyTorque<0.0f) & (applyJump<0.0f)) {
        		if (bikeDirc == 1.0f) playerTorque = torqueVal;
        		else playerTorque = torqueVal;//1500.0f;
        		applyTorque = 0.0f;
        		if (cl.isBikeOnGround()) playerTorque *= 0.95f;
        	}
        	// Replay actions
        	if ((ReplayVars.spinLDown == false) & (!isReplay)) {
        		ReplayVars.replaySpinL.add(1);
        		ReplayVars.replaySpinLTime.add(repTimer);
        		ReplayVars.spinLDown = true;
        	}
        } else if (GameInput.isDown(GameInput.KEY_SPINR)) {
        	lrIsDown = true;
        	if ((applyTorque<0.0f) & (applyJump<0.0f)) {
        		if (bikeDirc == 1.0f) playerTorque = -torqueVal;//-1500.0f;
        		else playerTorque = -torqueVal;
        		applyTorque = 0.0f;
        		if (cl.isBikeOnGround()) playerTorque *= 0.95f;
        	}
        	// Replay actions
        	if ((ReplayVars.spinRDown == false) & (!isReplay)) {
        		ReplayVars.replaySpinR.add(1);
        		ReplayVars.replaySpinRTime.add(repTimer);
        		ReplayVars.spinRDown = true;
        	}
        } else {
        	lrIsDown = false;
        	// Replay actions
        	if ((ReplayVars.spinLDown == true) & (!isReplay)) {
        		ReplayVars.replaySpinL.add(0);
        		ReplayVars.replaySpinLTime.add(repTimer);
        		ReplayVars.spinLDown = false;
        	}
        	if ((ReplayVars.spinRDown == true) & (!isReplay)) {
        		ReplayVars.replaySpinR.add(0);
        		ReplayVars.replaySpinRTime.add(repTimer);
        		ReplayVars.spinRDown = false;
        	}
        }
        // Tricks
        if ((GameInput.isPressed(GameInput.KEY_BUNNY)) & (cl.isBikeOnGround()) & (applyJump<0.0f) & (applyTorque<0.0f)) {
        	playerJump = 0;
        	applyJump = 0.0f;
        	if  (!isReplay) {
	        	ReplayVars.replayKeyPressTime.add(repTimer);
	        	ReplayVars.replayKeyPress.add(GameInput.KEY_BUNNY);
        	} else ReplayVars.UpdateKeyPress();
        } else if (GameInput.isDown(GameInput.KEY_NITROUS)) {
        	if ((collectNitrous > 0) | (nitrousLevel > 0.0f)) {
        		applyNitrous = 1;
        		nitrousLevel -= 0.002f;
        		if ((nitrousLevel < 0.0f) & (collectNitrous > 1)) {
        			nitrousLevel = 1.0f;
        			collectNitrous -= 1;
        		} else if (nitrousLevel < 0.0f) {
        			nitrousLevel = 0.0f;
        			collectNitrous -= 1;
        		}
            	// Replay actions
            	if ((ReplayVars.nitrousDown == false) & (!isReplay)) {
            		ReplayVars.replayNitrous.add(1);
            		ReplayVars.replayNitrousTime.add(repTimer);
            		ReplayVars.nitrousDown = true;
            	}
        	}
        } else if (GameInput.isDown(GameInput.KEY_NITROUS)==false) {
        	applyNitrous = 0;
        	// Replay actions
        	if ((ReplayVars.nitrousDown == true) & (!isReplay)) {
        		ReplayVars.replayNitrous.add(0);
        		ReplayVars.replayNitrousTime.add(repTimer);
        		ReplayVars.nitrousDown = false;
        	}
        }
        //if ((applyTorque<0.0f) & (applyJump<0.0f)) bikeAngle = bikeBodyC.getAngle();

    }
    
    public void update(float dt) {
        switch (mState)
        {
           case STARTING:
               initiateSceneLoad();
               break;
              
           case LOADING:
               processSceneLoad();
               break;

           case LOADED:
               // Include a "press any key to begin"
               break;
              
           case RUNNING:
        	   handleReplays(dt);
        	   handleInput();
        	   mWorld.step(dt, mVelocityIter, mPositionIter);
	       	   	if (cl.isFinished()) {
	     	   		if (collectJewel == 0) {
	     	   			timerTotal = (int) (TimeUtils.millis()) - timerStart;
	     	   			// Check the records
     	   				if (mode == 1) {
     	   					GameVars.CheckTimes(GameVars.plyrTimesTrain.get(GameVars.currentPlayer).get(levelID), 2, levelID, timerTotal, false);
     	   					GameVars.CheckTimes(GameVars.worldTimesTrain.get(levelID), 2, levelID, timerTotal, true);
     	   				} else if (mode == 2) {
     	   					GameVars.CheckTimes(GameVars.plyrTimes.get(GameVars.currentPlayer).get(levelID), 0, levelID, timerTotal, false);
     	   					GameVars.CheckTimes(GameVars.worldTimes.get(levelID), 0, levelID, timerTotal, true);
     	   				}
	     	   			// Check the records with a diamond
	     	   			if (collectDiamond) {
	     	   				if (mode == 1) {
	     	   					// Set the Diamond
	     	   					GameVars.SetDiamondTrain(levelID);
	     	   					// Check the time
	     	   					GameVars.CheckTimes(GameVars.plyrTimesTrainDmnd.get(GameVars.currentPlayer).get(levelID), 3, levelID, timerTotal, false);
	     	   					GameVars.CheckTimes(GameVars.worldTimesTrainDmnd.get(levelID), 3, levelID, timerTotal, true);
	     	   				} else if (mode == 2) {
	     	   					// Set the Diamond
	     	   					GameVars.SetDiamond(levelID);
	     	   					// Check the time
	     	   					GameVars.CheckTimes(GameVars.plyrTimesDmnd.get(GameVars.currentPlayer).get(levelID), 1, levelID, timerTotal, false);
	     	   					GameVars.CheckTimes(GameVars.worldTimesDmnd.get(levelID), 1, levelID, timerTotal, true);
	     	   				}
	     	   			}
	     	   			System.out.println(GameVars.getTimeString(timerTotal));
	     	   			gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
	     	   			break;
	     	   		} else cl.notFinished();
	     	   	} else if ((cl.isPlayerDead()) | (forcequit)) {
	            	//Game.res.getSound("hit").play();
//	     	   		gsm.setState(GameStateManager.PEEK, false, null);//Gdx.app.exit();
//	     	   		System.out.println("NEED TO CORRECT THIS!!!");
//	     	   		if (forcequit) gsm.setState(GameStateManager.PEEK, false, null);//Gdx.app.exit();
//	     	   		else {
//	     	   			cl.playerDead=false;
//	     	   			bikeBodyLW.setTransform(bikeBodyLWpos, bikeBodyLWang);
//	     	   			bikeBodyRW.setTransform(bikeBodyRWpos, bikeBodyRWang);
//	     	   			bikeBodyH.setTransform(bikeBodyHpos, bikeBodyHang);
//	     	   			bikeBodyC.setTransform(bikeBodyCpos, bikeBodyCang);
//	     	   			mState=GAME_STATE.LOADING;
//	     	   		}
	            	gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
	            	break;
	     	   	}
        	   updateBike(dt);
        	   updateCollect();
        	   updateFallingBodies(dt);
        	   updateKinematicBodies(dt);
        	   updateSwitches();
        	   if (canTransport < 0.0f) updateTransport();
        	   else canTransport += dt;
        	   if (canTransport >= transportTime) {
        		   cl.clearTransportBody();
        		   canTransport = -1.0f;
        	   }
        	   cl.clearBikeBodyCollide();
        	   if (applyTorque >= 0.0f) applyTorque += dt;
        	   if (applyTorque >= spinTime) {
        		   playerTorque = 0.0f;
        		   applyTorque = -1.0f;
        	   }
        	   if (applyJump >= 0.0f) applyJump += dt;
        	   if (applyJump >= jumpTime) applyJump = -1.0f;
        	   break;
        }
    }

//    private void updateAnimate() {
//    	// Animate the Jewels
//    	for (int i = 0; i < animateJewel.length; i++) {
//    		if (animateJewel[i] == -1) {
//    			if (Math.random() < 0.005f) animateJewel[i] = 0;
//    		} else {
//    			if (animateJewel[i] == jewelSprites.length) {
//    				// Reset back to the first spite
//    				
//    				// Reset the animation counter
//    				animateJewel[i] = -1;
//    			} else {
//    				// Set the new animation
//    				
//    				// Step the jewel animation counter
//    				animateJewel[i] += 1;
//    			}
//    		}
//    	}
//    }

	private void switchBikeDirection() {
		// Change the Bike Direction
		bikeDirc *= -1.0f;
		bikeScaleLev *= -1.0f;
		bikeScale += bikeScaleLev;

 	   // Destroy all joints, and create the left-facing joints
 	   // First Destroy
 	   mWorld.destroyJoint(leftWheel);
 	   mWorld.destroyJoint(rightWheel);
 	   mWorld.destroyJoint(leftRope);
 	   mWorld.destroyJoint(rightRope);
 	   // Now Create
 	   if (bikeDirc == 1.0f) {
 	 	   leftWheel = (WheelJoint) mWorld.createJoint(leftWheelR);
 	 	   rightWheel = (WheelJoint) mWorld.createJoint(rightWheelR);
 	 	   leftRope = (RopeJoint) mWorld.createJoint(leftRopeR);
 	 	   rightRope = (RopeJoint) mWorld.createJoint(rightRopeR);
 	   } else {
 	 	   leftWheel = (WheelJoint) mWorld.createJoint(leftWheelL);
 	 	   rightWheel = (WheelJoint) mWorld.createJoint(rightWheelL);
 	 	   leftRope = (RopeJoint) mWorld.createJoint(leftRopeL);
 	 	   rightRope = (RopeJoint) mWorld.createJoint(rightRopeL);
 	   }
 	   // Set the motor wheel
//		if (bikeDirc == 1.0f) {
//			leftWheel.enableMotor(true);
//			rightWheel.enableMotor(false);
//		} else {
//			leftWheel.enableMotor(true);
//			rightWheel.enableMotor(true);
//		}
		// Change the Head position
	    //Vector2 temppos = new Vector2(bikeBodyH.getPosition().x - bikeDirc*0.1f*MathUtils.cos(bikeBodyH.getAngle()) , bikeBodyH.getPosition().y - bikeDirc*0.1f*MathUtils.sin(bikeBodyH.getAngle()));
	    //bikeBodyH.setTransform(temppos, bikeBodyH.getAngle());
	    // Switch the texture
	}

	private void updateBike(float dt) {
		if (motorTorque != 0.0f) {
			if (bikeDirc == 1.0f) {
				if (bikeBodyLW.getAngularVelocity() > -100.0f) bikeBodyLW.applyTorque(-1.0f*(3*applyNitrous+1.0f)*Math.max(motorTorque, 500.0f/(1.0f+Math.abs(bikeBodyLW.getAngularVelocity()))), false);
			} else {
				if (bikeBodyRW.getAngularVelocity() < 100.0f) bikeBodyRW.applyTorque((3*applyNitrous+1.0f)*Math.max(motorTorque, 500.0f/(1.0f+Math.abs(bikeBodyRW.getAngularVelocity()))), false);
			}
		}
		if ((bikeScale > -1.0f) & (bikeScale < 1.0f)) {
			bikeScale += bikeScaleLev;
			if (bikeScale < -1.0f) {
				bikeScale = -1.0f;
			} else if (bikeScale > 1.0f) {
				bikeScale = 1.0f;
			}
		}
//		if (playerTorque != 0.0f) {
//			bikeBodyC.setAngularVelocity(bikeBodyC.getAngularVelocity()+0.015f*playerTorque);
//			System.out.println(0.015f*playerTorque);
//			System.out.println(applyTorque);
//			System.out.println(spinTime);
//			//bikeBodyC.applyTorque(playerTorque, false);
		if ((applyTorque >= 0.0f) & (applyTorque/spinTime<0.3f)){
			if ((lrIsDown)|((!lrIsDown)&(applyTorque/spinTime<0.1f))) {
				//bikeBodyC.setAngularVelocity(bikeBodyC.getAngularVelocity()+0.015f*1500.0f*applyTorque/spinTime);
				//bikeBodyC.applyTorque(0.07f*playerTorque*(applyTorque+0.4f)/spinTime, false);
				bikeBodyC.applyTorque(playerTorque, false);
			}
			//if (getAngleDiff() > 1.22f) { // 1.22 radians ~ 70 degrees
			//	bikeBodyC.setAngularVelocity(0.0f);
			//}
		} else if (applyJump >= 0.0) {
			float scale = 3000.0f;
			if (applyJump == 0) {
				if (bikeDirc == 1.0f) bikeBodyC.applyTorque(scale, false);
				else bikeBodyC.applyTorque(-scale, false);
				playerJump = bikeDirc;
			} else if ((applyJump >= 10.0f*dt)&((playerJump==1)|(playerJump==-1))) {
				bikeBodyC.applyForceToCenter(mWorld.getGravity().nor().scl(-Math.abs(playerJump)*0.7f*scale), false);
				playerJump *= 2;
			} else if ((applyJump >= 10.0f*dt)&((playerJump==2)|(playerJump==-2))) {
				bikeBodyC.applyTorque(-0.24f*playerJump*scale, false);
				playerJump = 0;
			}
		}
		Vector3 pos = new Vector3(bikeBodyC.getWorldCenter(), 0);
//		Vector2 posShft;
//		if (gravityScale == -1.0f) posShft = mWorld.getGravity().nor().scl(-(float)Math.sin(bikeScale*Math.PI/2)*B2DVars.SCRWIDTH/4.0f);
//		else {
//			posShft = (mWorld.getGravity().nor().scl((float)Math.sin(gravityScale*Math.PI/2)).add(gravityPrev.scl(1.0f-(float)Math.sin(gravityScale*Math.PI/2)))).nor().scl(-(float)Math.sin(bikeScale*Math.PI/2)*B2DVars.SCRWIDTH/4.0f);
//			gravityScale += 0.001f;
//			if (gravityScale > 1.0f) gravityScale = -1.0f;
//		}
		// Update the camera position...
		Vector2 posShft, gravVect;
		float angleGrav;
		if (gravityScale == -1.0f) posShft = mWorld.getGravity().nor().scl(-B2DVars.SCRWIDTH/8.0f);
		else {
			//posShft = (mWorld.getGravity().nor().scl((float)Math.sin(gravityScale*Math.PI/2)).add(gravityPrev.scl(1.0f-(float)Math.sin(gravityScale*Math.PI/2)))).nor().scl(-B2DVars.SCRWIDTH/8.0f);
			angleGrav = (float) (Math.acos(gravityPrev.cpy().nor().dot(mWorld.getGravity().cpy().nor())));
			gravVect = gravityPrev.cpy().nor().scl(-B2DVars.SCRWIDTH/8.0f);//.scl(gravityNext.len() * (float) (Math.cos(angleGrav*0.5f)/Math.cos(angleGrav*(0.5f - gravityScale))));
			angleGrav *= (float)Math.sin(gravityScale*Math.PI/2);
			if (dircGrav < 0.0f) angleGrav *= -1.0f; // Rotate clockwise
			posShft = new Vector2((float)(gravVect.x*Math.cos(angleGrav) - gravVect.y*Math.sin(angleGrav)), (float)(gravVect.x*Math.sin(angleGrav) + gravVect.y*Math.cos(angleGrav)));
			gravityScale += 0.005f;
			if (gravityScale > 1.0f) gravityScale = -1.0f;
		}
		Vector2 shftCpy = posShft.cpy().scl(-(float)Math.sin(bikeScale*Math.PI/2)); 
		pos.x += posShft.x - shftCpy.y;
		pos.y += posShft.y + shftCpy.x;
		b2dCam.position.set(pos);
		b2dCam.update();
	}

    private void updateCollect() {
    	// check for collected keys or jewels
    	Array<Body> bodies = cl.getBodies();
    	for(int i = 0; i < bodies.size; i++) {
    		String collectID = (String)mScene.getCustom(bodies.get(i), "collect", null);
    		if (collectID != null) {
    			boolean noKeys = false;
    			if (collectID.equals("DoorRed")) {
    				if (collectKeyRed > 0) {
    					collectKeyRed -= 1;
    					Body bbcollide = cl.getBikeBodyCollide().pop();
    					doorArr[0] = bodies.get(i).getPosition().x;
    					doorArr[1] = bodies.get(i).getPosition().y;
    					doorArr[3] = 2.0f*ObjectVars.objectDoor[5]*B2DVars.EPPM;
    					doorArr[4] = ((Float)mScene.getCustom(bodies.get(i), "angle", 0.0f));
    					doorArr[2] = 0.01f*PolygonOperations.OpenDoorDirection(doorArr[0],doorArr[1],bbcollide.getPosition().x,bbcollide.getPosition().y,doorArr[4]);
    					doorImages.add(doorArr.clone());
        				//Game.res.getSound("opendoor").play();
    				} else noKeys = true;
    			} else if (collectID.equals("DoorGreen")) {
    				if (collectKeyGreen > 0) {
    					collectKeyGreen -= 1;
    					Body bbcollide = cl.getBikeBodyCollide().pop();
    					doorArr[0] = bodies.get(i).getPosition().x;
    					doorArr[1] = bodies.get(i).getPosition().y;
    					doorArr[3] = 2.0f*ObjectVars.objectDoor[5]*B2DVars.EPPM;
    					doorArr[4] = ((Float)mScene.getCustom(bodies.get(i), "angle", 0.0f));
    					doorArr[2] = 0.01f*PolygonOperations.OpenDoorDirection(doorArr[0],doorArr[1],bbcollide.getPosition().x,bbcollide.getPosition().y,doorArr[4]);
    					doorImages.add(doorArr.clone());
        				//Game.res.getSound("opendoor").play();
    				} else noKeys = true;
    			} else if (collectID.equals("DoorBlue")) {
    				if (collectKeyBlue > 0) {
    					collectKeyBlue -= 1;
    					Body bbcollide = cl.getBikeBodyCollide().pop();
    					doorArr[0] = bodies.get(i).getPosition().x;
    					doorArr[1] = bodies.get(i).getPosition().y;
    					doorArr[3] = 2.0f*ObjectVars.objectDoor[5]*B2DVars.EPPM;
    					doorArr[4] = ((Float)mScene.getCustom(bodies.get(i), "angle", 0.0f));
    					doorArr[2] = 0.01f*PolygonOperations.OpenDoorDirection(doorArr[0],doorArr[1],bbcollide.getPosition().x,bbcollide.getPosition().y,doorArr[4]);
    					doorImages.add(doorArr.clone());
        				//Game.res.getSound("opendoor").play();
    				} else noKeys = true;
    			} else if (collectID.equals("Gravity")) {
    				float angleGrav;
    				if (gravityScale >= 0.0f) {
    					angleGrav = (float) (Math.acos(gravityPrev.cpy().nor().dot(mWorld.getGravity().cpy().nor())));
    					angleGrav *= (float)Math.sin(gravityScale*Math.PI/2);
    					Vector2 gravVect = gravityPrev.cpy().nor().scl(gravityPrev.len());
    					if (dircGrav < 0.0f) angleGrav *= -1.0f; // Rotate clockwise
    					gravityPrev = new Vector2((float)(gravVect.x*Math.cos(angleGrav) - gravVect.y*Math.sin(angleGrav)), (float)(gravVect.x*Math.sin(angleGrav) + gravVect.y*Math.cos(angleGrav)));
    					//gravityPrev = mWorld.getGravity().cpy().nor();
    				
    				} else gravityPrev = mWorld.getGravity().cpy().nor();
    				Vector2 gravNext = (Vector2)mScene.getCustom(bodies.get(i), "gravityVector", mWorld.getGravity());
    				dircGrav = gravityPrev.x*gravNext.y - gravityPrev.y*gravNext.x;
    				gravityScale = 0.0f;
    				mWorld.setGravity(gravNext);
    				//Game.res.getSound("gravity").play();
    			} else if (collectID.equals("KeyRed")) {
    				collectKeyRed += 1;
    				//Game.res.getSound("key").play();
    			} else if (collectID.equals("KeyGreen")) {
    				collectKeyGreen += 1;
    				//Game.res.getSound("key").play();
    			} else if (collectID.equals("KeyBlue")) {
    				collectKeyBlue += 1;
    				//Game.res.getSound("key").play();
    			} else if (collectID.equals("Nitrous")) {
    				if ((collectNitrous == 0) & (nitrousLevel == 0.0f)) nitrousLevel = 1.0f;
    				collectNitrous += 1;
    				//Game.res.getSound("jewel").play();
    			} else if (collectID.equals("Jewel")) {
    				if (collectJewel != 0) collectJewel -= 1;
    				//Game.res.getSound("jewel").play();
    			} else if (collectID.equals("Diamond")) {
    				collectJewel = 0;
    				collectDiamond = true; // The player has collected the diamond
    				//Game.res.getSound("jewel").play();
    			}
    			if (!noKeys) {
	        		// Remove the collected item from the loop
	    			nullvar = -1;
	        		for (int j = 0; j < remBodies.size; j++) {
	        			if (remBodies.get(j) == bodies.get(i)) {
	        				mSpatials.removeIndex(remBodiesIdx.get(j));
	        				nullvar = j;
	        			} else if (nullvar != -1) {
	        				remBodiesIdx.set(j, remBodiesIdx.get(j)-1);
	        			}
	        		}
	        		if (nullvar != -1) {
	    				remBodies.removeIndex(nullvar);
	    				remBodiesIdx.removeIndex(nullvar);
	        		}
	        		mWorld.destroyBody(bodies.get(i));
    			}
    		}
    	}
    	if ((collectJewel == 0) & (finishRad == 0.0f)) finishRad += 0.1f;
    	// Update keys in the contact listener
    	if (collectKeyRed!=0) cl.setKey(0, true);
    	else cl.setKey(0, false);
    	if (collectKeyGreen!=0) cl.setKey(1, true);
    	else cl.setKey(1, false);
    	if (collectKeyBlue!=0) cl.setKey(2, true);
    	else cl.setKey(2, false);
    	bodies.clear();
    }

    private void updateFallingBodies(float dt) {
    	Array<Body> joints = cl.getJoints();
    	boolean touched;
    	for (int i = 0; i < joints.size; i++) {
    		touched = false;
    		for (int j = 0; j < fallingJoints.size; j++) {
        		if (joints.get(i).getJointList().first().joint.equals(fallingJoints.get(j).getJointList().first().joint)) touched = true;
    		}
    		if (touched == false) {
    			fallingJoints.add(joints.get(i));
    			fallingJointsFallTime.add((Float) mScene.getCustom(joints.get(i), "FallTime", fallTime));
    			fallingJointsTime.add(0.0f);
    		}
    	}
    	int i = 0;
    	for (int j = 0; j < fallingJointsTime.size; j++) {
    		fallingJointsTime.set(i, dt+fallingJointsTime.get(i));
    		if (fallingJointsTime.get(i)>fallingJointsFallTime.get(i)) {
    			mWorld.destroyJoint(fallingJoints.get(i).getJointList().first().joint);
    			fallingJoints.removeIndex(i);
    			fallingJointsFallTime.removeIndex(i);
    			fallingJointsTime.removeIndex(i);
    		} else i += 1;
    	}
    	joints.clear();
    }

    private void updateKinematicBodies(float dt) {
    	// Step each kinematic body along by the specified amount
    	float moveBy, gotoScale, speed, tval;
    	Vector2 currentPosition, linVel;
    	for (int i=0; i<kinematicBodies.size; i++) {
    		currentPosition = kinematicBodies.get(i).getPosition().cpy();
    		// Calculate the new location
    		moveBy = 0.0f;
			if (kinematicLengthLeft[i] > kinematicLength[i]) kinematicLengthLeft[i] = kinematicLength[i];
    		if (kinematicLength[i] < 4.0f*kinematicSpeed[i]) {
    			if (kinematicLengthLeft[i] > kinematicLength[i]/2) {
    				// Easing out
	    			tval = (1-2*((kinematicLength[i]-kinematicLengthLeft[i])/kinematicLength[i]));
	    			if (tval >= 1.0f) tval = 0.999f;
	    			speed = kinematicSpeed[i]*(float) Math.sqrt(1.0f-tval*tval);
	        		moveBy = speed*dt;
    			} else {
    				// Easing in
	    			tval = (1-2*(kinematicLengthLeft[i]/kinematicLength[i]));
	    			if (tval >= 1.0f) tval = 0.999f;
	    			speed = kinematicSpeed[i]*(float) Math.sqrt(1.0f - tval*tval);
	        		moveBy = speed*dt;
    			}
    		} else {
	    		if (kinematicLengthLeft[i] < 2.0f*kinematicSpeed[i]) {
	    			tval = (1.0f-0.5f*(kinematicLengthLeft[i]/kinematicSpeed[i]));
	    			if (tval >= 1.0f) tval = 0.999f;
	    			speed = kinematicSpeed[i]*(float) Math.sqrt(1.0f - tval*tval);
	        		moveBy = speed*dt;
	    		} else if ((kinematicLength[i]-kinematicLengthLeft[i]) < 2.0f*kinematicSpeed[i]) {
	    			tval = (1.0f-0.5f*((kinematicLength[i]-kinematicLengthLeft[i])/kinematicSpeed[i]));
	    			if (tval >= 1.0f) tval = 0.999f;
	    			speed = kinematicSpeed[i]*(float) Math.sqrt(1.0f-tval*tval);
	        		moveBy = speed*dt;
	    		} else moveBy = kinematicSpeed[i]*dt;
    		}
    		if (moveBy < 0.001f) moveBy = 0.001f;
			// gotoScale is the distance between the current location and the next vertex on the path
			gotoScale = (kinematicPath.get(i)[kinematicIndex[i]+kinematicDirection[i]].cpy().sub(currentPosition.cpy())).len();
			while (moveBy > gotoScale) {
				if (gotoScale == 0.0) break;
				moveBy -= gotoScale;
				kinematicLengthLeft[i] = kinematicLengthLeft[i] - gotoScale;
				kinematicIndex[i] = kinematicIndex[i] + kinematicDirection[i];
				currentPosition = kinematicPath.get(i)[kinematicIndex[i]].cpy();
				if ((kinematicIndex[i]==0) | (kinematicIndex[i]==kinematicPath.get(i).length-1)) {
					kinematicDirection[i] *= -1;
					kinematicLengthLeft[i] = kinematicLength[i];
				}
				gotoScale = (kinematicPath.get(i)[kinematicIndex[i]+kinematicDirection[i]].cpy().sub(currentPosition.cpy())).len();
			}
			currentPosition = ((kinematicPath.get(i)[kinematicIndex[i]+kinematicDirection[i]].cpy().sub(currentPosition.cpy())).nor().scl(moveBy)).add(currentPosition.cpy());
			kinematicLengthLeft[i] = kinematicLengthLeft[i] - moveBy;
			// Calculate the linear velocity (dx/dt)
			linVel = (currentPosition.cpy().sub(kinematicBodies.get(i).getPosition().cpy())).scl(1.0f/dt);
			currentPosition = currentPosition.cpy().sub(kinematicBodies.get(i).getLinearVelocity().cpy().scl(dt));
    		// Move the body to the next location
    		kinematicBodies.get(i).setTransform(currentPosition.cpy(), kinematicBodies.get(i).getAngle());
    		kinematicBodies.get(i).setLinearVelocity(linVel.cpy());

    	}
    	return;
    }

    @SuppressWarnings("unchecked")
	private void updateSwitches() {
    	// check if a switch has been touched
    	Array<Body> bodies = cl.getSwitchBody();
    	if (bodies.size != 0) {
    		int switchIdx = (Integer) mScene.getCustom(bodies.get(0), "switchID", -1);
    		if (switchIdx >= 0) {
    			float[] switchArr = switchGate.get(switchIdx);
    			Body bbcollide = cl.getBikeBodyCollide().pop();
        		boolean switchit = PolygonOperations.SwitchOnOff(switchArr[4],switchArr[5],bbcollide.getPosition().x,bbcollide.getPosition().y,MathUtils.degreesToRadians*switchArr[6],switchArr[7]);
        		if (switchit) {
        			switchArr[7] *= -1.0f;
        			switchArr[8] = 1.0f - switchArr[8];
        			if (switchArr[8] == 0.0f) switchArr[9] = 1.0f;
        			else switchArr[9] = -1.0f;
        			switchGate.set(switchIdx,switchArr.clone());
        		}
    		}
    	}
    	bodies.clear();
    	// Create or destroy the gate fixtures
    	float[] switchArrt;
    	PolygonShape shape;
		Array<Integer> userData = (Array<Integer>) switchGateBody.getUserData();
    	Array<Fixture> fixtList = switchGateBody.getFixtureList();
    	FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_WHEEL | B2DVars.BIT_HEAD | B2DVars.BIT_CHAIN;
		fdef.friction = 0.9f;
		fdef.restitution = 0.2f;
    	for (int i=0; i<switchGate.size; i++) {
    		switchArrt = switchGate.get(i);
    		if (switchArrt[9] == 1.0f) {
    			// Create a new fixture
    			shape = new PolygonShape();
    			shape.setAsBox(0.0332f, switchArrt[2]/2, new Vector2(switchArrt[0], switchArrt[1]), MathUtils.degreesToRadians*switchArrt[3]);
    			fdef.shape = shape;
    			// create player foot fixture
    			switchGateBody.createFixture(fdef);
    			userData.add(i);
    			shape.dispose();
    			switchArrt[9] = 0.0f;
    			switchGate.set(i,switchArrt.clone());
    		} else if (switchArrt[9] == -1.0f) {
    			// Destroy a fixture
    			for (int j=0; j<fixtList.size; j++) {
    				if (userData.get(j) == i) {
    					switchGateBody.destroyFixture(fixtList.get(j));
    					userData.removeIndex(j);
    	    			switchArrt[9] = 0.0f;
    	    			switchGate.set(i,switchArrt.clone());
    					break;
    				}
    			}
    		}
    	}
		switchGateBody.setUserData(userData);
    }

    private void updateTransport() {
    	// check if a transport has been touched
    	Array<Body> bodies = cl.getTransportBody();
    	if (bodies.size != 0) {
    		Vector2 transportXY = (Vector2)mScene.getCustom(bodies.get(0), "transportXY", null);
    		float transportAngle = (Float)mScene.getCustom(bodies.get(0), "transportAngle", 0.0f);
    		if (transportXY != null) {
    			// Derive the offset from the chassis to the centre of the transporter and account for rotation (use the chassis as reference)
    			Vector2 offsetXY = new Vector2(bikeBodyC.getPosition().x-bodies.get(0).getPosition().x, bikeBodyC.getPosition().y-bodies.get(0).getPosition().y);
    			float[] cCoord = PolygonOperations.RotateCoordinate(offsetXY.x, offsetXY.y, transportAngle*MathUtils.radiansToDegrees, 0.0f, 0.0f);
    			Vector2 transXY = new Vector2(cCoord[0],cCoord[1]).sub(offsetXY);
    			// Rotate the whole bike
    			// Transform the left wheel
    			offsetXY = bikeBodyC.getPosition().cpy();
    			float switchAngle = transportAngle*MathUtils.radiansToDegrees;
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyLW.getPosition().x-offsetXY.x, bikeBodyLW.getPosition().y-offsetXY.y, switchAngle, 0.0f, 0.0f);
    			Vector2 shiftLW = new Vector2(cCoord[0],cCoord[1]).add(offsetXY).sub(bikeBodyLW.getPosition());
    			// Transform the Right Wheel
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyRW.getPosition().x-offsetXY.x, bikeBodyRW.getPosition().y-offsetXY.y, switchAngle, 0.0f, 0.0f);
    			Vector2 shiftRW = new Vector2(cCoord[0],cCoord[1]).add(offsetXY).sub(bikeBodyRW.getPosition());
    			// Transform the Head
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyH.getPosition().x-offsetXY.x, bikeBodyH.getPosition().y-offsetXY.y, switchAngle, 0.0f, 0.0f);
    			Vector2 shiftH = new Vector2(cCoord[0],cCoord[1]).add(offsetXY).sub(bikeBodyH.getPosition());
    			// Transform the bike
    			bikeBodyLW.setTransform(bikeBodyLW.getPosition().add(transportXY).add(transXY).add(shiftLW), bikeBodyLW.getAngle()+transportAngle);
    			bikeBodyRW.setTransform(bikeBodyRW.getPosition().add(transportXY).add(transXY).add(shiftRW), bikeBodyRW.getAngle()+transportAngle);
    			bikeBodyH.setTransform(bikeBodyH.getPosition().add(transportXY).add(transXY).add(shiftH), bikeBodyH.getAngle()+transportAngle);
    			bikeBodyC.setTransform(bikeBodyC.getPosition().add(transportXY).add(transXY), bikeBodyC.getAngle()+transportAngle);
    			// Transform the velocity for each component of the bike
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyC.getLinearVelocity().x, bikeBodyC.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
    			bikeBodyC.setLinearVelocity(cCoord[0],cCoord[1]);
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyH.getLinearVelocity().x, bikeBodyH.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
    			bikeBodyH.setLinearVelocity(cCoord[0],cCoord[1]);
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyLW.getLinearVelocity().x, bikeBodyLW.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
    			bikeBodyLW.setLinearVelocity(cCoord[0],cCoord[1]);
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyRW.getLinearVelocity().x, bikeBodyRW.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
    			bikeBodyRW.setLinearVelocity(cCoord[0],cCoord[1]);
    			// Force transporters to be inactive
    			canTransport = 0.0f;
    		}
    	}
    	bodies.clear();
    }

    public void render() {
        // clear screen
    	Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        switch (mState)
        {
           case STARTING:
        	   break;
           case LOADING:
//              if (!mHideLoadingText)
//              {
//                 mBatch.setProjectionMatrix(mTextCam.combined);
//                 mBatch.begin();
//                 mBitmapFont.draw(mBatch,"Loading...",10,40);
//                 mBatch.end();
//              }
        	   break;
           case LOADED:
        	   mNextState = GAME_STATE.RUNNING;
               // Start the timer
               timerStart = (int) (TimeUtils.millis());
        	   break;
           case RUNNING:
        	   renderWorld();
        	   break;
           default:
        	   break;
        }
        
        // state transitions here...
        mPrevState = mState;
        mState = mNextState;
    }

    
    private void initiateSceneLoad()
    {
       if (mUseAssetManager)
       {
          // kick off asset manager operations...
          mAssetManager = new AssetManager();
          mAssetManager.setLoader(RubeScene.class, new RubeSceneAsyncLoader(new InternalFileHandleResolver()));
          // kick things off..
          mAssetManager.load(LEVEL_FILE_LIST[mRubeFileList][mRubeFileIndex], RubeScene.class);
       }
       mNextState = GAME_STATE.LOADING;
    }
    
    /**
     * Either performs a blocking load or a poll on the asset manager load...
     */
    private void processSceneLoad()
    {
       if (mAssetManager == null)
       {
          // perform a blocking load...
          RubeSceneLoader loader = new RubeSceneLoader();
          for (int i = 0; i < LEVEL_FILE_LIST[mRubeFileList].length; i++)
          {
             // each iteration adds to the scene that is ultimately returned...
        	  if ((i==0) & (editorString != null)) {
        		  // Load an editor scene
        		  mScene = loader.addEditorScene(editorString);
        		  mRubeFileIndex++;
        	  } else {
        		  // Load a level
        		  mScene = loader.addScene(Gdx.files.internal(LEVEL_FILE_LIST[mRubeFileList][mRubeFileIndex++]));
        	  }
          }
          processScene();
          mNextState = GAME_STATE.LOADED;
       }
       else if (mAssetManager.update())
       {
          // each iteration adds to the scene that is ultimately returned...
          mScene = mAssetManager.get(LEVEL_FILE_LIST[mRubeFileList][mRubeFileIndex++], RubeScene.class);
          if (mRubeFileIndex < LEVEL_FILE_LIST[mRubeFileList].length)
          {
             mAssetManager.load(LEVEL_FILE_LIST[mRubeFileList][mRubeFileIndex], RubeScene.class);
          }
          else
          {
             processScene();
             mNextState = GAME_STATE.LOADED;
          }
       }
    }
    
    /**
     * Builds up world based on info from the scene...
     */
    private void processScene()
    {
       createSpatialsFromRubeImages(mScene);
       createPolySpatialsFromRubeFixtures(mScene);

       mWorld = mScene.getWorld();
       // configure simulation settings
       mVelocityIter = mScene.velocityIterations;
       mPositionIter = mScene.positionIterations;
//       if (mScene.stepsPerSecond != 0)
//       {
//          mSecondsPerStep = 1f / mScene.stepsPerSecond;
//       }
       mWorld.setContactListener(cl);

       //
       // example of custom property handling
       //
//       Array<Body> bodies = mScene.getBodies();
//       if ((bodies != null) && (bodies.size > 0))
//       {
//          for (int i = 0; i < bodies.size; i++)
//          {
//             Body body = bodies.get(i);
//             String gameInfo = (String)mScene.getCustom(body, "collect", null);
//             if (gameInfo != null)
//             {
//                System.out.println("GameInfo custom property: " + gameInfo);
//             }
//          }
//       }

       // Example of accessing data based on name
//       System.out.println("body0 count: " + mScene.getNamed(Body.class, "body0").size);
//       System.out.println("fixture0 count: " + mScene.getNamed(Fixture.class, "fixture0").size);
       //mScene.printStats();
       
       // Get the starting position
       gameInfo = mScene.getNamed(Body.class, "GameInfo").first();
       startPosition = (Vector2) mScene.getCustom(gameInfo, "startPosition", null);
       finishPosition = (Vector2) mScene.getCustom(gameInfo, "finishPosition", null);
       startDirection = (Float) mScene.getCustom(gameInfo, "startDirection", 1.0f);
       startAngle = (Float) mScene.getCustom(gameInfo, "startAngle", 0.0f);
       collectJewel = (Integer) mScene.getCustom(gameInfo, "numJewel", 0);
       String skyTextureName = (String) mScene.getCustom(gameInfo, "skyTexture", "data/images/sky_bluesky.png");
       sky = new Sprite(BikeGameTextures.LoadTexture(FilenameUtils.getBaseName(skyTextureName),2));
       bikeDirc = startDirection; // 1=right, -1=left
       bikeScale = startDirection;
       bikeScaleLev *= startDirection;

       if ((skyTextureName.equals("data/images/sky_mars.png")) | (skyTextureName.equals("data/images/sky_moon.png"))) {
    	   paintBackdrop = false;
    	   timer.setColor(0.5f, 0.5f, 0.5f, 1);
       } else paintBackdrop = true;
       // Get the two bike wheel motors
       leftWheel = mScene.getNamed(WheelJoint.class, "leftwheel").first();
       rightWheel = mScene.getNamed(WheelJoint.class, "rightwheel").first();
       leftRope = mScene.getNamed(RopeJoint.class, "leftrope").first();
       rightRope = mScene.getNamed(RopeJoint.class, "rightrope").first();
       // Bug with Box2D -- set the anchor for each joint manually
       Vector2 lWR_lAnchorA = new Vector2(-0.5f,-0.3f);
       Vector2 lWR_lAxisA = new Vector2(0.0f,1.0f);
       Vector2 rWR_lAnchorA = new Vector2(0.5f,-0.3f);
       Vector2 rWR_lAxisA = new Vector2(-0.416146844625f,0.909297406673f);
       Vector2 lRR_lAnchorA = new Vector2(-0.5f,-0.52f);
       Vector2 rRR_lAnchorA = new Vector2(0.591552257538f,-0.50004543390f);
       // Obtain and set the wheel joint definitions
       // First, facing right
       leftWheelR.bodyA = leftWheel.getBodyA();
       leftWheelR.bodyB = leftWheel.getBodyB();
       leftWheelR.collideConnected = leftWheel.getCollideConnected();
       leftWheelR.localAnchorA.set(lWR_lAnchorA.cpy());
       leftWheelR.localAnchorB.set(new Vector2(0,0));
       leftWheelR.localAxisA.set(lWR_lAxisA.cpy());
       leftWheelR.enableMotor = leftWheel.isMotorEnabled();
       leftWheelR.motorSpeed = leftWheel.getMotorSpeed();
       leftWheelR.maxMotorTorque = leftWheel.getMaxMotorTorque();
       leftWheelR.frequencyHz = leftWheel.getSpringFrequencyHz();
       leftWheelR.dampingRatio = leftWheel.getSpringDampingRatio();
       rightWheelR.bodyA = rightWheel.getBodyA();
       rightWheelR.bodyB = rightWheel.getBodyB();
       rightWheelR.collideConnected = rightWheel.getCollideConnected();
       rightWheelR.localAnchorA.set(rWR_lAnchorA.cpy());
       rightWheelR.localAnchorB.set(new Vector2(0,0));
       rightWheelR.localAxisA.set(rWR_lAxisA.cpy());
       rightWheelR.enableMotor = rightWheel.isMotorEnabled();
       rightWheelR.motorSpeed = rightWheel.getMotorSpeed();
       rightWheelR.maxMotorTorque = rightWheel.getMaxMotorTorque();
       rightWheelR.frequencyHz = rightWheel.getSpringFrequencyHz();
       rightWheelR.dampingRatio = rightWheel.getSpringDampingRatio();
       // Now when facing left
       leftWheelL.bodyA = leftWheel.getBodyA();
       leftWheelL.bodyB = leftWheel.getBodyB();
       leftWheelL.collideConnected = leftWheel.getCollideConnected();
       leftWheelL.localAnchorA.set(lWR_lAnchorA.cpy());
       leftWheelL.localAnchorB.set(new Vector2(0,0));
       Vector2 tempVec = rWR_lAxisA.cpy();
       tempVec.x = -tempVec.x;
       leftWheelL.localAxisA.set(tempVec.cpy());
       leftWheelL.enableMotor = rightWheel.isMotorEnabled();
       leftWheelL.motorSpeed = rightWheel.getMotorSpeed();
       leftWheelL.maxMotorTorque = rightWheel.getMaxMotorTorque();
       leftWheelL.frequencyHz = rightWheel.getSpringFrequencyHz();
       leftWheelL.dampingRatio = rightWheel.getSpringDampingRatio();
       rightWheelL.bodyA = rightWheel.getBodyA();
       rightWheelL.bodyB = rightWheel.getBodyB();
       rightWheelL.collideConnected = rightWheel.getCollideConnected();
       rightWheelL.localAnchorA.set(rWR_lAnchorA.cpy());
       rightWheelL.localAnchorB.set(new Vector2(0,0));
       rightWheelL.localAxisA.set(lWR_lAxisA.cpy());
       rightWheelL.enableMotor = leftWheel.isMotorEnabled();
       rightWheelL.motorSpeed = leftWheel.getMotorSpeed();
       rightWheelL.maxMotorTorque = leftWheel.getMaxMotorTorque();
       rightWheelL.frequencyHz = leftWheel.getSpringFrequencyHz();
       rightWheelL.dampingRatio = leftWheel.getSpringDampingRatio();
       // Obtain and set the rope joint definitions
       // First, facing right
       leftRopeR.bodyA = leftRope.getBodyA();
       leftRopeR.bodyB = leftRope.getBodyB();
       leftRopeR.collideConnected = leftRope.getCollideConnected();
       leftRopeR.localAnchorA.set(lRR_lAnchorA.cpy());
       leftRopeR.localAnchorB.set(new Vector2(0,0));
       leftRopeR.maxLength = leftRope.getMaxLength();
       rightRopeR.bodyA = rightRope.getBodyA();
       rightRopeR.bodyB = rightRope.getBodyB();
       rightRopeR.collideConnected = rightRope.getCollideConnected();
       rightRopeR.localAnchorA.set(rRR_lAnchorA.cpy());
       rightRopeR.localAnchorB.set(new Vector2(0,0));
       rightRopeR.maxLength = rightRope.getMaxLength();
       // Now when facing left
       leftRopeL.bodyA = leftRope.getBodyA();
       leftRopeL.bodyB = leftRope.getBodyB();
       leftRopeL.collideConnected = leftRope.getCollideConnected();
       tempVec = rRR_lAnchorA.cpy();
       tempVec.x = -tempVec.x;
       leftRopeL.localAnchorA.set(tempVec.cpy());
       leftRopeL.localAnchorB.set(new Vector2(0,0));
       leftRopeL.maxLength = rightRope.getMaxLength();
       rightRopeL.bodyA = rightRope.getBodyA();
       rightRopeL.bodyB = rightRope.getBodyB();
       rightRopeL.collideConnected = rightRope.getCollideConnected();
       tempVec = lRR_lAnchorA.cpy();
       tempVec.x = -tempVec.x;
       rightRopeL.localAnchorA.set(tempVec.cpy());
       rightRopeL.localAnchorB.set(new Vector2(0,0));
       rightRopeL.maxLength = leftRope.getMaxLength();

       if (startDirection == -1.0f) {
    	   // Destroy all joints, and create the left-facing joints
    	   // First Destroy
    	   mWorld.destroyJoint(leftWheel);
    	   mWorld.destroyJoint(rightWheel);
    	   mWorld.destroyJoint(leftRope);
    	   mWorld.destroyJoint(rightRope);
    	   // Now Create
    	   leftWheel = (WheelJoint) mWorld.createJoint(leftWheelL);
    	   rightWheel = (WheelJoint) mWorld.createJoint(rightWheelL);
    	   leftRope = (RopeJoint) mWorld.createJoint(leftRopeL);
    	   rightRope = (RopeJoint) mWorld.createJoint(rightRopeL);
       }

//       bikeBody.add(mScene.getNamed(Body.class, "bikebody0").first());
//       bikeBody.add(mScene.getNamed(Body.class, "bikebody1").first());
//       bikeBody.add(mScene.getNamed(Body.class, "bikebody2").first());
//       bikeBody.add(mScene.getNamed(Body.class, "bikebody3").first());
//       bikeBody.add(mScene.getNamed(Body.class, "bikebody4").first());
//       bikeBody.add(mScene.getNamed(Body.class, "bikebodyl").first());
//       bikeBody.add(mScene.getNamed(Body.class, "bikebodyr").first());
//       for (int i = 0; i < bikeBody.size; i++) {
//    	   bikeBodyPart = bikeBody.get(i);
//    	   bikeBodyPart.setTransform(startPosition, 0.0f);
//       }
       bikeBodyLW = mScene.getNamed(Body.class, "bikeleftwheel").first();
       bikeBodyRW = mScene.getNamed(Body.class, "bikerightwheel").first();
       bikeBodyH = mScene.getNamed(Body.class, "bikehead").first();
       bikeBodyC = mScene.getNamed(Body.class, "bikebody").first();
       //bikeAngle = bikeBodyC.getAngle();
       bikeBodyLW.setSleepingAllowed(false);
       bikeBodyRW.setSleepingAllowed(false);
       bikeBodyH.setSleepingAllowed(false);
       bikeBodyC.setSleepingAllowed(false);
       bikeBodyC.setAngularDamping(2.0f);
       // First, get the transform for rotating the body
       // Transform the left wheel
       bikeBodyLW.setTransform(new Vector2(0.0f,0.0f), startAngle);
       // Transform the Right Wheel
       float[] cCoord = PolygonOperations.RotateCoordinate(bikeBodyRW.getPosition().x, bikeBodyRW.getPosition().y, startAngle*MathUtils.radiansToDegrees, 0.0f, 0.0f);
       Vector2 temppos = new Vector2(cCoord[0],cCoord[1]);
       bikeBodyRW.setTransform(temppos, startAngle);
       // Transform the Head
       cCoord = PolygonOperations.RotateCoordinate(bikeBodyH.getPosition().x, bikeBodyH.getPosition().y, startAngle*MathUtils.radiansToDegrees, 0.0f, 0.0f);
       temppos = new Vector2(cCoord[0],cCoord[1]);
       bikeBodyH.setTransform(temppos, startAngle);
       // Transform the Chassis
       cCoord = PolygonOperations.RotateCoordinate(bikeBodyC.getPosition().x, bikeBodyC.getPosition().y, startAngle*MathUtils.radiansToDegrees, 0.0f, 0.0f);
       temppos = new Vector2(cCoord[0],cCoord[1]);
       bikeBodyC.setTransform(temppos, startAngle);
       // Translate the bike into the starting position
       bikeBodyLW.setTransform(bikeBodyLW.getPosition().add(startPosition), bikeBodyLW.getAngle());
       bikeBodyRW.setTransform(bikeBodyRW.getPosition().add(startPosition), bikeBodyRW.getAngle());
       bikeBodyH.setTransform(bikeBodyH.getPosition().add(startPosition), bikeBodyH.getAngle());
       bikeBodyC.setTransform(bikeBodyC.getPosition().add(startPosition), bikeBodyC.getAngle());

       // Get all references to switches
       // Gate switches
       int gcntr = 0;
       float[] tempSwitchG = new float[10];
       Body tempBody;
       Vector2 posSwitch;
       while (true) {
    	   try {
    		   tempBody = mScene.getNamed(Body.class, "SwitchGate"+gcntr).first();
    	   } catch (NullPointerException e) {
    		   break;
    	   }
    	   posSwitch = (Vector2) mScene.getCustom(tempBody, "gatePos", null);
    	   tempSwitchG[0] = posSwitch.x;
    	   tempSwitchG[1] = posSwitch.y;
    	   tempSwitchG[2] = (Float) mScene.getCustom(tempBody, "gateLength", 0.0f);
    	   tempSwitchG[3] = MathUtils.radiansToDegrees * (Float) mScene.getCustom(tempBody, "gateAngle", 0.0f);
    	   posSwitch = (Vector2) mScene.getCustom(tempBody, "switchPos", null);
    	   tempSwitchG[4] = posSwitch.x;
    	   tempSwitchG[5] = posSwitch.y;
    	   tempSwitchG[6] = MathUtils.radiansToDegrees * (Float) mScene.getCustom(tempBody, "switchAngle", 0.0f);
    	   tempSwitchG[7] = (Float) mScene.getCustom(tempBody, "switchLR", 1.0f);
    	   tempSwitchG[8] = (Float) mScene.getCustom(tempBody, "switchOC", 0.0f);
    	   tempSwitchG[9] = 1.0f - tempSwitchG[8]; // -1=destroy gate, 0=do nothing, 1=create gate
    	   switchGate.add(tempSwitchG.clone());
    	   gcntr +=1;
       }
       BodyDef bdef = new BodyDef();
       bdef.type = BodyType.StaticBody;
       bdef.position.set(0, 0);
       bdef.fixedRotation = true;
       switchGateBody = mWorld.createBody(bdef);
       switchGateBody.setUserData(new Array<Integer>());

       // Find all kinematic bodies
       Array<Body> bodies = new Array<Body>();
       mWorld.getBodies(bodies);
       RubeVertexArray vertices;
       for (int i=0; i<bodies.size; i++) {
    	   if (bodies.get(i).getType().equals(BodyType.KinematicBody)) {
    		   // Only consider bodies where the path is set
    		   vertices = (RubeVertexArray) mScene.getCustom(bodies.get(i), "path", null);
    		   if (vertices != null) {
        		   kinematicBodies.add(bodies.get(i));
    			   kinematicPath.add(vertices.toVector2().clone());
    		   }
    	   }
       }
       if (kinematicBodies.size != 0) {
    	   kinematicDirection = new int[kinematicBodies.size];
    	   kinematicIndex = new int[kinematicBodies.size];
    	   kinematicLength = new float[kinematicBodies.size];
    	   kinematicLengthLeft = new float[kinematicBodies.size];
    	   kinematicSpeed = new float[kinematicBodies.size];
    	   float leftover;
    	   for (int i=0; i<kinematicBodies.size; i++) {
    		   kinematicDirection[i] = (Integer) mScene.getCustom(kinematicBodies.get(i), "direction", 1);
    		   kinematicIndex[i]  = (Integer) mScene.getCustom(kinematicBodies.get(i), "index", 0);
    		   kinematicLength[i] = (Float) mScene.getCustom(kinematicBodies.get(i), "pathlength", -1.0f);
    		   kinematicSpeed[i]  = (Float) mScene.getCustom(kinematicBodies.get(i), "speed", -1.0f);
    		   //if (kinematicSpeed[i] > kinematicLength[i]/4.0f) kinematicSpeed[i] = kinematicLength[i]/4.0f; 
    		   leftover = 0.0f;
    		   if (kinematicDirection[i] == 1) {
    		        if (kinematicIndex[i]==kinematicPath.get(i).length-1) {
    		        	// We cannot move in this direction if we are at the first index
     				   	kinematicDirection[i] *= -1;
     				   	leftover = kinematicLength[i];    		        	
    		        } else {
    		        	for (int j=kinematicIndex[i]; j<kinematicPath.get(i).length-1; j++) leftover += (kinematicPath.get(i)[j].cpy().sub(kinematicPath.get(i)[j+1].cpy())).len();
    		        }
    		   } else {
    			   if (kinematicIndex[i]==0) {
    				   // We cannot move in this direction if we are at the first index
    				   kinematicDirection[i] *= -1;
    				   leftover = kinematicLength[i];
    			   } else {
    				   for (int j=0; j<kinematicIndex[i]; j++) leftover += (kinematicPath.get(i)[j].cpy().sub(kinematicPath.get(i)[j+1].cpy())).len();
    			   }
    		   }
    		   kinematicLengthLeft[i] = leftover;
    	   }
       }
       bodies.clear();
       //mScene.clear(); // no longer need any scene references
    }
    
    @Override
    public void dispose() {
    	//ReplayVars.SaveReplay("replay.rpl");
    	GameInputProcessor.Disable(false);
    	if (mBatch != null) mBatch.dispose();
    	if (mPolyBatch != null) mPolyBatch.dispose();
    	if (b2dr != null) b2dr.dispose();
    	if (mWorld != null) mWorld.dispose();
    	if (mAssetManager != null) mAssetManager.dispose();
    	if (timer != null) timer.dispose();
    	if (timerWR != null) timerWR.dispose();
    	if (timerPB != null) timerPB.dispose();
    	if (keyRedCntr != null) keyRedCntr.dispose();
    	if (keyGreenCntr != null) keyGreenCntr.dispose();
    	if (keyBlueCntr != null) keyBlueCntr.dispose();
    	if (jewelCntr != null) jewelCntr.dispose();
    	if (texture != null) texture.dispose();
    	if (mTextureRegionMap != null) mTextureRegionMap.clear();
    	if (mTextureMap != null) mTextureMap.clear();
    	// Clear all of the arrays
    	if (mSpatials != null) mSpatials.clear();
    	if (mDecors != null) mDecors.clear();
    	if (mPolySpatials != null) mPolySpatials.clear();
    	if (switchGate != null) switchGate.clear();
    	if (doorImages != null) doorImages.clear();
    	if (transportImages != null) transportImages.clear();
    	if (remBodies != null) remBodies.clear();
    	if (remBodiesIdx != null) remBodiesIdx.clear();
    	if (kinematicBodies != null) kinematicBodies.clear();
    	if (kinematicPath != null) kinematicPath.clear();
    	if (fallingJoints != null) fallingJoints.clear();
    	if (fallingJointsTime != null) fallingJointsTime.clear();
    	if (mScene != null) mScene.clear();
    	mScene = null;
    }

    private void renderWorld() {
    	// Render the background Sky
		mBatch.setProjectionMatrix(hudCam.combined);
    	mBatch.begin();
    	mBatch.draw(sky, hudCam.position.x-SCRWIDTH/2, hudCam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH, BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);
    	if (paintBackdrop) {
    		mBatch.draw(mountains, hudCam.position.x-SCRWIDTH*(1+2*bikeBodyC.getPosition().x/1000.0f)/2, hudCam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH*2, SCRWIDTH/4, 1.0f, 1.0f, 0.0f);
    		mBatch.draw(trees, hudCam.position.x-SCRWIDTH*(1+2*(2.4219f-1.0f)*bikeBodyC.getPosition().x/1000.0f)/2, hudCam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH*2.4219f, SCRWIDTH/5, 1.0f, 1.0f, 0.0f);
    	}
    	mBatch.end();

    	if ((mSpatials != null) && (mSpatials.size > 0))
    	{
    		mBatch.setProjectionMatrix(b2dCam.combined);
    		mBatch.begin();
    		for (int i = 0; i < mSpatials.size; i++)
    		{
    			mSpatials.get(i).render(mBatch, 0);
    		}
    		mBatch.end();
    	}

    	if ((mDecors != null) && (mDecors.size > 0))
    	{
    		mBatch.begin();
    		for (int i = 0; i < mDecors.size; i++)
    		{
    			mDecors.get(i).render(mBatch);
    		}
    		mBatch.end();
    	}

    	// Render the open (or opening) doors
    	mBatch.begin();
    	float[] gateArr;
    	float xcen, ycen;
    	for (int i = 0; i < doorImages.size; i++) {
    		gateArr = doorImages.get(i);
    		if (gateArr[2] < 0.0f) {
    			xcen = gateArr[0] + gateArr[2]*((float)Math.cos(gateArr[4])-1)/2 + ObjectVars.objectDoor[4]*B2DVars.EPPM*(float)Math.cos(gateArr[4]);
    			ycen = gateArr[1] + gateArr[2]*(float)Math.sin(gateArr[4])/2 - ObjectVars.objectDoor[5]*B2DVars.EPPM + ObjectVars.objectDoor[4]*B2DVars.EPPM*(float)Math.sin(gateArr[4]);
    			mBatch.draw(openDoor, xcen, ycen, gateArr[2]/2, gateArr[3]/2, gateArr[2], gateArr[3], 1, 1, MathUtils.radiansToDegrees*gateArr[4]);
    			if (-gateArr[2] < gateArr[3]/2) {
    				gateArr[2] -= 0.01f;
    				doorImages.set(i, gateArr.clone());
    			}
    		} else {
    			xcen = gateArr[0] + gateArr[2]*((float)Math.cos(gateArr[4])-1)/2 - ObjectVars.objectDoor[4]*B2DVars.EPPM*(float)Math.cos(gateArr[4]);
    			ycen = gateArr[1] + gateArr[2]*(float)Math.sin(gateArr[4])/2 - ObjectVars.objectDoor[5]*B2DVars.EPPM - ObjectVars.objectDoor[4]*B2DVars.EPPM*(float)Math.sin(gateArr[4]);
    			mBatch.draw(openDoor, xcen, ycen, gateArr[2]/2, gateArr[3]/2, gateArr[2], gateArr[3], 1, 1, MathUtils.radiansToDegrees*gateArr[4]);
    			if (gateArr[2] < gateArr[3]/2) {
    				gateArr[2] += 0.01f;
    				doorImages.set(i, gateArr.clone());
    			}
    		}
    	}
    	mBatch.end();

       // Render the overlay on every transporter
//       mBatch.begin();
//       transportOverlay.setU(transportScrollTimer);
//       transportOverlay.setU2(transportScrollTimer+1);
//       float[] transArr;
//       for (int i = 0; i < transportImages.size; i++) {
//    	   transArr = transportImages.get(i);
//    	   mBatch.draw(transportOverlay, transArr[0], transArr[1], transArr[2]/2, transArr[3]/2, transArr[2], transArr[3], transArr[4], transArr[4], transArr[5]);
//       }
//       transportScrollTimer += Gdx.graphics.getDeltaTime()/5;
//       if (transportScrollTimer > 1.0f) transportScrollTimer = 0.0f;
//       mBatch.end();
       
       // Render the Switches
       mBatch.begin();
       float[] switchArr;
       // Gate Switches
       for (int i = 0; i < switchGate.size; i++) {
    	   switchArr = switchGate.get(i);
	  	   // Draw the switch
	  	   float swLR;   
	  	   if (switchArr[7] == -1.0f) swLR = -0.3f;
	  	   else swLR = 0.3f;
		   xcen = switchArr[4] + 0.0281f*(float)Math.cos(MathUtils.degreesToRadians*(switchArr[6]+90.0f));
		   ycen = switchArr[5] + 0.0281f*(float)Math.sin(MathUtils.degreesToRadians*(switchArr[6]+90.0f));
	  	   if (switchArr[8] == 0.0f) {
	  		   mBatch.draw(switchRL, xcen-swLR/2, ycen-0.15f, swLR/2, 0.15f, swLR, 0.3f, 1, 1, switchArr[6]);
	  		   mBatch.draw(metalBar, switchArr[0]-0.0332f, switchArr[1]-switchArr[2]/2, 0.0332f, switchArr[2]/2, 0.0664f, switchArr[2], 1, 1, switchArr[3]);
	  	   } else {
	  		   mBatch.draw(switchGL, xcen-swLR/2, ycen-0.15f, swLR/2, 0.15f, swLR, 0.3f, 1, 1, switchArr[6]);
	  	   }
       }
       // Render other switches...
       mBatch.end();

       // Render the oscillating finish ball
       if (finishRad > 0.0f) {
    	   mBatch.begin();
    	   mBatch.draw(finishFG, finishPosition.x-finishRad, finishPosition.y-finishRad, finishRad, finishRad, 2.0f*finishRad, 2.0f*finishRad, 1.0f, 1.0f, finAngle);
    	   finAngle += 5.0f;
    	   if (finAngle >= 360.0f) finAngle -= 360.0f;
    	   if (finishRad > ObjectVars.objectFinishBall[2]*B2DVars.EPPM) finishRad = ObjectVars.objectFinishBall[2]*B2DVars.EPPM;
    	   else finishRad += 0.01f;
    	   mBatch.end();
       }

       // Render the colour of the bike
       mBatch.begin();
       float bcx, bcy, angle;
       float bscale = (float)Math.sin(bikeScale*Math.PI/2);
       //if ((bscale == -1.0f) | (bscale == 1.0f)) {
       if (true) {
           float wcx, wcy;
           // Render the rear suspension
    	   if (bikeDirc == 1.0f) {
    		   wcx = bikeBodyLW.getPosition().x;
    		   wcy = bikeBodyLW.getPosition().y;
    	       if (bscale > 0.0f) mBatch.setColor(1, 1, 1, bscale);
    	       else mBatch.setColor(1, 1, 1, 0);
    	   } else {
    		   wcx = bikeBodyRW.getPosition().x;
    		   wcy = bikeBodyRW.getPosition().y;    		   
    	       if (bscale < 0.0f) mBatch.setColor(1, 1, 1, -bscale);
    	       else mBatch.setColor(1, 1, 1, 0);
    	   }
           bcx = bikeBodyC.getPosition().x;
           bcy = bikeBodyC.getPosition().y;
           float[] cCoord = PolygonOperations.RotateCoordinate(-0.143796f*bikeDirc,-0.218244f,MathUtils.radiansToDegrees*bikeBodyC.getAngle(),0.0f,0.0f);
           bcx += cCoord[0];
           bcy += cCoord[1];
           float height = 1.0955f*0.054051f;
           float width = 1.0955f*(float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
           // Calculate the angle
           angle = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
           cCoord = PolygonOperations.RotateCoordinate(-0.3f*height,-0.5f*height,MathUtils.radiansToDegrees*angle,0.0f,0.0f);
           mBatch.draw(suspensionRear, wcx+cCoord[0], wcy+cCoord[1], 0.0f, 0.0f, width, height, 1.0f, 1.0f, MathUtils.radiansToDegrees*angle);
           // Render the front suspension
    	   if (bikeDirc == 1.0f) {
    		   wcx = bikeBodyRW.getPosition().x;
    		   wcy = bikeBodyRW.getPosition().y;
    	   } else {
    		   wcx = bikeBodyLW.getPosition().x;
    		   wcy = bikeBodyLW.getPosition().y;    		   
    	   }
           cCoord = PolygonOperations.RotateCoordinate(0.2192f*bikeDirc,0.2614f,MathUtils.radiansToDegrees*bikeBodyC.getAngle(),0.0f,0.0f);
           bcx = bikeBodyC.getPosition().x + cCoord[0];
           bcy = bikeBodyC.getPosition().y + cCoord[1];
           height = 0.06714876f;
           width = (512.0f/497.0f) * (float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
           // Calculate the angle
           angle = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
           mBatch.draw(suspensionFront, wcx-width*15.0f/512.0f, wcy-bikeDirc*height*15.0f/64.0f, width*15.0f/512.0f, bikeDirc*height*15.0f/64.0f, width, bikeDirc*height, 1.0f, 1.0f, MathUtils.radiansToDegrees*angle);    	   
       }
       bcx = bikeBodyC.getPosition().x;
       bcy = bikeBodyC.getPosition().y;
       angle = bikeBodyC.getAngle();
       // Change the colour of the bike
       //mBatch.setColor(1, 0, 1, 1); // Magenta
       mBatch.setColor(0.1f, 0.5f, 1, 1); // Blue
       mBatch.draw(bikeColour, bcx-bscale*0.72f, bcy-0.3f, bscale*0.72f, 0.3f, bscale*1.44f, 1.125f, 1.0f, 1.0f, MathUtils.radiansToDegrees*angle);
       mBatch.setColor(1, 1, 1, 1);
       bcx = bikeBodyC.getPosition().x;
       bcy = bikeBodyC.getPosition().y;
       angle = bikeBodyC.getAngle();
       // Render the bike overlay and player
       mBatch.draw(bikeOverlay, bcx-bscale*0.72f, bcy-0.3f, bscale*0.72f, 0.3f, bscale*1.44f, 1.125f, 1.0f, 1.0f, MathUtils.radiansToDegrees*angle);
       mBatch.end();

       // Render the ground
       if ((mPolySpatials != null) && (mPolySpatials.size > 0))
       {
          mPolyBatch.setProjectionMatrix(b2dCam.combined);
          mPolyBatch.begin();
          for (int i = 0; i < mPolySpatials.size; i++)
          {
             mPolySpatials.get(i).render(mPolyBatch, 0);
          }
          mPolyBatch.end();
       }

       // Render some items onto the HUD
       renderHUD();

       if (debug) b2dr.render(mWorld, b2dCam.combined);
    }

    private void renderHUD()
    {
        mBatch.setProjectionMatrix(hudCam.combined);
        float pThick = 30.0f;
        mBatch.begin();
        // Draw the shading on the top bar
        //mBatch.draw(panelShadeA, 0, BikeGame.V_HEIGHT-pThick, 0, 0, 1, 1, BikeGame.V_WIDTH, pThick, 0);
        //mBatch.draw(panelShadeB, 0, BikeGame.V_HEIGHT-pThick-10, 0, 0, 1, 10, BikeGame.V_WIDTH, 1, 0);
    	//mBatch.draw(panelShadeC, BikeGame.V_WIDTH - timerWidth - 10, BikeGame.V_HEIGHT-30, 0, 0, 7, 30, 1, 1, 0);
    	//panelContainer.draw(mBatch, BikeGame.V_WIDTH - timerWidth - jcntrWidth - 128.0f*0.2f - 30.0f, BikeGame.V_HEIGHT-29, 128.0f*0.2f+jcntrWidth+10.0f, 28);
        float vshift = 0.0f;
        // Draw the timer
        timerCurrent = (int) (TimeUtils.millis()) - timerStart;
        String timeStr = GameVars.getTimeString(timerCurrent);
    	timer.draw(mBatch, timeStr, SCRWIDTH-timerWidth-10.0f, BikeGame.V_HEIGHT-(pThick-timerHeight)/2.0f);
    	vshift += timerHeight +5;
    	// WR
    	timerWR.draw(mBatch, "WR  " + worldRecord, SCRWIDTH-timerWRWidth-10.0f, BikeGame.V_HEIGHT-vshift-(pThick-timerWRHeight)/2.0f);
    	vshift += timerWRHeight + 5;
    	// PB
    	timerPB.draw(mBatch, "PB  " + personalRecord, SCRWIDTH-timerWRWidth-10.0f, BikeGame.V_HEIGHT-vshift-(pThick-timerWRHeight)/2.0f);
    	vshift += timerWRHeight + 8;
        // Draw the jewel and it's counter
        jewelCntr.draw(mBatch, String.format("%02d", collectJewel),SCRWIDTH - jcntrWidth - 10.0f,BikeGame.V_HEIGHT-vshift-(pThick-jcntrHeight)/2.0f);
        mBatch.draw(jewelSprite, SCRWIDTH - jcntrWidth - 128.0f*0.2f - 20.0f, BikeGame.V_HEIGHT-vshift-(pThick-128.0f*0.2f)/2.0f-128.0f*0.2f, 0, 0, 128.0f, 128.0f, 0.2f, 0.2f, 0);
        vshift += pThick;
    	// Draw the key counters
        keyRedCntr.draw(mBatch, String.format("%02d", collectKeyRed),     SCRWIDTH - jcntrWidth - 10.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f);
        keyGreenCntr.draw(mBatch, String.format("%02d", collectKeyGreen), SCRWIDTH - jcntrWidth - 10.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f-15.0f);
        keyBlueCntr.draw(mBatch, String.format("%02d", collectKeyBlue),   SCRWIDTH - jcntrWidth - 10.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f-30.0f);
        vshift += 10;
        // Draw the keys
        mBatch.draw(keyRed,   SCRWIDTH - jcntrWidth - 135.0f*0.2f - 20.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f, 50.0f, 0.2f, 0.2f, 0);
        mBatch.draw(keyGreen, SCRWIDTH - jcntrWidth - 135.0f*0.2f - 20.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f-15.0f, 0, 0, 135.0f, 50.0f, 0.2f, 0.2f, 0);
        mBatch.draw(keyBlue,  SCRWIDTH - jcntrWidth - 135.0f*0.2f - 20.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f-30.0f, 0, 0, 135.0f, 50.0f, 0.2f, 0.2f, 0);

    	// Draw the nitrous counter
        vshift += 40;
        nitrousCntr.draw(mBatch, String.format("%02d", collectNitrous), SCRWIDTH - jcntrWidth - 10.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f);
        vshift += 67.5f*0.2f;
        // Draw the nitrous image
        mBatch.draw(nitrous,  SCRWIDTH - jcntrWidth - 135.0f*0.2f - 20.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f, 67.5f, 0.2f, 0.2f, 0);
        // Draw nitrous tube and fluid
        vshift += 10;
        mBatch.draw(nitrousFluid,  SCRWIDTH - jcntrWidth - 135.0f*0.2f - 20.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, nitrousLevel*135.0f, 50.0f, 0.2f, 0.2f, 0);       
        mBatch.draw(nitrousTube,  SCRWIDTH - jcntrWidth - 135.0f*0.2f - 20.0f, BikeGame.V_HEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f, 50.0f, 0.2f, 0.2f, 0);       
        mBatch.end();

    }
    /**
     * Creates an array of SimpleSpatial objects from RubeImages.
     * 
     * @param scene2
     */
    private void createSpatialsFromRubeImages(RubeScene scene)
    {
    	// Render the images
    	Array<RubeImage> images = scene.getImages();
    	float[] transArr = new float[6];
    	if ((images != null) && (images.size > 0))
    	{
    		mSpatials = new Array<SimpleSpatial>();
    		transportImages = new Array<float[]>();
    		doorImages = new Array<float[]>();
    		remBodies = new Array<Body>();
    		remBodiesIdx = new Array<Integer>();
    		SimpleSpatial spatial;
    		for (int i = 0; i < images.size; i++)
    		{
    			RubeImage image = images.get(i);
    			mTmp.set(image.width, image.height);
    			String textureFileName = "data/" + image.file;
    			//texture = mTextureMap.get(textureFileName);
//    			if (texture == null)
//    			{
				mTextureMap.put(textureFileName, BikeGameTextures.LoadTexture(FilenameUtils.getBaseName(textureFileName),0));
//    			} else {
//    				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//    			}
    			spatial = new SimpleSpatial(BikeGameTextures.LoadTexture(FilenameUtils.getBaseName(textureFileName),0), image.flip, image.body, image.color, mTmp, image.center, image.angleInRads * MathUtils.radiansToDegrees);
//             	if (image.name.startsWith("Jewel")) {
//          	  	 spatial = new SimpleSpatial(jewelSprites[0], image.flip, image.body, image.color, mTmp, image.center,
//         	   			 image.angleInRads * MathUtils.radiansToDegrees);
//         	    } else {
//         	   		spatial = new SimpleSpatial(texture, image.flip, image.body, image.color, mTmp, image.center,
//         	   			 image.angleInRads * MathUtils.radiansToDegrees);
//             	}
    			mSpatials.add(spatial);
    			if ((image.name.startsWith("Padlock"))|(image.name.startsWith("Key"))|(image.name.startsWith("Nitrous"))|(image.name.startsWith("Jewel"))|(image.name.startsWith("Diamond"))|(image.name.startsWith("Gravity"))) {
    				remBodies.add(image.body);
    				remBodiesIdx.add(i);
    			} else if (image.name.startsWith("Transport")) {
    				transArr[0] = image.body.getPosition().x-image.width/2;
    				transArr[1] = image.body.getPosition().y-image.height/2;
    				transArr[2] = image.width;
    				transArr[3] = image.height;
    				transArr[4] = image.scale;
    				transArr[5] = MathUtils.radiansToDegrees*image.angleInRads;
    				transportImages.add(transArr.clone());
    			}
    		}
    	}
    	// Render the decorations
    	Array<RubeDecor> decors = scene.getDecors();
    	if ((decors != null) && (decors.size > 0))
    	{
    		if (mSpatials==null) mSpatials = new Array<SimpleSpatial>();
    		SimpleSpatial spatial;
    		for (int i = 0; i < decors.size; i++)
    		{
    			RubeDecor decor = decors.get(i);
    			mTmp.set(decor.width, decor.height);
    			String textureFileName = "data/" + decor.file;
				mTextureMap.put(textureFileName, BikeGameTextures.LoadTexture(FilenameUtils.getBaseName(textureFileName),2));
//    			texture = mTextureMap.get(textureFileName);
//    			if (texture == null)
//    			{
//    				texture = new Texture(textureFileName);
//    				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//    				mTextureMap.put(textureFileName, texture);
//    			} else {
//    				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//    			}
    			spatial = new SimpleSpatial(BikeGameTextures.LoadTexture(FilenameUtils.getBaseName(textureFileName),2), decor.flip, null, decor.color, mTmp, decor.center, decor.angleInRads * MathUtils.radiansToDegrees);
    			mSpatials.add(spatial);
    		}
    	}
    }

    /**
     * Creates an array of PolySpatials based on fixture information from the scene. Note that
     * fixtures create aligned textures.
     * 
     * @param scene
     */
    private void createPolySpatialsFromRubeFixtures(RubeScene scene)
    {
       Array<Body> bodies = scene.getBodies();
       
       EarClippingTriangulator ect = new EarClippingTriangulator();

       if ((bodies != null) && (bodies.size > 0))
       {
          mPolySpatials = new Array<PolySpatial>();
          Vector2 bodyPos = new Vector2();
          // for each body in the scene...
          for (int i = 0; i < bodies.size; i++)
          {
             Body body = bodies.get(i);
             bodyPos.set(body.getPosition());
             
             float bodyAngle = body.getAngle()*MathUtils.radiansToDegrees;

             Array<Fixture> fixtures = body.getFixtureList();

             if ((fixtures != null) && (fixtures.size > 0))
             {
                // for each fixture on the body...
                for (int j = 0; j < fixtures.size; j++)
                {
                   Fixture fixture = fixtures.get(j);

                   String textureName = (String)scene.getCustom(fixture, "TextureMask", null);
                   if (textureName != null)
                   {
                      String textureFileName = "data/" + textureName;
                      texture = mTextureMap.get(textureFileName);
                      TextureRegion textureRegion = null;
                      if (texture == null) {
                    	  texture = new Texture(textureFileName);
                    	  texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                    	  texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                    	  mTextureMap.put(textureFileName, texture);
                    	  textureRegion = new TextureRegion(texture);
                    	  mTextureRegionMap.put(texture, textureRegion);
                      } else {
                          texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                          textureRegion = mTextureRegionMap.get(texture);
                      }

                      // only handle polygons at this point -- no chain, edge, or circle fixtures.
                      if (fixture.getType() == Shape.Type.Polygon)
                      {
                         PolygonShape shape = (PolygonShape) fixture.getShape();
                         int vertexCount = shape.getVertexCount();
                         float[] vertices = new float[vertexCount * 2];

                         // static bodies are texture aligned and do not get drawn based off of the related body.
                         if (body.getType() == BodyType.StaticBody)
                         {
                            for (int k = 0; k < vertexCount; k++)
                            {

                               shape.getVertex(k, mTmp);
                               mTmp.rotate(bodyAngle);
                               mTmp.add(bodyPos); // convert local coordinates to world coordinates to that textures are
                                                  // aligned
                               vertices[k * 2] = mTmp.x/PolySpatial.PIXELS_PER_METER;
                               vertices[k * 2 + 1] = mTmp.y/PolySpatial.PIXELS_PER_METER;
                            }
                            
                            short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                            PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                            PolySpatial spatial = new PolySpatial(region, Color.WHITE);
                            mPolySpatials.add(spatial);
                         }
                         else
                         {
                            // all other fixtures are aligned based on their associated body.
                            for (int k = 0; k < vertexCount; k++)
                            {
                               shape.getVertex(k, mTmp);
                               vertices[k * 2] = mTmp.x/PolySpatial.PIXELS_PER_METER;
                               vertices[k * 2 + 1] = mTmp.y/PolySpatial.PIXELS_PER_METER;
                            }
                            short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                            PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                            PolySpatial spatial = new PolySpatial(region, body, Color.WHITE);
                            mPolySpatials.add(spatial);
                         }
                      }
                      else if (fixture.getType() == Shape.Type.Circle)
                      {
                         CircleShape shape = (CircleShape)fixture.getShape();
                         float radius = shape.getRadius();
                         int vertexCount = (int)(12f * radius);
                         float [] vertices = new float[vertexCount*2];
                         if (body.getType() == BodyType.StaticBody)
                         {
                            mTmp3.set(shape.getPosition());
                            for (int k = 0; k < vertexCount; k++)
                            {
                               // set the initial position
                               mTmp.set(radius,0);
                               // rotate it by 1/vertexCount * k
                               mTmp.rotate(360f*k/vertexCount);
                               // add it to the position.
                               mTmp.add(mTmp3);
                               mTmp.rotate(bodyAngle);
                               mTmp.add(bodyPos); // convert local coordinates to world coordinates so that textures are aligned
                               vertices[k*2] = mTmp.x/PolySpatial.PIXELS_PER_METER;
                               vertices[k*2+1] = mTmp.y/PolySpatial.PIXELS_PER_METER;
                            }
                            short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                            PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                            PolySpatial spatial = new PolySpatial(region, Color.WHITE);
                            mPolySpatials.add(spatial);
                         }
                         else
                         {
                            mTmp3.set(shape.getPosition());
                            for (int k = 0; k < vertexCount; k++)
                            {
                               // set the initial position
                               mTmp.set(radius,0);
                               // rotate it by 1/vertexCount * k
                               mTmp.rotate(360f*k/vertexCount);
                               // add it to the position.
                               mTmp.add(mTmp3);
                               vertices[k*2] = mTmp.x/PolySpatial.PIXELS_PER_METER;
                               vertices[k*2+1] = mTmp.y/PolySpatial.PIXELS_PER_METER;
                            }
                            short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                            PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                            PolySpatial spatial = new PolySpatial(region, body, Color.WHITE);
                            mPolySpatials.add(spatial);
                         }
                      }
                   }
                }
             }
          }
       }
    }

}
