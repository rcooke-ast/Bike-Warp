/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BikeGameSounds;

/**
 *
 * @author rcooke
 */
public class GameContactListener implements ContactListener {

	public boolean playerDead;
	private boolean hasRedKey=false, hasGreenKey=false, hasBlueKey=false;
	private boolean finishTouch;
	private int bikeGroundL=0;
	private int bikeGroundR=0;
	private Array<Body> bodiesToRemove;
	private Array<Body> jointsToRemove; // This is indexed as a body, so that we get the body the joints are associated with
	private Array<Body> bikeBodyCollide;
	private Array<Body> transportBody;
	private Array<Body> switchBody;

	public GameContactListener() {
		super();
		bodiesToRemove = new Array<Body>();
		jointsToRemove = new Array<Body>();
		bikeBodyCollide = new Array<Body>();
		transportBody = new Array<Body>();
		switchBody = new Array<Body>();
	}

    @Override
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null) return;
        short bitA = fa.getFilterData().categoryBits;
        short bitB = fb.getFilterData().categoryBits;

        // Head/Wheel Collision -> Finish
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_FINISH)) |
        		((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_FINISH)) ) {
        	finishTouch = true;
        } else if ( ((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_FINISH)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_FINISH)) ) {
        	finishTouch = true;
        }
        // Head Collision -> Dead
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_GROUND)) |
        		((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_SPIKE)) |
        		((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_GROUND)) |
        		((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_SPIKE))) {
        	playerDead = true;
        }
        // Tire Collision -> Dead
        if ( ((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_SPIKE)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_SPIKE)) ){
        	playerDead = true;
        }
        // Head/Wheel Collision -> Jewel
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_JEWEL)) |
        		((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_JEWEL)) ) {
        	bodiesToRemove.add(fb.getBody());
        } else if ( ((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_JEWEL)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_JEWEL)) ) {
        	bodiesToRemove.add(fa.getBody());
        }
        // Head/Wheel Collision -> Key
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_KEY)) |
        		((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_KEY)) ) {
        	bodiesToRemove.add(fb.getBody());
        } else if ( ((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_KEY)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_KEY)) ) {
        	bodiesToRemove.add(fa.getBody());
        }
        // Head/Wheel Collision -> Gravity
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_GRAVITY)) |
        		((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_GRAVITY)) ) {
        	bodiesToRemove.add(fb.getBody());
        } else if ( ((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_GRAVITY)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_GRAVITY)) ) {
        	bodiesToRemove.add(fa.getBody());
        }
        // Head/Wheel/Ground Collision -> Switch
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_SWITCH)) |
        		((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_SWITCH)) |
        		((bitA == B2DVars.BIT_GROUND) & (bitB == B2DVars.BIT_SWITCH))) {
        	switchBody.add(fb.getBody());
        	bikeBodyCollide.add(fa.getBody());
        } else if ( ((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_SWITCH)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_SWITCH)) |
        		((bitB == B2DVars.BIT_GROUND) & (bitA == B2DVars.BIT_SWITCH))) {
        	switchBody.add(fa.getBody());
        	bikeBodyCollide.add(fb.getBody());
        }
        // Head/Wheel Collision -> Transport
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_TRANSPORT)) |
        		((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_TRANSPORT)) ) {
        	transportBody.add(fb.getBody());
        } else if ( ((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_TRANSPORT)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_TRANSPORT)) ) {
        	transportBody.add(fa.getBody());
        }
        // Now deal with anything having User Data
        if (fa.getUserData()==null || fb.getUserData()==null) return;
        // Tire Collision -> Ground
        if ((fa.getUserData().equals("LeftWheel"))|(fb.getUserData().equals("LeftWheel"))) {
        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeGroundL++;
        } else if ((fa.getUserData().equals("RightWheel"))|(fb.getUserData().equals("RightWheel"))) {
        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeGroundR++;
        }
        // Tire Collision -> Falling Ground
        if ((fa.getUserData().equals("LeftWheel"))&(fb.getUserData().equals("GroundFall"))) {
        	if (fb.getBody().getJointList().size != 0) jointsToRemove.add(fb.getBody());
        } else if ((fb.getUserData().equals("LeftWheel"))&(fa.getUserData().equals("GroundFall"))) {
        	if (fa.getBody().getJointList().size != 0) jointsToRemove.add(fa.getBody());
        } else if ((fa.getUserData().equals("RightWheel"))&(fb.getUserData().equals("GroundFall"))) {
        	if (fb.getBody().getJointList().size != 0) jointsToRemove.add(fb.getBody());
        } else if ((fb.getUserData().equals("RightWheel"))&(fa.getUserData().equals("GroundFall"))) {
        	if (fa.getBody().getJointList().size != 0) jointsToRemove.add(fa.getBody());
        }

    }

    @Override
    public void endContact(Contact c) {
    	Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        
    	if(fa == null || fb == null) return;
        short bitA = fa.getFilterData().categoryBits;
        short bitB = fb.getFilterData().categoryBits;

        // Now deal with anything having User Data
        if (fa.getUserData()==null || fb.getUserData()==null) return;
        // Tire Collision -> Ground
        if ((fa.getUserData().equals("LeftWheel"))|(fb.getUserData().equals("LeftWheel"))) {
        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeGroundL--;
        } else if ((fa.getUserData().equals("RightWheel"))|(fb.getUserData().equals("RightWheel"))) {
        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeGroundR--;
        }
    }

    @Override
    public void preSolve(Contact c, Manifold m) {
    	Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null) return;
        
        short bitA = fa.getFilterData().categoryBits;
        short bitB = fb.getFilterData().categoryBits;

        // Head/Wheel Collision -> Door
        if ( ((bitA == B2DVars.BIT_HEAD) & (bitB == B2DVars.BIT_DOOR)) |
        		((bitA == B2DVars.BIT_WHEEL) & (bitB == B2DVars.BIT_DOOR)) ) {
        	bodiesToRemove.add(fb.getBody());
        	bikeBodyCollide.add(fa.getBody());
        	if (fb.getUserData().equals("DoorRed") & (hasRedKey)) c.setEnabled(false);
        	else if (fb.getUserData().equals("DoorGreen") & (hasGreenKey)) c.setEnabled(false);
        	else if (fb.getUserData().equals("DoorBlue") & (hasBlueKey)) c.setEnabled(false);
        } else if ( ((bitB == B2DVars.BIT_HEAD) & (bitA == B2DVars.BIT_DOOR)) |
        		((bitB == B2DVars.BIT_WHEEL) & (bitA == B2DVars.BIT_DOOR)) ) {
        	bodiesToRemove.add(fa.getBody());
        	bikeBodyCollide.add(fb.getBody());
        	if (fa.getUserData().equals("DoorRed") & (hasRedKey)) c.setEnabled(false);
        	else if (fa.getUserData().equals("DoorGreen") & (hasGreenKey)) c.setEnabled(false);
        	else if (fa.getUserData().equals("DoorBlue") & (hasBlueKey)) c.setEnabled(false);
        }
        // Tire Collision -> Ground
//        if ((fa.getUserData().equals("LeftWheel"))|(fb.getUserData().equals("LeftWheel"))) {
//        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeStartGroundL++;
//        } else if ((fa.getUserData().equals("RightWheel"))|(fb.getUserData().equals("RightWheel"))) {
//        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeStartGroundR++;
//        }
    }

    @Override
    public void postSolve(Contact c, ContactImpulse ci) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null) return;
        
        short bitA = fa.getFilterData().categoryBits;
        short bitB = fb.getFilterData().categoryBits;

        // Tire Collision -> Ground
//        if ((fa.getUserData().equals("LeftWheel"))|(fb.getUserData().equals("LeftWheel"))) {
//        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeStartGroundL--;
//        } else if ((fa.getUserData().equals("RightWheel"))|(fb.getUserData().equals("RightWheel"))) {
//        	if ((bitA == B2DVars.BIT_GROUND) | (bitB == B2DVars.BIT_GROUND)) bikeStartGroundR--;
//        }
    }

    public Array<Body> getBodies() { return bodiesToRemove; }
    public Array<Body> getJoints() { return jointsToRemove; }
    public Array<Body> getTransportBody() { return transportBody; }
    public Array<Body> getSwitchBody() { return switchBody; }
    public Array<Body> getBikeBodyCollide() { return bikeBodyCollide; }
    public void clearTransportBody() { transportBody.clear(); }
    public void clearBikeBodyCollide() { bikeBodyCollide.clear(); }
    public boolean isPlayerDead() { return playerDead; }
    public boolean isBikeOnGround() { return ((bikeGroundL>0)&(bikeGroundR>0)); }
    public boolean isFinished() { return finishTouch; }
    public void notFinished() { finishTouch=false; }
    public void setKey(int i, boolean val) { 
    	if (i==0) hasRedKey=val;
    	else if (i==1) hasGreenKey=val;
    	else if (i==2) hasBlueKey=val;
    }
}
