/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Images by Craziwolf

package com.mygdx.game.states;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.*;

import static com.mygdx.game.handlers.B2DVars.PPM;

import com.mygdx.game.utilities.ColorUtils;
import com.mygdx.game.utilities.FileUtils;
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
    private boolean isReplay;
    private float replayTime;
    private int mVelocityIter = 8;
    private int mPositionIter = 3;

    private Array<SimpleSpatial> mSpatials; // used for rendering rube images
    private Array<SimpleImage> mDecors; // used for rendering decorations
    private Array<PolySpatial> mPolySpatials;
    private Array<PolySpatial> mAnimatedBG;
    private Array<PolySpatial> mCollisionlessFG;
    private Array<PolySpatial> mCollisionlessBG;
    private Array<PolySpatial> waterfallBackground;
    private Array<PolySpatial> rainBackground;
    private Map<String, Texture> mTextureMap;
    private Map<Texture, TextureRegion> mTextureRegionMap;
    private Body gameInfo;
    private Array<float[]> switchGate;
    private Array<Body> remBodies;
    private Array<Fixture> triggerFixtList;
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
    private Body bikeBodyR;
    private Body bikeBodyC;
    private Body switchGateBody, triggerBody;
    private Body waterfallBody;
    private float waterfallPos;
    private Body rainBody;
    private float rainPos;
    private Body animatedBGBody;
    private float animatedBGPos, animatedBGSpeed;
    private float bikeDirc = 1.0f;
	private float dircGrav;
    private float bikeScale = 1.0f;
    private float bikeScaleLev = 0.05f;
    private float gravityScale = -1.0f;
    private Vector2 bounds = null; 
    private Vector2 gravityPrev = new Vector2(), gravityNew = new Vector2(), gravityOld = new Vector2();
    private float motorTorque = 0.0f;
    private float playerTorque = 0.0f;
    private float applyTorque = -1.0f;
    private int applyNitrous = 0;
//    private int applyRocket = 0;
    private float playerJump = 0.0f;
    private float applyJump = -1.0f;
    private float canTransport = -1.0f;
    private float spinTime = 0.6f; // Time (in s) before spin can recommence
    private float jumpTime = 0.6f; // Time (in s) before jump can recommence
    private float transportTime = 2.5f; // Time (in s) before transporters are activated
    private float fallTime = 5.0f; // Time (in s) before a falling platform will fall after being touched
    private float nitrousLevel = 0.0f; // Current level of nitrous
    private float rocketLevel = 0.0f; // Current level of rocket
    private float soundTimeGem = 0.0f, soundTimeKey=0.0f, soundTimeNitrous=0.0f, soundTimeDoor=0.0f, soundTimeGravity=0.0f;  // Time between sounds
    private float finAngle = 0.0f, finishRad = 0.0f;
    private Vector2 startPosition, finishPosition;
    private float startDirection;
    private float startAngle;
    private Texture texture;
    private Sprite blackScreen, sky, background, foreground, finishFG, openDoor, switchGL, switchRL, metalBar;
    private Sprite bikeWheel, bikeColour, bikeOverlay, suspensionRear, suspensionFront;
    private float[] bikeCol;
    private BitmapFont timer, timerWR, timerPB;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private int timerStart, timerCurrent, timerTotal;
    private String worldRecord, personalRecord;
    private float timerWidth, timerHeight, timerWRWidth, timerWRHeight, jcntrWidth, jcntrHeight, infoWidth;
    private int collectKeyRed=0, collectKeyGreen=0, collectKeyBlue=0, collectNitrous=0, collectRocket=0;
    //private int[] animateJewel;
    private float SCRWIDTH, SCRHEIGHT, HUDScaleFactor;
    private BitmapFont keyRedCntr, keyGreenCntr, keyBlueCntr, jewelCntr, nitrousCntr;
    private BitmapFont infoText;
    private int collectJewel;
    private boolean collectDiamond;
    private int nullvar;
    private boolean forcequit, forceRestart, lrIsDown, paintBackdrop, paintForeground;
    private float backgroundLimit;
    
    // Index of sounds to be played
    private int soundGem, soundBikeSwitch, soundDiamond, soundCollide, soundHit, soundNitrous, soundKey, soundGravity, soundDoor, soundSwitch, soundTransport, soundFinish;
    private Sound soundBikeIdle, soundBikeMove;
    private Music soundWaterfall, soundRain, soundWind;
    private float[] musicVolumes = new float[DecorVars.platformSounds.length-1];
    private boolean containsAnimatedBG, containsWaterfall, containsRain, containsWind;
    private Array<float[]> waterfallVerts, rainVerts, animBGVerts;
    private Array<Integer> waterfallSounds, rainSounds;
    private long soundIDBikeIdle, soundIDBikeMove;
    private final float bikeMaxVolume = 0.1f;
    private float bikeVolume, bikePitch;

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
    	         "data/bikeright_withfullbody.json"
    	         // Here are some alternative vertices for the rider body
//                 "x" : [  0.100,  0.242, 0.108, -0.077, -0.167 ],
//                 "y" : [ -0.416, -0.354, 0.088,  0.304,  0.141 ]
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
    	// Mode values:
    	// 0 = Editor play
    	// 1 = Play Custom
    	// 2 = Play Game
    	// 3 = Replay Custom
    	// 4 = Replay Game
        super(gsm);
        editorString = editorScene;
        levelID = levID;
        mode = modeValue;
        if ((mode==3) | (mode==4)) {
            isReplay = true;
        }
    	// Create the Play instance
    	create();
    }
    
    public void create() {
    	
    	forcequit = false;
    	forceRestart = false;
        // Set the contact listener
        cl = new GameContactListener();

        // Restart the timerTotal
        if (!isReplay) {
	        GameVars.SetTimerTotal(-1);
	        GameVars.SetPersonalBest(false);
	        GameVars.SetWorldRecord(false);
        }
        
        // Set up box2d camera
        this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SCRWIDTH = BikeGame.viewport.width;
        SCRHEIGHT = BikeGame.viewport.height;
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, SCRWIDTH/PPM, SCRHEIGHT/PPM);
        b2dCam.position.set(0, 0, 0);
        b2dCam.zoom = B2DVars.SCRWIDTH/(SCRWIDTH/PPM);
        b2dCam.update();

        //hudCam.setToOrtho(false);

        hudCam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
        hudCam.position.set(SCRWIDTH/2,SCRHEIGHT/2,0);
        //hudCam.position.set(Gdx.graphics.getWidth()/2,SCRHEIGHT/2,0);
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
        	worldRecord = GameVars.getTimeString(-1);
            personalRecord = GameVars.getTimeString(-1);
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
        bikeCol = GameVars.GetPlayerBikeColor();
        bikeColour = new Sprite(BikeGameTextures.LoadTexture("bike_white",0));
        bikeOverlay = new Sprite(BikeGameTextures.LoadTexture("bike_overlay",0));
        suspensionRear = new Sprite(BikeGameTextures.LoadTexture("rear_suspension",0));
        suspensionFront = new Sprite(BikeGameTextures.LoadTexture("front_suspension",0));
        bikeWheel = new Sprite(BikeGameTextures.LoadTexture("bikewheel",0));

        // Load the finish ball textures
        finishFG = new Sprite(BikeGameTextures.LoadTexture("finish_whirl",0));

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

        // Load the sounds
        soundGem = BikeGameSounds.GetSoundIndex("gem_collect");
        soundBikeSwitch = BikeGameSounds.GetSoundIndex("bike_switch");
        soundDiamond = BikeGameSounds.GetSoundIndex("diamond_collect");
        soundCollide = BikeGameSounds.GetSoundIndex("collide");
        soundHit = BikeGameSounds.GetSoundIndex("bike_crash");
        soundNitrous = BikeGameSounds.GetSoundIndex("nitrous");
        soundKey = BikeGameSounds.GetSoundIndex("key_collect");
        soundGravity = BikeGameSounds.GetSoundIndex("gravity");
        soundDoor = BikeGameSounds.GetSoundIndex("door");
        soundSwitch = BikeGameSounds.GetSoundIndex("switch");
        soundTransport = BikeGameSounds.GetSoundIndex("transport");
        soundFinish = BikeGameSounds.GetSoundIndex("finish");
        containsAnimatedBG = false;
        containsWaterfall = false;
        containsRain = false;
        resetMusicVolumes();

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
        timer.getData().setScale(0.35f);
        glyphLayout.setText(timer, "00:00:000");
        timerWidth = glyphLayout.width;
        timerHeight = glyphLayout.height;
        HUDScaleFactor = 0.2f*SCRWIDTH/timerWidth;
        timerWidth *= HUDScaleFactor;
        timerHeight *= HUDScaleFactor;
        timer.getData().setScale(0.35f*HUDScaleFactor);
        // World Record Timer
        timerWR = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        timerWR.setColor(0.8f, 0.725f, 0, 1);
        timerWR.getData().setScale(0.2f*HUDScaleFactor);
        // Personal Best Timer
        timerPB = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        timerPB.setColor(0.5f, 0.5f, 0.5f, 1);
        timerPB.getData().setScale(0.2f*HUDScaleFactor);
        glyphLayout.setText(timerWR, "WR  00:00:000");
        timerWRWidth = glyphLayout.width;
        timerWRHeight = glyphLayout.height;
        keyRedCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        keyRedCntr.getData().setScale(0.25f*HUDScaleFactor);
        keyRedCntr.setColor(1, 0, 0, 1);
        keyGreenCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        keyGreenCntr.getData().setScale(0.25f*HUDScaleFactor);
        keyGreenCntr.setColor(0.2f, 1, 0.2f, 1);
        keyBlueCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        keyBlueCntr.getData().setScale(0.25f*HUDScaleFactor);
        keyBlueCntr.setColor(0, 0.7f, 1, 1);
        jewelCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        jewelCntr.getData().setScale(0.25f*HUDScaleFactor);
        //jewelCntr.setColor(0.85f, 0.85f, 0, 1);
        jewelCntr.setColor(0.1f, 0.8f, 0.1f, 1);
        nitrousCntr = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        nitrousCntr.getData().setScale(0.25f*HUDScaleFactor);
        nitrousCntr.setColor(0.2f, 0.2f, 1, 1);
        glyphLayout.setText(jewelCntr, "00");
        jcntrWidth = glyphLayout.width;
        jcntrHeight = glyphLayout.height;
        // Information text at the beginning of the game
        infoText = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        float scaleVal = 1.0f;
        infoText.getData().setScale(scaleVal);
        glyphLayout.setText(infoText, "Press ESC to return to menu");
        infoWidth = glyphLayout.width;
        scaleVal = 0.25f*(SCRWIDTH-0.075f*SCRHEIGHT)/infoWidth;
        infoText.getData().setScale(scaleVal);
        infoWidth = glyphLayout.width;

        // Initiate some arrays
        switchGate = new Array<float[]>();
        kinematicBodies = new Array<Body>();
        kinematicPath = new Array<Vector2[]>();
        fallingJoints = new Array<Body>();
        fallingJointsFallTime = new Array<Float>();
        fallingJointsTime = new Array<Float>();
        waterfallPos = 0.0f;
        waterfallVerts = new Array<float[]>();
        waterfallSounds = new Array<Integer>();
        rainPos = 0.0f;
        rainVerts = new Array<float[]>();
        rainSounds = new Array<Integer>();
        animatedBGPos = 0.0f;
        animatedBGSpeed = 0.0f;
        animBGVerts = new Array<float[]>();

    	playerJump = 100.0f;
    	lrIsDown = false;
    	
    	// Set the replay in motion
    	replayTime = 0.0f;
    }
    
    public void handleInput() {
    	// ESC is pressed
        if (GameInput.isPressed(GameInput.KEY_ESC)) {
        	if (mState == GAME_STATE.LOADED) {
        		// The user exits the level before playing
            	gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
            	gsm.SetPlaying(false);
        	}
        	mNextState = GAME_STATE.RUNNING;  // Ensure that forcequit can be applied
        	forcequit = true;
        } else if (GameInput.isPressed(GameInput.KEY_RESTART)) {
        	mNextState = GAME_STATE.RUNNING;  // Ensure that forceRestart can be applied
        	forceRestart = true;
        }
        // Check for all input if the game is running - make sure replay is not set
        if ((mState.equals(GAME_STATE.RUNNING)) && (!isReplay)) {
            // Accelerate
            if (GameInput.isDown(GameInput.KEY_ACCEL)) motorTorque = 10.0f;
            else motorTorque = 0.0f;
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
            } else {
        		bikeBodyLW.setFixedRotation(false);        	
        		bikeBodyRW.setFixedRotation(false);        	
            }
            // Change Direction
            if (GameInput.isPressed(GameInput.KEY_CHDIR)) {
            	if ((mode != 0) && (!isReplay)) ReplayVars.replayChangeDir.add(replayTime);
            	switchBikeDirection();
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
            } else if (GameInput.isDown(GameInput.KEY_SPINR)) {
            	lrIsDown = true;
            	if ((applyTorque<0.0f) & (applyJump<0.0f)) {
            		if (bikeDirc == 1.0f) playerTorque = -torqueVal;//-1500.0f;
            		else playerTorque = -torqueVal;
            		applyTorque = 0.0f;
            		if (cl.isBikeOnGround()) playerTorque *= 0.95f;
            	}
            } else lrIsDown = false;
            // Tricks
            if ((GameInput.isPressed(GameInput.KEY_BUNNY)) & (cl.isBikeOnGround()) & (applyJump<0.0f) & (applyTorque<0.0f)) {
            	playerJump = 0;
            	applyJump = 0.0f;
            }
            if (GameInput.isDown(GameInput.KEY_NITROUS)) {
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
            	}
            } else if (GameInput.isDown(GameInput.KEY_NITROUS)==false) {
            	applyNitrous = 0;
            }
//            if (GameInput.isDown(GameInput.KEY_ROCKET)) {
//            	applyRocket = 1;
////            	if ((collectRocket > 0) | (rocketLevel > 0.0f)) {
////            		applyRocket = 1;
////            		rocketLevel -= 0.002f;
////            		if ((rocketLevel < 0.0f) & (collectRocket > 1)) {
////            			rocketLevel = 1.0f;
////            			collectRocket -= 1;
////            		} else if (rocketLevel < 0.0f) {
////            			rocketLevel = 0.0f;
////            			collectRocket -= 1;
////            		}
////            	}
//            } else if (GameInput.isDown(GameInput.KEY_ROCKET)==false) {
//            	applyRocket = 0;
//            }
            //if ((applyTorque<0.0f) & (applyJump<0.0f)) bikeAngle = bikeBodyC.getAngle();
        } else if (mState.equals(GAME_STATE.LOADED)) {
        	if ((GameInput.isPressed(GameInput.KEY_ENTER)) | (isReplay)) {
                mPrevState = mState;
                // Reset the replay
            	if (!isReplay) {
            		ReplayVars.Reset(editorString, levelID, mode);
	   				replayTime = 0.0f;
	   				ReplayVars.replayCntr = 0;
	   				ReplayVars.replayCDCntr = 0;
            	}
	            // Start the bike sound loops
	    	    //int bikeStart = BikeGameSounds.GetSoundIndex("bike_start");
	    	    //BikeGameSounds.PlaySound(bikeStart, 1.0f);
	            soundBikeIdle = BikeGameSounds.LoadBikeIdle();
	            soundIDBikeIdle = soundBikeIdle.play(bikeMaxVolume);
	            soundBikeIdle.setLooping(soundIDBikeIdle, true);
	            soundBikeIdle.setPitch(soundIDBikeIdle, 1.0f);
	            // sound of the bike moving
	            soundBikeMove = BikeGameSounds.LoadBikeMove();
	            soundIDBikeMove = soundBikeIdle.play(0.0f);
	            soundBikeMove.setPitch(soundIDBikeMove, 1.0f);
	            soundBikeMove.setLooping(soundIDBikeMove, true);
	            // Load the waterfall music
                soundWaterfall = BikeGameSounds.LoadWaterfall();
                soundWaterfall.setLooping(true);
                soundWaterfall.setVolume(0.0f);
                soundWaterfall.play();
                // Load the rain music
                soundRain = BikeGameSounds.LoadRain();
                soundRain.setLooping(true);
                soundRain.setVolume(0.0f);
                soundRain.play();
                // Load the wind music
                soundWind = BikeGameSounds.LoadWind();
                soundWind.setLooping(true);
                soundWind.setVolume(0.0f);
                soundWind.play();
                // Change the state
                mNextState = GAME_STATE.RUNNING;
	            // Start the timer
	            timerStart = (int) (TimeUtils.millis());
        	} else return;
        } else return;
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
        	   handleInput();
               break;
              
           case RUNNING:
        	   handleInput();
        	   mWorld.step(dt, mVelocityIter, mPositionIter);
	       	   if (cl.isFinished()) {
	     	   		if (collectJewel == 0) {
	     	   			timerTotal = (int) (TimeUtils.millis()) - timerStart;
	     	   			if ((mode == 1) || (mode == 2)) storeReplay(dt);
	     	   			if ((!isReplay) && (mode != 0)) ReplayVars.replayTimer = timerTotal;
	     	   			StopSounds();
	     	   			BikeGameSounds.PlaySound(soundFinish, 1.0f);
	     	   			if (!isReplay) GameVars.SetTimerTotal(timerTotal);
	     	   			// Check the records with a diamond
	     	   			if (collectDiamond) {
	     	   				if (mode == 2) {
	     	   					// Set the Diamond
	     	   					GameVars.SetDiamond(levelID);
	     	   					// Check the time
	     	   					GameVars.CheckTimes(GameVars.plyrTimesDmnd.get(GameVars.currentPlayer).get(levelID).clone(), 1, levelID, timerTotal, false);
	     	   					GameVars.CheckTimes(GameVars.worldTimesDmnd.get(levelID).clone(), 1, levelID, timerTotal, true);
	     	   				}
	     	   			} else {
		     	   			// Check the records without the diamond
	     	   				if (mode == 2) {
	     	   					GameVars.CheckTimes(GameVars.plyrTimes.get(GameVars.currentPlayer).get(levelID).clone(), 0, levelID, timerTotal, false);
	     	   					GameVars.CheckTimes(GameVars.worldTimes.get(levelID).clone(), 0, levelID, timerTotal, true);
	     	   				}	     	   				
	     	   			}
	     	   			//System.out.println(GameVars.getTimeString(timerTotal));
	     	   			if (mode == 1) LevelsListCustom.updateRecords();
	     	   			else if (mode == 2) {
		     	   			GameVars.SetLevelComplete(levelID);
		     	   			LevelsListGame.updateRecords();
	     	   			}
	     	   			gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
	     	   			gsm.SetPlaying(false);
	     	   			break;
	     	   		} else cl.notFinished();
	     	   } else if ((cl.isPlayerDead() && !isReplay) | (forcequit) | (forceRestart)) {
	       		    BikeGameSounds.PlaySound(soundHit, bikeMaxVolume/2.0f);
	       		    StopSounds();
    	   			if (!isReplay) {
	     	   			if ((mode == 1) || (mode == 2)) storeReplay(dt);
    	   				GameVars.SetTimerTotal(-1);
    	     		    ReplayVars.replayTimer = (int) (TimeUtils.millis()) - timerStart;
    	   			}
    	   			if (forcequit) {
    	            	gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
    	            	gsm.SetPlaying(false);
    	   			} else {
    	   				// forceRestart is true, or the player died in the level
    	            	// If it's not a replay, reset the replay variables
//    	            	if (!isReplay) ReplayVars.Reset(editorString, levelID, mode);
    	   				// Exit the current level
    	            	gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
    	            	gsm.SetPlaying(false);
    	            	// Start it again
    	            	if (mode != 0) {
    	            		gsm.setState(GameStateManager.PLAY, true, editorString, levelID, mode);
    	            		gsm.SetPlaying(true);
    	            	}
    	   			}
	            	break;
	     	   }
	       	   // Update the bike position
        	   if (isReplay) updateBikeReplay(dt);
        	   else {
	       		   updateBike(dt);       
	       		   if ((mode == 1) || (mode == 2)) storeReplay(dt);
	       	   }
	       	   // Update the other elements in the scene
        	   updateSounds(dt);
        	   updateCollect();
        	   updateFallingBodies(dt);
        	   updateTriggerBodies(dt);
        	   updateKinematicBodies(dt);
        	   updateSwitches();
               if (containsAnimatedBG) updateAnimatedBG(dt);
               resetMusicVolumes();
               if (containsWaterfall) updateWaterfall(dt);
        	   if (containsRain) updateRain(dt);
        	   updateMusicVolumes();
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

    private void StopSounds() {
		if (soundBikeIdle != null) soundBikeIdle.setLooping(soundIDBikeIdle, false);
		if (soundWaterfall != null) soundWaterfall.setLooping(false);
        if (soundRain != null) soundRain.setLooping(false);
        if (soundWind != null) soundWind.setLooping(false);
    }
    
    private void updateSounds(float dt) {
    	// Gems
		if (soundTimeGem > 0.0f) soundTimeGem -= dt;
		else soundTimeGem = 0.0f;
		// Keys
		if (soundTimeKey > 0.0f) soundTimeKey -= dt;
		else soundTimeKey = 0.0f;
		// Nitrous
		if (soundTimeNitrous > 0.0f) soundTimeNitrous -= dt;
		else soundTimeNitrous = 0.0f;
		// Doors
		if (soundTimeDoor > 0.0f) soundTimeDoor -= dt;
		else soundTimeDoor = 0.0f;
		// Gravity
		if (soundTimeGravity > 0.0f) soundTimeGravity -= dt;
		else soundTimeGravity = 0.0f;
    }

    private void switchBikeDirection() {
		BikeGameSounds.PlaySound(soundBikeSwitch, 0.1f);
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

	private void updateBikeReplay(float dt) {
		Vector2 temppos;
		replayTime += dt;
		int rIndex = ReplayVars.GetIndex(replayTime);		
		float time1 = ReplayVars.replayTime.get(rIndex);
		float time2 = ReplayVars.replayTime.get(rIndex+1);
		float mid = (replayTime-time1)/(time2-time1);
		// Transform LW
		float lw_x = Interpolation.linear.apply(ReplayVars.replayLW_X.get(rIndex), ReplayVars.replayLW_X.get(rIndex+1), mid);
		float lw_y = Interpolation.linear.apply(ReplayVars.replayLW_Y.get(rIndex), ReplayVars.replayLW_Y.get(rIndex+1), mid);
		float lw_a = Interpolation.linear.apply(ReplayVars.replayLW_A.get(rIndex), ReplayVars.replayLW_A.get(rIndex+1), mid);
		float lw_v = Interpolation.linear.apply(ReplayVars.replayLW_V.get(rIndex), ReplayVars.replayLW_V.get(rIndex+1), mid);
		temppos = new Vector2(lw_x, lw_y);
		bikeBodyLW.setTransform(temppos.cpy(), lw_a);
		bikeBodyLW.setAngularVelocity(lw_v);
		// Transform RW
		float rw_x = Interpolation.linear.apply(ReplayVars.replayRW_X.get(rIndex), ReplayVars.replayRW_X.get(rIndex+1), mid);
		float rw_y = Interpolation.linear.apply(ReplayVars.replayRW_Y.get(rIndex), ReplayVars.replayRW_Y.get(rIndex+1), mid);
		float rw_a = Interpolation.linear.apply(ReplayVars.replayRW_A.get(rIndex), ReplayVars.replayRW_A.get(rIndex+1), mid);
		float rw_v = Interpolation.linear.apply(ReplayVars.replayRW_V.get(rIndex), ReplayVars.replayRW_V.get(rIndex+1), mid);
//	    float[] cCoord = PolygonOperations.RotateCoordinate(rw_x, rw_y, rw_a, 0.0f, 0.0f);
//	    temppos = new Vector2(cCoord[0],cCoord[1]);
//	    bikeBodyRW.setTransform(temppos.add(bikeBodyRW.getWorldCenter()), rw_a);
		temppos = new Vector2(rw_x, rw_y);
		bikeBodyRW.setTransform(temppos.cpy(), rw_a);
		//bikeBodyRW.setTransform(bikeBodyLW.getPosition().sub(bikeBodyRW.getPosition());)
		bikeBodyRW.setAngularVelocity(rw_v);
		// Transform Chassis
		float bike_a = Interpolation.linear.apply(ReplayVars.replayBike_A.get(rIndex), ReplayVars.replayBike_A.get(rIndex+1), mid);
		float bike_x = Interpolation.linear.apply(ReplayVars.replayBike_X.get(rIndex), ReplayVars.replayBike_X.get(rIndex+1), mid);
		float bike_y = Interpolation.linear.apply(ReplayVars.replayBike_Y.get(rIndex), ReplayVars.replayBike_Y.get(rIndex+1), mid);
		temppos = new Vector2(bike_x, bike_y);
        //bikeBodyC.setTransform(temppos.add(bikeBodyC.getWorldCenter().cpy()), bike_a);
        bikeBodyC.setTransform(temppos, bike_a);
		// Transform Head
		bike_x = Interpolation.linear.apply(ReplayVars.replayHead_X.get(rIndex), ReplayVars.replayHead_X.get(rIndex+1), mid);
		bike_y = Interpolation.linear.apply(ReplayVars.replayHead_Y.get(rIndex), ReplayVars.replayHead_Y.get(rIndex+1), mid);
		temppos = new Vector2(bike_x, bike_y);
        bikeBodyH.setTransform(temppos, bike_a);
		// Transform Rider body
		bike_a = Interpolation.linear.apply(ReplayVars.replayRider_A.get(rIndex), ReplayVars.replayRider_A.get(rIndex+1), mid);
		bike_x = Interpolation.linear.apply(ReplayVars.replayRider_X.get(rIndex), ReplayVars.replayRider_X.get(rIndex+1), mid);
		bike_y = Interpolation.linear.apply(ReplayVars.replayRider_Y.get(rIndex), ReplayVars.replayRider_Y.get(rIndex+1), mid);
		temppos = new Vector2(bike_x, bike_y);
        bikeBodyR.setTransform(temppos, bike_a);

		// Update the Bike Sound
		UpdateBikeSound();

		// Check if the bike direction needs to be switched
		if (ReplayVars.CheckSwitchDirection(rIndex)) switchBikeDirection();
		if ((bikeScale > -1.0f) & (bikeScale < 1.0f)) {
			bikeScale += bikeScaleLev;
			if (bikeScale < -1.0f) {
				bikeScale = -1.0f;
			} else if (bikeScale > 1.0f) {
				bikeScale = 1.0f;
			}
		}
		// Update the camera position
		updateCameraPostion();
		
		// Check if escape was pressed - if so, end the replay
		if (1000.0f*replayTime > ReplayVars.replayTimer) {
			forcequit = true;
		}
	}

	private void storeReplay(float dt) {
		replayTime += dt;
		Vector2 bikeCen = bikeBodyC.getPosition();
		Vector2 headCen = bikeBodyH.getPosition();
		Vector2 riderCen = bikeBodyR.getPosition();
		Vector2 LWCen = bikeBodyLW.getPosition();
		Vector2 RWCen = bikeBodyRW.getPosition();
		// Store all of the information
		ReplayVars.replayTime.add(replayTime);
		// Head
		ReplayVars.replayHead_X.add(headCen.x);
		ReplayVars.replayHead_Y.add(headCen.y);
		// Torso
		ReplayVars.replayRider_X.add(riderCen.x);
		ReplayVars.replayRider_Y.add(riderCen.y);
		ReplayVars.replayRider_A.add(bikeBodyR.getAngle());
		// Bike
		ReplayVars.replayBike_X.add(bikeCen.x);
		ReplayVars.replayBike_Y.add(bikeCen.y);
		ReplayVars.replayBike_A.add(bikeBodyC.getAngle());
		// Left Wheel
		ReplayVars.replayLW_X.add(LWCen.x);
		ReplayVars.replayLW_Y.add(LWCen.y);
		ReplayVars.replayLW_A.add(bikeBodyLW.getAngle());
		ReplayVars.replayLW_V.add(bikeBodyLW.getAngularVelocity());
		// Right Wheel
		ReplayVars.replayRW_X.add(RWCen.x);
		ReplayVars.replayRW_Y.add(RWCen.y);
		ReplayVars.replayRW_A.add(bikeBodyRW.getAngle());
		ReplayVars.replayRW_V.add(bikeBodyRW.getAngularVelocity());
	}
	
	private void UpdateBikeSound() {
		if (bikeDirc == 1.0f) {
			if (bikeBodyLW.getAngularVelocity() > -100.0f) {
				bikePitch = 1.0f - 1.0f*bikeBodyLW.getAngularVelocity()/100.0f;
				bikeVolume = -bikeMaxVolume*bikeBodyLW.getAngularVelocity()/5.0f;
			}
		} else {
			if (bikeBodyRW.getAngularVelocity() < 100.0f) {
				bikePitch = 1.0f + 1.0f*bikeBodyRW.getAngularVelocity()/100.0f;				
				bikeVolume = bikeMaxVolume*bikeBodyRW.getAngularVelocity()/5.0f;
			}
		}
		// Update Bike Volume
		if (bikeVolume > bikeMaxVolume) bikeVolume = bikeMaxVolume;
		else if (bikeVolume < 0.0f) bikeVolume = 0.0f;
		soundBikeIdle.setVolume(soundIDBikeIdle, bikeMaxVolume-bikeVolume);
		soundBikeMove.setVolume(soundIDBikeMove, bikeVolume);
		// Update Bike Pitch
		if (bikePitch > 2.0f) bikeVolume = 2.0f;
		else if (bikePitch < 0.5f) bikePitch = 0.5f;
		soundBikeMove.setPitch(soundIDBikeMove, bikePitch);
	}
	
	private void updateBike(float dt) {
		if (motorTorque != 0.0f) {
			if (bikeDirc == 1.0f) {
				if (bikeBodyLW.getAngularVelocity() > -100.0f) {
					bikeBodyLW.applyTorque(-1.0f*(3*applyNitrous+1.0f)*Math.max(motorTorque, 500.0f/(1.0f+Math.abs(bikeBodyLW.getAngularVelocity()))), false);
				}
			} else {
				if (bikeBodyRW.getAngularVelocity() < 100.0f) {
					bikeBodyRW.applyTorque((3*applyNitrous+1.0f)*Math.max(motorTorque, 500.0f/(1.0f+Math.abs(bikeBodyRW.getAngularVelocity()))), false);
				}
			}
		}
		UpdateBikeSound();
		if ((bikeScale > -1.0f) & (bikeScale < 1.0f)) {
			bikeScale += bikeScaleLev;
			if (bikeScale < -1.0f) {
				bikeScale = -1.0f;
			} else if (bikeScale > 1.0f) {
				bikeScale = 1.0f;
			}
		}
//		if (applyRocket==1) {
//			Vector2 temppos;
//			float factor=3.0f;
//			double addAngle = 0.0;
//			if (bikeDirc < 0.0f) addAngle = Math.PI;
//			double angleRocket = bikeBodyC.getAngle() + addAngle;
//			temppos = new Vector2(factor*(float)Math.cos(angleRocket), factor*(float) Math.sin(angleRocket));
//			bikeBodyC.setLinearVelocity(temppos.cpy());
//		}
		
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
		// Update the camera position...
		updateCameraPostion();
	}

	private void updateCameraPostion() {
		Vector3 pos = new Vector3(bikeBodyC.getWorldCenter(), 0);
		Vector2 posShft, gravVect, gravVectN;
		float angleGrav;
		float lenGravNew = gravityNew.len();
        float lenGravPrev = gravityPrev.len();
		if (gravityScale == -1.0f) posShft = gravityNew.cpy().nor().scl(-B2DVars.SCRWIDTH/8.0f);
		else {
			//posShft = (mWorld.getGravity().nor().scl((float)Math.sin(gravityScale*Math.PI/2)).add(gravityPrev.scl(1.0f-(float)Math.sin(gravityScale*Math.PI/2)))).nor().scl(-B2DVars.SCRWIDTH/8.0f);
			angleGrav = (float) (Math.acos(gravityPrev.cpy().nor().dot(gravityNew.cpy().nor())));
			if (lenGravPrev != 0) {
                gravVect = gravityPrev.cpy().nor().scl(-B2DVars.SCRWIDTH / 8.0f);//.scl(gravityNext.len() * (float) (Math.cos(angleGrav*0.5f)/Math.cos(angleGrav*(0.5f - gravityScale))));
                if (lenGravNew == 0) gravVect.scl(1.0f-gravityScale);
            } else if (lenGravNew != 0) {
                angleGrav = 0.0f;
                gravVect = gravityNew.cpy().nor().scl(-B2DVars.SCRWIDTH / 8.0f);//.scl(gravityNext.len() * (float) (Math.cos(angleGrav*0.5f)/Math.cos(angleGrav*(0.5f - gravityScale))));
                gravVect.scl(gravityScale);
            } else {
                gravVect = gravityPrev.cpy().nor().scl(-B2DVars.SCRWIDTH / 8.0f);
            }
			angleGrav *= (float)Math.sin(gravityScale*Math.PI/2);
			if (dircGrav < 0.0f) angleGrav *= -1.0f; // Rotate clockwise
			posShft = new Vector2((float)(gravVect.x*Math.cos(angleGrav) - gravVect.y*Math.sin(angleGrav)), (float)(gravVect.x*Math.sin(angleGrav) + gravVect.y*Math.cos(angleGrav)));
			gravityScale += 0.01f;
			if (gravityScale > 1.0f) {
				mWorld.setGravity(gravityNew.cpy());
				gravityScale = -1.0f;
			} else if (gravityScale >= 0.0f) {
				gravVect = gravityOld.cpy().scl(1.0f-gravityScale).add(gravityNew.cpy().scl(gravityScale));
				mWorld.setGravity(gravVect.cpy());
			}
		}
		Vector2 shftCpy = posShft.cpy().scl(-(float)Math.sin(bikeScale*Math.PI/2)); 
		pos.x += posShft.x - shftCpy.y;
		pos.y += posShft.y + shftCpy.x;
		b2dCam.position.set(pos);
		b2dCam.update();		
	}

	private boolean checkCollect(Body tst, Array<Body> allBodies) {
        // This routine checks if a body has already been collected
        for (int ii=0; ii< allBodies.size; ii++) {
            if (allBodies.get(ii)==tst) return true;
        }
        return false;
    }

    private void updateCollect() {
    	// check for collected keys or jewels
    	Array<Body> bodies = cl.getBodies();
        Array<Body> colBodies = new Array<Body>();
    	for(int i = 0; i < bodies.size; i++) {
    		String collectID = (String)mScene.getCustom(bodies.get(i), "collect", null);
    		if ((collectID != null) && (!checkCollect(bodies.get(i), colBodies))){
    			boolean noKeys = false;
    			if (collectID.equals("DoorRed")) {
                    colBodies.add(bodies.get(i));
    				if (collectKeyRed > 0) {
    					collectKeyRed -= 1;
    					Body bbcollide = cl.getBikeBodyCollide().pop();
    					doorArr[0] = bodies.get(i).getPosition().x;
    					doorArr[1] = bodies.get(i).getPosition().y;
    					doorArr[3] = 2.0f*ObjectVars.objectDoor[5]*B2DVars.EPPM;
    					doorArr[4] = ((Float)mScene.getCustom(bodies.get(i), "angle", 0.0f));
    					doorArr[2] = 0.01f*PolygonOperations.OpenDoorDirection(doorArr[0],doorArr[1],bbcollide.getPosition().x,bbcollide.getPosition().y,doorArr[4]);
    					doorImages.add(doorArr.clone());
        				if (soundTimeDoor <= 0.0f) {
        					BikeGameSounds.PlaySound(soundDoor, 1.0f);
        					soundTimeDoor = 1.5f; //  This is half the duration of a Door sound
        				}
    				} else noKeys = true;
    			} else if (collectID.equals("DoorGreen")) {
                    colBodies.add(bodies.get(i));
    				if (collectKeyGreen > 0) {
    					collectKeyGreen -= 1;
    					Body bbcollide = cl.getBikeBodyCollide().pop();
    					doorArr[0] = bodies.get(i).getPosition().x;
    					doorArr[1] = bodies.get(i).getPosition().y;
    					doorArr[3] = 2.0f*ObjectVars.objectDoor[5]*B2DVars.EPPM;
    					doorArr[4] = ((Float)mScene.getCustom(bodies.get(i), "angle", 0.0f));
    					doorArr[2] = 0.01f*PolygonOperations.OpenDoorDirection(doorArr[0],doorArr[1],bbcollide.getPosition().x,bbcollide.getPosition().y,doorArr[4]);
    					doorImages.add(doorArr.clone());
        				if (soundTimeDoor <= 0.0f) {
        					BikeGameSounds.PlaySound(soundDoor, 1.0f);
        					soundTimeDoor = 1.5f; //  This is half the duration of a Door sound
        				}
    				} else noKeys = true;
    			} else if (collectID.equals("DoorBlue")) {
                    colBodies.add(bodies.get(i));
    				if (collectKeyBlue > 0) {
    					collectKeyBlue -= 1;
    					Body bbcollide = cl.getBikeBodyCollide().pop();
    					doorArr[0] = bodies.get(i).getPosition().x;
    					doorArr[1] = bodies.get(i).getPosition().y;
    					doorArr[3] = 2.0f*ObjectVars.objectDoor[5]*B2DVars.EPPM;
    					doorArr[4] = ((Float)mScene.getCustom(bodies.get(i), "angle", 0.0f));
    					doorArr[2] = 0.01f*PolygonOperations.OpenDoorDirection(doorArr[0],doorArr[1],bbcollide.getPosition().x,bbcollide.getPosition().y,doorArr[4]);
    					doorImages.add(doorArr.clone());
        				if (soundTimeDoor <= 0.0f) {
        					BikeGameSounds.PlaySound(soundDoor, 1.0f);
        					soundTimeDoor = 1.5f; //  This is half the duration of a Door sound
        				}
    				} else noKeys = true;
    			} else if (collectID.equals("Gravity")) {
                    colBodies.add(bodies.get(i));
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
					gravityOld = mWorld.getGravity().cpy();
    				gravityNew = gravNext.cpy();
    				//mWorld.setGravity(gravNext);
    				if (soundTimeGravity <= 0.0f) {
    					BikeGameSounds.PlaySound(soundGravity, 1.0f);
    					soundTimeGravity = 1.0f; //  This is half the duration of a Gravity sound
    				}
    			} else if (collectID.equals("KeyRed")) {
                    colBodies.add(bodies.get(i));
    				collectKeyRed += 1;
    				if (soundTimeKey <= 0.0f) {
    					BikeGameSounds.PlaySound(soundKey, 1.0f);
    					soundTimeKey = 1.0f; //  This is half the duration of a Key sound
    				}
    			} else if (collectID.equals("KeyGreen")) {
                    colBodies.add(bodies.get(i));
    				collectKeyGreen += 1;
    				if (soundTimeKey <= 0.0f) {
    					BikeGameSounds.PlaySound(soundKey, 1.0f);
    					soundTimeKey = 1.0f; //  This is half the duration of a Key sound
    				}
    			} else if (collectID.equals("KeyBlue")) {
                    colBodies.add(bodies.get(i));
    				collectKeyBlue += 1;
    				if (soundTimeKey <= 0.0f) {
    					BikeGameSounds.PlaySound(soundKey, 1.0f);
    					soundTimeKey = 1.0f; //  This is half the duration of a Key sound
    				}
    			} else if (collectID.equals("Nitrous")) {
                    colBodies.add(bodies.get(i));
    				if ((collectNitrous == 0) & (nitrousLevel == 0.0f)) nitrousLevel = 1.0f;
    				collectNitrous += 1;
    				if (soundTimeNitrous <= 0.0f) {
    					BikeGameSounds.PlaySound(soundNitrous, 1.0f);
    					soundTimeNitrous = 1.0f; //  This is half the duration of a Nitrous sound
    				}
    			} else if (collectID.equals("Jewel")) {
                    colBodies.add(bodies.get(i));
    				if (collectJewel != 0) collectJewel -= 1;
    				if (soundTimeGem <= 0.0f) {
    					BikeGameSounds.PlaySound(soundGem, 1.0f);
    					soundTimeGem = 1.0f; //  This is half the duration of a gem sound
    				}
    			} else if (collectID.equals("Diamond")) {
                    colBodies.add(bodies.get(i));
    				collectJewel = 0;
    				collectDiamond = true; // The player has collected the diamond
    				BikeGameSounds.PlaySound(soundDiamond, 1.0f);
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

    private void updateTriggerBodies (float dt) {
    	// First delete the trigger joints
    	Array<Body> joints = cl.getTriggerJoints();
    	try {
	    	for (int i = 0; i < joints.size; i++) {
	    		mWorld.destroyJoint(joints.get(i).getJointList().first().joint);
	    		triggerFixtList = joints.get(i).getFixtureList();
	    		for (int j=0; j < triggerFixtList.size; j++) {
	    			if (triggerFixtList.get(j).getUserData().equals("GroundTrigger")) {
	    				joints.get(i).destroyFixture(triggerFixtList.get(j));
	    			}
	    		}
	    		triggerFixtList.clear();
	    	}
	    	joints.clear();
    	} catch (IllegalStateException e) {}  
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
    	// Check if a switch has been touched
    	Array<Body> bodies = cl.getSwitchBody();
    	int switchIdx;
    	if (bodies.size != 0) {
    		// Check switches
    		for (int i=0; i<bodies.size; i++) {
	    		switchIdx = (Integer) mScene.getCustom(bodies.get(i), "switchID", -1);
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
	        			BikeGameSounds.PlaySound(soundSwitch, 1.0f);
	        		}
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
            Vector2 gravNext = (Vector2)mScene.getCustom(bodies.get(0), "gravityVector", mWorld.getGravity().cpy());
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
    			// Transform the body
                cCoord = PolygonOperations.RotateCoordinate(bikeBodyR.getPosition().x-offsetXY.x, bikeBodyR.getPosition().y-offsetXY.y, switchAngle, 0.0f, 0.0f);
                Vector2 shiftR = new Vector2(cCoord[0],cCoord[1]).add(offsetXY).sub(bikeBodyR.getPosition());
    			// Transform the bike
    			bikeBodyLW.setTransform(bikeBodyLW.getPosition().add(transportXY).add(transXY).add(shiftLW), bikeBodyLW.getAngle()+transportAngle);
    			bikeBodyRW.setTransform(bikeBodyRW.getPosition().add(transportXY).add(transXY).add(shiftRW), bikeBodyRW.getAngle()+transportAngle);
                bikeBodyR.setTransform(bikeBodyR.getPosition().add(transportXY).add(transXY).add(shiftR), bikeBodyR.getAngle()+transportAngle);
    			bikeBodyH.setTransform(bikeBodyH.getPosition().add(transportXY).add(transXY).add(shiftH), bikeBodyH.getAngle()+transportAngle);
    			bikeBodyC.setTransform(bikeBodyC.getPosition().add(transportXY).add(transXY), bikeBodyC.getAngle()+transportAngle);
    			// Transform the velocity for each component of the bike
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyC.getLinearVelocity().x, bikeBodyC.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
    			bikeBodyC.setLinearVelocity(cCoord[0],cCoord[1]);
                cCoord = PolygonOperations.RotateCoordinate(bikeBodyH.getLinearVelocity().x, bikeBodyH.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
                bikeBodyH.setLinearVelocity(cCoord[0],cCoord[1]);
                cCoord = PolygonOperations.RotateCoordinate(bikeBodyR.getLinearVelocity().x, bikeBodyR.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
                bikeBodyR.setLinearVelocity(cCoord[0],cCoord[1]);
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyLW.getLinearVelocity().x, bikeBodyLW.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
    			bikeBodyLW.setLinearVelocity(cCoord[0],cCoord[1]);
    			cCoord = PolygonOperations.RotateCoordinate(bikeBodyRW.getLinearVelocity().x, bikeBodyRW.getLinearVelocity().y, switchAngle, 0.0f, 0.0f);
    			bikeBodyRW.setLinearVelocity(cCoord[0],cCoord[1]);
    			// Force transporters to be inactive
    			canTransport = 0.0f;
    			BikeGameSounds.PlaySound(soundTransport, 1.0f);
    			// Now update the gravity, if it's an invisible transport
				float angleGrav;
				if (gravityScale >= 0.0f) {
					angleGrav = (float) (Math.acos(gravityPrev.cpy().nor().dot(mWorld.getGravity().cpy().nor())));
					angleGrav *= (float)Math.sin(gravityScale*Math.PI/2);
					Vector2 gravVect = gravityPrev.cpy().nor().scl(gravityPrev.len());
					if (dircGrav < 0.0f) angleGrav *= -1.0f; // Rotate clockwise
					gravityPrev = new Vector2((float)(gravVect.x*Math.cos(angleGrav) - gravVect.y*Math.sin(angleGrav)), (float)(gravVect.x*Math.sin(angleGrav) + gravVect.y*Math.cos(angleGrav)));
					//gravityPrev = mWorld.getGravity().cpy().nor();
				} else gravityPrev = mWorld.getGravity().cpy().nor();
				dircGrav = gravityPrev.x*gravNext.y - gravityPrev.y*gravNext.x;
				gravityScale = 0.0f;
				gravityOld = mWorld.getGravity().cpy();
				gravityNew = gravNext.cpy();
    		}
    	}
    	bodies.clear();
    }

    private void updateWaterfall(float dt) {
    	// Shift the waterfall
    	float shift = dt*9.8f;
    	waterfallPos -= shift;
    	if (waterfallPos <= -512.0f*PolySpatial.PIXELS_PER_METER) {
    		waterfallPos += 1024.0f*PolySpatial.PIXELS_PER_METER;
    	}
    	waterfallBody.setTransform(0.0f, waterfallPos, 0.0f);
    	// Update the volume of the waterfall, depending on how close the rider is to the waterfall
    	float fadeDist = 20.0f/PolySpatial.PIXELS_PER_METER;
    	Vector2 riderPos = bikeBodyH.getPosition().scl(1.0f/PolySpatial.PIXELS_PER_METER);
    	int idxa, idxb, flag=0;
    	float xa, ya, xb, yb, dist, grad, gradb, intc, intcb, xint, yint, mindist = 0.0f;
    	// First check if the rider is inside a waterfall
        float[] volumes = new float[musicVolumes.length];
        for (int ss=1; ss < DecorVars.platformSounds.length; ss++) {
            for (int j=0; j < waterfallVerts.size; j++) {
                if (waterfallSounds.get(j) != DecorVars.GetSoundIndexFromString(DecorVars.platformSounds[ss])) {
                    continue;
                }
                if (PolygonOperations.PointInPolygon(waterfallVerts.get(j), riderPos.x, riderPos.y)) {
                    volumes[ss-1] = 1.0f; // Maximum volume
                    break;
                } else {
                    for (int i=0; i < waterfallVerts.get(j).length/2; i++) {
                        idxa = i;
                        if (i == waterfallVerts.get(j).length/2 - 1) idxb = 0;
                        else idxb = i+1;
                        // Calculate the gradient
                        xa = waterfallVerts.get(j)[2*idxa];
                        ya = waterfallVerts.get(j)[2*idxa+1];
                        xb = waterfallVerts.get(j)[2*idxb];
                        yb = waterfallVerts.get(j)[2*idxb+1];
                        if (xa==xb) {
                            if (ya>yb) {
                                if (riderPos.y>ya) yint = riderPos.y-ya;
                                else if (riderPos.y<yb) yint = yb-riderPos.y;
                                else yint = 0.0f;
                            } else {
                                if (riderPos.y>yb) yint = riderPos.y-yb;
                                else if (riderPos.y<ya) yint = ya-riderPos.y;
                                else yint = 0.0f;
                            }
                            dist = (float) Math.sqrt((riderPos.x-xa)*(riderPos.x-xa) + yint*yint);
                        } else if (ya==yb) {
                            if (xa>xb) {
                                if (riderPos.x>xa) yint = riderPos.x-xa;
                                else if (riderPos.x<xb) yint = xb-riderPos.x;
                                else yint = 0.0f;
                            } else {
                                if (riderPos.x>xb) yint = riderPos.x-xb;
                                else if (riderPos.x<xa) yint = xa-riderPos.x;
                                else yint = 0.0f;
                            }
                            dist = (float) Math.sqrt((riderPos.y-ya)*(riderPos.y-ya) + yint*yint);
                        } else {
                            grad = (yb-ya)/(xb-xa);
                            intc = ya - grad*xa;
                            gradb = -(xb-xa)/(yb-ya);
                            intcb = riderPos.y - gradb*riderPos.x;
                            // Calculate the intersection, and make sure the intersection is within bounds
                            xint = (intcb-intc)/(grad-gradb);
                            if (xa < xb) {
                                if (xint<xa) xint = xa;
                                else if (xint>xb) xint = xb;
                            } else {
                                if (xint<xb) xint = xb;
                                else if (xint>xa) xint = xa;
                            }
                            // Calculate the distance between the intersection and the cursor
                            yint = grad*xint + intc;
                            dist = (float) Math.sqrt((riderPos.x-xint)*(riderPos.x-xint) + (riderPos.y-yint)*(riderPos.y-yint));
                        }
                        if ((dist < mindist) | (flag==0)) {
                            mindist = dist;
                            flag = 1;
                        }
                    }
                    // Set the volume (dist)
                    dist = (fadeDist-mindist)/fadeDist;
                    if (dist > volumes[ss-1]) volumes[ss-1] = dist;
                }
            }
            // Set the sound volume of the waterfall
            if (volumes[ss-1] > musicVolumes[ss-1]) musicVolumes[ss-1] = volumes[ss-1];
        }
    }

    private void updateRain(float dt) {
    	// Shift the waterfall
    	float shift = dt*9.8f;
    	rainPos -= shift;
    	if (rainPos <= -903.0f*PolySpatial.PIXELS_PER_METER) {
    		rainPos += 1806.0f*PolySpatial.PIXELS_PER_METER;
    	}
    	rainBody.setTransform(0.0f, rainPos, 0.0f);
    	// Update the volume of the rain, depending on how close the rider is to the rain
    	float fadeDist = 20.0f/PolySpatial.PIXELS_PER_METER;
    	Vector2 riderPos = bikeBodyH.getPosition().scl(1.0f/PolySpatial.PIXELS_PER_METER);
    	int idxa, idxb, flag=0;
    	float xa, ya, xb, yb, dist, grad, gradb, intc, intcb, xint, yint, mindist = 0.0f;
    	// First check if the rider is inside a rain
        float[] volumes = new float[musicVolumes.length];
        for (int ss=1; ss < DecorVars.platformSounds.length; ss++) {
            for (int j = 0; j < rainVerts.size; j++) {
                if (rainSounds.get(j) != DecorVars.GetSoundIndexFromString(DecorVars.platformSounds[ss])) {
                    continue;
                }
                if (PolygonOperations.PointInPolygon(rainVerts.get(j), riderPos.x, riderPos.y)) {
                    volumes[ss-1] = 1.0f; // Maximum volume
                    break;
                } else {
                    for (int i = 0; i < rainVerts.get(j).length / 2; i++) {
                        idxa = i;
                        if (i == rainVerts.get(j).length / 2 - 1) idxb = 0;
                        else idxb = i + 1;
                        // Calculate the gradient
                        xa = rainVerts.get(j)[2 * idxa];
                        ya = rainVerts.get(j)[2 * idxa + 1];
                        xb = rainVerts.get(j)[2 * idxb];
                        yb = rainVerts.get(j)[2 * idxb + 1];
                        if (xa == xb) {
                            if (ya > yb) {
                                if (riderPos.y > ya) yint = riderPos.y - ya;
                                else if (riderPos.y < yb) yint = yb - riderPos.y;
                                else yint = 0.0f;
                            } else {
                                if (riderPos.y > yb) yint = riderPos.y - yb;
                                else if (riderPos.y < ya) yint = ya - riderPos.y;
                                else yint = 0.0f;
                            }
                            dist = (float) Math.sqrt((riderPos.x - xa) * (riderPos.x - xa) + yint * yint);
                        } else if (ya == yb) {
                            if (xa > xb) {
                                if (riderPos.x > xa) yint = riderPos.x - xa;
                                else if (riderPos.x < xb) yint = xb - riderPos.x;
                                else yint = 0.0f;
                            } else {
                                if (riderPos.x > xb) yint = riderPos.x - xb;
                                else if (riderPos.x < xa) yint = xa - riderPos.x;
                                else yint = 0.0f;
                            }
                            dist = (float) Math.sqrt((riderPos.y - ya) * (riderPos.y - ya) + yint * yint);
                        } else {
                            grad = (yb - ya) / (xb - xa);
                            intc = ya - grad * xa;
                            gradb = -(xb - xa) / (yb - ya);
                            intcb = riderPos.y - gradb * riderPos.x;
                            // Calculate the intersection, and make sure the intersection is within bounds
                            xint = (intcb - intc) / (grad - gradb);
                            if (xa < xb) {
                                if (xint < xa) xint = xa;
                                else if (xint > xb) xint = xb;
                            } else {
                                if (xint < xb) xint = xb;
                                else if (xint > xa) xint = xa;
                            }
                            // Calculate the distance between the intersection and the cursor
                            yint = grad * xint + intc;
                            dist = (float) Math.sqrt((riderPos.x - xint) * (riderPos.x - xint) + (riderPos.y - yint) * (riderPos.y - yint));
                        }
                        if ((dist < mindist) | (flag == 0)) {
                            mindist = dist;
                            flag = 1;
                        }
                    }
                    // Set the volume (dist)
                    dist = (fadeDist - mindist) / fadeDist;
                    if (dist > volumes[ss-1]) volumes[ss-1] = dist;
                }
            }
            // Set the sound volume of the waterfall
            if (volumes[ss-1] > musicVolumes[ss-1]) musicVolumes[ss-1] = volumes[ss-1];
        }
    }

    private void resetMusicVolumes() {
        for (int ss=0; ss<musicVolumes.length; ss++) {
            musicVolumes[ss] = 0.0f;
        }
    }

    private void updateMusicVolumes() {
        for (int ss=0; ss<musicVolumes.length; ss++) {
            switch (ss+1) {
                case DecorVars.soundRain: soundRain.setVolume(musicVolumes[ss]);
                case DecorVars.soundWaterfall: soundWaterfall.setVolume(musicVolumes[ss]);
                case DecorVars.soundWind: soundWind.setVolume(musicVolumes[ss]);
            }
        }
    }

    private void updateAnimatedBG(float dt) {
        // Shift the waterfall
        float shift = dt*animatedBGSpeed;
        animatedBGPos -= shift;
        if (animatedBGPos <= -1000.0f*PolySpatial.PIXELS_PER_METER) {
            animatedBGPos += 2000.0f*PolySpatial.PIXELS_PER_METER;
        }
        animatedBGBody.setTransform(0.0f, animatedBGPos, 0.0f);
    }

    public void render() {
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);  // Black
        //Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1); // Lilac
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport((int) BikeGame.viewport.x, (int) BikeGame.viewport.y, (int) BikeGame.viewport.width, (int) BikeGame.viewport.height);
        //Gdx.gl.glViewport(0, 0, SCRWIDTH, SCRHEIGHT);

        switch (mState)
        {
           case STARTING:
        	   break;
           case LOADING:
        	   break;
           case LOADED:
        	   renderWorld();
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
    private void processSceneLoad() {

       if (mAssetManager == null)
       {
          // perform a blocking load...
          RubeSceneLoader loader = new RubeSceneLoader();
          for (int i = 0; i < LEVEL_FILE_LIST[mRubeFileList].length; i++)
          {
             // each iteration adds to the scene that is ultimately returned...
        	  if ((i==0) & (editorString != null)) {
        		  // Load an editor scene
//        		  Thread.setDefaultUncaughtExceptionHandler( (thread, throwable) -> {
//        		        System.out.println(throwable.getMessage());
//        		        mScene = loader.addEditorScene(editorString);
//        		        System.out.println("made it!");
//        		       	});
        	       // Handle uncaught exceptions
        		  mScene = loader.addEditorScene(editorString);
//          	   gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
//     	    	   gsm.SetPlaying(false);
//         	       return;
        		  mRubeFileIndex++;
        	  } else {
        		  // Load a level
        		  mScene = loader.addScene(Gdx.files.internal(LEVEL_FILE_LIST[mRubeFileList][mRubeFileIndex++]));
        	  }
          }
          int failed = processScene();
          if (failed==0) {
              gravityOld = mWorld.getGravity().cpy();
              gravityNew = mWorld.getGravity().cpy();
              updateCameraPostion();
          }
          mNextState = GAME_STATE.LOADED;
       } else if (mAssetManager.update()) {
          // each iteration adds to the scene that is ultimately returned...
          mScene = mAssetManager.get(LEVEL_FILE_LIST[mRubeFileList][mRubeFileIndex++], RubeScene.class);
          if (mRubeFileIndex < LEVEL_FILE_LIST[mRubeFileList].length)
          {
             mAssetManager.load(LEVEL_FILE_LIST[mRubeFileList][mRubeFileIndex], RubeScene.class);
          }
          else
          {
             int failed = processScene();
             mNextState = GAME_STATE.LOADED;
          }
       }
    }
    
    /**
     * Builds up world based on info from the scene...
     */
    private int processScene() {
        createSpatialsFromRubeImages(mScene);
        createPolySpatialsFromRubeFixtures(mScene);

        mWorld = mScene.getWorld();
        // configure simulation settings
        mVelocityIter = mScene.velocityIterations;
        mPositionIter = mScene.positionIterations;
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

        // Get the starting position
        try {
    	    gameInfo = mScene.getNamed(Body.class, "GameInfo").first();
        } catch (NullPointerException e) {
       	    gsm.setState(GameStateManager.PEEK, false, null, levelID, mode);
       	    gsm.SetPlaying(false);
       	    return 1;
        }
        startPosition = (Vector2) mScene.getCustom(gameInfo, "startPosition", null);
        finishPosition = (Vector2) mScene.getCustom(gameInfo, "finishPosition", null);
        startDirection = (Float) mScene.getCustom(gameInfo, "startDirection", 1.0f);
        startAngle = (Float) mScene.getCustom(gameInfo, "startAngle", 0.0f);
        collectJewel = (Integer) mScene.getCustom(gameInfo, "numJewel", 0);
        bounds = (Vector2) mScene.getCustom(gameInfo, "bounds", new Vector2(0.0f, 1000.0f));
        String skyTextureName = (String) mScene.getCustom(gameInfo, "skyTexture", "data/images/sky_bluesky.png");
        sky = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName(skyTextureName),2));
        blackScreen = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("data/images/sky_moon.png"),2));
        bikeDirc = startDirection; // 1=right, -1=left
        bikeScale = startDirection;
        bikeScaleLev *= startDirection;

        // Load the foreground/background textures
        String bgTextName = (String) mScene.getCustom(gameInfo, "bgTexture", null);
        String fgTextName = (String) mScene.getCustom(gameInfo, "fgTexture", null);
        paintForeground = true;
        paintBackdrop = true;
        backgroundLimit = BikeGameTextures.BackgroundLimit(bgTextName); // Must be less than 0.5 (0.0 means the texture will start in the vertical middle of the screen, 0.5 is the bottom, 0.3 means the texture will start 20% from the bottom of the screen);
        if ((fgTextName == null) || (fgTextName.equalsIgnoreCase("none"))) {
            paintForeground = false;
        } else foreground = new Sprite(BikeGameTextures.LoadTexture(fgTextName,2));
        if ((bgTextName == null) || (bgTextName.equalsIgnoreCase("none"))) {
            paintBackdrop = false;
        } else background = new Sprite(BikeGameTextures.LoadTexture(bgTextName,2));
        // Change the timer colour if certain backgrounds are being used
        if ((paintBackdrop == false) || (skyTextureName.equals("data/images/sky_mars.png")) || (skyTextureName.equals("data/images/sky_moon.png")) || (bgTextName.equalsIgnoreCase("background_space"))) {
    	    timer.setColor(0.5f, 0.5f, 0.5f, 1);
        }

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

        bikeBodyLW = mScene.getNamed(Body.class, "bikeleftwheel").first();
        bikeBodyRW = mScene.getNamed(Body.class, "bikerightwheel").first();
        bikeBodyH = mScene.getNamed(Body.class, "bikehead").first();
        bikeBodyR = mScene.getNamed(Body.class, "riderbody").first();
        bikeBodyC = mScene.getNamed(Body.class, "bikebody").first();
        //bikeAngle = bikeBodyC.getAngle();
        bikeBodyLW.setSleepingAllowed(false);
        bikeBodyRW.setSleepingAllowed(false);
        bikeBodyH.setSleepingAllowed(false);
        bikeBodyR.setSleepingAllowed(false);
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
        // Transform the Rider Body
        cCoord = PolygonOperations.RotateCoordinate(bikeBodyR.getPosition().x, bikeBodyR.getPosition().y, startAngle*MathUtils.radiansToDegrees, 0.0f, 0.0f);
        temppos = new Vector2(cCoord[0],cCoord[1]);
        bikeBodyR.setTransform(temppos, startAngle);
        // Transform the Chassis
        cCoord = PolygonOperations.RotateCoordinate(bikeBodyC.getPosition().x, bikeBodyC.getPosition().y, startAngle*MathUtils.radiansToDegrees, 0.0f, 0.0f);
        temppos = new Vector2(cCoord[0],cCoord[1]);
        bikeBodyC.setTransform(temppos, startAngle);
        // Translate the bike into the starting position
        bikeBodyLW.setTransform(bikeBodyLW.getPosition().add(startPosition), bikeBodyLW.getAngle());
        bikeBodyRW.setTransform(bikeBodyRW.getPosition().add(startPosition), bikeBodyRW.getAngle());
        bikeBodyH.setTransform(bikeBodyH.getPosition().add(startPosition), bikeBodyH.getAngle());
        bikeBodyR.setTransform(bikeBodyR.getPosition().add(startPosition), bikeBodyR.getAngle());
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

        // Get the waterfall+rain body
        waterfallBody = mScene.getNamed(Body.class, "Waterfall").first();
        rainBody = mScene.getNamed(Body.class, "Rain").first();

//       // Get all references to trigger platforms and create the fixture
//       // Create a trigger body, which will contain all of the trigger fixtures
//       bdef = new BodyDef();
//       bdef.type = BodyType.StaticBody;
//       bdef.position.set(0, 0);
//       bdef.fixedRotation = true;
//       triggerBody = mWorld.createBody(bdef);
//       Array<String> userData = new Array<String>();
//       // Create a fixture definition for this body
//       float[] triggerArr;
//       PolygonShape shape;
//       FixtureDef fdef = new FixtureDef();
//       fdef.filter.categoryBits = B2DVars.BIT_GROUND;
//       fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_WHEEL | B2DVars.BIT_HEAD;
//       fdef.isSensor = true;
//       fdef.friction = 0.0f;
//       fdef.restitution = 1.0f;
//       // Go through all trigger platforms in the level
//       int tcntr = 0;
//       float trigLength, trigAngle;
//       Vector2 posTrigger;
//       while (true) {
//    	   try {
//    		   tempBody = mScene.getNamed(Body.class, "Trigger"+tcntr).first();
//    	   } catch (NullPointerException e) {
//    		   break;
//    	   }
//    	   posTrigger = (Vector2) mScene.getCustom(tempBody, "triggerPos", null);
//    	   trigLength = (Float) mScene.getCustom(tempBody, "triggerLength", 0.001f);
//    	   trigAngle = (Float) mScene.getCustom(tempBody, "triggerAngle", 0.001f);
//    	   shape = new PolygonShape();
//    	   shape.setAsBox(ObjectVars.objectTriggerWidth, trigLength, posTrigger, trigAngle);
//    	   fdef.shape = shape;
//    	   userData.add("trig_" + tcntr);
//    	   // Add a fixture for this trigger
//    	   triggerBody.createFixture(fdef);
//    	   shape.dispose();
//    	   tcntr +=1;
//       }
//       triggerBody.setUserData(userData);
//       triggerFixtList = triggerBody.getFixtureList();

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
       
        // Reset the sounds times
        soundTimeGem = 0.0f;
        soundTimeKey = 0.0f;
        soundTimeNitrous = 0.0f;
        soundTimeDoor = 0.0f;
        soundTimeGravity = 0.0f;
        return 0;
    }
    
    @Override
    public void dispose() {
    	GameInputProcessor.Disable(false);
    	if (infoText != null) infoText.dispose();
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
    	if (waterfallBackground != null) waterfallBackground.clear();
    	if (rainBackground != null) rainBackground.clear();
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
    	if (soundBikeIdle != null) soundBikeIdle.dispose();
    	if (soundBikeMove != null) soundBikeMove.dispose();
    	if (soundWaterfall != null) soundWaterfall.dispose();
        if (soundRain != null) soundRain.dispose();
        if (soundWind != null) soundWind.dispose();
    	mScene = null;
    }

    private void renderWorld() {
    	// Render the background Sky
		mBatch.setProjectionMatrix(hudCam.combined);
    	mBatch.begin();
    	mBatch.draw(sky, hudCam.position.x-SCRWIDTH/2, hudCam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        float bgwidth, scaling = 1.0f;
    	if (paintBackdrop) {
            //mBatch.draw(background, bcx-bscale*0.72f, bcy-0.3f, bscale*0.72f, 0.3f, bscale*1.44f, 1.125f, 1.0f, 1.0f, MathUtils.radiansToDegrees*angle);
            bgwidth = (SCRHEIGHT * background.getWidth() / background.getHeight()) / (0.5f + backgroundLimit);
            if (bgwidth < SCRWIDTH) scaling = SCRWIDTH / bgwidth;
            bgwidth *= scaling;
            if (bikeDirc == 1.0f) {
                mBatch.draw(background, hudCam.position.x - SCRWIDTH / 2 - (bgwidth - SCRWIDTH) * (bikeBodyRW.getPosition().x - bounds.x) / (bounds.y - bounds.x), hudCam.position.y - scaling * SCRHEIGHT * backgroundLimit, 0, 0, bgwidth, scaling * SCRHEIGHT * (0.5f + backgroundLimit), 1.0f, 1.0f, 0.0f);
            } else {
                mBatch.draw(background, hudCam.position.x - SCRWIDTH / 2 - (bgwidth - SCRWIDTH) * (bikeBodyLW.getPosition().x - bounds.x) / (bounds.y - bounds.x), hudCam.position.y - scaling * SCRHEIGHT * backgroundLimit, 0, 0, bgwidth, scaling * SCRHEIGHT * (0.5f + backgroundLimit), 1.0f, 1.0f, 0.0f);
            }
        }

        // Render the animated background
        if ((mAnimatedBG != null) && (mAnimatedBG.size > 0)) {
            mPolyBatch.setProjectionMatrix(b2dCam.combined);
            mPolyBatch.begin();
            for (int i = 0; i < mAnimatedBG.size; i++) {
                mAnimatedBG.get(i).render(mPolyBatch, 0);
            }
            mPolyBatch.end();
        }

        if (paintForeground) {
            bgwidth = (scaling*SCRHEIGHT*foreground.getWidth()/foreground.getHeight())/3.0f;
            mBatch.draw(foreground, hudCam.position.x - SCRWIDTH/2 - (bgwidth-SCRWIDTH)*(bikeBodyRW.getPosition().x-bounds.x)/(bounds.y-bounds.x), hudCam.position.y-scaling*SCRHEIGHT/2, 0, 0, bgwidth, scaling*SCRHEIGHT/3.0f, 1.0f, 1.0f, 0.0f);
        }
    	mBatch.end();

    	// Render the collisionless background
        if ((mCollisionlessBG != null) && (mCollisionlessBG.size > 0)) {
        	mPolyBatch.setProjectionMatrix(b2dCam.combined);
            mPolyBatch.begin();
            for (int i = 0; i < mCollisionlessBG.size; i++) {
            	mCollisionlessBG.get(i).render(mPolyBatch, 0);
            }
            mPolyBatch.end();
    	}

        // Render all of the spatials
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
    				gateArr[2] -= 0.0037f;
    				doorImages.set(i, gateArr.clone());
    			}
    		} else {
    			xcen = gateArr[0] + gateArr[2]*((float)Math.cos(gateArr[4])-1)/2 - ObjectVars.objectDoor[4]*B2DVars.EPPM*(float)Math.cos(gateArr[4]);
    			ycen = gateArr[1] + gateArr[2]*(float)Math.sin(gateArr[4])/2 - ObjectVars.objectDoor[5]*B2DVars.EPPM - ObjectVars.objectDoor[4]*B2DVars.EPPM*(float)Math.sin(gateArr[4]);
    			mBatch.draw(openDoor, xcen, ycen, gateArr[2]/2, gateArr[3]/2, gateArr[2], gateArr[3], 1, 1, MathUtils.radiansToDegrees*gateArr[4]);
    			if (gateArr[2] < gateArr[3]/2) {
    				gateArr[2] += 0.0037f;
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

	   	// Render the decorations
	   	if ((mDecors != null) && (mDecors.size > 0))
	   	{
	   		mBatch.begin();
	   		for (int i = 0; i < mDecors.size; i++)
	   		{
	   			mDecors.get(i).render(mBatch);
	   		}
	   		mBatch.end();
	   	}
	
        // Render the colour of the bike
        mBatch.begin();
        float bcx, bcy, angle;
        float bscale = (float)Math.sin(bikeScale*Math.PI/2);
        //if ((bscale == -1.0f) | (bscale == 1.0f)) {
        if (true) {
        	// Render the wheels
        	mBatch.draw(bikeWheel, bikeBodyLW.getPosition().x-0.22f, bikeBodyLW.getPosition().y-0.22f, 0.22f, 0.22f, 0.44f, 0.44f, 1.0f, 1.0f, MathUtils.radiansToDegrees*bikeBodyLW.getAngle());
        	mBatch.draw(bikeWheel, bikeBodyRW.getPosition().x-0.22f, bikeBodyRW.getPosition().y-0.22f, 0.22f, 0.22f, 0.44f, 0.44f, 1.0f, 1.0f, MathUtils.radiansToDegrees*bikeBodyRW.getAngle());
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
        mBatch.setColor(bikeCol[0], bikeCol[1], bikeCol[2], 1);
        mBatch.draw(bikeColour, bcx-bscale*0.72f, bcy-0.3f, bscale*0.72f, 0.3f, bscale*1.44f, 1.125f, 1.0f, 1.0f, MathUtils.radiansToDegrees*angle);
        mBatch.setColor(1, 1, 1, 1);
        bcx = bikeBodyC.getPosition().x;
        bcy = bikeBodyC.getPosition().y;
        angle = bikeBodyC.getAngle();
        // Render the bike overlay and player
        mBatch.draw(bikeOverlay, bcx-bscale*0.72f, bcy-0.3f, bscale*0.72f, 0.3f, bscale*1.44f, 1.125f, 1.0f, 1.0f, MathUtils.radiansToDegrees*angle);
        mBatch.end();

       // Render the ground, grass, rain, and waterfalls
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

	   // Render the collisionless foreground
	   if ((mCollisionlessFG != null) && (mCollisionlessFG.size > 0)) {
		   mPolyBatch.setProjectionMatrix(b2dCam.combined);
	       mPolyBatch.begin();
	       for (int i = 0; i < mCollisionlessFG.size; i++) {
	    	   mCollisionlessFG.get(i).render(mPolyBatch, 0);
	       }
	       mPolyBatch.end();
	   	}

        // Render some items onto the HUD
        renderHUD();

        // If the game is loaded, but paused, render a foreground transparency with text
        if (mState == GAME_STATE.LOADED) {
    	    mBatch.begin();
    	    mBatch.setColor(1, 1, 1, 0.6f);
    	    mBatch.draw(blackScreen, hudCam.position.x-SCRWIDTH/2, hudCam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
    	    String infoString = "Press Enter to begin level\nPress R to restart level\nPress ESC or Q to return to menu\n\n";
            if (mode==2) infoString += LevelsListGame.gameLevelTips[levelID+1];
            infoText.draw(mBatch, infoString, hudCam.position.x-infoWidth/2.0f, hudCam.position.y, infoWidth, Align.center, true);
    	    mBatch.end();
        }
       
        if (debug) b2dr.render(mWorld, b2dCam.combined);
    }

    private void renderHUD()
    {
        mBatch.setProjectionMatrix(hudCam.combined);
        float pThick = 30.0f*HUDScaleFactor;
        mBatch.begin();
        // Draw the shading on the top bar
        //mBatch.draw(panelShadeA, 0, SCRHEIGHT-pThick, 0, 0, 1, 1, BikeGame.V_WIDTH, pThick, 0);
        //mBatch.draw(panelShadeB, 0, SCRHEIGHT-pThick-10, 0, 0, 1, 10, BikeGame.V_WIDTH, 1, 0);
    	//mBatch.draw(panelShadeC, BikeGame.V_WIDTH - timerWidth - 10, SCRHEIGHT-30, 0, 0, 7, 30, 1, 1, 0);
    	//panelContainer.draw(mBatch, BikeGame.V_WIDTH - timerWidth - jcntrWidth - 128.0f*0.2f - 30.0f, SCRHEIGHT-29, 128.0f*0.2f+jcntrWidth+10.0f, 28);
        float vshift = 0.0f;
        // Draw the timer
        String timeStr = GameVars.getTimeString(0);
        if (mState == GAME_STATE.RUNNING) {
            timerCurrent = (int) (TimeUtils.millis()) - timerStart;
            timeStr = GameVars.getTimeString(timerCurrent);
        }
    	timer.draw(mBatch, timeStr, SCRWIDTH-timerWidth-10.0f*HUDScaleFactor, SCRHEIGHT-(pThick-timerHeight)/2.0f);
    	// If this is a replay, don't display anything else on screen.
    	if (isReplay) {
    		mBatch.end();
    		return;
    	}
    	vshift += timerHeight + 5*HUDScaleFactor;
    	// WR
    	timerWR.draw(mBatch, "WR  " + worldRecord, SCRWIDTH-timerWRWidth-10.0f*HUDScaleFactor, SCRHEIGHT-vshift-(pThick-timerWRHeight)/2.0f);
    	vshift += timerWRHeight + 5*HUDScaleFactor;
    	// PB
    	timerPB.draw(mBatch, "PB  " + personalRecord, SCRWIDTH-timerWRWidth-10.0f*HUDScaleFactor, SCRHEIGHT-vshift-(pThick-timerWRHeight)/2.0f);
    	vshift += timerWRHeight + 8*HUDScaleFactor;
    	if (collectJewel != 0) {
	        // Draw the jewel and it's counter
	        jewelCntr.draw(mBatch, String.format("%02d", collectJewel),SCRWIDTH - jcntrWidth - 10.0f*HUDScaleFactor,SCRHEIGHT-vshift-(pThick-jcntrHeight)/2.0f);
	        mBatch.draw(jewelSprite, SCRWIDTH - jcntrWidth + (-128.0f*0.15f - 20.0f)*HUDScaleFactor, SCRHEIGHT-vshift-(pThick-128.0f*0.15f*HUDScaleFactor)/2.0f-128.0f*0.15f*HUDScaleFactor, 0, 0, 128.0f*HUDScaleFactor, 128.0f*HUDScaleFactor, 0.15f, 0.15f, 0);
	        vshift += pThick;
    	}

    	// Draw the key counters
    	if (collectKeyRed != 0) {
            keyRedCntr.draw(mBatch, String.format("%02d", collectKeyRed),     SCRWIDTH - jcntrWidth - 10.0f*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f);
            vshift += 10*HUDScaleFactor;
            mBatch.draw(keyRed,   SCRWIDTH - jcntrWidth + (-135.0f*0.2f - 20.0f)*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f*HUDScaleFactor, 50.0f*HUDScaleFactor, 0.2f, 0.2f, 0);
            vshift += 5*HUDScaleFactor;
    	}
    	if (collectKeyGreen != 0) {
    		keyGreenCntr.draw(mBatch, String.format("%02d", collectKeyGreen), SCRWIDTH - jcntrWidth - 10.0f*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f);
            vshift += 10*HUDScaleFactor;
            mBatch.draw(keyGreen, SCRWIDTH - jcntrWidth + (-135.0f*0.2f - 20.0f)*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f*HUDScaleFactor, 50.0f*HUDScaleFactor, 0.2f, 0.2f, 0);
            vshift += 5*HUDScaleFactor;
    	}
    	if (collectKeyBlue != 0) {
    		keyBlueCntr.draw(mBatch, String.format("%02d", collectKeyBlue),   SCRWIDTH - jcntrWidth - 10.0f*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f);
            vshift += 10*HUDScaleFactor;
            mBatch.draw(keyBlue,  SCRWIDTH - jcntrWidth + (-135.0f*0.2f - 20.0f)*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f*HUDScaleFactor, 50.0f*HUDScaleFactor, 0.2f, 0.2f, 0);
            vshift += 5*HUDScaleFactor;
    	}
    	if (collectKeyRed+collectKeyGreen+collectKeyBlue != 0) vshift -= 5*HUDScaleFactor;

    	// Draw the nitrous counter
    	vshift += 5*HUDScaleFactor;
    	if ((collectNitrous != 0) | (nitrousLevel != 0.0f)) {
	        nitrousCntr.draw(mBatch, String.format("%02d", collectNitrous), SCRWIDTH - jcntrWidth - 10.0f*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f);
	        vshift += 67.5f*0.2f*HUDScaleFactor;
	        // Draw the nitrous image
	        mBatch.draw(nitrous,  SCRWIDTH - jcntrWidth + (-135.0f*0.2f - 20.0f)*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f*HUDScaleFactor, 67.5f*HUDScaleFactor, 0.2f, 0.2f, 0);
	        // Draw nitrous tube and fluid
	        vshift += 10*HUDScaleFactor;
	        mBatch.draw(nitrousFluid,  SCRWIDTH - jcntrWidth + (-135.0f*0.2f - 20.0f)*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, nitrousLevel*135.0f*HUDScaleFactor, 50.0f*HUDScaleFactor, 0.2f, 0.2f, 0);
	        mBatch.draw(nitrousTube,  SCRWIDTH - jcntrWidth + (-135.0f*0.2f - 20.0f)*HUDScaleFactor, SCRHEIGHT-vshift-(pThick/4.0f-jcntrHeight)/2.0f, 0, 0, 135.0f*HUDScaleFactor, 50.0f*HUDScaleFactor, 0.2f, 0.2f, 0);
        }
        mBatch.end();

    }
    /**
     * Creates an array of SimpleSpatial objects from RubeImages.
     * 
     * //@param scene2
     */
    private void createSpatialsFromRubeImages(RubeScene scene)
    {
    	// Render the images
    	Array<RubeImage> images = scene.getImages();
    	float[] transArr = new float[6];
    	if ((images != null) && (images.size > 0))
    	{
    		mSpatials = new Array<SimpleSpatial>();
    		//mDecors = new Array<SimpleImage>();
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
				mTextureMap.put(textureFileName, BikeGameTextures.LoadTexture(FileUtils.getBaseName(textureFileName),0));
//    			} else {
//    				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//    			}
    			spatial = new SimpleSpatial(BikeGameTextures.LoadTexture(FileUtils.getBaseName(textureFileName),0), image.flip, image.body, image.color, mTmp, image.center, image.angleInRads * MathUtils.radiansToDegrees);
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
				mTextureMap.put(textureFileName, BikeGameTextures.LoadTexture(FileUtils.getBaseName(textureFileName),2));
//    			texture = mTextureMap.get(textureFileName);
//    			if (texture == null)
//    			{
//    				texture = new Texture(textureFileName);
//    				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//    				mTextureMap.put(textureFileName, texture);
//    			} else {
//    				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//    			}
    			spatial = new SimpleSpatial(BikeGameTextures.LoadTexture(FileUtils.getBaseName(textureFileName),2), decor.flip, null, decor.color, mTmp, decor.center, decor.angleInRads * MathUtils.radiansToDegrees);
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
       boolean isWF=false, isFG=false, isBG=false, isAnimBG=false, isRN=false;
       
       EarClippingTriangulator ect = new EarClippingTriangulator();

       if ((bodies != null) && (bodies.size > 0))
       {
          mPolySpatials = new Array<PolySpatial>();
          waterfallBackground = new Array<PolySpatial>();
          rainBackground = new Array<PolySpatial>();
          mAnimatedBG = new Array<PolySpatial>();
          mCollisionlessFG = new Array<PolySpatial>();
          mCollisionlessBG = new Array<PolySpatial>();
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
                	  // Reset boolean flags
                	  isWF = false;
                	  isRN = false;
                	  isFG = false;
                	  isBG = false;
                      isAnimBG = false;
                      String testType = (String)scene.getCustom(fixture, "Type", null);
                      int soundID = (int)scene.getCustom(fixture, "Sound", DecorVars.soundNone);
                	  // Grab texture
                      String textureFileName;
                      if (textureName.startsWith("COLOR_")) textureFileName = textureName;
                      else textureFileName = "data/" + textureName;
                      if (textureFileName.equalsIgnoreCase("data/images/waterfall.png")) {
                    	  containsWaterfall = true;
                    	  isWF = true;
                      }
                      if (textureFileName.equalsIgnoreCase("data/images/rain.png")) {
                    	  containsRain = true;
                    	  isRN = true;
                      }
                      if (testType != null) {
                    	  if (testType.equalsIgnoreCase("CollisionlessBG")) isBG = true;
                    	  else if (testType.equalsIgnoreCase("CollisionlessFG")) isFG = true;
                          else if (testType.equalsIgnoreCase("AnimatedBG")) {
                              isAnimBG = true;
                              containsAnimatedBG = true;
                          }
                      }
                      // Setup the texture region
                       TextureRegion textureRegion = null;
                      if (textureFileName.startsWith("COLOR_")) {
                          float[] colarr = ColorUtils.ConvertStringToColor(textureFileName);
                          Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                          pix.setColor(colarr[0], colarr[1], colarr[2], colarr[3]);
                          pix.fill();
                          Texture textureFilled = new Texture(pix);
                          textureRegion = new TextureRegion(textureFilled);
                      } else {
                          texture = mTextureMap.get(textureFileName);
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
                             if (isAnimBG) {
                                mAnimatedBG.add(spatial);
                             } else if (isBG) {
                            	mCollisionlessBG.add(spatial);
                            } else if (isFG) {
                            	mCollisionlessFG.add(spatial);
                            } else mPolySpatials.add(spatial);
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
                            if (isWF) {
                                waterfallVerts.add(vertices.clone());
                                waterfallSounds.add(soundID);
                            }
                            if (isRN) {
                                rainVerts.add(vertices.clone());
                                rainSounds.add(soundID);
                            }
                            short [] triangleIndices = ect.computeTriangles(vertices).toArray();
                            PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
                            PolySpatial spatial = new PolySpatial(region, body, Color.WHITE);
                            if (isAnimBG) {
                               mAnimatedBG.add(spatial);
                            } else if (isBG) {
                               mCollisionlessBG.add(spatial);
                            } else if (isFG) {
                               mCollisionlessFG.add(spatial);
                            } else mPolySpatials.add(spatial);
                            //mPolySpatials.add(spatial);
                         }
                      }
                      else if (fixture.getType() == Shape.Type.Circle)
                      {
                         CircleShape shape = (CircleShape)fixture.getShape();
                         float radius = shape.getRadius();
                         int vertexCount = (int)(100f * radius);
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
                            if (isAnimBG) {
                               mAnimatedBG.add(spatial);
                            } else if (isBG) {
                               mCollisionlessBG.add(spatial);
                            } else if (isFG) {
                               mCollisionlessFG.add(spatial);
                            } else mPolySpatials.add(spatial);
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
