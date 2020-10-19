/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.states;

import static com.mygdx.game.handlers.B2DVars.PPM;

import java.awt.Cursor;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.B2DVars;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.handlers.DecorVars;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameInputProcessor;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.LevelVars;
import com.mygdx.game.handlers.ObjectVars;
import com.mygdx.game.utilities.*;
import com.mygdx.game.utilities.json.JSONException;
import com.mygdx.game.utilities.json.JSONObject;

/**
 *
 * @author rcooke
 */
public class Editor extends GameState {

	private InputMultiplexer inputMultiplexer;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private SpriteBatch mBatch = new SpriteBatch();
	private PolygonSpriteBatch polyBatch = new PolygonSpriteBatch();
	private Sprite traceImage;
	private Sprite decorImage;
	private String[] traceList;
	private String[] nullList = new String[0];
	private String[] itemsXYonly = {"Move X and Y", "Move X only", "Move Y only"};
	private String[] itemsPRC = {"Polygon", "Rectangle", "Circle", "Set Texture", "Set Color"};
	private String[] itemsPRCT = {"Polygon", "Rectangle", "Circle", "Set Color", "Set Texture", "Set Type"};
	private String[] itemsPRCP = {"Polygon", "Rectangle", "Circle", "Set Path", "Set Texture", "Set Color"};
	private String[] itemsADM = {"Add", "Delete", "Move"};
	private String[] itemsADMRSFv = {"Add", "Delete", "Move", "Rotate", "Scale", "Flip x", "Flip y", "Add Vertex", "Delete Vertex", "Move Vertex"};
	private String[] itemsADMR = {"Add", "Delete", "Move", "Rotate"};
	private String[] objectList = {"Ball & Chain", "Boulder", "Bridge", "Crate", "Diamond", "Doors/Keys", "Emerald", "Gate Switch", "Gravity", "Log", "Nitrous", "Pendulum", "Planet", "Spike", "Spike Zone", "Transport", "Transport (invisible)", "Start", "Finish"};
	private String[] decorateList = {"Grass", "Bin Bag", "Sign",
//			"Sign (10)", "Sign (20)", "Sign (30)", "Sign (40)", "Sign (50)", "Sign (60)", "Sign (80)", "Sign (100)", "Sign (Bumps Ahead)", "Sign (Dash)", "Sign (Dot)",
//			"Sign (Do Not Enter)", "Sign (Exclamation)", "Sign (Motorbikes)", "Sign (No Motorbikes)", "Sign (Ramp Ahead)", "Sign (Reduce Speed)", "Sign (Stop)",
			"Rain", "Rock", "Tree", "Tyre Stack", "Waterfall"};
    private String[] levelPropList = {"Gravity", "Ground Texture", "Sky Texture", "Background Texture", "Level Bounds", "Foreground Texture"};
	private String[] groundTextureList = DecorVars.GetPlatformTextures();
	private String[] skyTextureList = {"Blue Sky", "Dusk", "Evening", "Islands", "Mars", "Moon", "Sunrise"};
	private String[] bgTextureList = {"None", "Mountains", "Space", "Waterfall"};
	private String[] fgTextureList = {"None", "Plants", "Trees"};
	private String[] platformTextures = DecorVars.GetPlatformTextures();
	private String[] platformColors = {"Adjust red value", "Adjust green value", "Adjust blue value", "Adjust opacity",
			"Set white", "Set light grey", "Set dark grey", "Set black", "Set red", "Set orange", "Set yellow",
			"Set green", "Set blue", "Set purple", "Set invisible"};
	private float[] platformColor;
	private String[] gravityList = {"Earth", "Moon", "Mars", "Zero"};
	private String[] loadList = {"Load Level", "New Level"};
	private String saveFName;
	private boolean enteringFilename, changesMade;

	private int totalNumMsgs = 20;
	private BitmapFont warnFont, signFont;
	private static GlyphLayout glyphLayout = new GlyphLayout();
	private String[] warnMessage;
	private float[] warnElapse;
	private int[] warnType;
	private float warnTime = 5.0f, warnHeight, toolbarWidth;
	private int warnNumber;

	// Setup the arrays to store new polygons
	private ArrayList<float[]> allPolygons = new ArrayList<float[]>();
	private ArrayList<Integer> allPolygonTypes = new ArrayList<Integer>();
	private ArrayList<float[]> allPolygonPaths = new ArrayList<float[]>();
	private ArrayList<PolygonSprite> allPolygonSprites = new ArrayList<PolygonSprite>();
	private ArrayList<String> allPolygonTextures = new ArrayList<String>();

	// Define the object list
	private ArrayList<float[]> allObjects = new ArrayList<float[]>();
	private ArrayList<float[]> allObjectArrows = new ArrayList<float[]>();
	private ArrayList<Integer> allObjectTypes = new ArrayList<Integer>();
	private ArrayList<float[]> allObjectCoords= new ArrayList<float[]>();

	// Define the decorations list
	private ArrayList<float[]> allDecors = new ArrayList<float[]>();
	private ArrayList<Integer> allDecorTypes = new ArrayList<Integer>();
	private ArrayList<Integer> allDecorPolys = new ArrayList<Integer>();
	private ArrayList<Object> allDecorImages = new ArrayList<Object>();

	// Setup the arrays when copying from alternative level
	private ArrayList<float[]> allPolygons_Alt = new ArrayList<float[]>();
	private ArrayList<Integer> allPolygonTypes_Alt = new ArrayList<Integer>();
	private ArrayList<float[]> allPolygonPaths_Alt = new ArrayList<float[]>();
	private ArrayList<PolygonSprite> allPolygonSprites_Alt = new ArrayList<PolygonSprite>();
	private ArrayList<String> allPolygonTextures_Alt = new ArrayList<String>();
	private ArrayList<float[]> allObjects_Alt = new ArrayList<float[]>();
	private ArrayList<float[]> allObjectArrows_Alt = new ArrayList<float[]>();
	private ArrayList<Integer> allObjectTypes_Alt = new ArrayList<Integer>();
	private ArrayList<float[]> allObjectCoords_Alt= new ArrayList<float[]>();
	private ArrayList<float[]> allDecors_Alt = new ArrayList<float[]>();
	private ArrayList<Integer> allDecorTypes_Alt = new ArrayList<Integer>();
	private ArrayList<Integer> allDecorPolys_Alt = new ArrayList<Integer>();
	private ArrayList<Object> allDecorImages_Alt = new ArrayList<Object>();

	// Decor the groups list
	private ArrayList<float[]> updateGroup;
	private ArrayList<Integer> groupArrays = new ArrayList<Integer>(); // index of allPolygons, allObjects, allDecors
	private ArrayList<Integer> groupPOD = new ArrayList<Integer>(); // Polygon (0), Object (1), or Decor (2)
	private ArrayList<Integer> groupTypes = new ArrayList<Integer>(); // allPolygonTypes, allObjectTypes, allDecorTypes
	private ArrayList<float[]> groupPaths = new ArrayList<float[]>(); // allPolygonPaths, allObjectArrows
	private ArrayList<String> groupTextures = new ArrayList<String>(); // allPolygonTextures
	private ArrayList<float[]> groupCoords = new ArrayList<float[]>(); // allObjectCoords
	private ArrayList<Integer> groupPolys = new ArrayList<Integer>(); // allDecorPolys

	// Define properties of the toolbar
	private Stage stage;
	private Skin skin;
	private boolean hideToolbar = false;
	private boolean setCursor = false;
	private boolean engageDelete = false;
	private boolean clearGrass = false, addGrass = false;
	private float opacity = 1.0f;

	private static final float maxzoom = 1200.0f/B2DVars.EPPM;  // The zoom can be extended to this width
	private static final float maxsize = 1000.0f/B2DVars.EPPM;  // The boundary where polygons can be drawn
	private float posx, posy;
	private float cursposx = 0, cursposy = 0;
	private float SCRWIDTH, SCRHEIGHT;
	private float startX, startY, endX, endY;
	private float nullvarA, nullvarB, nullvarC, nullvarD;
	private Pixmap pixMapPoly;
	private Texture textureFilled;

	private float[] trcImgProp;
	private String jsonLevelString;
	private float[] newPoly = null;
	private float[] updatePoly = null;
	private float[] ghostPoly = null;
	private float[] updatePath = null;
	private float[] updatePathVertex = null;
	private float[] newCoord = new float[2];
	private static final float boundaryX = 1000.0f/B2DVars.EPPM;
	private static final float boundaryY = 1000.0f/B2DVars.EPPM;
	private float[] boundsBG = new float[2];
	private boolean loadedAlt = false;  // If the alternative objects are loaded, set to true, otherwise false
	private boolean drawingPoly = false;  // Is a polygon currently being drawn
	private boolean copyPoly = false;  // Is something currently being copied
	private boolean flipX=false, flipY=false, rotPoly=false;
	private ArrayList<float[]> polyDraw;  // Store the vertices of the new polygon in an ArrayList
	private float[] shapeDraw = null;  // Store the vertices of the new shape
	private float tempx, tempy; // new vertex to be tested
	private static final float polyEndThreshold = 0.01f;
	private ArrayList<Integer> groupPolySelect;
	private ArrayList<float[]> updateGroupPoly;
	private String currentTexture = "";
	private int polySelect = -1, vertSelect = -1, segmSelect = -1;
	private int polyHover = -1, vertHover = -1, segmHover = -1, decorHover=-1, objectHover=-1;
	private int objectSelect = -1, decorSelect = -1, finishObjNumber;
	private int pLevelIndex = 0, pStaticIndex = 0, pKinematicIndex = 0, pFallingIndex = 0, pTriggerIndex = 0, pObjectIndex = 0;
	private boolean triggerSelect = false;
	private int numJewels = 0; // Number of jewels currently inserted
	private int ctype;  // The color of the door/key
	private int tentry; // The transport entry (which entry point is currently being investigated)
	private float xcen, ycen;
	private float[] transPoly = new float[8];
	private float[] startDirPoly = new float[ObjectVars.objectArrow.length];

	// Undo Operations
	private ArrayList<ArrayList<Object>> undoArray = new ArrayList<ArrayList<Object>>();
	private int undoIndex = 0, undoCurrent = 0;
	private final int undoMax = 10;

	// Setup some toolbar buttons
	//private TextButton buttonLoad;
	private SelectBox selectLoadLevel;
	private TextButton buttonSave;
	private TextButton buttonExit;
	private TextButton buttonExecute;
	private TextButton buttonUndo;
	private TextButton buttonRedo;
	private TextButton buttonPan;
	private TextButton buttonTraceImage;
	private TextButton buttonLevelProp;
	private TextButton buttonAddStatic;
	private TextButton buttonAddKinetic;
	private TextButton buttonAddFalling;
	private TextButton buttonAddTrigger;
	private TextButton buttonCopyPaste;
	private TextButton buttonCopyFromLevel;
	private TextButton buttonGroupSelect;
	private TextButton buttonAddObject;
	private TextButton buttonDecorate;
	private TextField textInputSave;
	private List listParent;
	private List listChild;
	private SplitPane splitPaneParent;
	private SplitPane splitPaneChild;
	private ScrollPane scrollPaneTBar;
	private Window windowTBar;

	// Use an integer to identify which mode is currently active
	/* mode:
	 * = 1  -->  pan/zoom
	 * = 2  -->  select polygon
	 * = 3  -->  create static polygon
	 * = 4  -->  create kinetic polygon
	 * = 5  -->  add object
	 * = 6  -->  add decoration
	 */

	// Modes:
	// 1 = Trace Image
	// 2 = Level Properties
	// 3 = static platform
	// 4 = kinetic (moving) platform
	// 5 = Object
	// 6 = Decorate
	// 7 = Falling platform (controlled by 3)
	// 8 = copy/paste
	// 9 = Trigger platform (controlled by 3)  <-- this is the same as a falling platform, but there's a different trigger
	// 10 = Pan/Zoom
	// 11 = Group Select
	// 12 = Copy from another level
	// 999 = Execute level
	// -999 = Load/Save level

	private int mode = 1;
	private String modeParent = "";
	private String modeChild = "";

	//private Matrix4 viewMatrix = new Matrix4();

	public Editor(GameStateManager gsm) {
        super(gsm);
        create();
    }

    public void create() {
    	Gdx.input.setCursorCatched(false);
    	//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
    	// First step is to set the hudCam for rendering messages
		this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		SCRWIDTH = BikeGame.viewport.width;
		SCRHEIGHT = BikeGame.viewport.height;
		GameInputProcessor.SetCrop(BikeGame.viewport.x, BikeGame.viewport.y);
        hudCam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
        hudCam.position.set(SCRWIDTH/2,SCRHEIGHT/2,0);
        //hudCam.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
        hudCam.zoom = 1.0f/(BikeGame.SCALE);
        hudCam.update();
    	// Create the stage for the toolbar
		stage = new Stage();
		// Set the viewport
		stage.getViewport().setScreenX((int) BikeGame.viewport.x);
		stage.getViewport().setScreenY((int) BikeGame.viewport.y);
		stage.getViewport().setScreenWidth((int) (SCRWIDTH));
		stage.getViewport().setScreenHeight((int) SCRHEIGHT);
//		stage.getViewport().update((int) (0.15f*SCRWIDTH), (int) SCRHEIGHT, false);
//		stage.getViewport().getCamera().position.x = BikeGame.viewport.x-SCRWIDTH/2;
//		stage.getViewport().getCamera().position.y = BikeGame.viewport.y-SCRHEIGHT/2;
//		stage.getViewport().getCamera().update();
		//Gdx.input.setInputProcessor(stage);
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(Gdx.input.getInputProcessor());
		Gdx.input.setInputProcessor(inputMultiplexer);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		LevelVars.setDefaults(); // Set the default properties for this level

		// Generate the initial variables of a new level
		ResetLevelDefaults();

		// TODO :: FIX THIS!!
		traceList = EditorIO.LoadTraceImages(new String[] {"None"});

		// Find all available background textures
		String[] bsList = bgTextureList.clone();
		bgTextureList = new String[bsList.length+platformTextures.length-1];
		for (int i=0; i<bsList.length; i++) {
			bgTextureList[i] = bsList[i];
		}
		for (int i=1; i<platformTextures.length; i++) {
			bgTextureList[i+bsList.length-1] = platformTextures[i];
		}

		// Setup the fonts
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/arialbd.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = 16;
		param.color = new Color(Color.WHITE);
		param.minFilter = Texture.TextureFilter.Linear;
		param.magFilter = Texture.TextureFilter.Linear;

		warnFont = gen.generateFont(param);
		param.color = new Color(Color.RED);
		signFont = gen.generateFont(param);
		gen.dispose();

//		warnFont = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
//		signFont = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
		warnMessage = new String[totalNumMsgs];
		warnElapse = new float[totalNumMsgs];
		warnType = new int[totalNumMsgs];
		warnFont.setColor(1, 0.5f, 0, 1);
		warnFont.getData().setScale(1);
		glyphLayout.setText(warnFont, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		warnHeight = 1.2f*glyphLayout.height;
		signFont.setColor(1, 0, 0, 1);
		signFont.getData().setScale(1.5f);

		//buttonLoad = new TextButton("Load", skin);
		selectLoadLevel = new SelectBox(skin);
		buttonSave = new TextButton("Save", skin);
		textInputSave = new TextField("", skin);
		buttonExit = new TextButton("Main Menu", skin);
		buttonExecute = new TextButton("Execute", skin);
		buttonUndo = new TextButton("Undo", skin);
		buttonRedo = new TextButton("Redo", skin);
		buttonPan = new TextButton("Pan", skin);
		buttonLevelProp = new TextButton("Level Properties", skin);
		buttonTraceImage = new TextButton("Trace Image", skin);
		buttonCopyPaste = new TextButton("Copy and Paste", skin);
		buttonCopyFromLevel = new TextButton("Copy from level", skin);
		buttonGroupSelect = new TextButton("Group Select", skin);
		buttonAddStatic = new TextButton("Platform", skin);
		buttonAddKinetic = new TextButton("Moving Platform", skin);
		buttonAddFalling = new TextButton("Falling Platform", skin);
		buttonAddTrigger = new TextButton("Trigger Platform", skin);
		buttonAddObject = new TextButton("Object", skin);
		buttonDecorate = new TextButton("Decorate", skin);
		buttonLevelProp.setChecked(true);

//		float a = 10.0f;
//		float b = -75.0f;
//		float c = 10.0f;
//		updatePoly = new float[1000];
//		for (int i=0; i<250; i++) {
//			updatePoly[2*i] = (a + b*i/c) * (float) Math.cos(i/c);
//			updatePoly[2*i+1] = (a + b*i/c) * (float) Math.sin(i/c);
//			updatePoly[1000 - 2*i - 2] = (2.0f*a + b*i/c) * (float) Math.cos(i/c);
//			updatePoly[1000 - 2*i - 1] = (2.0f*a + b*i/c) * (float) Math.sin(i/c);
//		}
//		AddPolygon(updatePoly.clone(), 0, 0);

		selectLoadLevel.setItems(EditorIO.LoadLevelNames(loadList));
		selectLoadLevel.setSelectedIndex(0);
		selectLoadLevel.setMaxListCount(0);
		textInputSave.setMaxLength(50);

		listParent = new List(skin);
		listParent.setItems(nullList);
		listParent.getSelection().setMultiple(false);
		ScrollPane scrollPaneParent = new ScrollPane(listParent, skin);
		scrollPaneParent.setFlickScroll(false);
		scrollPaneParent.setSmoothScrolling(true);
		scrollPaneParent.setScrollBarPositions(false, true);
		scrollPaneParent.setHeight(scrollPaneParent.getPrefHeight());
		//splitPaneParent = new SplitPane(scrollPaneP, scrollPaneP, true, skin, "default-horizontal");
		//splitPaneParent.setHeight(100.0f);

		listChild = new List(skin);
		listChild.setItems(nullList);
		listChild.getSelection().setMultiple(false);
		ScrollPane scrollPaneChild = new ScrollPane(listChild, skin);
		scrollPaneChild.setFlickScroll(false);
		scrollPaneChild.setSmoothScrolling(true);
		scrollPaneChild.setScrollBarPositions(false, true);
		scrollPaneChild.setHeight(scrollPaneChild.getPrefHeight());
		//splitPaneChild = new SplitPane(scrollPaneC, scrollPaneC, true, skin, "default-horizontal");
		//splitPaneParent.setHeight(100.0f);

		// TODO :: Need to print messages correctly (e.g. click Copy-Paste - the text looks wonky)
		windowTBar = new Window("Toolbar", skin);
		windowTBar.align(Align.top | Align.center);
		windowTBar.getTitleLabel().setAlignment(Align.center);
		windowTBar.setPosition(BikeGame.viewport.x, BikeGame.viewport.y, Align.left);
		//window.setPosition(0, 0);
		windowTBar.defaults().spaceBottom(3);
		windowTBar.row().fill().expandX().colspan(2);
		//window.add(buttonLoad);
		windowTBar.add(buttonExit);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(selectLoadLevel);
		windowTBar.row().fill().colspan(1);
		windowTBar.add(buttonSave,textInputSave);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonExecute);
		windowTBar.row().fill().expandX().colspan(1);
		windowTBar.add(buttonRedo, buttonUndo);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonLevelProp);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonPan);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonTraceImage);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonCopyPaste);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonCopyFromLevel);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonGroupSelect);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonAddStatic);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonAddKinetic);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonAddFalling);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonAddTrigger);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonAddObject);
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.add(buttonDecorate);
		float height = windowTBar.getHeight();
		windowTBar.row().fill().align(Align.left).colspan(2);
		windowTBar.add(scrollPaneParent).minHeight(140).maxHeight((SCRHEIGHT-height)/2).maxWidth(windowTBar.getPrefWidth());
		windowTBar.row().fill().align(Align.left).colspan(2);
		windowTBar.add(scrollPaneChild).minHeight(140).maxHeight((SCRHEIGHT-height)/2).maxWidth(windowTBar.getPrefWidth());
		windowTBar.row().fill().expandX().colspan(2);
		windowTBar.pack();
		windowTBar.setHeight(SCRHEIGHT);
//		scrollPaneTBar = new ScrollPane(windowTBar, skin);
//		scrollPaneTBar.setFlickScroll(false);
//		scrollPaneTBar.setSmoothScrolling(true);
//		scrollPaneTBar.setScrollBarPositions(false, false);
//		//scrollPaneTBar.setScrollingDisabled(true, false);
//		scrollPaneTBar.setHeight(SCRHEIGHT);
//		scrollPaneTBar.setWidth((windowTBar.getPrefWidth()+2));
		toolbarWidth = (windowTBar.getPrefWidth()+2)/BikeGame.SCALE;
		//stage.addActor(scrollPaneTBar);
		stage.addActor(windowTBar);
		// Hover over the toolbar
//		scrollPaneTBar.addListener(new ChangeListener() {
		windowTBar.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (!hideToolbar) {
					if (!drawingPoly) ResetHoverSelect();
				}
			}
		});

		// Setup the listeners for the toolbar menu
		selectLoadLevel.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						selectLoadLevel.setItems(EditorIO.LoadLevelNames(loadList));
						if (!drawingPoly) {
							UncheckButtons(false);
							listParent.setItems(nullList);
							SetChildList();
							mode = -999;
						}
					}
				}
			}
		});
		selectLoadLevel.addListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			public void changed (ChangeEvent event, Actor actor) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						if (selectLoadLevel.getSelectedIndex() != 0) {
							if (changesMade) {
								Message("Changes made since last save!", 1);
								Message("Selecting 'Load Level' again may overwrite these changes", 1);
								changesMade = false;
								SaveLevel(true); // Autosave
								selectLoadLevel.setSelectedIndex(0);
							} else {
								if (selectLoadLevel.getSelectedIndex() == 1) {
									// Refresh the canvas - A new level is being created
									System.out.println("LEVEL WAS RESET 01!");
									ResetLevelDefaults();
									selectLoadLevel.setSelectedIndex(0);
									textInputSave.setText("");
								} else {
									ArrayList<Object> loadedArray = EditorIO.loadLevel((String) selectLoadLevel.getSelected()+".lvl");
									allPolygons = (ArrayList<float[]>) loadedArray.get(0);
									allPolygonTypes = (ArrayList<Integer>) loadedArray.get(1);
									allPolygonPaths = (ArrayList<float[]>) loadedArray.get(2);
									allPolygonTextures = (ArrayList<String>) loadedArray.get(3);
									allObjects = (ArrayList<float[]>) loadedArray.get(4);
									allObjectArrows = (ArrayList<float[]>) loadedArray.get(5);
									allObjectCoords = (ArrayList<float[]>) loadedArray.get(6);
									allObjectTypes = (ArrayList<Integer>) loadedArray.get(7);
									allDecors = (ArrayList<float[]>) loadedArray.get(8);
									allDecorTypes = (ArrayList<Integer>) loadedArray.get(9);
									allDecorPolys = (ArrayList<Integer>) loadedArray.get(10);
									allDecorImages = (ArrayList<Object>) loadedArray.get(11);
									String[] setLVs = (String[]) loadedArray.get(12);
									for (int i=0; i<setLVs.length; i++) LevelVars.set(i, setLVs[i]);
									// Initialise the PolygonSprites
									allPolygonSprites = new ArrayList<PolygonSprite>();
									for (int i=0; i<allPolygons.size(); i++) {
										allPolygonSprites.add(null);
										if ((allPolygonTextures.get(i).startsWith("COLOR_")) && (allPolygonTypes.get(i)%2==0)) {
											MakePolygonSprite(i);
										}
										// TEMPORARY - DELETE THIS!
//										mode = 4;
//										if ((allPolygonTypes.get(i)==2) && (allPolygons.get(i).length!=8)) {
//											cursposx = allPolygonPaths.get(i)[4];
//											cursposy = allPolygonPaths.get(i)[5];
//											ScalePolygon(i, 0.5f);
//											UpdatePolygon(i, false);
//										}
									}
									polySelect=-1;
									// Temporary
//									System.out.println("ERROR - DELETE THIS!!!");
//									for (int i=0; i<allPolygons.size(); i++) {
//										if (allPolygonTypes.get(i) == 2) {
//											float[] tmp = allPolygonPaths.get(i).clone();
//											tmp[0] *= 2;
//											tmp[1] *= 2;
//											allPolygonPaths.set(i, tmp.clone());
//										}
//									}
									// Temporary for Waterfalls/Rain
//									for (int i=0; i<allDecors.size(); i++) {
//										if (((allDecorTypes.get(i) == DecorVars.Waterfall) | (allDecorTypes.get(i) == DecorVars.Rain)) && (allDecors.get(i).length==8)) {
//											float[] tmp = new float[9];
//											for (int jj=0; jj<4; jj++) {
//												tmp[2*jj] = allDecors.get(i)[2*jj];
//												tmp[2*jj+1] = allDecors.get(i)[2*jj+1];
//											}
//											tmp[8] = 1;
//											allDecors.set(i, tmp.clone());
//										}
//									}
									// Restore the original settings of this level
									RestoreLevelDefaults();
									warnMessage[warnNumber] = "Level '"+selectLoadLevel.getSelected()+"' loaded successfully";
									warnElapse[warnNumber] = 0.0f;
									warnType[warnNumber] = 0;
									warnNumber += 1;
									selectLoadLevel.setSelectedIndex(0);
									// Calculate the number of jewels
								}
							}
						}
					}
					//selectLoadLevel.setItems(loadList);
				}
//				String levelName = "";
//				ArrayList<Object> loadedArray = EditorIO.loadLevel(levelName);
//				allPolygons = (ArrayList<float[]>) loadedArray.get(0);
//				allPolygonTypes = (ArrayList<Integer>) loadedArray.get(1);
//				allPolygonPaths = (ArrayList<float[]>) loadedArray.get(2);
//				allObjects = (ArrayList<float[]>) loadedArray.get(3);
//				allObjectArrows = (ArrayList<float[]>) loadedArray.get(4);
//				allObjectTypes = (ArrayList<Integer>) loadedArray.get(5);
//				String[] setLVs = (String[]) loadedArray.get(6);
//				for (int i=0; i<setLVs.length; i++) LevelVars.set(i, setLVs[i]);
			}
		});

//		buttonLoad.addListener(new ClickListener() {
//			public void clicked (InputEvent event, float x, float y) {
//				if (!drawingPoly) {
//					UncheckButtons(false);
//					listParent.setItems(nullList);
//					SetChildList();
//					mode = -999;
//				}
//				String levelName = "";
//				ArrayList<Object> loadedArray = EditorIO.loadLevel(levelName);
//				allPolygons = (ArrayList<float[]>) loadedArray.get(0);
//				allPolygonTypes = (ArrayList<Integer>) loadedArray.get(1);
//				allPolygonPaths = (ArrayList<float[]>) loadedArray.get(2);
//				allObjects = (ArrayList<float[]>) loadedArray.get(3);
//				allObjectArrows = (ArrayList<float[]>) loadedArray.get(4);
//				allObjectTypes = (ArrayList<Integer>) loadedArray.get(5);
//				String[] setLVs = (String[]) loadedArray.get(6);
//				for (int i=0; i<setLVs.length; i++) LevelVars.set(i, setLVs[i]);
//			}
//		});

		buttonUndo.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						UncheckButtons(false);
						listParent.setItems(nullList);
						SetChildList();
						mode = -999;
						Undo();
					}
				}
			}
		});

		buttonRedo.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						UncheckButtons(false);
						listParent.setItems(nullList);
						SetChildList();
						mode = -999;
						Redo();
					}
				}
			}
		});

		buttonSave.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						// Set the number of emeralds
						SetEmeralds();
						UncheckButtons(false);
						listParent.setItems(nullList);
						SetChildList();
						mode = -999;
						SaveLevel(false);
					}
				}
//				try {
//					//jsonLevelString = EditorIO.serialize(allPolygons,allPolygonTypes,allPolygonPaths,allObjects,allObjectArrows,allObjectTypes);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				System.out.println(jsonLevelString);
			}
		});

		textInputSave.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						enteringFilename = true;
						UncheckButtons(true);
					}
				}
			}
		});

		buttonExit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						if (!changesMade) {
							gsm.setState(GameStateManager.PEEK, false, "none", -1, 0);
						} else {
							Message("Changes made since last save!", 1);
							Message("Selecting 'Main Menu' again will exit without saving", 1);
							SaveLevel(true); // Autosave
							changesMade = false;
						}
						UncheckButtons(false);
					}
				}
			}
		});

		buttonExecute.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Message("Tip: You can also press 'E' to execute the level", 0);
				// Set the number of emeralds
				SetEmeralds();
				ExecuteLevel();
			}
		});

		buttonTraceImage.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 1;
						UncheckButtons(false);
						buttonTraceImage.setChecked(true);
						listParent.setItems(traceList);
						SetChildList();
						ResetHoverSelect();
					}
				}
			}
		});

		buttonLevelProp.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 2;
						UncheckButtons(false);
						listParent.setItems(levelPropList);
						listParent.setSelectedIndex(pLevelIndex);
						SetChildList();
						ResetHoverSelect();
						buttonLevelProp.setChecked(true);
					}
				}
			}
		});

		buttonPan.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 10;
						UncheckButtons(false);
						buttonPan.setChecked(true);
						listParent.setItems(nullList);
						SetChildList();
						ResetHoverSelect();
					}
				}
			}
		});

		buttonAddStatic.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 3;
						UncheckButtons(false);
						listParent.setItems(itemsPRCT);
						listParent.setSelectedIndex(pStaticIndex);
						SetChildList();
						ResetHoverSelect();
						buttonAddStatic.setChecked(true);
					}
				}
			}
		});

		buttonAddKinetic.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 4;
						UncheckButtons(false);
						listParent.setItems(itemsPRCP);
						listParent.setSelectedIndex(pKinematicIndex);
						SetChildList();
						ResetHoverSelect();
						buttonAddKinetic.setChecked(true);
					}
				}
			}
		});

		buttonAddFalling.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 7;
						UncheckButtons(false);
						listParent.setItems(itemsPRC);
						listParent.setSelectedIndex(pFallingIndex);
						SetChildList();
						ResetHoverSelect();
						buttonAddFalling.setChecked(true);
					}
				}
			}
		});

		buttonAddTrigger.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 9;
						UncheckButtons(false);
						listParent.setItems(itemsPRC);
						listParent.setSelectedIndex(pTriggerIndex);
						SetChildList();
						ResetHoverSelect();
						buttonAddTrigger.setChecked(true);
					}
				}
			}
		});

		buttonCopyPaste.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 8;
						UncheckButtons(false);
						listParent.setItems("Platform", "Object");
						SetChildList();
						ResetHoverSelect();
						buttonCopyPaste.setChecked(true);
						Message("Click an object to select it, then click again to paste a copy of it, or,", 0);
						Message("click and drag over multiple platforms to select multiple platforms.", 0);
						Message("If you want to flip the selection in x and y, press the x and y keys,", 0);
						Message("on the keyboard.  Click again to paste", 0);
					}
				}
			}
		});

		buttonCopyFromLevel.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 12;
						UncheckButtons(false);
						listParent.setItems(EditorIO.LoadLevelNames(new String[] {}));
						SetChildList();
						ResetHoverSelect();
						buttonCopyFromLevel.setChecked(true);
						Message("Select a level from the list to temporarily load it on screen.", 0);
						Message("Click and drag over multiple platforms/objects/decorations that", 0);
						Message("you want to copy from the temporary level into the current level.", 0);
					}
				}
			}
		});

		buttonGroupSelect.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 11;
						UncheckButtons(false);
						listParent.setItems("Copy", "Delete", "Move");
						SetChildList();
						ResetHoverSelect();
						buttonGroupSelect.setChecked(true);
						Message("Select a mode (Move/Copy/Delete) and then click and", 0);
						Message("drag over multiple platforms/objects/decorations", 0);
						Message("Click again to move/paste, press 'd' to confirm delete", 0);
					}
				}
			}
		});

		buttonAddObject.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 5;
						UncheckButtons(false);
						listParent.setItems(objectList);
						listParent.setSelectedIndex(pObjectIndex);
						SetChildList();
						ResetHoverSelect();
						buttonAddObject.setChecked(true);
					}
				}
			}
		});

		buttonDecorate.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						Message("You should only decorate a level when you have finished placing your platforms!", 1);
						Message("Adding/Removing platforms after decorating may produce unexpected results!", 1);
						mode = 6;
						UncheckButtons(false);
						listParent.setItems(decorateList);
						SetChildList();
						ResetHoverSelect();
						buttonDecorate.setChecked(true);
					}
				}
			}
		});

		scrollPaneParent.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					GameInput.MBRELEASE=false;
					if ((!drawingPoly) & (listParent.getSelected() != null)) {
						modeParent = listParent.getSelected().toString();
						SetChildList();
						if (mode == 1) {
							if (modeParent.equalsIgnoreCase("none")) {
								traceImage = null;
								listChild.setItems();
								trcImgProp = new float[] {1000.0f, 1000.0f, 100.0f, 100.0f, 1.0f, 0.0f};
							} else {
								traceImage = new Sprite(new Texture(modeParent));
								float ratio = traceImage.getHeight()/traceImage.getWidth();
								trcImgProp = new float[] {1000.0f, 1000.0f, 100.0f, 100.0f*ratio, 1.0f, 0.0f};
							}
						} else if (mode == 12) {
							ArrayList<Object> loadedArray = EditorIO.loadLevel((String) modeParent+".lvl");
							allPolygons_Alt = (ArrayList<float[]>) loadedArray.get(0);
							allPolygonTypes_Alt = (ArrayList<Integer>) loadedArray.get(1);
							allPolygonPaths_Alt = (ArrayList<float[]>) loadedArray.get(2);
							allPolygonTextures_Alt = (ArrayList<String>) loadedArray.get(3);
							allObjects_Alt = (ArrayList<float[]>) loadedArray.get(4);
							allObjectArrows_Alt = (ArrayList<float[]>) loadedArray.get(5);
							allObjectCoords_Alt = (ArrayList<float[]>) loadedArray.get(6);
							allObjectTypes_Alt = (ArrayList<Integer>) loadedArray.get(7);
							allDecors_Alt = (ArrayList<float[]>) loadedArray.get(8);
							allDecorTypes_Alt = (ArrayList<Integer>) loadedArray.get(9);
							allDecorPolys_Alt = (ArrayList<Integer>) loadedArray.get(10);
							allDecorImages_Alt = (ArrayList<Object>) loadedArray.get(11);
							// Initialise the PolygonSprites
							allPolygonSprites_Alt = new ArrayList<PolygonSprite>();
							for (int i=0; i<allPolygons_Alt.size(); i++) {
								allPolygonSprites_Alt.add(null);
							}
							loadedAlt = true;
						}
						ResetHoverSelect();
						polySelect = -1;
						updatePathVertex = null;
					}
				}
			}
		});

		scrollPaneChild.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					GameInput.MBRELEASE=false;
					if ((listParent.getItems().size != 0) & ((listChild.getItems().size != 0))) {
						modeParent = listParent.getSelected().toString();
						// Flip the starting direction every time this option is clicked
						if ((mode==5) & (modeParent.equals("Start")) & (listChild.getSelected().toString().equals("Flip Direction"))) {
							if (LevelVars.get(LevelVars.PROP_START_DIRECTION).equals("Left")) LevelVars.set(LevelVars.PROP_START_DIRECTION, "Right");
							else LevelVars.set(LevelVars.PROP_START_DIRECTION, "Left");
						} else if ((mode==4) & (modeParent.equals("Set Path")) & (listChild.getSelected().toString().equals("Flip Direction")) & (polySelect != -1)) {
							float[] tempArr = allPolygonPaths.get(polySelect).clone();
							tempArr[3] *= -1;
							allPolygonPaths.set(polySelect, tempArr.clone());
						} else if ((mode==4) & (modeParent.equals("Set Path")) & (listChild.getSelected().toString().equals("Flip Rotation")) & (polySelect != -1)) {
							float[] tempArr = allPolygonPaths.get(polySelect).clone();
							tempArr[2] *= -1;
							allPolygonPaths.set(polySelect, tempArr.clone());
						}
						if (listChild.getItems().size != 0) {
							String chldMd = listChild.getSelected().toString();
							if (chldMd.equals("Delete")) {
								Message("First select object, then press 'd' to delete", 0);
							} else if ((modeParent.equals("Polygon")) && (chldMd.equals("Rotate"))) {
								Message("Polygons will rotate about the red circle (a.k.a. the 'cursor')", 0);
								Message("Press 'c' first and then click at the position you want to set the cursor", 0);
							} else if ((modeParent.equals("Polygon")) && (chldMd.equals("Scale"))) {
								Message("Polygons will scale about the red circle (a.k.a. the 'cursor')", 0);
								Message("Press 'c' first and then click at the position you want to set the cursor", 0);
							} else if (chldMd.equals("Rotate")) {
								Message("Click and drag the object to rotate it", 0);
							} else if (chldMd.equals("Delete Vertex")) {
								Message("First select vertex, then press 'd' to delete", 0);
							} else if (chldMd.equals("Delete All Grass")) {
								Message("Press 'd' to delete all grass from the level - you have been warned!", 0);
							} else if (chldMd.equals("Add All Grass")) {
								Message("Press 'd' to add grass to every segment on all platforms - you have been warned!", 0);
							} else if (chldMd.equals("Next Item")) {
								Message("Click on an item of this type to change the property of this item", 0);
							}
						}
					}

					if (!drawingPoly) {
						objectSelect = -1;
						decorSelect = -1;
						if (mode!=4) polySelect = -1;
						vertSelect = -1;
						segmSelect = -1;
						polyHover = -1;
						decorHover = -1;
						objectHover = -1;
						vertHover = -1;
						segmHover = -1;
						updatePathVertex = null;
						tentry = 0;
					}
				}
			}
		});
    }

    private void ExecuteLevel() {
		if (!hideToolbar) {
			if (!drawingPoly) {
				UncheckButtons(false);
				if (!CheckVertInt(false)) {
					// No intersections were found, so let's do some more checks...
					try {
						jsonLevelString = EditorIO.JSONserialize(allPolygons,allPolygonTypes,allPolygonPaths,allPolygonTextures,allObjects,allObjectArrows,allObjectCoords,allObjectTypes,allDecors,allDecorTypes,allDecorPolys,allDecorImages);
						try (FileWriter file = new FileWriter("tst.json")) {
							JSONObject json = new JSONObject(jsonLevelString); // Convert text to object
							file.write(json.toString(4));
				            file.flush();
						} catch (IOException e) {
							System.out.println(e);
						} catch (JSONException e) {
							System.out.println(e);
						}
						if (jsonLevelString.equalsIgnoreCase("GRASS_ERROR")) {
							Message("Level is not playable!", 2);
							Message("There is a problem with the grass placement", 2);
							Message("Try clearing the grass and execute the level again", 0);
							Message("Remember to add grass only when the level is finished", 0);
						} else if (jsonLevelString.startsWith("CU")) {
							Message("Unable to play level!", 2);
							try {
								int gotoPoly = Integer.parseInt(jsonLevelString.split(" ")[1]);
								int idxcls=0;
								float clsdist = 999999999.9f, dist=0.0f;
								if (jsonLevelString.split(" ")[2].equals("P")) {
									Message("Two vertices in this polygon are too close together", 1);
									for (int i=1; i<allPolygons.get(gotoPoly).length/2; i++) {
										dist = (float) Math.sqrt((Math.pow(allPolygons.get(gotoPoly)[2*i]-allPolygons.get(gotoPoly)[2*i-2], 2) +
												Math.pow(allPolygons.get(gotoPoly)[2*i+1]-allPolygons.get(gotoPoly)[2*i-1], 2)));
										if (dist < clsdist) {
											clsdist = dist;
											idxcls = i;
										}
									}
									MoveCameraTo(allPolygons.get(gotoPoly)[2*idxcls], allPolygons.get(gotoPoly)[2*idxcls+1], true);
								} else if (jsonLevelString.split(" ")[2].equals("D")) {
									Message("Two vertices in the grass are too close together", 1);
									for (int i=0; i<allDecors.get(gotoPoly).length/2; i++) {
										xcen += allDecors.get(gotoPoly)[2*i];
										ycen += allDecors.get(gotoPoly)[2*i+1];
									}
									xcen = xcen/(float) (allDecors.get(gotoPoly).length/2);
									ycen = ycen/(float) (allDecors.get(gotoPoly).length/2);
									MoveCameraTo(xcen,ycen,true);
								} else if (jsonLevelString.split(" ")[2].equals("Pl")) {
									Message("This planet is too small - Two vertices in the polygon are too close together", 1);
									for (int i=1; i<allPolygons.get(gotoPoly).length/2; i++) {
										dist = (float) Math.sqrt((Math.pow(allPolygons.get(gotoPoly)[2*i]-allPolygons.get(gotoPoly)[2*i-2], 2) +
												Math.pow(allPolygons.get(gotoPoly)[2*i+1]-allPolygons.get(gotoPoly)[2*i-1], 2)));
										if (dist < clsdist) {
											clsdist = dist;
											idxcls = i;
										}
									}
									MoveCameraTo(allPolygons.get(gotoPoly)[2*idxcls], allPolygons.get(gotoPoly)[2*idxcls+1], true);
								}
							} catch (Exception e) {}
						} else if (jsonLevelString.startsWith("BD")) {
							Message("Unable to play level!", 2);
							try {
								int gotoPoly = Integer.parseInt(jsonLevelString.split(" ")[1]);
								int idxcls=0;
								float clsdist = 999999999.9f, dist=0.0f;
								if (jsonLevelString.split(" ")[2].equals("P")) {
									Message("Cannot decompose this polygon", 1);
									Message("Please check there are no overlapping vertices, intersections, or very small segments", 1);
									for (int i=1; i<allPolygons.get(gotoPoly).length/2; i++) {
										dist = (float) Math.sqrt((Math.pow(allPolygons.get(gotoPoly)[2*i]-allPolygons.get(gotoPoly)[2*i-2], 2) +
												Math.pow(allPolygons.get(gotoPoly)[2*i+1]-allPolygons.get(gotoPoly)[2*i-1], 2)));
										if (dist < clsdist) {
											clsdist = dist;
											idxcls = i;
										}
									}
									MoveCameraTo(allPolygons.get(gotoPoly)[2*idxcls], allPolygons.get(gotoPoly)[2*idxcls+1], true);
								} else if (jsonLevelString.split(" ")[2].equals("D")) {
									Message("Cannot decompose this decoration", 1);
									Message("Please check there are no overlapping vertices, intersections, or very small segments", 1);
									for (int i=0; i<allDecors.get(gotoPoly).length/2; i++) {
										xcen += allDecors.get(gotoPoly)[2*i];
										ycen += allDecors.get(gotoPoly)[2*i+1];
									}
									xcen = xcen/(float) (allDecors.get(gotoPoly).length/2);
									ycen = ycen/(float) (allDecors.get(gotoPoly).length/2);
									MoveCameraTo(xcen,ycen,true);
								}
							} catch (Exception e) {}
						} else if (jsonLevelString.startsWith("CA")) {
							Message("Unable to play level!", 2);
							try {
								int gotoPoly = Integer.parseInt(jsonLevelString.split(" ")[1]);
								int idxcls=0;
								float clsdist = 999999999.9f, dist=0.0f;
								if (jsonLevelString.split(" ")[2].equals("P")) {
									Message("Two vertices in this polygon are too close together (generating a small area)", 1);
									Message("Try separating these vertices", 1);
									for (int i=1; i<allPolygons.get(gotoPoly).length/2; i++) {
										dist = (float) Math.sqrt((Math.pow(allPolygons.get(gotoPoly)[2*i]-allPolygons.get(gotoPoly)[2*i-2], 2) +
												Math.pow(allPolygons.get(gotoPoly)[2*i+1]-allPolygons.get(gotoPoly)[2*i-1], 2)));
										if (dist < clsdist) {
											clsdist = dist;
											idxcls = i;
										}
									}
									MoveCameraTo(allPolygons.get(gotoPoly)[2*idxcls], allPolygons.get(gotoPoly)[2*idxcls+1], true);
								} else if (jsonLevelString.split(" ")[2].equals("D")) {
									Message("Two vertices in this decoration are too close together (generating a small area)", 1);
									Message("Try separating these vertices", 1);
									for (int i=0; i<allDecors.get(gotoPoly).length/2; i++) {
										xcen += allDecors.get(gotoPoly)[2*i];
										ycen += allDecors.get(gotoPoly)[2*i+1];
									}
									xcen = xcen/(float) (allDecors.get(gotoPoly).length/2);
									ycen = ycen/(float) (allDecors.get(gotoPoly).length/2);
									MoveCameraTo(xcen,ycen,true);
								}
							} catch (Exception e) {}
						} else {
							enteringFilename = false;
							stage.setKeyboardFocus(null);
							hideToolbar = true;
							gsm.SetPlaying(true);
							// Now launch the level!
							gsm.setState(GameStateManager.PLAY, true, jsonLevelString, -1, 0);
						}
					} catch (JSONException e) {
						e.printStackTrace();
//					} catch (IndexOutOfBoundsException e) {
//						Message("A polygon in this level cannot be converted (an index was found out of bounds)", 2);
//						Message("It is possible two vertices are too close together", 1);
					}
				}
		    	// When we come back from the level, make sure we reset the hudCam (used for messages)
				this.game.resize(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
				SCRWIDTH = BikeGame.viewport.width;
				SCRHEIGHT = BikeGame.viewport.height;
		        hudCam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
		        hudCam.position.set(SCRWIDTH/2,SCRHEIGHT/2,0);
		        //hudCam.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
		        hudCam.zoom = 1.0f/(BikeGame.SCALE);
		        hudCam.update();
		    	// Reset the input processor
				Gdx.input.setInputProcessor(inputMultiplexer);
				listParent.setItems(nullList);
				SetChildList();
				ResetHoverSelect();
				mode = 999;
			}
		}
    }

    public void ResetSelect() {
    	polySelect = -1;
    	vertSelect = -1;
    	segmSelect = -1;
    	polyHover = -1;
    	decorHover = -1;
    	objectHover = -1;
    	vertHover = -1;
    	segmHover = -1;
    	objectSelect = -1;
    	decorSelect = -1;
    	currentTexture = "";
	}

	public void ResetGroups() {
		groupArrays = new ArrayList<Integer>(); // index of allPolygons, allObjects, allDecors
		groupPOD = new ArrayList<Integer>(); // Polygon (0), Object (1), or Decor (2)
		groupTypes = new ArrayList<Integer>(); // allPolygonTypes, allObjectTypes, allDecorTypes
		groupPaths = new ArrayList<float[]>(); // allPolygonPaths, allObjectArrows
		groupTextures = new ArrayList<String>(); // allPolygonTextures
		groupCoords = new ArrayList<float[]>(); // allObjectCoords
		groupPolys = new ArrayList<Integer>(); // allDecorPolys
		updateGroup = null;
		copyPoly = false;
		engageDelete = false;
		addGrass = false;
		clearGrass = false;
	}

	private void ResetAltObjects() {
		// Prepare storage arrays
		allObjects_Alt = new ArrayList<float[]>();
		allObjectArrows_Alt = new ArrayList<float[]>();
		allObjectTypes_Alt = new ArrayList<Integer>();
		allObjectCoords_Alt = new ArrayList<float[]>();
		allPolygons_Alt = new ArrayList<float[]>();
		allPolygonTypes_Alt = new ArrayList<Integer>();
		allPolygonPaths_Alt = new ArrayList<float[]>();
		allPolygonTextures_Alt = new ArrayList<String>();
		allPolygonSprites_Alt = new ArrayList<PolygonSprite>();
		allDecors_Alt = new ArrayList<float[]>();
		allDecorTypes_Alt = new ArrayList<Integer>();
		allDecorPolys_Alt = new ArrayList<Integer>();
		allDecorImages_Alt = new ArrayList<Object>();
	}

    public void ResetLevelDefaults() {
		System.out.println("LEVEL WAS RESET!");
    	// Prepare storage arrays
		allObjects = new ArrayList<float[]>();
		allObjectArrows = new ArrayList<float[]>();
		allObjectTypes = new ArrayList<Integer>();
		allObjectCoords= new ArrayList<float[]>();
    	allPolygons = new ArrayList<float[]>();
    	allPolygonTypes = new ArrayList<Integer>();
    	allPolygonPaths = new ArrayList<float[]>();
    	allPolygonTextures = new ArrayList<String>();
		allPolygonSprites = new ArrayList<PolygonSprite>();
    	allDecors = new ArrayList<float[]>();
    	allDecorTypes = new ArrayList<Integer>();
		allDecorPolys = new ArrayList<Integer>();
		allDecorImages = new ArrayList<Object>();
		ResetAltObjects();
		updateGroup = null;
		groupArrays = new ArrayList<Integer>(); // index of allPolygons, allObjects, allDecors
		groupPOD = new ArrayList<Integer>(); // Polygon (0), Object (1), or Decor (2)
		groupTypes = new ArrayList<Integer>(); // allPolygonTypes, allObjectTypes, allDecorTypes
		groupPaths = new ArrayList<float[]>(); // allPolygonPaths, allObjectArrows
		groupTextures = new ArrayList<String>(); // allPolygonTextures
		groupCoords = new ArrayList<float[]>(); // allObjectCoords
		groupPolys = new ArrayList<Integer>(); // allDecorPolys
    	// Prepare undo array (need to reset this after adding start/finish/diamond)
    	undoArray = new ArrayList<ArrayList<Object>>();
    	for (int i=0; i<undoMax; i++) undoArray.add(null);
    	undoIndex=-1;
    	undoCurrent=-1;
    	// Reset update parameters
    	newPoly = null;
    	updatePoly = null;
    	updateGroupPoly = null;
    	updatePath = null;
    	updatePathVertex = null;
    	newCoord = new float[2];
    	boundsBG = new float[] {0.0f, boundaryX};
    	traceImage = null;
    	trcImgProp = new float[] {1000.0f, 1000.0f, 100.0f, 100.0f, 1.0f, 0.0f};  // x, y, width, height, scale, rotation
    	flipX=false;
    	flipY=false;
    	rotPoly=false;
		loadedAlt = false;
    	drawingPoly = false;  // Is a polygon currently being drawn
    	copyPoly = false;  // Is something currently being copied
    	shapeDraw = null;  // Store the vertices of the new shape
    	groupPolySelect = new ArrayList<Integer>();
    	ResetSelect();
    	pLevelIndex = 0;
    	pStaticIndex = 0;
    	pKinematicIndex = 0;
    	pObjectIndex = 0;
    	numJewels = 0; // Number of jewels currently inserted (1=Diamond jewel)
    	transPoly = new float[8];
    	startDirPoly = new float[ObjectVars.objectArrow.length];
		platformColor = new float[4];

    	// Use an integer to identify which mode is currently active
    	/* mode:
    	 * = 1  -->  pan/zoom
    	 * = 2  -->  select polygon
    	 * = 3  -->  create static polygon
    	 * = 4  -->  create kinetic polygon
    	 * = 5  -->  add object
    	 * = 6  -->  add decoration
    	 */
    	mode = 1;
    	modeParent = "";
    	modeChild = "";
		// Create the start and finish buttons, and add them to the canvas
		// Initial Gravity
		newPoly = ObjectVars.objectGravity.clone();
		ShiftObject(1100.0f, 1000.0f);
		AddObject(ObjectVars.Gravity, 1100.0f, 1000.0f, 0.0f);
		// Start Object
		newPoly = ObjectVars.objectStart.clone();
		ShiftObject(1000.0f, 1000.0f);
		AddObject(ObjectVars.Start, 1000.0f, 1000.0f, 90.0f);
		// The Diamond Jewel
		newPoly = ObjectVars.objectJewel.clone();
		ShiftObject(1200.0f, 1000.0f);
		AddObject(ObjectVars.Jewel, 1200.0f, 1000.0f, -999.9f);
		// If new objects are to be added, do it before finish
		//
		// Finish Object
		finishObjNumber = 3; // WARNING :: Make sure this number is the same as that specified in EditorIO
		newPoly = ObjectVars.objectFinish.clone();
		ShiftObject(900.0f, 1000.0f);
		AddObject(ObjectVars.Finish, 900.0f, 1000.0f, -999.9f);
		// By default, make the saving filename field disabled 
		enteringFilename = false;
		saveFName = null;
		changesMade = false;
		// Reset undo array
		for (int i=0; i<undoMax; i++) undoArray.set(i, null);
		undoIndex = -1;
		undoCurrent = -1;
    	UpdateUndo();  // Need to add the empty 'allBlah' arraylists to begin with - Needs to be -1 so the first index is the basic level load
		// Set the starting position of the camera
		// Perform Zoom
		cam.zoom = 0.1f/B2DVars.EPPM;
		//cam.lookAt(1000.0f/B2DVars.EPPM, 1000.0f/B2DVars.EPPM, 0.0f);
    }

    public void RestoreLevelDefaults() {
		System.out.println("Restoring level defaults!");
    	// Set the level name in the save textbox
		saveFName = FileUtils.getBaseName((String) selectLoadLevel.getSelected());
		textInputSave.setText(saveFName);
		// Determine the number of jewels in the level
		numJewels = 0;
		for (int i=finishObjNumber; i<allObjectTypes.size(); i++) {
			if (allObjectTypes.get(i) == ObjectVars.Jewel) numJewels += 1;
		}
		//
    }

    public boolean CheckVertInt(boolean autosave) {
		// Check for vertices that are too close
		float[] chkVertices = PolygonOperations.CheckVertexSizes(allPolygons, allPolygonTypes, allDecors, allDecorTypes);
		// Check for intersecting segments
		float[] chkIntsct = PolygonOperations.CheckIntersections(allPolygons, allPolygonTypes, allDecors, allDecorTypes);
		if (chkVertices!=null) {
			if (autosave) return true;
			// Go to the intersection
			MoveCameraTo(chkVertices[0],chkVertices[1],true);
			// Print a warning message
			Message("Level not saved/executed -- Two vertices are too close together (maybe a duplicate?)", 2);
			Message("Fix the polygon first, and then save the level", 1);
			return true;
		} else if (chkIntsct!=null) {
			if (autosave) return true;
			// Go to the intersection
			MoveCameraTo(chkIntsct[0],chkIntsct[1],true);
			// Print a warning message
			Message("Level not saved/executed -- There was an intersection between these segments", 2);
			Message("Fix the polygon first, and then save the level", 1);
			return true;
		}
    	return false;
    }

    public void handleInput() {
		if (GameInput.isPressed(GameInput.KEY_T)) hideToolbar = !hideToolbar;
		if (GameInput.isPressed(GameInput.KEY_C)) setCursor = true;
		if (GameInput.isPressed(GameInput.KEY_E)) ExecuteLevel();
		if (GameInput.isPressed(GameInput.KEY_X)) flipX=!flipX;
		if (GameInput.isPressed(GameInput.KEY_Y)) flipY=!flipY;
		if (GameInput.isPressed(GameInput.KEY_R)) rotPoly=true;
//		if (GameInput.isPressed(GameInput.KEY_LEFT)) Undo();
//		if (GameInput.isPressed(GameInput.KEY_RIGHT)) Redo();
		if ((GameInput.isPressed(GameInput.KEY_D)) & ((engageDelete) | (clearGrass) | (addGrass))) {
			if ((mode==4) & (modeParent.equals("Set Path"))) {
				if (vertSelect != -1) {
					if (allPolygonPaths.get(polySelect).length >= 12) {
						DeleteVertexPath(polySelect, vertSelect);
						vertSelect = -1;
						vertHover = -1;
						polyHover = -1;
						decorHover = -1;
						objectHover = -1;
						segmHover = -1;
					} else if (allPolygonPaths.get(polySelect).length == 10) {
						float[] pathArr = new float[6];
						for (int i=0; i<6; i++) pathArr[i] = allPolygonPaths.get(polySelect)[i];
						allPolygonPaths.set(polySelect, pathArr.clone());
						vertSelect = -1;
						vertHover = -1;
						polyHover = -1;
						decorHover = -1;
						objectHover = -1;
						segmHover = -1;
					}
				}
			} else if ((mode == 11) & (engageDelete)) {
				// Group delete
				int cntr_poly = 0;
				int cntr_obj = 0;
				int cntr_decor = 0;
				for (int gg=0; gg<groupArrays.size(); gg++) {
					if (groupPOD.get(gg) == 0) {
						polySelect = groupArrays.get(gg)-cntr_poly;
						DeletePolygon(polySelect, false);
						cntr_poly += 1;
					} else if (groupPOD.get(gg) == 1) {
						objectSelect = groupArrays.get(gg)-cntr_obj;
						DeleteObject(objectSelect, false);
						cntr_obj += 1;
					} else if (groupPOD.get(gg) == 2) {
						decorSelect = groupArrays.get(gg)-cntr_decor;
						DeleteDecor(decorSelect, false);
						cntr_decor += 1;
					}
				}
				Message(cntr_poly + " platforms deleted", 0);
				Message(cntr_obj + " objects deleted", 0);
				Message(cntr_decor + " decorations deleted", 0);
				polySelect = -1;
				objectSelect = -1;
				decorSelect = -1;
				ResetGroups();
				SaveLevel(true);
			} else {
				if (clearGrass) {
					DeleteAllGrass();
					clearGrass = false;
				} else if (addGrass) {
					AddAllGrass();
					addGrass = false;
				} else if (polySelect != -1) {
					if (vertSelect != -1) {
						if (allPolygons.get(polySelect).length <= 6) DeletePolygon(polySelect, true);
						else DeleteVertex(polySelect, vertSelect);
						vertSelect = -1;
					} else {
						DeletePolygon(polySelect, true);
					}
					engageDelete = false;
					polySelect = -1;
				} else if (objectSelect != -1) {
					DeleteObject(objectSelect, true);
					engageDelete = false;
					objectSelect = -1;
				} else if (decorSelect != -1) {
					if (vertSelect != -1) {
						if (allDecors.get(decorSelect).length <= 6) DeleteDecor(decorSelect, true);
						else DeleteVertex(decorSelect, vertSelect);
						vertSelect = -1;
					} else {
						DeleteDecor(decorSelect, true);
					}
					engageDelete = false;
					decorSelect = -1;
				} else if ((polySelect == -1) | (objectSelect == -1) | (decorSelect == -1)) {
					// This never does anything...
					//if (vertSelect != -1) System.out.println("Press the 'd' key to delete the selected vertex");
					//else System.out.println("Select Polygon and press the 'd' key to delete the selected polygon");
				}
				vertHover = -1;
				polyHover = -1;
				decorHover = -1;
				objectHover = -1;
				segmHover = -1;
			}
		}
		if (setCursor) {
			if (GameInput.MBJUSTPRESSED) {
				cursposx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				//cursposx = cam.position.x + cam.zoom*(GameInput.MBUPX/(BikeGame.SCALE) - 0.5f*SCRWIDTH);
				cursposy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				setCursor = false;
			}
			return;
		}
		if (GameInput.SCROLL != 0) {
    		ControlZoom();
    	}
		stage.setKeyboardFocus(null);
		if (enteringFilename) stage.setKeyboardFocus(textInputSave);
		// Cycle through the different operating modes
        if (mode==1) {
    		try {
    			ControlMode1();
    		} catch (Throwable e) {e.printStackTrace();}
//        	if (GameInput.MBDRAG==true) {
//        		ControlPan();
//        	}
        } else if (mode==2) {
    		try {
    			ControlMode2();
    		} catch (Throwable e) {e.printStackTrace();}
        } else if (mode==3) {
        	try {
        		ControlMode3(0);
        	} catch (Throwable e) {e.printStackTrace();}
        } else if (mode==4) {
        	try {
        		ControlMode4();
        	} catch (Throwable e) {e.printStackTrace();}
        } else if (mode==5) {
        	try {
        		ControlMode5();
        	} catch (Throwable e) {e.printStackTrace();}
        } else if (mode==6) {
        	try {
        		ControlMode6();
        	} catch (Throwable e) {e.printStackTrace();}
        } else if (mode==7) {
        	try {
        		ControlMode3(1); // Use Control Mode 3, but set the platform to be a falling platform (i.e. set the argument to 1)
        	} catch (Throwable e) {e.printStackTrace();}
        } else if (mode==8) {
        	ControlMode8();
//        	try {
//        		ControlMode8();
//        	} catch (Exception e) {}        	
        } else if (mode==9) {
        	try {
        		ControlMode3(2); // Use Control Mode 3, but set the platform to be a trigger platform (i.e. set the argument to 2)
        	} catch (Throwable e) {e.printStackTrace();}
        } else if (mode==10) {
        	if (GameInput.MBDRAG==true) {
        		ControlPan();
        	}
        } else if (mode==11) {
        	try {
				ControlMode11();
			} catch (Throwable e) {e.printStackTrace();}
		} else if (mode==12) {
        	try {
        		ControlMode12();
			} catch (Throwable e) {e.printStackTrace();}
		}
        GameInput.MBJUSTPRESSED = false;
        GameInput.MBJUSTDRAGGED = false;
//        if (GameInput.isPressed(GameInput.BUTTON2)) {
//        	cam.translate(-10,-5,0);
//        	System.out.println("BUTTON2 pressed");
//        }

    }

	public void update(float dt) {
		if (gsm.isPlaying) hideToolbar = true;
		else hideToolbar = false;
    	handleInput();
    	cam.update();
    	stage.setScrollFocus(null); // Forces scrolling to be used for zooming only
    	updateWarnings(dt);
    	updateToolbar(false);
    }

	private void updateToolbar(boolean hide) {
		if ((drawingPoly) | (hideToolbar) | (copyPoly) | (hide)) {
			buttonExit.setDisabled(true);
			selectLoadLevel.setDisabled(true);
			buttonSave.setDisabled(true);
			textInputSave.setDisabled(true);
			buttonExecute.setDisabled(true);
			buttonTraceImage.setDisabled(true);
			buttonLevelProp.setDisabled(true);
			buttonPan.setDisabled(true);
			buttonAddStatic.setDisabled(true);
			buttonAddKinetic.setDisabled(true);
			buttonAddFalling.setDisabled(true);
			buttonAddObject.setDisabled(true);
			buttonDecorate.setDisabled(true);
			//scrollPaneTBar.setVisible(false);
			windowTBar.setVisible(false);
		} else {
			buttonExit.setDisabled(false);
			selectLoadLevel.setDisabled(false);
			buttonSave.setDisabled(false);
			textInputSave.setDisabled(false);
			buttonExecute.setDisabled(false);
			buttonTraceImage.setDisabled(false);
			buttonLevelProp.setDisabled(false);
			buttonPan.setDisabled(false);
			buttonAddStatic.setDisabled(false);
			buttonAddKinetic.setDisabled(false);
			buttonAddFalling.setDisabled(false);
			buttonAddObject.setDisabled(false);
			buttonDecorate.setDisabled(false);
			windowTBar.setVisible(true);
			//scrollPaneTBar.setVisible(true);
		}
	}

	private void updateWarnings(float dt) {
    	if (warnNumber >= totalNumMsgs) {
    		warnNumber = 0;
    	}
    	boolean alloff = true;
        for (int i=0; i<totalNumMsgs; i++) {
        	if (warnMessage[i] != null) {
        		alloff = false;
        		warnElapse[i] += dt;
        		if (warnElapse[i] > warnTime) {
        			warnMessage[i] = null;
        			warnElapse[i] = 0.0f;
        			warnType[i] = -1;
        		}
        	}
        }
        if (alloff) warnNumber = 0;
	}

	public void render() {
        // clear screen
    	Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glViewport((int) BikeGame.viewport.x, (int) BikeGame.viewport.y, (int) BikeGame.viewport.width, (int) BikeGame.viewport.height);
        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.setProjectionMatrix(cam.combined);
		polyBatch.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeType.Line);
        // Draw the boundary region
        Gdx.gl20.glLineWidth(5);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(0, 0, boundaryX, boundaryY);
        shapeRenderer.setColor(0, 1, 0, 0.5f);
        shapeRenderer.end();
        // Draw the bounds of the background
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 0.1f, 0.1f);
        shapeRenderer.rect(boundsBG[0], 0, boundsBG[1]-boundsBG[0], boundaryY);
        shapeRenderer.end();

        // Draw the objects
		shapeRenderer.begin(ShapeType.Line);
		Gdx.gl20.glLineWidth(2);
		renderAllObjects();

        // Draw the decorations
		renderAllDecors();
		shapeRenderer.end();

		// Draw the polygons (not including the current polygon)
		renderAllPolygons();

		shapeRenderer.begin(ShapeType.Line);
		// Draw all of the things that are being updated
		renderUpdates();
		// Draw the current polygon
		renderCurrentPoly();
		shapeRenderer.end();

		// Draw a selected vertex or segment if necessary
		shapeRenderer.begin(ShapeType.Line);
		renderVertexSegments();
		shapeRenderer.end();

        // Draw the cursor
		shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1.0f);
        shapeRenderer.circle(cursposx, cursposy, cam.zoom*SCRWIDTH*polyEndThreshold);
		shapeRenderer.end();

        // Draw the decoration signs
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        renderSigns();
        sb.end();

        // If there are any warning/error messages, write them to screen
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		renderInfoWarnErrors();
		sb.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

        // Draw a trace image, if it exists
        if (traceImage != null) {
        	mBatch.setColor(1, 1, 1, 0.5f);
			mBatch.setProjectionMatrix(cam.combined);
	    	mBatch.begin();
	    	mBatch.draw(traceImage, trcImgProp[0]-trcImgProp[2]/2, trcImgProp[1]-trcImgProp[3]/2, trcImgProp[2]/2, trcImgProp[3]/2, trcImgProp[2], trcImgProp[3], 1, 1, trcImgProp[5]);
	    	mBatch.end();
        }

        // Draw rectangular decorations
		renderAllDecorsRect();

        // Finally, draw the toolbar
        if (!hideToolbar) {
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
        }
    }

    private void renderAllPolygons() {
		// First check if the alternative level should be rendered
		if (allPolygons_Alt.size() != 0) {
			for (int i = 0; i<allPolygons_Alt.size(); i++) {
				renderPolygon(allPolygons_Alt.get(i), allPolygonTypes_Alt.get(i), allPolygonPaths_Alt.get(i), allPolygonSprites_Alt.get(i), allPolygonTextures_Alt.get(i), -99);
			}
		} else if ((allPolygons.size() != 0) && (!loadedAlt)) {
			for (int i = 0; i<allPolygons.size(); i++) {
				renderPolygon(allPolygons.get(i), allPolygonTypes.get(i), allPolygonPaths.get(i), allPolygonSprites.get(i), allPolygonTextures.get(i), i);
			}
		}
	}

    private void renderPolygon(float[] poly, Integer polyType, float[] polyPath, PolygonSprite polySprite, String polyTexture, int i) {
		float[] extraPoly;
		float[] colarr;
		PolygonSprite polySpr;
		if (polyTexture.startsWith("COLOR_")) {
			colarr = ColorUtils.ConvertStringToColor(polyTexture);
			if (polyType%2 == 1) {
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(colarr[0], colarr[1], colarr[2], colarr[3]);
				shapeRenderer.circle(poly[0], poly[1], poly[2]);
				shapeRenderer.end();
			} else if (polySprite != null) {
				polyBatch.begin();
				polySpr = polySprite;
				polySpr.setColor(colarr[0], colarr[1], colarr[2], colarr[3]);
				polySpr.draw(polyBatch);
				polyBatch.end();
			}
		}
		shapeRenderer.begin(ShapeType.Line);
		Gdx.gl20.glLineWidth(2);
		if ((polyType <= 1) |
				((polyType >= 8)&(polyType <= 11))) {
			// Static Polygons
			if (polySelect == i) shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1);
			else shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.5f);
			// Draw the static polygon
			if (polyType%2 == 0) {
				shapeRenderer.polygon(poly);
			} else if (polyType%2 == 1) {
				shapeRenderer.circle(poly[0], poly[1], poly[2]);
			}
		} else if (polyType <= 3) {
			// Kinematic Polygons
			if (polySelect == i) opacity = 1.0f;
			else opacity = 0.5f;
			shapeRenderer.setColor(0.1f, 0.5f, 1, opacity);
			int sz = polyPath.length;
			// Draw the starting point of the kinematic polygon
			if (polyType == 2) {
				shapeRenderer.polygon(poly);
				if (sz > 6) {
					extraPoly = poly.clone();
					if ((ghostPoly != null)&(polySelect==i)) {
						for (int j=0; j < extraPoly.length/2; j++) {
							extraPoly[2*j] += (ghostPoly[0]-polyPath[4]);
							extraPoly[2*j+1] += (ghostPoly[1]-polyPath[5]);
						}
					} else {
						for (int j=0; j < extraPoly.length/2; j++) {
							extraPoly[2*j] += (polyPath[sz-2]-polyPath[4]);
							extraPoly[2*j+1] += (polyPath[sz-1]-polyPath[5]);
						}
					}
					shapeRenderer.setColor(0.2f, 0.4f, 0.9f, 0.5f*opacity);
					shapeRenderer.polygon(extraPoly);
				}
			} else if (polyType == 3) {
				shapeRenderer.circle(poly[0], poly[1], poly[2]);
				if (sz > 6) {
					extraPoly = poly.clone();
					if ((ghostPoly != null)&(polySelect==i)) {
						extraPoly[0] += (ghostPoly[0]-polyPath[4]);
						extraPoly[1] += (ghostPoly[1]-polyPath[5]);
					} else {
						extraPoly[0] += (polyPath[sz-2]-polyPath[4]);
						extraPoly[1] += (polyPath[sz-1]-polyPath[5]);
					}
					shapeRenderer.setColor(0.2f, 0.4f, 0.9f, 0.5f*opacity);
					shapeRenderer.circle(extraPoly[0],extraPoly[1],extraPoly[2]);
				}
			}
			// Draw the centre of the body
			// omega,velocity,omegaDir,velocityDir,xcen,ycen
			shapeRenderer.setColor(1, 0, 0, opacity);
			shapeRenderer.circle(polyPath[4], polyPath[5], 5);
			shapeRenderer.line(polyPath[4]-5, polyPath[5], polyPath[4]+5, polyPath[5]);
			shapeRenderer.line(polyPath[4], polyPath[5]-5, polyPath[4], polyPath[5]+5);
			// Draw the path (and the path being currently updated)
			if (sz > 6) {
				for (int j=0; j < (sz-6)/2 - 1; j++) shapeRenderer.line(polyPath[6+2*j], polyPath[6+2*j+1], polyPath[6+2*j+2], polyPath[6+2*j+3]);
				if ((updatePathVertex != null) & (polySelect==i)) {
					shapeRenderer.setColor(1, 1, 0.1f, opacity);
					nullvarC = (float) Math.sqrt((updatePathVertex[0]-polyPath[6])*(updatePathVertex[0]-polyPath[6]) + (updatePathVertex[1]-polyPath[7])*(updatePathVertex[1]-polyPath[7]));
					nullvarD = (float) Math.sqrt((updatePathVertex[0]-polyPath[sz-2])*(updatePathVertex[0]-polyPath[sz-2]) + (updatePathVertex[1]-polyPath[sz-1])*(updatePathVertex[1]-polyPath[sz-1]));
					if (nullvarC < nullvarD) shapeRenderer.line(polyPath[6], polyPath[7], updatePathVertex[0], updatePathVertex[1]);
					else shapeRenderer.line(polyPath[sz-2], polyPath[sz-1], updatePathVertex[0], updatePathVertex[1]);
					shapeRenderer.setColor(1, 0, 0, opacity);
				}
			}
			if ((updatePathVertex != null) & (polySelect==i)) {
				shapeRenderer.setColor(1, 1, 0.1f, opacity);
				if ((updatePathVertex.length == 2) & (sz==6)) {
					// Extending path for the first time
					shapeRenderer.line(polyPath[4], polyPath[5], updatePathVertex[0], updatePathVertex[1]);
				} else if (updatePathVertex.length == 4) {

				}
				shapeRenderer.setColor(1, 0, 0, opacity);
			}
			// Draw the initial velocity vector
			shapeRenderer.setColor(1, 0.75f, 0.75f, opacity);
			float[] arrowArray = ObjectVars.objectArrow1D.clone();
			if (sz >= 10) {
				float tempval = 0.0f, bestval = -1.0f;
				int spine = 0;
				// Identify the spine vertex (from which the velocity vector will be drawn)
				for (int j = 0; j < (sz-6)/2; j++){
					if (bestval == -1.0f) {
						bestval = (float) Math.sqrt((polyPath[4]-polyPath[6+2*j])*(polyPath[4]-polyPath[6+2*j]) + (polyPath[5]-polyPath[6+2*j+1])*(polyPath[5]-polyPath[6+2*j+1]));
						spine = j;
					} else {
						tempval = (float) Math.sqrt((polyPath[4]-polyPath[6+2*j])*(polyPath[4]-polyPath[6+2*j]) + (polyPath[5]-polyPath[6+2*j+1])*(polyPath[5]-polyPath[6+2*j+1]));
						if (tempval < bestval) {
							bestval = (float) Math.sqrt((polyPath[4]-polyPath[6+2*j])*(polyPath[4]-polyPath[6+2*j]) + (polyPath[5]-polyPath[6+2*j+1])*(polyPath[5]-polyPath[6+2*j+1]));
							spine = j;
						}
					}
				}
				arrowArray[0] += polyPath[1];
				arrowArray[2] += polyPath[1];
				arrowArray[4] += polyPath[1];
				if (polyPath[3]==1) {
					if (6+2*(spine+1)==sz) tempval = PolygonOperations.GetAngle(polyPath[6+2*spine-2], polyPath[6+2*spine-1], polyPath[6+2*spine], polyPath[6+2*spine+1]);
					else tempval = PolygonOperations.GetAngle(polyPath[6+2*spine], polyPath[6+2*spine+1], polyPath[6+2*spine+2], polyPath[6+2*spine+3]);
					PolygonOperations.RotateXYArray(arrowArray, tempval, 0.0f, 0.0f);
					for (int j = 0; j<3;j++) {
						arrowArray[2*j] += polyPath[4];
						arrowArray[2*j+1] += polyPath[5];
					}
					shapeRenderer.line(arrowArray[2], arrowArray[3], polyPath[4], polyPath[5]);
					shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[0], arrowArray[1]);
					shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[4], arrowArray[5]);
				} else {
					if (spine==0) tempval = PolygonOperations.GetAngle(polyPath[6+2*spine+2], polyPath[6+2*spine+3], polyPath[6+2*spine], polyPath[6+2*spine+1]);
					else tempval = PolygonOperations.GetAngle(polyPath[6+2*spine], polyPath[6+2*spine+1], polyPath[6+2*spine-2], polyPath[6+2*spine-1]);
					PolygonOperations.RotateXYArray(arrowArray, tempval, 0.0f, 0.0f);
					for (int j = 0; j<3;j++) {
						arrowArray[2*j] += polyPath[4];
						arrowArray[2*j+1] += polyPath[5];
					}
					shapeRenderer.line(arrowArray[2], arrowArray[3], polyPath[4], polyPath[5]);
					shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[0], arrowArray[1]);
					shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[4], arrowArray[5]);
				}
			}
			// Draw the rotation angle per second
			arrowArray = ObjectVars.objectArrow1D.clone();
			PolygonOperations.RotateXYArray(arrowArray, MathUtils.PI/2, 0.0f, 0.0f);
			arrowArray[0] += 30.0f;
			arrowArray[2] += 30.0f;
			arrowArray[4] += 30.0f;
			if (polyPath[2]==1) PolygonOperations.RotateXYArray(arrowArray, (polyPath[0])*MathUtils.degreesToRadians, 0.0f, 0.0f);
			else PolygonOperations.RotateXYArray(arrowArray, (360-polyPath[0])*MathUtils.degreesToRadians, 0.0f, 0.0f);
			arrowArray[1] *= polyPath[2];
			arrowArray[3] *= polyPath[2];
			arrowArray[5] *= polyPath[2];
			for (int j = 0; j<3;j++) {
				arrowArray[2*j] += polyPath[4];
				arrowArray[2*j+1] += polyPath[5];
			}
			if (polyPath[2]==1) shapeRenderer.arc(polyPath[4], polyPath[5], 30, 0, polyPath[0]);
			else shapeRenderer.arc(polyPath[4], polyPath[5], 30, polyPath[0], 360-polyPath[0]);
			shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[0], arrowArray[1]);
			shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[4], arrowArray[5]);
		} else if (polyType <= 5) {
			// Falling Polygons
			if (polySelect == i) shapeRenderer.setColor(1, 0, 0, 1);
			else shapeRenderer.setColor(1, 0, 0, 0.5f);
			// Draw the falling polygon
			if (polyType == 4) {
				shapeRenderer.polygon(poly);
			} else if (polyType == 5) {
				shapeRenderer.circle(poly[0], poly[1], poly[2]);
			}
			// Draw the centre of the body
			// fall time, damping, sign x, sign y
			shapeRenderer.setColor(0.8f, 0.2f, 0.2f, opacity);
			shapeRenderer.circle(polyPath[2], polyPath[3], 5);
			shapeRenderer.line(polyPath[2]-5, polyPath[3], polyPath[2]+5, polyPath[3]);
			shapeRenderer.line(polyPath[2], polyPath[3]-5, polyPath[2], polyPath[3]+5);
			// Draw the fall time arrow
			shapeRenderer.line(polyPath[2], polyPath[3], polyPath[2], polyPath[3]-polyPath[0]/B2DVars.EPPM);
			shapeRenderer.line(polyPath[2]-10, polyPath[3]-polyPath[0]/B2DVars.EPPM, polyPath[2]+10, polyPath[3]-polyPath[0]/B2DVars.EPPM);
			// Draw the damping arrow
			shapeRenderer.line(polyPath[2], polyPath[3], polyPath[2], polyPath[3]+polyPath[1]/B2DVars.EPPM);
			shapeRenderer.line(polyPath[2]-10, polyPath[3]+polyPath[1]/B2DVars.EPPM, polyPath[2]+10, polyPath[3]+polyPath[1]/B2DVars.EPPM);
		} else if (polyType <= 7) {
			// Trigger Polygons
			if (polySelect == i) shapeRenderer.setColor(1, 0, 1, 1);
			else shapeRenderer.setColor(1, 0, 1, 0.5f);
			// Draw the trigger polygon
			if (polyType == 6) {
				shapeRenderer.polygon(poly);
			} else if (polyType == 7) {
				shapeRenderer.circle(poly[0], poly[1], poly[2]);
			}
			// Draw a line between the body and the centre of the trigger
			if (polySelect == i) shapeRenderer.setColor(0.9f, 0.5f, 0.9f, 1);
			else shapeRenderer.setColor(0.9f, 0.5f, 0.9f, 0.5f);
			shapeRenderer.line(polyPath[0], polyPath[1], polyPath[2], polyPath[3]);
			// Draw Trigger
			extraPoly = new float[] {polyPath[2]-ObjectVars.objectTriggerWidth, polyPath[3]-polyPath[4]/2,
					polyPath[2]+ObjectVars.objectTriggerWidth, polyPath[3]-polyPath[4]/2,
					polyPath[2]+ObjectVars.objectTriggerWidth, polyPath[3]+polyPath[4]/2,
					polyPath[2]-ObjectVars.objectTriggerWidth, polyPath[3]+polyPath[4]/2};
			PolygonOperations.RotateXYArray(extraPoly, polyPath[5], polyPath[2], polyPath[3]);
			shapeRenderer.polygon(extraPoly);
		}
		shapeRenderer.end();
	}

	private void renderCurrentPoly() {
		if (drawingPoly == true) {
			shapeRenderer.setColor(1, 1, 0.1f, 1.0f);
			if (shapeDraw != null) {
				// Drawing a circle or a rectangle
				if (shapeDraw.length == 3) {
					tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
					tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
					shapeRenderer.circle(shapeDraw[0], shapeDraw[1], (float) Math.sqrt((tempx-shapeDraw[0])*(tempx-shapeDraw[0]) + (tempy-shapeDraw[1])*(tempy-shapeDraw[1])));
				} else if (shapeDraw.length == 8) {
					tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
					tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
					shapeDraw[2] = tempx;
					shapeDraw[3] = shapeDraw[1];
					shapeDraw[4] = tempx;
					shapeDraw[5] = tempy;
					shapeDraw[6] = shapeDraw[0];
					shapeDraw[7] = tempy;
					shapeRenderer.polygon(shapeDraw);
				}
			} else {
				// Drawing a polygon
				for (int i = 0; i<polyDraw.size()-1; i++){
					shapeRenderer.point(polyDraw.get(i)[0], polyDraw.get(i)[1], 0);
					shapeRenderer.line(polyDraw.get(i)[0], polyDraw.get(i)[1], polyDraw.get(i+1)[0], polyDraw.get(i+1)[1]);
				}
				shapeRenderer.point(polyDraw.get(polyDraw.size()-1)[0], polyDraw.get(polyDraw.size()-1)[1], 0);
				// Draw the vertex that is currently being investigated
				tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				if (GameInput.MBMOVEX >= 0) {
					shapeRenderer.line(polyDraw.get(polyDraw.size()-1)[0], polyDraw.get(polyDraw.size()-1)[1], tempx, tempy);
					// If the cursor is close to closing the polygon, draw a yellow circle
					if (polyDraw.size() >= 3) {
						if (Math.sqrt((tempx-polyDraw.get(0)[0])*(tempx-polyDraw.get(0)[0]) + (tempy-polyDraw.get(0)[1])*(tempy-polyDraw.get(0)[1])) < cam.zoom*SCRWIDTH*polyEndThreshold) {
							shapeRenderer.circle(polyDraw.get(0)[0], polyDraw.get(0)[1], cam.zoom*SCRWIDTH*polyEndThreshold);
						}
					}
				}
			}
		}
	}

	private void renderAllObjects() {
		if (allObjects_Alt.size() != 0) {
			for (int i = 0; i < allObjects_Alt.size(); i++) {
				renderObject(allObjects_Alt.get(i), allObjectTypes_Alt.get(i), allObjectArrows_Alt.get(i), -99, false);
			}
		} else if ((allObjects.size() != 0) && (!loadedAlt)) {
			for (int i = 0; i < allObjects.size(); i++) {
				renderObject(allObjects.get(i), allObjectTypes.get(i), allObjectArrows.get(i), i, false);
			}
		}
	}

	private void renderObject(float[] objarray, int otype, float[] objArrow, int i, boolean isUpdate) {
		float rxcen, rycen, rangle;
		float[] rCoord;
		opacity = 0.5f;
		if (objectSelect == i) opacity=1.0f;
		if  (otype == ObjectVars.BallChain) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			shapeRenderer.circle(objarray[0], objarray[1], objarray[2]);
			shapeRenderer.rect(objarray[3], objarray[4],objarray[5],objarray[6]);
			// Render the line of maximum length
			rxcen = objarray[3]+0.5f*objarray[5];
			rycen = objarray[4]+0.5f*objarray[6];
			rangle = PolygonOperations.GetAngle(rxcen, rycen, objarray[0], objarray[1]);
			rCoord = PolygonOperations.RotateCoordinate(rxcen+objarray[7], rycen, MathUtils.radiansToDegrees*rangle, rxcen, rycen);
			shapeRenderer.line(rxcen,rycen,rCoord[0],rCoord[1]);
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1.0f, 1.0f, 0, 0.8f);
			shapeRenderer.line(objarray[0], objarray[1], objarray[3]+0.5f*objarray[5], objarray[4]+0.5f*objarray[6]);
		} else if (otype == ObjectVars.Boulder) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			shapeRenderer.circle(objarray[0], objarray[1],objarray[2]);
		} else if (otype == ObjectVars.Bridge) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0.5f, 0, opacity);
			for (int j=0; j<3; j++) {
				shapeRenderer.line(objarray[2*j], objarray[2*j+1], objarray[2*j+2], objarray[2*j+3]);
				shapeRenderer.line(objarray[8+2*j], objarray[8+2*j+1], objarray[8+2*j+2], objarray[8+2*j+3]);
			}
			shapeRenderer.line(objarray[2*3], objarray[2*3+1], objarray[0], objarray[1]);
			shapeRenderer.line(objarray[8+2*3], objarray[8+2*3+1], objarray[8], objarray[9]);
			// Draw the line connecting the two bridge ends
			shapeRenderer.line(0.5f*(objarray[0]+objarray[4]), 0.5f*(objarray[1]+objarray[5]), 0.5f*(objarray[8]+objarray[12]), 0.5f*(objarray[9]+objarray[13]));
			// Draw the Bridge Sag
			rxcen = 0.25f*(objarray[0]+objarray[4]+objarray[8]+objarray[12]);
			rycen = 0.25f*(objarray[1]+objarray[5]+objarray[9]+objarray[13]);
			shapeRenderer.line(rxcen, rycen, rxcen, rycen-objarray[16]);
		} else if (otype == ObjectVars.Crate) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0.5f, 0, opacity);
			shapeRenderer.polygon(objarray);
			shapeRenderer.line(objarray[0], objarray[1], objarray[4], objarray[5]);
			shapeRenderer.line(objarray[2], objarray[3], objarray[6], objarray[7]);
		} else if (otype == ObjectVars.DoorBlue) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0, 0.7f, 1, opacity);
			shapeRenderer.polygon(objarray);
			shapeRenderer.circle(0.5f*(objarray[4]+objarray[6]),0.5f*(objarray[5]+objarray[7]),1.5f*ObjectVars.objectDoor[4]);
		} else if (otype == ObjectVars.DoorGreen) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0, 1, 0, opacity);
			shapeRenderer.polygon(objarray);
			shapeRenderer.circle(0.5f*(objarray[4]+objarray[6]),0.5f*(objarray[5]+objarray[7]),1.5f*ObjectVars.objectDoor[4]);
		} else if (otype == ObjectVars.DoorRed) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0, 0, opacity);
			shapeRenderer.polygon(objarray);
			shapeRenderer.circle(0.5f*(objarray[4]+objarray[6]),0.5f*(objarray[5]+objarray[7]),1.5f*ObjectVars.objectDoor[4]);
		} else if (otype == ObjectVars.KeyBlue) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0, 0.7f, 1, opacity);
			shapeRenderer.polygon(objarray);
		} else if (otype == ObjectVars.KeyGreen) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0, 1, 0, opacity);
			shapeRenderer.polygon(objarray);
		} else if (otype == ObjectVars.KeyRed) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0, 0, opacity);
			shapeRenderer.polygon(objarray);
		} else if (otype == ObjectVars.GateSwitch) {
			// Draw line connecting switch box and gate
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 1, 1, opacity);
			rxcen = 0.25f*(objarray[8]+objarray[10]+objarray[12]+objarray[14]);
			rycen = 0.25f*(objarray[9]+objarray[11]+objarray[13]+objarray[15]);
			shapeRenderer.line(0.5f*(objarray[0] + objarray[4]), 0.5f*(objarray[1] + objarray[5]), rxcen, rycen);
			// Draw Gate
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.7f*(1.0f-objarray[17]), 0.7f*objarray[17], 0, opacity);
			for (int j = 0; j<8; j++) {
				transPoly[j] = objarray[j];
			}
			shapeRenderer.polygon(transPoly);
			// Draw Switch box
			for (int j = 8; j<16; j++) {
				transPoly[j-8] = objarray[j];
			}
			shapeRenderer.polygon(transPoly);
			// Draw Switch
			rangle = PolygonOperations.GetAngle(objarray[8], objarray[9], objarray[10], objarray[11]);
			rCoord = PolygonOperations.RotateCoordinate(0.0f, 10.0f, ((MathUtils.radiansToDegrees*rangle)+objarray[16]), 0.0f, 0.0f);
			shapeRenderer.line(rxcen, rycen, rxcen+rCoord[0], rycen+rCoord[1]);
		} else if (ObjectVars.IsGravity(otype)) {
			if (i==0) shapeRenderer.setColor(1, 0.8f, 0, opacity);
			else if (otype == ObjectVars.GravityEarth) shapeRenderer.setColor(0, 0.7f, 1, opacity);
			else if (otype == ObjectVars.GravityMars) shapeRenderer.setColor(1, 0.1f, 0, opacity);
			else if (otype == ObjectVars.GravityMoon) shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			else if (otype == ObjectVars.GravityZero) shapeRenderer.setColor(0.3f, 0.3f, 0.3f, opacity);
			else shapeRenderer.setColor(1, 0.5f, 1, opacity);
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			shapeRenderer.polygon(objarray);
			shapeRenderer.polygon(objArrow);
		} else if (otype == ObjectVars.Jewel) {
			if (i==2) {
				// Diamond Jewel
				if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
				else shapeRenderer.setColor(0.85f, 0.85f, 0.85f, opacity);
				xcen = (0.5f*13.0f*(objarray[0]+objarray[4]) + 2.3f*objarray[2])/(13.0f+2.3f);
				ycen = (0.5f*13.0f*(objarray[1]+objarray[5]) + 2.3f*objarray[3])/(13.0f+2.3f);
				shapeRenderer.circle(xcen, ycen, ObjectVars.objectStartWheels[2]); // Use the same radius as the start wheels
			} else shapeRenderer.setColor(0.0f, 0.7f, 0, opacity);
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			shapeRenderer.polygon(objarray);
			//shapeRenderer.arc(objarray[0], objarray[1], objarray[2], 0, 60, 2);
			//shapeRenderer.arc(objarray[0], objarray[1], objarray[2], 60, 60, 2);
			//shapeRenderer.arc(objarray[0], objarray[1], objarray[2], 120, 60, 2);
			//shapeRenderer.arc(objarray[0], objarray[1], objarray[2], 180, 60, 2);
			//shapeRenderer.arc(objarray[0], objarray[1], objarray[2], 240, 60, 2);
			//shapeRenderer.arc(objarray[0], objarray[1], objarray[2], 300, 60, 2);
		} else if (otype == ObjectVars.Log) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0.5f, 0, opacity);
			shapeRenderer.circle(objarray[0], objarray[1],objarray[2]);
			shapeRenderer.circle(objarray[0], objarray[1],0.75f*objarray[2]);
			shapeRenderer.circle(objarray[0], objarray[1],0.50f*objarray[2]);
			shapeRenderer.circle(objarray[0], objarray[1],0.25f*objarray[2]);
		} else if (otype == ObjectVars.Nitrous) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0, 0.7f, 1, opacity);
			shapeRenderer.polygon(objarray);
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0.65f, 0, opacity);
			shapeRenderer.line(objarray[0], objarray[1], objarray[4], objarray[5]);
			shapeRenderer.line(objarray[2], objarray[3], objarray[6], objarray[7]);
		} else if (otype == ObjectVars.Pendulum) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			shapeRenderer.circle(objarray[0], objarray[1],objarray[2]);
			shapeRenderer.rect(objarray[3], objarray[4],objarray[5],objarray[6]);
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
			shapeRenderer.line(objarray[0], objarray[1], objarray[3]+0.5f*objarray[5], objarray[4]+0.5f*objarray[6]);
		} else if (ObjectVars.IsPlanet(otype)) {
			if (otype == ObjectVars.PlanetSun) shapeRenderer.setColor(1, 0.8f, 0, opacity);
			else if (otype == ObjectVars.PlanetMercury) shapeRenderer.setColor(0.6f, 0.6f, 0.6f, opacity);
			else if (otype == ObjectVars.PlanetVenus) shapeRenderer.setColor(0.9f, 0.9f, 0, opacity);
			else if (otype == ObjectVars.PlanetEarth) shapeRenderer.setColor(0.0f, 0.7f, 0.7f, opacity);
			else if (otype == ObjectVars.PlanetMars) shapeRenderer.setColor(1.0f, 0.0f, 0.0f, opacity);
			else if (otype == ObjectVars.PlanetJupiter) shapeRenderer.setColor(1f, 0.2f, 0.2f, opacity);
			else if (otype == ObjectVars.PlanetSaturn) shapeRenderer.setColor(1.0f, 0.9f, 0.2f, opacity);
			else if (otype == ObjectVars.PlanetUranus) shapeRenderer.setColor(0.2f, 0.8f, 1.0f, opacity);
			else if (otype == ObjectVars.PlanetNeptune) shapeRenderer.setColor(0.0f, 0.0f, 1.0f, opacity);
			else shapeRenderer.setColor(1, 1, 1, opacity);
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			if (otype == ObjectVars.PlanetSaturn) shapeRenderer.polygon(objarray);
			else shapeRenderer.circle(objarray[0], objarray[1],objarray[2]);
		} else if (otype == ObjectVars.Spike) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			shapeRenderer.circle(objarray[0], objarray[1],objarray[2]);
			shapeRenderer.triangle(objarray[0], objarray[1]+1.3f*objarray[2], objarray[0]-1.126f*objarray[2], objarray[1]-0.65f*objarray[2], objarray[0]+1.126f*objarray[2], objarray[1]-0.65f*objarray[2]);
		} else if (otype == ObjectVars.SpikeZone) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			shapeRenderer.polygon(objarray);
			shapeRenderer.line(objarray[0], objarray[1], objarray[4], objarray[5]);
			shapeRenderer.line(objarray[2], objarray[3], objarray[6], objarray[7]);
		} else if ((otype == ObjectVars.Transport) | ObjectVars.IsTransportInvisible(otype)) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 1, 1, opacity);
			shapeRenderer.line(0.5f*(objarray[0] + objarray[4]), 0.5f*(objarray[1] + objarray[5]), 0.5f*(objarray[8] + objarray[12]), 0.5f*(objarray[9] + objarray[13]));
			for (int j = 0; j<8; j++) {
				transPoly[j] = objarray[j];
			}
			if (ObjectVars.IsTransportInvisible(otype)) {
				// Render the gravity arrow
				shapeRenderer.polygon(objArrow);
			}
			shapeRenderer.polygon(transPoly);
			for (int j = 8; j<16; j++) {
				transPoly[j-8] = objarray[j];
			}
			shapeRenderer.polygon(transPoly);
			// Entry and exit blue/red lines
			shapeRenderer.setColor(1, 0, 0, opacity);
			shapeRenderer.line(objarray[0], objarray[1], objarray[2], objarray[3]);
			shapeRenderer.line(objarray[8], objarray[9], objarray[10], objarray[11]);
			shapeRenderer.setColor(0, 0.7f, 1, opacity);
			shapeRenderer.line(objarray[4], objarray[5], objarray[6], objarray[7]);
			shapeRenderer.line(objarray[12], objarray[13], objarray[14], objarray[15]);
		} else if (otype == ObjectVars.Start) {
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0.8f, 0, opacity);
			shapeRenderer.polygon(objarray);
			shapeRenderer.polygon(objArrow);
			rangle = PolygonOperations.GetAngle(objArrow[0], objArrow[1], objArrow[2], objArrow[3]);
			xcen = objarray[0]-ObjectVars.objectStart[0];
			ycen = objarray[1]-ObjectVars.objectStart[1];
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.7f*opacity);
			shapeRenderer.circle(xcen, ycen, ObjectVars.objectStartWheels[2]);
			shapeRenderer.circle(xcen + ObjectVars.objectStartWheels[3]*(float)Math.cos(rangle), ycen + ObjectVars.objectStartWheels[3]*(float)Math.sin(rangle), ObjectVars.objectStartWheels[2]);
			shapeRenderer.line(xcen, ycen, xcen + ObjectVars.objectStartWheels[3]*(float)Math.cos(rangle), ycen + ObjectVars.objectStartWheels[3]*(float)Math.sin(rangle));
			shapeRenderer.line(xcen, ycen, xcen + 0.5f*ObjectVars.objectStartWheels[3]*(float)Math.cos(rangle+0.5f), ycen + 0.5f*ObjectVars.objectStartWheels[3]*(float)Math.sin(rangle+0.5f));
			shapeRenderer.line(xcen + 0.5f*ObjectVars.objectStartWheels[3]*(float)Math.cos(rangle+0.5f), ycen + 0.5f*ObjectVars.objectStartWheels[3]*(float)Math.sin(rangle+0.5f), xcen+ ObjectVars.objectStartWheels[3]*(float)Math.cos(rangle), ycen + ObjectVars.objectStartWheels[3]*(float)Math.sin(rangle));
			// Draw the starting direction
			// Make a copy of the arrow
			startDirPoly = ObjectVars.objectArrow.clone();
			// Rotate the Arrow in the appropriate direction
			if (LevelVars.get(LevelVars.PROP_START_DIRECTION).equals("Left")) PolygonOperations.RotateXYArray(startDirPoly, rangle+1.5f*(float)Math.PI, 0, 0);
			else PolygonOperations.RotateXYArray(startDirPoly, rangle+0.5f*(float)Math.PI, 0, 0);
			// Draw the Head and the head arrow
			rangle += Math.atan(ObjectVars.objectStartWheels[6]/ObjectVars.objectStartWheels[5]);
			float length = (float) Math.sqrt(ObjectVars.objectStartWheels[6]*ObjectVars.objectStartWheels[6] + ObjectVars.objectStartWheels[5]*ObjectVars.objectStartWheels[5]);
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(1, 0.705f, 0.522f, opacity);
			shapeRenderer.circle(xcen + length*(float)Math.cos(rangle), ycen + length*(float)Math.sin(rangle), ObjectVars.objectStartWheels[4]);
			for (int j=0; j<startDirPoly.length/2; j++) {
				startDirPoly[2*j] += xcen+length*(float)Math.cos(rangle);
				startDirPoly[2*j+1] += ycen+length*(float)Math.sin(rangle);
			}
			shapeRenderer.polygon(startDirPoly);
		} else if (otype == ObjectVars.Finish) {
			rxcen = objarray[0]-ObjectVars.objectFinish[0];
			rycen = objarray[1]-ObjectVars.objectFinish[1];
			if (isUpdate) shapeRenderer.setColor(1, 1, 0.1f, 1);
			else shapeRenderer.setColor(0, 0.7f, 1.0f, opacity);
			shapeRenderer.polygon(objarray);
			shapeRenderer.circle(rxcen, rycen, ObjectVars.objectFinishBall[2]);
		}
	}

	private void renderAllDecors() {
		if (allDecors_Alt.size() != 0) {
			for (int i = 0; i < allDecors_Alt.size(); i++) {
				renderDecor(allDecors_Alt.get(i), allDecorTypes_Alt.get(i), i);
			}
		} else if ((allDecors.size() != 0) && (!loadedAlt)) {
			for (int i = 0; i < allDecors.size(); i++) {
				renderDecor(allDecors.get(i), allDecorTypes.get(i), i);
			}
		}
	}

	private void renderDecor(float[] decor, Integer dTyp, int i) {
		float[] rCoord;
		if (decorSelect == i) opacity=1.0f;
		else opacity = 0.5f;
		if  (DecorVars.IsRoadSign(dTyp)) {
			shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			shapeRenderer.circle(decor[0], decor[1],decor[2]);
			// Render the pole
			rCoord = PolygonOperations.RotateCoordinate(decor[0], decor[1]-5.0f*decor[2], MathUtils.radiansToDegrees*decor[3], decor[0], decor[1]);
			shapeRenderer.line(decor[0], decor[1], rCoord[0], rCoord[1]);
		} else if (dTyp == DecorVars.Grass) {
			shapeRenderer.setColor(0, 1, 0, opacity);
			shapeRenderer.polygon(decor);
		} else if (dTyp == DecorVars.Rain) {
			shapeRenderer.setColor(0, 0, 0.6f, opacity);
			rCoord = Arrays.copyOfRange(decor, 0, 8);
			shapeRenderer.polygon(rCoord);
		} else if (dTyp == DecorVars.Waterfall) {
			shapeRenderer.setColor(0, 0, 0.8f, opacity);
			rCoord = Arrays.copyOfRange(decor, 0, 8);
			shapeRenderer.polygon(rCoord);
		} else if (dTyp == DecorVars.LargeStone) {
			shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
			shapeRenderer.circle(decor[0], decor[1], decor[2]);
		}
	}

	private void renderAllDecorsRect() {
		if (allDecors_Alt.size() != 0) {
			mBatch.setColor(1, 1, 1, 0.6f);
			mBatch.setProjectionMatrix(cam.combined);
			mBatch.begin();
			for (int i = 0; i < allDecors_Alt.size(); i++) {
				if (DecorVars.IsRect(allDecorTypes_Alt.get(i))) {
					renderDecorRect(allDecors_Alt.get(i), allDecorTypes_Alt.get(i), i);
				}
			}
			mBatch.end();
		} else if ((allDecors.size() != 0) && (!loadedAlt)) {
			mBatch.setColor(1, 1, 1, 0.6f);
			mBatch.setProjectionMatrix(cam.combined);
			mBatch.begin();
			for (int i = 0; i < allDecors.size(); i++) {
				if (DecorVars.IsRect(allDecorTypes.get(i))) {
					renderDecorRect(allDecors.get(i), allDecorTypes.get(i), i);
				}
			}
			mBatch.end();
		}
	}

	private void renderDecorRect(float[] decor, Integer dTyp, int i) {
		String textName;
		float [] extraPoly;
		float xcen, ycen, xlen, ylen, rotAngle;
		textName = DecorVars.GetImageRect(dTyp, (int) decor[8]);
		extraPoly = DecorVars.GetCoordRect(dTyp, (int) decor[8]);
		xcen = 0.5f*(decor[0]+decor[4]);
		ycen = 0.5f*(decor[1]+decor[5]);
		xlen = (float) Math.sqrt((decor[2]-decor[0])*(decor[2]-decor[0]) + (decor[3]-decor[1])*(decor[3]-decor[1]));
		ylen = (float) Math.sqrt((decor[4]-decor[2])*(decor[4]-decor[2]) + (decor[5]-decor[3])*(decor[5]-decor[3]));
		rotAngle = PolygonOperations.GetAngle(decor[0], decor[1], decor[2], decor[3]);
		decorImage = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName(textName), 2));
		mBatch.draw(decorImage, xcen-xlen/2, ycen-ylen/2, xlen/2, ylen/2, xlen, ylen, 1, 1, (float) Math.toDegrees(rotAngle));
	}

	private void renderUpdates() {
		float[] rCoord;
		float [] extraPoly;
		// Draw the updated group polygons
		if (updateGroupPoly != null) {
			shapeRenderer.setColor(1, 1, 0.1f, 1);
			for (int i = 0; i<updateGroupPoly.size(); i++){
				if (mode==12) {
					// Use Alternative objects
					if (allPolygonTypes_Alt.get(groupPolySelect.get(i)) % 2 == 0)
						shapeRenderer.polygon(updateGroupPoly.get(i));
					else if (allPolygonTypes_Alt.get(groupPolySelect.get(i)) % 2 == 1)
						shapeRenderer.circle(updateGroupPoly.get(i)[0], updateGroupPoly.get(i)[1], updateGroupPoly.get(i)[2]);
				} else {
					if (allPolygonTypes.get(groupPolySelect.get(i)) % 2 == 0)
						shapeRenderer.polygon(updateGroupPoly.get(i));
					else if (allPolygonTypes.get(groupPolySelect.get(i)) % 2 == 1)
						shapeRenderer.circle(updateGroupPoly.get(i)[0], updateGroupPoly.get(i)[1], updateGroupPoly.get(i)[2]);
				}
			}
		}
		// Draw the updated polygon
		if (updatePoly != null) {
			shapeRenderer.setColor(1, 1, 0.1f, 1);
			if (polySelect != -1) {
				if (mode==6) {
					shapeRenderer.polygon(updatePoly);
				} else {
					if (allPolygonTypes.get(polySelect)%2 == 0) shapeRenderer.polygon(updatePoly);
					else if (allPolygonTypes.get(polySelect)%2 == 1) shapeRenderer.circle(updatePoly[0], updatePoly[1], updatePoly[2]);
				}
			} else if (objectSelect != -1) {
				if (updatePoly.length == 3) shapeRenderer.circle(updatePoly[0],updatePoly[1],updatePoly[2]);
				else if (updatePoly.length == 4) shapeRenderer.line(updatePoly[0],updatePoly[1],updatePoly[2],updatePoly[3]);
				else shapeRenderer.polygon(updatePoly);
			} else if (decorSelect != -1) {
				if (DecorVars.IsRoadSign(allDecorTypes.get(decorSelect))) {
					shapeRenderer.circle(updatePoly[0],updatePoly[1],updatePoly[2]);
					rCoord = PolygonOperations.RotateCoordinate(updatePoly[0], updatePoly[1]-5.0f*updatePoly[2], MathUtils.radiansToDegrees*updatePoly[3], updatePoly[0], updatePoly[1]);
					shapeRenderer.line(updatePoly[0], updatePoly[1], rCoord[0], rCoord[1]);
				} else if (allDecorTypes.get(decorSelect) == DecorVars.Rain) {
					shapeRenderer.polygon(updatePoly);
				} else if (allDecorTypes.get(decorSelect) == DecorVars.Waterfall) {
					shapeRenderer.polygon(updatePoly);
				} else if (DecorVars.IsRect(allDecorTypes.get(decorSelect))) {
					float[] rectUpdPoly = new float[8];
					for (int i=0; i<8; i++) rectUpdPoly[i] = updatePoly[i];
					shapeRenderer.polygon(rectUpdPoly);
				} else shapeRenderer.polygon(updatePoly);
			}
		}
		// Draw the updated path
		if (updatePath != null) {
			shapeRenderer.setColor(1, 1, 0.1f, 1);
			if (polySelect != -1) {
				if (mode==4) { // Moving Platform
					for (int i=0; i<(updatePath.length-6)/2-1; i++) shapeRenderer.line(updatePath[6+2*i],updatePath[6+2*i+1],updatePath[6+2*i+2],updatePath[6+2*i+3]);
				} else if (mode==7) { // Falling platform
					// Draw the sign location
					shapeRenderer.circle(updatePath[2], updatePath[3], 5);
					shapeRenderer.line(updatePath[2]-5, updatePath[3], updatePath[2]+5, updatePath[3]);
					shapeRenderer.line(updatePath[2], updatePath[3]-5, updatePath[2], updatePath[3]+5);
					// Draw the fall time arrow
					shapeRenderer.line(updatePath[2], updatePath[3], updatePath[2], updatePath[3]-updatePath[0]/B2DVars.EPPM);
					shapeRenderer.line(updatePath[2]-10, updatePath[3]-updatePath[0]/B2DVars.EPPM, updatePath[2]+10, updatePath[3]-updatePath[0]/B2DVars.EPPM);
					// Draw the damping arrow
					shapeRenderer.line(updatePath[2], updatePath[3], updatePath[2], updatePath[3]+updatePath[1]/B2DVars.EPPM);
					shapeRenderer.line(updatePath[2]-10, updatePath[3]+updatePath[1]/B2DVars.EPPM, updatePath[2]+10, updatePath[3]+updatePath[1]/B2DVars.EPPM);
				} else if (mode==9) {
					extraPoly = new float[] {updatePath[2]-5.0f, updatePath[3]-updatePath[4]/2,
							updatePath[2]+5.0f, updatePath[3]-updatePath[4]/2,
							updatePath[2]+5.0f, updatePath[3]+updatePath[4]/2,
							updatePath[2]-5.0f, updatePath[3]+updatePath[4]/2};
					PolygonOperations.RotateXYArray(extraPoly, updatePath[5], updatePath[2], updatePath[3]);
					shapeRenderer.polygon(extraPoly);
				}
			}
		}
		// Draw the updated group
		float[] rectUpdPoly = new float[8];
		if (updateGroup != null) {
			for (int pp=0; pp<updateGroup.size(); pp++) {
				if (groupPOD.get(pp) == 2) {
					// Decor
					shapeRenderer.setColor(1, 1, 0.1f, 1);
					if (mode==12) {
						if (DecorVars.IsRoadSign(allDecorTypes_Alt.get(groupArrays.get(pp)))) {
							shapeRenderer.circle(updateGroup.get(pp)[0], updateGroup.get(pp)[1], updateGroup.get(pp)[2]);
							rCoord = PolygonOperations.RotateCoordinate(updateGroup.get(pp)[0], updateGroup.get(pp)[1] - 5.0f * updateGroup.get(pp)[2], MathUtils.radiansToDegrees * updateGroup.get(pp)[3], updateGroup.get(pp)[0], updateGroup.get(pp)[1]);
							shapeRenderer.line(updateGroup.get(pp)[0], updateGroup.get(pp)[1], rCoord[0], rCoord[1]);
						} else if (DecorVars.IsRect(allDecorTypes_Alt.get(groupArrays.get(pp)))) {
							rectUpdPoly = new float[8];
							for (int i = 0; i < 8; i++) rectUpdPoly[i] = updateGroup.get(pp)[i];
							shapeRenderer.polygon(rectUpdPoly);
						} else shapeRenderer.polygon(updateGroup.get(pp));
					} else {
						if (DecorVars.IsRoadSign(allDecorTypes.get(groupArrays.get(pp)))) {
							shapeRenderer.circle(updateGroup.get(pp)[0], updateGroup.get(pp)[1], updateGroup.get(pp)[2]);
							rCoord = PolygonOperations.RotateCoordinate(updateGroup.get(pp)[0], updateGroup.get(pp)[1] - 5.0f * updateGroup.get(pp)[2], MathUtils.radiansToDegrees * updateGroup.get(pp)[3], updateGroup.get(pp)[0], updateGroup.get(pp)[1]);
							shapeRenderer.line(updateGroup.get(pp)[0], updateGroup.get(pp)[1], rCoord[0], rCoord[1]);
						} else if (DecorVars.IsRect(allDecorTypes.get(groupArrays.get(pp)))) {
							rectUpdPoly = new float[8];
							for (int i = 0; i < 8; i++) rectUpdPoly[i] = updateGroup.get(pp)[i];
							shapeRenderer.polygon(rectUpdPoly);
						} else shapeRenderer.polygon(updateGroup.get(pp));
					}
				} else if (groupPOD.get(pp) == 1) {
					// Color is set for each object separately
					if (mode==12) {
						renderObject(updateGroup.get(pp), allObjectTypes_Alt.get(groupArrays.get(pp)), allObjectArrows_Alt.get(groupArrays.get(pp)), groupArrays.get(pp), true);
					} else {
						renderObject(updateGroup.get(pp), allObjectTypes.get(groupArrays.get(pp)), allObjectArrows.get(groupArrays.get(pp)), groupArrays.get(pp), true);
					}
				} else {
					shapeRenderer.setColor(1, 1, 0.1f, 1);
					if (updateGroup.get(pp).length == 3) {
						shapeRenderer.circle(updateGroup.get(pp)[0], updateGroup.get(pp)[1], updateGroup.get(pp)[2]);
					} else {
						shapeRenderer.polygon(updateGroup.get(pp));
					}
				}
			}
		}
	}

	private void renderVertexSegments() {
		if ((mode==4)&(modeParent.equals("Set Path"))) {
			if ((segmSelect != -1) & (vertSelect != -1) & (polySelect != -1) & (allPolygons.size()!=0)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.line(allPolygonPaths.get(polySelect)[6+2*segmSelect],allPolygonPaths.get(polySelect)[6+2*segmSelect+1],allPolygonPaths.get(polySelect)[6+2*(segmSelect+1)],allPolygonPaths.get(polySelect)[6+2*(segmSelect+1)+1]);
			} else if ((vertSelect != -1) & (polySelect != -1)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.circle(allPolygonPaths.get(polySelect)[6+2*vertSelect], allPolygonPaths.get(polySelect)[6+2*vertSelect+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((segmHover != -1) & (vertHover != -1) & (polySelect != -1)) {
				// Draw the hover segment
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.line(allPolygonPaths.get(polySelect)[6+2*segmHover],allPolygonPaths.get(polySelect)[6+2*segmHover+1],allPolygonPaths.get(polySelect)[6+2*(segmHover+1)],allPolygonPaths.get(polySelect)[6+2*(segmHover+1)+1]);
				shapeRenderer.circle(0.5f*(allPolygonPaths.get(polySelect)[6+2*segmHover]+allPolygonPaths.get(polySelect)[6+2*(segmHover+1)]), 0.5f*(allPolygonPaths.get(polySelect)[6+2*segmHover+1]+allPolygonPaths.get(polySelect)[6+2*(segmHover+1)+1]), cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((vertHover != -1) & (polySelect != -1)) {
				// Draw the hover vertex
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.circle(allPolygonPaths.get(polySelect)[6+2*vertHover], allPolygonPaths.get(polySelect)[6+2*vertHover+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			}
		} else if (mode==5) {
			if ((segmSelect != -1) & (vertSelect != -1) & (objectSelect != -1)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				int segmNext = segmSelect + 1;
				if (segmNext==allObjects.get(objectSelect).length/2) segmNext = 0;
				shapeRenderer.line(allObjects.get(objectSelect)[2*segmNext],allObjects.get(objectSelect)[2*segmNext+1],allObjects.get(objectSelect)[2*segmSelect],allObjects.get(objectSelect)[2*segmSelect+1]);
				//shapeRenderer.line(allPolygons.get(polySelect)[2*vertSelect],allPolygons.get(polySelect)[2*vertSelect+1],allPolygons.get(polySelect)[2*segmSelect],allPolygons.get(polySelect)[2*segmSelect+1]);
			} else if ((vertSelect != -1) & (objectSelect != -1)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.circle(allObjects.get(objectSelect)[2*vertSelect], allObjects.get(objectSelect)[2*vertSelect+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((segmHover != -1) & (vertHover != -1) & (objectHover != -1)) {
				// Draw the hover segment
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				int segmNext = segmHover + 1;
				if (segmNext==allObjects.get(objectHover).length/2) segmNext = 0;
				shapeRenderer.line(allObjects.get(objectHover)[2*segmNext],allObjects.get(objectHover)[2*segmNext+1],allObjects.get(objectHover)[2*segmHover],allObjects.get(objectHover)[2*segmHover+1]);
				//shapeRenderer.line(allPolygons.get(polyHover)[2*vertHover],allPolygons.get(polyHover)[2*vertHover+1],allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+1]);
				shapeRenderer.circle(0.5f*(allObjects.get(objectHover)[2*segmNext]+allObjects.get(objectHover)[2*segmHover]), 0.5f*(allObjects.get(objectHover)[2*segmNext+1]+allObjects.get(objectHover)[2*segmHover+1]), cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((vertHover != -1) & (objectHover != -1)) {
				// Draw the hover vertex
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.circle(allObjects.get(objectHover)[2*vertHover], allObjects.get(objectHover)[2*vertHover+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			}
		} else if (mode==6) {
			if ((segmSelect != -1) & (vertSelect != -1) & (decorSelect != -1)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				int segmNext = segmSelect + 1;
				if (segmNext==allDecors.get(decorSelect).length/2) segmNext = 0;
				shapeRenderer.line(allDecors.get(decorSelect)[2*segmNext],allDecors.get(decorSelect)[2*segmNext+1],allDecors.get(decorSelect)[2*segmSelect],allDecors.get(decorSelect)[2*segmSelect+1]);
				//shapeRenderer.line(allPolygons.get(polySelect)[2*vertSelect],allPolygons.get(polySelect)[2*vertSelect+1],allPolygons.get(polySelect)[2*segmSelect],allPolygons.get(polySelect)[2*segmSelect+1]);
			} else if ((vertSelect != -1) & (decorSelect != -1)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.circle(allDecors.get(decorSelect)[2*vertSelect], allDecors.get(decorSelect)[2*vertSelect+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((segmHover != -1) & (vertHover != -1) & (decorHover != -1)) {
				// Draw the hover segment
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				int segmNext = segmHover + 1;
				if (segmNext==allDecors.get(decorHover).length/2) segmNext = 0;
				shapeRenderer.line(allDecors.get(decorHover)[2*segmNext],allDecors.get(decorHover)[2*segmNext+1],allDecors.get(decorHover)[2*segmHover],allDecors.get(decorHover)[2*segmHover+1]);
				//shapeRenderer.line(allPolygons.get(polyHover)[2*vertHover],allPolygons.get(polyHover)[2*vertHover+1],allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+1]);
				shapeRenderer.circle(0.5f*(allDecors.get(decorHover)[2*segmNext]+allDecors.get(decorHover)[2*segmHover]), 0.5f*(allDecors.get(decorHover)[2*segmNext+1]+allDecors.get(decorHover)[2*segmHover+1]), cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((vertHover != -1) & (decorHover != -1)) {
				// Draw the hover vertex
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				shapeRenderer.circle(allDecors.get(decorHover)[2*vertHover], allDecors.get(decorHover)[2*vertHover+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			}
		} else if (allPolygons.size()!=0) {
			if ((segmSelect != -1) & (vertSelect != -1) & (polySelect != -1)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				int segmNext = segmSelect + 1;
				if (segmNext==allPolygons.get(polySelect).length/2) segmNext = 0;
				shapeRenderer.line(allPolygons.get(polySelect)[2*segmNext],allPolygons.get(polySelect)[2*segmNext+1],allPolygons.get(polySelect)[2*segmSelect],allPolygons.get(polySelect)[2*segmSelect+1]);
				//shapeRenderer.line(allPolygons.get(polySelect)[2*vertSelect],allPolygons.get(polySelect)[2*vertSelect+1],allPolygons.get(polySelect)[2*segmSelect],allPolygons.get(polySelect)[2*segmSelect+1]);
			} else if ((vertSelect != -1) & (polySelect != -1)) {
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				if (mode==6) shapeRenderer.circle(allDecors.get(polySelect)[2*vertSelect], allDecors.get(polySelect)[2*vertSelect+1], cam.zoom*SCRWIDTH*polyEndThreshold);
				else shapeRenderer.circle(allPolygons.get(polySelect)[2*vertSelect], allPolygons.get(polySelect)[2*vertSelect+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((segmHover != -1) & (vertHover != -1) & (polyHover != -1)) {
				// Draw the hover segment
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				int segmNext = segmHover + 1;
				if (segmNext==allPolygons.get(polyHover).length/2) segmNext = 0;
				shapeRenderer.line(allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[2*segmNext+1],allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+1]);
				//shapeRenderer.line(allPolygons.get(polyHover)[2*vertHover],allPolygons.get(polyHover)[2*vertHover+1],allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+1]);
				shapeRenderer.circle(0.5f*(allPolygons.get(polyHover)[2*segmNext]+allPolygons.get(polyHover)[2*segmHover]), 0.5f*(allPolygons.get(polyHover)[2*segmNext+1]+allPolygons.get(polyHover)[2*segmHover+1]), cam.zoom*SCRWIDTH*polyEndThreshold);
			} else if ((vertHover != -1) & (polyHover != -1)) {
				// Draw the hover vertex
				shapeRenderer.setColor(1, 1, 0.1f, 1);
				if (mode==6) shapeRenderer.circle(allDecors.get(polyHover)[2*vertHover], allDecors.get(polyHover)[2*vertHover+1], cam.zoom*SCRWIDTH*polyEndThreshold);
				else shapeRenderer.circle(allPolygons.get(polyHover)[2*vertHover], allPolygons.get(polyHover)[2*vertHover+1], cam.zoom*SCRWIDTH*polyEndThreshold);
			}
		}
	}

	private void renderSigns() {
		String textName;
		float signWidth;
		int dTyp;
		if (allDecors.size() != 0) {
			for (int i = 0; i<allDecors.size(); i++){
				dTyp = allDecorTypes.get(i);
				if (decorSelect == i) opacity=1.0f;
				else opacity = 0.5f;
				if  (DecorVars.IsRoadSign(dTyp)) {
					glyphLayout.setText(signFont, DecorVars.GetObjectName(allDecorTypes.get(i)));
					signWidth = glyphLayout.width;  // This is actually height, not width
					signFont.draw(sb, DecorVars.GetObjectName(allDecorTypes.get(i)), allDecors.get(i)[0]-signWidth/2, allDecors.get(i)[1]+0.3f*allDecors.get(i)[2]);
				}
			}
		}
		// Draw the texture names on each polygon
		float textadd = 0.0f;
		if (allPolygons.size() != 0) {
			for (int i = 0; i<allPolygons.size(); i++) {
				textName = allPolygonTextures.get(i);
				textadd = 0.0f;
				if (!textName.equals("")) {
					glyphLayout.setText(signFont, textName);
					signWidth = glyphLayout.height;  // This is actually height, not width
					signFont.draw(sb, textName, allPolygons.get(i)[0], allPolygons.get(i)[1] + signWidth / 2);
					textadd = 1.2f * signWidth;
				}
				if ((allPolygonTypes.get(i) <= 1) |
						((allPolygonTypes.get(i) >= 8) & (allPolygonTypes.get(i) <= 11))) {
					glyphLayout.setText(signFont, "Static Background Foreground");
					signWidth = glyphLayout.height;  // This is actually height, not width
					if (allPolygonTypes.get(i) <= 1)
						signFont.draw(sb, "Static", allPolygons.get(i)[0], allPolygons.get(i)[1] + signWidth / 2 + textadd);
					else if (allPolygonTypes.get(i) <= 9)
						signFont.draw(sb, "Background", allPolygons.get(i)[0], allPolygons.get(i)[1] + signWidth / 2 + textadd);
					else if (allPolygonTypes.get(i) <= 11)
						signFont.draw(sb, "Foreground", allPolygons.get(i)[0], allPolygons.get(i)[1] + signWidth / 2 + textadd);
				}
			}
		}
		// Draw the gravity names next to each gravity/transport symbol
		if (allObjects.size() > 3) {
			for (int i=3; i<allObjects.size(); i++) {
				if (ObjectVars.IsGravity(allObjectTypes.get(i))) {
					if (allObjectTypes.get(i) == ObjectVars.GravityEarth) textName = "Earth";
					else if (allObjectTypes.get(i) == ObjectVars.GravityMars) textName = "Mars";
					else if (allObjectTypes.get(i) == ObjectVars.GravityMoon) textName = "Moon";
					else if (allObjectTypes.get(i) == ObjectVars.GravityZero) textName = "Zero";
					else textName = "Default";
					glyphLayout.setText(signFont, textName);
					signWidth = glyphLayout.height;  // This is actually height, not width
					signFont.draw(sb, textName, allObjectCoords.get(i)[0], allObjectCoords.get(i)[1]+25.0f+signWidth/2);
				} else if (ObjectVars.IsTransportInvisible(allObjectTypes.get(i))) {
					if (allObjectTypes.get(i) == ObjectVars.TransportInvisibleEarth) textName = "Earth";
					else if (allObjectTypes.get(i) == ObjectVars.TransportInvisibleMars) textName = "Mars";
					else if (allObjectTypes.get(i) == ObjectVars.TransportInvisibleMoon) textName = "Moon";
					else if (allObjectTypes.get(i) == ObjectVars.TransportInvisibleZero) textName = "Zero";
					else textName = "Default";
					glyphLayout.setText(signFont, textName);
					signWidth = glyphLayout.height;  // This is actually height, not width
					signFont.draw(sb, textName, allObjectCoords.get(i)[0], allObjectCoords.get(i)[1]+signWidth/2);
				} else if (ObjectVars.IsPlanet(allObjectTypes.get(i))) {
					if (allObjectTypes.get(i) == ObjectVars.PlanetSun) textName = "Sun";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetMercury) textName = "Mercury";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetVenus) textName = "Venus";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetEarth) textName = "Earth";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetMars) textName = "Mars";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetJupiter) textName = "Jupiter";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetSaturn) textName = "Saturn";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetUranus) textName = "Uranus";
					else if (allObjectTypes.get(i) == ObjectVars.PlanetNeptune) textName = "Neptune";
					else textName = "Default";
					glyphLayout.setText(signFont, textName);
					signWidth = glyphLayout.height;  // This is actually height, not width
					signFont.draw(sb, textName, allObjectCoords.get(i)[0], allObjectCoords.get(i)[1]+signWidth/2);
				}
			}
		}
	}

	public void renderInfoWarnErrors() {
		for (int i = 0; i < totalNumMsgs; i++) {
			if (warnMessage[i] != null) {
				if (warnType[i] == 0) warnFont.setColor(0.1f, 0.9f, 0.1f, 1);
				else if (warnType[i] == 1) warnFont.setColor(1, 0.5f, 0, 1);
				else warnFont.setColor(1, 0, 0, 1);
				warnFont.draw(sb, warnMessage[i], toolbarWidth * 1.1f, SCRHEIGHT - (1.1f * i + 2) * warnHeight);
			}
		}
	}

	public void dispose() {
    	if (stage != null) stage.dispose();
    	if (skin != null) skin.dispose();
    	if (warnFont != null) warnFont.dispose();
    	if (signFont != null) signFont.dispose();
		if (textureFilled != null) textureFilled.dispose();
		if (pixMapPoly != null) pixMapPoly.dispose();
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	 /////////////////////////////////
	///                           ///
   ///   ALL TOOLBAR OPERATIONS  ///
  ///                           ///
 /////////////////////////////////

//	@SuppressWarnings("unchecked")
//	private int Redo(int index) {
//		// Increase
//		index += 1;
//		// Check if we've reached the maximum
//		if (index+1 == undoMax) {
//			Message("No more operations to redo!", 0);
//			return 0;
//		} else if (undoArray.get(index) == null) {
//			Message("No more operations to redo!", 0);
//			return 0;
//		}
//		// Update the variables
//		allPolygons = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(0)).clone();
//		allPolygonTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(1)).clone();
//		allPolygonPaths = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(2)).clone();
//		allPolygonTextures = (ArrayList<String>) ((ArrayList<String>) undoArray.get(index).get(3)).clone();
//		allObjects = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(4)).clone();
//		allObjectArrows = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(5)).clone();
//		allObjectCoords = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(6)).clone();
//		allObjectTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(7)).clone();
//		allDecors = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(8)).clone();
//		allDecorTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(9)).clone();
//		allDecorPolys = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(10)).clone();
//		return 1;
//	}
//
//	@SuppressWarnings("unchecked")
//	private int Undo(int index) {
//		// Decrease
//		index -= 1;
//		// Check if we've reached the maximum number of allowed undos
//		if (index < 0) {
//			Message("Cannot undo any more", 0);
//			return 0;
//		} else if (undoArray.get(index) == null) {
//			Message("Cannot undo any more (empty array)", 0);
//			return 0;
//		};
//		// Update the variables
//		// Update the variables
//		allPolygons = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(0)).clone();
//		allPolygonTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(1)).clone();
//		allPolygonPaths = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(2)).clone();
//		allPolygonTextures = (ArrayList<String>) ((ArrayList<String>) undoArray.get(index).get(3)).clone();
//		allObjects = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(4)).clone();
//		allObjectArrows = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(5)).clone();
//		allObjectCoords = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(6)).clone();
//		allObjectTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(7)).clone();
//		allDecors = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(index).get(8)).clone();
//		allDecorTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(9)).clone();
//		allDecorPolys = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(index).get(10)).clone();
//		return -1;
//	}
//
//	private int UpdateUndo(int index) {
//		// NOTE :: undoIndex represents the current index of undoArray
//		// So, if many changes have been made, and nothing has been undone, undoIndex should be undoMax-1,
//		// so that the final element of undoArray represents the latest change.
//		// If undo has been pressed once, then undoIndex represents the index of undoArray that is currently loaded on screen.
//		//
//		// Increment
//		index += 1;
//		// Construct a new array of current objects
//		ArrayList<Object> retarr = new ArrayList<Object>();
//		ArrayList<Object> tmparr;
//		retarr.add(allPolygons.clone());
//		retarr.add(allPolygonTypes.clone());
//		retarr.add(allPolygonPaths.clone());
//		retarr.add(allPolygonTextures.clone());
//		retarr.add(allObjects.clone());
//		retarr.add(allObjectArrows.clone());
//		retarr.add(allObjectCoords.clone());
//		retarr.add(allObjectTypes.clone());
//		retarr.add(allDecors.clone());
//		retarr.add(allDecorTypes.clone());
//		retarr.add(allDecorPolys.clone());
//		// Check if we've reached our limit
//		if (index == undoMax) {
//			// Shift everything down one
//			for (int i=0; i<undoMax-1; i++) {
//				tmparr = (ArrayList<Object>) undoArray.get(i+1).clone();
//				undoArray.set(i, (ArrayList<Object>) tmparr.clone());
//			}
//			// Now reset undoIndex to be the final element
//			index = undoMax-1;
//			return 0;
//		}
//		// Update the stored array
//		undoArray.set(index, (ArrayList<Object>) retarr.clone());
//		// Make everything null if a change is made
//		if (index+1 < undoMax) {
//			// Make everything else null
//			for (int i=index+1; i<undoMax; i++) {
//				undoArray.set(i, null);
//			}
//		}
//		return 1;
//	}

	@SuppressWarnings("unchecked")
	private void Redo() {
		if (undoIndex == undoCurrent) {
			Message("No more operations to redo!", 0);
			return;
		}
		// Increase
		undoIndex += 1;
		// Check if we've reached the maximum
		if (undoIndex == undoMax) {
			undoIndex = 0;
		}
		if (undoArray.get(undoIndex) == null) {
			Message("No more operations to redo!", 0);
			undoIndex -= 1;
			if (undoIndex < 0) undoIndex = undoMax-1;
			return;
		}
		// Update the variables
		allPolygons = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(0)).clone();
		allPolygonTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(1)).clone();
		allPolygonPaths = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(2)).clone();
		allPolygonTextures = (ArrayList<String>) ((ArrayList<String>) undoArray.get(undoIndex).get(3)).clone();
		allPolygonSprites = (ArrayList<PolygonSprite>) ((ArrayList<PolygonSprite>) undoArray.get(undoIndex).get(4)).clone();
		allObjects = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(5)).clone();
		allObjectArrows = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(6)).clone();
		allObjectCoords = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(7)).clone();
		allObjectTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(8)).clone();
		allDecors = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(9)).clone();
		allDecorTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(10)).clone();
		allDecorPolys = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(11)).clone();
		allDecorImages = (ArrayList<Object>) ((ArrayList<Object>) undoArray.get(undoIndex).get(12)).clone();
	}

	@SuppressWarnings("unchecked")
	private void Undo() {
		if ((undoIndex == undoCurrent+1) | ((undoIndex==0) & (undoCurrent==undoMax-1))) {
			Message("Cannot undo anymore!", 0);
			return;
		}
		// Decrease
		undoIndex -= 1;
		// Check if we've reached the maximum number of allowed undos
		if (undoIndex < 0) {
			undoIndex = undoMax-1;
		}
		if (undoArray.get(undoIndex) == null) {
			undoIndex += 1;
			if (undoIndex == undoMax) undoIndex = 0;
			Message("Cannot undo any more", 0);
			return;
		};
		// Update the variables
		allPolygons = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(0)).clone();
		allPolygonTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(1)).clone();
		allPolygonPaths = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(2)).clone();
		allPolygonTextures = (ArrayList<String>) ((ArrayList<String>) undoArray.get(undoIndex).get(3)).clone();
		allPolygonSprites = (ArrayList<PolygonSprite>) ((ArrayList<PolygonSprite>) undoArray.get(undoIndex).get(4)).clone();
		allObjects = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(5)).clone();
		allObjectArrows = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(6)).clone();
		allObjectCoords = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(7)).clone();
		allObjectTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(8)).clone();
		allDecors = (ArrayList<float[]>) ((ArrayList<float[]>) undoArray.get(undoIndex).get(9)).clone();
		allDecorTypes = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(10)).clone();
		allDecorPolys = (ArrayList<Integer>) ((ArrayList<Integer>) undoArray.get(undoIndex).get(11)).clone();
		allDecorImages = (ArrayList<Object>) ((ArrayList<Object>) undoArray.get(undoIndex).get(12)).clone();
	}

	private void UpdateUndo() {
		// NOTE :: undoIndex represents the current index of undoArray
		// So, if many changes have been made, and nothing has been undone, undoIndex should be undoMax-1,
		// so that the final element of undoArray represents the latest change.
		// If undo has been pressed once, then undoIndex represents the index of undoArray that is currently loaded on screen.
		//
		// Increment
		undoIndex += 1;
		// Construct a new array of current objects
		ArrayList<Object> retarr = new ArrayList<Object>();
		ArrayList<Object> tmparr;
		retarr.add(allPolygons.clone());
		retarr.add(allPolygonTypes.clone());
		retarr.add(allPolygonPaths.clone());
		retarr.add(allPolygonTextures.clone());
		retarr.add(allPolygonSprites.clone());
		retarr.add(allObjects.clone());
		retarr.add(allObjectArrows.clone());
		retarr.add(allObjectCoords.clone());
		retarr.add(allObjectTypes.clone());
		retarr.add(allDecors.clone());
		retarr.add(allDecorTypes.clone());
		retarr.add(allDecorPolys.clone());
		retarr.add(allDecorImages.clone());
		// Check if we've reached our limit
		if (undoIndex == undoMax) undoIndex = 0;
		// Set the new value
		undoArray.set(undoIndex, (ArrayList<Object>) retarr.clone());
		// Make everything null if a change is made
		if ((undoIndex == undoCurrent+1) | ((undoIndex==0) & (undoCurrent==undoMax-1))) {
			// All up to date
			undoCurrent = undoIndex;
			return;
		} else {
			if (undoCurrent > undoIndex) {
				// Make everything else null
				for (int i=undoIndex+1; i<=undoCurrent; i++) {
					undoArray.set(i, null);
				}				
			} else {
				for (int i=0; i<=undoCurrent; i++) {
					undoArray.set(i, null);
				}								
				for (int i=undoIndex+1; i<undoMax; i++) {
					undoArray.set(i, null);
				}
			}
		}
		undoCurrent = undoIndex;
	}

	private void SaveLevel(boolean autosave) {
		if (!CheckVertInt(autosave)) {
			// No intersections were found, so the level can be saved without errors
			try {
				if (autosave) {
					String isSaved = EditorIO.saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allPolygonTextures, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, allDecorImages,"autosave.lvl");
					// If a change has been made, update the undo arrays
					UpdateUndo();
					if (undoCurrent == undoMax) undoCurrent = 0;
				} else {
					String temptext = textInputSave.getText();
					if ((temptext == null) | (temptext.equals(""))) {
						Message("File not saved -- You must enter a filename", 2);
					} else {
						saveFName = temptext;
						String isSaved = EditorIO.saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allPolygonTextures, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, allDecorImages, saveFName+".lvl");
						if (!isSaved.equals("")) {
							Message(isSaved, 2);
						} else {
							Message("Level saved: "+saveFName+".lvl", 0);
							changesMade = false;
						}
						// Autosave as well
						isSaved = EditorIO.saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allPolygonTextures, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, allDecorImages, "autosave.lvl");
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void SetEmeralds() {
		// Calculate emeralds first
		numJewels = 0;
		for (int i=finishObjNumber; i<allObjectTypes.size(); i++) {
			if (allObjectTypes.get(i) == ObjectVars.Jewel) numJewels += 1;			
		}
		LevelVars.set(LevelVars.PROP_NUMJEWELS, Integer.toString(numJewels));		
	}

	private void Message(String msg, int mType) {
		if (warnNumber < totalNumMsgs) {
			warnMessage[warnNumber] = msg;
			warnElapse[warnNumber] = 0.0f;
			warnType[warnNumber] = mType;
			warnNumber += 1;
		}
	}

	public void ControlMode1() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		if ((modeChild.equals("Put")) & (GameInput.MBJUSTPRESSED==true)) {
			trcImgProp[0] = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			trcImgProp[1] = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		} else if ((modeChild.equals("Scale")) & (GameInput.MBDRAG==true)) {
			startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
			endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    		nullvarA = (float) (Math.sqrt((endX-trcImgProp[0])*(endX-trcImgProp[0]) + (endY-trcImgProp[1])*(endY-trcImgProp[1]))/Math.sqrt((startX-trcImgProp[0])*(startX-trcImgProp[0]) + (startY-trcImgProp[1])*(startY-trcImgProp[1])));
    		if (nullvarA < 0.0f) nullvarA *= -1.0f;
    		trcImgProp[2] = nullvarB*nullvarA;
    		trcImgProp[3] = nullvarC*nullvarA;
		} else if (modeChild.equals("Scale")) {
			nullvarB = trcImgProp[2];
			nullvarC = trcImgProp[3];
		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
			startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
			endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    		nullvarA = (float) Math.sqrt((endX-trcImgProp[0])*(endX-trcImgProp[0]) + (endY-trcImgProp[1])*(endY-trcImgProp[1]));
    		nullvarB = (float) Math.sqrt((startX-trcImgProp[0])*(startX-trcImgProp[0]) + (startY-trcImgProp[1])*(startY-trcImgProp[1]));
    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
    		if ((startX == trcImgProp[0]) & (startY == trcImgProp[1])) return; // No rotation
    		else if (startX == trcImgProp[0]) {
    			if (endX>startX) nullvarD *= -1.0f;
    			if (startY<trcImgProp[1]) nullvarD *= -1.0f;
    		} else {
    			if (endY < endX*((startY-trcImgProp[1])/(startX-trcImgProp[0])) + (startY - startX*((startY-trcImgProp[1])/(startX-trcImgProp[0])))) nullvarD *= -1.0f;
    			if (startX < trcImgProp[0]) nullvarD *= -1.0f;
    		}
    		// Set the angle
    		trcImgProp[5] = (float) Math.toDegrees(nullvarD);
		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true)) {
        	GameInput.MBRELEASE=false;
		}
	}

	public void ControlMode2() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString(); 
//		if (modeParent.equals("Collect Jewels")) {
//    		LevelVars.set(LevelVars.PROP_NUMJEWELS, modeChild);
//		} else if (modeParent.equals("Gravity")) {
		if (modeParent.equals("Gravity")) {
    		LevelVars.set(LevelVars.PROP_GRAVITY, modeChild);
		} else if (modeParent.equals("Ground Texture")) {
    		LevelVars.set(LevelVars.PROP_GROUND_TEXTURE, modeChild);
		} else if (modeParent.equals("Sky Texture")) {
    		LevelVars.set(LevelVars.PROP_SKY_TEXTURE, modeChild);
		} else if (modeParent.equals("Background Texture")) {
			LevelVars.set(LevelVars.PROP_BG_TEXTURE, modeChild);
		} else if (modeParent.equals("Level Bounds")) {
			if (((modeChild.equals("Set Bounds")) & (GameInput.MBDRAG==true))) {
				boundsBG[0] = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				boundsBG[1] = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			} else if (modeChild.equals("Reset Bounds")) {
				boundsBG = new float[] {0.0f, boundaryX};
			}
			if (boundsBG[0] > boundsBG[1]) {
				tempx = boundsBG[0];
				boundsBG[0] = boundsBG[1];
				boundsBG[1] = tempx;
			}
			if (boundsBG[0] < 0.0f) boundsBG[0] = 0.0f;
			if (boundsBG[0] > boundaryX) boundsBG[0] = boundaryX;
    		LevelVars.set(LevelVars.PROP_BG_BOUNDSX1, String.valueOf(boundsBG[0]));
    		LevelVars.set(LevelVars.PROP_BG_BOUNDSX2, String.valueOf(boundsBG[1]));
		} else if (modeParent.equals("Foreground Texture")) {
			LevelVars.set(LevelVars.PROP_FG_TEXTURE, modeChild);
		}
	}

	public void ControlMode3(int falling) {
		// Ordinary platforms - if falling is 1/2, this becomes a falling/trigger platform
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		if (modeParent.equals("Polygon")) {
			if (modeChild.equals("Add")) {
				if (GameInput.MBJUSTPRESSED) {
					if (drawingPoly) {
						DrawPolygon(-1);
					} else {
						polyDraw = new ArrayList<float[]>();
						drawingPoly = true;
						DrawPolygon(-1);
					}
				}
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectPolygon("up");
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG)) {
				if (polySelect == -1) {
					SelectPolygon("down");
					// Deal with the case when the user selects a trigger but not the trigger platform
					if ((polySelect != -1) & (triggerSelect == true)) polySelect = -1;
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom * (GameInput.MBDRAGX - startX) / BikeGame.SCALE;
					endY = -cam.zoom * (GameInput.MBDRAGY - startY) / BikeGame.SCALE;
					MovePolygon(polySelect, endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED == true) & (polySelect != -1)) {
//				System.out.println("-----------");
//				for (int i=0; i<allPolygons.get(polySelect).length; i++) {
//					System.out.println(allPolygons.get(polySelect)[i]);
//				}
				UpdatePolygon(polySelect, true);
				polySelect = -1;
			} else if ((modeChild.equals("Scale")) & (GameInput.MBDRAG == true)) {
				if (polySelect == -1) {
					SelectPolygon("down");
					startX = cam.position.x + cam.zoom * (GameInput.MBDOWNX / BikeGame.SCALE - 0.5f * SCRWIDTH);
					startY = cam.position.y - cam.zoom * (GameInput.MBDOWNY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				} else {
					endX = cam.position.x + cam.zoom * (GameInput.MBDRAGX / BikeGame.SCALE - 0.5f * SCRWIDTH);
					endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					nullvarA = (float) (Math.sqrt((endX - cursposx) * (endX - cursposx) + (endY - cursposy) * (endY - cursposy)) / Math.sqrt((startX - cursposx) * (startX - cursposx) + (startY - cursposy) * (startY - cursposy)));
					ScalePolygon(polySelect, nullvarA);
				}
			} else if ((modeChild.equals("Scale")) & (GameInput.MBJUSTPRESSED == true) & (polySelect != -1)) {
				UpdatePolygon(polySelect, true);
				polySelect = -1;
			} else if ((modeChild.equals("Flip x")) & (GameInput.MBJUSTPRESSED == true) & (polySelect == -1)) {
				SelectPolygon("up");
				if (polySelect != -1) {
					FlipPolygon(polySelect, "x");
					UpdatePolygon(polySelect, true);
					polySelect = -1;
				}
			} else if ((modeChild.equals("Flip y")) & (GameInput.MBJUSTPRESSED == true) & (polySelect == -1)) {
				SelectPolygon("up");
				if (polySelect != -1) {
					FlipPolygon(polySelect, "y");
					UpdatePolygon(polySelect, true);
					polySelect = -1;
				}
			} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG == true)) {
				if (polySelect == -1) {
					SelectPolygon("down");
					startX = cam.position.x + cam.zoom * (GameInput.MBDOWNX / BikeGame.SCALE - 0.5f * SCRWIDTH);
					startY = cam.position.y - cam.zoom * (GameInput.MBDOWNY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				} else {
					endX = cam.position.x + cam.zoom * (GameInput.MBDRAGX / BikeGame.SCALE - 0.5f * SCRWIDTH);
					endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					nullvarA = (float) Math.sqrt((endX - cursposx) * (endX - cursposx) + (endY - cursposy) * (endY - cursposy));
					nullvarB = (float) Math.sqrt((startX - cursposx) * (startX - cursposx) + (startY - cursposy) * (startY - cursposy));
					nullvarC = (float) Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
					nullvarD = (float) Math.acos((nullvarA * nullvarA + nullvarB * nullvarB - nullvarC * nullvarC) / (2.0f * nullvarA * nullvarB));
					if ((startX == cursposx) & (startY == cursposy)) return; // No rotation
					else if (startX == cursposx) {
						if (endX > startX) nullvarD *= -1.0f;
						if (startY < cursposy) nullvarD *= -1.0f;
					} else {
						if (endY < endX * ((startY - cursposy) / (startX - cursposx)) + (startY - startX * ((startY - cursposy) / (startX - cursposx))))
							nullvarD *= -1.0f;
						if (startX < cursposx) nullvarD *= -1.0f;
					}
					RotatePolygon(polySelect, nullvarD);
				}
			} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE == true) & (polySelect != -1)) {
				UpdatePolygon(polySelect, true);
				polySelect = -1;
				GameInput.MBRELEASE = false;
			} else if (modeChild.equals("Add Vertex")) {
				tempx = cam.position.x + cam.zoom * (GameInput.MBMOVEX / BikeGame.SCALE - 0.5f * SCRWIDTH);
				tempy = cam.position.y - cam.zoom * (GameInput.MBMOVEY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				if (GameInput.MBDRAG == true) {
					if (vertSelect == -1) {
						FindNearestSegment(false);
					} else {
						startX = cam.position.x + cam.zoom * (GameInput.MBDRAGX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						startY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						int segmNext = segmSelect + 1;
						if (segmNext == allPolygons.get(polySelect).length / 2) segmNext = 0;
						if (segmNext < segmSelect) AddVertex(polySelect, segmNext, segmSelect, startX, startY);
						else AddVertex(polySelect, segmSelect, segmNext, startX, startY);
					}
				} else if ((GameInput.MBJUSTPRESSED == true) & (polySelect != -1) & (vertSelect != -1)) {
					UpdatePolygon(polySelect, true);
					polySelect = -1;
					vertSelect = -1;
				} else FindNearestSegment(true);
			} else if (modeChild.equals("Delete Vertex")) {
				tempx = cam.position.x + cam.zoom * (GameInput.MBMOVEX / BikeGame.SCALE - 0.5f * SCRWIDTH);
				tempy = cam.position.y - cam.zoom * (GameInput.MBMOVEY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				if (engageDelete == false) {
					FindNearestVertex(true);
				}
				if (GameInput.MBJUSTPRESSED == true) {
					FindNearestVertex(false);
					engageDelete = true;
				}
			} else if (modeChild.equals("Move Vertex")) {
				tempx = cam.position.x + cam.zoom * (GameInput.MBMOVEX / BikeGame.SCALE - 0.5f * SCRWIDTH);
				tempy = cam.position.y - cam.zoom * (GameInput.MBMOVEY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				if (GameInput.MBDRAG == true) {
					if (vertSelect == -1) {
						FindNearestVertex(false);
						startX = GameInput.MBDOWNX;
						startY = GameInput.MBDOWNY;
					} else {
						endX = cam.zoom * (GameInput.MBDRAGX - startX) / BikeGame.SCALE;
						endY = -cam.zoom * (GameInput.MBDRAGY - startY) / BikeGame.SCALE;
						MoveVertex(polySelect, vertSelect, endX, endY);
					}
				} else if ((GameInput.MBJUSTPRESSED == true) & (polySelect != -1) & (vertSelect != -1)) {
					UpdatePolygon(polySelect, true);
					polySelect = -1;
					vertSelect = -1;
				} else FindNearestVertex(true);
			} else if (mode == 7) { // Some options that are only relevant to falling platforms
				// Falling platform
				if ((modeChild.equals("Set Sign")) & (GameInput.MBDRAG == true)) {
					if (polySelect == -1) SelectPolygon("down");
					else {
						tempx = cam.position.x + cam.zoom * (GameInput.MBDRAGX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						tempy = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						MoveFallingSign(polySelect);
					}
				} else if ((modeChild.equals("Set Sign")) & (GameInput.MBRELEASE == true) & (polySelect != -1)) {
					UpdatePath(polySelect);
					polySelect = -1;
					GameInput.MBRELEASE = false;
				} else if ((modeChild.equals("Set Fall Time")) & (GameInput.MBDRAG == true)) {
					if (polySelect == -1) SelectPolygon("down");
					else {
						endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						float fallTime = (allPolygonPaths.get(polySelect)[3] - endY) * B2DVars.EPPM;
						if (fallTime < 0.0f) fallTime = 0.0f;
						warnMessage[warnNumber] = "Fall time = " + String.format("%.2f", fallTime) + " seconds";
						warnElapse[warnNumber] = 0.0f;
						warnType[warnNumber] = 0;
						updatePath = allPolygonPaths.get(polySelect).clone();
						updatePath[0] = fallTime;
					}
				} else if ((modeChild.equals("Set Fall Time")) & (GameInput.MBRELEASE == true) & (polySelect != -1)) {
					UpdatePath(polySelect);
					polySelect = -1;
					GameInput.MBRELEASE = false;
				} else if ((modeChild.equals("Set Damping")) & (GameInput.MBDRAG == true)) {
					if (polySelect == -1) SelectPolygon("down");
					else {
						endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						float damping = (allPolygonPaths.get(polySelect)[3] - endY) * B2DVars.EPPM;
						if (damping > 0.0f) damping = 0.0f;
						warnMessage[warnNumber] = "Damping = " + String.format("%.2f", -damping) + " per second";
						warnElapse[warnNumber] = 0.0f;
						warnType[warnNumber] = 0;
						updatePath = allPolygonPaths.get(polySelect).clone();
						updatePath[1] = -damping;
					}
				} else if ((modeChild.equals("Set Damping")) & (GameInput.MBRELEASE == true) & (polySelect != -1)) {
					UpdatePath(polySelect);
					polySelect = -1;
					GameInput.MBRELEASE = false;
				}
			} else if (mode == 9) {
				// Trigger platform
				if ((modeChild.equals("Move Trigger")) & (GameInput.MBDRAG == true)) {
					if (polySelect == -1) {
						SelectPolygon("down");
						// Deal with the case when the user selects a trigger platform but not the trigger
						if ((polySelect != -1) & (triggerSelect == false)) polySelect = -1;
						startX = GameInput.MBDOWNX;
						startY = GameInput.MBDOWNY;
					} else {
						endX = cam.zoom * (GameInput.MBDRAGX - startX) / BikeGame.SCALE;
						endY = -cam.zoom * (GameInput.MBDRAGY - startY) / BikeGame.SCALE;
						MovePath(polySelect, endX, endY);
					}
				} else if ((modeChild.equals("Move Trigger")) & (GameInput.MBJUSTPRESSED == true) & (polySelect != -1) & (triggerSelect)) {
					UpdatePath(polySelect);
					polySelect = -1;
					triggerSelect = false;
				} else if ((modeChild.equals("Scale Trigger")) & (GameInput.MBDRAG == true)) {
					if (polySelect == -1) {
						SelectPolygon("down");
						startX = cam.position.x + cam.zoom * (GameInput.MBDOWNX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						startY = cam.position.y - cam.zoom * (GameInput.MBDOWNY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					} else if (triggerSelect) {
						endX = cam.position.x + cam.zoom * (GameInput.MBDRAGX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						nullvarA = (float) (Math.sqrt((endX - allPolygonPaths.get(polySelect)[2]) * (endX - allPolygonPaths.get(polySelect)[2]) + (endY - allPolygonPaths.get(polySelect)[3]) * (endY - allPolygonPaths.get(polySelect)[3])) / Math.sqrt((startX - allPolygonPaths.get(polySelect)[2]) * (startX - allPolygonPaths.get(polySelect)[2]) + (startY - allPolygonPaths.get(polySelect)[3]) * (startY - allPolygonPaths.get(polySelect)[3])));
						ScalePath(polySelect, nullvarA);
					}
				} else if ((modeChild.equals("Scale Trigger")) & (GameInput.MBJUSTPRESSED == true) & (polySelect != -1) & (triggerSelect)) {
					UpdatePath(polySelect);
					polySelect = -1;
					triggerSelect = false;
				} else if ((modeChild.equals("Rotate Trigger")) & (GameInput.MBDRAG == true)) {
					if (polySelect == -1) {
						SelectPolygon("down");
						startX = cam.position.x + cam.zoom * (GameInput.MBDOWNX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						startY = cam.position.y - cam.zoom * (GameInput.MBDOWNY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					} else {
						endX = cam.position.x + cam.zoom * (GameInput.MBDRAGX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						float trigx = allPolygonPaths.get(polySelect)[2];
						float trigy = allPolygonPaths.get(polySelect)[3];
						nullvarA = (float) Math.sqrt((endX - trigx) * (endX - trigx) + (endY - trigy) * (endY - trigy));
						nullvarB = (float) Math.sqrt((startX - trigx) * (startX - trigx) + (startY - trigy) * (startY - trigy));
						nullvarC = (float) Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
						nullvarD = (float) Math.acos((nullvarA * nullvarA + nullvarB * nullvarB - nullvarC * nullvarC) / (2.0f * nullvarA * nullvarB));
						if ((startX == trigx) & (startY == trigy)) return; // No rotation
						else if (startX == trigx) {
							if (endX > startX) nullvarD *= -1.0f;
							if (startY < trigy) nullvarD *= -1.0f;
						} else {
							if (endY < endX * ((startY - trigy) / (startX - trigx)) + (startY - startX * ((startY - trigy) / (startX - trigx))))
								nullvarD *= -1.0f;
							if (startX < trigx) nullvarD *= -1.0f;
						}
						RotatePath(polySelect, nullvarD);
					}
				} else if ((modeChild.equals("Rotate Trigger")) & (GameInput.MBRELEASE == true) & (polySelect != -1) & (triggerSelect)) {
					UpdatePath(polySelect);
					polySelect = -1;
					triggerSelect = false;
					GameInput.MBRELEASE = false;
				}
			}
		} else if (modeParent.equals("Rectangle")) {
			if (modeChild.equals("Add")) {
				if (GameInput.MBJUSTPRESSED) {
					if (drawingPoly) {
						tempx = cam.position.x + cam.zoom * (GameInput.MBMOVEX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						tempy = cam.position.y - cam.zoom * (GameInput.MBMOVEY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						shapeDraw[2] = tempx;
						shapeDraw[3] = shapeDraw[1];
						shapeDraw[4] = tempx;
						shapeDraw[5] = tempy;
						shapeDraw[6] = shapeDraw[0];
						shapeDraw[7] = tempy;
						if (falling == 0) AddPolygon(shapeDraw, 0, 8);
						else if (falling == 1) AddPolygon(shapeDraw, 4, 8);
						else if (falling == 2) AddPolygon(shapeDraw, 6, 8);
						drawingPoly = false;
						shapeDraw = null;
					} else {
						shapeDraw = new float[8];
						drawingPoly = true;
						shapeDraw[0] = cam.position.x + cam.zoom * (GameInput.MBUPX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						shapeDraw[1] = cam.position.y - cam.zoom * (GameInput.MBUPY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					}
				}
			}
		} else if (modeParent.equals("Circle")) {
			if (modeChild.equals("Add")) {
				if (GameInput.MBJUSTPRESSED) {
					if (drawingPoly == true) {
						tempx = cam.position.x + cam.zoom * (GameInput.MBMOVEX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						tempy = cam.position.y - cam.zoom * (GameInput.MBMOVEY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
						shapeDraw[2] = (float) Math.sqrt((tempx - shapeDraw[0]) * (tempx - shapeDraw[0]) + (tempy - shapeDraw[1]) * (tempy - shapeDraw[1]));
						if (falling == 0) AddPolygon(shapeDraw, 1, 3);
						else if (falling == 1) AddPolygon(shapeDraw, 5, 3);
						else if (falling == 2) AddPolygon(shapeDraw, 7, 3);
						drawingPoly = false;
						shapeDraw = null;
					} else {
						shapeDraw = new float[3];
						drawingPoly = true;
						shapeDraw[0] = cam.position.x + cam.zoom * (GameInput.MBUPX / BikeGame.SCALE - 0.5f * SCRWIDTH);
						shapeDraw[1] = cam.position.y - cam.zoom * (GameInput.MBUPY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					}
				}
			}
		} else if (modeParent.equals("Set Texture")) {
			if (GameInput.MBJUSTPRESSED) {
				SelectPolygon("down");
				if (polySelect != -1) {
					UpdatePlatformTexture();
					polySelect = -1;
				}
			}
			currentTexture = "";
		} else if (modeParent.equals("Set Type")) {
			if (GameInput.MBJUSTPRESSED) {
				SelectPolygon("down");
				if (polySelect != -1) {
					UpdatePlatformType();
					polySelect = -1;
				}
			}
		} else if (modeParent.equals("Set Color")) {
			if (GameInput.MBDRAG == true) {
				if (polySelect == -1) {
					SelectPolygon("down");
					if ((polySelect != -1) && !(allPolygonTextures.get(polySelect).startsWith("COLOR_"))) {
						platformColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
					} else if (polySelect == -1) {
						Message("No polygon selected", 1);
					} else if (allPolygonTextures.get(polySelect).startsWith("COLOR_")) {
						platformColor = ColorUtils.ConvertStringToColor(allPolygonTextures.get(polySelect));
					}
					startY = cam.position.y - cam.zoom * (GameInput.MBDOWNY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				} else {
					endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					float changeValue = 1.0f-(GameInput.MBDRAGY / BikeGame.SCALE)/SCRHEIGHT;
					if (modeChild.equals("Adjust red value")) {
						if (changeValue < 0.0f) platformColor[0] = 0.0f;
						else if (changeValue > 1.0f) platformColor[0] = 1.0f;
						else platformColor[0] = changeValue;
					} else if (modeChild.equals("Adjust green value")) {
						if (changeValue < 0.0f) platformColor[1] = 0.0f;
						else if (changeValue > 1.0f) platformColor[1] = 1.0f;
						else platformColor[1] = changeValue;
					} else if (modeChild.equals("Adjust blue value")) {
						if (changeValue < 0.0f) platformColor[2] = 0.0f;
						else if (changeValue > 1.0f) platformColor[2] = 1.0f;
						else platformColor[2] = changeValue;
					} else if (modeChild.equals("Adjust opacity")) {
						if (changeValue < 0.0f) platformColor[3] = 0.0f;
						else if (changeValue > 1.0f) platformColor[3] = 1.0f;
						else platformColor[3] = changeValue;
					}
					if (polySelect != -1) UpdatePlatformColor();
				}
			} else {
				if (GameInput.MBJUSTPRESSED) {
					SelectPolygon("down");
				} else if (polySelect != -1) {
					if (modeChild.equals("Set white")) {
						platformColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
					} else if (modeChild.equals("Set light grey")) {
						platformColor = new float[]{0.7f, 0.7f, 0.7f, 1.0f};
					} else if (modeChild.equals("Set dark grey")) {
						platformColor = new float[]{0.35f, 0.35f, 0.35f, 1.0f};
					} else if (modeChild.equals("Set black")) {
						platformColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set red")) {
						platformColor = new float[]{1.0f, 0.0f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set orange")) {
						platformColor = new float[]{1.0f, 0.8f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set yellow")) {
						platformColor = new float[]{1.0f, 1.0f, 0.1f, 1.0f};
					} else if (modeChild.equals("Set green")) {
						platformColor = new float[]{0.0f, 1.0f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set blue")) {
						platformColor = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
					} else if (modeChild.equals("Set purple")) {
						platformColor = new float[]{1.0f, 0.5f, 1.0f, 1.0f};
					} else if (modeChild.equals("Set invisible")) {
						platformColor = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
					}
					if (polySelect != -1) UpdatePlatformColor();
					polySelect = -1;
				}
			}
		}
	}

	public void ControlMode4() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString(); 
    	if (modeParent.equals("Polygon")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly == true) {
    	        		DrawPolygon(-1);        		
    	        	} else {
    	        		polyDraw = new ArrayList<float[]>();
    	        		drawingPoly = true;
    	        		DrawPolygon(-1);
    	        	}
    	    	}
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectPolygon("up");
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    				//startX = GameInput.MBDOWNX;
    				//startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
					//endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		//endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MovePolygon(polySelect, endX-startX, endY-startY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1)) {
    			float delx = updatePoly[0]-allPolygons.get(polySelect)[0];
    			float dely = updatePoly[1]-allPolygons.get(polySelect)[1];
    			updatePath = allPolygonPaths.get(polySelect).clone();
    			for (int j=4; j<updatePath.length; j++) {
    				if (j%2==0) updatePath[j] += delx;
    				else updatePath[j] += dely;
    			}
    			allPolygonPaths.set(polySelect, updatePath.clone());
    			updatePath = null;
    			UpdatePolygon(polySelect, true);
    			polySelect = -1;
    		} else if ((modeChild.equals("Flip x")) & (GameInput.MBJUSTPRESSED==true) & (polySelect == -1)) {
    			SelectPolygon("up");
    			if (polySelect != -1) {
	            	FlipPolygon(polySelect, "x");
	    			UpdatePolygon(polySelect, true);
	    			polySelect = -1;	            	
    			}
    		} else if ((modeChild.equals("Flip y")) & (GameInput.MBJUSTPRESSED==true) & (polySelect == -1)) {
    			SelectPolygon("up");
    			if (polySelect != -1) {
	            	FlipPolygon(polySelect, "y");
	    			UpdatePolygon(polySelect, true);
	    			polySelect = -1;	            	
    			}
    		} else if ((modeChild.equals("Scale")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) (Math.sqrt((endX-cursposx)*(endX-cursposx) + (endY-cursposy)*(endY-cursposy))/Math.sqrt((startX-cursposx)*(startX-cursposx) + (startY-cursposy)*(startY-cursposy)));
	            	ScalePolygon(polySelect, nullvarA);
    			}
    		} else if ((modeChild.equals("Scale")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1)) {
    			UpdatePolygon(polySelect, true);
    			polySelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-cursposx)*(endX-cursposx) + (endY-cursposy)*(endY-cursposy));
		    		nullvarB = (float) Math.sqrt((startX-cursposx)*(startX-cursposx) + (startY-cursposy)*(startY-cursposy));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == cursposx) & (startY == cursposy)) return; // No rotation
		    		else if (startX == cursposx) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<cursposy) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-cursposy)/(startX-cursposx)) + (startY - startX*((startY-cursposy)/(startX-cursposx)))) nullvarD *= -1.0f;
		    			if (startX < cursposx) nullvarD *= -1.0f;
		    		}
	            	RotatePolygon(polySelect, nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (polySelect != -1)) {
    			UpdatePolygon(polySelect, true);
    			polySelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if (modeChild.equals("Add Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestSegment(false);
        			} else {
            			startX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
            			startY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
            			int segmNext = segmSelect + 1;
            			if (segmNext==allPolygons.get(polySelect).length/2) segmNext = 0;
            			if (segmNext < segmSelect) AddVertex(polySelect, segmNext, segmSelect, startX, startY);
            			else AddVertex(polySelect, segmSelect, segmNext, startX, startY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1)) {
             			UpdatePolygon(polySelect, true);
             			polySelect = -1;
             			vertSelect = -1;
        		} else FindNearestSegment(true);
    		} else if (modeChild.equals("Delete Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			if (engageDelete==false) {
    				FindNearestVertex(true);
    			}
    			if (GameInput.MBJUSTPRESSED==true) {
    				FindNearestVertex(false);
    				engageDelete = true;
    			}
    		} else if (modeChild.equals("Move Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestVertex(false);
        				startX = GameInput.MBDOWNX;
        				startY = GameInput.MBDOWNY;
        			} else {
    					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
    		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
    	            	MoveVertex(polySelect, vertSelect, endX, endY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1)) {
        			UpdatePolygon(polySelect, true);
        			polySelect = -1;
        			vertSelect = -1;
        		} else FindNearestVertex(true);
    		}
    	} else if (modeParent.equals("Rectangle")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly) {
    	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    	    			shapeDraw[2] = tempx;
    	    			shapeDraw[3] = shapeDraw[1];
    	    			shapeDraw[4] = tempx;
    	    			shapeDraw[5] = tempy;
    	    			shapeDraw[6] = shapeDraw[0];
    	    			shapeDraw[7] = tempy;
    	    			AddPolygon(shapeDraw, 2, 8);
    			    	drawingPoly = false;
    			    	shapeDraw = null;
    	        	} else {
    	        		shapeDraw = new float[8];
    	        		drawingPoly = true;
    	        		shapeDraw[0] = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    	        		shapeDraw[1] = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    	        	}
    	    	}
    		}
    	} else if (modeParent.equals("Circle")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly == true) {
    	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    	    			shapeDraw[2] = (float) Math.sqrt((tempx-shapeDraw[0])*(tempx-shapeDraw[0]) + (tempy-shapeDraw[1])*(tempy-shapeDraw[1]));
    	    			AddPolygon(shapeDraw, 3, 3);
    			    	drawingPoly = false;
    			    	shapeDraw = null;
    	        	} else {
    	        		shapeDraw = new float[3];
    	        		drawingPoly = true;
    	        		shapeDraw[0] = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    	        		shapeDraw[1] = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    	        	}
    	    	}
    		}
    	} else if (modeParent.equals("Set Path")) {
			if (modeChild.equals("Select Polygon") & (GameInput.MBJUSTPRESSED==true)) {
				ghostPoly = null;
				SelectPolygon("up");
			} else if ((GameInput.MBJUSTPRESSED==true) & (modeChild.equals("Extend Path")) & (polySelect != -1)) {
				// Is this the first point being added?
				int sz = allPolygonPaths.get(polySelect).length;
				float[] newPath;
				if (sz == 6) {
					newPath = new float[10];
					for (int i=0; i<6; i++) newPath[i] = allPolygonPaths.get(polySelect)[i];
					newPath[6] = allPolygonPaths.get(polySelect)[4];
					newPath[7] = allPolygonPaths.get(polySelect)[5];
					newPath[8] = updatePathVertex[0];
					newPath[9] = updatePathVertex[1];
				} else {
					newPath = new float[sz+2];
					for (int i=0; i<6; i++) newPath[i] = allPolygonPaths.get(polySelect)[i];
					// Find which edge is closest to the point to be added
					nullvarA = (float) Math.sqrt((updatePathVertex[0]-allPolygonPaths.get(polySelect)[6])*(updatePathVertex[0]-allPolygonPaths.get(polySelect)[6]) + (updatePathVertex[1]-allPolygonPaths.get(polySelect)[7])*(updatePathVertex[1]-allPolygonPaths.get(polySelect)[7]));
					nullvarB = (float) Math.sqrt((updatePathVertex[0]-allPolygonPaths.get(polySelect)[sz-2])*(updatePathVertex[0]-allPolygonPaths.get(polySelect)[sz-2]) + (updatePathVertex[1]-allPolygonPaths.get(polySelect)[sz-1])*(updatePathVertex[1]-allPolygonPaths.get(polySelect)[sz-1]));
					if (nullvarA < nullvarB) {
						newPath[6] = updatePathVertex[0];
						newPath[7] = updatePathVertex[1];
						for (int i=8; i<sz+2; i++) newPath[i] = allPolygonPaths.get(polySelect)[i-2];
					} else {
						for (int i=6; i<sz; i++) newPath[i] = allPolygonPaths.get(polySelect)[i];						
						newPath[sz] = updatePathVertex[0];
						newPath[sz+1] = updatePathVertex[1];
					}
				}
				allPolygonPaths.set(polySelect, newPath.clone());
				updatePathVertex = null;
			} else if ((modeChild.equals("Extend Path")) & (polySelect != -1)) {
				updatePathVertex = new float[2];
				updatePathVertex[0] = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				updatePathVertex[1] = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
			} else if ((modeChild.equals("Move Path")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
				startX = GameInput.MBDOWNX;
				startY = GameInput.MBDOWNY;
				endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
				endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
				MovePath(polySelect, endX, endY);
    		} else if ((modeChild.equals("Move Path")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (updatePath!=null)) {
    			UpdatePath(polySelect);
    		} else if ((modeChild.equals("Rotate Path")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
   				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
   				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
	    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	    		tempx = allPolygonPaths.get(polySelect)[4];
	    		tempy = allPolygonPaths.get(polySelect)[5];;
	    		nullvarA = (float) Math.sqrt((endX-tempx)*(endX-tempx) + (endY-tempy)*(endY-tempy));
	    		nullvarB = (float) Math.sqrt((startX-tempx)*(startX-tempx) + (startY-tempy)*(startY-tempy));
	    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
	    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    	if ((startX == tempx) & (startY == tempy)) return; // No rotation
		    	else if (startX == tempx) {
		    		if (endX>startX) nullvarD *= -1.0f;
		    		if (startY<tempy) nullvarD *= -1.0f;
		    	} else {
		    		if (endY < endX*((startY-tempy)/(startX-tempx)) + (startY - startX*((startY-tempy)/(startX-tempx)))) nullvarD *= -1.0f;
		    		if (startX < tempx) nullvarD *= -1.0f;
		    	}
		    	RotatePath(polySelect, nullvarD);
    		} else if ((modeChild.equals("Rotate Path")) & (GameInput.MBRELEASE==true) & (polySelect != -1) & (updatePath!=null)) {
    			UpdatePath(polySelect);
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Scale Path")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
   				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
   				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
	    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	    		nullvarB = allPolygonPaths.get(polySelect)[4];
	    		nullvarC = allPolygonPaths.get(polySelect)[5];
	    		nullvarA = (float) (Math.sqrt((endX-nullvarB)*(endX-nullvarB) + (endY-nullvarC)*(endY-nullvarC))/Math.sqrt((startX-nullvarB)*(startX-nullvarB) + (startY-nullvarC)*(startY-nullvarC)));
            	ScalePath(polySelect, nullvarA);
    		} else if ((modeChild.equals("Scale Path")) & (GameInput.MBRELEASE==true) & (polySelect != -1) & (updatePath!=null)) {
    			UpdatePath(polySelect);
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Flip Path x")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1)) {
            	FlipPath(polySelect, "x");
    			UpdatePath(polySelect);
    		} else if ((modeChild.equals("Flip Path y")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1)) {
            	FlipPath(polySelect, "y");
    			UpdatePath(polySelect);
    		} else if ((modeChild.equals("Insert Vertex")) & (polySelect != -1)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) FindNearestSegmentPath(false, polySelect, false);
        			else {
            			startX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
            			startY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
            			AddVertexPath(polySelect, segmSelect, segmSelect+1, startX, startY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1) & (updatePath!=null)) {
        			UpdatePath(polySelect);
        			vertSelect = -1;
        		} else FindNearestSegmentPath(true, polySelect, false);
    		} else if ((modeChild.equals("Move Vertex")) & (polySelect != -1)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestVertexPath(false, polySelect);
        				startX = GameInput.MBDOWNX;
        				startY = GameInput.MBDOWNY;
        			} else {
    					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
    		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
    	            	MoveVertexPath(polySelect, vertSelect, endX, endY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1) & (updatePath!=null)) {
        			UpdatePath(polySelect);
        			vertSelect = -1;
        		} else FindNearestVertexPath(true, polySelect);
    		} else if ((modeChild.equals("Move Ghost")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
        		FindNearestSegmentPath(false, polySelect, true);
    		} else if ((modeChild.equals("Delete Vertex")) & (polySelect != -1)) {
    			if (allPolygonPaths.get(polySelect).length > 6) {
	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	    			if (engageDelete==false) FindNearestVertexPath(true, polySelect);
	    			if (GameInput.MBJUSTPRESSED==true) {
	    				FindNearestVertexPath(false, polySelect);
	    				engageDelete = true;
	    			}
    			}
    		} else if ((modeChild.equals("Set Rotation")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
    			float[] newArr = allPolygonPaths.get(polySelect).clone();
    			endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			float angle = PolygonOperations.GetAngle(newArr[4], newArr[5], endX, endY);
    			newArr[0] = MathUtils.radiansToDegrees*angle;
    			allPolygonPaths.set(polySelect, newArr.clone());
    		} else if ((modeChild.equals("Set Speed")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
    			float[] newArr = allPolygonPaths.get(polySelect).clone();
    			endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			newArr[1] = (float) Math.sqrt((newArr[4]-endX)*(newArr[4]-endX) + (newArr[5]-endY)*(newArr[5]-endY));
    			allPolygonPaths.set(polySelect, newArr.clone());
    		}
    	} else if (modeParent.equals("Set Texture")) {
	    	if (GameInput.MBJUSTPRESSED) {
	    		SelectPolygon("down");
	    		if (polySelect != -1) {
	    			UpdatePlatformTexture();
	    			polySelect = -1;
	    		}
	    	}
			currentTexture = "";
    	} else if (modeParent.equals("Set Color")) {
			if (GameInput.MBDRAG == true) {
				if (polySelect == -1) {
					SelectPolygon("down");
					if ((polySelect != -1) && !(allPolygonTextures.get(polySelect).startsWith("COLOR_"))) {
						platformColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
					} else if (polySelect == -1) {
						Message("No polygon selected", 1);
					} else if (allPolygonTextures.get(polySelect).startsWith("COLOR_")) {
						platformColor = ColorUtils.ConvertStringToColor(allPolygonTextures.get(polySelect));
					}
					startY = cam.position.y - cam.zoom * (GameInput.MBDOWNY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				} else {
					endY = cam.position.y - cam.zoom * (GameInput.MBDRAGY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
					float changeValue = 1.0f-(GameInput.MBDRAGY / BikeGame.SCALE)/SCRHEIGHT;
					if (modeChild.equals("Adjust red value")) {
						if (changeValue < 0.0f) platformColor[0] = 0.0f;
						else if (changeValue > 1.0f) platformColor[0] = 1.0f;
						else platformColor[0] = changeValue;
					} else if (modeChild.equals("Adjust green value")) {
						if (changeValue < 0.0f) platformColor[1] = 0.0f;
						else if (changeValue > 1.0f) platformColor[1] = 1.0f;
						else platformColor[1] = changeValue;
					} else if (modeChild.equals("Adjust blue value")) {
						if (changeValue < 0.0f) platformColor[2] = 0.0f;
						else if (changeValue > 1.0f) platformColor[2] = 1.0f;
						else platformColor[2] = changeValue;
					} else if (modeChild.equals("Adjust opacity")) {
						if (changeValue < 0.0f) platformColor[3] = 0.0f;
						else if (changeValue > 1.0f) platformColor[3] = 1.0f;
						else platformColor[3] = changeValue;
					}
					if (polySelect != -1) UpdatePlatformColor();
				}
			} else {
				if (GameInput.MBJUSTPRESSED) {
					SelectPolygon("down");
				} else if (polySelect != -1) {
					if (modeChild.equals("Set white")) {
						platformColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
					} else if (modeChild.equals("Set light grey")) {
						platformColor = new float[]{0.7f, 0.7f, 0.7f, 1.0f};
					} else if (modeChild.equals("Set dark grey")) {
						platformColor = new float[]{0.35f, 0.35f, 0.35f, 1.0f};
					} else if (modeChild.equals("Set black")) {
						platformColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set red")) {
						platformColor = new float[]{1.0f, 0.0f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set orange")) {
						platformColor = new float[]{1.0f, 0.8f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set yellow")) {
						platformColor = new float[]{1.0f, 1.0f, 0.1f, 1.0f};
					} else if (modeChild.equals("Set green")) {
						platformColor = new float[]{0.0f, 1.0f, 0.0f, 1.0f};
					} else if (modeChild.equals("Set blue")) {
						platformColor = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
					} else if (modeChild.equals("Set purple")) {
						platformColor = new float[]{1.0f, 0.5f, 1.0f, 1.0f};
					} else if (modeChild.equals("Set invisible")) {
						platformColor = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
					}
					if (polySelect != -1) UpdatePlatformColor();
					polySelect = -1;
				}
			}
		}
	}

	public void ControlMode5() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		if ((modeParent.equals("Ball & Chain")) | (modeParent.equals("Pendulum"))) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				if (modeParent.equals("Ball & Chain")) AddObject(ObjectVars.BallChain, tempx, tempy, -999.9f);
				else if (modeParent.equals("Pendulum")) AddObject(ObjectVars.Pendulum, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				if (modeParent.equals("Ball & Chain")) SelectObject("up", ObjectVars.BallChain, false, true);
				else if (modeParent.equals("Pendulum")) SelectObject("up", ObjectVars.Pendulum, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move Ball")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					if (modeParent.equals("Ball & Chain"))  SelectObject("down", ObjectVars.BallChain, false, true);
					else if (modeParent.equals("Pendulum")) SelectObject("down", ObjectVars.Pendulum, false, true);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveball", endX, endY);
				}
			} else if ((modeChild.equals("Move Ball")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveball", true);
				objectSelect = -1;
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					if (modeParent.equals("Ball & Chain"))  SelectObject("down", ObjectVars.BallChain, false, false);
					else if (modeParent.equals("Pendulum")) SelectObject("down", ObjectVars.Pendulum, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveanchor", endX, endY);
				}
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveanchor", true);
				objectSelect = -1;
			} else if ((modeChild.equals("Set Max Length")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					if (modeParent.equals("Ball & Chain"))  SelectObject("down", ObjectVars.BallChain, false, true);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveball", endX, endY);
				}
			} else if ((modeChild.equals("Set Max Length")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				newPoly = allObjects.get(objectSelect);
				float txv = newPoly[3] + 0.5f*newPoly[5]; 
				float tyv = newPoly[4] + 0.5f*newPoly[6];
				tempx = (float) Math.sqrt((updatePoly[0]-txv)*(updatePoly[0]-txv) + (updatePoly[1]-tyv)*(updatePoly[1]-tyv));
				tempy = (float) Math.sqrt((newPoly[0]-txv)*(newPoly[0]-txv) + (newPoly[1]-tyv)*(newPoly[1]-tyv));
				if (tempx < tempy) {
					UpdateObject(objectSelect, "moveball", true);
					newPoly = allObjects.get(objectSelect);
					tempx *= 1.00001f; // Make the maximum length just a fraction longer
				}
				newPoly[7] = tempx;
				allObjects.set(objectSelect, newPoly.clone());
				updatePoly = null;
				objectSelect = -1;
			}
		} else if (modeParent.equals("Boulder")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddObject(ObjectVars.Boulder, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Boulder, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Boulder, false, true);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
			}
		} else if (modeParent.equals("Bridge")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddObject(ObjectVars.Bridge, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Bridge, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Bridge, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveentry", endX, endY);
				}
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveentry", true);
				objectSelect = -1;
			} else if ((modeChild.equals("Set Bridge Sag")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Bridge, false, false);
					startY = GameInput.MBDOWNY;
				} else {
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		float bsag = 0.0f;
		    		if (tentry==1) bsag = (allObjects.get(objectSelect)[1]-endY)*B2DVars.EPPM;
		    		else if (tentry==2) bsag = (allObjects.get(objectSelect)[9]-endY)*B2DVars.EPPM;
		    		if (bsag < 0.0f) bsag = 0.0f;
					warnMessage[warnNumber] = "Bridge Sag = "+ String.format("%.2f", bsag)+" metres";
					warnElapse[warnNumber] = 0.0f;
					warnType[warnNumber] = 0;
					updatePoly = new float[4];
					tempx = 0.25f*(allObjects.get(objectSelect)[0]+allObjects.get(objectSelect)[4]+allObjects.get(objectSelect)[8]+allObjects.get(objectSelect)[12]);
					tempy = 0.25f*(allObjects.get(objectSelect)[1]+allObjects.get(objectSelect)[5]+allObjects.get(objectSelect)[9]+allObjects.get(objectSelect)[13]);
					updatePoly[0] = tempx;
					updatePoly[1] = tempy;
					updatePoly[2] = tempx;
					updatePoly[3] = tempy-bsag/B2DVars.EPPM;
				}
			} else if ((modeChild.equals("Set Bridge Sag")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				newPoly = allObjects.get(objectSelect).clone();
				newPoly[16] = updatePoly[1]-updatePoly[3];
				allObjects.set(objectSelect, newPoly.clone());
				newPoly = null;
				updatePoly = null;
				objectSelect = -1;
			}
		} else if (modeParent.equals("Crate")) {
    		if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			AddObject(ObjectVars.Crate, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.Crate, false, false);
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Crate, false, false);
    				startX = GameInput.MBDOWNX;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move", true);
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Crate, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allObjectCoords.get(objectSelect)[0])*(endX-allObjectCoords.get(objectSelect)[0]) + (endY-allObjectCoords.get(objectSelect)[1])*(endY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allObjectCoords.get(objectSelect)[0])*(startX-allObjectCoords.get(objectSelect)[0]) + (startY-allObjectCoords.get(objectSelect)[1])*(startY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allObjectCoords.get(objectSelect)[0]) & (startY == allObjectCoords.get(objectSelect)[1])) return; // No rotation
		    		else if (startX == allObjectCoords.get(objectSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allObjectCoords.get(objectSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])) + (startY - startX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allObjectCoords.get(objectSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "object", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotateobject", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Diamond")) {
			if ((modeChild.equals("Put")) & (GameInput.MBJUSTPRESSED)) {
				objectSelect = 2;
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				newPoly = ObjectVars.objectJewel.clone();
				ShiftObject(tempx, tempy);
				updatePoly = allObjects.set(objectSelect, newPoly.clone());
				newCoord = new float[2];
				newCoord[0] = tempx;
				newCoord[1] = tempy;
				newPoly = allObjectCoords.set(objectSelect, newCoord.clone());
				updatePoly = null;
				changesMade=true;
				SaveLevel(true);
				objectSelect = -1;
			}
		} else if (modeParent.equals("Doors/Keys")) {
//			if (modeParent.equals("Door (blue)")) ctype = ObjectVars.DoorBlue;
//			else if (modeParent.equals("Door (green)")) ctype = ObjectVars.DoorGreen;
//			else if (modeParent.equals("Door (red)")) ctype = ObjectVars.DoorRed;
//			else if (modeParent.equals("Key (blue)")) ctype = ObjectVars.KeyBlue;
//			else if (modeParent.equals("Key (green)")) ctype = ObjectVars.KeyGreen;
//			else if (modeParent.equals("Key (red)")) ctype = ObjectVars.KeyRed;
//			else System.out.println("Door/Key color not specified");
    		if ((modeChild.equals("Add Door")) & (GameInput.MBJUSTPRESSED)){
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			AddObject(ObjectVars.DoorBlue, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Add Key")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom * (GameInput.MBUPX / BikeGame.SCALE - 0.5f * SCRWIDTH);
				tempy = cam.position.y - cam.zoom * (GameInput.MBUPY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				AddObject(ObjectVars.KeyBlue, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectDoorKey("up", false, false);
    			if (objectSelect != -1) engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
					SelectDoorKey("down", false, false);
    				startX = GameInput.MBDOWNX;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move", true);
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
					SelectDoorKey("down", false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allObjectCoords.get(objectSelect)[0])*(endX-allObjectCoords.get(objectSelect)[0]) + (endY-allObjectCoords.get(objectSelect)[1])*(endY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allObjectCoords.get(objectSelect)[0])*(startX-allObjectCoords.get(objectSelect)[0]) + (startY-allObjectCoords.get(objectSelect)[1])*(startY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allObjectCoords.get(objectSelect)[0]) & (startY == allObjectCoords.get(objectSelect)[1])) return; // No rotation
		    		else if (startX == allObjectCoords.get(objectSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allObjectCoords.get(objectSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])) + (startY - startX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allObjectCoords.get(objectSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "object", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotateobject", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Set Colour")) & (GameInput.MBJUSTPRESSED)) {
				SelectDoorKey("up", false, false);
				if (objectSelect != - 1) {
					// Change the colour of the door/key
					IncrementObject(allObjectTypes.get(objectSelect));
					objectSelect = -1;
				}
			}
		} else if (modeParent.equals("Gate Switch")) {
    		if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			AddObject(ObjectVars.GateSwitch, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.GateSwitch, false, false);
    			engageDelete = true;
    		} else if ((modeChild.equals("Move Gate/Switch")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.GateSwitch, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveentry", endX, endY);
				}
			} else if ((modeChild.equals("Move Gate/Switch")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveentry", true);
				objectSelect = -1;
			} else if ((modeChild.equals("Rotate Gate/Switch")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.GateSwitch, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		xcen = 0.25f*(allObjects.get(objectSelect)[0+8*(tentry-1)] + allObjects.get(objectSelect)[2+8*(tentry-1)] + allObjects.get(objectSelect)[4+8*(tentry-1)] + allObjects.get(objectSelect)[6+8*(tentry-1)]);
		    		ycen = 0.25f*(allObjects.get(objectSelect)[1+8*(tentry-1)] + allObjects.get(objectSelect)[3+8*(tentry-1)] + allObjects.get(objectSelect)[5+8*(tentry-1)] + allObjects.get(objectSelect)[7+8*(tentry-1)]);
		    		nullvarA = (float) Math.sqrt((endX-xcen)*(endX-xcen) + (endY-ycen)*(endY-ycen));
		    		nullvarB = (float) Math.sqrt((startX-xcen)*(startX-xcen) + (startY-ycen)*(startY-ycen));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == xcen) & (startY == ycen)) return; // No rotation
		    		else if (startX == xcen) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<ycen) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-ycen)/(startX-xcen)) + (startY - startX*((startY-ycen)/(startX-xcen)))) nullvarD *= -1.0f;
		    			if (startX < xcen) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "rotateentry", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate Gate/Switch")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotateentry", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Scale Gate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) SelectObject("down", ObjectVars.GateSwitch, false, false);
    			else if (tentry==1) {
					xcen = 0.5f*(allObjects.get(objectSelect)[0] + allObjects.get(objectSelect)[4]);
					ycen = 0.5f*(allObjects.get(objectSelect)[1] + allObjects.get(objectSelect)[5]);
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = PolygonOperations.GetAngle(allObjects.get(objectSelect)[2], allObjects.get(objectSelect)[3], allObjects.get(objectSelect)[4], allObjects.get(objectSelect)[5]);
		    		nullvarB = PolygonOperations.GetAngle(xcen, ycen, endX, endY) - nullvarA;
		    		//nullvarB = 0.5f*PolygonOperations.SideLength(allObjects.get(objectSelect)[2], allObjects.get(objectSelect)[3], allObjects.get(objectSelect)[4], allObjects.get(objectSelect)[5]);
		    		nullvarC = (float) Math.abs(Math.cos(nullvarB)) * PolygonOperations.SideLength(xcen, ycen, endX, endY);
	            	ScaleObject(objectSelect, nullvarC, nullvarA);
    			}
    		} else if ((modeChild.equals("Scale Gate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1) & (tentry==1)) {
    			UpdateObject(objectSelect, "scalegate", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Gate Open/Close")) & (GameInput.MBRELEASE==true) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.GateSwitch, false, false);
    			if (objectSelect != -1) {
    				updatePoly = allObjects.get(objectSelect).clone();
    				updatePoly[17] = 1.0f-updatePoly[17];
    				newPoly = allObjects.set(objectSelect, updatePoly.clone());
        			// Nullify the update Polygon
        			updatePoly = null;
        			objectSelect = -1;
    			}
    			GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Flip Switch")) & (GameInput.MBRELEASE==true) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.GateSwitch, false, false);
    			if (objectSelect != -1) {
    				updatePoly = allObjects.get(objectSelect).clone();
    				updatePoly[16] *= -1.0f;
    				newPoly = allObjects.set(objectSelect, updatePoly.clone());
        			// Nullify the update Polygon
        			updatePoly = null;
        			objectSelect = -1;
    			}
    			GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Gravity")) {
    		if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			AddObject(ObjectVars.Gravity, tempx, tempy, 0.0f);
    		}
    		else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectGravity("up", ObjectVars.Gravity, false, false);
    			if (objectSelect==0) objectSelect = -1; // Cannot delete the initial gravity
    			else engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectGravity("down", ObjectVars.Gravity, false, false);
    				startX = GameInput.MBDOWNX;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move", true);
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectGravity("down", ObjectVars.Gravity, true, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allObjectCoords.get(objectSelect)[0])*(endX-allObjectCoords.get(objectSelect)[0]) + (endY-allObjectCoords.get(objectSelect)[1])*(endY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allObjectCoords.get(objectSelect)[0])*(startX-allObjectCoords.get(objectSelect)[0]) + (startY-allObjectCoords.get(objectSelect)[1])*(startY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allObjectCoords.get(objectSelect)[0]) & (startY == allObjectCoords.get(objectSelect)[1])) return; // No rotation
		    		else if (startX == allObjectCoords.get(objectSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allObjectCoords.get(objectSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])) + (startY - startX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allObjectCoords.get(objectSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "arrow", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotatearrow", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Next Item")) & (GameInput.MBJUSTPRESSED)) {
    			// Select the object
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectGravity("down", ObjectVars.Gravity, false, false);
				// Increment variation by 1
				if (objectSelect >= 3) {
					IncrementObject(allObjectTypes.get(objectSelect));
				}
			}
    	} else if (modeParent.equals("Emerald")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddObject(ObjectVars.Jewel, tempx, tempy, -999.9f);
				numJewels += 1;
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Jewel, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Jewel, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Jewel, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allObjectCoords.get(objectSelect)[0])*(endX-allObjectCoords.get(objectSelect)[0]) + (endY-allObjectCoords.get(objectSelect)[1])*(endY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allObjectCoords.get(objectSelect)[0])*(startX-allObjectCoords.get(objectSelect)[0]) + (startY-allObjectCoords.get(objectSelect)[1])*(startY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allObjectCoords.get(objectSelect)[0]) & (startY == allObjectCoords.get(objectSelect)[1])) return; // No rotation
		    		else if (startX == allObjectCoords.get(objectSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allObjectCoords.get(objectSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])) + (startY - startX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allObjectCoords.get(objectSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "object", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotateobject", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Log")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddObject(ObjectVars.Log, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Log, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Log, false, true);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
			}
		} else if (modeParent.equals("Nitrous")) {
    		if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			AddObject(ObjectVars.Nitrous, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.Nitrous, false, false);
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Nitrous, false, false);
    				startX = GameInput.MBDOWNX;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move", true);
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Nitrous, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allObjectCoords.get(objectSelect)[0])*(endX-allObjectCoords.get(objectSelect)[0]) + (endY-allObjectCoords.get(objectSelect)[1])*(endY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allObjectCoords.get(objectSelect)[0])*(startX-allObjectCoords.get(objectSelect)[0]) + (startY-allObjectCoords.get(objectSelect)[1])*(startY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allObjectCoords.get(objectSelect)[0]) & (startY == allObjectCoords.get(objectSelect)[1])) return; // No rotation
		    		else if (startX == allObjectCoords.get(objectSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allObjectCoords.get(objectSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])) + (startY - startX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allObjectCoords.get(objectSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "object", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotateobject", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Planet")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom * (GameInput.MBUPX / BikeGame.SCALE - 0.5f * SCRWIDTH);
				tempy = cam.position.y - cam.zoom * (GameInput.MBUPY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
				AddObject(ObjectVars.PlanetSun, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectPlanet("up", ObjectVars.PlanetSun, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectPlanet("down", ObjectVars.PlanetSun, false, true);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
					endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
					MoveObject(objectSelect, "moveplanet", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
			} else if ((modeChild.equals("Next Item")) & (GameInput.MBJUSTPRESSED)) {
				// Select the object
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectPlanet("down", ObjectVars.PlanetSun, false, true);
				// Increment variation by 1
				if (objectSelect != -1) {
					IncrementObject(allObjectTypes.get(objectSelect));
				}
			}
		} else if (modeParent.equals("Spike")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddObject(ObjectVars.Spike, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Spike, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Spike, false, true);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
			}
		} else if (modeParent.equals("Spike Zone")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddObject(ObjectVars.SpikeZone, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.SpikeZone, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.SpikeZone, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
    		} else if (modeChild.equals("Move Segment")) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	    		if (GameInput.MBDRAG==true) {
	    			if (vertSelect == -1) {
	    				FindNearestSegmentObject(false);
	    			} else {
	        			startX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
	        			startY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	        			int segmNext = segmSelect + 1;
	        			if (segmNext==allObjects.get(objectSelect).length/2) segmNext = 0;
	        			if (segmNext < segmSelect) MoveSegment(objectSelect, segmNext, segmSelect, startX, startY);
	        			else MoveSegment(objectSelect, segmSelect, segmNext, startX, startY);
	    			}
	    		} else if ((GameInput.MBJUSTPRESSED==true) & (objectSelect != -1) & (vertSelect != -1)) {
	         			UpdateObject(objectSelect, "update", true);
	         			objectSelect = -1;
	         			vertSelect = -1;
	    		} else FindNearestSegmentObject(true);
    		}
		} else if ((modeParent.equals("Transport")) | (modeParent.equals("Transport (invisible)"))) {
			int transIdx = ObjectVars.Transport;
			if (modeParent.equals("Transport (invisible)")) transIdx = ObjectVars.TransportInvisible;
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				if (modeParent.equals("Transport")) AddObject(transIdx, tempx, tempy, -999.9f);
				else if (modeParent.equals("Transport (invisible)")) AddObject(transIdx, tempx, tempy, 0.0f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectTransport("up", transIdx, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move Entry")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectTransport("down", transIdx, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else if (tentry != 0) {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveentry", endX, endY);
				}
			} else if ((modeChild.equals("Move Entry")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1) & (tentry!=0)) {
				UpdateObject(objectSelect, "moveentry", true);
				objectSelect = -1;
				tentry = 0;
			} else if ((modeChild.equals("Rotate Entry")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectTransport("down", transIdx, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		xcen = 0.5f*(allObjects.get(objectSelect)[0+8*(tentry-1)] + allObjects.get(objectSelect)[4+8*(tentry-1)]);
		    		ycen = 0.5f*(allObjects.get(objectSelect)[1+8*(tentry-1)] + allObjects.get(objectSelect)[5+8*(tentry-1)]);
		    		nullvarA = (float) Math.sqrt((endX-xcen)*(endX-xcen) + (endY-ycen)*(endY-ycen));
		    		nullvarB = (float) Math.sqrt((startX-xcen)*(startX-xcen) + (startY-ycen)*(startY-ycen));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == xcen) & (startY == ycen)) return; // No rotation
		    		else if (startX == xcen) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<ycen) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-ycen)/(startX-xcen)) + (startY - startX*((startY-ycen)/(startX-xcen)))) nullvarD *= -1.0f;
		    			if (startX < xcen) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "rotateentry", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate Entry")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotateentry", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Rotate Gravity")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectTransport("down", transIdx, true, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allObjectCoords.get(objectSelect)[0])*(endX-allObjectCoords.get(objectSelect)[0]) + (endY-allObjectCoords.get(objectSelect)[1])*(endY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allObjectCoords.get(objectSelect)[0])*(startX-allObjectCoords.get(objectSelect)[0]) + (startY-allObjectCoords.get(objectSelect)[1])*(startY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allObjectCoords.get(objectSelect)[0]) & (startY == allObjectCoords.get(objectSelect)[1])) return; // No rotation
		    		else if (startX == allObjectCoords.get(objectSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allObjectCoords.get(objectSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])) + (startY - startX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allObjectCoords.get(objectSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "arrow", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate Gravity")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotatearrow", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Next Item")) & (GameInput.MBJUSTPRESSED)) {
    			// Select the object
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectTransport("down", transIdx, false, false);
				// Increment variation by 1
				if (objectSelect != -1) {
					IncrementObject(allObjectTypes.get(objectSelect));
				}
			}
		} else if (modeParent.equals("Start")) {
			if ((modeChild.equals("Put")) & (GameInput.MBJUSTPRESSED)) {
				objectSelect = 1;
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				newPoly = ObjectVars.objectStart.clone();
				ShiftObject(tempx, tempy);
				updatePoly = allObjects.set(objectSelect, newPoly.clone());
				newCoord = new float[2];
				newCoord[0] = tempx;
				newCoord[1] = tempy;
				newPoly = allObjectCoords.set(objectSelect, newCoord.clone());
				newPoly = allObjectArrows.get(objectSelect);
				float xcen = 0.5f*(newPoly[0]+newPoly[12]);
				float ycen = 0.5f*(newPoly[1]+newPoly[13]);
				for (int i = 0; i<newPoly.length/2; i++) {
					newPoly[2*i] += (tempx-xcen);
					newPoly[2*i+1] += (tempy-ycen);
				}
				updatePoly = allObjectArrows.set(objectSelect, newPoly.clone());
				updatePoly = null;
				objectSelect = -1;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Start, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
					endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
					MoveObject(objectSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Start, true, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allObjectCoords.get(objectSelect)[0])*(endX-allObjectCoords.get(objectSelect)[0]) + (endY-allObjectCoords.get(objectSelect)[1])*(endY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allObjectCoords.get(objectSelect)[0])*(startX-allObjectCoords.get(objectSelect)[0]) + (startY-allObjectCoords.get(objectSelect)[1])*(startY-allObjectCoords.get(objectSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allObjectCoords.get(objectSelect)[0]) & (startY == allObjectCoords.get(objectSelect)[1])) return; // No rotation
		    		else if (startX == allObjectCoords.get(objectSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allObjectCoords.get(objectSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])) + (startY - startX*((startY-allObjectCoords.get(objectSelect)[1])/(startX-allObjectCoords.get(objectSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allObjectCoords.get(objectSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateObject(objectSelect, "arrow", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "rotatearrow", true);
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
    	} else if (modeParent.equals("Finish")) {
			if ((modeChild.equals("Put")) & (GameInput.MBJUSTPRESSED)) {
				objectSelect = finishObjNumber;
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				newPoly = ObjectVars.objectFinish.clone();
				ShiftObject(tempx, tempy);
				updatePoly = allObjects.set(objectSelect, newPoly.clone());
				newCoord = new float[2];
				newCoord[0] = tempx;
				newCoord[1] = tempy;
				newPoly = allObjectCoords.set(objectSelect, newCoord.clone());
				updatePoly = null;
				objectSelect = -1;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Finish, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
					endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
					MoveObject(objectSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move", true);
				objectSelect = -1;
			}
    	}
	}

	public void ControlMode6() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		if (modeParent.equals("Sign")) {
			int objNum = DecorVars.RoadSign_Stop;
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddDecor(objNum, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectDecorSign("up", objNum, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (decorSelect == -1) {
					SelectDecorSign("down", objNum, false, true);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveDecor(decorSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (decorSelect != -1)) {
				UpdateDecor(decorSelect, "movecircle");
				decorSelect = -1;
			} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (decorSelect == -1) {
    				SelectDecorSign("down", objNum, true, true);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-allDecors.get(decorSelect)[0])*(endX-allDecors.get(decorSelect)[0]) + (endY-allDecors.get(decorSelect)[1])*(endY-allDecors.get(decorSelect)[1]));
		    		nullvarB = (float) Math.sqrt((startX-allDecors.get(decorSelect)[0])*(startX-allDecors.get(decorSelect)[0]) + (startY-allDecors.get(decorSelect)[1])*(startY-allDecors.get(decorSelect)[1]));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == allDecors.get(decorSelect)[0]) & (startY == allDecors.get(decorSelect)[1])) return; // No rotation
		    		else if (startX == allDecors.get(decorSelect)[0]) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<allDecors.get(decorSelect)[1]) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-allDecors.get(decorSelect)[1])/(startX-allDecors.get(decorSelect)[0])) + (startY - startX*((startY-allDecors.get(decorSelect)[1])/(startX-allDecors.get(decorSelect)[0])))) nullvarD *= -1.0f;
		    			if (startX < allDecors.get(decorSelect)[0]) nullvarD *= -1.0f;
		    		}
	            	RotateDecor(decorSelect, "roadsign", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (decorSelect != -1)) {
    			UpdateDecor(decorSelect, "rotateobject");
    			decorSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Next Item")) & (GameInput.MBJUSTPRESSED)) {
				// Select the decoration
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectDecorSign("down", objNum, false, true);
				// Increment variation by 1
				if (decorSelect != -1) {
					IncrementDecorSign();
				}
			}
		} else if (modeParent.equals("Grass")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				AddDecor(DecorVars.Grass, tempx, tempy, -999.9f);
			} else if (modeChild.equals("Add")) {
				FindNearestSegment(true);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectDecor("up", DecorVars.Grass, false, false);
				if (decorSelect != -1) engageDelete = true;
    		} else if (modeChild.equals("Move Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestVertex(false);
        				startX = GameInput.MBDOWNX;
        				startY = GameInput.MBDOWNY;
        			} else {
    					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
    		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
    	            	MoveVertex(decorSelect, vertSelect, endX, endY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (decorSelect != -1) & (vertSelect != -1)) {
        			UpdateDecor(decorSelect, "move");
        			decorSelect = -1;
        			vertSelect = -1;
        		} else FindNearestVertex(true);
        	} else if (modeChild.equals("Delete All Grass")) {
        		clearGrass = true;
        	} else if (modeChild.equals("Add All Grass")) {
        		addGrass = true;
        	}
		} else if ((modeParent.equals("Rain")) | (modeParent.equals("Waterfall"))) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				if (modeParent.equals("Rain")) AddDecor(DecorVars.Rain, tempx, tempy, -999.9f);
				else if (modeParent.equals("Waterfall")) AddDecor(DecorVars.Waterfall, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				if (modeParent.equals("Rain")) SelectDecor("up", DecorVars.Rain, false, false);
				else if (modeParent.equals("Waterfall")) SelectDecor("up", DecorVars.Waterfall, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (decorSelect == -1) {
					if (modeParent.equals("Rain")) SelectDecor("down", DecorVars.Rain, false, false);
					else if (modeParent.equals("Waterfall")) SelectDecor("down", DecorVars.Waterfall, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveDecor(decorSelect, "WForRain", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (decorSelect != -1)) {
				UpdateDecor(decorSelect, "WForRain");
				decorSelect = -1;
    		} else if (modeChild.equals("Move Segment")) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	    		if (GameInput.MBDRAG==true) {
	    			if (vertSelect == -1) {
	    				FindNearestSegmentDecor(false);
	    			} else {
	        			startX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
	        			startY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	        			int segmNext = segmSelect + 1;
	        			if (segmNext==allDecors.get(decorSelect).length/2) segmNext = 0;
	        			if (segmNext < segmSelect) MoveSegment(decorSelect, segmNext, segmSelect, startX, startY);
	        			else MoveSegment(decorSelect, segmSelect, segmNext, startX, startY);
	    			}
	    		} else if ((GameInput.MBJUSTPRESSED) & (decorSelect != -1) & (vertSelect != -1)) {
	         			UpdateDecor(decorSelect, "WForRain");
	         			decorSelect = -1;
	         			vertSelect = -1;
	    		} else FindNearestSegmentDecor(true);
        	} else if ((modeChild.equals("Toggle FG/BG")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				if (modeParent.equals("Rain")) SelectDecor("up", DecorVars.Rain, false, false);
				else if (modeParent.equals("Waterfall")) SelectDecor("up", DecorVars.Waterfall, false, false);
				if (decorSelect != -1) {
					newPoly = allDecors.get(decorSelect).clone();
					newPoly[8] = 1-allDecors.get(decorSelect)[8];
					allDecors.set(decorSelect, newPoly.clone());
					if (allDecors.get(decorSelect)[8]<0.5f) Message("Decoration moved to background", 0);
					else Message("Decoration moved to foreground", 0);
					decorSelect = -1;
					newPoly = null;
				}
			}
		} else if (modeParent.equals("Bin Bag")) {
			int objNum;
			if (modeParent.equals("Bin Bag")) objNum=DecorVars.BinBag;
			else return;
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddDecor(objNum, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectDecor("up", objNum, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (decorSelect == -1) {
					SelectDecor("down", objNum, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveDecor(decorSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (decorSelect != -1)) {
				UpdateDecor(decorSelect, "move");
				decorSelect = -1;
			} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (decorSelect == -1) {
    				SelectDecor("down", objNum, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
    				float xcen = 0.5f*(allDecors.get(decorSelect)[0]+allDecors.get(decorSelect)[4]);
    				float ycen = 0.5f*(allDecors.get(decorSelect)[1]+allDecors.get(decorSelect)[5]);
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-xcen)*(endX-xcen) + (endY-ycen)*(endY-ycen));
		    		nullvarB = (float) Math.sqrt((startX-xcen)*(startX-xcen) + (startY-ycen)*(startY-ycen));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == xcen) & (startY == ycen)) return; // No rotation
		    		else if (startX == xcen) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<ycen) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-ycen)/(startX-xcen)) + (startY - startX*((startY-ycen)/(startX-xcen)))) nullvarD *= -1.0f;
		    			if (startX < xcen) nullvarD *= -1.0f;
		    		}
		    		RotateDecor(decorSelect, "rect", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (decorSelect != -1)) {
    			UpdateDecor(decorSelect, "rotateobject");
    			decorSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if ((modeParent.equals("Rock")) | (modeParent.equals("Tree")) | (modeParent.equals("Tyre Stack"))) {
			int objNum;
			if (modeParent.equals("Rock")) objNum=DecorVars.Rock;
			else if (modeParent.equals("Tree")) objNum=DecorVars.Tree;
			else if (modeParent.equals("Tyre Stack")) objNum=DecorVars.TyreStack;
			else return;
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				AddDecor(objNum, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectDecor("up", objNum, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (decorSelect == -1) {
					SelectDecor("down", objNum, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
					endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
					MoveDecor(decorSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Scale")) & (GameInput.MBJUSTPRESSED==true) & (decorSelect != -1)) {
				UpdateDecor(decorSelect, "scale");
				decorSelect = -1;
			} else if ((modeChild.equals("Scale")) & (GameInput.MBDRAG==true)) {
				if (decorSelect == -1) {
					SelectDecor("down", objNum, false, false);
					startX = GameInput.MBDOWNX;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX-startX)/BikeGame.SCALE;
					endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
					nullvarA = 0.5f*(allDecors.get(decorSelect)[0] + allDecors.get(decorSelect)[4]);
					nullvarB = 0.5f*(allDecors.get(decorSelect)[1] + allDecors.get(decorSelect)[5]);
					nullvarC = (float) (Math.sqrt((endX - nullvarA) * (endX - nullvarA) + (endY - nullvarB) * (endY - nullvarB)) / Math.sqrt((startX - nullvarA) * (startX - nullvarA) + (startY - nullvarB) * (startY - nullvarB)));
					ScaleDecor(decorSelect, nullvarC);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (decorSelect != -1)) {
				UpdateDecor(decorSelect, "move");
				decorSelect = -1;
			} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (decorSelect == -1) {
    				SelectDecor("down", objNum, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			} else {
    				float xcen = 0.5f*(allDecors.get(decorSelect)[0]+allDecors.get(decorSelect)[4]);
    				float ycen = 0.5f*(allDecors.get(decorSelect)[1]+allDecors.get(decorSelect)[5]);
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		    		nullvarA = (float) Math.sqrt((endX-xcen)*(endX-xcen) + (endY-ycen)*(endY-ycen));
		    		nullvarB = (float) Math.sqrt((startX-xcen)*(startX-xcen) + (startY-ycen)*(startY-ycen));
		    		nullvarC = (float) Math.sqrt((startX-endX)*(startX-endX) + (startY-endY)*(startY-endY));
		    		nullvarD = (float) Math.acos((nullvarA*nullvarA + nullvarB*nullvarB - nullvarC*nullvarC)/(2.0f*nullvarA*nullvarB));
		    		if ((startX == xcen) & (startY == ycen)) return; // No rotation
		    		else if (startX == xcen) {
		    			if (endX>startX) nullvarD *= -1.0f;
		    			if (startY<ycen) nullvarD *= -1.0f;
		    		} else {
		    			if (endY < endX*((startY-ycen)/(startX-xcen)) + (startY - startX*((startY-ycen)/(startX-xcen)))) nullvarD *= -1.0f;
		    			if (startX < xcen) nullvarD *= -1.0f;
		    		}
		    		RotateDecor(decorSelect, "rect", nullvarD);
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBRELEASE==true) & (decorSelect != -1)) {
    			UpdateDecor(decorSelect, "rotateobject");
    			decorSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Next Item")) & (GameInput.MBJUSTPRESSED)) {
    			// Select the decoration
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				SelectDecor("down", objNum, false, false);
				// Increment variation by 1
				if (decorSelect != -1) {
					IncrementDecor(objNum);
				}
			}
		}
	}

	public void ControlMode8() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		boolean doX = false;
		boolean doY = false;
		if ((modeChild.equals("Move X and Y")) | (modeChild.equals("Move X only"))) doX = true;
		if ((modeChild.equals("Move X and Y")) | (modeChild.equals("Move Y only"))) doY = true;
		if (modeParent.equals("Platform")) {
			if ((GameInput.MBJUSTPRESSED==true) & (polySelect == -1) & (groupPolySelect.size() == 0)) {
   				SelectPolygon("up");
   				if (polySelect != -1) {
   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
   	   				copyPoly = true;
   				}
			} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (updatePoly!=null)) {
				if ((startX==endX)&(startY==endY)) {
					warnMessage[warnNumber] = "Cannot paste platform exactly on top of the copy";
					warnElapse[warnNumber] = 0.0f;
					warnType[warnNumber] = 2;
					warnNumber += 1;
				} else {
					CopyPolygon(updatePoly.clone(),polySelect);
				}
				polySelect = -1;
				updatePoly = null;
				flipX=false;
				flipY=false;
				rotPoly=false;
				copyPoly = false;
			} else if (polySelect != -1) {
    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				updatePoly = allPolygons.get(polySelect).clone();
		    	CheckFlipRotate(false);
		    	if (allPolygonTypes.get(polySelect)%2 == 0) {
		    		for (int i = 0; i<allPolygons.get(polySelect).length; i++){
		    			if ((i%2==0)&(doX)) updatePoly[i] += (endX-startX);
		    			else if ((i%2==1)&(doY)) updatePoly[i] += (endY-startY);
		    		}
		    	} else if (allPolygonTypes.get(polySelect)%2 == 1) {
		    		if (doX) updatePoly[0] += (endX-startX);
		    		if (doY) updatePoly[1] += (endY-startY);
		    	}
			} else if ((GameInput.MBJUSTDRAGGED) & (polySelect == -1) & (groupPolySelect.size() == 0)) {
				SelectGroupPolygons(1);
				if (groupPolySelect.size() == 0) {
					warnMessage[warnNumber] = "No polygons inside selection box";
					warnElapse[warnNumber] = 0.0f;
					warnType[warnNumber] = 2;
					warnNumber += 1;
				} else {
   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
   	   				copyPoly = true;
				}
			} else if ((GameInput.MBJUSTPRESSED==true) & (groupPolySelect.size() != 0) & (updateGroupPoly!=null)) {
				if ((startX==endX)&(startY==endY)) {
					warnMessage[warnNumber] = "Cannot paste platform exactly on top of the copy";
					warnElapse[warnNumber] = 0.0f;
					warnType[warnNumber] = 2;
					warnNumber += 1;
				} else {
			    	for (int i=0; i < groupPolySelect.size(); i++) {
			    		CopyPolygon(updateGroupPoly.get(i).clone(), groupPolySelect.get(i));
			    	}
				}
				groupPolySelect = new ArrayList<Integer>();
				updateGroupPoly = null;
				flipX=false;
				flipY=false;
				rotPoly=false;
				copyPoly = false;
			} else if (groupPolySelect.size() != 0) {
    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
    			updateGroupPoly = new ArrayList<float[]>();
    			for (int i=0; i<groupPolySelect.size(); i++) {
    				updatePoly = allPolygons.get(groupPolySelect.get(i)).clone();
    		    	if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 0) {
			    		for (int j=0; j<allPolygons.get(groupPolySelect.get(i)).length; j++){
			    			if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
			    			else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
			    		}
			    	} else if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 1) {
			    		if (doX) updatePoly[0] += (endX-startX);
			    		if (doY) updatePoly[1] += (endY-startY);
			    	}
    				updateGroupPoly.add(updatePoly.clone());    				
    			}
    			CheckFlipRotate(false);
    			updatePoly = null;
			}
		} else if (modeParent.equals("Object")) {
			float shiftX = 0.0f;
			float shiftY = 0.0f;
			if ((GameInput.MBJUSTPRESSED==true) & (objectSelect == -1)) {
   				SelectObjectAny("up");
				if (objectSelect <= 3) objectSelect = -1;  // Can't select the starting objects
   				if (objectSelect != -1) {
   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
   				}
			} else if ((GameInput.MBJUSTPRESSED==true) & (objectSelect != -1) & (newPoly!=null)) {
				if ((startX==endX)&(startY==endY)) {
					warnMessage[warnNumber] = "Cannot paste object exactly on top of the copy";
					warnElapse[warnNumber] = 0.0f;
					warnType[warnNumber] = 2;
					warnNumber += 1;
				} else {
		    		if (doX) shiftX += (endX-startX);
		    		if (doY) shiftY += (endY-startY);
					CopyObject(newPoly.clone(), objectSelect, shiftX, shiftY);
				}
				objectSelect = -1;
				newPoly = null;
				updatePoly = null;
			} else if (objectSelect != -1) {
    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
	    		if (doX) shiftX += (endX-startX);
	    		if (doY) shiftY += (endY-startY);
    			MoveObjectCopy(objectSelect, shiftX, shiftY);
			}
		} else if (modeParent.equals("Collisionless")) {
			if ((GameInput.MBJUSTPRESSED==true) & (decorSelect == -1) & (groupPolySelect.size() == 0)) {
   				SelectDecor("up", -1, false, false);
   				if (decorSelect != -1) {
   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
   	   				copyPoly = true;
   				}
			} else if ((GameInput.MBJUSTPRESSED==true) & (decorSelect != -1) & (updatePoly!=null)) {
				if ((startX==endX)&(startY==endY)) {
					warnMessage[warnNumber] = "Cannot paste collisionless platform exactly on top of the copy";
					warnElapse[warnNumber] = 0.0f;
					warnType[warnNumber] = 2;
					warnNumber += 1;
				} else {
					CopyDecor(updatePoly.clone(), decorSelect);
				}
				decorSelect = -1;
				updatePoly = null;
				flipX=false;
				flipY=false;
				rotPoly=false;
				copyPoly = false;
			} else if (decorSelect != -1) {
    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				updatePoly = allDecors.get(decorSelect).clone();
		    	CheckFlipRotate(false);
	    		for (int i = 0; i<allDecors.get(decorSelect).length; i++){
	    			if ((i%2==0)&(doX)) updatePoly[i] += (endX-startX);
	    			else if ((i%2==1)&(doY)) updatePoly[i] += (endY-startY);
	    		}
			}
//			} else if ((GameInput.MBJUSTDRAGGED) & (polySelect == -1) & (groupPolySelect.size() == 0)) {
//				SelectGroupPolygons();
//				if (groupPolySelect.size() == 0) {
//					warnMessage[warnNumber] = "No polygons inside selection box";
//					warnElapse[warnNumber] = 0.0f;
//					warnType[warnNumber] = 2;
//					warnNumber += 1;
//				} else {
//   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
//   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
//   	   				copyPoly = true;
//				}
//			} else if ((GameInput.MBJUSTPRESSED==true) & (groupPolySelect.size() != 0) & (updateGroupPoly!=null)) {
//				if ((startX==endX)&(startY==endY)) {
//					warnMessage[warnNumber] = "Cannot paste platform exactly on top of the copy";
//					warnElapse[warnNumber] = 0.0f;
//					warnType[warnNumber] = 2;
//					warnNumber += 1;
//				} else {
//			    	for (int i=0; i < groupPolySelect.size(); i++) {
//			    		CopyPolygon(updateGroupPoly.get(i).clone(), groupPolySelect.get(i));
//			    	}
//				}
//				groupPolySelect = new ArrayList<Integer>();
//				updateGroupPoly = null;
//				flipX=false;
//				flipY=false;
//				rotPoly=false;
//				copyPoly = false;
//			} else if (groupPolySelect.size() != 0) {
//    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
//    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
//    			updateGroupPoly = new ArrayList<float[]>();
//    			for (int i=0; i<groupPolySelect.size(); i++) {
//    				updatePoly = allPolygons.get(groupPolySelect.get(i)).clone();
//    		    	if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 0) {
//			    		for (int j=0; j<allPolygons.get(groupPolySelect.get(i)).length; j++){
//			    			if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
//			    			else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
//			    		}
//			    	} else if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 1) {
//			    		if (doX) updatePoly[0] += (endX-startX);
//			    		if (doY) updatePoly[1] += (endY-startY);
//			    	}
//    				updateGroupPoly.add(updatePoly.clone());    				
//    			}
//    			CheckFlipRotate(false);
//    			updatePoly = null;
//			}
		}
	}

	public void ControlMode11() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		boolean doX = false;
		boolean doY = false;
		float shiftX, shiftY;
		int polidx;
		int otype;
		if ((modeChild.equals("Move X and Y")) | (modeChild.equals("Move X only"))) doX = true;
		if ((modeChild.equals("Move X and Y")) | (modeChild.equals("Move Y only"))) doY = true;
		if ((modeChild.equals("Copy X and Y")) | (modeChild.equals("Copy X only"))) doX = true;
		if ((modeChild.equals("Copy X and Y")) | (modeChild.equals("Copy Y only"))) doY = true;
		// Define the action
		int action = 0; // Copy=0, Delete=1, Move=2
		if (modeParent.equals("Copy")) action = 0;
		else if (modeParent.equals("Delete")) action = 1;
		else if (modeParent.equals("Move")) action = 2;
		// Perform the operation
		if ((GameInput.MBJUSTDRAGGED) && (groupArrays.size() == 0)) {
			if (action == 2) SelectGroup(0); // Allowed to move start/finish/diamond/initial gravity objects
			else SelectGroup(4); // cannot delete/copy the start/finish/diamond/initial gravity objects
			if (groupArrays.size() == 0) {
				warnMessage[warnNumber] = "Nothing inside selection box";
				warnElapse[warnNumber] = 0.0f;
				warnType[warnNumber] = 2;
				warnNumber += 1;
				copyPoly = false;
				engageDelete = false;
			} else {
				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				if (action==1) {
					boolean hasGrass = CheckGroupGrass();
					if (hasGrass) {
						Message("A selected polygon has grass on it. You should only", 1);
						Message("apply grass when the level is finished. Please delete", 1);
						Message("all grass: Decorate > Grass > Delete All Grass", 1);
						ResetGroups();
					} else {
						engageDelete = true;
						Message("Press 'd' to delete", 0);
					}
				}
				else copyPoly = true;
			}
		} else if ((GameInput.MBJUSTPRESSED) && (groupArrays.size() != 0) && (updateGroup != null)) {
			if ((startX==endX)&(startY==endY)) {
				if (action == 0) warnMessage[warnNumber] = "Cannot paste group exactly on top of the copy";
				else if (action == 2) warnMessage[warnNumber] = "Cannot move group exactly on top of the copy";
				else warnMessage[warnNumber] = "";
				warnElapse[warnNumber] = 0.0f;
				warnType[warnNumber] = 2;
				warnNumber += 1;
				copyPoly = false;
				engageDelete = false;
			} else {
				for (int i=0; i < groupArrays.size(); i++) {
					if (action == 0) {// Copy
						if (groupPOD.get(i) == 0) CopyPolygon(updateGroup.get(i).clone(), groupArrays.get(i));
						else if (groupPOD.get(i) == 1) {
							shiftX = 0.0f;
							shiftY = 0.0f;
							if (doX) shiftX = (endX-startX);
							if (doY) shiftY = (endY-startY);
							CopyObject(updateGroup.get(i).clone(), groupArrays.get(i), shiftX, shiftY);
						}
						else if (groupPOD.get(i) == 2) CopyDecor(updateGroup.get(i).clone(), groupArrays.get(i));
					} else if (action == 1) {// Delete
						engageDelete = false;
						//  Only need to reset the group arrays, which is done below (after this if loop)
					} else if (action == 2) {// Move
						shiftX = 0.0f;
						shiftY = 0.0f;
						if (doX) shiftX = (endX-startX);
						if (doY) shiftY = (endY-startY);
						if (groupPOD.get(i) == 0) {
							//polySelect = groupArrays.get(i);
							MovePolygon(groupArrays.get(i), shiftX, shiftY);
							newPoly = allPolygons.set(groupArrays.get(i), updatePoly.clone());
							MovePath(groupArrays.get(i), shiftX, shiftY);
							if (updatePath != null) newPoly = allPolygonPaths.set(groupArrays.get(i), updatePath.clone());
							//UpdatePolygon(groupArrays.get(i), false);
							//newPoly = null;
							//polySelect = -1;
						} else if (groupPOD.get(i) == 1) {
							otype = groupTypes.get(i);
							if ((otype==ObjectVars.BallChain) | (otype==ObjectVars.Pendulum)) {
								MoveObject(groupArrays.get(i), "moveball", shiftX, shiftY);
								UpdateObject(groupArrays.get(i), "moveball", false);
								MoveObject(groupArrays.get(i), "moveanchor", shiftX, shiftY);
								UpdateObject(groupArrays.get(i), "moveanchor", false);
							} else if (ObjectVars.IsPlanet(otype)) {
								MoveObject(groupArrays.get(i), "moveplanet", shiftX, shiftY);
								UpdateObject(groupArrays.get(i), "move", false);
							} else if ((otype==ObjectVars.Transport) | (ObjectVars.IsTransportInvisible(otype)) | (otype==ObjectVars.GateSwitch) | (otype==ObjectVars.Bridge)) {
								tentry = 1;
								MoveObject(groupArrays.get(i), "moveentry", shiftX, shiftY);
								UpdateObject(groupArrays.get(i), "moveentry", false);
								tentry = 2;
								MoveObject(groupArrays.get(i), "moveentry", shiftX, shiftY);
								UpdateObject(groupArrays.get(i), "moveentry", false);
								tentry = 0; // Reset
							} else if ((otype==ObjectVars.Boulder) | (otype==ObjectVars.Spike) | (otype==ObjectVars.Log)) {
								MoveObject(groupArrays.get(i), "circle", shiftX, shiftY);
								UpdateObject(groupArrays.get(i), "move", false);
							} else {
								MoveObject(groupArrays.get(i), "polygon", shiftX, shiftY);
								UpdateObject(groupArrays.get(i), "move", false);
							}
						} else if (groupPOD.get(i) == 2) {
							otype = groupTypes.get(i);
							MoveDecor(groupArrays.get(i), "mode", shiftX, shiftY);
							if (DecorVars.IsRoadSign(otype)) {
								UpdateDecor(groupArrays.get(i), "movecircle");
							} else {
								UpdateDecor(groupArrays.get(i), "move");
							}
						}
					}
				}
			}
			// Reset
			ResetGroups();
			SaveLevel(true);
		} else if (groupArrays.size() != 0) {
			if (action == 1) {
				// When deleting, just use the same position as the objects started
				endX = cam.position.x + cam.zoom * (GameInput.MBUPX / BikeGame.SCALE - 0.5f * SCRWIDTH);
				endY = cam.position.y - cam.zoom * (GameInput.MBUPY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
			} else {
				endX = cam.position.x + cam.zoom * (GameInput.MBMOVEX / BikeGame.SCALE - 0.5f * SCRWIDTH);
				endY = cam.position.y - cam.zoom * (GameInput.MBMOVEY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
			}
			updateGroup = new ArrayList<float[]>();
			for (int i=0; i<groupArrays.size(); i++) {
				polidx = groupArrays.get(i);
				if (groupPOD.get(i)==0) {
					updatePoly = allPolygons.get(polidx).clone();
					if (groupTypes.get(i)%2 == 0) {
						for (int j=0; j<allPolygons.get(polidx).length; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					} else if (groupTypes.get(i)%2 == 1) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
					}
				} else if (groupPOD.get(i)==1) {
					updatePoly = allObjects.get(polidx).clone();
					if (allObjects.get(polidx).length == 3) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
					} else if ((allObjectTypes.get(polidx)==ObjectVars.BallChain) | (allObjectTypes.get(polidx)==ObjectVars.Pendulum)) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
						if (doX) updatePoly[3] += (endX-startX);
						if (doY) updatePoly[4] += (endY-startY);
					} else if (allObjectTypes.get(polidx)==ObjectVars.Bridge) {
						for (int j=0; j<allObjects.get(polidx).length-1; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					} else {
						for (int j=0; j<allObjects.get(polidx).length; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					}
				} else if (groupPOD.get(i)==2) {
					updatePoly = allDecors.get(polidx).clone();
					if ((allDecors.get(polidx).length == 3) || (DecorVars.IsRoadSign(allDecorTypes.get(polidx)))) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
					} else if (DecorVars.IsRect(allDecorTypes.get(polidx))) {
						for (int j=0; j<allDecors.get(polidx).length-1; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					} else {
						for (int j=0; j<allDecors.get(polidx).length; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					}
				}
				updateGroup.add(updatePoly.clone());
			}
			updatePoly = null;
		}
	}

	public void ControlMode12() {
		boolean doX = false;
		boolean doY = false;
		float shiftX, shiftY;
		int polidx;
		int otype;
//		if ((modeChild.equals("Move X and Y")) | (modeChild.equals("Move X only"))) doX = true;
//		if ((modeChild.equals("Move X and Y")) | (modeChild.equals("Move Y only"))) doY = true;
//		if ((modeChild.equals("Copy X and Y")) | (modeChild.equals("Copy X only"))) doX = true;
//		if ((modeChild.equals("Copy X and Y")) | (modeChild.equals("Copy Y only"))) doY = true;
		doX = true;
		doY = true;
		if ((GameInput.MBJUSTDRAGGED) && (groupArrays.size() == 0)) {
			SelectGroup(4); // cannot copy the start/finish/diamond/initial gravity objects
			if (groupArrays.size() == 0) {
				warnMessage[warnNumber] = "Nothing inside selection box";
				warnElapse[warnNumber] = 0.0f;
				warnType[warnNumber] = 2;
				warnNumber += 1;
				loadedAlt = false;
				ResetAltObjects();
			} else {
				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
				loadedAlt = true;
				Message("Now click where you want to copy the objects", 0);
			}
		} else if ((GameInput.MBJUSTPRESSED) && (groupArrays.size() != 0) && (updateGroup != null)) {
			for (int i=0; i < groupArrays.size(); i++) {
				if (groupPOD.get(i) == 0) CopyPolygon(updateGroup.get(i).clone(), groupArrays.get(i));
				else if (groupPOD.get(i) == 1) {
					shiftX = 0.0f;
					shiftY = 0.0f;
					if (doX) shiftX = (endX-startX);
					if (doY) shiftY = (endY-startY);
					CopyObject(updateGroup.get(i).clone(), groupArrays.get(i), shiftX, shiftY);
				}
				else if (groupPOD.get(i) == 2) CopyDecor(updateGroup.get(i).clone(), groupArrays.get(i));
			}
			loadedAlt = false;
			ResetAltObjects();
			// Reset
			ResetGroups();
			SaveLevel(true);
		} else if (groupArrays.size() != 0) {
			endX = cam.position.x + cam.zoom * (GameInput.MBMOVEX / BikeGame.SCALE - 0.5f * SCRWIDTH);
			endY = cam.position.y - cam.zoom * (GameInput.MBMOVEY / BikeGame.SCALE - 0.5f * SCRHEIGHT);
			updateGroup = new ArrayList<float[]>();
			for (int i=0; i<groupArrays.size(); i++) {
				polidx = groupArrays.get(i);
				if (groupPOD.get(i)==0) {
					updatePoly = allPolygons_Alt.get(polidx).clone();
					if (groupTypes.get(i)%2 == 0) {
						for (int j=0; j<allPolygons_Alt.get(polidx).length; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					} else if (groupTypes.get(i)%2 == 1) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
					}
				} else if (groupPOD.get(i)==1) {
					updatePoly = allObjects_Alt.get(polidx).clone();
					if (allObjects_Alt.get(polidx).length == 3) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
					} else if ((allObjectTypes_Alt.get(polidx)==ObjectVars.BallChain) | (allObjectTypes_Alt.get(polidx)==ObjectVars.Pendulum)) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
						if (doX) updatePoly[3] += (endX-startX);
						if (doY) updatePoly[4] += (endY-startY);
					} else if (allObjectTypes_Alt.get(polidx)==ObjectVars.Bridge) {
						for (int j=0; j<allObjects_Alt.get(polidx).length-1; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					} else {
						for (int j=0; j<allObjects_Alt.get(polidx).length; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					}
				} else if (groupPOD.get(i)==2) {
					updatePoly = allDecors_Alt.get(polidx).clone();
					if ((allDecors_Alt.get(polidx).length == 3) || (DecorVars.IsRoadSign(allDecorTypes_Alt.get(polidx)))) {
						if (doX) updatePoly[0] += (endX-startX);
						if (doY) updatePoly[1] += (endY-startY);
					} else if (DecorVars.IsRect(allDecorTypes_Alt.get(polidx))) {
						for (int j=0; j<allDecors_Alt.get(polidx).length-1; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					} else {
						for (int j=0; j<allDecors_Alt.get(polidx).length; j++){
							if ((j%2==0)&(doX)) updatePoly[j] += (endX-startX);
							else if ((j%2==1)&(doY)) updatePoly[j] += (endY-startY);
						}
					}
				}
				updateGroup.add(updatePoly.clone());
			}
			updatePoly = null;
		}
	}

	public void UncheckButtons(boolean keybrd) {
		//buttonLoad.setChecked(false);
		buttonSave.setChecked(false);
		buttonExit.setChecked(false);
		buttonExecute.setChecked(false);
		buttonUndo.setChecked(false);
		buttonRedo.setChecked(false);
		buttonTraceImage.setChecked(false);
		buttonLevelProp.setChecked(false);
		buttonPan.setChecked(false);
		buttonAddStatic.setChecked(false);
		buttonAddKinetic.setChecked(false);
		buttonAddFalling.setChecked(false);
		buttonAddTrigger.setChecked(false);
		buttonCopyPaste.setChecked(false);
		buttonCopyFromLevel.setChecked(false);
		buttonGroupSelect.setChecked(false);
		buttonAddObject.setChecked(false);
		buttonDecorate.setChecked(false);
		//convexPolygons = null;
		if (!keybrd) enteringFilename = false;
	}

	public void ResetHoverSelect() {
		objectSelect = -1;
		decorSelect = -1;
		if (mode!=4) {
			polySelect = -1;
			groupPolySelect = new ArrayList<Integer>();
		}
		vertSelect = -1;
		segmSelect = -1;
		polyHover = -1;
		objectHover = -1;
		decorHover = -1;
		vertHover = -1;
		segmHover = -1;
		updatePathVertex = null;
		tentry = 0;
		triggerSelect = false;
	}

	public void SetChildList() {
		/* mode:
		 * = 1  -->  pan/zoom
		 * = 2  -->  change level properties
		 * = 3  -->  create static polygon
		 * = 4  -->  create kinetic polygon
		 * = 5  -->  add object
		 * = 6  -->  add decoration
		 * = 8  -->  copy and paste
		 */
		if (listParent.getSelected() != null) {
			modeParent = listParent.getSelected().toString();
			switch (mode) {
			case 1 :
				listChild.setItems("Put", "Scale", "Rotate");
				break;
			case 2 :
//				if (modeParent.equals("Collect Jewels")) {
//					jewelNumber = new String[numJewels+1];
//					for (int i = 0; i<numJewels+1; i++) jewelNumber[i] = String.format("%d", i);
//					listChild.setItems(jewelNumber);
//					pLevelIndex = GetListIndex("Collect Jewels",levelPropList);
//					if (Integer.parseInt(LevelVars.get(LevelVars.PROP_NUMJEWELS)) > numJewels) listChild.setSelectedIndex(0);
//					else listChild.setSelectedIndex(Integer.parseInt(LevelVars.get(LevelVars.PROP_NUMJEWELS)));
//				} else if (modeParent.equals("Gravity")) {
				if (modeParent.equals("Gravity")) {
					listChild.setItems(gravityList);
					pLevelIndex = GetListIndex("Gravity",levelPropList);
					listChild.setSelectedIndex(GetListIndex(LevelVars.get(LevelVars.PROP_GRAVITY),gravityList));
				} else if (modeParent.equals("Ground Texture")) {
					listChild.setItems(groundTextureList);
					pLevelIndex = GetListIndex("Ground Texture",levelPropList);
					listChild.setSelectedIndex(GetListIndex(LevelVars.get(LevelVars.PROP_GROUND_TEXTURE),groundTextureList));
				} else if (modeParent.equals("Sky Texture")) {
					listChild.setItems(skyTextureList);
					pLevelIndex = GetListIndex("Sky Texture",levelPropList);
					listChild.setSelectedIndex(GetListIndex(LevelVars.get(LevelVars.PROP_SKY_TEXTURE),skyTextureList));
				} else if (modeParent.equals("Background Texture")) {
					listChild.setItems(bgTextureList);
					pLevelIndex = GetListIndex("Background Texture", levelPropList);
					listChild.setSelectedIndex(GetListIndex(LevelVars.get(LevelVars.PROP_BG_TEXTURE),bgTextureList));
				} else if (modeParent.equals("Level Bounds")) {
					listChild.setItems("Set Bounds", "Reset Bounds");
					Message("Click and drag on the canvas to set the background texture bounds", 0);
					Message("This should be at least as wide as the level", 0);
				} else if (modeParent.equals("Foreground Texture")) {
					listChild.setItems(fgTextureList);
					pLevelIndex = GetListIndex("Foreground Texture", levelPropList);
					listChild.setSelectedIndex(GetListIndex(LevelVars.get(LevelVars.PROP_FG_TEXTURE),fgTextureList));
				} else listChild.setItems(nullList);				
				break;
			case 3 :
				if (modeParent.equals("Polygon")) {
					listChild.setItems(itemsADMRSFv);
					pStaticIndex = GetListIndex("Polygon",itemsPRCT);
				} else if (modeParent.equals("Rectangle")) {
					listChild.setItems("Add");
					pStaticIndex = GetListIndex("Rectangle",itemsPRCT);
				} else if (modeParent.equals("Circle")) {
					listChild.setItems("Add");
					pStaticIndex = GetListIndex("Circle",itemsPRCT);
				} else if (modeParent.equals("Set Color")) {
					listChild.setItems(platformColors);
					pStaticIndex = GetListIndex("Set Color", itemsPRCT);
					Message("Select an option then drag on a platform to change the color", 0);
				} else if (modeParent.equals("Set Texture")) {
					listChild.setItems(platformTextures);
					pStaticIndex = GetListIndex("Set Texture", itemsPRCT);
					Message("Select the texture, then click on the polygon to apply that texture", 0);
				} else if (modeParent.equals("Set Type")) {
					listChild.setItems("Static", "Background", "Foreground");
					pStaticIndex = GetListIndex("Set Type", itemsPRCT);
					Message("Select the platform type, then click on the polygon to set the type", 0);
				}
				break;
			case 4 : 
				if (modeParent.equals("Polygon")) {
					listChild.setItems(itemsADMRSFv);
					pKinematicIndex = GetListIndex("Polygon",itemsPRCP);
				} else if (modeParent.equals("Rectangle")) {
					listChild.setItems("Add");
					pKinematicIndex = GetListIndex("Rectangle",itemsPRCP);
				} else if (modeParent.equals("Circle")) {
					listChild.setItems("Add");
					pKinematicIndex = GetListIndex("Circle",itemsPRCP);
				} else if (modeParent.equals("Set Path")) {
					listChild.setItems("Select Polygon", "Extend Path", "Move Path", "Rotate Path", "Scale Path", "Flip Path x", "Flip Path y", "Insert Vertex", "Move Vertex", "Delete Vertex", "Flip Direction", "Flip Rotation", "Set Rotation", "Set Speed", "Move Ghost");
					pKinematicIndex = GetListIndex("Set Path",itemsPRCP);
				} else if (modeParent.equals("Set Texture")) {
					listChild.setItems(platformTextures);
					pKinematicIndex = GetListIndex("Set Texture",itemsPRCP);
		    		Message("Select the texture, then click on the polygon to apply that texture", 0);
				} else if (modeParent.equals("Set Color")) {
					listChild.setItems(platformColors);
					pKinematicIndex = GetListIndex("Set Color", itemsPRCP);
					Message("Select an option then drag on a platform to change the color", 0);
				}
				break;
			case 5 :
				if (modeParent.equals("Ball & Chain")) {
					listChild.setItems("Add", "Delete", "Move Ball", "Move Anchor", "Set Max Length");
					pObjectIndex = GetListIndex("Ball & Chain",objectList);
				} else if (modeParent.equals("Boulder")) {
					listChild.setItems(itemsADM);
					pObjectIndex = GetListIndex("Boulder",objectList);
				} else if (modeParent.equals("Bridge")) {
					listChild.setItems("Add", "Delete", "Move Anchor", "Set Bridge Sag");
					pObjectIndex = GetListIndex("Bridge",objectList);
				} else if (modeParent.equals("Crate")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Crate",objectList);
				} else if (modeParent.equals("Diamond")) {
					listChild.setItems("Put");
					pObjectIndex = GetListIndex("Emerald",objectList);
				} else if (modeParent.equals("Doors/Keys")) {
					listChild.setItems("Add Door", "Add Key", "Delete", "Move", "Rotate", "Set Colour");
					pObjectIndex = GetListIndex("Doors/Keys",objectList);
				} else if (modeParent.equals("Gate Switch")) {
					listChild.setItems("Add", "Delete", "Move Gate/Switch", "Rotate Gate/Switch", "Scale Gate", "Gate Open/Close", "Flip Switch");
					pObjectIndex = GetListIndex("Gate Switch",objectList);
				} else if (modeParent.equals("Gravity")) {
					listChild.setItems("Add", "Delete", "Move", "Next Item", "Rotate");
					pObjectIndex = GetListIndex("",objectList);
				} else if (modeParent.equals("Emerald")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Emerald",objectList);
				} else if (modeParent.equals("Log")) {
					listChild.setItems(itemsADM);
					pObjectIndex = GetListIndex("Log",objectList);
				} else if (modeParent.equals("Nitrous")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Nitrous",objectList);
				} else if (modeParent.equals("Pendulum")) {
					listChild.setItems("Add", "Delete", "Move Ball", "Move Anchor");
					pObjectIndex = GetListIndex("Pendulum",objectList);
				} else if (modeParent.equals("Planet")) {
					listChild.setItems("Add", "Delete", "Move", "Next Item", "Rotate");
					pObjectIndex = GetListIndex("Planet",objectList);
				} else if (modeParent.equals("Spike")) {
					listChild.setItems(itemsADM);
					pObjectIndex = GetListIndex("Spike",objectList);
				} else if (modeParent.equals("Spike Zone")) {
					listChild.setItems("Add", "Delete", "Move", "Move Segment");
					pObjectIndex = GetListIndex("Spike Zone",objectList);
				} else if (modeParent.equals("Transport")) {
					listChild.setItems("Add", "Delete", "Move Entry", "Rotate Entry");
					pObjectIndex = GetListIndex("Transport",objectList);
				} else if (modeParent.equals("Transport (invisible)")) {
					listChild.setItems("Add", "Delete", "Move Entry", "Next Item", "Rotate Entry", "Rotate Gravity");
					pObjectIndex = GetListIndex("Transport (invisible)",objectList);
				} else if (modeParent.equals("Start")) {
					listChild.setItems("Put", "Move", "Rotate", "Flip Direction");
					pObjectIndex = GetListIndex("Start",objectList);
				} else if (modeParent.equals("Finish")) {
					listChild.setItems("Put","Move");
					pObjectIndex = GetListIndex("Finish",objectList);
				} else if (modeParent.equals("")) {
					listChild.setItems(nullList);
					pObjectIndex = GetListIndex("",objectList);
				} else if (modeParent.equals("")) {
					listChild.setItems(nullList);
					pObjectIndex = GetListIndex("",objectList);
				} else if (modeParent.equals("")) {
					listChild.setItems(nullList);
					pObjectIndex = GetListIndex("",objectList);
				} else listChild.setItems(nullList);
				break;
			case 6 :
				if (modeParent.startsWith("Sign")) {
					listChild.setItems("Add", "Delete", "Move", "Next Item", "Rotate");
				} else if (modeParent.equals("Grass")) {
					listChild.setItems("Add", "Delete", "Move Vertex", "Add All Grass", "Delete All Grass");
				} else if (modeParent.equals("Rain")) {
					listChild.setItems("Add", "Delete", "Move", "Move Segment", "Toggle FG/BG");
				} else if (modeParent.equals("Waterfall")) {
					listChild.setItems("Add", "Delete", "Move", "Move Segment", "Toggle FG/BG");
				} else if (modeParent.equals("Bin Bag")) {
					listChild.setItems(itemsADMR);
				} else if ((modeParent.equals("Rock")) || (modeParent.equals("Tree")) || (modeParent.equals("Tyre Stack"))) {
					listChild.setItems("Add", "Delete", "Move", "Next Item", "Rotate", "Scale");
				} else listChild.setItems(itemsADMR);
				break;
			case 7 :
				if (modeParent.equals("Polygon")) {
					listChild.setItems("Add", "Delete", "Move", "Rotate", "Scale", "Flip x", "Flip y", "Add Vertex", "Delete Vertex", "Move Vertex", "Set Sign", "Set Fall Time", "Set Damping");
					pFallingIndex = GetListIndex("Polygon",itemsPRC);
				} else if (modeParent.equals("Rectangle")) {
					listChild.setItems("Add");
					pFallingIndex = GetListIndex("Rectangle",itemsPRC);
				} else if (modeParent.equals("Circle")) {
					listChild.setItems("Add");
					pFallingIndex = GetListIndex("Circle",itemsPRC);
				} else if (modeParent.equals("Set Texture")) {
					listChild.setItems(platformTextures);
					pFallingIndex = GetListIndex("Set Texture", itemsPRC);
		    		Message("Select the texture, then click on the polygon to apply that texture", 0);
				} else if (modeParent.equals("Set Color")) {
					listChild.setItems(platformColors);
					pFallingIndex = GetListIndex("Set Color", itemsPRC);
					Message("Select an option then drag on a platform to change the color", 0);
				}
				break;
			case 8 :
				if (modeParent.equals("Platform")) {
					listChild.setItems(itemsXYonly);
				} else if (modeParent.equals("Object")) {
					listChild.setItems(itemsXYonly);
				}
				break;
			case 9 :
				if (modeParent.equals("Polygon")) {
					listChild.setItems("Add", "Delete", "Move", "Rotate", "Scale", "Flip x", "Flip y", "Add Vertex", "Delete Vertex", "Move Vertex", "Move Trigger", "Rotate Trigger", "Scale Trigger");
					pTriggerIndex = GetListIndex("Polygon",itemsPRC);
				} else if (modeParent.equals("Rectangle")) {
					listChild.setItems("Add");
					pTriggerIndex = GetListIndex("Rectangle",itemsPRC);
				} else if (modeParent.equals("Circle")) {
					listChild.setItems("Add");
					pTriggerIndex = GetListIndex("Circle",itemsPRC);
				} else if (modeParent.equals("Set Texture")) {
					listChild.setItems(platformTextures);
					pTriggerIndex = GetListIndex("Set Texture", itemsPRC);
		    		Message("Select the texture, then click on the polygon to apply that texture", 0);
				} else if (modeParent.equals("Set Color")) {
					listChild.setItems(platformColors);
					pTriggerIndex = GetListIndex("Set Color", itemsPRC);
					Message("Select an option then drag on a platform to change the color", 0);
				}
				break;
			case 10 :
				listChild.setItems(nullList);
				break;
			case 11 :
				if (modeParent.equals("Copy")) {
					listChild.setItems("Copy X and Y", "Copy X only", "Copy Y only");
				} else if (modeParent.equals("Delete")) {
					listChild.setItems(" ");
				} else if (modeParent.equals("Move")) {
					listChild.setItems("Move X and Y", "Move X only", "Move Y only");
				} else listChild.setItems(nullList);
				break;
			case 12 :
				listChild.setItems(nullList);
				break;
			default :
				listChild.setItems(nullList);
				break;
			}
		} else listChild.setItems(nullList);

	}

     /////////////////////////////////////////
    ///                                   ///
   ///   ALL POLYGON/VERTEX OPERATIONS   ///
  ///                                   ///
 /////////////////////////////////////////

	public void UpdatePlatformTexture() {
		currentTexture = listChild.getSelected().toString();
		if (currentTexture.equals("Default")) {
			allPolygonTextures.set(polySelect, "");
		} else {
			allPolygonTextures.set(polySelect, currentTexture);
		}
	}

	public void UpdatePlatformType() {
		String platformType = listChild.getSelected().toString();
		int addExtra = 0;
		if (allPolygonTypes.get(polySelect)%2==0) addExtra = 0;
		else if (allPolygonTypes.get(polySelect)%2==1) addExtra = 1;
		if (platformType.equals("Static")) {
			allPolygonTypes.set(polySelect, addExtra);
		} else if (platformType.equals("Background")) {
			allPolygonTypes.set(polySelect, 8+addExtra);
		} else if (platformType.equals("Foreground")) {
			allPolygonTypes.set(polySelect, 10+addExtra);
		}
	}

	public void UpdatePlatformColor() {
		String colorString = String.format("COLOR_%1$f_%2$f_%3$f_%4$f",platformColor[0],platformColor[1],platformColor[2],platformColor[3]);
		allPolygonTextures.set(polySelect, colorString);
		MakePolygonSprite(polySelect);
	}

	public void MakePolygonSprite(int pint) {
		if ((allPolygonTextures.get(pint).startsWith("COLOR_")) && (allPolygonTypes.get(pint)%2==0)) {
			pixMapPoly = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pixMapPoly.setColor(1, 1, 1, 1);
			pixMapPoly.fill();
			textureFilled = new Texture(pixMapPoly);
			TextureRegion texturePolyRegion = new TextureRegion(textureFilled);
			// Dispose of unwanted variables
			EarClippingTriangulator triangulator = new EarClippingTriangulator();
			short [] triangleIndices = triangulator.computeTriangles(allPolygons.get(pint)).toArray();
			PolygonRegion polyReg = new PolygonRegion(texturePolyRegion, allPolygons.get(pint), triangleIndices);
			PolygonSprite polySpr = new PolygonSprite(polyReg);
			allPolygonSprites.set(pint, polySpr);
		}
	}

	public void AddPolygon(float[] newPoly, int ptype, int psize) {
    	changesMade = true;
    	if (mode == 6) {
    		allDecors.add(newPoly);
    		allDecorTypes.add(ptype);
    		allDecorPolys.add(0);
    		SaveLevel(true);
    		return;
    	} else {
			allPolygons.add(newPoly);
			allPolygonTypes.add(ptype);
			allPolygonTextures.add("");
			allPolygonSprites.add(null);
    	}
		float xcenp = 0.0f, ycenp = 0.0f;
		if (mode==4) {
			if (newPoly.length==3) {
				xcenp = newPoly[0];
				ycenp = newPoly[1];
			} else {
				for (int i=0;i<newPoly.length/2;i++) {
					xcenp += newPoly[2*i];
					ycenp += newPoly[2*i+1];
				}
				xcenp /= newPoly.length/2;
				ycenp /= newPoly.length/2;
			}
			float[] newArr = {0.0f,100.0f,1.0f,1.0f,xcenp,ycenp};
			allPolygonPaths.add(newArr.clone());
		} else if (mode==7) {
			// Falling platform
			int imax = 0;
			float maxv = -10000.0f;
			if (ptype==4) {
				for (int i=0; i<newPoly.length/2; i++) {
					if (newPoly[2*i+1] > maxv) {
						imax = i;
						maxv = newPoly[2*i+1];
					}
				}
				xcenp = newPoly[2*imax];
				ycenp = newPoly[2*imax+1];
			} else {
				xcenp = newPoly[0];
				ycenp = newPoly[1]+newPoly[2];				
			}
			float[] newArr = {5.0f, 0.5f, xcenp, ycenp};
			allPolygonPaths.add(newArr.clone());
		} else if (mode==9) {
			// Trigger platform
			int imax = 0;
			float maxv = -10000.0f;
			if (ptype==6) {
				for (int i=0; i<newPoly.length/2; i++) {
					if (newPoly[2*i+1] > maxv) {
						imax = i;
						maxv = newPoly[2*i+1]; 
					}
				}
				xcenp = newPoly[2*imax];
				ycenp = newPoly[2*imax+1];
			} else {
				xcenp = newPoly[0];
				ycenp = newPoly[1];				
			}
			// (x, y of point on platform), (x, y of middle of trigger), (length, angle) of trigger
			float[] newArr = {xcenp, ycenp, xcenp-100.0f, ycenp-1000.0f, 500.0f, 0.0f};
			allPolygonPaths.add(newArr.clone());
		} else allPolygonPaths.add(null);
		SaveLevel(true);
	}

    public void AddVertex(int idx, int verti, int vertj, float startX, float startY) {
    	changesMade = true;
    	if (mode==6) {
	    	updatePoly = new float[allDecors.get(idx).length + 2];
	    	int cntr = 0;
	    	if (vertj != verti+1) {
	    		// Add the new vertex on the end of the array
	        	for (int i = 0; i<allDecors.get(idx).length/2; i++){
	        		updatePoly[2*i] = allDecors.get(idx)[2*i];
	        		updatePoly[2*i+1] = allDecors.get(idx)[2*i+1];
	        		}
	        	updatePoly[allDecors.get(idx).length] = startX;
	        	updatePoly[allDecors.get(idx).length+1] = startY;
	    	} else {
	        	for (int i = 0; i<allDecors.get(idx).length/2; i++){
	        		if (i == vertj) {
	        			updatePoly[2*cntr] = startX;
	        			updatePoly[2*cntr+1] = startY;
	        			cntr += 1;
	        		}
	    			updatePoly[2*cntr] = allDecors.get(idx)[2*i];
	    			updatePoly[2*cntr+1] = allDecors.get(idx)[2*i+1];
	    			cntr += 1;
	        	}
	    	}
    	} else {
	    	updatePoly = new float[allPolygons.get(idx).length + 2];
	    	int cntr = 0;
	    	if (vertj != verti+1) {
	    		// Add the new vertex on the end of the array
	        	for (int i = 0; i<allPolygons.get(idx).length/2; i++){
	        		updatePoly[2*i] = allPolygons.get(idx)[2*i];
	        		updatePoly[2*i+1] = allPolygons.get(idx)[2*i+1];
	        		}
	        	updatePoly[allPolygons.get(idx).length] = startX;
	        	updatePoly[allPolygons.get(idx).length+1] = startY;
	    	} else {
	        	for (int i = 0; i<allPolygons.get(idx).length/2; i++){
	        		if (i == vertj) {
	        			updatePoly[2*cntr] = startX;
	        			updatePoly[2*cntr+1] = startY;
	        			cntr += 1;
	        		}
	    			updatePoly[2*cntr] = allPolygons.get(idx)[2*i];
	    			updatePoly[2*cntr+1] = allPolygons.get(idx)[2*i+1];
	    			cntr += 1;
	        	}
	    	}
    	}
    	SaveLevel(true);
    }

    public void MoveSegment(int idx, int verti, int vertj, float startX, float startY) {
    	// Currently, this is only designed to move the segment in X or Y.
    	changesMade = true;
    	if (mode==5) {
	    	updatePoly = allObjects.get(idx).clone();
    		// Move the segment
	    	if (Math.abs(allObjects.get(idx)[2*verti]-allObjects.get(idx)[2*vertj]) < (Math.abs(allObjects.get(idx)[2*verti+1]-allObjects.get(idx)[2*vertj+1]))) {
	    		// More similar x values, so move in the x direction
	    		updatePoly[2*verti] = startX;
	    		updatePoly[2*vertj] = startX;
	    	} else {
	    		// More similar y values, so move in the y direction
	    		updatePoly[2*verti+1] = startY;
	    		updatePoly[2*vertj+1] = startY;
	    	}
    	} else if (mode == 6) {
    		if ((allDecorTypes.get(idx)==DecorVars.Waterfall) | (allDecorTypes.get(idx)==DecorVars.Rain)) {
				updatePoly = Arrays.copyOfRange(allDecors.get(idx).clone(), 0, 8);
			} else {
				updatePoly = allDecors.get(idx).clone();
			}
    		// Move the segment
	    	if (Math.abs(allDecors.get(idx)[2*verti]-allDecors.get(idx)[2*vertj]) < (Math.abs(allDecors.get(idx)[2*verti+1]-allDecors.get(idx)[2*vertj+1]))) {
	    		// More similar x values, so move in the x direction
	    		updatePoly[2*verti] = startX;
	    		updatePoly[2*vertj] = startX;
	    	} else {
	    		// More similar y values, so move in the y direction
	    		updatePoly[2*verti+1] = startY;
	    		updatePoly[2*vertj+1] = startY;
	    	}    		
    	}
    }

    public void AddVertexPath(int idx, int verti, int vertj, float startX, float startY) {
    	changesMade = true;
    	updatePath = new float[allPolygonPaths.get(idx).length + 2];
    	int cntr = 0;
    	for (int i = 0; i<6; i++) updatePath[i] = allPolygonPaths.get(idx)[i];
       	for (int i = 0; i<(allPolygonPaths.get(idx).length-6)/2; i++) {
       		if (i == vertj) {
       			updatePath[6+2*cntr] = startX;
       			updatePath[6+2*cntr+1] = startY;
       			cntr += 1;
       		}
       		updatePath[6+2*cntr] = allPolygonPaths.get(idx)[6+2*i];
       		updatePath[6+2*cntr+1] = allPolygonPaths.get(idx)[6+2*i+1];
       		cntr += 1;
    	}
    }

    public void CopyPolygon(float[] newPoly, int idx) {
    	changesMade = true;
		allPolygons.add(newPoly);
		ArrayList<float[]> polys;
		ArrayList<float[]> polyPath;
		ArrayList<Integer> polyTypes;
		if (mode == 12) {
			allPolygonTypes.add(allPolygonTypes_Alt.get(idx));
			allPolygonTextures.add(allPolygonTextures_Alt.get(idx));
			if (allPolygonTypes_Alt.get(idx)%2==0) allPolygonSprites.add(allPolygonSprites_Alt.get(idx));
			else allPolygonSprites.add(null);
			polys = (ArrayList<float[]>) allPolygons_Alt.clone();
			polyPath = (ArrayList<float[]>) allPolygonPaths_Alt.clone();
			polyTypes = (ArrayList<Integer>) allPolygonTypes_Alt.clone();
		} else {
			allPolygonTypes.add(allPolygonTypes.get(idx));
			allPolygonTextures.add(allPolygonTextures.get(idx));
			if (allPolygonTypes.get(idx)%2==0) allPolygonSprites.add(allPolygonSprites.get(idx));
			else allPolygonSprites.add(null);
			polys = (ArrayList<float[]>) allPolygons.clone();
			polyPath = (ArrayList<float[]>) allPolygonPaths.clone();
			polyTypes = (ArrayList<Integer>) allPolygonTypes.clone();
		}
		if (polyPath.get(idx)==null) {
			allPolygonPaths.add(null);
		} else {
			if ((polyTypes.get(idx)==4) | (polyTypes.get(idx)==5)) {
				float[] newArr = polyPath.get(idx).clone();
				newArr[2] += (newPoly[0]-polys.get(idx)[0]);
				newArr[3] += (newPoly[1]-polys.get(idx)[1]);
				allPolygonPaths.add(newArr.clone());
			} else if ((polyTypes.get(idx)==6) | (polyTypes.get(idx)==7)) {
				float[] newArr = polyPath.get(idx).clone();
				newArr[0] += (newPoly[0]-polys.get(idx)[0]);
				newArr[1] += (newPoly[1]-polys.get(idx)[1]);
				newArr[2] += (newPoly[0]-polys.get(idx)[0]);
				newArr[3] += (newPoly[1]-polys.get(idx)[1]);
				allPolygonPaths.add(newArr.clone());
			} else {
				float xcenp = 0.0f, ycenp = 0.0f;
				if (newPoly.length==3) {
					xcenp = newPoly[0];
					ycenp = newPoly[1];
				} else {
					for (int i=0; i<newPoly.length/2; i++) {
						xcenp += newPoly[2*i];
						ycenp += newPoly[2*i+1];
					}
					xcenp /= newPoly.length/2;
					ycenp /= newPoly.length/2;
				}
				float[] newArr = polyPath.get(idx).clone();
		       	for (int i = 0; i<(polyPath.get(idx).length-6)/2; i++) {
		       		newArr[6+2*i] += (xcenp-polyPath.get(idx)[4]);
		       		newArr[6+2*i+1] += (ycenp-polyPath.get(idx)[5]);
		    	}
				newArr[4] = xcenp;
				newArr[5] = ycenp;
				allPolygonPaths.add(newArr.clone());
			}
		}
		SaveLevel(true);
	}

    public void DeletePolygon(int idx, boolean autosave) {
    	changesMade = true;
		allPolygons.remove(idx);
		allPolygonTypes.remove(idx);
		allPolygonPaths.remove(idx);
		allPolygonTextures.remove(idx);
		allPolygonSprites.remove(idx);
		// Check if any grass decorations need to be deleted or shifted
		int cnt=0;
		int sz=allDecorTypes.size();
		for (int i=0; i<sz; i++) {
			if (allDecorTypes.get(cnt)==DecorVars.Grass) {
				if (allDecorPolys.get(cnt)==idx) {
					allDecors.remove(cnt);
					allDecorTypes.remove(cnt);
					allDecorPolys.remove(cnt);
				} else if (allDecorPolys.get(cnt)>idx) {
					allDecorPolys.set(cnt, allDecorPolys.get(cnt)-1);
					cnt += 1;
				} else cnt += 1;
			}
		}
		polySelect = -1;
		if (autosave) SaveLevel(true);
	}

    public void DeleteVertex(int idx, int vert) {
    	if (mode == 6) {
        	updatePoly = new float[allDecors.get(idx).length - 2];
        	int cntr = 0;
        	for (int i = 0; i<allDecors.get(idx).length/2; i++){
        		if (i != vert) {
        			updatePoly[2*cntr] = allDecors.get(idx)[2*i];
        			updatePoly[2*cntr+1] = allDecors.get(idx)[2*i+1];
        			cntr += 1;
        		}
        	}
    		UpdateDecor(idx, "move");
    	} else {
        	updatePoly = new float[allPolygons.get(idx).length - 2];
        	int cntr = 0;
        	for (int i = 0; i<allPolygons.get(idx).length/2; i++){
        		if (i != vert) {
        			updatePoly[2*cntr] = allPolygons.get(idx)[2*i];
        			updatePoly[2*cntr+1] = allPolygons.get(idx)[2*i+1];
        			cntr += 1;
        		}
        	}
    		UpdatePolygon(idx, true);
    	}
	}
   
    public void DeleteVertexPath(int idx, int vert) {
    	changesMade = true;
    	float[] newPath = new float[allPolygonPaths.get(idx).length - 2];
    	int cntr = 0;
    	for (int i = 0; i<6; i++) newPath[i] = allPolygonPaths.get(idx)[i];
    	for (int i = 0; i<(allPolygonPaths.get(idx).length-6)/2; i++){
    		if (i != vert) {
    			newPath[6+2*cntr] = allPolygonPaths.get(idx)[6+2*i];
    			newPath[6+2*cntr+1] = allPolygonPaths.get(idx)[6+2*i+1];
    			cntr += 1;
    		}
    	}
    	allPolygonPaths.set(idx, newPath.clone());
    	SaveLevel(true);
	}
   
	public void DrawPolygon(int typeAdd) {
		// Make sure the new point is valid (does not intersect other lines and is not out of bounds)
		tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		// If the point is acceptable, add it to the polygon or close the polygon
		if (polyDraw.size() >= 3) {
			if (Math.sqrt((tempx-polyDraw.get(0)[0])*(tempx-polyDraw.get(0)[0]) + (tempy-polyDraw.get(0)[1])*(tempy-polyDraw.get(0)[1])) < cam.zoom*SCRWIDTH*polyEndThreshold) {
				MakePolygon();
				if (mode == 3) AddPolygon(newPoly, 0, polyDraw.size());
				else if (mode == 4) AddPolygon(newPoly, 2, polyDraw.size());
				else if (mode == 6) AddPolygon(newPoly, typeAdd, polyDraw.size());
				else if (mode == 7) AddPolygon(newPoly, 4, polyDraw.size());
				else if (mode == 9) AddPolygon(newPoly, 6, polyDraw.size());
		    	drawingPoly = false;
			} else {
				int idx = polyDraw.size()-1;
				float len2 = (tempx-polyDraw.get(idx)[0])*(tempx-polyDraw.get(idx)[0]) + (tempy-polyDraw.get(idx)[1])*(tempy-polyDraw.get(idx)[1]);
				if (B2DVars.EPPM*B2DVars.EPPM*len2 > 0.0025f) {
					newCoord[0] = tempx;
					newCoord[1] = tempy;
					polyDraw.add(newCoord.clone()); // Add a new coordinate value
				}
			}
		} else {
			// Check the coordinate is far enough away
			if (polyDraw.size() != 0) {
				int idx = polyDraw.size()-1;
				float len2 = (tempx-polyDraw.get(idx)[0])*(tempx-polyDraw.get(idx)[0]) + (tempy-polyDraw.get(idx)[1])*(tempy-polyDraw.get(idx)[1]);
				if (B2DVars.EPPM*B2DVars.EPPM*len2 > 0.0025f) {
					newCoord[0] = tempx;
					newCoord[1] = tempy;
					polyDraw.add(newCoord.clone()); // Add a new coordinate value
				}
			} else {
				newCoord[0] = tempx;
				newCoord[1] = tempy;
				polyDraw.add(newCoord.clone()); // Add a new coordinate value
			}
		}
	}

	public void CheckFlipRotate(boolean reset) {
    	if (flipX) {
    		if (polySelect != -1) FlipPolygon(0);
    		else if (decorSelect != -1) FlipDecor(0);
    		if (reset) flipX = false;
    	}
    	if (flipY) {
    		if (polySelect != -1) FlipPolygon(1);
    		else if (decorSelect != -1) FlipDecor(1);
    		if (reset) flipY = false;    		    		
    	}
    	if (rotPoly) {
    		// TODO : rotate polygon
    		if (reset) rotPoly = false;
    	}
	}
	
	public void FlipPolygon(int xy) {
    	float xcen = 0.0f, ycen=0.0f, cntr=0.0f;
    	float[] tempPoly;
        if (updateGroupPoly != null) {
        	// Find the centre of the polygons
        	for (int i = 0; i<updateGroupPoly.size(); i++){
        			if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 0) {
        				for (int j=0; j<updateGroupPoly.get(i).length/2; j++) {
        					xcen += updateGroupPoly.get(i)[2*j];
        					ycen += updateGroupPoly.get(i)[2*j+1];
        					cntr += 1.0f;
        				}
        			}
        			else if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 1) {
        				xcen += updateGroupPoly.get(i)[0];
        				ycen += updateGroupPoly.get(i)[1];
        				cntr += 1.0f;
        			}
        	}
        	xcen /= cntr;
        	ycen /= cntr;
        	// Now flip in the x or y direction
        	for (int i = 0; i<updateGroupPoly.size(); i++){
        		tempPoly = updateGroupPoly.get(i).clone();
    			if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 0) {
    				for (int j=0; j<tempPoly.length/2; j++) {
    					if (xy == 0) tempPoly[2*j] = 2*xcen - updateGroupPoly.get(i)[2*j];
    					else if (xy == 1) tempPoly[2*j+1] = 2*ycen - updateGroupPoly.get(i)[2*j+1];
    				}
    			}
    			else if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 1) {
					if (xy == 0) tempPoly[0] = 2*xcen - updateGroupPoly.get(i)[0];
					else if (xy == 1) tempPoly[1] = 2*ycen - updateGroupPoly.get(i)[1];
    			}
				updateGroupPoly.set(i, tempPoly.clone());
        	}
        } else if (updatePoly != null) {
        	// Find the centre of the polygons
			if (allPolygonTypes.get(polySelect)%2 == 0) {
				for (int j=0; j<updatePoly.length/2; j++) {
					xcen += updatePoly[2*j];
					ycen += updatePoly[2*j+1];
					cntr += 1.0f;
				}
			} // Don't need to flip a circle
        	xcen /= cntr;
        	ycen /= cntr;
        	// Now flip in the x or y direction
			if (allPolygonTypes.get(polySelect)%2 == 0) {
				tempPoly = updatePoly.clone();
				for (int j=0; j<tempPoly.length/2; j++) {
					if (xy == 0) tempPoly[2*j] = 2*xcen - updatePoly[2*j];
					else if (xy == 1) tempPoly[2*j+1] = 2*ycen - updatePoly[2*j+1];
				}
				updatePoly = tempPoly.clone();
			} // Don't need to flip a circle
        }
	}

	public void FindNearestSegment(boolean hover) {
		//FindNearestVertex(hover);
		int idxa, idxb, idxmin, polymin, flag;
		float grad, intc, gradb, intcb, xint, yint, xa, xb, ya, yb;
		float dist, tdist, mindist;
		mindist = 0.0f;
		idxmin = 0;
		polymin = 0;
		flag = 0;
		float[] arraySegm;
		for (int j=0; j<allPolygons.size(); j++) {
			if ((!hover) & (polySelect != -1)) {
				if (j!=polySelect) continue;
			}
			if (mode==3) {
				if ((allPolygonTypes.get(j) != 0) & (allPolygonTypes.get(j) != 1) &
						(allPolygonTypes.get(j) != 8) & (allPolygonTypes.get(j) != 9) &
						(allPolygonTypes.get(j) != 10) & (allPolygonTypes.get(j) != 11)) continue;
			} else if (mode==4) {
				if ((allPolygonTypes.get(j) != 2) & (allPolygonTypes.get(j) != 3)) continue;
			} else if (mode==7) {
				if ((allPolygonTypes.get(j) != 4) & (allPolygonTypes.get(j) != 5)) continue;
			} else if (mode==9) {
				if ((allPolygonTypes.get(j) != 6) & (allPolygonTypes.get(j) != 7)) continue;
			}
			if (allPolygonTypes.get(j)%2 == 0) {
				arraySegm = allPolygons.get(j).clone();
				for (int i=0; i<arraySegm.length/2; i++) {
					idxa = i;
					if (i == arraySegm.length/2 - 1) idxb = 0;
					else idxb = i+1;
					// Calculate the gradient
					xa = arraySegm[2*idxa];
					ya = arraySegm[2*idxa+1];
					xb = arraySegm[2*idxb];
					yb = arraySegm[2*idxb+1];
					if (xa==xb) {
						if (ya>yb) {
							if (tempy>ya) yint = tempy-ya;
							else if (tempy<yb) yint = yb-tempy;
							else yint = 0.0f;
						} else {
							if (tempy>yb) yint = tempy-yb;
							else if (tempy<ya) yint = ya-tempy;
							else yint = 0.0f;
						}
						dist = (float) Math.sqrt((tempx-xa)*(tempx-xa) + yint*yint);
					} else if (ya==yb) {
						if (xa>xb) {
							if (tempx>xa) yint = tempx-xa;
							else if (tempx<xb) yint = xb-tempx;
							else yint = 0.0f;
						} else {
							if (tempx>xb) yint = tempx-xb;
							else if (tempx<xa) yint = xa-tempx;
							else yint = 0.0f;
						}
						dist = (float) Math.sqrt((tempy-ya)*(tempy-ya) + yint*yint);
					} else {
						grad = (yb-ya)/(xb-xa);
						intc = ya - grad*xa;
						gradb = -(xb-xa)/(yb-ya);
						intcb = tempy - gradb*tempx;
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
						dist = (float) Math.sqrt((tempx-xint)*(tempx-xint) + (tempy-yint)*(tempy-yint));
					}
					if ((dist < mindist) | (flag==0)) {
						mindist = dist;
						idxmin = i;
						polymin = j;
						flag=1;
					}
				}
			}
		}
		if (hover) {
			polyHover = polymin;
			vertHover = idxmin;
			segmHover = idxmin;
		} else {
			polySelect = polymin;
			vertSelect = idxmin;
			segmSelect = idxmin;
		}
	}

	public void FindNearestSegmentPath(boolean hover, int idx, boolean ghost) {
		int idxa, idxb, idxmin, polymin, flag;
		float grad, intc, gradb, intcb, xint, yint, xa, xb, ya, yb;
		float dist, tdist, mindist, xtst, ytst, xcls, ycls;
		mindist = 0.0f;
		idxmin = 0;
		polymin = 0;
		flag = 0;
		float[] arraySegm = allPolygonPaths.get(idx).clone();
		if (arraySegm.length==6) return;
		xtst = arraySegm[arraySegm.length-2];
		ytst = arraySegm[arraySegm.length-1];
		xcls = arraySegm[arraySegm.length-2];
		ycls = arraySegm[arraySegm.length-1];
		for (int i=0; i<(arraySegm.length-6)/2-1; i++) {
			idxa = i;
			idxb = i+1;
			// Calculate the gradient
			xa = arraySegm[6+2*idxa];
			ya = arraySegm[6+2*idxa+1];
			xb = arraySegm[6+2*idxb];
			yb = arraySegm[6+2*idxb+1];
			if (xa==xb) {
				xtst = xa;
				ytst = tempy;
				if (ya>yb) {
					if (tempy>ya) {
						yint = tempy-ya;
						ytst = ya;
					} else if (tempy<yb) {
						yint = yb-tempy;
						ytst = yb;
					} else yint = 0.0f;
				} else {
					if (tempy>yb) {
						yint = tempy-yb;
						ytst = yb;
					} else if (tempy<ya) {
						yint = ya-tempy;
						ytst = ya;
					} else yint = 0.0f;
				}
			} else if (ya==yb) {
				xtst = tempx;
				ytst = ya;
				if (xa>xb) {
					if (tempx>xa) {
						yint = tempx-xa;
						xtst = xa;
					} else if (tempx<xb) {
						yint = xb-tempx;
						xtst = xb;
					} else yint = 0.0f;
				} else {
					if (tempx>xb) {
						yint = tempx-xb;
						xtst = xb;
					} else if (tempx<xa) {
						yint = xa-tempx;
						xtst = xa;
					} else yint = 0.0f;
				}
			} else {
				grad = (yb-ya)/(xb-xa);
				intc = ya - grad*xa;
				gradb = -(xb-xa)/(yb-ya);
				intcb = tempy - gradb*tempx;
				// Calculate the intersection, and make sure the intersection is within bounds
				xint = (intcb-intc)/(grad-gradb);
				if (xa < xb) {
					if (xint<xa) xint = xa;
					else if (xint>xb) xint = xb;
				} else {
					if (xint<xb) xint = xb;
					else if (xint>xa) xint = xa;
				}
				xtst = xint;
				ytst = grad*xtst+intc;
			}
			// Calculate the distance between the intersection and the cursor
			dist = (float) Math.sqrt((tempx-xtst)*(tempx-xtst) + (tempy-ytst)*(tempy-ytst));
			if ((dist < mindist) | (flag==0)) {
				mindist = dist;
				idxmin = i;
				flag=1;
				xcls = xtst;
				ycls = ytst;
			}
		}
		if (ghost) {
			ghostPoly = new float[2];
			ghostPoly[0] = xcls;
			ghostPoly[1] = ycls;
			return;
		}
		if (hover) {
			vertHover = idxmin;
			segmHover = idxmin;
		} else {
			vertSelect = idxmin;
			segmSelect = idxmin;
		}
	}

	public void FindNearestVertex(boolean hover) {
		float tempval = 0.0f, bestval = -1.0f;
		if (mode == 6) {
			// Then decorating
			for (int i = 0; i < allDecors.size(); i++) {
				if ((allDecorTypes.get(i)==DecorVars.Grass) | (allDecorTypes.get(i)==DecorVars.Rain) | (allDecorTypes.get(i)==DecorVars.Waterfall)) {
					if ((modeParent.equals("Grass")) & (allDecorTypes.get(i)!=DecorVars.Grass)) continue;
					if ((modeParent.equals("Rain")) & (allDecorTypes.get(i)!=DecorVars.Rain)) continue;
					if ((modeParent.equals("Waterfall")) & (allDecorTypes.get(i)!=DecorVars.Waterfall)) continue;
					for (int j = 0; j < allDecors.get(i).length/2; j++) {
						if (bestval == -1.0f) {
							bestval = (float) Math.sqrt((tempx-allDecors.get(i)[2*j])*(tempx-allDecors.get(i)[2*j]) + (tempy-allDecors.get(i)[2*j+1])*(tempy-allDecors.get(i)[2*j+1]));
							if (hover) {
								decorHover = i;
								vertHover = j;
							} else {
								decorSelect = i;
								vertSelect = j;							
							}
						} else {
							tempval = (float) Math.sqrt((tempx-allDecors.get(i)[2*j])*(tempx-allDecors.get(i)[2*j]) + (tempy-allDecors.get(i)[2*j+1])*(tempy-allDecors.get(i)[2*j+1]));
							if (tempval < bestval) {
								bestval = (float) Math.sqrt((tempx-allDecors.get(i)[2*j])*(tempx-allDecors.get(i)[2*j]) + (tempy-allDecors.get(i)[2*j+1])*(tempy-allDecors.get(i)[2*j+1]));
								if (hover) {
									decorHover = i;
									vertHover = j;
								} else {
									decorSelect = i;
									vertSelect = j;							
								}
							}
						}
					}
				} 
			}			
		} else {
			// Polygons
			for (int i = 0; i < allPolygons.size(); i++){
				if ((mode == 3) | (mode == 4) | (mode == 7) | (mode == 9)) {
					if (((allPolygonTypes.get(i) == 0)&(mode==3)) |
							((allPolygonTypes.get(i) == 8)&(mode==3)) |
							((allPolygonTypes.get(i) == 10)&(mode==3)) |
							((allPolygonTypes.get(i) == 2)&(mode==4)) |
							((allPolygonTypes.get(i) == 4)&(mode==7)) |
							((allPolygonTypes.get(i) == 6)&(mode==9))) {
						for (int j = 0; j < allPolygons.get(i).length/2; j++){
							if (bestval == -1.0f) {
								bestval = (float) Math.sqrt((tempx-allPolygons.get(i)[2*j])*(tempx-allPolygons.get(i)[2*j]) + (tempy-allPolygons.get(i)[2*j+1])*(tempy-allPolygons.get(i)[2*j+1]));
								if (hover) {
									polyHover = i;
									vertHover = j;
								} else {
									polySelect = i;
									vertSelect = j;							
								}
							} else
								tempval = (float) Math.sqrt((tempx-allPolygons.get(i)[2*j])*(tempx-allPolygons.get(i)[2*j]) + (tempy-allPolygons.get(i)[2*j+1])*(tempy-allPolygons.get(i)[2*j+1]));
								if (tempval < bestval) {
									bestval = (float) Math.sqrt((tempx-allPolygons.get(i)[2*j])*(tempx-allPolygons.get(i)[2*j]) + (tempy-allPolygons.get(i)[2*j+1])*(tempy-allPolygons.get(i)[2*j+1]));
									if (hover) {
										polyHover = i;
										vertHover = j;
									} else {
										polySelect = i;
										vertSelect = j;							
									}
								}
						}
					} else if (((allPolygonTypes.get(i) == 1)&(mode==3)) |
							((allPolygonTypes.get(i) == 9)&(mode==3)) |
							((allPolygonTypes.get(i) == 11)&(mode==3)) |
							((allPolygonTypes.get(i) == 3)&(mode==4)) |
							((allPolygonTypes.get(i) == 5)&(mode==7)) |
							((allPolygonTypes.get(i) == 7)&(mode==9))) {
						// Don't do anything
					}
				}
			}
		}
	}

	public void FindNearestVertexPath(boolean hover, int idx) {
		float tempval = 0.0f, bestval = -1.0f;
		int spine = 0;
		// Identify the spine vertex (which cannot be deleted or moved individually)
		for (int i = 0; i < (allPolygonPaths.get(idx).length-6)/2; i++){
			if (bestval == -1.0f) {
				bestval = (float) Math.sqrt((allPolygonPaths.get(idx)[4]-allPolygonPaths.get(idx)[6+2*i])*(allPolygonPaths.get(idx)[4]-allPolygonPaths.get(idx)[6+2*i]) + (allPolygonPaths.get(idx)[5]-allPolygonPaths.get(idx)[6+2*i+1])*(allPolygonPaths.get(idx)[5]-allPolygonPaths.get(idx)[6+2*i+1]));
				spine = i;
			} else {
				tempval = (float) Math.sqrt((allPolygonPaths.get(idx)[4]-allPolygonPaths.get(idx)[6+2*i])*(allPolygonPaths.get(idx)[4]-allPolygonPaths.get(idx)[6+2*i]) + (allPolygonPaths.get(idx)[5]-allPolygonPaths.get(idx)[6+2*i+1])*(allPolygonPaths.get(idx)[5]-allPolygonPaths.get(idx)[6+2*i+1]));
				if (tempval < bestval) {
					bestval = (float) Math.sqrt((allPolygonPaths.get(idx)[4]-allPolygonPaths.get(idx)[6+2*i])*(allPolygonPaths.get(idx)[4]-allPolygonPaths.get(idx)[6+2*i]) + (allPolygonPaths.get(idx)[5]-allPolygonPaths.get(idx)[6+2*i+1])*(allPolygonPaths.get(idx)[5]-allPolygonPaths.get(idx)[6+2*i+1]));
					spine = i;
				}
			}
		}
		// Now find the closest vertex (excluding the spine vertex)
		bestval = -1.0f;
		for (int i = 0; i < (allPolygonPaths.get(idx).length-6)/2; i++){
			if (i != spine) {
				if (bestval == -1.0f) {
					bestval = (float) Math.sqrt((tempx-allPolygonPaths.get(idx)[6+2*i])*(tempx-allPolygonPaths.get(idx)[6+2*i]) + (tempy-allPolygonPaths.get(idx)[6+2*i+1])*(tempy-allPolygonPaths.get(idx)[6+2*i+1]));
					if (hover) vertHover = i;
					else vertSelect = i;
				} else {
					tempval = (float) Math.sqrt((tempx-allPolygonPaths.get(idx)[6+2*i])*(tempx-allPolygonPaths.get(idx)[6+2*i]) + (tempy-allPolygonPaths.get(idx)[6+2*i+1])*(tempy-allPolygonPaths.get(idx)[6+2*i+1]));
					if (tempval < bestval) {
						bestval = (float) Math.sqrt((tempx-allPolygonPaths.get(idx)[6+2*i])*(tempx-allPolygonPaths.get(idx)[6+2*i]) + (tempy-allPolygonPaths.get(idx)[6+2*i+1])*(tempy-allPolygonPaths.get(idx)[6+2*i+1]));
						if (hover) vertHover = i;
						else vertSelect = i;
					}
				}
			}
		} 
	}

    public void FlipPath(int idx, String flip) {
    	updatePath = allPolygonPaths.get(idx).clone();
		if (flip.equals("x")) {
			for (int i = 0; i < (allPolygonPaths.get(idx).length-6)/2; i++) {
    			updatePath[6+2*i] = 2*updatePath[4] - updatePath[6+2*i];
    		}
		} else if (flip.equals("y")) {
    		for (int i = 0; i < (allPolygonPaths.get(idx).length-6)/2; i++){
    			updatePath[6+2*i+1] = 2*updatePath[5] - updatePath[6+2*i+1];
    		}
		}
	}

    public void FlipPolygon(int idx, String flip) {
    	if (mode == 6) {
	    	updatePoly = allDecors.get(idx).clone();
    		float avx = 0.0f;
    		float avy = 0.0f;
    		for (int i = 0; i<allDecors.get(idx).length/2; i++){
    			avx += updatePoly[2*i];
    			avy += updatePoly[2*i+1];
    		}
    		avx /= (float) (allDecors.get(idx).length/2);
    		avy /= (float) (allDecors.get(idx).length/2);
    		if (flip.equals("x")) {
	    		for (int i = 0; i<allDecors.get(idx).length/2; i++){
	    			updatePoly[2*i] = 2*avx - updatePoly[2*i];
	    		}
    		} else if (flip.equals("y")) {
	    		for (int i = 0; i<allDecors.get(idx).length/2; i++){
	    			updatePoly[2*i+1] = 2*avy - updatePoly[2*i+1];
	    		}
    		}    		
    	} else {
	    	updatePoly = allPolygons.get(idx).clone();
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) |
					((allPolygonTypes.get(idx) == 8)&(mode==3)) |
					((allPolygonTypes.get(idx) == 10)&(mode==3)) |
					((allPolygonTypes.get(idx) == 2)&(mode==4)) |
					((allPolygonTypes.get(idx) == 4)&(mode==7)) |
					((allPolygonTypes.get(idx) == 6)&(mode==9))) {
	    		float avx = 0.0f;
	    		float avy = 0.0f;
	    		for (int i = 0; i<allPolygons.get(idx).length/2; i++){
	    			avx += updatePoly[2*i];
	    			avy += updatePoly[2*i+1];
	    		}
	    		avx /= (float) (allPolygons.get(idx).length/2);
	    		avy /= (float) (allPolygons.get(idx).length/2);
	    		if (flip.equals("x")) {
		    		for (int i = 0; i<allPolygons.get(idx).length/2; i++){
		    			updatePoly[2*i] = 2*avx - updatePoly[2*i];
		    		}
	    		} else if (flip.equals("y")) {
		    		for (int i = 0; i<allPolygons.get(idx).length/2; i++){
		    			updatePoly[2*i+1] = 2*avy - updatePoly[2*i+1];
		    		}
	    		}
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) |
					((allPolygonTypes.get(idx) == 9)&(mode==3)) |
					((allPolygonTypes.get(idx) == 11)&(mode==3)) |
					((allPolygonTypes.get(idx) == 3)&(mode==4)) |
					((allPolygonTypes.get(idx) == 5)&(mode==7)) |
					((allPolygonTypes.get(idx) == 7)&(mode==9))) {
	    		// A circle shape --> Do nothing
	    	}
    	}
	}

    public void MakePolygon() {
    	newPoly = new float[2*polyDraw.size()];
    	for (int i = 0; i<polyDraw.size(); i++){
    		newPoly[2*i] = polyDraw.get(i)[0];
    		newPoly[2*i+1] = polyDraw.get(i)[1];
    	}
	}

	public void MoveFallingSign(int idx) {
		// Only used for falling platforms that aren't circles
		int idxa, idxb, idxmin, polymin, flag;
		float grad, intc, gradb, intcb, xint, yint, xa, xb, ya, yb;
		float dist, tdist, mindist, xtst, ytst, xcls, ycls;
		mindist = 0.0f;
		idxmin = 0;
		polymin = 0;
		flag = 0;
		updatePath = allPolygonPaths.get(idx).clone();
		float[] arraySegm = allPolygons.get(idx).clone();
		if (allPolygonTypes.get(idx) != 4) return;
		xtst = arraySegm[arraySegm.length-2];
		ytst = arraySegm[arraySegm.length-1];
		xcls = arraySegm[arraySegm.length-2];
		ycls = arraySegm[arraySegm.length-1];
		for (int i=0; i<arraySegm.length/2; i++) {
			idxa = i;
			idxb = i+1;
			if (i==arraySegm.length/2-1) idxb=0; // Wrap the polygon
			// Calculate the gradient
			xa = arraySegm[2*idxa];
			ya = arraySegm[2*idxa+1];
			xb = arraySegm[2*idxb];
			yb = arraySegm[2*idxb+1];
			if (xa==xb) {
				xtst = xa;
				ytst = tempy;
				if (ya>yb) {
					if (tempy>ya) {
						yint = tempy-ya;
						ytst = ya;
					} else if (tempy<yb) {
						yint = yb-tempy;
						ytst = yb;
					} else yint = 0.0f;
				} else {
					if (tempy>yb) {
						yint = tempy-yb;
						ytst = yb;
					} else if (tempy<ya) {
						yint = ya-tempy;
						ytst = ya;
					} else yint = 0.0f;
				}
			} else if (ya==yb) {
				xtst = tempx;
				ytst = ya;
				if (xa>xb) {
					if (tempx>xa) {
						yint = tempx-xa;
						xtst = xa;
					} else if (tempx<xb) {
						yint = xb-tempx;
						xtst = xb;
					} else yint = 0.0f;
				} else {
					if (tempx>xb) {
						yint = tempx-xb;
						xtst = xb;
					} else if (tempx<xa) {
						yint = xa-tempx;
						xtst = xa;
					} else yint = 0.0f;
				}
			} else {
				grad = (yb-ya)/(xb-xa);
				intc = ya - grad*xa;
				gradb = -(xb-xa)/(yb-ya);
				intcb = tempy - gradb*tempx;
				// Calculate the intersection, and make sure the intersection is within bounds
				xint = (intcb-intc)/(grad-gradb);
				if (xa < xb) {
					if (xint<xa) xint = xa;
					else if (xint>xb) xint = xb;
				} else {
					if (xint<xb) xint = xb;
					else if (xint>xa) xint = xa;
				}
				xtst = xint;
				ytst = grad*xtst+intc;
			}
			// Calculate the distance between the intersection and the cursor
			dist = (float) Math.sqrt((tempx-xtst)*(tempx-xtst) + (tempy-ytst)*(tempy-ytst));
			if ((dist < mindist) | (flag==0)) {
				mindist = dist;
				idxmin = i;
				flag=1;
				xcls = xtst;
				ycls = ytst;
			}
		}
		updatePath[2] = xcls;
		updatePath[3] = ycls;
	}

    public void MovePath(int idx, float shiftX, float shiftY) {
		updatePath = null;
		if (allPolygonPaths.get(idx) == null) return;
    	updatePath = allPolygonPaths.get(idx).clone();
    	if (mode == 9) {
    		// Trigger platforms
    		updatePath[2] += shiftX;
    		updatePath[3] += shiftY;
    	} else if (mode == 11) {
			if ((allPolygonTypes.get(idx)==6) || (allPolygonTypes.get(idx)==7)) {
				// Trigger platforms
				updatePath[0] += shiftX;
				updatePath[1] += shiftY;
				updatePath[2] += shiftX;
				updatePath[3] += shiftY;
			} else if ((allPolygonTypes.get(idx)==4) || (allPolygonTypes.get(idx)==5)) {
				for (int i = 2; i<allPolygonPaths.get(idx).length; i++) {
					if (i%2==0) updatePath[i] += shiftX;
					else updatePath[i] += shiftY;
				}
			} else if ((allPolygonTypes.get(idx)==2) || (allPolygonTypes.get(idx)==3)) {
				for (int i = 4; i<allPolygonPaths.get(idx).length; i++) {
					if (i%2==0) updatePath[i] += shiftX;
					else updatePath[i] += shiftY;
				}
			}
		} else {
	    	for (int i = 4; i<allPolygonPaths.get(idx).length; i++) {
	    		if (i%2==0) updatePath[i] += shiftX;
	    		else updatePath[i] += shiftY;
	    	}
    	}
	}

    public void MovePolygon(int idx, float shiftX, float shiftY) {
    	updatePoly = allPolygons.get(idx).clone();
    	if ((mode == 3) | (mode == 4) | (mode == 7) | (mode == 9)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) |
					((allPolygonTypes.get(idx) == 8)&(mode==3)) |
					((allPolygonTypes.get(idx) == 10)&(mode==3)) |
					((allPolygonTypes.get(idx) == 2)&(mode==4)) |
					((allPolygonTypes.get(idx) == 4)&(mode==7)) |
					((allPolygonTypes.get(idx) == 6)&(mode==9))) {
	    		for (int i = 0; i<allPolygons.get(idx).length/2; i++){
	    			updatePoly[2*i] += shiftX;
	    			updatePoly[2*i+1] += shiftY;
	    		}
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) |
					((allPolygonTypes.get(idx) == 9)&(mode==3)) |
					((allPolygonTypes.get(idx) == 11)&(mode==3)) |
					((allPolygonTypes.get(idx) == 3)&(mode==4)) |
					((allPolygonTypes.get(idx) == 5)&(mode==7)) |
					((allPolygonTypes.get(idx) == 7)&(mode==9))) {
	    		updatePoly[0] += shiftX;
	    		updatePoly[1] += shiftY;
	    	}
    	} else if (mode == 11) {
    		if (updatePoly.length==3) {
				updatePoly[0] += shiftX;
				updatePoly[1] += shiftY;
			} else {
				for (int i = 0; i<allPolygons.get(idx).length/2; i++){
					updatePoly[2*i] += shiftX;
					updatePoly[2*i+1] += shiftY;
				}
			}
		}
	}

    public void MoveVertex(int idx, int vert, float shiftX, float shiftY) {
    	if (mode == 6) updatePoly = allDecors.get(idx).clone();
    	else updatePoly = allPolygons.get(idx).clone();
    	if (mode == 6) {
    		updatePoly[2*vert] += shiftX;
    		updatePoly[2*vert+1] += shiftY;    		
    	} else if ((mode == 3) | (mode == 7) | (mode == 9)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) |
					((allPolygonTypes.get(idx) == 8)&(mode==3)) |
					((allPolygonTypes.get(idx) == 10)&(mode==3)) |
					((allPolygonTypes.get(idx) == 4)&(mode==7)) |
					((allPolygonTypes.get(idx) == 6)&(mode==9))) {
	    		updatePoly[2*vert] += shiftX;
	    		updatePoly[2*vert+1] += shiftY;
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) |
					((allPolygonTypes.get(idx) == 9)&(mode==3)) |
					((allPolygonTypes.get(idx) == 11)&(mode==3)) |
					((allPolygonTypes.get(idx) == 5)&(mode==7)) |
					((allPolygonTypes.get(idx) == 7)&(mode==9))) {
	    		// Do nothing
	    	}
    	} else if (mode == 4) {
	    	if (allPolygonTypes.get(idx) == 2) {
	    		updatePoly[2*vert] += shiftX;
	    		updatePoly[2*vert+1] += shiftY;
	    	} else if (allPolygonTypes.get(idx) == 3) {
	    		// Do nothing
	    	}    		
    	}
	}

    public void MoveVertexPath(int idx, int vert, float shiftX, float shiftY) {
    	updatePath = allPolygonPaths.get(idx).clone();
   		updatePath[6+2*vert] += shiftX;
   		updatePath[6+2*vert+1] += shiftY;
	}

    public void RotatePolygon(int idx, float angle) {
    	updatePoly = allPolygons.get(idx).clone();
    	if ((mode == 3) | (mode == 7) | (mode == 9)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) |
					((allPolygonTypes.get(idx) == 8)&(mode==3)) |
					((allPolygonTypes.get(idx) == 10)&(mode==3)) |
					((allPolygonTypes.get(idx) == 4)&(mode==7)) |
					((allPolygonTypes.get(idx) == 6)&(mode==9))) {
	    		for (int i = 0; i<allPolygons.get(idx).length; i++){
	    			if (i%2==0) updatePoly[i] = cursposx + (allPolygons.get(idx)[i]-cursposx)*(float) Math.cos(angle) - (allPolygons.get(idx)[i+1]-cursposy)*(float) Math.sin(angle);
	    			else updatePoly[i] = cursposy + (allPolygons.get(idx)[i-1]-cursposx)*(float) Math.sin(angle) + (allPolygons.get(idx)[i]-cursposy)*(float) Math.cos(angle);
	    		}
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) |
					((allPolygonTypes.get(idx) == 9)&(mode==3)) |
					((allPolygonTypes.get(idx) == 11)&(mode==3)) |
					((allPolygonTypes.get(idx) == 5)&(mode==7)) |
					((allPolygonTypes.get(idx) == 7)&(mode==9))) {
	    		updatePoly[0] = cursposx + (allPolygons.get(idx)[0]-cursposx)*(float) Math.cos(angle) - (allPolygons.get(idx)[1]-cursposy)*(float) Math.sin(angle);
	    		updatePoly[1] = cursposy + (allPolygons.get(idx)[0]-cursposx)*(float) Math.sin(angle) + (allPolygons.get(idx)[1]-cursposy)*(float) Math.cos(angle);
	    	}
    	} else if (mode == 4) {
	    	if (allPolygonTypes.get(idx) == 2) {
	    		for (int i = 0; i<allPolygons.get(idx).length; i++){
	    			if (i%2==0) updatePoly[i] = cursposx + (allPolygons.get(idx)[i]-cursposx)*(float) Math.cos(angle) - (allPolygons.get(idx)[i+1]-cursposy)*(float) Math.sin(angle);
	    			else updatePoly[i] = cursposy + (allPolygons.get(idx)[i-1]-cursposx)*(float) Math.sin(angle) + (allPolygons.get(idx)[i]-cursposy)*(float) Math.cos(angle);
	    		}
	    	} else if (allPolygonTypes.get(idx) == 3) {
	    		updatePoly[0] = cursposx + (allPolygons.get(idx)[0]-cursposx)*(float) Math.cos(angle) - (allPolygons.get(idx)[1]-cursposy)*(float) Math.sin(angle);
	    		updatePoly[1] = cursposy + (allPolygons.get(idx)[0]-cursposx)*(float) Math.sin(angle) + (allPolygons.get(idx)[1]-cursposy)*(float) Math.cos(angle);
	    	}    		
    	}
	}

	public void RotatePath(int idx, float angle) {
    	updatePath = allPolygonPaths.get(idx).clone();
    	if (mode == 9) {
    		updatePath[5] = angle;
    	} else {
	    	for (int i = 6; i<allPolygonPaths.get(idx).length; i++){
	    		if (i%2==0) updatePath[i] = allPolygonPaths.get(idx)[4] + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[4])*(float) Math.cos(angle) - (allPolygonPaths.get(idx)[i+1]-allPolygonPaths.get(idx)[5])*(float) Math.sin(angle);
	    		else updatePath[i] = allPolygonPaths.get(idx)[5] + (allPolygonPaths.get(idx)[i-1]-allPolygonPaths.get(idx)[4])*(float) Math.sin(angle) + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[5])*(float) Math.cos(angle);
	    	}
    	}
	}

    public void ScalePolygon(int idx, float scale) {
    	if (scale < 0.0f) scale *= -1.0f;
    	if (mode == 6) updatePoly = allDecors.get(idx).clone();
    	else updatePoly = allPolygons.get(idx).clone();
    	if (mode == 6) {
    		for (int i = 0; i<allDecors.get(idx).length; i++){
    			if (i%2==0) {
    				updatePoly[i] = cursposx + (allDecors.get(idx)[i]-cursposx)*scale;
    			} else {
    				updatePoly[i] = cursposy + (allDecors.get(idx)[i]-cursposy)*scale;
    			}
    		}
    	} else if ((mode == 3) | (mode==7) | (mode==9)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) |
					((allPolygonTypes.get(idx) == 8)&(mode==3)) |
					((allPolygonTypes.get(idx) == 10)&(mode==3)) |
					((allPolygonTypes.get(idx) == 4)&(mode==7)) |
					((allPolygonTypes.get(idx) == 6)&(mode==9))) {
	    		for (int i = 0; i<allPolygons.get(idx).length; i++){
	    			if (i%2==0) {
	    				updatePoly[i] = cursposx + (allPolygons.get(idx)[i]-cursposx)*scale;
	    			} else {
	    				updatePoly[i] = cursposy + (allPolygons.get(idx)[i]-cursposy)*scale;
	    			}
	    		}
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) |
					((allPolygonTypes.get(idx) == 9)&(mode==3)) |
					((allPolygonTypes.get(idx) == 11)&(mode==3)) |
					((allPolygonTypes.get(idx) == 5)&(mode==7)) |
					((allPolygonTypes.get(idx) == 7)&(mode==9))) {
	    		updatePoly[0] = cursposx + (allPolygons.get(idx)[0]-cursposx)*scale;
	    		updatePoly[1] = cursposy + (allPolygons.get(idx)[1]-cursposy)*scale;
	    		updatePoly[2] *= scale;
	    	}
    	} else if (mode == 4) {
	    	if (allPolygonTypes.get(idx) == 2) {
	    		for (int i = 0; i<allPolygons.get(idx).length; i++){
	    			if (i%2==0) {
	    				updatePoly[i] = cursposx + (allPolygons.get(idx)[i]-cursposx)*scale;
	    			} else {
	    				updatePoly[i] = cursposy + (allPolygons.get(idx)[i]-cursposy)*scale;
	    			}
	    		}
	    	} else if (allPolygonTypes.get(idx) == 3) {
	    		updatePoly[0] = cursposx + (allPolygons.get(idx)[0]-cursposx)*scale;
	    		updatePoly[1] = cursposy + (allPolygons.get(idx)[1]-cursposy)*scale;
	    		updatePoly[2] *= scale;
	    	}
    	}
	}

    public void ScalePath(int idx, float scale) {
    	if (scale < 0.0f) scale *= -1.0f;
    	updatePath = allPolygonPaths.get(idx).clone();
    	if (mode == 9) {
    		updatePath[4] *= scale;
    	} else {
	    	for (int i = 6; i<allPolygonPaths.get(idx).length; i++) {
	    		if (i%2==0) updatePath[i] = allPolygonPaths.get(idx)[4] + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[4])*scale;
	    		else updatePath[i] = allPolygonPaths.get(idx)[5] + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[5])*scale;
	    	}
    	}
	}

	public void SelectGroup(int objmin) {
		// Reset groups
		updateGroup = null;
		groupArrays = new ArrayList<Integer>(); // index of allPolygons, allObjects, allDecors
		groupPOD = new ArrayList<Integer>(); // Polygon (0), Object (1), or Decor (2)
		groupTypes = new ArrayList<Integer>(); // allPolygonTypes, allObjectTypes, allDecorTypes
		groupPaths = new ArrayList<float[]>(); // allPolygonPaths, allObjectArrows
		groupTextures = new ArrayList<String>(); // allPolygonTextures
		groupCoords = new ArrayList<float[]>(); // allObjectCoords
		groupPolys = new ArrayList<Integer>(); // allDecorPolys
		// Select all types of items within a selection box
		SelectGroupPolygons(0);
		SelectGroupObjects(objmin);
		SelectGroupDecors();
		ResetSelect();
	}

    public void SelectGroupPolygons(int polyOnly) {
    	ResetSelect();
    	float x1, x2, y1, y2, tmp;
    	float[] meanxy = new float[2];
		x1 = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		y1 = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		x2 = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		y2 = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		// Check the min max values
    	if (x1 > x2) {
    		tmp = x2;
    		x2 = x1;
    		x1 = tmp;
    	}
    	if (y1 > y2) {
    		tmp = y2;
    		y2 = y1;
    		y1 = tmp;
    	}
    	// Loop through all polygons and find the polys that are inside the selection boundary
		if (mode==12) {
			// Using alternative polygons
			for (int i = 0; i < allPolygons_Alt.size(); i++) {
				if (allPolygonTypes_Alt.get(i) % 2 == 0) {
					meanxy = PolygonOperations.MeanXY(allPolygons_Alt.get(i).clone());
				} else if (allPolygonTypes_Alt.get(i) % 2 == 1) {
					meanxy[0] = allPolygons_Alt.get(i)[0];
					meanxy[1] = allPolygons_Alt.get(i)[1];
				}
				if ((x1 < meanxy[0]) & (meanxy[0] < x2) & (y1 < meanxy[1]) & (meanxy[1] < y2)) {
					// Poly is inside selection
					if (polyOnly == 1) groupPolySelect.add(i);
					else {
						groupArrays.add(i);
						groupPOD.add(0);
						groupTypes.add(allPolygonTypes_Alt.get(i));
						groupPaths.add(allPolygonPaths_Alt.get(i));
						groupTextures.add(allPolygonTextures_Alt.get(i));
						groupCoords.add(null);
						groupPolys.add(null);
					}
				}
			}
		} else {
			for (int i = 0; i < allPolygons.size(); i++) {
				if (allPolygonTypes.get(i) % 2 == 0) {
					meanxy = PolygonOperations.MeanXY(allPolygons.get(i).clone());
				} else if (allPolygonTypes.get(i) % 2 == 1) {
					meanxy[0] = allPolygons.get(i)[0];
					meanxy[1] = allPolygons.get(i)[1];
				}
				if ((x1 < meanxy[0]) & (meanxy[0] < x2) & (y1 < meanxy[1]) & (meanxy[1] < y2)) {
					// Poly is inside selection
					if (polyOnly == 1) groupPolySelect.add(i);
					else {
						groupArrays.add(i);
						groupPOD.add(0);
						groupTypes.add(allPolygonTypes.get(i));
						groupPaths.add(allPolygonPaths.get(i));
						groupTextures.add(allPolygonTextures.get(i));
						groupCoords.add(null);
						groupPolys.add(null);
					}
				}
			}
		}
    }

	public void SelectPolygon(String downup) {
		ResetSelect();
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		} else if (downup.equals("up")){
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);			
		}
		boolean inside = false;
		triggerSelect = false;
		float [] extraPoly;
		for (int i = 0; i<allPolygons.size(); i++){
			if ((mode == 3) | (mode == 4) | (mode == 7) | (mode == 9)) {
				// Check for triggers first
				if ((mode==9) & ((allPolygonTypes.get(i) == 6) | (allPolygonTypes.get(i) == 7)) & (allPolygonPaths.get(i) != null)) {
					// Check if the user has clicked inside the trigger of a trigger platform
					// Make the trigger box
		        	extraPoly = new float[] {allPolygonPaths.get(i)[2]-ObjectVars.objectTriggerWidth, allPolygonPaths.get(i)[3]-allPolygonPaths.get(i)[4]/2,
		        			allPolygonPaths.get(i)[2]+ObjectVars.objectTriggerWidth, allPolygonPaths.get(i)[3]-allPolygonPaths.get(i)[4]/2,
		        			allPolygonPaths.get(i)[2]+ObjectVars.objectTriggerWidth, allPolygonPaths.get(i)[3]+allPolygonPaths.get(i)[4]/2,
		        			allPolygonPaths.get(i)[2]-ObjectVars.objectTriggerWidth, allPolygonPaths.get(i)[3]+allPolygonPaths.get(i)[4]/2};
		        	PolygonOperations.RotateXYArray(extraPoly, allPolygonPaths.get(i)[5], allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3]);
					inside = PolygonOperations.PointInPolygon(extraPoly.clone(),tempx,tempy);
					if (inside == true) {
						triggerSelect = true;
						polySelect = i;
						return;
					}

				}
				if (((allPolygonTypes.get(i) == 0)&(mode==3)) |
						((allPolygonTypes.get(i) == 8)&(mode==3)) |
						((allPolygonTypes.get(i) == 10)&(mode==3)) |
						((allPolygonTypes.get(i) == 2)&(mode==4)) |
						((allPolygonTypes.get(i) == 4)&(mode==7)) |
						((allPolygonTypes.get(i) == 6)&(mode==9))) {
					inside = PolygonOperations.PointInPolygon(allPolygons.get(i).clone(),tempx,tempy);
				} else if (((allPolygonTypes.get(i) == 1)&(mode==3)) |
						((allPolygonTypes.get(i) == 9)&(mode==3)) |
						((allPolygonTypes.get(i) == 11)&(mode==3)) |
						((allPolygonTypes.get(i) == 3)&(mode==4)) |
						((allPolygonTypes.get(i) == 5)&(mode==7)) |
						((allPolygonTypes.get(i) == 7)&(mode==9))) {
					if (Math.sqrt((tempx-allPolygons.get(i)[0])*(tempx-allPolygons.get(i)[0]) + (tempy-allPolygons.get(i)[1])*(tempy-allPolygons.get(i)[1])) < allPolygons.get(i)[2]) {
						inside = true;
					}
				}
			} else if (mode == 8) {
				if (allPolygonTypes.get(i)%2 == 0) {
					inside = PolygonOperations.PointInPolygon(allPolygons.get(i).clone(),tempx,tempy);
				} else if (allPolygonTypes.get(i)%2 == 1) {
					if (Math.sqrt((tempx-allPolygons.get(i)[0])*(tempx-allPolygons.get(i)[0]) + (tempy-allPolygons.get(i)[1])*(tempy-allPolygons.get(i)[1])) < allPolygons.get(i)[2]) {
						inside = true;
					}
				}				
			}
			if (inside == true) {
				polySelect = i;
				return;
			}
		}
		polySelect = -1;
		return;
	}

	public void UpdatePath(int idx) {
		changesMade = true;
		newPoly = allPolygonPaths.set(idx, updatePath.clone());
		updatePath = null;
		SaveLevel(true);
	}

	public void UpdatePolygon(int idx, boolean autosave) {
		changesMade = true;
		newPoly = allPolygons.set(idx, updatePoly.clone());
		if (allPolygonTypes.get(idx)%2==0) MakePolygonSprite(idx);
		if ((allPolygonTypes.get(idx)==4) || (allPolygonTypes.get(idx)==5)) {
			// Update the location of the sign for a falling platform
			updatePath = allPolygonPaths.get(idx).clone();
			int imax = 0;
			float maxv = -10000.0f;
			for (int i=0; i<updatePoly.length/2; i++) {
				if (updatePoly[2*i+1] > maxv) {
					imax = i;
					maxv = updatePoly[2*i+1]; 
				}
			}
			updatePath[2] = updatePoly[2*imax];
			updatePath[3] = updatePoly[2*imax+1];
			allPolygonPaths.set(idx, updatePath.clone());
			updatePath=null;
		} else if ((allPolygonTypes.get(idx)==6) || (allPolygonTypes.get(idx)==7)) {
			// Update the path properties for a trigger platform
			updatePath = allPolygonPaths.get(idx).clone();
			int imax = 0;
			float maxv = -10000.0f;
			for (int i=0; i<updatePoly.length/2; i++) {
				if (updatePoly[2*i+1] > maxv) {
					imax = i;
					maxv = updatePoly[2*i+1]; 
				}
			}
			updatePath[0] = updatePoly[2*imax];
			updatePath[1] = updatePoly[2*imax+1];
			allPolygonPaths.set(idx, updatePath.clone());
			updatePath=null;
		}
		updatePoly = null;
		if (autosave) SaveLevel(true);
	}

    /////////////////////////////////
   ///                           ///
  ///   ALL OBJECT OPERATIONS   ///
 ///                           ///
/////////////////////////////////

	public void AddObject(int otype, float xcen, float ycen, float angle) {
		changesMade = true;
		MakeObject(otype, xcen, ycen, angle);
		newCoord[0] = xcen;
		newCoord[1] = ycen;
		allObjects.add(newPoly.clone());
		allObjectTypes.add(otype);
		allObjectCoords.add(newCoord.clone());
		if (angle == -999.9f) {
			allObjectArrows.add(null);
		} else {
			MakeObject(-1, xcen, ycen, angle);
			allObjectArrows.add(newPoly);
		}
		SaveLevel(true);
	}

	public void CopyObject(float[] newObject, int idx, float shiftX, float shiftY) {
		changesMade = true;
		allObjects.add(newObject);
		ArrayList<float[]> objArrows;
		if (mode==12) {
			newCoord = allObjectCoords_Alt.get(idx).clone();
			objArrows = (ArrayList<float[]>) allObjectArrows_Alt.clone();
			allObjectTypes.add(allObjectTypes_Alt.get(idx).intValue());
			if (allObjectTypes_Alt.get(idx).intValue() == ObjectVars.Jewel) numJewels += 1; // Check if it was an emerald that was copied
		} else {
			newCoord = allObjectCoords.get(idx).clone();
			objArrows = (ArrayList<float[]>) allObjectArrows.clone();
			allObjectTypes.add(allObjectTypes.get(idx).intValue());
			if (allObjectTypes.get(idx).intValue() == ObjectVars.Jewel) numJewels += 1; // Check if it was an emerald that was copied
		}
		newCoord[0] += shiftX;
		newCoord[1] += shiftY;
		allObjectCoords.add(newCoord.clone());
		if (objArrows.get(idx)!=null) {
			newPoly = objArrows.get(idx).clone();
			for (int i=0; i<newPoly.length/2; i++) {
				newPoly[2*i] += shiftX;
				newPoly[2*i+1] += shiftY;
			}
			allObjectArrows.add(newPoly.clone());
			newPoly=null;
		} else allObjectArrows.add(null);
		SaveLevel(true);
	}

	public void DeleteObject(int idx, boolean autosave) {
		changesMade = true;
		if (allObjectTypes.get(idx) == ObjectVars.Jewel) numJewels -= 1;
		allObjects.remove(idx);
		allObjectTypes.remove(idx);
		allObjectCoords.remove(idx);
		allObjectArrows.remove(idx);
		objectSelect = -1;
		if (autosave) SaveLevel(true);
	}
  	
	public void FindNearestSegmentObject(boolean hover) {
		int idxa, idxb, idxmin, polymin, flag;
		float grad, intc, gradb, intcb, xint, yint, xa, xb, ya, yb;
		float dist, tdist, mindist;
		mindist = 0.0f;
		idxmin = 0;
		polymin = 0;
		flag = 0;
		float[] arraySegm;
		for (int j=0; j<allObjects.size(); j++) {
			if ((!hover) & (objectSelect != -1)) {
				if (j!=objectSelect) continue;
			}
			if (allObjectTypes.get(j) == ObjectVars.SpikeZone) {
//				if ((modeParent.equals("Rain")) & (allDecorTypes.get(j)!=DecorVars.Rain)) continue;
//				if ((modeParent.equals("Waterfall")) & (allDecorTypes.get(j)!=DecorVars.Waterfall)) continue;
//				if ((modeParent.equals("Collisionless BG")) & (allDecorTypes.get(j)!=DecorVars.CollisionlessBG)) continue;
//				if ((modeParent.equals("Collisionless FG")) & (allDecorTypes.get(j)!=DecorVars.CollisionlessFG)) continue;
				arraySegm = allObjects.get(j).clone();
				for (int i=0; i<arraySegm.length/2; i++) {
					idxa = i;
					if (i == arraySegm.length/2 - 1) idxb = 0;
					else idxb = i+1;
					// Calculate the gradient
					xa = arraySegm[2*idxa];
					ya = arraySegm[2*idxa+1];
					xb = arraySegm[2*idxb];
					yb = arraySegm[2*idxb+1];
					if (xa==xb) {
						if (ya>yb) {
							if (tempy>ya) yint = tempy-ya;
							else if (tempy<yb) yint = yb-tempy;
							else yint = 0.0f;
						} else {
							if (tempy>yb) yint = tempy-yb;
							else if (tempy<ya) yint = ya-tempy;
							else yint = 0.0f;
						}
						dist = (float) Math.sqrt((tempx-xa)*(tempx-xa) + yint*yint);
					} else if (ya==yb) {
						if (xa>xb) {
							if (tempx>xa) yint = tempx-xa;
							else if (tempx<xb) yint = xb-tempx;
							else yint = 0.0f;
						} else {
							if (tempx>xb) yint = tempx-xb;
							else if (tempx<xa) yint = xa-tempx;
							else yint = 0.0f;
						}
						dist = (float) Math.sqrt((tempy-ya)*(tempy-ya) + yint*yint);
					} else {
						grad = (yb-ya)/(xb-xa);
						intc = ya - grad*xa;
						gradb = -(xb-xa)/(yb-ya);
						intcb = tempy - gradb*tempx;
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
						dist = (float) Math.sqrt((tempx-xint)*(tempx-xint) + (tempy-yint)*(tempy-yint));
					}
					if ((dist < mindist) | (flag==0)) {
						mindist = dist;
						idxmin = i;
						polymin = j;
						flag=1;
					}
				}
			}
		}
		if (hover) {
			objectHover = polymin;
			vertHover = idxmin;
			segmHover = idxmin;
		} else {
			objectSelect = polymin;
			vertSelect = idxmin;
			segmSelect = idxmin;
		}
	}

	public void MakeObject(int otype, float xcen, float ycen, float angle) {
		angle *= MathUtils.PI/180.0;
		if (otype == ObjectVars.Arrow) {
			newPoly = new float[ObjectVars.objectArrow.length];
			for (int i = 0; i<ObjectVars.objectArrow.length/2; i++){
				nullvarA = ObjectVars.objectArrow[2*i] + xcen;
				nullvarB = ObjectVars.objectArrow[2*i+1] + ycen;
				newPoly[2*i] = xcen + (nullvarA-xcen)*(float) Math.cos(angle) - (nullvarB-ycen)*(float) Math.sin(angle);
				newPoly[2*i+1] = ycen + (nullvarA-xcen)*(float) Math.sin(angle) + (nullvarB-ycen)*(float) Math.cos(angle);
			}
		} else if ((otype == ObjectVars.BallChain) | (otype == ObjectVars.Pendulum)) {
			newPoly = new float[ObjectVars.objectBallChain.length];
			newPoly[0] = ObjectVars.objectBallChain[0] + xcen;
			newPoly[1] = ObjectVars.objectBallChain[1] + ycen;
			newPoly[2] = ObjectVars.objectBallChain[2];
			newPoly[3] = ObjectVars.objectBallChain[3] + xcen;
			newPoly[4] = ObjectVars.objectBallChain[4] + ycen;
			newPoly[5] = ObjectVars.objectBallChain[5];
			newPoly[6] = ObjectVars.objectBallChain[6];
			newPoly[7] = ObjectVars.objectBallChain[7];
		} else if (otype == ObjectVars.Boulder) {
			newPoly = new float[ObjectVars.objectBoulder.length];
			newPoly[0] = ObjectVars.objectBoulder[0] + xcen;
			newPoly[1] = ObjectVars.objectBoulder[1] + ycen;
			newPoly[2] = ObjectVars.objectBoulder[2];
		} else if (otype == ObjectVars.Bridge) {
			newPoly = new float[ObjectVars.objectBridge.length];
			for (int i = 0; i<(ObjectVars.objectBridge.length-1)/2; i++){
				newPoly[2*i] = ObjectVars.objectBridge[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectBridge[2*i+1] + ycen;
			}
			newPoly[16] = ObjectVars.objectBridge[16];
		} else if (otype == ObjectVars.Crate) {
			newPoly = new float[ObjectVars.objectCrate.length];
			for (int i = 0; i<ObjectVars.objectCrate.length/2; i++){
				newPoly[2*i] = ObjectVars.objectCrate[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectCrate[2*i+1] + ycen;
			}
		} else if ((otype == ObjectVars.DoorBlue) | (otype == ObjectVars.DoorGreen) | (otype == ObjectVars.DoorRed)) {
			newPoly = new float[ObjectVars.objectDoor.length];
			for (int i = 0; i<ObjectVars.objectDoor.length/2; i++){
				newPoly[2*i] = ObjectVars.objectDoor[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectDoor[2*i+1] + ycen;
			}
		} else if ((otype == ObjectVars.KeyBlue) | (otype == ObjectVars.KeyGreen) | (otype == ObjectVars.KeyRed)) {
			newPoly = new float[ObjectVars.objectKey.length];
			for (int i = 0; i<ObjectVars.objectKey.length/2; i++){
				newPoly[2*i] = ObjectVars.objectKey[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectKey[2*i+1] + ycen;
			}
		} else if (otype == ObjectVars.GateSwitch) {
			newPoly = new float[ObjectVars.objectGateSwitch.length];
			for (int i = 0; i<ObjectVars.objectGateSwitch.length/2 - 1; i++){
				newPoly[2*i] = ObjectVars.objectGateSwitch[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectGateSwitch[2*i+1] + ycen;
			}
			newPoly[ObjectVars.objectGateSwitch.length-2] = ObjectVars.objectGateSwitch[ObjectVars.objectGateSwitch.length-2];
			newPoly[ObjectVars.objectGateSwitch.length-1] = ObjectVars.objectGateSwitch[ObjectVars.objectGateSwitch.length-1];
		} else if (otype == ObjectVars.Gravity) {
			newPoly = new float[ObjectVars.objectGravity.length];
			for (int i = 0; i<ObjectVars.objectGravity.length/2; i++){
				newPoly[2*i] = ObjectVars.objectGravity[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectGravity[2*i+1] + ycen;
			}
		} else if (otype == ObjectVars.Jewel) {
			newPoly = new float[ObjectVars.objectJewel.length];
			for (int i = 0; i<ObjectVars.objectJewel.length/2; i++){
				newPoly[2*i] = ObjectVars.objectJewel[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectJewel[2*i+1] + ycen;
			}
		} else if (otype == ObjectVars.Log) {
			newPoly = new float[ObjectVars.objectLog.length];
			newPoly[0] = ObjectVars.objectLog[0] + xcen;
			newPoly[1] = ObjectVars.objectLog[1] + ycen;
			newPoly[2] = ObjectVars.objectLog[2];
		} else if (otype == ObjectVars.Nitrous) {
			newPoly = new float[ObjectVars.objectNitrous.length];
			for (int i = 0; i<ObjectVars.objectNitrous.length/2; i++){
				newPoly[2*i] = ObjectVars.objectNitrous[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectNitrous[2*i+1] + ycen;
			}
		} else if (otype == ObjectVars.Spike) {
			newPoly = new float[ObjectVars.objectSpike.length];
			newPoly[0] = ObjectVars.objectSpike[0] + xcen;
			newPoly[1] = ObjectVars.objectSpike[1] + ycen;
			newPoly[2] = ObjectVars.objectSpike[2];
		} else if (otype == ObjectVars.SpikeZone) {
			newPoly = new float[ObjectVars.objectSpikeZone.length];
			for (int i = 0; i<ObjectVars.objectSpikeZone.length/2; i++){
				newPoly[2*i] = ObjectVars.objectSpikeZone[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectSpikeZone[2*i+1] + ycen;
			}
		} else if ((otype == ObjectVars.Transport) | (ObjectVars.IsTransportInvisible(otype))) {
			newPoly = new float[ObjectVars.objectTransport.length];
			for (int i = 0; i<ObjectVars.objectTransport.length/2; i++){
				newPoly[2*i] = ObjectVars.objectTransport[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectTransport[2*i+1] + ycen;
			}
		} else if (ObjectVars.IsPlanet(otype)) {
			newPoly = ObjectVars.MakePlanet(otype, xcen, ycen).clone();
		}
	}

	public void MoveObject(int idx, String mode, float shiftX, float shiftY) {
		// Now perform the operation
		if (mode.equals("polygon")) {
			updatePoly = allObjects.get(idx).clone();
			for (int i = 0; i<allObjects.get(idx).length/2; i++){
				updatePoly[2*i] += shiftX;
				updatePoly[2*i+1] += shiftY;
			}
		} else if (mode.equals("circle")) {
			updatePoly = allObjects.get(idx).clone();
			updatePoly[0] += shiftX;
			updatePoly[1] += shiftY;
		} else if (mode.equals("moveball")) {
			updatePoly = new float[3];
			updatePoly[0] = allObjects.get(idx)[0] + shiftX;
			updatePoly[1] = allObjects.get(idx)[1] + shiftY;
			updatePoly[2] = allObjects.get(idx)[2];
		} else if (mode.equals("moveanchor")) {
			updatePoly = new float[8];
			updatePoly[0] = allObjects.get(idx)[3] + shiftX;
			updatePoly[1] = allObjects.get(idx)[4] + shiftY;
			updatePoly[2] = allObjects.get(idx)[3] + allObjects.get(idx)[5] + shiftX;
			updatePoly[3] = allObjects.get(idx)[4] + shiftY;
			updatePoly[4] = allObjects.get(idx)[3] + allObjects.get(idx)[5] + shiftX;
			updatePoly[5] = allObjects.get(idx)[4] + allObjects.get(idx)[6] + shiftY;
			updatePoly[6] = allObjects.get(idx)[3] + shiftX;
			updatePoly[7] = allObjects.get(idx)[4] + allObjects.get(idx)[6] + shiftY;
		} else if (mode.equals("moveentry")) {
			updatePoly = new float[8];
			updatePoly[0] = allObjects.get(idx)[0+8*(tentry-1)] + shiftX;
			updatePoly[1] = allObjects.get(idx)[1+8*(tentry-1)] + shiftY;
			updatePoly[2] = allObjects.get(idx)[2+8*(tentry-1)] + shiftX;
			updatePoly[3] = allObjects.get(idx)[3+8*(tentry-1)] + shiftY;
			updatePoly[4] = allObjects.get(idx)[4+8*(tentry-1)] + shiftX;
			updatePoly[5] = allObjects.get(idx)[5+8*(tentry-1)] + shiftY;
			updatePoly[6] = allObjects.get(idx)[6+8*(tentry-1)] + shiftX;
			updatePoly[7] = allObjects.get(idx)[7+8*(tentry-1)] + shiftY;			
		} else if (mode.equals("moveplanet")) {
			if (allObjectTypes.get(idx)==ObjectVars.PlanetSaturn) {
				updatePoly = allObjects.get(idx).clone();
				for (int i = 0; i<allObjects.get(idx).length/2; i++){
					updatePoly[2*i] += shiftX;
					updatePoly[2*i+1] += shiftY;
				}
			} else {
				updatePoly = allObjects.get(idx).clone();
				updatePoly[0] += shiftX;
				updatePoly[1] += shiftY;
			}
		}
	}

	public void MoveObjectCopy(int idx, float shiftX, float shiftY) {
		int otype = allObjectTypes.get(idx);
		newPoly = allObjects.get(idx).clone();
		if ((otype==ObjectVars.BallChain) | (otype==ObjectVars.Pendulum)) {
			// Shift the newPoly
			newPoly[0] += shiftX;
			newPoly[1] += shiftY;
			newPoly[3] += shiftX;
			newPoly[4] += shiftY;
			// Set the updated polygon
			if (Math.sqrt((tempx-allObjects.get(idx)[0])*(tempx-allObjects.get(idx)[0]) + (tempy-allObjects.get(idx)[1])*(tempy-allObjects.get(idx)[1])) < allObjects.get(idx)[2]) {
				updatePoly = new float[3];
				updatePoly[0] = allObjects.get(idx)[0] + shiftX;
				updatePoly[1] = allObjects.get(idx)[1] + shiftY;
				updatePoly[2] = allObjects.get(idx)[2];
			} else {
				updatePoly = new float[8];
				updatePoly[0] = allObjects.get(idx)[3] + shiftX;
				updatePoly[1] = allObjects.get(idx)[4] + shiftY;
				updatePoly[2] = allObjects.get(idx)[3] + allObjects.get(idx)[5] + shiftX;
				updatePoly[3] = allObjects.get(idx)[4] + shiftY;
				updatePoly[4] = allObjects.get(idx)[3] + allObjects.get(idx)[5] + shiftX;
				updatePoly[5] = allObjects.get(idx)[4] + allObjects.get(idx)[6] + shiftY;
				updatePoly[6] = allObjects.get(idx)[3] + shiftX;
				updatePoly[7] = allObjects.get(idx)[4] + allObjects.get(idx)[6] + shiftY;
			}
		} else if ((otype==ObjectVars.Transport) | (ObjectVars.IsTransportInvisible(otype)) | (otype==ObjectVars.GateSwitch) | (otype==ObjectVars.Bridge)) {
			updatePoly = new float[8];
			int tmp = tentry;
			if ((tentry!=1) && (tentry!=2)) tentry = 1;  // This is a hack because mode=11 (group select) doesn't set tentry.
			updatePoly[0] = allObjects.get(idx)[0+8*(tentry-1)] + shiftX;
			updatePoly[1] = allObjects.get(idx)[1+8*(tentry-1)] + shiftY;
			updatePoly[2] = allObjects.get(idx)[2+8*(tentry-1)] + shiftX;
			updatePoly[3] = allObjects.get(idx)[3+8*(tentry-1)] + shiftY;
			updatePoly[4] = allObjects.get(idx)[4+8*(tentry-1)] + shiftX;
			updatePoly[5] = allObjects.get(idx)[5+8*(tentry-1)] + shiftY;
			updatePoly[6] = allObjects.get(idx)[6+8*(tentry-1)] + shiftX;
			updatePoly[7] = allObjects.get(idx)[7+8*(tentry-1)] + shiftY;
			tentry = tmp;
			for (int i=0; i<8; i++) {
				newPoly[2*i] += shiftX;
				newPoly[2*i+1] += shiftY;
			}
		} else if (ObjectVars.IsPlanet(otype)) {
			if (allObjectTypes.get(idx)==ObjectVars.PlanetSaturn) {
				newPoly = allObjects.get(idx).clone();
				for (int i = 0; i<allObjects.get(idx).length/2; i++){
					newPoly[2*i] += shiftX;
					newPoly[2*i+1] += shiftY;
				}
			} else {
				newPoly = allObjects.get(idx).clone();
				newPoly[0] += shiftX;
				newPoly[1] += shiftY;
			}
			updatePoly = newPoly.clone();
		} else if ((otype==ObjectVars.Boulder) | (otype==ObjectVars.Spike) | (otype==ObjectVars.Log)) {
			updatePoly = allObjects.get(idx).clone();
			updatePoly[0] += shiftX;
			updatePoly[1] += shiftY;
			newPoly[0] += shiftX;
			newPoly[1] += shiftY;
		} else {
			updatePoly = allObjects.get(idx).clone();
			for (int i = 0; i<allObjects.get(idx).length/2; i++) {
				updatePoly[2*i] += shiftX;
				updatePoly[2*i+1] += shiftY;
				newPoly[2*i] += shiftX;
				newPoly[2*i+1] += shiftY;
			}
		}
	}

	public void RotateObject(int idx, String mode, float angle) {
		if (mode.equals("arrow")) {
			if (allObjectArrows.get(idx) == null) return;
			updatePoly = allObjectArrows.get(idx).clone();
			for (int i = 0; i<allObjectArrows.get(idx).length; i++){
				if (i%2==0) {
					updatePoly[i] = allObjectCoords.get(idx)[0] + (allObjectArrows.get(idx)[i]-allObjectCoords.get(idx)[0])*(float) Math.cos(angle) - (allObjectArrows.get(idx)[i+1]-allObjectCoords.get(idx)[1])*(float) Math.sin(angle);
				} else {
					updatePoly[i] = allObjectCoords.get(idx)[1] + (allObjectArrows.get(idx)[i-1]-allObjectCoords.get(idx)[0])*(float) Math.sin(angle) + (allObjectArrows.get(idx)[i]-allObjectCoords.get(idx)[1])*(float) Math.cos(angle);
				}
			}
			
		} else if (mode.equals("object")) {
			if (allObjects.get(idx) == null) return;
			updatePoly = allObjects.get(idx).clone();
			for (int i = 0; i<allObjects.get(idx).length; i++){
				if (i%2==0) {
					updatePoly[i] = allObjectCoords.get(idx)[0] + (allObjects.get(idx)[i]-allObjectCoords.get(idx)[0])*(float) Math.cos(angle) - (allObjects.get(idx)[i+1]-allObjectCoords.get(idx)[1])*(float) Math.sin(angle);
				} else {
					updatePoly[i] = allObjectCoords.get(idx)[1] + (allObjects.get(idx)[i-1]-allObjectCoords.get(idx)[0])*(float) Math.sin(angle) + (allObjects.get(idx)[i]-allObjectCoords.get(idx)[1])*(float) Math.cos(angle);
				}
			}
		} else if (mode.equals("rotateentry")) {
			if (allObjects.get(idx) == null) return;
			updatePoly = new float[8];
			for (int i = 0; i<allObjects.get(idx).length/4; i++){
				updatePoly[2*i] = xcen + (allObjects.get(idx)[2*i+(tentry-1)*8]-xcen)*(float) Math.cos(angle) - (allObjects.get(idx)[2*i+1+(tentry-1)*8]-ycen)*(float) Math.sin(angle);
				updatePoly[2*i+1] = ycen + (allObjects.get(idx)[2*i+(tentry-1)*8]-xcen)*(float) Math.sin(angle) + (allObjects.get(idx)[2*i+1+(tentry-1)*8]-ycen)*(float) Math.cos(angle);
			}			
    	}
	}

	public void ScaleObject(int idx, float hside, float angle) {
		updatePoly = new float[8];
		float xc = 0.5f*(allObjects.get(idx)[0]+allObjects.get(idx)[4]);
		float yc = 0.5f*(allObjects.get(idx)[1]+allObjects.get(idx)[5]);
		for (int i = 0; i<updatePoly.length/2; i++) {
			updatePoly[2*i]   = ObjectVars.objectGateSwitch[2*i];
			updatePoly[2*i+1] = hside*Math.signum(ObjectVars.objectGateSwitch[2*i+1]);
		}
		PolygonOperations.RotateXYArray(updatePoly, angle-MathUtils.PI/2, 0, 0);
		for (int i = 0; i<updatePoly.length/2; i++) {
			updatePoly[2*i]   += xc;
			updatePoly[2*i+1] += yc;
		}
	}

	public void SelectTransport(String downup, int otype, boolean rotate, boolean circle) {
		if (otype == ObjectVars.Transport) {
			SelectObject(downup, otype, rotate, circle);
		} else {
			SelectObject(downup, ObjectVars.TransportInvisible, rotate, circle);
			if (objectSelect != -1) return;
			SelectObject(downup, ObjectVars.TransportInvisibleEarth, rotate, circle);
			if (objectSelect != -1) return;
			SelectObject(downup, ObjectVars.TransportInvisibleMars, rotate, circle);
			if (objectSelect != -1) return;
			SelectObject(downup, ObjectVars.TransportInvisibleMoon, rotate, circle);
			if (objectSelect != -1) return;
			SelectObject(downup, ObjectVars.TransportInvisibleZero, rotate, circle);
			if (objectSelect != -1) return;
		}
	}

	public void SelectDoorKey(String downup, boolean rotate, boolean circle) {
		SelectObject(downup, ObjectVars.DoorBlue, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.DoorGreen, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.DoorRed, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.KeyBlue, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.KeyGreen, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.KeyRed, rotate, circle);
		if (objectSelect != -1) return;
	}

	public void SelectGravity(String downup, int otype, boolean rotate, boolean circle) {
		SelectObject(downup, ObjectVars.Gravity, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.GravityEarth, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.GravityMars, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.GravityMoon, rotate, circle);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.GravityZero, rotate, circle);
		if (objectSelect != -1) return;
	}

	public void SelectPlanet(String downup, int otype, boolean rotate, boolean circle) {
		SelectObject(downup, ObjectVars.PlanetSun, rotate, true);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetMercury, rotate, true);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetVenus, rotate, true);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetEarth, rotate, true);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetMars, rotate, true);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetJupiter, rotate, true);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetSaturn, rotate, false);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetUranus, rotate, true);
		if (objectSelect != -1) return;
		SelectObject(downup, ObjectVars.PlanetNeptune, rotate, true);
		if (objectSelect != -1) return;
	}

	public void SelectGroupObjects(int objmin) {
		ResetSelect();
		float x1, x2, y1, y2, tmp;
		x1 = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		y1 = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		x2 = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		y2 = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		// Check the min max values
		if (x1 > x2) {
			tmp = x2;
			x2 = x1;
			x1 = tmp;
		}
		if (y1 > y2) {
			tmp = y2;
			y2 = y1;
			y1 = tmp;
		}
		// Loop through all polygons and find the polys that are inside the selection boundary
		if (mode==12) {
			// Using alternative polygons
			for (int i = objmin; i<allObjects_Alt.size(); i++){
				if ((x1 < allObjectCoords_Alt.get(i)[0]) & (allObjectCoords_Alt.get(i)[0] < x2) & (y1 < allObjectCoords_Alt.get(i)[1]) & (allObjectCoords_Alt.get(i)[1] < y2)) {
					// Object is inside selection
					groupArrays.add(i);
					groupPOD.add(1);
					groupTypes.add(allObjectTypes_Alt.get(i));
					groupPaths.add(allObjectArrows_Alt.get(i));
					groupTextures.add("");
					groupCoords.add(allObjectCoords_Alt.get(i));
					groupPolys.add(null);
				}
			}
		} else {
			for (int i = objmin; i<allObjects.size(); i++){
				if ((x1 < allObjectCoords.get(i)[0]) & (allObjectCoords.get(i)[0] < x2) & (y1 < allObjectCoords.get(i)[1]) & (allObjectCoords.get(i)[1] < y2)) {
					// Object is inside selection
					groupArrays.add(i);
					groupPOD.add(1);
					groupTypes.add(allObjectTypes.get(i));
					groupPaths.add(allObjectArrows.get(i));
					groupTextures.add("");
					groupCoords.add(allObjectCoords.get(i));
					groupPolys.add(null);
				}
			}
		}
	}

	public void SelectObject(String downup, int otype, boolean rotate, boolean circle) {
		ResetSelect();
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		} else if (downup.equals("up")){
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);			
		}
		boolean inside = false;
		for (int i = 0; i<allObjects.size(); i++){
			if (allObjectTypes.get(i) == otype) {
				if ((otype==ObjectVars.BallChain) | (otype==ObjectVars.Pendulum)) {
					if (!circle) {
						newPoly = new float[8];
						newPoly[0] = allObjects.get(i)[3];
						newPoly[1] = allObjects.get(i)[4];
						newPoly[2] = allObjects.get(i)[3]+allObjects.get(i)[5];
						newPoly[3] = allObjects.get(i)[4];
						newPoly[4] = allObjects.get(i)[3]+allObjects.get(i)[5];
						newPoly[5] = allObjects.get(i)[4]+allObjects.get(i)[6];
						newPoly[6] = allObjects.get(i)[3];
						newPoly[7] = allObjects.get(i)[4]+allObjects.get(i)[6];
						inside = PolygonOperations.PointInPolygon(newPoly,tempx,tempy);
					} else {
						if (Math.sqrt((tempx-allObjects.get(i)[0])*(tempx-allObjects.get(i)[0]) + (tempy-allObjects.get(i)[1])*(tempy-allObjects.get(i)[1])) < allObjects.get(i)[2]) {
							inside = true;
						}
					}
				} else if ((otype==ObjectVars.Transport) | (ObjectVars.IsTransportInvisible(otype)) | (otype==ObjectVars.GateSwitch) | (otype==ObjectVars.Bridge)) {
					tentry = 0;
					newPoly = new float[8];
					newPoly[0] = allObjects.get(i)[0];
					newPoly[1] = allObjects.get(i)[1];
					newPoly[2] = allObjects.get(i)[2];
					newPoly[3] = allObjects.get(i)[3];
					newPoly[4] = allObjects.get(i)[4];
					newPoly[5] = allObjects.get(i)[5];
					newPoly[6] = allObjects.get(i)[6];
					newPoly[7] = allObjects.get(i)[7];
					if (rotate) inside = PolygonOperations.PointInPolygon(allObjectArrows.get(i),tempx,tempy);
					else inside = PolygonOperations.PointInPolygon(newPoly,tempx,tempy);
					if (inside) tentry = 1;
					if ((!inside) & (!rotate)) {
						// Check the other entry point
						newPoly = new float[8];
						newPoly[0] = allObjects.get(i)[8];
						newPoly[1] = allObjects.get(i)[9];
						newPoly[2] = allObjects.get(i)[10];
						newPoly[3] = allObjects.get(i)[11];
						newPoly[4] = allObjects.get(i)[12];
						newPoly[5] = allObjects.get(i)[13];
						newPoly[6] = allObjects.get(i)[14];
						newPoly[7] = allObjects.get(i)[15];
						inside = PolygonOperations.PointInPolygon(newPoly,tempx,tempy);
						if (inside) tentry = 2;
					}
				} else {
					if (!circle) {
						if (rotate) inside = PolygonOperations.PointInPolygon(allObjectArrows.get(i),tempx,tempy);
						else inside = PolygonOperations.PointInPolygon(allObjects.get(i),tempx,tempy);					
					} else {
						if (rotate) {
							if (Math.sqrt((tempx-allObjects.get(i)[0])*(tempx-allObjects.get(i)[0]) + (tempy-allObjects.get(i)[1])*(tempy-allObjects.get(i)[1])) < allObjects.get(i)[2]) {
								inside = true;
							}
						} else {
							if (Math.sqrt((tempx-allObjects.get(i)[0])*(tempx-allObjects.get(i)[0]) + (tempy-allObjects.get(i)[1])*(tempy-allObjects.get(i)[1])) < allObjects.get(i)[2]) {
								inside = true;
							}
						}
					}
				}
			}
			if (inside == true) {
				objectSelect = i;
				return;
			}
		}
		objectSelect = -1;
	}

	public void SelectObjectAny(String downup) {
		ResetSelect();
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		} else if (downup.equals("up")){
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);			
		}
		boolean inside = false;
		int otype;
		for (int i = 0; i<allObjects.size(); i++){
			otype = allObjectTypes.get(i);
			if ((otype==ObjectVars.Finish) | (otype==ObjectVars.Start)) {
				inside = false;
			} else if ((otype==ObjectVars.BallChain) | (otype==ObjectVars.Pendulum)) {
				// First check the ball
				if (Math.sqrt((tempx-allObjects.get(i)[0])*(tempx-allObjects.get(i)[0]) + (tempy-allObjects.get(i)[1])*(tempy-allObjects.get(i)[1])) < allObjects.get(i)[2]) {
					inside = true;
				}
				// Now check the anchor
				if (!inside) {
					newPoly = new float[8];
					newPoly[0] = allObjects.get(i)[3];
					newPoly[1] = allObjects.get(i)[4];
					newPoly[2] = allObjects.get(i)[3]+allObjects.get(i)[5];
					newPoly[3] = allObjects.get(i)[4];
					newPoly[4] = allObjects.get(i)[3]+allObjects.get(i)[5];
					newPoly[5] = allObjects.get(i)[4]+allObjects.get(i)[6];
					newPoly[6] = allObjects.get(i)[3];
					newPoly[7] = allObjects.get(i)[4]+allObjects.get(i)[6];
					inside = PolygonOperations.PointInPolygon(newPoly,tempx,tempy);
				}
			} else if ((otype==ObjectVars.Transport) | (ObjectVars.IsTransportInvisible(otype)) | (otype==ObjectVars.GateSwitch) | (otype==ObjectVars.Bridge)) {
				tentry = 0;
				newPoly = new float[8];
				newPoly[0] = allObjects.get(i)[0];
				newPoly[1] = allObjects.get(i)[1];
				newPoly[2] = allObjects.get(i)[2];
				newPoly[3] = allObjects.get(i)[3];
				newPoly[4] = allObjects.get(i)[4];
				newPoly[5] = allObjects.get(i)[5];
				newPoly[6] = allObjects.get(i)[6];
				newPoly[7] = allObjects.get(i)[7];
				inside = PolygonOperations.PointInPolygon(newPoly,tempx,tempy);
				if (!inside) {
					// Check the other entry point
					newPoly = new float[8];
					newPoly[0] = allObjects.get(i)[8];
					newPoly[1] = allObjects.get(i)[9];
					newPoly[2] = allObjects.get(i)[10];
					newPoly[3] = allObjects.get(i)[11];
					newPoly[4] = allObjects.get(i)[12];
					newPoly[5] = allObjects.get(i)[13];
					newPoly[6] = allObjects.get(i)[14];
					newPoly[7] = allObjects.get(i)[15];
					inside = PolygonOperations.PointInPolygon(newPoly,tempx,tempy);
					if (inside) tentry = 2;
				} else tentry = 1;
			} else {
				if ((otype==ObjectVars.Boulder) | (otype==ObjectVars.Spike) | (otype==ObjectVars.Log)) {
					// Check all circle type objects
					if (Math.sqrt((tempx-allObjects.get(i)[0])*(tempx-allObjects.get(i)[0]) + (tempy-allObjects.get(i)[1])*(tempy-allObjects.get(i)[1])) < allObjects.get(i)[2]) {
						inside = true;
					}
				} else {
					// Check all polygon type objects
					inside = PolygonOperations.PointInPolygon(allObjects.get(i),tempx,tempy);					
				}
			}
			if (inside) {
				newPoly = null;
				objectSelect = i;
				return;
			}
		}
		newPoly = null;
		objectSelect = -1;
	}

	public void ShiftObject(float shiftX, float shiftY) {
		for (int i = 0; i<newPoly.length/2; i++){
			newPoly[2*i] += shiftX;
			newPoly[2*i+1] += shiftY;
		}
	}

	public void IncrementObject(int objNum) {
		if (ObjectVars.IsDoor(objNum)) {
			if (objNum == ObjectVars.DoorBlue) allObjectTypes.set(objectSelect, ObjectVars.DoorGreen);
			else if (objNum == ObjectVars.DoorGreen) allObjectTypes.set(objectSelect, ObjectVars.DoorRed);
			else if (objNum == ObjectVars.DoorRed) allObjectTypes.set(objectSelect, ObjectVars.DoorBlue);
		} else if (ObjectVars.IsKey(objNum)) {
			if (objNum == ObjectVars.KeyBlue) allObjectTypes.set(objectSelect, ObjectVars.KeyGreen);
			else if (objNum == ObjectVars.KeyGreen) allObjectTypes.set(objectSelect, ObjectVars.KeyRed);
			else if (objNum == ObjectVars.KeyRed) allObjectTypes.set(objectSelect, ObjectVars.KeyBlue);
		} else if (ObjectVars.IsGravity(objNum)) {
			if (objNum == ObjectVars.Gravity) allObjectTypes.set(objectSelect, ObjectVars.GravityEarth);
			else if (objNum == ObjectVars.GravityEarth) allObjectTypes.set(objectSelect, ObjectVars.GravityMars);
			else if (objNum == ObjectVars.GravityMars) allObjectTypes.set(objectSelect, ObjectVars.GravityMoon);
			else if (objNum == ObjectVars.GravityMoon) allObjectTypes.set(objectSelect, ObjectVars.GravityZero);
			else if (objNum == ObjectVars.GravityZero) allObjectTypes.set(objectSelect, ObjectVars.Gravity);
		} else if (ObjectVars.IsTransportInvisible(objNum)) {
			if (objNum == ObjectVars.TransportInvisible) allObjectTypes.set(objectSelect, ObjectVars.TransportInvisibleEarth);
			else if (objNum == ObjectVars.TransportInvisibleEarth) allObjectTypes.set(objectSelect, ObjectVars.TransportInvisibleMars);
			else if (objNum == ObjectVars.TransportInvisibleMars) allObjectTypes.set(objectSelect, ObjectVars.TransportInvisibleMoon);
			else if (objNum == ObjectVars.TransportInvisibleMoon) allObjectTypes.set(objectSelect, ObjectVars.TransportInvisibleZero);
			else if (objNum == ObjectVars.TransportInvisibleZero) allObjectTypes.set(objectSelect, ObjectVars.TransportInvisible);
		} else if (ObjectVars.IsPlanet(objNum)) {
			// Find the centre of the planet
			float shiftX, shiftY;
			if (allObjectTypes.get(objectSelect)==ObjectVars.PlanetSaturn) {
				int xmn = ObjectVars.GetSaturnMinMax(0,0);
				int xmx = ObjectVars.GetSaturnMinMax(0,1);
				int ymn = ObjectVars.GetSaturnMinMax(1,0);
				int ymx = ObjectVars.GetSaturnMinMax(1,1);
				float xmin = B2DVars.EPPM*allObjects.get(objectSelect)[xmn];
				float ymin = B2DVars.EPPM*allObjects.get(objectSelect)[ymn];
				float xmax = B2DVars.EPPM*allObjects.get(objectSelect)[xmx];
				float ymax = B2DVars.EPPM*allObjects.get(objectSelect)[ymx];
				shiftX = 0.5f*(xmin+xmax);
				shiftY = 0.5f*(ymin+ymax);
			} else {
				shiftX = allObjects.get(objectSelect)[0];
				shiftY = allObjects.get(objectSelect)[1];
			}
			// Loop through all planets to get the next one
			if (objNum == ObjectVars.PlanetSun) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetMercury);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetMercury, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetMercury) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetVenus);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetVenus, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetVenus) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetEarth);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetEarth, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetEarth) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetMars);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetMars, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetMars) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetJupiter);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetJupiter, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetJupiter) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetSaturn);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetSaturn, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetSaturn) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetUranus);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetUranus, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetUranus) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetNeptune);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetNeptune, shiftX, shiftY);
			} else if (objNum == ObjectVars.PlanetNeptune) {
				allObjectTypes.set(objectSelect, ObjectVars.PlanetSun);
				updatePoly = ObjectVars.MakePlanet(ObjectVars.PlanetSun, shiftX, shiftY);
			}
			// Finally, update the object with the updated poly
			allObjects.set(objectSelect, updatePoly.clone());
		}
	}

	public void UpdateObject(int idx, String mode, boolean saveit) {
		changesMade = true;
		if (mode.equals("update")) {
			newPoly = allObjects.set(idx, updatePoly.clone());
		} else if (mode.equals("move")) {
			// Get the total shift
			float shiftX = updatePoly[0]-allObjects.get(idx)[0];
			float shiftY = updatePoly[1]-allObjects.get(idx)[1];
			newPoly = allObjects.set(idx, updatePoly.clone());
			// Update the Coordinate
			newCoord = allObjectCoords.get(idx).clone();
			newCoord[0] += shiftX;
			newCoord[1] += shiftY;
			newPoly = allObjectCoords.set(idx, newCoord.clone());
			// Update the Arrow
			if (allObjectArrows.get(idx) != null) {
				updatePoly = allObjectArrows.get(idx);
				for (int i = 0; i<allObjectArrows.get(idx).length/2; i++){
					updatePoly[2*i] += shiftX;
					updatePoly[2*i+1] += shiftY;
				}
			}
		} else if (mode.equals("moveball")) {
			float shiftX = updatePoly[0]-allObjects.get(idx)[0];
			float shiftY = updatePoly[1]-allObjects.get(idx)[1];
			newPoly = allObjects.get(idx).clone();
			newPoly[0] = updatePoly[0];
			newPoly[1] = updatePoly[1];
			updatePoly = allObjects.set(idx, newPoly.clone());
			// Update the Coordinate
			newCoord = allObjectCoords.get(idx).clone();
			newCoord[0] += shiftX;
			newCoord[1] += shiftY;
			newPoly = allObjectCoords.set(idx, newCoord.clone());
		} else if ((mode.equals("moveentry")) | (mode.equals("rotateentry"))) {
			float shiftX = updatePoly[0]-allObjects.get(idx)[0+8*(tentry-1)];
			float shiftY = updatePoly[1]-allObjects.get(idx)[1+8*(tentry-1)];
			// Update the Coordinate
			if ((mode.equals("moveentry")) & (tentry==1)) {
				newCoord = allObjectCoords.get(idx).clone();
				newCoord[0] += shiftX;
				newCoord[1] += shiftY;
				newPoly = allObjectCoords.set(idx, newCoord.clone());
			}
			// Update the Arrow
			if ((allObjectArrows.get(idx) != null) & (tentry==1) & (mode.equals("moveentry"))) {
				newPoly = allObjectArrows.get(idx).clone();
				for (int i = 0; i<allObjectArrows.get(idx).length/2; i++){
					newPoly[2*i] += shiftX;
					newPoly[2*i+1] += shiftY;
				}
				newPoly = allObjectArrows.set(idx, newPoly.clone());
			}
			newPoly = allObjects.get(idx).clone();
			newPoly[0+8*(tentry-1)] = updatePoly[0];
			newPoly[1+8*(tentry-1)] = updatePoly[1];
			newPoly[2+8*(tentry-1)] = updatePoly[2];
			newPoly[3+8*(tentry-1)] = updatePoly[3];
			newPoly[4+8*(tentry-1)] = updatePoly[4];
			newPoly[5+8*(tentry-1)] = updatePoly[5];
			newPoly[6+8*(tentry-1)] = updatePoly[6];
			newPoly[7+8*(tentry-1)] = updatePoly[7];
			updatePoly = allObjects.set(idx, newPoly.clone());
			tentry = 0;
		} else if (mode.equals("moveanchor")) {
			newPoly = allObjects.get(idx).clone();
			newPoly[3] = updatePoly[0];
			newPoly[4] = updatePoly[1];
			newPoly[5] = updatePoly[2]-updatePoly[0];
			newPoly[6] = updatePoly[5]-updatePoly[1];
			updatePoly = allObjects.set(idx, newPoly.clone());
		} else if (mode.equals("rotateobject")) {
			newPoly = allObjects.set(idx, updatePoly.clone());
		} else if (mode.equals("rotatearrow")) {
			newPoly = allObjectArrows.set(idx, updatePoly.clone());
		} else if (mode.equals("scalegate")) {
			newPoly = allObjects.get(idx).clone();
			for (int i = 0; i<updatePoly.length; i++) {
				newPoly[i] = updatePoly[i];
				allObjects.set(idx, newPoly.clone());
			}
		}
		// Nullify the update Polygon
		updatePoly = null;
		if (saveit) SaveLevel(true);
	}

    /////////////////////////////////
   ///                           ///
  ///  ALL DECORATE OPERATIONS  ///
 ///                           ///
/////////////////////////////////

	public void AddDecor(int otype, float xcen, float ycen, float angle) {
		changesMade = true;
		if ((otype==DecorVars.Grass) & (polyHover != -1) & (segmHover != -1)) {
			MakeGrass();
			allDecorPolys.add(polyHover);
		} else {
			MakeDecor(otype, xcen, ycen, angle);
			newCoord[0] = xcen;
			newCoord[1] = ycen;
			allDecorPolys.add(-1);
		}
		allDecors.add(newPoly.clone());
		allDecorTypes.add(otype);
		allDecorImages.add(null);
		SaveLevel(true);
	}

    public void CopyDecor(float[] newPoly, int idx) {
    	changesMade = true;
		allDecors.add(newPoly);
		if (mode == 12) {
			allDecorTypes.add(allDecorTypes_Alt.get(idx));
			allDecorPolys.add(allDecorPolys_Alt.get(idx));
			allDecorImages.add(allDecorImages_Alt.get(idx));
		} else {
			allDecorTypes.add(allDecorTypes.get(idx));
			allDecorPolys.add(allDecorPolys.get(idx));
			allDecorImages.add(allDecorImages.get(idx));
		}
		SaveLevel(true);
	}

    public void AddAllGrass() {
    	// Start by deleting all grass
    	DeleteAllGrass();
    	// Now add all grass back in
		for (int i = 0; i<allPolygons.size(); i++) {
			if ((allPolygonTypes.get(i)%2 == 0) && (allPolygonTypes.get(i) != 8) && (allPolygonTypes.get(i) != 10)){
				polyHover = i;
				for (int j = 0; j < allPolygons.get(i).length/2; j++) {
					segmHover = j;
					AddDecor(DecorVars.Grass, 0.0f, 0.0f, 0.0f);
				}
			}
		}
		polyHover = -1;
		segmHover = -1;
    }

    public void DeleteAllGrass() {
    	boolean noGrass;
    	while (true) {
    		noGrass = true;
			for (int i = 0; i<allDecors.size(); i++) {
				if (allDecorTypes.get(i) == DecorVars.Grass) {
					DeleteDecor(i, true);
					noGrass=false;
					break;
				}
			}
			if (noGrass) break;
    	}
    }
	
	public void DeleteDecor(int idx, boolean autosave) {
		changesMade = true;
		allDecors.remove(idx);
		allDecorTypes.remove(idx);
		allDecorPolys.remove(idx);
		allDecorImages.remove(idx);
		decorSelect = -1;
		if (autosave) SaveLevel(true);
	}
  	
	public void FlipDecor(int xy) {
    	float xcen = 0.0f, ycen=0.0f, cntr=0.0f;
    	float[] tempPoly;
//        if (updateGroupPoly != null) {
//        	// Find the centre of the polygons
//        	for (int i = 0; i<updateGroupPoly.size(); i++){
//        			if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 0) {
//        				for (int j=0; j<updateGroupPoly.get(i).length/2; j++) {
//        					xcen += updateGroupPoly.get(i)[2*j];
//        					ycen += updateGroupPoly.get(i)[2*j+1];
//        					cntr += 1.0f;
//        				}
//        			}
//        			else if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 1) {
//        				xcen += updateGroupPoly.get(i)[0];
//        				ycen += updateGroupPoly.get(i)[1];
//        				cntr += 1.0f;
//        			}
//        	}
//        	xcen /= cntr;
//        	ycen /= cntr;
//        	// Now flip in the x or y direction
//        	for (int i = 0; i<updateGroupPoly.size(); i++){
//        		tempPoly = updateGroupPoly.get(i).clone();
//    			if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 0) {
//    				for (int j=0; j<tempPoly.length/2; j++) {
//    					if (xy == 0) tempPoly[2*j] = 2*xcen - updateGroupPoly.get(i)[2*j];
//    					else if (xy == 1) tempPoly[2*j+1] = 2*ycen - updateGroupPoly.get(i)[2*j+1];
//    				}
//    			}
//    			else if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 1) {
//					if (xy == 0) tempPoly[0] = 2*xcen - updateGroupPoly.get(i)[0];
//					else if (xy == 1) tempPoly[1] = 2*ycen - updateGroupPoly.get(i)[1];
//    			}
//				updateGroupPoly.set(i, tempPoly.clone());
//        	}
//        } else
    	if (updatePoly != null) {
        	// Find the centre of the polygons
			for (int j=0; j<updatePoly.length/2; j++) {
				xcen += updatePoly[2*j];
				ycen += updatePoly[2*j+1];
				cntr += 1.0f;
			}
        	xcen /= cntr;
        	ycen /= cntr;
        	// Now flip in the x or y direction
			tempPoly = updatePoly.clone();
			for (int j=0; j<tempPoly.length/2; j++) {
				if (xy == 0) tempPoly[2*j] = 2*xcen - updatePoly[2*j];
				else if (xy == 1) tempPoly[2*j+1] = 2*ycen - updatePoly[2*j+1];
			}
			updatePoly = tempPoly.clone();
        }
	}

	public void MakeDecor(int otype, float xcen, float ycen, float angle) {
		angle *= MathUtils.PI/180.0;
		if (otype==DecorVars.Waterfall) {
			newPoly = new float[DecorVars.decorWaterfall.length];
			for (int i=0; i<(DecorVars.decorWaterfall.length-1)/2; i++) {
				newPoly[2*i] = DecorVars.decorWaterfall[2*i] + xcen;
				newPoly[2*i+1] = DecorVars.decorWaterfall[2*i+1] + ycen;
			}
			newPoly[DecorVars.decorWaterfall.length-1] = DecorVars.decorWaterfall[DecorVars.decorWaterfall.length-1];
		} else if (otype==DecorVars.Rain) {
			newPoly = new float[DecorVars.decorRain.length];
			for (int i=0; i<(DecorVars.decorRain.length-1)/2; i++) {
				newPoly[2*i] = DecorVars.decorRain[2*i] + xcen;
				newPoly[2*i+1] = DecorVars.decorRain[2*i+1] + ycen;
			}
			newPoly[DecorVars.decorRain.length-1] = DecorVars.decorRain[DecorVars.decorRain.length-1];
		} else if (DecorVars.IsRoadSign(otype)) {
			newPoly = new float[DecorVars.decorCircleRoadSign.length];
			newPoly[0] = DecorVars.decorCircleRoadSign[0] + xcen;
			newPoly[1] = DecorVars.decorCircleRoadSign[1] + ycen;
			newPoly[2] = DecorVars.decorCircleRoadSign[2];
			newPoly[3] = DecorVars.decorCircleRoadSign[3];
		} else if (otype==DecorVars.BinBag) {
			newPoly = DecorVars.GetRectMultiple(otype, 0, xcen, ycen);
		} else if (otype==DecorVars.Rock) {
			newPoly = DecorVars.GetRectMultiple(otype, 0, xcen, ycen);
		} else if (otype==DecorVars.Tree) {
			newPoly = DecorVars.GetRectMultiple(otype, 0, xcen, ycen);
		} else if (otype==DecorVars.TyreStack) {
			newPoly = DecorVars.GetRectMultiple(otype, 0, xcen, ycen);
		}
	}

	private boolean CheckGroupGrass() {
		for (int ii=0; ii<groupPOD.size(); ii++) {
			if ((groupPOD.get(ii)==2) & (groupTypes.get(ii)==DecorVars.Grass)) {
				return true;
			}
		}
		return false;
	}

	public void MakeGrass() {
		float grassIn  = 3.5f;
		float grassOut = 7.0f;
		float angle = 0.0f;
		int lenPoly = allPolygons.get(polyHover).length/2;
		Vector2 vecA = new Vector2(), vecB = new Vector2();
		newPoly = new float[8];
		int segmNext = segmHover + 1;
		if (segmNext == lenPoly) segmNext = 0;
		// Do the first four vertices
		// Find the angle between the two segments connected to segmHover vertex
		if (segmHover==0) {
			vecA = new Vector2(allPolygons.get(polyHover)[2*lenPoly-2]-allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*lenPoly-1]-allPolygons.get(polyHover)[2*segmHover+1]);
			vecB = new Vector2(allPolygons.get(polyHover)[2*segmHover+2]-allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+3]-allPolygons.get(polyHover)[2*segmHover+1]);
		} else if (segmHover == lenPoly - 1) {
			vecA = new Vector2(allPolygons.get(polyHover)[2*segmHover-2]-allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover-1]-allPolygons.get(polyHover)[2*segmHover+1]);
			vecB = new Vector2(allPolygons.get(polyHover)[0]-allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[1]-allPolygons.get(polyHover)[2*segmHover+1]);			
		} else {
			vecA = new Vector2(allPolygons.get(polyHover)[2*segmHover-2]-allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover-1]-allPolygons.get(polyHover)[2*segmHover+1]);
			vecB = new Vector2(allPolygons.get(polyHover)[2*segmHover+2]-allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+3]-allPolygons.get(polyHover)[2*segmHover+1]);
		}
		if ((vecA.len() != 0) & (vecB.len() != 0)) {
			angle = (float) Math.sin(Math.abs(Math.acos((vecA.x*vecB.x + vecA.y*vecB.y)/(vecA.len()*vecB.len())))/2);
			vecA.nor().add(vecB.nor()).nor();
			// Determine which direction is inside the polygon
			if (PolygonOperations.PointInPolygon(allPolygons.get(polyHover), vecA.x*grassIn/angle + allPolygons.get(polyHover)[2*segmHover], vecA.y*grassIn/angle+ allPolygons.get(polyHover)[2*segmHover+1])) {
				newPoly[0] = allPolygons.get(polyHover)[2*segmHover]   + vecA.x*grassIn/angle;
				newPoly[1] = allPolygons.get(polyHover)[2*segmHover+1] + vecA.y*grassIn/angle;
				newPoly[2] = allPolygons.get(polyHover)[2*segmHover]   - vecA.x*grassOut/angle;
				newPoly[3] = allPolygons.get(polyHover)[2*segmHover+1] - vecA.y*grassOut/angle;
			} else {
				newPoly[2] = allPolygons.get(polyHover)[2*segmHover]   + vecA.x*grassOut/angle;
				newPoly[3] = allPolygons.get(polyHover)[2*segmHover+1] + vecA.y*grassOut/angle;
				newPoly[0] = allPolygons.get(polyHover)[2*segmHover]   - vecA.x*grassIn/angle;
				newPoly[1] = allPolygons.get(polyHover)[2*segmHover+1] - vecA.y*grassIn/angle;
			}
		} else {
			newPoly = allPolygons.get(polyHover).clone();
			Message("Overlapping vertices in polygon! Delete one vertex before adding grass", 2);
			Message("Grass applied to the entire polygon", 1);
			return;
		}
		// Do the second four vertices
		if (segmNext==0) {
			vecA = new Vector2(allPolygons.get(polyHover)[2*lenPoly-2]-allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[2*lenPoly-1]-allPolygons.get(polyHover)[2*segmNext+1]);
			vecB = new Vector2(allPolygons.get(polyHover)[2*segmNext+2]-allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[2*segmNext+3]-allPolygons.get(polyHover)[2*segmNext+1]);
		} else if (segmNext == lenPoly - 1) {
			vecA = new Vector2(allPolygons.get(polyHover)[2*segmNext-2]-allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[2*segmNext-1]-allPolygons.get(polyHover)[2*segmNext+1]);
			vecB = new Vector2(allPolygons.get(polyHover)[0]-allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[1]-allPolygons.get(polyHover)[2*segmNext+1]);
		} else {
			vecA = new Vector2(allPolygons.get(polyHover)[2*segmNext-2]-allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[2*segmNext-1]-allPolygons.get(polyHover)[2*segmNext+1]);
			vecB = new Vector2(allPolygons.get(polyHover)[2*segmNext+2]-allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[2*segmNext+3]-allPolygons.get(polyHover)[2*segmNext+1]);
		}
		if ((vecA.len() != 0) & (vecB.len() != 0)) {
			angle = (float) Math.sin(Math.abs(Math.acos((vecA.x*vecB.x + vecA.y*vecB.y)/(vecA.len()*vecB.len())))/2);
			vecA.nor().add(vecB.nor()).nor();
			// Determine which direction is inside the polygon
			if (PolygonOperations.PointInPolygon(allPolygons.get(polyHover), vecA.x*grassIn/angle + allPolygons.get(polyHover)[2*segmNext], vecA.y*grassIn/angle + allPolygons.get(polyHover)[2*segmNext+1])) {
				newPoly[6] = allPolygons.get(polyHover)[2*segmNext]   + vecA.x*grassIn/angle;
				newPoly[7] = allPolygons.get(polyHover)[2*segmNext+1] + vecA.y*grassIn/angle;
				newPoly[4] = allPolygons.get(polyHover)[2*segmNext]   - vecA.x*grassOut/angle;
				newPoly[5] = allPolygons.get(polyHover)[2*segmNext+1] - vecA.y*grassOut/angle;
			} else {
				newPoly[4] = allPolygons.get(polyHover)[2*segmNext]   + vecA.x*grassOut/angle;
				newPoly[5] = allPolygons.get(polyHover)[2*segmNext+1] + vecA.y*grassOut/angle;
				newPoly[6] = allPolygons.get(polyHover)[2*segmNext]   - vecA.x*grassIn/angle;
				newPoly[7] = allPolygons.get(polyHover)[2*segmNext+1] - vecA.y*grassIn/angle;
			}
		} else {
			newPoly = allPolygons.get(polyHover).clone();
			Message("Overlapping vertices in polygon! Delete one vertex before adding grass", 2);
			Message("Grass applied to the entire polygon", 1);
		}
	}

	public void FindNearestSegmentDecor(boolean hover) {
		int idxa, idxb, idxmin, polymin, flag;
		float grad, intc, gradb, intcb, xint, yint, xa, xb, ya, yb;
		float dist, tdist, mindist;
		mindist = 0.0f;
		idxmin = 0;
		polymin = 0;
		flag = 0;
		float[] arraySegm;
		for (int j=0; j<allDecors.size(); j++) {
			if ((!hover) & (decorSelect != -1)) {
				if (j!=decorSelect) continue;
			}
			if ((allDecorTypes.get(j) == DecorVars.Waterfall) | (allDecorTypes.get(j) == DecorVars.Rain)) {
				if ((modeParent.equals("Rain")) & (allDecorTypes.get(j)!=DecorVars.Rain)) continue;
				if ((modeParent.equals("Waterfall")) & (allDecorTypes.get(j)!=DecorVars.Waterfall)) continue;
				arraySegm = Arrays.copyOfRange(allDecors.get(j).clone(), 0, 8);
				for (int i=0; i<arraySegm.length/2; i++) {
					idxa = i;
					if (i == arraySegm.length/2 - 1) idxb = 0;
					else idxb = i+1;
					// Calculate the gradient
					xa = arraySegm[2*idxa];
					ya = arraySegm[2*idxa+1];
					xb = arraySegm[2*idxb];
					yb = arraySegm[2*idxb+1];
					if (xa==xb) {
						if (ya>yb) {
							if (tempy>ya) yint = tempy-ya;
							else if (tempy<yb) yint = yb-tempy;
							else yint = 0.0f;
						} else {
							if (tempy>yb) yint = tempy-yb;
							else if (tempy<ya) yint = ya-tempy;
							else yint = 0.0f;
						}
						dist = (float) Math.sqrt((tempx-xa)*(tempx-xa) + yint*yint);
					} else if (ya==yb) {
						if (xa>xb) {
							if (tempx>xa) yint = tempx-xa;
							else if (tempx<xb) yint = xb-tempx;
							else yint = 0.0f;
						} else {
							if (tempx>xb) yint = tempx-xb;
							else if (tempx<xa) yint = xa-tempx;
							else yint = 0.0f;
						}
						dist = (float) Math.sqrt((tempy-ya)*(tempy-ya) + yint*yint);
					} else {
						grad = (yb-ya)/(xb-xa);
						intc = ya - grad*xa;
						gradb = -(xb-xa)/(yb-ya);
						intcb = tempy - gradb*tempx;
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
						dist = (float) Math.sqrt((tempx-xint)*(tempx-xint) + (tempy-yint)*(tempy-yint));
					}
					if ((dist < mindist) | (flag==0)) {
						mindist = dist;
						idxmin = i;
						polymin = j;
						flag=1;
					}
				}
			}
		}
		if (hover) {
			decorHover = polymin;
			vertHover = idxmin;
			segmHover = idxmin;
		} else {
			decorSelect = polymin;
			vertSelect = idxmin;
			segmSelect = idxmin;
		}
	}

	public void ScaleDecor(int idx, float scale) {
		float xcen, ycen;
		if (scale < 0.0f) scale *= -1.0f;
		updatePoly = allDecors.get(idx).clone();
		if (DecorVars.IsRect(allDecorTypes.get(idx))) {
			xcen = 0.5f*(allDecors.get(idx)[0] + allDecors.get(idx)[4]);
			ycen = 0.5f*(allDecors.get(idx)[1] + allDecors.get(idx)[5]);
			for (int i = 0; i<allDecors.get(idx).length-1; i++){
				if (i%2==0) {
					updatePoly[i] = xcen + (allDecors.get(idx)[i]-xcen)*scale;
				} else {
					updatePoly[i] = ycen + (allDecors.get(idx)[i]-ycen)*scale;
				}
			}
		}
	}

	public void MoveDecor(int idx, String mode, float shiftX, float shiftY) {
		if ((mode.equals("circle")) || (allDecors.get(idx).length==3) || (DecorVars.IsRoadSign(allDecorTypes.get(idx)))) {
			updatePoly = allDecors.get(idx).clone();
			updatePoly[0] += shiftX;
			updatePoly[1] += shiftY;
		} else if (mode.equals("polygon")) {
			updatePoly = allDecors.get(idx).clone();
			for (int i = 0; i<allDecors.get(idx).length/2; i++) {
				updatePoly[2*i] += shiftX;
				updatePoly[2*i+1] += shiftY;
			}
		} else if (mode.equals("WForRain")) {
			updatePoly = Arrays.copyOfRange(allDecors.get(idx).clone(), 0, 8);
			for (int i = 0; i<allDecors.get(idx).length/2; i++) {
				updatePoly[2*i] += shiftX;
				updatePoly[2*i+1] += shiftY;
			}
		} else {
			// Just do the same as polygon
			updatePoly = allDecors.get(idx).clone();
			for (int i = 0; i<allDecors.get(idx).length/2; i++) {
				updatePoly[2*i] += shiftX;
				updatePoly[2*i+1] += shiftY;
			}
		}
	}
	
	public void RotateDecor(int idx, String mode, float angle) {
		if (allDecors.get(idx) == null) return;
		if (mode.equals("object")) {
			updatePoly = allDecors.get(idx).clone();
			for (int i = 0; i<allDecors.get(idx).length; i++){
				if (i%2==0) {
					updatePoly[i] = cursposx + (allDecors.get(idx)[i]-cursposx)*(float) Math.cos(angle) - (allDecors.get(idx)[i+1]-cursposy)*(float) Math.sin(angle);
				} else {
					updatePoly[i] = cursposy + (allDecors.get(idx)[i-1]-cursposx)*(float) Math.sin(angle) + (allDecors.get(idx)[i]-cursposy)*(float) Math.cos(angle);
				}
			}
    	} else if (mode.equals("rect")) {
			updatePoly = new float[9];
			float xcen;
			float ycen;
			for (int i = 0; i<8; i++){
				xcen = (float) (0.5*(allDecors.get(idx)[0] + allDecors.get(idx)[4]));
				ycen = (float) 0.5*(allDecors.get(idx)[1] + allDecors.get(idx)[5]);
				if (i%2==0) {
					updatePoly[i] = xcen + (allDecors.get(idx)[i]-xcen)*(float) Math.cos(angle) - (allDecors.get(idx)[i+1]-ycen)*(float) Math.sin(angle);
				} else {
					updatePoly[i] = ycen + (allDecors.get(idx)[i-1]-xcen)*(float) Math.sin(angle) + (allDecors.get(idx)[i]-ycen)*(float) Math.cos(angle);
				}
			}
			updatePoly[8] = allDecors.get(idx)[8];
    	} else if (mode.equals("roadsign")) {
    		updatePoly = allDecors.get(idx).clone();
    		updatePoly[3] = angle;
    	}
	}

	public void SelectGroupDecors() {
		ResetSelect();
		float x1, x2, y1, y2, tmp;
		float[] meanxy = new float[2];
		x1 = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		y1 = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		x2 = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		y2 = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		// Check the min max values
		if (x1 > x2) {
			tmp = x2;
			x2 = x1;
			x1 = tmp;
		}
		if (y1 > y2) {
			tmp = y2;
			y2 = y1;
			y1 = tmp;
		}
		// Loop through all polygons and find the polys that are inside the selection boundary
		if (mode==12) {
			for (int i = 0; i<allDecors_Alt.size(); i++){
				if ((allDecors_Alt.get(i).length == 3) || (DecorVars.IsRoadSign(allDecorTypes_Alt.get(i)))) {
					// It's a circle
					meanxy[0] = allDecors.get(i)[0];
					meanxy[1] = allDecors.get(i)[1];
				} else meanxy = PolygonOperations.MeanXY(allDecors_Alt.get(i).clone());
				// Test if the decor is inside the bounding box
				if ((x1 < meanxy[0]) & (meanxy[0] < x2) & (y1 < meanxy[1]) & (meanxy[1] < y2)) {
					// Poly is inside selection
					groupArrays.add(i);
					groupPOD.add(2);
					groupTypes.add(allDecorTypes_Alt.get(i));
					groupPaths.add(null);
					groupTextures.add("");
					groupCoords.add(null);
					groupPolys.add(allDecorPolys_Alt.get(i));
				}
			}
		} else {
			for (int i = 0; i<allDecors.size(); i++){
				if ((allDecors.get(i).length == 3) || (DecorVars.IsRoadSign(allDecorTypes.get(i)))) {
					// It's a circle
					meanxy[0] = allDecors.get(i)[0];
					meanxy[1] = allDecors.get(i)[1];
				} else meanxy = PolygonOperations.MeanXY(allDecors.get(i).clone());
				// Test if the decor is inside the bounding box
				if ((x1 < meanxy[0]) & (meanxy[0] < x2) & (y1 < meanxy[1]) & (meanxy[1] < y2)) {
					// Poly is inside selection
					groupArrays.add(i);
					groupPOD.add(2);
					groupTypes.add(allDecorTypes.get(i));
					groupPaths.add(null);
					groupTextures.add("");
					groupCoords.add(null);
					groupPolys.add(allDecorPolys.get(i));
				}
			}
		}
	}

	public void SelectDecorSign(String downup, int otype, boolean rotate, boolean circle) {
		SelectDecor(downup, DecorVars.RoadSign_10, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_20, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_30, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_40, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_50, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_60, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_80, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_100, rotate, circle);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_Bumps, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_Dot, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_Dash, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_DoNotEnter, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_Exclamation, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_Motorbike, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_NoMotorbike, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_RampAhead, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_ReduceSpeed, rotate, true);
		if (decorSelect != -1) return;
		SelectDecor(downup, DecorVars.RoadSign_Stop, rotate, true);
		if (decorSelect != -1) return;
	}
	public void SelectDecor(String downup, int otype, boolean rotate, boolean circle) {
		// otype = -1 means don't check the object type
		ResetSelect();
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		} else if (downup.equals("up")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*SCRWIDTH);
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*SCRHEIGHT);			
		}
		boolean inside = false;
		decorSelect = -1;
		for (int i = 0; i<allDecors.size(); i++) {
			if ((allDecorTypes.get(i) == otype) | (otype == -1)) {
				if (!circle) {
					if ((otype==DecorVars.Waterfall) | (otype==DecorVars.Rain)) {
						inside = PolygonOperations.PointInPolygon(Arrays.copyOfRange(allDecors.get(i).clone(),0,8),tempx,tempy);
					} else {
						inside = PolygonOperations.PointInPolygon(allDecors.get(i),tempx,tempy);
					}
				} else {
					if (Math.sqrt((tempx-allDecors.get(i)[0])*(tempx-allDecors.get(i)[0]) + (tempy-allDecors.get(i)[1])*(tempy-allDecors.get(i)[1])) < allDecors.get(i)[2]) {
						inside = true;
					}
				}
			}
			if (inside == true) {
				decorSelect = i;
				break;
			}
		}
		return;
	}

	public void ShiftDecor(float shiftX, float shiftY) {
		for (int i = 0; i<newPoly.length/2; i++){
			newPoly[2*i] += shiftX;
			newPoly[2*i+1] += shiftY;
		}
	}

	public void IncrementDecorSign() {
		int dectype = allDecorTypes.get(decorSelect);
		if (dectype == DecorVars.RoadSign_Stop) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_DoNotEnter);
		} else if (dectype == DecorVars.RoadSign_DoNotEnter) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_Bumps);
		} else if (dectype == DecorVars.RoadSign_Bumps) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_Exclamation);
		} else if (dectype == DecorVars.RoadSign_Exclamation) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_Motorbike);
		} else if (dectype == DecorVars.RoadSign_Motorbike) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_NoMotorbike);
		} else if (dectype == DecorVars.RoadSign_NoMotorbike) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_RampAhead);
		} else if (dectype == DecorVars.RoadSign_RampAhead) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_ReduceSpeed);
		} else if (dectype == DecorVars.RoadSign_ReduceSpeed) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_10);
		} else if (dectype == DecorVars.RoadSign_10) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_20);
		} else if (dectype == DecorVars.RoadSign_20) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_30);
		} else if (dectype == DecorVars.RoadSign_30) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_40);
		} else if (dectype == DecorVars.RoadSign_40) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_50);
		} else if (dectype == DecorVars.RoadSign_50) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_60);
		} else if (dectype == DecorVars.RoadSign_60) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_80);
		} else if (dectype == DecorVars.RoadSign_80) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_100);
		} else if (dectype == DecorVars.RoadSign_100) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_Dash);
		} else if (dectype == DecorVars.RoadSign_Dash) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_Dot);
		} else if (dectype == DecorVars.RoadSign_Dot) {
			allDecorTypes.set(decorSelect, DecorVars.RoadSign_Stop);
		}
	}

	public void IncrementDecor(int objNum) {
		// This routine only works on rectangular decorations
		float shiftX = 0.5f*(allDecors.get(decorSelect)[0]+allDecors.get(decorSelect)[4]);
		float shiftY = 0.5f*(allDecors.get(decorSelect)[1]+allDecors.get(decorSelect)[5]);
		updatePoly = DecorVars.GetNextRectMultiple(objNum, (int) allDecors.get(decorSelect)[8], shiftX, shiftY);
		allDecors.set(decorSelect, updatePoly.clone());
		updatePoly = null;
	}
	
	public void UpdateDecor(int idx, String mode) {
		changesMade = true;
		if (mode.equals("move")) {
			newPoly = allDecors.set(idx, updatePoly.clone());
		} else if (mode.equals("movecircle")) {
			float shiftX = updatePoly[0]-allDecors.get(idx)[0];
			float shiftY = updatePoly[1]-allDecors.get(idx)[1];
			newPoly = allDecors.get(idx).clone();
			newPoly[0] = updatePoly[0];
			newPoly[1] = updatePoly[1];
			updatePoly = allDecors.set(idx, newPoly.clone());
		} else if (mode.equals("rotateobject")) {
			//if (updatePoly != null) newPoly = allDecors.set(idx, updatePoly.clone());
			newPoly = allDecors.set(idx, updatePoly.clone());
		} else if (mode.equals("scale")) {
			newPoly = allDecors.set(idx, updatePoly.clone());
		} else if (mode.equals("WForRain")) {
			newPoly = allDecors.get(idx).clone();
			for (int ii=0; ii<4; ii++) {
				newPoly[2*ii] = updatePoly[2*ii];
				newPoly[2*ii+1] = updatePoly[2*ii+1];
			}
			updatePoly = allDecors.set(idx, newPoly.clone());
		}
		// Nullify the update Polygon
		updatePoly = null;
		SaveLevel(true);
	} 

	 /////////////////////////////////
	///                           ///
   ///   ALL CAMERA OPERATIONS   ///
  ///                           ///
 /////////////////////////////////

	private void MoveCameraTo(float cenx, float ceny, boolean resetZoom) {
		cam.translate(cenx-cam.position.x, ceny-cam.position.y);
		if (resetZoom) cam.zoom = 0.05f;
	}

	private void ControlPan() {
		// Perform Pan
		//startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		//startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		//endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		//endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		posx = cam.zoom*(GameInput.MBDOWNX-GameInput.MBDRAGX)/BikeGame.SCALE;
		posy = cam.zoom*(GameInput.MBDRAGY-GameInput.MBDOWNY)/BikeGame.SCALE;
		if ((cam.position.x>(maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH) & (cam.position.x<(maxzoom-0.5f*cam.zoom*SCRWIDTH))) {
			cam.translate(posx,0,0);
		}
		if ((cam.position.y>(maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT) & (cam.position.y<(maxzoom-0.5f*cam.zoom*SCRHEIGHT))) {
			cam.translate(0,posy,0);
		}
		// Make sure that we are within bounds
		if ((cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH) & (cam.position.x > (maxzoom-0.5f*cam.zoom*SCRWIDTH))){
		} else if (cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH) {
			cam.translate((maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH-cam.position.x,0,0);
		} else if (cam.position.x > (maxzoom-0.5f*cam.zoom*SCRWIDTH)) {
			cam.translate((maxzoom-0.5f*cam.zoom*SCRWIDTH)-cam.position.x,0,0);
		}
		if ((cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT) & (cam.position.y > (maxzoom-0.5f*cam.zoom*SCRHEIGHT))) {
		} else if (cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT) {
			cam.translate(0,(maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT-cam.position.y,0);
		} else if (cam.position.y > (maxzoom-0.5f*cam.zoom*SCRHEIGHT)) {
			cam.translate(0,(maxzoom-0.5f*cam.zoom*SCRHEIGHT)-cam.position.y,0);
		}
		// Update the cursor position
	    GameInput.MBDOWNX = GameInput.MBDRAGX;
	    GameInput.MBDOWNY = GameInput.MBDRAGY;
	}


	private void ControlZoom() {
		// Perform Zoom
		float scrwid = cam.zoom*SCRWIDTH;
		float scrhei = cam.zoom*SCRHEIGHT;
		if ((scrwid > maxzoom) & (scrhei > maxzoom)) {
			cam.zoom = Math.max(maxzoom/(SCRWIDTH), maxzoom/SCRHEIGHT);
			return;
		}

		// Calculate new Zoom
		if (GameInput.SCROLL == 1) {
			if (cam.zoom > 1000.0f) nullvarA = 20.0f;
			else if (cam.zoom > 100.0f) nullvarA = 5.0f;
			else if (cam.zoom > 50.0f) nullvarA = 2.0f;
			else if (cam.zoom > 10.0f) nullvarA = 0.5f;
			else if (cam.zoom > 10.0f) nullvarA = 0.5f;
			else if (cam.zoom > 5.0f) nullvarA = 0.2f;
			else if (cam.zoom > 2.0f) nullvarA = 0.05f;
			else if (cam.zoom > 0.5f) nullvarA = 0.02f;
			else if (cam.zoom > 0.1f) nullvarA = 0.01f;
			else if (cam.zoom <= 0.1f) nullvarA = 0.01f;
			GameInput.SCROLL = 0;
		} else if (GameInput.SCROLL == -1) {
			if (cam.zoom > 1000.0f) nullvarA = -20.0f;
			else if (cam.zoom > 100.0f) nullvarA = -5.0f;
			else if (cam.zoom > 50.0f) nullvarA = -2.0f;
			else if (cam.zoom > 10.0f) nullvarA = -0.5f;
			else if (cam.zoom > 10.0f) nullvarA = -0.5f;
			else if (cam.zoom > 5.0f) nullvarA = -0.2f;
			else if (cam.zoom > 2.0f) nullvarA = -0.05f;
			else if (cam.zoom > 0.5f) nullvarA = -0.02f;
			else if (cam.zoom > 0.1f) nullvarA = -0.01f;
			else if (cam.zoom <= 0.01f) nullvarA = 0.0f;
			else if (cam.zoom <= 0.1f) nullvarA = -0.01f;
			GameInput.SCROLL = 0;
		}
		
		// Translate to zoom in on cursor position
		posx = - nullvarA*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*SCRWIDTH);
		posy = nullvarA*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*SCRHEIGHT);
		if ((cam.position.x>(maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH) & (cam.position.x<(maxzoom-0.5f*cam.zoom*SCRWIDTH))) {
			cam.translate(posx,0,0);
		}
		if ((cam.position.y>(maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT) & (cam.position.y<(maxzoom-0.5f*cam.zoom*SCRHEIGHT))) {
			cam.translate(0,posy,0);
		}
		// Apply new zoom
		cam.zoom += nullvarA;

		// Perform corrections if the zoom takes us out of bounds
		if ((cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH) & (cam.position.x > (maxzoom-0.5f*cam.zoom*SCRWIDTH))) {
		} else if (cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH) {
			cam.translate((maxsize-maxzoom)+0.5f*cam.zoom*SCRWIDTH-cam.position.x,0,0);
		} else if (cam.position.x > (maxzoom-0.5f*cam.zoom*SCRWIDTH)) {
			cam.translate((maxzoom-0.5f*cam.zoom*SCRWIDTH)-cam.position.x,0,0);
		}
		if ((cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT) & (cam.position.y > (maxzoom-0.5f*cam.zoom*SCRHEIGHT))) {
		} else if (cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT) {
			cam.translate(0,(maxsize-maxzoom)+0.5f*cam.zoom*SCRHEIGHT-cam.position.y,0);
		} else if (cam.position.y > (maxzoom-0.5f*cam.zoom*SCRHEIGHT)) {
			cam.translate(0,(maxzoom-0.5f*cam.zoom*SCRHEIGHT)-cam.position.y,0);
		}
		//System.out.println("---------");
	}
	
	public int GetListIndex(String s, String[] arr) {
		for (int i = 0; i<arr.length; i++) {
			if (s.equals(arr[i])) return i;
		}
		return 0;
	}

}