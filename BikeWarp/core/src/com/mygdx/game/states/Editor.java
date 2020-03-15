/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.states;

import static com.mygdx.game.handlers.B2DVars.PPM;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragScrollListener;
import com.mygdx.game.BikeGame;
import com.mygdx.game.handlers.B2DVars;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.handlers.DecorVars;
import com.mygdx.game.handlers.GameContactListener;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameInputProcessor;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.LevelVars;
import com.mygdx.game.handlers.ObjectVars;
import com.mygdx.game.utilities.BayazitDecomposer;
import com.mygdx.game.utilities.EditorIO;
import com.mygdx.game.utilities.PolygonOperations;

/**
 *
 * @author rcooke
 */
public class Editor extends GameState {

	private InputMultiplexer inputMultiplexer;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private String[] nullList = new String[0];
	private String[] itemsXYonly = {"Move X and Y", "Move X only", "Move Y only"};
	private String[] itemsPRC = {"Polygon", "Rectangle", "Circle"};
	private String[] itemsPRCP = {"Polygon", "Rectangle", "Circle", "Set Path"};
	private String[] itemsADM = {"Add", "Delete", "Move"};
	private String[] itemsADMRSFv = {"Add", "Delete", "Move", "Rotate", "Scale", "Flip x", "Flip y", "Add Vertex", "Delete Vertex", "Move Vertex"};
	private String[] itemsADMR = {"Add", "Delete", "Move", "Rotate"};
	private String[] objectList = {"Ball & Chain", "Boulder", "Bridge", "Crate", "Diamond", "Door (blue)", "Door (green)", "Door (red)", "Gate Switch", "Gravity", "Jewel", "Key (blue)", "Key (green)", "Key (red)", "Log", "Nitrous", "Pendulum", "Spike", "Transport", "Start", "Finish"};
	private String[] decorateList = {"Grass",
			"Sign (10)", "Sign (20)", "Sign (30)", "Sign (40)", "Sign (50)", "Sign (60)", "Sign (80)", "Sign (100)", "Sign (Bumps Ahead)",
			"Sign (Do Not Enter)", "Sign (Exclamation)", "Sign (Motorbikes)", "Sign (No Motorbikes)", "Sign (Ramp Ahead)", "Sign (Reduce Speed)",
			"Sign (Stop)"};
    private String[] levelPropList = {"Collect Jewels", "Gravity", "Ground Texture", "Sky Texture"};
	private String[] groundTextureList = {"Cracked Mud", "Gravel", "Ice", "Mars"};
	private String[] skyTextureList = {"Blue Sky", "Evening", "Islands", "Mars", "Sunrise"};
	private String[] gravityList = {"Earth", "Moon", "Mars"};
	private String[] loadList = {"Load Level", "New Level"};
	private String[] jewelNumber;
	private String saveFName;
	private boolean enteringFilename, changesMade;

	private int totalNumMsgs = 20;
	private BitmapFont warnFont, signFont;
	private String[] warnMessage;
	private float[] warnElapse;
	private int[] warnType;
	private float warnTime = 5.0f, warnHeight, toolbarWidth;
	private int warnNumber;

	// Define the object list
	private ArrayList<float[]> allObjects = new ArrayList<float[]>();
	private ArrayList<float[]> allObjectArrows = new ArrayList<float[]>();
	private ArrayList<Integer> allObjectTypes = new ArrayList<Integer>();
	private ArrayList<float[]> allObjectCoords= new ArrayList<float[]>();

	// Define the decorations list
	private ArrayList<float[]> allDecors = new ArrayList<float[]>();
	private ArrayList<Integer> allDecorTypes = new ArrayList<Integer>();
	private ArrayList<Integer> allDecorPolys = new ArrayList<Integer>();

	// Define properties of the toolbar
	private Stage stage;
	private Skin skin;
	private boolean hideToolbar = false;
	private boolean setCursor = false;
	private boolean engageDelete = false;
	private float opacity = 1.0f;

	private static final float maxzoom = 1200.0f/B2DVars.EPPM;  // The zoom can be extended to this width
	private static final float maxsize = 1000.0f/B2DVars.EPPM;  // The boundary where polygons can be drawn
	private float posx, posy;
	private float cursposx = 0, cursposy = 0;
	private float scrwidth, scrheight, scrscale, SCTOSCRW;
	private float startX, startY, endX, endY;
	private float nullvarA, nullvarB, nullvarC, nullvarD;

	// Setup the arrays to store new polygons
	private ArrayList<float[]> allPolygons = new ArrayList<float[]>();
	private ArrayList<Integer> allPolygonTypes = new ArrayList<Integer>();
	private ArrayList<float[]> allPolygonPaths = new ArrayList<float[]>();
	private String jsonLevelString;
	private float[] newPoly = null;
	private float[] updatePoly = null;
	private float[] ghostPoly = null;
	private float[] updatePath = null;
	private float[] updatePathVertex = null;
	private float[] newCoord = new float[2];
	private static final float boundaryX = 1000.0f/B2DVars.EPPM;
	private static final float boundaryY = 1000.0f/B2DVars.EPPM;
	private boolean drawingPoly = false;  // Is a polygon currently being drawn
	private ArrayList<float[]> polyDraw;  // Store the vertices of the new polygon in an ArrayList
	private float[] shapeDraw = null;  // Store the vertices of the new shape
	private float tempx, tempy; // new vertex to be tested
	private static final float polyEndThreshold = 0.01f;
	private ArrayList<Integer> groupPolySelect;
	private ArrayList<float[]> updateGroupPoly;
	private int polySelect = -1, vertSelect = -1, segmSelect = -1;
	private int polyHover = -1, vertHover = -1, segmHover = -1;
	private int objectSelect = -1, decorSelect = -1, finishObjNumber;
	private int pLevelIndex = 0, pStaticIndex = 0, pKinematicIndex = 0, pFallingIndex = 0, pObjectIndex = 0;
	private int numJewels = 0; // Number of jewels currently inserted
	private int ctype;  // The color of the door/key
	private int tentry; // The transport entry (which entry point is currently being investigated)
	private float xcen, ycen;
	private float[] transPoly = new float[8];
	private float[] startDirPoly = new float[ObjectVars.objectArrow.length];

	// Setup some toolbar buttons
	//private TextButton buttonLoad;
	private SelectBox selectLoadLevel;
	private TextButton buttonSave;
	private TextButton buttonExit;
	private TextButton buttonExecute;
	private TextButton buttonPan;
	private TextButton buttonLevelProp;
	private TextButton buttonAddStatic;
	private TextButton buttonAddKinetic;
	private TextButton buttonAddFalling;
	private TextButton buttonCopyPaste;
	private TextButton buttonAddObject;
	private TextButton buttonDecorate;
	private TextField textInputSave;
	private List listParent;
	private List listChild;
	private SplitPane splitPaneParent;
	private SplitPane splitPaneChild;
	private ScrollPane scrollPaneTBar;
	
	// Use an integer to identify which mode is currently active
	/* mode:
	 * = 1  -->  pan/zoom
	 * = 2  -->  select polygon
	 * = 3  -->  create static polygon
	 * = 4  -->  create kinetic polygon
	 * = 5  -->  add object
	 * = 6  -->  add decoration
	 */
	private int mode = 1;
	private String modeParent = "";
	private String modeChild = "";
	
	//private Matrix4 viewMatrix = new Matrix4();

	public Editor(GameStateManager gsm) {
        super(gsm);
        create();
    }

    public void create() {
    	//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
    	// First step is to set the hudCam for rendering messages
        //float SCTOSCRH = ((float) Gdx.graphics.getWidth()*Gdx.graphics.getDesktopDisplayMode().height)/((float) Gdx.graphics.getDesktopDisplayMode().width);
        SCTOSCRW = ((float) Gdx.graphics.getHeight()*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
        hudCam.setToOrtho(false, SCTOSCRW, Gdx.graphics.getHeight());
        hudCam.position.set(SCTOSCRW/(BikeGame.SCALE*2),BikeGame.V_HEIGHT/2,0);
        //hudCam.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
        hudCam.zoom = 1.0f/(BikeGame.SCALE);
        hudCam.update();
    	// Create the stage for the toolbar
		stage = new Stage();
		scrscale = (((float)BikeGame.V_HEIGHT)/(float)BikeGame.V_WIDTH)* ((float) Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
		//Gdx.input.setInputProcessor(stage);
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(Gdx.input.getInputProcessor());
		Gdx.input.setInputProcessor(inputMultiplexer);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		LevelVars.setDefaults(); // Set the default properties for this level

		// Generate the initial variables of a new level
		ResetLevelDefaults();

		warnFont = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
		signFont = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
		warnMessage = new String[totalNumMsgs];
		warnElapse = new float[totalNumMsgs];
		warnType = new int[totalNumMsgs];
		warnFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		warnFont.setColor(1, 0.5f, 0, 1);
		warnFont.setScale(0.5f);
		warnHeight = warnFont.getBounds("WARNING").height;
		signFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		signFont.setColor(1, 0, 0, 1);
		signFont.setScale(1.5f);
		
		//buttonLoad = new TextButton("Load", skin);
		selectLoadLevel = new SelectBox(skin);
		buttonSave = new TextButton("Save", skin);
		textInputSave = new TextField("", skin);
		buttonExit = new TextButton("Main Menu", skin);
		buttonExecute = new TextButton("Execute", skin);
		buttonPan = new TextButton("Pan", skin);
		buttonLevelProp = new TextButton("Level Properties", skin);
		buttonCopyPaste = new TextButton("Copy and Paste", skin);
		buttonAddStatic = new TextButton("Static Platform", skin);
		buttonAddKinetic = new TextButton("Moving Platform", skin);
		buttonAddFalling = new TextButton("Falling Platform", skin);
		buttonAddObject = new TextButton("Object", skin);
		buttonDecorate = new TextButton("Decorate", skin);
		buttonPan.setChecked(true);

		selectLoadLevel.setItems(EditorIO.LoadLevelNames(loadList));
		selectLoadLevel.setSelectedIndex(0);
		selectLoadLevel.setMaxListCount(0);
		textInputSave.setMaxLength(11);

		listParent = new List(skin);
		listParent.setItems(nullList);
		listParent.getSelection().setMultiple(false);
		ScrollPane scrollPaneParent = new ScrollPane(listParent, skin);
		scrollPaneParent.setFlickScroll(false);
		scrollPaneParent.setSmoothScrolling(true);
		scrollPaneParent.setScrollBarPositions(true, true);
		scrollPaneParent.setHeight(scrollPaneParent.getPrefHeight());
		//splitPaneParent = new SplitPane(scrollPaneP, scrollPaneP, true, skin, "default-horizontal");
		//splitPaneParent.setHeight(100.0f);
		
		listChild = new List(skin);
		listChild.setItems(nullList);
		listChild.getSelection().setMultiple(false);
		ScrollPane scrollPaneChild = new ScrollPane(listChild, skin);
		scrollPaneChild.setFlickScroll(false);
		scrollPaneChild.setSmoothScrolling(true);
		scrollPaneChild.setScrollBarPositions(true, true);
		scrollPaneChild.setHeight(scrollPaneChild.getPrefHeight());
		//splitPaneChild = new SplitPane(scrollPaneC, scrollPaneC, true, skin, "default-horizontal");
		//splitPaneParent.setHeight(100.0f);

		Window window = new Window("Toolbar", skin);
		window.align(Align.top | Align.center);
		window.setPosition(0, 0);
		window.setPosition(0, 0);
		window.defaults().spaceBottom(3);
		window.row().fill().expandX().colspan(2);
		//window.add(buttonLoad);
		window.add(buttonExit);
		window.row().fill().expandX().colspan(2);
		window.add(selectLoadLevel);
		window.row().fill().colspan(1);
		window.add(buttonSave,textInputSave);
		window.row().fill().expandX().colspan(2);
		window.add(buttonExecute);
		window.row().fill().expandX().colspan(2);
		window.add(buttonPan);
		window.row().fill().expandX().colspan(2);
		window.add(buttonLevelProp);
		window.row().fill().expandX().colspan(2);
		window.add(buttonCopyPaste);
		window.row().fill().expandX().colspan(2);
		window.add(buttonAddStatic);
		window.row().fill().expandX().colspan(2);
		window.add(buttonAddKinetic);
		window.row().fill().expandX().colspan(2);
		window.add(buttonAddFalling);
		window.row().fill().expandX().colspan(2);
		window.add(buttonAddObject);
		window.row().fill().expandX().colspan(2);
		window.add(buttonDecorate);
		window.row().fill().expandX().colspan(2);
		window.add(scrollPaneParent).minHeight(140).maxHeight(220).fill().expand();
		window.row().fill().expandX().colspan(2);
		window.add(scrollPaneChild).minHeight(70).maxHeight(220).fill().expand();
		window.row().fill().expandX().colspan(2);
		window.pack();
		scrollPaneTBar = new ScrollPane(window, skin);
		scrollPaneTBar.setFlickScroll(false);
		scrollPaneTBar.setSmoothScrolling(true);
		scrollPaneTBar.setScrollBarPositions(true, true);
		//scrollPaneTBar.setScrollingDisabled(true, false);
		scrollPaneTBar.setHeight(BikeGame.V_HEIGHT*BikeGame.SCALE);
		scrollPaneTBar.setWidth((window.getPrefWidth()+2));
		toolbarWidth = scrscale*(window.getPrefWidth()+2)/BikeGame.SCALE;
		stage.addActor(scrollPaneTBar);
		// Hover over the toolbar
		scrollPaneTBar.addListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
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
								warnMessage[warnNumber] = "Changes made since last save!";
								warnElapse[warnNumber] = 0.0f;
								warnType[warnNumber] = 1;
								warnNumber += 1;
								warnMessage[warnNumber] = "Selecting 'Load Level' again may overwrite these changes";
								warnElapse[warnNumber] = 0.0f;
								warnType[warnNumber] = 1;
								warnNumber += 1;
								changesMade = false;
								selectLoadLevel.setSelectedIndex(0);
							} else {
								if (selectLoadLevel.getSelectedIndex() == 1) {
									// Refresh the canvas - A new level is being created
									ResetLevelDefaults();
									selectLoadLevel.setSelectedIndex(0);
									textInputSave.setText("");
								} else {
									ArrayList<Object> loadedArray = EditorIO.loadLevel((String) selectLoadLevel.getSelected()+".lvl");
									allPolygons = (ArrayList<float[]>) loadedArray.get(0);
									allPolygonTypes = (ArrayList<Integer>) loadedArray.get(1);
									allPolygonPaths = (ArrayList<float[]>) loadedArray.get(2);
									allObjects = (ArrayList<float[]>) loadedArray.get(3);
									allObjectArrows = (ArrayList<float[]>) loadedArray.get(4);
									allObjectCoords = (ArrayList<float[]>) loadedArray.get(5);
									allObjectTypes = (ArrayList<Integer>) loadedArray.get(6);
									allDecors = (ArrayList<float[]>) loadedArray.get(7);
									allDecorTypes = (ArrayList<Integer>) loadedArray.get(8);
									allDecorPolys = (ArrayList<Integer>) loadedArray.get(9);
									String[] setLVs = (String[]) loadedArray.get(10);
									for (int i=0; i<setLVs.length; i++) LevelVars.set(i, setLVs[i]);
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

		buttonSave.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						UncheckButtons(false);
						listParent.setItems(nullList);
						SetChildList();
						mode = -999;
						// Check for vertices that are too close
						float[] chkVertices = PolygonOperations.CheckVertexSizes(allPolygons, allPolygonTypes, allDecors, allDecorTypes);
						// Check for intersecting segments
						float[] chkIntsct = PolygonOperations.CheckIntersections(allPolygons, allPolygonTypes, allDecors, allDecorTypes);
						if (chkVertices!=null) {
							// Go to the intersection
							MoveCameraTo(chkVertices[0],chkVertices[1],true);
							// Print a warning message
							warnMessage[warnNumber] = "File not saved -- Two vertices are too close together (maybe a duplicate?)";
							warnElapse[warnNumber] = 0.0f;
							warnType[warnNumber] = 2;
							warnNumber += 1;
							warnMessage[warnNumber] = "Fix the polygon first, and then save the level";
							warnElapse[warnNumber] = 0.0f;
							warnType[warnNumber] = 1;
							warnNumber += 1;
						} else if (chkIntsct!=null) {
							// Go to the intersection
							MoveCameraTo(chkIntsct[0],chkIntsct[1],true);
							// Print a warning message
							warnMessage[warnNumber] = "File not saved -- There was an intersection between these segments";
							warnElapse[warnNumber] = 0.0f;
							warnType[warnNumber] = 2;
							warnNumber += 1;
							warnMessage[warnNumber] = "Fix the polygon first, and then save the level";
							warnElapse[warnNumber] = 0.0f;
							warnType[warnNumber] = 1;
							warnNumber += 1;
						} else {
							// No intersections were found, so the level can be saved without errors
							try {
								String temptext = textInputSave.getText();
								if ((temptext == null) | (temptext.equals(""))) {
									warnMessage[warnNumber] = "File not saved -- You must enter a filename";
									warnElapse[warnNumber] = 0.0f;
									warnType[warnNumber] = 2;
									warnNumber += 1;
								} else {
									saveFName = temptext;
									String isSaved = EditorIO.saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, saveFName+".lvl");
									if (!isSaved.equals("")) {
										warnMessage[warnNumber] = isSaved;
										warnElapse[warnNumber] = 0.0f;
										warnType[warnNumber] = 2;
										warnNumber += 1;
									} else {
										warnMessage[warnNumber] = "Level saved: "+saveFName+".lvl";
										warnElapse[warnNumber] = 0.0f;
										warnType[warnNumber] = 0;
										warnNumber += 1;
										changesMade = false;
									}
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
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
							warnMessage[warnNumber] = "Changes made since last save!";
							warnElapse[warnNumber] = 0.0f;
							warnType[warnNumber] = 1;
							warnNumber += 1;
							warnMessage[warnNumber] = "Selecting 'Main Menu' again will exit without saving";
							warnElapse[warnNumber] = 0.0f;
							warnType[warnNumber] = 1;
							warnNumber += 1;
							changesMade = false;
						}
						UncheckButtons(false);
					}
				}
			}
		});

		buttonExecute.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						UncheckButtons(false);
						try {
							jsonLevelString = EditorIO.JSONserialize(allPolygons,allPolygonTypes,allPolygonPaths,allObjects,allObjectArrows,allObjectCoords,allObjectTypes,allDecors,allDecorTypes,allDecorPolys);
							if (jsonLevelString.startsWith("CU")) {
								warnMessage[warnNumber] = "Unable to play level!";
								warnElapse[warnNumber] = 0.0f;
								warnType[warnNumber] = 2;
								warnNumber += 1;
								try {
									int gotoPoly = Integer.parseInt(jsonLevelString.split(" ")[1]);
									float xcen=0.0f, ycen=0.0f;
									if (jsonLevelString.split(" ")[2].equals("P")) {
										for (int i=0; i<allPolygons.get(gotoPoly).length/2; i++) {
											xcen += allPolygons.get(gotoPoly)[2*i];
											ycen += allPolygons.get(gotoPoly)[2*i+1];
										}
										xcen /= (allPolygons.get(gotoPoly).length/2);
										ycen /= (allPolygons.get(gotoPoly).length/2);
										MoveCameraTo(xcen,ycen,true);
										warnMessage[warnNumber] = "Two vertices in this polygon are too close together";
										warnElapse[warnNumber] = 0.0f;
										warnType[warnNumber] = 1;
										warnNumber += 1;
									} else if (jsonLevelString.split(" ")[2].equals("D")) {
										for (int i=0; i<allDecors.get(gotoPoly).length/2; i++) {
											xcen += allDecors.get(gotoPoly)[2*i];
											ycen += allDecors.get(gotoPoly)[2*i+1];
										}
										xcen = xcen/(float) (allDecors.get(gotoPoly).length/2);
										ycen = ycen/(float) (allDecors.get(gotoPoly).length/2);										
										MoveCameraTo(xcen,ycen,true);
										warnMessage[warnNumber] = "Two vertices in the grass are too close together";
										warnElapse[warnNumber] = 0.0f;
										warnType[warnNumber] = 1;
										warnNumber += 1;
									}
								} catch (Exception e) {}
							} else {
								enteringFilename = false;
								stage.setKeyboardFocus(null);
								gsm.setState(GameStateManager.PLAY, true, jsonLevelString, -1, 0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
				    	// When we come back from the level, make sure we reset the hudCam (used for messages)
				        SCTOSCRW = ((float) Gdx.graphics.getHeight()*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
				        hudCam.setToOrtho(false, SCTOSCRW, Gdx.graphics.getHeight());
				        hudCam.position.set(SCTOSCRW/(BikeGame.SCALE*2),BikeGame.V_HEIGHT/2,0);
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
		});

		buttonPan.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 1;
						UncheckButtons(false);
						buttonPan.setChecked(true);
						listParent.setItems(nullList);
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

		buttonAddStatic.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 3;
						UncheckButtons(false);
						listParent.setItems(itemsPRC);
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

		buttonCopyPaste.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (!hideToolbar) {
					if (!drawingPoly) {
						mode = 8;
						UncheckButtons(false);
						listParent.setItems("Platform","Object");
						SetChildList();
						ResetHoverSelect();
						buttonCopyPaste.setChecked(true);
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
					if (!drawingPoly) {
						SetChildList();
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
					if (listParent.getItems().size != 0) {
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
					}
					if (!drawingPoly) {
						objectSelect = -1;
						decorSelect = -1;
						if (mode!=4) polySelect = -1;
						vertSelect = -1;
						segmSelect = -1;
						polyHover = -1;
						vertHover = -1;
						segmHover = -1;
						updatePathVertex = null;
						tentry = 0;
					}
				}
			}
		});
    }

    public void ResetLevelDefaults() {
		allObjects = new ArrayList<float[]>();
		allObjectArrows = new ArrayList<float[]>();
		allObjectTypes = new ArrayList<Integer>();
		allObjectCoords= new ArrayList<float[]>();
    	allPolygons = new ArrayList<float[]>();
    	allPolygonTypes = new ArrayList<Integer>();
    	allPolygonPaths = new ArrayList<float[]>();
    	allDecors = new ArrayList<float[]>();
    	allDecorTypes = new ArrayList<Integer>();
    	allDecorPolys = new ArrayList<Integer>();
    	newPoly = null;
    	updatePoly = null;
    	updateGroupPoly = null;
    	updatePath = null;
    	updatePathVertex = null;
    	newCoord = new float[2];
    	drawingPoly = false;  // Is a polygon currently being drawn
    	shapeDraw = null;  // Store the vertices of the new shape
    	groupPolySelect = new ArrayList<Integer>();
    	polySelect = -1;
    	vertSelect = -1;
    	segmSelect = -1;
    	polyHover = -1;
    	vertHover = -1;
    	segmHover = -1;
    	objectSelect = -1;
    	decorSelect = -1;
    	pLevelIndex = 0;
    	pStaticIndex = 0;
    	pKinematicIndex = 0;
    	pObjectIndex = 0;
    	numJewels = 1; // Number of jewels currently inserted (1=Diamond jewel)
    	transPoly = new float[8];
    	startDirPoly = new float[ObjectVars.objectArrow.length];

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
		// Set the starting position of the camera
		// Perform Zoom
		cam.zoom = 0.1f/B2DVars.EPPM;
		//cam.lookAt(1000.0f/B2DVars.EPPM, 1000.0f/B2DVars.EPPM, 0.0f);
    }

    public void RestoreLevelDefaults() {
    	// Set the level name in the save textbox
		saveFName = FilenameUtils.getBaseName((String) selectLoadLevel.getSelected());
		textInputSave.setText(saveFName);
		// Determine the number of jewels in the level
		numJewels = 1;
		for (int i=finishObjNumber; i<allObjectTypes.size(); i++) {
			if (allObjectTypes.get(i) == ObjectVars.Jewel) numJewels += 1;			
		}
		//
    }

    public void handleInput() {
		if (GameInput.isPressed(GameInput.KEY_T)) hideToolbar = !hideToolbar;
		if (GameInput.isPressed(GameInput.KEY_C)) setCursor = true;
		if ((GameInput.isPressed(GameInput.KEY_D)) & (engageDelete)) {
			if ((mode==4) & (modeParent.equals("Set Path"))) {
				if (vertSelect != -1) {
					if (allPolygonPaths.get(polySelect).length >= 12) {
						DeleteVertexPath(polySelect, vertSelect);
						vertSelect = -1;
						vertHover = -1;
						polyHover = -1;
						segmHover = -1;
					} else if (allPolygonPaths.get(polySelect).length == 10) {
						float[] pathArr = new float[6];
						for (int i=0; i<6; i++) pathArr[i] = allPolygonPaths.get(polySelect)[i];
						allPolygonPaths.set(polySelect, pathArr.clone());
						vertSelect = -1;
						vertHover = -1;
						polyHover = -1;
						segmHover = -1;
					}
				}
			} else {
				if (polySelect != -1) {
					if (vertSelect != -1) {
						if (allPolygons.get(polySelect).length <= 6) DeletePolygon(polySelect);
						else DeleteVertex(polySelect, vertSelect);
						vertSelect = -1;
					} else {
						DeletePolygon(polySelect);
					}
					engageDelete = false;
					polySelect = -1;
				} else if (objectSelect != -1) {
					DeleteObject(objectSelect);
					engageDelete = false;
					objectSelect = -1;
				} else if (decorSelect != -1) {
					DeleteDecor(decorSelect);
					engageDelete = false;
					decorSelect = -1;
				} else if ((polySelect == -1) | (objectSelect == -1) | (decorSelect == -1)) {
					// This never does anything...
					//if (vertSelect != -1) System.out.println("Press the 'd' key to delete the selected vertex");
					//else System.out.println("Select Polygon and press the 'd' key to delete the selected polygon");
				}
				vertHover = -1;
				polyHover = -1;
				segmHover = -1;
			}
		}
		if (setCursor == true) {
			if (GameInput.MBJUSTPRESSED == true) {
				cursposx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				//cursposx = cam.position.x + cam.zoom*(GameInput.MBUPX/(BikeGame.SCALE) - 0.5f*BikeGame.V_WIDTH);
				cursposy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
        	if (GameInput.MBDRAG==true) {
        		ControlPan();
        	}
        } else if (mode==2) {
    		try {
    			ControlMode2();
    		} catch (Exception e) {}
        } else if (mode==3) {
        	try {
        		ControlMode3(false);
        	} catch (Exception e) {}
        } else if (mode==4) {
        	try {
        		ControlMode4();
        	} catch (Exception e) {}
        } else if (mode==5) {
        	try {
        		ControlMode5();
        	} catch (Exception e) {}
        } else if (mode==6) {
        	try {
        		ControlMode6();
        	} catch (Exception e) {}
        } else if (mode==7) {
        	try {
        		ControlMode3(true); // Use Control Mode 3, but set the platform to be a falling platform (i.e. set the argument to true)
        	} catch (Exception e) {}
        } else if (mode==8) {
        	ControlMode8();
//        	try {
//        		ControlMode8();
//        	} catch (Exception e) {}        	
        }
        GameInput.MBJUSTPRESSED = false;
        GameInput.MBJUSTDRAGGED = false;
//        if (GameInput.isPressed(GameInput.BUTTON2)) {
//        	cam.translate(-10,-5,0);
//        	System.out.println("BUTTON2 pressed");
//        }
    
    }

	public void update(float dt) {
    	handleInput();
    	cam.update();
    	stage.setScrollFocus(null); // Forces scrolling to be used for zooming only
    	updateWarnings(dt);
    	updateToolbar();
    }

	private void updateToolbar() {
		if ((drawingPoly) | (hideToolbar)) {
			buttonExit.setDisabled(true);
			selectLoadLevel.setDisabled(true);
			buttonSave.setDisabled(true);
			textInputSave.setDisabled(true);
			buttonExecute.setDisabled(true);
			buttonPan.setDisabled(true);
			buttonLevelProp.setDisabled(true);
			buttonAddStatic.setDisabled(true);
			buttonAddKinetic.setDisabled(true);
			buttonAddFalling.setDisabled(true);
			buttonAddObject.setDisabled(true);
			buttonDecorate.setDisabled(true);
			scrollPaneTBar.setVisible(false);
		} else {
			buttonExit.setDisabled(false);
			selectLoadLevel.setDisabled(false);
			buttonSave.setDisabled(false);
			textInputSave.setDisabled(false);
			buttonExecute.setDisabled(false);
			buttonPan.setDisabled(false);
			buttonLevelProp.setDisabled(false);
			buttonAddStatic.setDisabled(false);
			buttonAddKinetic.setDisabled(false);
			buttonAddFalling.setDisabled(false);
			buttonAddObject.setDisabled(false);
			buttonDecorate.setDisabled(false);
			scrollPaneTBar.setVisible(true);
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
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeType.Line);
        // Draw the boundary region
        Gdx.gl20.glLineWidth(5);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(0, 0, boundaryX, boundaryY);
        shapeRenderer.setColor(0, 1, 0, 0.5f);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeType.Line);
        Gdx.gl20.glLineWidth(2);
        // Draw the polygons (not including the current polygon)
        float[] extraPoly;
        if (allPolygons.size() != 0) {
	        for (int i = 0; i<allPolygons.size(); i++) {
	        	if (allPolygonTypes.get(i) <= 1) {
	        		// Static Polygons
	        		if (polySelect == i) shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1);
	        		else shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.5f);
	        		// Draw the static polygon
		        	if (allPolygonTypes.get(i) == 0) {
		        		shapeRenderer.polygon(allPolygons.get(i));
		        	} else if (allPolygonTypes.get(i) == 1) {
		        		shapeRenderer.circle(allPolygons.get(i)[0], allPolygons.get(i)[1], allPolygons.get(i)[2]);
		        	}
	        	} else if (allPolygonTypes.get(i) <= 3) {
	        		// Kinematic Polygons
	        		if (polySelect == i) opacity = 1.0f;
	        		else opacity = 0.5f;
	        		shapeRenderer.setColor(0.1f, 0.5f, 1, opacity);
	        		int sz = allPolygonPaths.get(i).length;
	        		// Draw the starting point of the kinematic polygon
		        	if (allPolygonTypes.get(i) == 2) {
		        		shapeRenderer.polygon(allPolygons.get(i));
						if (sz > 6) {
							extraPoly = allPolygons.get(i).clone();
							if ((ghostPoly != null)&(polySelect==i)) {
								for (int j=0; j < extraPoly.length/2; j++) {
									extraPoly[2*j] += (ghostPoly[0]-allPolygonPaths.get(i)[4]);
									extraPoly[2*j+1] += (ghostPoly[1]-allPolygonPaths.get(i)[5]);
								}
							} else {
								for (int j=0; j < extraPoly.length/2; j++) {
									extraPoly[2*j] += (allPolygonPaths.get(i)[sz-2]-allPolygonPaths.get(i)[4]);
									extraPoly[2*j+1] += (allPolygonPaths.get(i)[sz-1]-allPolygonPaths.get(i)[5]);
								}
							}
							shapeRenderer.setColor(0.2f, 0.4f, 0.9f, 0.5f*opacity);
							shapeRenderer.polygon(extraPoly);
						}
		        	} else if (allPolygonTypes.get(i) == 3) {
		        		shapeRenderer.circle(allPolygons.get(i)[0], allPolygons.get(i)[1], allPolygons.get(i)[2]);
						if (sz > 6) {
							extraPoly = allPolygons.get(i).clone();
							if ((ghostPoly != null)&(polySelect==i)) {
								extraPoly[0] += (ghostPoly[0]-allPolygonPaths.get(i)[4]);
								extraPoly[1] += (ghostPoly[1]-allPolygonPaths.get(i)[5]);
							} else {
								extraPoly[0] += (allPolygonPaths.get(i)[sz-2]-allPolygonPaths.get(i)[4]);
								extraPoly[1] += (allPolygonPaths.get(i)[sz-1]-allPolygonPaths.get(i)[5]);
							}
							shapeRenderer.setColor(0.2f, 0.4f, 0.9f, 0.5f*opacity);
							shapeRenderer.circle(extraPoly[0],extraPoly[1],extraPoly[2]);
						}
		        	}
		        	// Draw the centre of the body
		        	// omega,velocity,omegaDir,velocityDir,xcen,ycen
		        	shapeRenderer.setColor(1, 0, 0, opacity);
		        	shapeRenderer.circle(allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5], 5);
		        	shapeRenderer.line(allPolygonPaths.get(i)[4]-5, allPolygonPaths.get(i)[5], allPolygonPaths.get(i)[4]+5, allPolygonPaths.get(i)[5]);
		        	shapeRenderer.line(allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5]-5, allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5]+5);
	        		// Draw the path (and the path being currently updated)
					if (sz > 6) {
						for (int j=0; j < (sz-6)/2 - 1; j++) shapeRenderer.line(allPolygonPaths.get(i)[6+2*j], allPolygonPaths.get(i)[6+2*j+1], allPolygonPaths.get(i)[6+2*j+2], allPolygonPaths.get(i)[6+2*j+3]);
						if ((updatePathVertex != null) & (polySelect==i)) {
							shapeRenderer.setColor(1, 1, 0.1f, opacity);
							nullvarC = (float) Math.sqrt((updatePathVertex[0]-allPolygonPaths.get(i)[6])*(updatePathVertex[0]-allPolygonPaths.get(i)[6]) + (updatePathVertex[1]-allPolygonPaths.get(i)[7])*(updatePathVertex[1]-allPolygonPaths.get(i)[7]));
							nullvarD = (float) Math.sqrt((updatePathVertex[0]-allPolygonPaths.get(i)[sz-2])*(updatePathVertex[0]-allPolygonPaths.get(i)[sz-2]) + (updatePathVertex[1]-allPolygonPaths.get(i)[sz-1])*(updatePathVertex[1]-allPolygonPaths.get(i)[sz-1]));
							if (nullvarC < nullvarD) shapeRenderer.line(allPolygonPaths.get(i)[6], allPolygonPaths.get(i)[7], updatePathVertex[0], updatePathVertex[1]);
							else shapeRenderer.line(allPolygonPaths.get(i)[sz-2], allPolygonPaths.get(i)[sz-1], updatePathVertex[0], updatePathVertex[1]);
							shapeRenderer.setColor(1, 0, 0, opacity);
						}
					}
					if ((updatePathVertex != null) & (polySelect==i)) {
						shapeRenderer.setColor(1, 1, 0.1f, opacity);
						if ((updatePathVertex.length == 2) & (sz==6)) {
							// Extending path for the first time
							shapeRenderer.line(allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5], updatePathVertex[0], updatePathVertex[1]);
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
								bestval = (float) Math.sqrt((allPolygonPaths.get(i)[4]-allPolygonPaths.get(i)[6+2*j])*(allPolygonPaths.get(i)[4]-allPolygonPaths.get(i)[6+2*j]) + (allPolygonPaths.get(i)[5]-allPolygonPaths.get(i)[6+2*j+1])*(allPolygonPaths.get(i)[5]-allPolygonPaths.get(i)[6+2*j+1]));
								spine = j;
							} else {
								tempval = (float) Math.sqrt((allPolygonPaths.get(i)[4]-allPolygonPaths.get(i)[6+2*j])*(allPolygonPaths.get(i)[4]-allPolygonPaths.get(i)[6+2*j]) + (allPolygonPaths.get(i)[5]-allPolygonPaths.get(i)[6+2*j+1])*(allPolygonPaths.get(i)[5]-allPolygonPaths.get(i)[6+2*j+1]));
								if (tempval < bestval) {
									bestval = (float) Math.sqrt((allPolygonPaths.get(i)[4]-allPolygonPaths.get(i)[6+2*j])*(allPolygonPaths.get(i)[4]-allPolygonPaths.get(i)[6+2*j]) + (allPolygonPaths.get(i)[5]-allPolygonPaths.get(i)[6+2*j+1])*(allPolygonPaths.get(i)[5]-allPolygonPaths.get(i)[6+2*j+1]));
									spine = j;
								}
							}
						}
						arrowArray[0] += allPolygonPaths.get(i)[1];
						arrowArray[2] += allPolygonPaths.get(i)[1];
						arrowArray[4] += allPolygonPaths.get(i)[1];
						if (allPolygonPaths.get(i)[3]==1) {
							if (6+2*(spine+1)==sz) tempval = PolygonOperations.GetAngle(allPolygonPaths.get(i)[6+2*spine-2], allPolygonPaths.get(i)[6+2*spine-1], allPolygonPaths.get(i)[6+2*spine], allPolygonPaths.get(i)[6+2*spine+1]);
							else tempval = PolygonOperations.GetAngle(allPolygonPaths.get(i)[6+2*spine], allPolygonPaths.get(i)[6+2*spine+1], allPolygonPaths.get(i)[6+2*spine+2], allPolygonPaths.get(i)[6+2*spine+3]);
							PolygonOperations.RotateXYArray(arrowArray, tempval, 0.0f, 0.0f);
							for (int j = 0; j<3;j++) {
								arrowArray[2*j] += allPolygonPaths.get(i)[4];
								arrowArray[2*j+1] += allPolygonPaths.get(i)[5];
							}
							shapeRenderer.line(arrowArray[2], arrowArray[3], allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5]);
							shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[0], arrowArray[1]);
							shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[4], arrowArray[5]);
						} else {
							if (spine==0) tempval = PolygonOperations.GetAngle(allPolygonPaths.get(i)[6+2*spine+2], allPolygonPaths.get(i)[6+2*spine+3], allPolygonPaths.get(i)[6+2*spine], allPolygonPaths.get(i)[6+2*spine+1]);
							else tempval = PolygonOperations.GetAngle(allPolygonPaths.get(i)[6+2*spine], allPolygonPaths.get(i)[6+2*spine+1], allPolygonPaths.get(i)[6+2*spine-2], allPolygonPaths.get(i)[6+2*spine-1]);
							PolygonOperations.RotateXYArray(arrowArray, tempval, 0.0f, 0.0f);
							for (int j = 0; j<3;j++) {
								arrowArray[2*j] += allPolygonPaths.get(i)[4];
								arrowArray[2*j+1] += allPolygonPaths.get(i)[5];
							}
							shapeRenderer.line(arrowArray[2], arrowArray[3], allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5]);
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
		        	if (allPolygonPaths.get(i)[2]==1) PolygonOperations.RotateXYArray(arrowArray, (allPolygonPaths.get(i)[0])*MathUtils.degreesToRadians, 0.0f, 0.0f);
		        	else PolygonOperations.RotateXYArray(arrowArray, (360-allPolygonPaths.get(i)[0])*MathUtils.degreesToRadians, 0.0f, 0.0f);
					arrowArray[1] *= allPolygonPaths.get(i)[2];
					arrowArray[3] *= allPolygonPaths.get(i)[2];
					arrowArray[5] *= allPolygonPaths.get(i)[2];
					for (int j = 0; j<3;j++) {
						arrowArray[2*j] += allPolygonPaths.get(i)[4];
						arrowArray[2*j+1] += allPolygonPaths.get(i)[5];
					}
		        	if (allPolygonPaths.get(i)[2]==1) shapeRenderer.arc(allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5], 30, 0, allPolygonPaths.get(i)[0]);
		        	else shapeRenderer.arc(allPolygonPaths.get(i)[4], allPolygonPaths.get(i)[5], 30, allPolygonPaths.get(i)[0], 360-allPolygonPaths.get(i)[0]);
					shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[0], arrowArray[1]);
					shapeRenderer.line(arrowArray[2], arrowArray[3], arrowArray[4], arrowArray[5]);						
	        	} else if (allPolygonTypes.get(i) <= 5) {
	        		// Falling Polygons
	        		if (polySelect == i) shapeRenderer.setColor(1, 0, 0, 1);
	        		else shapeRenderer.setColor(1, 0, 0, 0.5f);
	        		// Draw the falling polygon
		        	if (allPolygonTypes.get(i) == 4) {
		        		shapeRenderer.polygon(allPolygons.get(i));
		        	} else if (allPolygonTypes.get(i) == 5) {
		        		shapeRenderer.circle(allPolygons.get(i)[0], allPolygons.get(i)[1], allPolygons.get(i)[2]);
		        	}
		        	// Draw the centre of the body
		        	// fall time, damping, sign x, sign y
		        	shapeRenderer.setColor(0.8f, 0.2f, 0.2f, opacity);
		        	shapeRenderer.circle(allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3], 5);
		        	shapeRenderer.line(allPolygonPaths.get(i)[2]-5, allPolygonPaths.get(i)[3], allPolygonPaths.get(i)[2]+5, allPolygonPaths.get(i)[3]);
		        	shapeRenderer.line(allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3]-5, allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3]+5);
		        	// Draw the fall time arrow
		        	shapeRenderer.line(allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3], allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3]-allPolygonPaths.get(i)[0]/B2DVars.EPPM);
		        	shapeRenderer.line(allPolygonPaths.get(i)[2]-10, allPolygonPaths.get(i)[3]-allPolygonPaths.get(i)[0]/B2DVars.EPPM, allPolygonPaths.get(i)[2]+10, allPolygonPaths.get(i)[3]-allPolygonPaths.get(i)[0]/B2DVars.EPPM);
		        	// Draw the damping arrow
		        	shapeRenderer.line(allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3], allPolygonPaths.get(i)[2], allPolygonPaths.get(i)[3]+allPolygonPaths.get(i)[1]/B2DVars.EPPM);
		        	shapeRenderer.line(allPolygonPaths.get(i)[2]-10, allPolygonPaths.get(i)[3]+allPolygonPaths.get(i)[1]/B2DVars.EPPM, allPolygonPaths.get(i)[2]+10, allPolygonPaths.get(i)[3]+allPolygonPaths.get(i)[1]/B2DVars.EPPM);
	        	}
	        }
        }
        // Draw the objects
        float rxcen, rycen, rangle;
        float[] rCoord;
        if (allObjects.size() != 0) {
	        for (int i = 0; i<allObjects.size(); i++){
	        	if (objectSelect == i) opacity=1.0f;
	        	else opacity = 0.5f;
	        	if  (allObjectTypes.get(i) == ObjectVars.BallChain) {
	        		shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],allObjects.get(i)[2]);
	        		shapeRenderer.rect(allObjects.get(i)[3], allObjects.get(i)[4],allObjects.get(i)[5],allObjects.get(i)[6]);
	        		// Render the line of maximum length
	        		rxcen = allObjects.get(i)[3]+0.5f*allObjects.get(i)[5];
	        		rycen = allObjects.get(i)[4]+0.5f*allObjects.get(i)[6];
	        		rangle = PolygonOperations.GetAngle(rxcen, rycen, allObjects.get(i)[0], allObjects.get(i)[1]);
	        		rCoord = PolygonOperations.RotateCoordinate(rxcen+allObjects.get(i)[7], rycen, MathUtils.radiansToDegrees*rangle, rxcen, rycen);
	        		shapeRenderer.line(rxcen,rycen,rCoord[0],rCoord[1]);
	        		shapeRenderer.setColor(1.0f, 1.0f, 0, 0.8f);
	        		shapeRenderer.line(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[3]+0.5f*allObjects.get(i)[5], allObjects.get(i)[4]+0.5f*allObjects.get(i)[6]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Boulder) {
	        		shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],allObjects.get(i)[2]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Bridge) {
	        		shapeRenderer.setColor(1, 0.5f, 0, opacity);
	        		for (int j=0; j<3; j++) {
	        			shapeRenderer.line(allObjects.get(i)[2*j], allObjects.get(i)[2*j+1], allObjects.get(i)[2*j+2], allObjects.get(i)[2*j+3]);
	        			shapeRenderer.line(allObjects.get(i)[8+2*j], allObjects.get(i)[8+2*j+1], allObjects.get(i)[8+2*j+2], allObjects.get(i)[8+2*j+3]);
	        		}
        			shapeRenderer.line(allObjects.get(i)[2*3], allObjects.get(i)[2*3+1], allObjects.get(i)[0], allObjects.get(i)[1]);
        			shapeRenderer.line(allObjects.get(i)[8+2*3], allObjects.get(i)[8+2*3+1], allObjects.get(i)[8], allObjects.get(i)[9]);
	        		// Draw the line connecting the two bridge ends
        			shapeRenderer.line(0.5f*(allObjects.get(i)[0]+allObjects.get(i)[4]), 0.5f*(allObjects.get(i)[1]+allObjects.get(i)[5]), 0.5f*(allObjects.get(i)[8]+allObjects.get(i)[12]), 0.5f*(allObjects.get(i)[9]+allObjects.get(i)[13]));
        			// Draw the Bridge Sag
	        		rxcen = 0.25f*(allObjects.get(i)[0]+allObjects.get(i)[4]+allObjects.get(i)[8]+allObjects.get(i)[12]);
	        		rycen = 0.25f*(allObjects.get(i)[1]+allObjects.get(i)[5]+allObjects.get(i)[9]+allObjects.get(i)[13]);
	        		shapeRenderer.line(rxcen, rycen, rxcen, rycen-allObjects.get(i)[16]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Crate) {
	        		shapeRenderer.setColor(1, 0.5f, 0, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.line(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[4], allObjects.get(i)[5]);
	        		shapeRenderer.line(allObjects.get(i)[2], allObjects.get(i)[3], allObjects.get(i)[6], allObjects.get(i)[7]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.DoorBlue) {
	        		shapeRenderer.setColor(0, 0.7f, 1, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.circle(0.5f*(allObjects.get(i)[4]+allObjects.get(i)[6]),0.5f*(allObjects.get(i)[5]+allObjects.get(i)[7]),1.5f*ObjectVars.objectDoor[4]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.DoorGreen) {
	        		shapeRenderer.setColor(0, 1, 0, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.circle(0.5f*(allObjects.get(i)[4]+allObjects.get(i)[6]),0.5f*(allObjects.get(i)[5]+allObjects.get(i)[7]),1.5f*ObjectVars.objectDoor[4]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.DoorRed) {
	        		shapeRenderer.setColor(1, 0, 0, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.circle(0.5f*(allObjects.get(i)[4]+allObjects.get(i)[6]),0.5f*(allObjects.get(i)[5]+allObjects.get(i)[7]),1.5f*ObjectVars.objectDoor[4]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.KeyBlue) {
	        		shapeRenderer.setColor(0, 0.7f, 1, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        	} else if (allObjectTypes.get(i) == ObjectVars.KeyGreen) {
	        		shapeRenderer.setColor(0, 1, 0, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        	} else if (allObjectTypes.get(i) == ObjectVars.KeyRed) {
	        		shapeRenderer.setColor(1, 0, 0, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        	} else if (allObjectTypes.get(i) == ObjectVars.GateSwitch) {
	        		// Draw line connecting switch box and gate
	        		shapeRenderer.setColor(1, 1, 1, opacity);
	        		rxcen = 0.25f*(allObjects.get(i)[8]+allObjects.get(i)[10]+allObjects.get(i)[12]+allObjects.get(i)[14]);
	        		rycen = 0.25f*(allObjects.get(i)[9]+allObjects.get(i)[11]+allObjects.get(i)[13]+allObjects.get(i)[15]);
	        		shapeRenderer.line(0.5f*(allObjects.get(i)[0] + allObjects.get(i)[4]), 0.5f*(allObjects.get(i)[1] + allObjects.get(i)[5]), rxcen, rycen);
	        		// Draw Gate
	        		shapeRenderer.setColor(0.7f*(1.0f-allObjects.get(i)[17]), 0.7f*allObjects.get(i)[17], 0, opacity);
	        		for (int j = 0; j<8; j++) {
	        			transPoly[j] = allObjects.get(i)[j];
	        		}
	        		shapeRenderer.polygon(transPoly);
	        		// Draw Switch box
	        		for (int j = 8; j<16; j++) {
	        			transPoly[j-8] = allObjects.get(i)[j];
	        		}
	        		shapeRenderer.polygon(transPoly);
	        		// Draw Switch
	        		rangle = PolygonOperations.GetAngle(allObjects.get(i)[8], allObjects.get(i)[9], allObjects.get(i)[10], allObjects.get(i)[11]);
	        		rCoord = PolygonOperations.RotateCoordinate(0.0f, 10.0f, ((MathUtils.radiansToDegrees*rangle)+allObjects.get(i)[16]), 0.0f, 0.0f);
		    		shapeRenderer.line(rxcen, rycen, rxcen+rCoord[0], rycen+rCoord[1]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Gravity) {
	        		if (i==0) shapeRenderer.setColor(1, 0.8f, 0, opacity);
	        		else shapeRenderer.setColor(1, 0.5f, 1, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.polygon(allObjectArrows.get(i));
	        	} else if (allObjectTypes.get(i) == ObjectVars.Jewel) {
	        		if (i==2) {
	        			// Diamond Jewel
	        			shapeRenderer.setColor(0.85f, 0.85f, 0.85f, opacity);
	        			xcen = (0.5f*13.0f*(allObjects.get(i)[0]+allObjects.get(i)[4]) + 2.3f*allObjects.get(i)[2])/(13.0f+2.3f);
		        		ycen = (0.5f*13.0f*(allObjects.get(i)[1]+allObjects.get(i)[5]) + 2.3f*allObjects.get(i)[3])/(13.0f+2.3f);
		        		shapeRenderer.circle(xcen, ycen, ObjectVars.objectStartWheels[2]); // Use the same radius as the start wheels
		        	} else shapeRenderer.setColor(0.7f, 0.7f, 0, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		//shapeRenderer.arc(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[2], 0, 60, 2);
	        		//shapeRenderer.arc(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[2], 60, 60, 2);
	        		//shapeRenderer.arc(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[2], 120, 60, 2);
	        		//shapeRenderer.arc(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[2], 180, 60, 2);
	        		//shapeRenderer.arc(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[2], 240, 60, 2);
	        		//shapeRenderer.arc(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[2], 300, 60, 2);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Log) {
	        		shapeRenderer.setColor(1, 0.5f, 0, opacity);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],allObjects.get(i)[2]);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],0.75f*allObjects.get(i)[2]);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],0.50f*allObjects.get(i)[2]);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],0.25f*allObjects.get(i)[2]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Nitrous) {
	        		shapeRenderer.setColor(0, 0.7f, 1, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.setColor(1, 0.65f, 0, opacity);
	        		shapeRenderer.line(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[4], allObjects.get(i)[5]);
	        		shapeRenderer.line(allObjects.get(i)[2], allObjects.get(i)[3], allObjects.get(i)[6], allObjects.get(i)[7]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Pendulum) {
	        		shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],allObjects.get(i)[2]);
	        		shapeRenderer.rect(allObjects.get(i)[3], allObjects.get(i)[4],allObjects.get(i)[5],allObjects.get(i)[6]);
	        		shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
	        		shapeRenderer.line(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[3]+0.5f*allObjects.get(i)[5], allObjects.get(i)[4]+0.5f*allObjects.get(i)[6]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Spike) {
	        		shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
	        		shapeRenderer.circle(allObjects.get(i)[0], allObjects.get(i)[1],allObjects.get(i)[2]);
	        		shapeRenderer.triangle(allObjects.get(i)[0], allObjects.get(i)[1]+1.3f*allObjects.get(i)[2], allObjects.get(i)[0]-1.126f*allObjects.get(i)[2], allObjects.get(i)[1]-0.65f*allObjects.get(i)[2], allObjects.get(i)[0]+1.126f*allObjects.get(i)[2], allObjects.get(i)[1]-0.65f*allObjects.get(i)[2]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Transport) {
	        		shapeRenderer.setColor(1, 1, 1, opacity);
	        		shapeRenderer.line(0.5f*(allObjects.get(i)[0] + allObjects.get(i)[4]), 0.5f*(allObjects.get(i)[1] + allObjects.get(i)[5]), 0.5f*(allObjects.get(i)[8] + allObjects.get(i)[12]), 0.5f*(allObjects.get(i)[9] + allObjects.get(i)[13]));
	        		for (int j = 0; j<8; j++) {
	        			transPoly[j] = allObjects.get(i)[j];
	        		}
	        		shapeRenderer.polygon(transPoly);
	        		for (int j = 8; j<16; j++) {
	        			transPoly[j-8] = allObjects.get(i)[j];
	        		}
	        		shapeRenderer.polygon(transPoly);
	        		// Entry and exit blue/red lines
	        		shapeRenderer.setColor(1, 0, 0, opacity);
		    		shapeRenderer.line(allObjects.get(i)[0], allObjects.get(i)[1], allObjects.get(i)[2], allObjects.get(i)[3]);
		    		shapeRenderer.line(allObjects.get(i)[8], allObjects.get(i)[9], allObjects.get(i)[10], allObjects.get(i)[11]);
	        		shapeRenderer.setColor(0, 0.7f, 1, opacity);
		    		shapeRenderer.line(allObjects.get(i)[4], allObjects.get(i)[5], allObjects.get(i)[6], allObjects.get(i)[7]);
		    		shapeRenderer.line(allObjects.get(i)[12], allObjects.get(i)[13], allObjects.get(i)[14], allObjects.get(i)[15]);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Start) {
	        		shapeRenderer.setColor(1, 0.8f, 0, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.polygon(allObjectArrows.get(i));
	        		rangle = PolygonOperations.GetAngle(allObjectArrows.get(i)[0], allObjectArrows.get(i)[1], allObjectArrows.get(i)[2], allObjectArrows.get(i)[3]);
	        		xcen = allObjects.get(i)[0]-ObjectVars.objectStart[0];
	        		ycen = allObjects.get(i)[1]-ObjectVars.objectStart[1];
	        		shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.7f*opacity);
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
	        		shapeRenderer.setColor(1, 0.705f, 0.522f, opacity);
	        		shapeRenderer.circle(xcen + length*(float)Math.cos(rangle), ycen + length*(float)Math.sin(rangle), ObjectVars.objectStartWheels[4]);
	        		for (int j=0; j<startDirPoly.length/2; j++) {
	        			startDirPoly[2*j] += xcen+length*(float)Math.cos(rangle);
	        			startDirPoly[2*j+1] += ycen+length*(float)Math.sin(rangle);
	        		}
	        		shapeRenderer.polygon(startDirPoly);
	        	} else if (allObjectTypes.get(i) == ObjectVars.Finish) {
	        		rxcen = allObjects.get(i)[0]-ObjectVars.objectFinish[0];
	        		rycen = allObjects.get(i)[1]-ObjectVars.objectFinish[1];
	        		shapeRenderer.setColor(0, 0.7f, 1.0f, opacity);
	        		shapeRenderer.polygon(allObjects.get(i));
	        		shapeRenderer.circle(rxcen, rycen, ObjectVars.objectFinishBall[2]);
	        	}
	        }
        }
        // Draw the decorations
        int dTyp;
        if (allDecors.size() != 0) {
	        for (int i = 0; i<allDecors.size(); i++){
	        	dTyp = allDecorTypes.get(i);
	        	if (decorSelect == i) opacity=1.0f;
	        	else opacity = 0.5f;
	        	if  (DecorVars.IsRoadSign(dTyp)) {
	        		shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
	        		shapeRenderer.circle(allDecors.get(i)[0], allDecors.get(i)[1],allDecors.get(i)[2]);
	        		// Render the pole
	        		rCoord = PolygonOperations.RotateCoordinate(allDecors.get(i)[0], allDecors.get(i)[1]-5.0f*allDecors.get(i)[2], MathUtils.radiansToDegrees*allDecors.get(i)[3], allDecors.get(i)[0], allDecors.get(i)[1]);
	        		shapeRenderer.line(allDecors.get(i)[0], allDecors.get(i)[1], rCoord[0], rCoord[1]);
	        	} else if (dTyp == DecorVars.Grass) {
	        		shapeRenderer.setColor(0, 1, 0, opacity);
	        		shapeRenderer.polygon(allDecors.get(i));
	        	} else if (dTyp == DecorVars.LargeStone) {
	        		shapeRenderer.setColor(0.7f, 0.7f, 0.7f, opacity);
	        		shapeRenderer.circle(allDecors.get(i)[0], allDecors.get(i)[1],allDecors.get(i)[2]);
	        	}
	        }
        }

        // Draw the current polygon
        if (drawingPoly == true) {
        	shapeRenderer.setColor(1, 1, 0.1f, 1.0f);
        	if (shapeDraw != null) {
        		// Drawing a circle or a rectangle
            	if (shapeDraw.length == 3) {
	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
            		shapeRenderer.circle(shapeDraw[0], shapeDraw[1], (float) Math.sqrt((tempx-shapeDraw[0])*(tempx-shapeDraw[0]) + (tempy-shapeDraw[1])*(tempy-shapeDraw[1])));
            	} else if (shapeDraw.length == 8) {
	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
        		tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
        		tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBMOVEX >= 0) {
        			shapeRenderer.line(polyDraw.get(polyDraw.size()-1)[0], polyDraw.get(polyDraw.size()-1)[1], tempx, tempy);
        			// If the cursor is close to closing the polygon, draw a yellow circle
        			if (polyDraw.size() >= 3) {
        				if (Math.sqrt((tempx-polyDraw.get(0)[0])*(tempx-polyDraw.get(0)[0]) + (tempy-polyDraw.get(0)[1])*(tempy-polyDraw.get(0)[1])) < cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold) {
        					shapeRenderer.circle(polyDraw.get(0)[0], polyDraw.get(0)[1], cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
        				}
        			}
        		}
        	}
        }
        // Draw the updated group polygons
        if (updateGroupPoly != null) {
        	shapeRenderer.setColor(1, 1, 0.1f, 1);
        	for (int i = 0; i<updateGroupPoly.size(); i++){
        			if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 0) shapeRenderer.polygon(updateGroupPoly.get(i));
        			else if (allPolygonTypes.get(groupPolySelect.get(i))%2 == 1) shapeRenderer.circle(updateGroupPoly.get(i)[0], updateGroupPoly.get(i)[1], updateGroupPoly.get(i)[2]);        			
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
        		}
        	}
        }
        // Draw a selected vertex or segment if necessary
        if ((mode==4)&(modeParent.equals("Set Path"))) {
	        if ((segmSelect != -1) & (vertSelect != -1) & (polySelect != -1) & (allPolygons.size()!=0)) {
	        	shapeRenderer.setColor(1, 1, 0.1f, 1);
	        	shapeRenderer.line(allPolygonPaths.get(polySelect)[6+2*segmSelect],allPolygonPaths.get(polySelect)[6+2*segmSelect+1],allPolygonPaths.get(polySelect)[6+2*(segmSelect+1)],allPolygonPaths.get(polySelect)[6+2*(segmSelect+1)+1]);
	        } else if ((vertSelect != -1) & (polySelect != -1)) {
	        	shapeRenderer.setColor(1, 1, 0.1f, 1);
	            shapeRenderer.circle(allPolygonPaths.get(polySelect)[6+2*vertSelect], allPolygonPaths.get(polySelect)[6+2*vertSelect+1], cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
	        } else if ((segmHover != -1) & (vertHover != -1) & (polySelect != -1)) {
	            // Draw the hover segment
	        	shapeRenderer.setColor(1, 1, 0.1f, 1);
	        	shapeRenderer.line(allPolygonPaths.get(polySelect)[6+2*segmHover],allPolygonPaths.get(polySelect)[6+2*segmHover+1],allPolygonPaths.get(polySelect)[6+2*(segmHover+1)],allPolygonPaths.get(polySelect)[6+2*(segmHover+1)+1]);        	
	            shapeRenderer.circle(0.5f*(allPolygonPaths.get(polySelect)[6+2*segmHover]+allPolygonPaths.get(polySelect)[6+2*(segmHover+1)]), 0.5f*(allPolygonPaths.get(polySelect)[6+2*segmHover+1]+allPolygonPaths.get(polySelect)[6+2*(segmHover+1)+1]), cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
	        } else if ((vertHover != -1) & (polySelect != -1)) {
	            // Draw the hover vertex
	        	shapeRenderer.setColor(1, 1, 0.1f, 1);
	            shapeRenderer.circle(allPolygonPaths.get(polySelect)[6+2*vertHover], allPolygonPaths.get(polySelect)[6+2*vertHover+1], cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
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
	        	if (mode==6) shapeRenderer.circle(allDecors.get(polySelect)[2*vertSelect], allDecors.get(polySelect)[2*vertSelect+1], cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold); 
	        	else shapeRenderer.circle(allPolygons.get(polySelect)[2*vertSelect], allPolygons.get(polySelect)[2*vertSelect+1], cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
	        } else if ((segmHover != -1) & (vertHover != -1) & (polyHover != -1)) {
	            // Draw the hover segment
	        	shapeRenderer.setColor(1, 1, 0.1f, 1);
	        	int segmNext = segmHover + 1;
	        	if (segmNext==allPolygons.get(polyHover).length/2) segmNext = 0;
	        	shapeRenderer.line(allPolygons.get(polyHover)[2*segmNext],allPolygons.get(polyHover)[2*segmNext+1],allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+1]);
	        	//shapeRenderer.line(allPolygons.get(polyHover)[2*vertHover],allPolygons.get(polyHover)[2*vertHover+1],allPolygons.get(polyHover)[2*segmHover],allPolygons.get(polyHover)[2*segmHover+1]);        	
	            shapeRenderer.circle(0.5f*(allPolygons.get(polyHover)[2*segmNext]+allPolygons.get(polyHover)[2*segmHover]), 0.5f*(allPolygons.get(polyHover)[2*segmNext+1]+allPolygons.get(polyHover)[2*segmHover+1]), cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
	        } else if ((vertHover != -1) & (polyHover != -1)) {
	            // Draw the hover vertex
	        	shapeRenderer.setColor(1, 1, 0.1f, 1);
	        	if (mode==6) shapeRenderer.circle(allDecors.get(polyHover)[2*vertHover], allDecors.get(polyHover)[2*vertHover+1], cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
	        	else shapeRenderer.circle(allPolygons.get(polyHover)[2*vertHover], allPolygons.get(polyHover)[2*vertHover+1], cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
	        }
        }
        // If a convex polygon has been calculated, draw it
//        if (convexPolygons != null) {
//	        for (int i = 0; i<convexPolygons.size(); i++){
//	        	shapeRenderer.setColor(0, 0, 1, 1);
//        		shapeRenderer.polygon(convexPolygons.get(i));
//	        }
//        }
        // Draw the cursor
        shapeRenderer.setColor(1, 0, 0, 1.0f);
        shapeRenderer.circle(cursposx, cursposy, cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold);
        shapeRenderer.end();

        // Draw the decoration signs
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        float signWidth;
        if (allDecors.size() != 0) {
	        for (int i = 0; i<allDecors.size(); i++){
	        	dTyp = allDecorTypes.get(i);
	        	if (decorSelect == i) opacity=1.0f;
	        	else opacity = 0.5f;
	        	if  (DecorVars.IsRoadSign(dTyp)) {
	        		signWidth = signFont.getBounds(DecorVars.GetObjectName(allDecorTypes.get(i))).width;
	        		signFont.draw(sb, DecorVars.GetObjectName(allDecorTypes.get(i)), allDecors.get(i)[0]-signWidth/2, allDecors.get(i)[1]+0.3f*allDecors.get(i)[2]);
	        	} else if (dTyp == DecorVars.LargeStone) {

	        	}
	        }
        }
        sb.end();

        // If there are any warning/error messages, write them to screen
        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        for (int i=0; i<totalNumMsgs; i++) {
        	if (warnMessage[i] != null) {
        		if (warnType[i] == 0) warnFont.setColor(0.1f, 0.9f, 0.1f, 1);
        		else if (warnType[i] == 1) warnFont.setColor(1, 0.5f, 0, 1);
        		else warnFont.setColor(1, 0, 0, 1);
        		warnFont.draw(sb, warnMessage[i], toolbarWidth*1.1f, BikeGame.V_HEIGHT-(1.1f*i+2)*warnHeight);
        	}
        }
        sb.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        // Finally, draw the toolbar
        if (!hideToolbar) {
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();        	
        }
    }
   
    public void dispose() {
    	if (stage != null) stage.dispose();
    	if (skin != null) skin.dispose();
    	if (warnFont != null) warnFont.dispose();
    	if (signFont != null) signFont.dispose();
    }

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	 /////////////////////////////////
	///                           ///
   ///   ALL TOOLBAR OPERATIONS  ///
  ///                           ///
 /////////////////////////////////

	public void ControlMode2() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString(); 
		if (modeParent.equals("Collect Jewels")) {
    		LevelVars.set(LevelVars.PROP_NUMJEWELS, modeChild);
		} else if (modeParent.equals("Gravity")) {
    		LevelVars.set(LevelVars.PROP_GRAVITY, modeChild);
		} else if (modeParent.equals("Ground Texture")) {
    		LevelVars.set(LevelVars.PROP_GROUND_TEXTURE, modeChild);
		} else if (modeParent.equals("Sky Texture")) {
    		LevelVars.set(LevelVars.PROP_SKY_TEXTURE, modeChild);
		}
	}

	public void ControlMode3(boolean falling) {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString(); 
    	if (modeParent.equals("Polygon")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly == true) {
    	        		DrawPolygon();        		
    	        	} else {
    	        		polyDraw = new ArrayList<float[]>();
    	        		drawingPoly = true;
    	        		DrawPolygon();
    	        	}
    	    	}
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectPolygon("up");
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = GameInput.MBDOWNX*scrscale;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MovePolygon(polySelect, endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1)) {
    			UpdatePolygon(polySelect);
    			polySelect = -1;
    		} else if ((modeChild.equals("Scale")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		    		nullvarA = (float) (Math.sqrt((endX-cursposx)*(endX-cursposx) + (endY-cursposy)*(endY-cursposy))/Math.sqrt((startX-cursposx)*(startX-cursposx) + (startY-cursposy)*(startY-cursposy)));
	            	ScalePolygon(polySelect, nullvarA);
    			}
    		} else if ((modeChild.equals("Scale")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1)) {
    			UpdatePolygon(polySelect);
    			polySelect = -1;
    		} else if ((modeChild.equals("Flip x")) & (GameInput.MBJUSTPRESSED==true) & (polySelect == -1)) {
    			SelectPolygon("up");
    			if (polySelect != -1) {
	            	FlipPolygon(polySelect, "x");
	    			UpdatePolygon(polySelect);
	    			polySelect = -1;	            	
    			}
    		} else if ((modeChild.equals("Flip y")) & (GameInput.MBJUSTPRESSED==true) & (polySelect == -1)) {
    			SelectPolygon("up");
    			if (polySelect != -1) {
	            	FlipPolygon(polySelect, "y");
	    			UpdatePolygon(polySelect);
	    			polySelect = -1;	            	
    			}
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdatePolygon(polySelect);
    			polySelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if (modeChild.equals("Add Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestSegment(false);
        			} else {
            			startX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
            			startY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
            			int segmNext = segmSelect + 1;
            			if (segmNext==allPolygons.get(polySelect).length/2) segmNext = 0;
            			if (segmNext < segmSelect) AddVertex(polySelect, segmNext, segmSelect, startX, startY);
            			else AddVertex(polySelect, segmSelect, segmNext, startX, startY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1)) {
             			UpdatePolygon(polySelect);
             			polySelect = -1;
             			vertSelect = -1;
        		} else FindNearestSegment(true);
    		} else if (modeChild.equals("Delete Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			if (engageDelete==false) {
    				FindNearestVertex(true);
    			}
    			if (GameInput.MBJUSTPRESSED==true) {
    				FindNearestVertex(false);
    				engageDelete = true;
    			}
    		} else if (modeChild.equals("Move Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestVertex(false);
        				startX = GameInput.MBDOWNX*scrscale;
        				startY = GameInput.MBDOWNY;
        			} else {
    					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
    		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
    	            	MoveVertex(polySelect, vertSelect, endX, endY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1)) {
        			UpdatePolygon(polySelect);
        			polySelect = -1;
        			vertSelect = -1;
        		} else FindNearestVertex(true);
    		} else if (mode==7) { // Some options that are only relevant to falling platforms
    			if ((modeChild.equals("Set Sign")) & (GameInput.MBDRAG==true)) {
    				if (polySelect == -1) SelectPolygon("down");
        			else {
            			tempx = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
            			tempy = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        				MoveFallingSign(polySelect);
        			}
        		} else if ((modeChild.equals("Set Sign")) & (GameInput.MBRELEASE==true) & (polySelect != -1)) {
        			UpdatePath(polySelect);
        			polySelect = -1;
                	GameInput.MBRELEASE=false;
    			} else if ((modeChild.equals("Set Fall Time")) & (GameInput.MBDRAG==true)) {
    				if (polySelect == -1) SelectPolygon("down");
        			else {
    		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    		    		float fallTime = (allPolygonPaths.get(polySelect)[3]-endY)*B2DVars.EPPM;
    		    		if (fallTime < 0.0f) fallTime = 0.0f;
						warnMessage[warnNumber] = "Fall time = "+ String.format("%.2f", fallTime)+" seconds";
						warnElapse[warnNumber] = 0.0f;
						warnType[warnNumber] = 0;
						updatePath = allPolygonPaths.get(polySelect).clone();
						updatePath[0] = fallTime;
        			}
        		} else if ((modeChild.equals("Set Fall Time")) & (GameInput.MBRELEASE==true) & (polySelect != -1)) {
        			UpdatePath(polySelect);
        			polySelect = -1;
                	GameInput.MBRELEASE=false;
    			} else if ((modeChild.equals("Set Damping")) & (GameInput.MBDRAG==true)) {
    				if (polySelect == -1) SelectPolygon("down");
        			else {
    		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    		    		float damping = (allPolygonPaths.get(polySelect)[3]-endY)*B2DVars.EPPM;
    		    		if (damping > 0.0f) damping = 0.0f;
						warnMessage[warnNumber] = "Damping = "+ String.format("%.2f", -damping)+" per second";
						warnElapse[warnNumber] = 0.0f;
						warnType[warnNumber] = 0;
						updatePath = allPolygonPaths.get(polySelect).clone();
						updatePath[1] = -damping;
        			}
        		} else if ((modeChild.equals("Set Damping")) & (GameInput.MBRELEASE==true) & (polySelect != -1)) {
        			UpdatePath(polySelect);
        			polySelect = -1;
                	GameInput.MBRELEASE=false;
    				
    			}
    		}
    	} else if (modeParent.equals("Rectangle")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly) {
    	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    	    			shapeDraw[2] = tempx;
    	    			shapeDraw[3] = shapeDraw[1];
    	    			shapeDraw[4] = tempx;
    	    			shapeDraw[5] = tempy;
    	    			shapeDraw[6] = shapeDraw[0];
    	    			shapeDraw[7] = tempy;
    	    			if (falling==false) AddPolygon(shapeDraw, 0, 8);
    	    			else AddPolygon(shapeDraw, 4, 8);
    			    	drawingPoly = false;
    			    	shapeDraw = null;
    	        	} else {
    	        		shapeDraw = new float[8];
    	        		drawingPoly = true;
    	        		shapeDraw[0] = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	        		shapeDraw[1] = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    	        	}
    	    	}
    		}
    	} else if (modeParent.equals("Circle")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly == true) {
    	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    	    			shapeDraw[2] = (float) Math.sqrt((tempx-shapeDraw[0])*(tempx-shapeDraw[0]) + (tempy-shapeDraw[1])*(tempy-shapeDraw[1]));
    	    			if (falling==false) AddPolygon(shapeDraw, 1, 3);
    	    			else AddPolygon(shapeDraw, 5, 3);
    			    	drawingPoly = false;
    			    	shapeDraw = null;
    	        	} else {
    	        		shapeDraw = new float[3];
    	        		drawingPoly = true;
    	        		shapeDraw[0] = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	        		shapeDraw[1] = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    	        	}
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
    	        		DrawPolygon();        		
    	        	} else {
    	        		polyDraw = new ArrayList<float[]>();
    	        		drawingPoly = true;
    	        		DrawPolygon();
    	        	}
    	    	}
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectPolygon("up");
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    				//startX = GameInput.MBDOWNX*scrscale;
    				//startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
					//endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
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
    			UpdatePolygon(polySelect);
    			polySelect = -1;
    		} else if ((modeChild.equals("Flip x")) & (GameInput.MBJUSTPRESSED==true) & (polySelect == -1)) {
    			SelectPolygon("up");
    			if (polySelect != -1) {
	            	FlipPolygon(polySelect, "x");
	    			UpdatePolygon(polySelect);
	    			polySelect = -1;	            	
    			}
    		} else if ((modeChild.equals("Flip y")) & (GameInput.MBJUSTPRESSED==true) & (polySelect == -1)) {
    			SelectPolygon("up");
    			if (polySelect != -1) {
	            	FlipPolygon(polySelect, "y");
	    			UpdatePolygon(polySelect);
	    			polySelect = -1;	            	
    			}
    		} else if ((modeChild.equals("Scale")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		    		nullvarA = (float) (Math.sqrt((endX-cursposx)*(endX-cursposx) + (endY-cursposy)*(endY-cursposy))/Math.sqrt((startX-cursposx)*(startX-cursposx) + (startY-cursposy)*(startY-cursposy)));
	            	ScalePolygon(polySelect, nullvarA);
    			}
    		} else if ((modeChild.equals("Scale")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1)) {
    			UpdatePolygon(polySelect);
    			polySelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (polySelect == -1) {
    				SelectPolygon("down");
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdatePolygon(polySelect);
    			polySelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if (modeChild.equals("Add Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestSegment(false);
        			} else {
            			startX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
            			startY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
            			int segmNext = segmSelect + 1;
            			if (segmNext==allPolygons.get(polySelect).length/2) segmNext = 0;
            			if (segmNext < segmSelect) AddVertex(polySelect, segmNext, segmSelect, startX, startY);
            			else AddVertex(polySelect, segmSelect, segmNext, startX, startY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1)) {
             			UpdatePolygon(polySelect);
             			polySelect = -1;
             			vertSelect = -1;
        		} else FindNearestSegment(true);
    		} else if (modeChild.equals("Delete Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			if (engageDelete==false) {
    				FindNearestVertex(true);
    			}
    			if (GameInput.MBJUSTPRESSED==true) {
    				FindNearestVertex(false);
    				engageDelete = true;
    			}
    		} else if (modeChild.equals("Move Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestVertex(false);
        				startX = GameInput.MBDOWNX*scrscale;
        				startY = GameInput.MBDOWNY;
        			} else {
    					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
    		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
    	            	MoveVertex(polySelect, vertSelect, endX, endY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1)) {
        			UpdatePolygon(polySelect);
        			polySelect = -1;
        			vertSelect = -1;
        		} else FindNearestVertex(true);
    		}
    	} else if (modeParent.equals("Rectangle")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly) {
    	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    	        		shapeDraw[0] = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	        		shapeDraw[1] = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    	        	}
    	    	}
    		}
    	} else if (modeParent.equals("Circle")) {
    		if (modeChild.equals("Add")) {
    	    	if (GameInput.MBJUSTPRESSED) {
    	        	if (drawingPoly == true) {
    	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    	    			shapeDraw[2] = (float) Math.sqrt((tempx-shapeDraw[0])*(tempx-shapeDraw[0]) + (tempy-shapeDraw[1])*(tempy-shapeDraw[1]));
    	    			AddPolygon(shapeDraw, 3, 3);
    			    	drawingPoly = false;
    			    	shapeDraw = null;
    	        	} else {
    	        		shapeDraw = new float[3];
    	        		drawingPoly = true;
    	        		shapeDraw[0] = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    	        		shapeDraw[1] = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
				updatePathVertex[0] = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				updatePathVertex[1] = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
			} else if ((modeChild.equals("Move Path")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
				startX = GameInput.MBDOWNX*scrscale;
				startY = GameInput.MBDOWNY;
				endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
				endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
				MovePath(polySelect, endX, endY);
    		} else if ((modeChild.equals("Move Path")) & (GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (updatePath!=null)) {
    			UpdatePath(polySelect);
    		} else if ((modeChild.equals("Rotate Path")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
   				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
   				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
	    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
   				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
   				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
	    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) FindNearestSegmentPath(false, polySelect, false);
        			else {
            			startX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
            			startY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
            			AddVertexPath(polySelect, segmSelect, segmSelect+1, startX, startY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1) & (updatePath!=null)) {
        			UpdatePath(polySelect);
        			vertSelect = -1;
        		} else FindNearestSegmentPath(true, polySelect, false);
    		} else if ((modeChild.equals("Move Vertex")) & (polySelect != -1)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestVertexPath(false, polySelect);
        				startX = GameInput.MBDOWNX*scrscale;
        				startY = GameInput.MBDOWNY;
        			} else {
    					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
    		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
    	            	MoveVertexPath(polySelect, vertSelect, endX, endY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1) & (updatePath!=null)) {
        			UpdatePath(polySelect);
        			vertSelect = -1;
        		} else FindNearestVertexPath(true, polySelect);
    		} else if ((modeChild.equals("Move Ghost")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		FindNearestSegmentPath(false, polySelect, true);
    		} else if ((modeChild.equals("Delete Vertex")) & (polySelect != -1)) {
    			if (allPolygonPaths.get(polySelect).length > 6) {
	    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
	    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
	    			if (engageDelete==false) FindNearestVertexPath(true, polySelect);
	    			if (GameInput.MBJUSTPRESSED==true) {
	    				FindNearestVertexPath(false, polySelect);
	    				engageDelete = true;
	    			}
    			}
    		} else if ((modeChild.equals("Set Rotation")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
    			float[] newArr = allPolygonPaths.get(polySelect).clone();
    			endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			float angle = PolygonOperations.GetAngle(newArr[4], newArr[5], endX, endY);
    			newArr[0] = MathUtils.radiansToDegrees*angle;
    			allPolygonPaths.set(polySelect, newArr.clone());
    		} else if ((modeChild.equals("Set Speed")) & (GameInput.MBDRAG==true) & (polySelect != -1)) {
    			float[] newArr = allPolygonPaths.get(polySelect).clone();
    			endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			newArr[1] = (float) Math.sqrt((newArr[4]-endX)*(newArr[4]-endX) + (newArr[5]-endY)*(newArr[5]-endY));
    			allPolygonPaths.set(polySelect, newArr.clone());
    		}
    	}
	}

	public void ControlMode5() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		if ((modeParent.equals("Ball & Chain")) | (modeParent.equals("Pendulum"))) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveball", endX, endY);
				}
			} else if ((modeChild.equals("Move Ball")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveball");
				objectSelect = -1;
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					if (modeParent.equals("Ball & Chain"))  SelectObject("down", ObjectVars.BallChain, false, false);
					else if (modeParent.equals("Pendulum")) SelectObject("down", ObjectVars.Pendulum, false, false);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveanchor", endX, endY);
				}
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveanchor");
				objectSelect = -1;
			} else if ((modeChild.equals("Set Max Length")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					if (modeParent.equals("Ball & Chain"))  SelectObject("down", ObjectVars.BallChain, false, true);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
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
					UpdateObject(objectSelect, "moveball");
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
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				AddObject(ObjectVars.Boulder, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Boulder, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Boulder, false, true);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move");
				objectSelect = -1;
			}
		} else if (modeParent.equals("Bridge")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				AddObject(ObjectVars.Bridge, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Bridge, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Bridge, false, false);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveentry", endX, endY);
				}
			} else if ((modeChild.equals("Move Anchor")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveentry");
				objectSelect = -1;
			} else if ((modeChild.equals("Set Bridge Sag")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Bridge, false, false);
					startY = GameInput.MBDOWNY;
				} else {
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			AddObject(ObjectVars.Crate, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.Crate, false, false);
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Crate, false, false);
    				startX = GameInput.MBDOWNX*scrscale;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move");
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Crate, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotateobject");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Diamond")) {
			if ((modeChild.equals("Put")) & (GameInput.MBJUSTPRESSED)) {
				objectSelect = 2;
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				newPoly = ObjectVars.objectJewel.clone();
				ShiftObject(tempx, tempy);
				updatePoly = allObjects.set(objectSelect, newPoly.clone());
				newCoord = new float[2];
				newCoord[0] = tempx;
				newCoord[1] = tempy;
				newPoly = allObjectCoords.set(objectSelect, newCoord.clone());
				updatePoly = null;
				objectSelect = -1;
			}
		} else if ((modeParent.equals("Door (blue)")) | (modeParent.equals("Door (green)")) | (modeParent.equals("Door (red)")) | (modeParent.equals("Key (blue)")) | (modeParent.equals("Key (green)")) | (modeParent.equals("Key (red)"))){
			if (modeParent.equals("Door (blue)")) ctype = ObjectVars.DoorBlue;
			else if (modeParent.equals("Door (green)")) ctype = ObjectVars.DoorGreen;
			else if (modeParent.equals("Door (red)")) ctype = ObjectVars.DoorRed;
			else if (modeParent.equals("Key (blue)")) ctype = ObjectVars.KeyBlue;
			else if (modeParent.equals("Key (green)")) ctype = ObjectVars.KeyGreen;
			else if (modeParent.equals("Key (red)")) ctype = ObjectVars.KeyRed;
			else System.out.println("Door/Key color not specified");
    		if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			AddObject(ctype, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ctype, false, false);
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ctype, false, false);
    				startX = GameInput.MBDOWNX*scrscale;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move");
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ctype, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotateobject");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Gate Switch")) {
    		if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			AddObject(ObjectVars.GateSwitch, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.GateSwitch, false, false);
    			engageDelete = true;
    		} else if ((modeChild.equals("Move Gate/Switch")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.GateSwitch, false, false);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveentry", endX, endY);
				}
			} else if ((modeChild.equals("Move Gate/Switch")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveentry");
				objectSelect = -1;
			} else if ((modeChild.equals("Rotate Gate/Switch")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.GateSwitch, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotateentry");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		} else if ((modeChild.equals("Scale Gate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) SelectObject("down", ObjectVars.GateSwitch, false, false);
    			else if (tentry==1) {
					xcen = 0.5f*(allObjects.get(objectSelect)[0] + allObjects.get(objectSelect)[4]);
					ycen = 0.5f*(allObjects.get(objectSelect)[1] + allObjects.get(objectSelect)[5]);
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		    		nullvarA = PolygonOperations.GetAngle(allObjects.get(objectSelect)[2], allObjects.get(objectSelect)[3], allObjects.get(objectSelect)[4], allObjects.get(objectSelect)[5]);
		    		nullvarB = PolygonOperations.GetAngle(xcen, ycen, endX, endY) - nullvarA;
		    		//nullvarB = 0.5f*PolygonOperations.SideLength(allObjects.get(objectSelect)[2], allObjects.get(objectSelect)[3], allObjects.get(objectSelect)[4], allObjects.get(objectSelect)[5]);
		    		nullvarC = (float) Math.abs(Math.cos(nullvarB)) * PolygonOperations.SideLength(xcen, ycen, endX, endY);
	            	ScaleObject(objectSelect, nullvarC, nullvarA);
    			}
    		} else if ((modeChild.equals("Scale Gate")) & (GameInput.MBRELEASE==true) & (objectSelect != -1) & (tentry==1)) {
    			UpdateObject(objectSelect, "scalegate");
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
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			AddObject(ObjectVars.Gravity, tempx, tempy, 0.0f);
    		}
    		else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.Gravity, false, false);
    			if (objectSelect==0) objectSelect = -1; // Cannot delete the initial gravity
    			else engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Gravity, false, false);
    				startX = GameInput.MBDOWNX*scrscale;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move");
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Gravity, true, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotatearrow");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
    	} else if (modeParent.equals("Jewel")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				AddObject(ObjectVars.Jewel, tempx, tempy, -999.9f);
				numJewels += 1;
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Jewel, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Jewel, false, false);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move");
				objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Jewel, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotateobject");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Log")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				AddObject(ObjectVars.Log, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Log, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Log, false, true);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move");
				objectSelect = -1;
			}
		} else if (modeParent.equals("Nitrous")) {
    		if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
    			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			AddObject(ObjectVars.Nitrous, tempx, tempy, -999.9f);
    		} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
    			SelectObject("up", ObjectVars.Nitrous, false, false);
    			engageDelete = true;
    		} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Nitrous, false, false);
    				startX = GameInput.MBDOWNX*scrscale;
    				startY = GameInput.MBDOWNY;
    			} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "polygon", endX, endY);
    			}
    		} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
    			UpdateObject(objectSelect, "move");
    			objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Nitrous, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotateobject");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Spike")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				AddObject(ObjectVars.Spike, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Spike, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Spike, false, true);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move");
				objectSelect = -1;
			}
		} else if (modeParent.equals("Transport")) {
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				AddObject(ObjectVars.Transport, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectObject("up", ObjectVars.Transport, false, false);
				engageDelete = true;
			} else if ((modeChild.equals("Move Entry")) & (GameInput.MBDRAG==true)) {
				if (objectSelect == -1) {
					SelectObject("down", ObjectVars.Transport, false, false);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveObject(objectSelect, "moveentry", endX, endY);
				}
			} else if ((modeChild.equals("Move Entry")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "moveentry");
				objectSelect = -1;
			} else if ((modeChild.equals("Rotate Entry")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Transport, false, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotateentry");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
		} else if (modeParent.equals("Start")) {
			if ((modeChild.equals("Put")) & (GameInput.MBJUSTPRESSED)) {
				objectSelect = 1;
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
					endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
					MoveObject(objectSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move");
				objectSelect = -1;
    		} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (objectSelect == -1) {
    				SelectObject("down", ObjectVars.Start, true, false);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			UpdateObject(objectSelect, "rotatearrow");
    			objectSelect = -1;
            	GameInput.MBRELEASE=false;
    		}
    	} else if (modeParent.equals("Finish")) {
			if ((modeChild.equals("Put")) & (GameInput.MBJUSTPRESSED)) {
				objectSelect = finishObjNumber;
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
					endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
					MoveObject(objectSelect, "polygon", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (objectSelect != -1)) {
				UpdateObject(objectSelect, "move");
				objectSelect = -1;
			}
    	}
	}

	public void ControlMode6() {
		if (listChild.getSelected() == null) return;
		modeChild = listChild.getSelected().toString();
		if (modeParent.startsWith("Sign")) {
			int objNum = DecorVars.GetObjectNumber(modeParent);
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)){
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				AddDecor(objNum, tempx, tempy, -999.9f);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
				tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				SelectDecor("up", objNum, false, true);
				engageDelete = true;
			} else if ((modeChild.equals("Move")) & (GameInput.MBDRAG==true)) {
				if (decorSelect == -1) {
					SelectDecor("down", objNum, false, true);
					startX = GameInput.MBDOWNX*scrscale;
					startY = GameInput.MBDOWNY;
				} else {
					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
	            	MoveDecor(decorSelect, "circle", endX, endY);
				}
			} else if ((modeChild.equals("Move")) & (GameInput.MBJUSTPRESSED==true) & (decorSelect != -1)) {
				UpdateDecor(decorSelect, "movecircle");
				decorSelect = -1;
			} else if ((modeChild.equals("Rotate")) & (GameInput.MBDRAG==true)) {
    			if (decorSelect == -1) {
    				SelectDecor("down", objNum, true, true);
    				startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    				startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
    			} else {
					endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		    		endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    		}
		} else if (modeParent.equals("Grass")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
			if ((modeChild.equals("Add")) & (GameInput.MBJUSTPRESSED)) {
				AddDecor(DecorVars.Grass, tempx, tempy, -999.9f);
			} else if (modeChild.equals("Add")) {
				FindNearestSegment(true);
			} else if ((modeChild.equals("Delete")) & (GameInput.MBJUSTPRESSED)) {
				SelectDecor("up", DecorVars.Grass, false, false);
				engageDelete = true;
    		} else if (modeChild.equals("Move Vertex")) {
    			tempx = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			tempy = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
        		if (GameInput.MBDRAG==true) {
        			if (vertSelect == -1) {
        				FindNearestVertex(false);
        				startX = GameInput.MBDOWNX*scrscale;
        				startY = GameInput.MBDOWNY;
        			} else {
    					endX = cam.zoom*(GameInput.MBDRAGX*scrscale-startX)/BikeGame.SCALE;
    		    		endY = - cam.zoom*(GameInput.MBDRAGY-startY)/BikeGame.SCALE;
    	            	MoveVertex(polySelect, vertSelect, endX, endY);
        			}
        		} else if ((GameInput.MBJUSTPRESSED==true) & (polySelect != -1) & (vertSelect != -1)) {
        			UpdateDecor(polySelect, "move");
        			polySelect = -1;
        			vertSelect = -1;
        		} else FindNearestVertex(true);			}
		} else if (modeParent.equals("Large Stone")) {

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
   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
			} else if (polySelect != -1) {
    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
				updatePoly = allPolygons.get(polySelect).clone();
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
				SelectGroupPolygons();
				if (groupPolySelect.size() == 0) {
					warnMessage[warnNumber] = "No polygons inside selection box";
					warnElapse[warnNumber] = 0.0f;
					warnType[warnNumber] = 2;
					warnNumber += 1;
				} else {
   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
			} else if (groupPolySelect.size() != 0) {
    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			updatePoly = null;
			}
		} else if (modeParent.equals("Object")) {
			float shiftX = 0.0f;
			float shiftY = 0.0f;
			if ((GameInput.MBJUSTPRESSED==true) & (objectSelect == -1)) {
   				SelectObjectAny("up");
   				if (objectSelect != -1) {
   	   				startX = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
   	   				startY = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
    			endX = cam.position.x + cam.zoom*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
    			endY = cam.position.y - cam.zoom*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
	    		if (doX) shiftX += (endX-startX);
	    		if (doY) shiftY += (endY-startY);
    			MoveObjectCopy(objectSelect, shiftX, shiftY);
			}
		}
	}

	public void UncheckButtons(boolean keybrd) {
		//buttonLoad.setChecked(false);
		buttonSave.setChecked(false);
		buttonExit.setChecked(false);
		buttonExecute.setChecked(false);
		buttonPan.setChecked(false);
		buttonLevelProp.setChecked(false);
		buttonAddStatic.setChecked(false);
		buttonAddKinetic.setChecked(false);
		buttonAddFalling.setChecked(false);
		buttonCopyPaste.setChecked(false);
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
		vertHover = -1;
		segmHover = -1;
		updatePathVertex = null;
		tentry = 0;	
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
				listChild.setItems(nullList);
				break;
			case 2 :
				if (modeParent.equals("Collect Jewels")) {
					jewelNumber = new String[numJewels+1];
					for (int i = 0; i<numJewels+1; i++) jewelNumber[i] = String.format("%d", i);
					listChild.setItems(jewelNumber);
					pLevelIndex = GetListIndex("Collect Jewels",levelPropList);
					if (Integer.parseInt(LevelVars.get(LevelVars.PROP_NUMJEWELS)) > numJewels) listChild.setSelectedIndex(0);
					else listChild.setSelectedIndex(Integer.parseInt(LevelVars.get(LevelVars.PROP_NUMJEWELS)));
				} else if (modeParent.equals("Gravity")) {
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
				} else listChild.setItems(nullList);
				break;
			case 3 :
				if (modeParent.equals("Polygon")) {
					listChild.setItems(itemsADMRSFv);
					pStaticIndex = GetListIndex("Polygon",itemsPRC);
				} else if (modeParent.equals("Rectangle")) {
					listChild.setItems("Add");
					pStaticIndex = GetListIndex("Rectangle",itemsPRC);
				} else if (modeParent.equals("Circle")) {
					listChild.setItems("Add");
					pStaticIndex = GetListIndex("Circle",itemsPRC);
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
					pObjectIndex = GetListIndex("Jewel",objectList);
				} else if (modeParent.equals("Door (blue)")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Door (blue)",objectList);
				} else if (modeParent.equals("Door (green)")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Door (green)",objectList);
				} else if (modeParent.equals("Door (red)")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Door (red)",objectList);
				} else if (modeParent.equals("Gate Switch")) {
					listChild.setItems("Add", "Delete", "Move Gate/Switch", "Rotate Gate/Switch", "Scale Gate", "Gate Open/Close", "Flip Switch");
					pObjectIndex = GetListIndex("Gate Switch",objectList);
				} else if (modeParent.equals("Gravity")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("",objectList);
				} else if (modeParent.equals("Jewel")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Jewel",objectList);
				} else if (modeParent.equals("Key (blue)")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Key (blue)",objectList);
				} else if (modeParent.equals("Key (green)")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Key (green)",objectList);
				} else if (modeParent.equals("Key (red)")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Key (red)",objectList);
				} else if (modeParent.equals("Log")) {
					listChild.setItems(itemsADM);
					pObjectIndex = GetListIndex("Log",objectList);
				} else if (modeParent.equals("Nitrous")) {
					listChild.setItems(itemsADMR);
					pObjectIndex = GetListIndex("Nitrous",objectList);
				} else if (modeParent.equals("Pendulum")) {
					listChild.setItems("Add", "Delete", "Move Ball", "Move Anchor");
					pObjectIndex = GetListIndex("Pendulum",objectList);
				} else if (modeParent.equals("Spike")) {
					listChild.setItems(itemsADM);
					pObjectIndex = GetListIndex("Spike",objectList);
				} else if (modeParent.equals("Transport")) {
					listChild.setItems("Add", "Delete", "Move Entry", "Rotate Entry");
					pObjectIndex = GetListIndex("Transport",objectList);
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
					listChild.setItems(itemsADMR);
				} else if (modeParent.equals("Grass")) {
					listChild.setItems("Add", "Delete", "Move Vertex");
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
				}
				break;
			case 8 :
				if (modeParent.equals("Platform")) {
					listChild.setItems(itemsXYonly);
				} else if (modeParent.equals("Object")) {
					listChild.setItems(itemsXYonly);
				}
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

    public void AddPolygon(float[] newPoly, int ptype, int psize) {
    	changesMade = true;
		allPolygons.add(newPoly);
		allPolygonTypes.add(ptype);
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
		} else allPolygonPaths.add(null);
	}

    public void AddVertex(int idx, int verti, int vertj, float startX, float startY) {
    	changesMade = true;
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
		allPolygonTypes.add(allPolygonTypes.get(idx));
		if (allPolygonPaths.get(idx)==null) {
			allPolygonPaths.add(null);
		} else {
			if ((allPolygonTypes.get(idx)==4) | (allPolygonTypes.get(idx)==5)) {
				float[] newArr = allPolygonPaths.get(idx).clone();
				newArr[2] += (newPoly[0]-allPolygons.get(idx)[0]);
				newArr[3] += (newPoly[1]-allPolygons.get(idx)[1]);
				allPolygonPaths.add(newArr.clone());
			} else {
				float xcenp = 0.0f, ycenp = 0.0f;
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
				float[] newArr = allPolygonPaths.get(idx).clone();
		       	for (int i = 0; i<(allPolygonPaths.get(idx).length-6)/2; i++) {
		       		newArr[6+2*i] += (xcenp-allPolygonPaths.get(idx)[4]);
		       		newArr[6+2*i+1] += (ycenp-allPolygonPaths.get(idx)[5]);
		    	}
				newArr[4] = xcenp;
				newArr[5] = ycenp;
				allPolygonPaths.add(newArr.clone());
			}
		} 
	}

    public void DeletePolygon(int idx) {
    	changesMade = true;
		allPolygons.remove(idx);
		allPolygonTypes.remove(idx);
		allPolygonPaths.remove(idx);
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
	}

    public void DeleteVertex(int idx, int vert) {
    	updatePoly = new float[allPolygons.get(idx).length - 2];
    	int cntr = 0;
    	for (int i = 0; i<allPolygons.get(idx).length/2; i++){
    		if (i != vert) {
    			updatePoly[2*cntr] = allPolygons.get(idx)[2*i];
    			updatePoly[2*cntr+1] = allPolygons.get(idx)[2*i+1];
    			cntr += 1;
    		}
    	}
		UpdatePolygon(idx);
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
	}
   
	public void DrawPolygon() {
		// Make sure the new point is valid (does not intersect other lines and is not out of bounds)
		tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		// If the point is acceptable, add it to the polygon or close the polygon
		if (polyDraw.size() >= 3) {
			if (Math.sqrt((tempx-polyDraw.get(0)[0])*(tempx-polyDraw.get(0)[0]) + (tempy-polyDraw.get(0)[1])*(tempy-polyDraw.get(0)[1])) < cam.zoom*BikeGame.V_WIDTH*scrscale*polyEndThreshold) {
				MakePolygon();
				if (mode == 3) AddPolygon(newPoly, 0, polyDraw.size());
				else if (mode == 4) AddPolygon(newPoly, 2, polyDraw.size());
				else if (mode == 7) AddPolygon(newPoly, 4, polyDraw.size());
		    	drawingPoly = false;
			} else { 
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
				if ((allPolygonTypes.get(j) != 0) & (allPolygonTypes.get(j) != 1)) continue;
			} else if (mode==4) {
				if ((allPolygonTypes.get(j) != 2) & (allPolygonTypes.get(j) != 3)) continue;
			} else if (mode==7) {
				if ((allPolygonTypes.get(j) != 4) & (allPolygonTypes.get(j) != 5)) continue;
			}
			if ((allPolygonTypes.get(j) == 0) | (allPolygonTypes.get(j) == 2) | (allPolygonTypes.get(j) == 4)) {
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
			} else if ((allPolygonTypes.get(j) == 1) | (allPolygonTypes.get(j) == 3) | (allPolygonTypes.get(j) == 5)) {
				// Do nothing
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
			// Then decorating (grass)
			for (int i = 0; i < allDecors.size(); i++) {
				if (allDecorTypes.get(i)==DecorVars.Grass) {
					for (int j = 0; j < allDecors.get(i).length/2; j++) {
						if (bestval == -1.0f) {
							bestval = (float) Math.sqrt((tempx-allDecors.get(i)[2*j])*(tempx-allDecors.get(i)[2*j]) + (tempy-allDecors.get(i)[2*j+1])*(tempy-allDecors.get(i)[2*j+1]));
							if (hover) {
								polyHover = i;
								vertHover = j;
							} else {
								polySelect = i;
								vertSelect = j;							
							}
						} else {
							tempval = (float) Math.sqrt((tempx-allDecors.get(i)[2*j])*(tempx-allDecors.get(i)[2*j]) + (tempy-allDecors.get(i)[2*j+1])*(tempy-allDecors.get(i)[2*j+1]));
							if (tempval < bestval) {
								bestval = (float) Math.sqrt((tempx-allDecors.get(i)[2*j])*(tempx-allDecors.get(i)[2*j]) + (tempy-allDecors.get(i)[2*j+1])*(tempy-allDecors.get(i)[2*j+1]));
								if (hover) {
									polyHover = i;
									vertHover = j;
								} else {
									polySelect = i;
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
				if ((mode == 3) | (mode == 4) | (mode == 7)) {
					if (((allPolygonTypes.get(i) == 0)&(mode==3)) | ((allPolygonTypes.get(i) == 2)&(mode==4)) | ((allPolygonTypes.get(i) == 4)&(mode==7))) {
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
					} else if (((allPolygonTypes.get(i) == 1)&(mode==3)) | ((allPolygonTypes.get(i) == 3)&(mode==4)) | ((allPolygonTypes.get(i) == 5)&(mode==7))) {
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
    	updatePoly = allPolygons.get(idx).clone();
    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) | ((allPolygonTypes.get(idx) == 2)&(mode==4)) | ((allPolygonTypes.get(idx) == 4)&(mode==7))) {
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
    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) | ((allPolygonTypes.get(idx) == 3)&(mode==4)) | ((allPolygonTypes.get(idx) == 5)&(mode==7))) {
    		// A circle shape --> Do nothing
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
    	updatePath = allPolygonPaths.get(idx).clone();
    	for (int i = 4; i<allPolygonPaths.get(idx).length; i++) {
    		if (i%2==0) updatePath[i] += shiftX;
    		else updatePath[i] += shiftY;
    	}
	}

    public void MovePolygon(int idx, float shiftX, float shiftY) {
    	updatePoly = allPolygons.get(idx).clone();
    	if ((mode == 3) | (mode == 4) | (mode == 7)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) | ((allPolygonTypes.get(idx) == 2)&(mode==4)) | ((allPolygonTypes.get(idx) == 4)&(mode==7))) {
	    		for (int i = 0; i<allPolygons.get(idx).length/2; i++){
	    			updatePoly[2*i] += shiftX;
	    			updatePoly[2*i+1] += shiftY;
	    		}
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) | ((allPolygonTypes.get(idx) == 3)&(mode==4)) | ((allPolygonTypes.get(idx) == 5)&(mode==7))) {
	    		updatePoly[0] += shiftX;
	    		updatePoly[1] += shiftY;
	    	}
    	}
	}

    public void MoveVertex(int idx, int vert, float shiftX, float shiftY) {
    	if (mode == 6) updatePoly = allDecors.get(idx).clone();
    	else updatePoly = allPolygons.get(idx).clone();
    	if (mode == 6) {
    		updatePoly[2*vert] += shiftX;
    		updatePoly[2*vert+1] += shiftY;    		
    	} else if ((mode == 3) | (mode == 7)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) | ((allPolygonTypes.get(idx) == 4)&(mode==7))) {
	    		updatePoly[2*vert] += shiftX;
	    		updatePoly[2*vert+1] += shiftY;
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) | ((allPolygonTypes.get(idx) == 5)&(mode==7))) {
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
    	if ((mode == 3) | (mode == 7)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) | ((allPolygonTypes.get(idx) == 4)&(mode==7))) {
	    		for (int i = 0; i<allPolygons.get(idx).length; i++){
	    			if (i%2==0) updatePoly[i] = cursposx + (allPolygons.get(idx)[i]-cursposx)*(float) Math.cos(angle) - (allPolygons.get(idx)[i+1]-cursposy)*(float) Math.sin(angle);
	    			else updatePoly[i] = cursposy + (allPolygons.get(idx)[i-1]-cursposx)*(float) Math.sin(angle) + (allPolygons.get(idx)[i]-cursposy)*(float) Math.cos(angle);
	    		}
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) | ((allPolygonTypes.get(idx) == 5)&(mode==7))) {
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
    	for (int i = 6; i<allPolygonPaths.get(idx).length; i++){
    		if (i%2==0) updatePath[i] = allPolygonPaths.get(idx)[4] + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[4])*(float) Math.cos(angle) - (allPolygonPaths.get(idx)[i+1]-allPolygonPaths.get(idx)[5])*(float) Math.sin(angle);
    		else updatePath[i] = allPolygonPaths.get(idx)[5] + (allPolygonPaths.get(idx)[i-1]-allPolygonPaths.get(idx)[4])*(float) Math.sin(angle) + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[5])*(float) Math.cos(angle);
    	}
	}

    public void ScalePolygon(int idx, float scale) {
    	if (scale < 0.0f) scale *= -1.0f;
    	updatePoly = allPolygons.get(idx).clone();
    	if ((mode == 3) | (mode==7)) {
	    	if (((allPolygonTypes.get(idx) == 0)&(mode==3)) | ((allPolygonTypes.get(idx) == 4)&(mode==7))) {
	    		for (int i = 0; i<allPolygons.get(idx).length; i++){
	    			if (i%2==0) {
	    				updatePoly[i] = cursposx + (allPolygons.get(idx)[i]-cursposx)*scale;
	    			} else {
	    				updatePoly[i] = cursposy + (allPolygons.get(idx)[i]-cursposy)*scale;
	    			}
	    		}
	    	} else if (((allPolygonTypes.get(idx) == 1)&(mode==3)) | ((allPolygonTypes.get(idx) == 5)&(mode==7))) {
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
    	for (int i = 6; i<allPolygonPaths.get(idx).length; i++) {
    		if (i%2==0) updatePath[i] = allPolygonPaths.get(idx)[4] + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[4])*scale;
    		else updatePath[i] = allPolygonPaths.get(idx)[5] + (allPolygonPaths.get(idx)[i]-allPolygonPaths.get(idx)[5])*scale;
    	}
	}

    public void SelectGroupPolygons() {
    	float x1, x2, y1, y2, tmp;
    	float[] meanxy = new float[2];
		x1 = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		y1 = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		x2 = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		y2 = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
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
		for (int i = 0; i<allPolygons.size(); i++){
			if (allPolygonTypes.get(i)%2 == 0) {
				meanxy = PolygonOperations.MeanXY(allPolygons.get(i).clone());
			} else if (allPolygonTypes.get(i)%2 == 1) {
				meanxy[0] = allPolygons.get(i)[0];
				meanxy[1] = allPolygons.get(i)[1];
			}
			if ((x1 < meanxy[0]) & (meanxy[0] < x2) & (y1 < meanxy[1]) & (meanxy[1] < y2)) {
				// Poly is inside selection
				groupPolySelect.add(i);
			}				
		}
    }

	public void SelectPolygon(String downup) {
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		} else if (downup.equals("up")){
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);			
		}
		boolean inside = false;
		for (int i = 0; i<allPolygons.size(); i++){
			if ((mode == 3) | (mode == 4) | (mode == 7)) {
				if (((allPolygonTypes.get(i) == 0)&(mode==3)) | ((allPolygonTypes.get(i) == 2)&(mode==4)) | ((allPolygonTypes.get(i) == 4)&(mode==7))) {
					inside = PolygonOperations.PointInPolygon(allPolygons.get(i).clone(),tempx,tempy);
				} else if (((allPolygonTypes.get(i) == 1)&(mode==3)) | ((allPolygonTypes.get(i) == 3)&(mode==4)) | ((allPolygonTypes.get(i) == 5)&(mode==7))) {
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
	}

	public void UpdatePolygon(int idx) {
		changesMade = true;
		newPoly = allPolygons.set(idx, updatePoly.clone());
		if (mode==7) {
			// Update the location of the sign for a falling platform
			updatePath = allPolygonPaths.get(polySelect).clone();
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
		}
		updatePoly = null;
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
	}

	public void CopyObject(float[] newObject, int idx, float shiftX, float shiftY) {
		changesMade = true;
		newCoord = allObjectCoords.get(idx).clone();
		newCoord[0] += shiftX;
		newCoord[1] += shiftY;
		allObjects.add(newObject);
		allObjectTypes.add(allObjectTypes.get(idx).intValue());
		allObjectCoords.add(newCoord.clone());
		if (allObjectArrows.get(idx)!=null) {
			newPoly = allObjectArrows.get(idx).clone();
			for (int i=0; i<newPoly.length/2; i++) {
				newPoly[2*i] += shiftX;
				newPoly[2*i+1] += shiftY;
			}
			allObjectArrows.add(newPoly.clone());
			newPoly=null;
		} else allObjectArrows.add(null);
	}

	public void DeleteObject(int idx) {
		changesMade = true;
		if (allObjectTypes.get(idx) == ObjectVars.Jewel) numJewels -= 1;
		allObjects.remove(idx);
		allObjectTypes.remove(idx);
		allObjectCoords.remove(idx);
		allObjectArrows.remove(idx);
		objectSelect = -1;
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
		} else if (otype == ObjectVars.Transport) {
			newPoly = new float[ObjectVars.objectTransport.length];
			for (int i = 0; i<ObjectVars.objectTransport.length/2; i++){
				newPoly[2*i] = ObjectVars.objectTransport[2*i] + xcen;
				newPoly[2*i+1] = ObjectVars.objectTransport[2*i+1] + ycen;
			}
		}
	}

	public void MoveObject(int idx, String mode, float shiftX, float shiftY) {
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
		} else if ((otype==ObjectVars.Transport) | (otype==ObjectVars.GateSwitch) | (otype==ObjectVars.Bridge)) {
			updatePoly = new float[8];
			updatePoly[0] = allObjects.get(idx)[0+8*(tentry-1)] + shiftX;
			updatePoly[1] = allObjects.get(idx)[1+8*(tentry-1)] + shiftY;
			updatePoly[2] = allObjects.get(idx)[2+8*(tentry-1)] + shiftX;
			updatePoly[3] = allObjects.get(idx)[3+8*(tentry-1)] + shiftY;
			updatePoly[4] = allObjects.get(idx)[4+8*(tentry-1)] + shiftX;
			updatePoly[5] = allObjects.get(idx)[5+8*(tentry-1)] + shiftY;
			updatePoly[6] = allObjects.get(idx)[6+8*(tentry-1)] + shiftX;
			updatePoly[7] = allObjects.get(idx)[7+8*(tentry-1)] + shiftY;			
			for (int i=0; i<8; i++) {
				newPoly[2*i] += shiftX;
				newPoly[2*i+1] += shiftY;
			}
		} else {
			if ((otype==ObjectVars.Boulder) | (otype==ObjectVars.Spike) | (otype==ObjectVars.Log)) {
				updatePoly = allObjects.get(idx).clone();
				updatePoly[0] += shiftX;
				updatePoly[1] += shiftY;
				newPoly[0] += shiftX;
				newPoly[1] += shiftY;
			} else {
				updatePoly = allObjects.get(idx).clone();
				for (int i = 0; i<allObjects.get(idx).length/2; i++){
					updatePoly[2*i] += shiftX;
					updatePoly[2*i+1] += shiftY;
					newPoly[2*i] += shiftX;
					newPoly[2*i+1] += shiftY;
				}
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

	public void SelectObject(String downup, int otype, boolean rotate, boolean circle) {
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		} else if (downup.equals("up")){
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);			
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
				} else if ((otype==ObjectVars.Transport) | (otype==ObjectVars.GateSwitch) | (otype==ObjectVars.Bridge)) {
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
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		} else if (downup.equals("up")){
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);			
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
			} else if ((otype==ObjectVars.Transport) | (otype==ObjectVars.GateSwitch) | (otype==ObjectVars.Bridge)) {
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

	public void UpdateObject(int idx, String mode) {
		changesMade = true;
		if (mode.equals("move")) {
			// Get the total shift
			float shiftX = updatePoly[0]-allObjects.get(idx)[0];
			float shiftY = updatePoly[1]-allObjects.get(idx)[1];
			newPoly = allObjects.set(idx, updatePoly.clone());
			// Update the Coordinate
			newCoord = allObjectCoords.get(idx);
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
			newCoord = allObjectCoords.get(idx);
			newCoord[0] += shiftX;
			newCoord[1] += shiftY;
			newPoly = allObjectCoords.set(idx, newCoord.clone());
		} else if ((mode.equals("moveentry")) | (mode.equals("rotateentry"))) {
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
	}

	public void DeleteDecor(int idx) {
		changesMade = true;
		allDecors.remove(idx);
		allDecorTypes.remove(idx);
		allDecorPolys.remove(idx);
		decorSelect = -1;
	}
  	
   
	public void MakeDecor(int otype, float xcen, float ycen, float angle) {
		angle *= MathUtils.PI/180.0;
		if (DecorVars.IsRoadSign(otype)) {
			newPoly = new float[DecorVars.decorCircleRoadSign.length];
			newPoly[0] = DecorVars.decorCircleRoadSign[0] + xcen;
			newPoly[1] = DecorVars.decorCircleRoadSign[1] + ycen;
			newPoly[2] = DecorVars.decorCircleRoadSign[2];
			newPoly[3] = DecorVars.decorCircleRoadSign[3];
		}
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
			if (PolygonOperations.PointInPolygon(allPolygons.get(polyHover), vecA.x*0.01f + allPolygons.get(polyHover)[2*segmHover], vecA.y*0.01f+ allPolygons.get(polyHover)[2*segmHover+1])) {
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
			warnMessage[warnNumber] = "Overlapping vertices in polygon! Delete one vertex before adding grass";
			warnElapse[warnNumber] = 0.0f;
			warnType[warnNumber] = 2;
			warnNumber += 1;
			warnMessage[warnNumber] = "Grass applied to the entire polygon";
			warnElapse[warnNumber] = 0.0f;
			warnType[warnNumber] = 1;
			warnNumber += 1;
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
			if (PolygonOperations.PointInPolygon(allPolygons.get(polyHover), vecA.x*0.01f + allPolygons.get(polyHover)[2*segmNext], vecA.y*0.01f + allPolygons.get(polyHover)[2*segmNext+1])) {
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
			warnMessage[warnNumber] = "Overlapping vertices in polygon! Delete one vertex before adding grass";
			warnElapse[warnNumber] = 0.0f;
			warnType[warnNumber] = 2;
			warnNumber += 1;
			warnMessage[warnNumber] = "Grass applied to the entire polygon";
			warnElapse[warnNumber] = 0.0f;
			warnType[warnNumber] = 1;
			warnNumber += 1;
		}
	}

	public void MoveDecor(int idx, String mode, float shiftX, float shiftY) {
		if (mode.equals("polygon")) {
			updatePoly = allDecors.get(idx).clone();
			for (int i = 0; i<allDecors.get(idx).length/2; i++){
				updatePoly[2*i] += shiftX;
				updatePoly[2*i+1] += shiftY;
			}
		} else if (mode.equals("circle")) {
			updatePoly = allDecors.get(idx).clone();
			updatePoly[0] += shiftX;
			updatePoly[1] += shiftY;
		}
	}
	
	public void RotateDecor(int idx, String mode, float angle) {
		if (mode.equals("object")) {
			updatePoly = allDecors.get(idx).clone();
			for (int i = 0; i<allDecors.get(idx).length; i++){
				if (i%2==0) {
					updatePoly[i] = cursposx + (allDecors.get(idx)[i]-cursposx)*(float) Math.cos(angle) - (allDecors.get(idx)[i+1]-cursposy)*(float) Math.sin(angle);
				} else {
					updatePoly[i] = cursposy + (allDecors.get(idx)[i-1]-cursposx)*(float) Math.sin(angle) + (allDecors.get(idx)[i]-cursposy)*(float) Math.cos(angle);
				}
			}
    	} else if (mode.equals("roadsign")) {
    		updatePoly = allDecors.get(idx).clone();
    		updatePoly[3] = angle;
    	}
	}

	public void SelectDecor(String downup, int otype, boolean rotate, boolean circle) {
		if (downup.equals("down")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		} else if (downup.equals("up")) {
			tempx = cam.position.x + cam.zoom*(GameInput.MBUPX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
			tempy = cam.position.y - cam.zoom*(GameInput.MBUPY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);			
		}
		boolean inside = false;
		decorSelect = -1;
		for (int i = 0; i<allDecors.size(); i++) {
			if (allDecorTypes.get(i) == otype) {
				if (!circle) {
					inside = PolygonOperations.PointInPolygon(allDecors.get(i),tempx,tempy);					
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
		}
		// Nullify the update Polygon
		updatePoly = null;
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
		//startX = cam.position.x + cam.zoom*(GameInput.MBDOWNX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH);
		//startY = cam.position.y - cam.zoom*(GameInput.MBDOWNY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		//endX = cam.position.x + cam.zoom*(GameInput.MBDRAGX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH);
		//endY = cam.position.y - cam.zoom*(GameInput.MBDRAGY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		posx = cam.zoom*(GameInput.MBDOWNX-GameInput.MBDRAGX)*scrscale/BikeGame.SCALE;
		posy = cam.zoom*(GameInput.MBDRAGY-GameInput.MBDOWNY)/BikeGame.SCALE;
		if ((cam.position.x>(maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale) & (cam.position.x<(maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale))) {
			cam.translate(posx,0,0);
		}
		if ((cam.position.y>(maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT) & (cam.position.y<(maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT))) {
			cam.translate(0,posy,0);
		}
		// Make sure that we are within bounds
		if ((cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale) & (cam.position.x > (maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale))){
		} else if (cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale) {
			cam.translate((maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale-cam.position.x,0,0);
		} else if (cam.position.x > (maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale)) {
			cam.translate((maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale)-cam.position.x,0,0);
		}
		if ((cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT) & (cam.position.y > (maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT))) {
		} else if (cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT) {
			cam.translate(0,(maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT-cam.position.y,0);
		} else if (cam.position.y > (maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT)) {
			cam.translate(0,(maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT)-cam.position.y,0);
		}
		// Update the cursor position
	    GameInput.MBDOWNX = GameInput.MBDRAGX;
	    GameInput.MBDOWNY = GameInput.MBDRAGY;
	}


	private void ControlZoom() {
		// Perform Zoom
		scrwidth = cam.zoom*BikeGame.V_WIDTH*scrscale;
		scrheight = cam.zoom*BikeGame.V_HEIGHT;
		if ((scrwidth > maxzoom) & (scrheight > maxzoom)) {
			cam.zoom = Math.max(maxzoom/(BikeGame.V_WIDTH*scrscale), maxzoom/BikeGame.V_HEIGHT);
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
		posx = - nullvarA*(GameInput.MBMOVEX/BikeGame.SCALE - 0.5f*BikeGame.V_WIDTH)*scrscale;
		posy = nullvarA*(GameInput.MBMOVEY/BikeGame.SCALE - 0.5f*BikeGame.V_HEIGHT);
		if ((cam.position.x>(maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale) & (cam.position.x<(maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale))) {
			cam.translate(posx,0,0);
		}
		if ((cam.position.y>(maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT) & (cam.position.y<(maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT))) {
			cam.translate(0,posy,0);
		}
		// Apply new zoom
		cam.zoom += nullvarA;

		// Perform corrections if the zoom takes us out of bounds
		if ((cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale) & (cam.position.x > (maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale))) {
		} else if (cam.position.x < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale) {
			cam.translate((maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale-cam.position.x,0,0);
		} else if (cam.position.x > (maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale)) {
			cam.translate((maxzoom-0.5f*cam.zoom*BikeGame.V_WIDTH*scrscale)-cam.position.x,0,0);
		}
		if ((cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT) & (cam.position.y > (maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT))) {
		} else if (cam.position.y < (maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT) {
			cam.translate(0,(maxsize-maxzoom)+0.5f*cam.zoom*BikeGame.V_HEIGHT-cam.position.y,0);
		} else if (cam.position.y > (maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT)) {
			cam.translate(0,(maxzoom-0.5f*cam.zoom*BikeGame.V_HEIGHT)-cam.position.y,0);
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