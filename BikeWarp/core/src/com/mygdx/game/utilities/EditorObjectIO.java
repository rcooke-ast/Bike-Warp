package com.mygdx.game.utilities;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utilities.PolygonOperations;
import com.mygdx.game.utilities.json.JSONException;
import com.mygdx.game.utilities.json.JSONStringer;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.DecorVars;
import com.mygdx.game.handlers.LevelVars;
import com.mygdx.game.handlers.ObjectVars;

public class EditorObjectIO {

	public static int AddBallChain(JSONStringer json, float[] fs, float friction, int cnt) throws JSONException {
		// Body order: Ball, Anchor, Shaft
		json.object(); // Start of Ball body
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(2500.0);
    	json.key("friction").value(friction);
    	json.key("restitution").value(0.1);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_SWITCH);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(B2DVars.EPPM*fs[2]);
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the boulder body
		json.key("linearVelocity").value(0);
		json.key("name").value("BallChainBoulder"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*fs[0]);
		json.key("y").value(B2DVars.EPPM*fs[1]);
		json.endObject();
		json.key("type").value(2);
		json.endObject(); // End of Ball
		json.object(); // Start of Anchor
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(2500.0);
    	json.key("friction").value(friction);
    	json.key("restitution").value(1.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_SWITCH);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(-B2DVars.EPPM*fs[5]*0.5f);
       	json.value(B2DVars.EPPM*fs[5]*0.5f);
       	json.value(B2DVars.EPPM*fs[5]*0.5f);
       	json.value(-B2DVars.EPPM*fs[5]*0.5f);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(-B2DVars.EPPM*fs[6]*0.5f);
       	json.value(-B2DVars.EPPM*fs[6]*0.5f);
       	json.value(B2DVars.EPPM*fs[6]*0.5f);
       	json.value(B2DVars.EPPM*fs[6]*0.5f);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the anchor body
		json.key("linearVelocity").value(0);
		json.key("name").value("BallChainAnchor"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*(fs[3]+fs[5]*0.5f));
		json.key("y").value(B2DVars.EPPM*(fs[4]+fs[6]*0.5f));
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Anchor
		float shaftMinLength = B2DVars.EPPM * (float) Math.sqrt( Math.pow(fs[0]-(fs[3]+0.5f*fs[5]),2) + Math.pow(fs[1]-(fs[4]+0.5f*fs[6]),2) );
		float shaftMaxLength = B2DVars.EPPM * fs[7];
		// Calculate the number of links that will be needed
		float imageScale = 400.0f/53.0f; // image width / image height
		float bodyLength = ObjectVars.ChainLinkSize;
		float linkLength = bodyLength - bodyLength/imageScale; // Link length in metres
		float tmpLinks = shaftMaxLength/linkLength; // shaftMaxLength should be specified in metres
		int nLinks = (int) (tmpLinks);
		if (nLinks==0) nLinks = 2; // Make sure there are not zero links
		if (nLinks%2 == 1) nLinks += 1; // Make sure there are an even number of links
		float sFact = tmpLinks / (float) (nLinks);
		linkLength *= sFact;
		bodyLength = linkLength/(1.0f - 1.0f/imageScale);
		float rotAngle = MathUtils.radiansToDegrees * (float) Math.acos(shaftMinLength/((float)(nLinks) * linkLength));
		float rotAngleBA;
		float halfWidth = 0.5f*bodyLength/imageScale;
		// Setup the odd and even chains
		float[] xshaftOdd = {-halfWidth, -halfWidth, halfWidth, halfWidth};
		float[] yshaftOdd = {-halfWidth*imageScale, halfWidth*imageScale, halfWidth*imageScale, -halfWidth*imageScale};
		float[] xshaftEven = {-halfWidth, -halfWidth, halfWidth, halfWidth};
		float[] yshaftEven = {-halfWidth*imageScale, halfWidth*imageScale, halfWidth*imageScale, -halfWidth*imageScale};
		float cxEven = 0.0f;
		float cyEven = -halfWidth*imageScale + halfWidth;
		float[] cCoord;
		// Rotate the odd array
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngle,0.0f,0.0f);
		// Rotate the even array
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,-rotAngle,0.0f,0.0f);
		cCoord = PolygonOperations.RotateCoordinate(cxEven,cyEven,-rotAngle,0.0f,0.0f);
		cxEven = cCoord[0];
		cyEven = cCoord[1];
		// Determine the ball-anchor axis rotation
		if (fs[0] == fs[3]+fs[5]*0.5f) rotAngleBA = 0.0f; // No rotation needed
		else rotAngleBA = 90.0f + MathUtils.radiansToDegrees*(float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
		if (fs[0] < fs[3]+fs[5]*0.5f) rotAngleBA += 180.0f; 
		// Rotate both arrays to lie on the Ball-Anchor axis
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngleBA,0.0f,0.0f);
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,rotAngleBA,0.0f,0.0f);
		float xcen, ycen;
		float chainShift = (float) (2.0f*linkLength*Math.cos(MathUtils.degreesToRadians*rotAngle));
		float iMult = 0.0f;
		float[] xshaft = new float[4], yshaft = new float[4];
		for (int i = 0; i < nLinks; i++) {
			// First set xshaft and yshaft
			if (i%2 == 0) {
				xshaft[0] = xshaftEven[0];
				xshaft[1] = xshaftEven[1];
				xshaft[2] = xshaftEven[2];
				xshaft[3] = xshaftEven[3];
				yshaft[0] = yshaftEven[0];
				yshaft[1] = yshaftEven[1];
				yshaft[2] = yshaftEven[2];
				yshaft[3] = yshaftEven[3];
				// Shift so that pivot is at the centre of the ball
				xcen = -cxEven;
				ycen = -cyEven;
				// Shift the link into it's position along the chain
				ycen += iMult*chainShift;
				// Now rotate this coordinate system into position
				cCoord = PolygonOperations.RotateCoordinate(xcen,ycen,rotAngleBA,0.0f,0.0f);
				xcen = cCoord[0];
				ycen = cCoord[1];
			} else {
				xshaft[0] = xshaftOdd[0];
				xshaft[1] = xshaftOdd[1];
				xshaft[2] = xshaftOdd[2];
				xshaft[3] = xshaftOdd[3];
				yshaft[0] = yshaftOdd[0];
				yshaft[1] = yshaftOdd[1];
				yshaft[2] = yshaftOdd[2];
				yshaft[3] = yshaftOdd[3];
				// Shift so that pivot is at the centre of the ball
				xcen = -cxEven;
				ycen = -cyEven;
				// Shift the odd link to join with the previous even link
				ycen += 0.5f*chainShift;
				// Shift the link into it's position along the chain
				ycen += iMult*chainShift;
				// Now rotate this coordinate system into position
				cCoord = PolygonOperations.RotateCoordinate(xcen,ycen,rotAngleBA,0.0f,0.0f);
				xcen = cCoord[0];
				ycen = cCoord[1];
				// Add one to iMult for the next even link
				iMult += 1.0f;
			}
			// Add this chain link to the json object
			json.object(); // Start of Chain Link
	        json.key("angle").value(0);
	        json.key("angularVelocity").value(0);
	        json.key("awake").value(true);
	        json.key("fixedRotation").value(false);
	        // Add the fixtures
	        json.key("fixture");
	        json.array();
	        json.object();
	    	json.key("density").value(7550.0);
	    	json.key("friction").value(0.0);
	    	json.key("restitution").value(0.8);
	        json.key("name").value("fixture8");
	        json.key("filter-categoryBits").value(B2DVars.BIT_CHAIN);
	        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR);
			json.key("polygon");
	        json.object(); // Begin polygon object
	        json.key("vertices");
	        json.object(); // Begin vertices object
	        json.key("x");
	        json.array();
	        for (int j = 0; j < xshaft.length; j++) json.value(xshaft[j]);
	        json.endArray();
	        json.key("y");
	        json.array();
	        for (int j = 0; j < yshaft.length; j++) json.value(yshaft[j]);
	       	json.endArray();
	        json.endObject(); // End the vertices object
	        json.endObject(); // End polygon object
	        json.endObject(); // End this fixture
	        json.endArray(); // End the array of fixtures
	        // Add some final properties for the link body
			json.key("linearVelocity").value(0);
			json.key("name").value("BallChain"+cnt+"_Link"+(i+1));
			json.key("position");
			json.object();
			json.key("x").value(B2DVars.EPPM*fs[0] + xcen);
			json.key("y").value(B2DVars.EPPM*fs[1] + ycen);
			json.endObject();
			json.key("type").value(2);
			json.endObject(); // End of Chain Link
		}
		return nLinks+2; // Return number of bodies added
	}

	public static int AddBoulder(JSONStringer json, float[] fs, int cnt) throws JSONException {
		json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(2500.0);
    	json.key("friction").value(0.2);
    	json.key("restitution").value(0.2);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_SWITCH);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(B2DVars.EPPM*fs[2]);
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the boulder body
		json.key("linearVelocity").value(0);
		json.key("name").value("Boulder"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*fs[0]);
		json.key("y").value(B2DVars.EPPM*fs[1]);
		json.endObject();
		json.key("type").value(2);
		json.endObject();
		return 1; // Return number of bodies added
	}

	public static int AddBridge(JSONStringer json, float[] fs, float friction, int cnt) throws JSONException {
		float xcen, ycen;
		// Body order: Anchor, Anchor, Shaft
		json.object(); // Start of first anchor
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(1000);
    	json.key("friction").value(friction);
    	json.key("restitution").value(0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
        json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
        xcen = 0.5f*(fs[0]+fs[4]);
        ycen = 0.5f*(fs[1]+fs[5]);
       	json.value(B2DVars.EPPM*(fs[0]-xcen));
       	json.value(B2DVars.EPPM*(fs[2]-xcen));
       	json.value(B2DVars.EPPM*(fs[4]-xcen));
       	json.value(B2DVars.EPPM*(fs[6]-xcen));
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*(fs[1]-ycen));
       	json.value(B2DVars.EPPM*(fs[3]-ycen));
       	json.value(B2DVars.EPPM*(fs[5]-ycen));
       	json.value(B2DVars.EPPM*(fs[7]-ycen));
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the boulder body
		json.key("linearVelocity").value(0);
		json.key("name").value("BridgeLAnchor"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*xcen);
		json.key("y").value(B2DVars.EPPM*ycen);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of First Anchor
		json.object(); // Start of Second Anchor
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(1000.0);
    	json.key("friction").value(friction);
    	json.key("restitution").value(0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
        xcen = 0.5f*(fs[8]+fs[12]);
        ycen = 0.5f*(fs[9]+fs[13]);
       	json.value(B2DVars.EPPM*(fs[8]-xcen));
       	json.value(B2DVars.EPPM*(fs[10]-xcen));
       	json.value(B2DVars.EPPM*(fs[12]-xcen));
       	json.value(B2DVars.EPPM*(fs[14]-xcen));
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*(fs[9]-ycen));
       	json.value(B2DVars.EPPM*(fs[11]-ycen));
       	json.value(B2DVars.EPPM*(fs[13]-ycen));
       	json.value(B2DVars.EPPM*(fs[15]-ycen));
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the anchor body
		json.key("linearVelocity").value(0);
		json.key("name").value("BridgeRAnchor"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*xcen);
		json.key("y").value(B2DVars.EPPM*ycen);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Second Anchor
		float shaftMinLength = B2DVars.EPPM * (float) Math.sqrt( Math.pow(xcen-0.5f*(fs[0]+fs[4]),2) + Math.pow(ycen-0.5f*(fs[1]+fs[5]),2) );
		float shaftMaxLength = (float) Math.sqrt( shaftMinLength*shaftMinLength + B2DVars.EPPM*B2DVars.EPPM*fs[16]*fs[16] );
		// Calculate the number of links that will be needed
		float imageScale = 400.0f/53.0f; // image width / image height
		float bodyLength = ObjectVars.ChainLinkSize;
		float linkLength = bodyLength - bodyLength/imageScale; // Link length in metres
		float tmpLinks = shaftMaxLength/linkLength; // shaftMaxLength should be specified in metres
		int nLinks = (int) (tmpLinks);
		if (nLinks==0) nLinks = 2; // Make sure there are not zero links
		if (nLinks%2 == 1) nLinks += 1; // Make sure there are an even number of links
		float sFact = tmpLinks / (float) (nLinks);
		linkLength *= sFact;
		bodyLength = linkLength/(1.0f - 1.0f/imageScale);
		float rotAngle = MathUtils.radiansToDegrees * (float) Math.acos(shaftMinLength/((float)(nLinks) * linkLength));
		float rotAngleBA;
		float halfWidth = 0.5f*bodyLength/imageScale;
		// Setup the odd and even chains
		float[] xshaftOdd = {-halfWidth, -halfWidth, halfWidth, halfWidth};
		float[] yshaftOdd = {-halfWidth*imageScale, halfWidth*imageScale, halfWidth*imageScale, -halfWidth*imageScale};
		float[] xshaftEven = {-halfWidth, -halfWidth, halfWidth, halfWidth};
		float[] yshaftEven = {-halfWidth*imageScale, halfWidth*imageScale, halfWidth*imageScale, -halfWidth*imageScale};
		float cxEven = 0.0f;
		float cyEven = -halfWidth*imageScale + halfWidth;
		float[] cCoord;
		// Rotate the odd array
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngle,0.0f,0.0f);
		// Rotate the even array
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,-rotAngle,0.0f,0.0f);
		cCoord = PolygonOperations.RotateCoordinate(cxEven,cyEven,-rotAngle,0.0f,0.0f);
		cxEven = cCoord[0];
		cyEven = cCoord[1];
		// Determine the ball-anchor axis rotation
		if (fs[0] == fs[3]+fs[5]*0.5f) rotAngleBA = 0.0f; // No rotation needed
		else rotAngleBA = 90.0f + MathUtils.radiansToDegrees*(float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
		if (fs[0] < fs[3]+fs[5]*0.5f) rotAngleBA += 180.0f; 
		// Rotate both arrays to lie on the Ball-Anchor axis
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngleBA,0.0f,0.0f);
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,rotAngleBA,0.0f,0.0f);
		float chainShift = (float) (2.0f*linkLength*Math.cos(MathUtils.degreesToRadians*rotAngle));
		float iMult = 0.0f;
		float[] xshaft = new float[4], yshaft = new float[4];
		for (int i = 0; i < nLinks; i++) {
			// First set xshaft and yshaft
			if (i%2 == 0) {
				xshaft[0] = xshaftEven[0];
				xshaft[1] = xshaftEven[1];
				xshaft[2] = xshaftEven[2];
				xshaft[3] = xshaftEven[3];
				yshaft[0] = yshaftEven[0];
				yshaft[1] = yshaftEven[1];
				yshaft[2] = yshaftEven[2];
				yshaft[3] = yshaftEven[3];
				// Shift so that pivot is at the centre of the ball
				xcen = -cxEven;
				ycen = -cyEven;
				// Shift the link into it's position along the chain
				ycen += iMult*chainShift;
				// Now rotate this coordinate system into position
				cCoord = PolygonOperations.RotateCoordinate(xcen,ycen,rotAngleBA,0.0f,0.0f);
				xcen = cCoord[0];
				ycen = cCoord[1];
			} else {
				xshaft[0] = xshaftOdd[0];
				xshaft[1] = xshaftOdd[1];
				xshaft[2] = xshaftOdd[2];
				xshaft[3] = xshaftOdd[3];
				yshaft[0] = yshaftOdd[0];
				yshaft[1] = yshaftOdd[1];
				yshaft[2] = yshaftOdd[2];
				yshaft[3] = yshaftOdd[3];
				// Shift so that pivot is at the centre of the ball
				xcen = -cxEven;
				ycen = -cyEven;
				// Shift the odd link to join with the previous even link
				ycen += 0.5f*chainShift;
				// Shift the link into it's position along the chain
				ycen += iMult*chainShift;
				// Now rotate this coordinate system into position
				cCoord = PolygonOperations.RotateCoordinate(xcen,ycen,rotAngleBA,0.0f,0.0f);
				xcen = cCoord[0];
				ycen = cCoord[1];
				// Add one to iMult for the next even link
				iMult += 1.0f;
			}
			// Add this chain link to the json object
			json.object(); // Start of Chain Link
	        json.key("angle").value(0);
	        json.key("angularVelocity").value(0);
	        json.key("awake").value(true);
	        json.key("fixedRotation").value(false);
	        // Add the fixtures
	        json.key("fixture");
	        json.array();
	        json.object();
	    	json.key("density").value(30.0);
	    	json.key("friction").value(friction);
	    	json.key("restitution").value(0.0f);
	        json.key("name").value("fixture8");
	        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
	        json.key("filter-maskBits").value(B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
			json.key("polygon");
	        json.object(); // Begin polygon object
	        json.key("vertices");
	        json.object(); // Begin vertices object
	        json.key("x");
	        json.array();
	        for (int j = 0; j < xshaft.length; j++) json.value(xshaft[j]);
	        json.endArray();
	        json.key("y");
	        json.array();
	        for (int j = 0; j < yshaft.length; j++) json.value(yshaft[j]);
	       	json.endArray();
	        json.endObject(); // End the vertices object
	        json.endObject(); // End polygon object
	        json.endObject(); // End this fixture
	        json.endArray(); // End the array of fixtures
	        // Add some final properties for the link body
			json.key("linearVelocity").value(0);
			json.key("name").value("Bridge"+cnt+"_Link"+(i+1));
			json.key("position");
			json.object();
			json.key("x").value(B2DVars.EPPM*0.5f*(fs[0]+fs[4]) + xcen);
			json.key("y").value(B2DVars.EPPM*0.5f*(fs[1]+fs[5]) + ycen);
			json.endObject();
			json.key("type").value(2);
			json.endObject(); // End of Chain Link
		}
		return nLinks+2; // Return number of bodies added
	}

	public static int AddCrate(JSONStringer json, float[] fs, int cnt) throws JSONException {
		float xcen = B2DVars.EPPM*(fs[0]+fs[4])*0.5f;
		float ycen = B2DVars.EPPM*(fs[1]+fs[5])*0.5f;
		json.object(); // Start of Crate
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(30.0);
    	json.key("friction").value(0.3);
    	json.key("restitution").value(0.5);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_SWITCH);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(B2DVars.EPPM*fs[0]-xcen);
       	json.value(B2DVars.EPPM*fs[2]-xcen);
       	json.value(B2DVars.EPPM*fs[4]-xcen);
       	json.value(B2DVars.EPPM*fs[6]-xcen);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*fs[1]-ycen);
       	json.value(B2DVars.EPPM*fs[3]-ycen);
       	json.value(B2DVars.EPPM*fs[5]-ycen);
       	json.value(B2DVars.EPPM*fs[7]-ycen);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the crate body
		json.key("linearVelocity").value(0);
		json.key("name").value("Crate"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(xcen);
		json.key("y").value(ycen);
		json.endObject();
		json.key("type").value(2);
		json.endObject(); // End of Crate
		return 1; // Return number of bodies added
	}

	public static int AddDoor(JSONStringer json, float[] fs, int cnt, int doorcolor) throws JSONException {
		float xcen = B2DVars.EPPM*(fs[0]+fs[4])*0.5f;
		float ycen = B2DVars.EPPM*(fs[1]+fs[5])*0.5f;
		float rotAngle = PolygonOperations.GetAngle(fs[0],fs[1],fs[2],fs[3]);
		json.object(); // Start of Door
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
		// Specify some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("collect");
        if (doorcolor == ObjectVars.DoorRed) json.key("string").value("DoorRed");
        else if (doorcolor == ObjectVars.DoorGreen) json.key("string").value("DoorGreen");
        else if (doorcolor == ObjectVars.DoorBlue) json.key("string").value("DoorBlue");
        json.endObject();
        json.object();
        json.key("name").value("angle");
        json.key("float").value(rotAngle);
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(1.0);
        json.key("name").value("fixture8");
        if (doorcolor == ObjectVars.DoorRed) json.key("userData").value("DoorRed");
        else if (doorcolor == ObjectVars.DoorGreen) json.key("userData").value("DoorGreen");
        else if (doorcolor == ObjectVars.DoorBlue) json.key("userData").value("DoorBlue");
        json.key("filter-categoryBits").value(B2DVars.BIT_DOOR);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
        for (int i = 0; i < fs.length/2; i++) json.value(B2DVars.EPPM*fs[2*i]-xcen);
        json.endArray();
        json.key("y");
        json.array();
        for (int i = 0; i < fs.length/2; i++) json.value(B2DVars.EPPM*fs[2*i+1]-ycen);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the door body
		json.key("linearVelocity").value(0);
		json.key("name").value("Door"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(xcen);
		json.key("y").value(ycen);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Door
		return 1; // Return number of bodies added
	}

	public static int AddFinish(JSONStringer json, float[] fs) throws JSONException {
		json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(true);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(0.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_FINISH);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(B2DVars.EPPM*ObjectVars.objectFinishBall[2]);
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the boulder body
		json.key("linearVelocity").value(0);
		json.key("name").value("Finish");
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*(fs[0]-ObjectVars.objectFinish[0]));
		json.key("y").value(B2DVars.EPPM*(fs[1]-ObjectVars.objectFinish[1]));
		json.endObject();
		json.key("type").value(0);
		json.endObject();
		return 1; // Return number of bodies added
	}

	public static int AddGateSwitch(JSONStringer json, float[] fs, int cnt) throws JSONException {
		//float xcen = B2DVars.EPPM*(fs[0]+fs[4])*0.5f;
		//float ycen = B2DVars.EPPM*(fs[1]+fs[5])*0.5f;
		json.object(); // Start of Door
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
		// Specify some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("switchID");
        json.key("int").value(cnt);
        json.endObject();
        json.object();
        json.key("name").value("gatePos");
        json.key("vec2");
        json.object();
        json.key("x").value(B2DVars.EPPM*(fs[0]+fs[4])*0.5f);
        json.key("y").value(B2DVars.EPPM*(fs[1]+fs[5])*0.5f);
        json.endObject();
        json.endObject();
        json.object();
        json.key("name").value("gateLength");
        json.key("float").value(B2DVars.EPPM*(float)Math.sqrt((fs[2]-fs[4])*(fs[2]-fs[4]) + (fs[3]-fs[5])*(fs[3]-fs[5])));
        json.endObject();
        json.object();
        json.key("name").value("gateAngle");
        json.key("float").value(PolygonOperations.GetAngle(fs[0],fs[1],fs[2],fs[3]));
        json.endObject();
        json.object();
        json.key("name").value("switchPos");
        json.key("vec2");
        json.object();
        json.key("x").value(B2DVars.EPPM*(fs[12]+fs[14])*0.5f);
        json.key("y").value(B2DVars.EPPM*(fs[13]+fs[15])*0.5f);
        json.endObject();
        json.endObject();
        json.object();
        json.key("name").value("switchAngle");
        json.key("float").value(PolygonOperations.GetAngle(fs[8],fs[9],fs[10],fs[11]));
        json.endObject();
        json.object();
        json.key("name").value("switchLR");
        json.key("float").value(fs[16]/ObjectVars.objectGateSwitch[16]);
        json.endObject();
        json.object();
        json.key("name").value("switchOC");
        json.key("float").value(fs[17]);
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(1.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_SWITCH);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(0.5f*B2DVars.EPPM*(ObjectVars.objectGateSwitch[13]-ObjectVars.objectGateSwitch[11]));
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the gate switch body
		json.key("linearVelocity").value(0);
		json.key("name").value("SwitchGate"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*(fs[12]+fs[14])*0.5f);
		json.key("y").value(B2DVars.EPPM*(fs[13]+fs[15])*0.5f);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Gate Switch
		return 1; // Return number of bodies added
	}

	public static int AddGravity(JSONStringer json, float[] fs, int cnt, Vector2 gravityVec) throws JSONException {
		float gravity = 0.0f;
        if (LevelVars.get(LevelVars.PROP_GRAVITY) == "Earth") gravity = B2DVars.GRAVITY_EARTH;
        else if (LevelVars.get(LevelVars.PROP_GRAVITY) == "Mars") gravity = B2DVars.GRAVITY_MARS;
        else if (LevelVars.get(LevelVars.PROP_GRAVITY) == "Moon") gravity = B2DVars.GRAVITY_MOON;
        else gravity = B2DVars.GRAVITY_EARTH;
        float xgrav = gravityVec.x*gravity;
        float ygrav = gravityVec.y*gravity;
		json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        // Add some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("collect");
        json.key("string").value("Gravity");
        json.endObject();
        json.object();
        json.key("name").value("gravityVector");
        json.key("vec2");
        json.object();
        json.key("x").value(xgrav);
        json.key("y").value(ygrav);
        json.endObject();
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(true);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(1);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GRAVITY);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(B2DVars.EPPM*20.3f);
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the gravity body
		json.key("linearVelocity").value(0);
		json.key("name").value("Gravity"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*fs[0]);
		json.key("y").value(B2DVars.EPPM*fs[1]);
		json.endObject();
		json.key("type").value(0);
		json.endObject();
		return 1; // Return number of bodies added
	}

	public static int AddJewel(JSONStringer json, float[] fs, int cnt) throws JSONException {
		float cenAngle = MathUtils.radiansToDegrees*PolygonOperations.GetAngle(fs[0], fs[1], fs[4], fs[5]);
		float[] xyCoord = PolygonOperations.RotateCoordinate(0.0f, -2.3f, cenAngle, 0.0f, 0.0f);
		float xcen = B2DVars.EPPM*((fs[0]+fs[4])*0.5f + xyCoord[0]);
		float ycen = B2DVars.EPPM*((fs[1]+fs[5])*0.5f + xyCoord[1]);
		json.object(); // Start of Jewel
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        // Add some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("collect");
        json.key("string").value("Jewel");
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0);
    	json.key("restitution").value(0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_JEWEL);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(B2DVars.EPPM*fs[0]-xcen);
       	json.value(B2DVars.EPPM*fs[2]-xcen);
       	json.value(B2DVars.EPPM*fs[4]-xcen);
       	json.value(B2DVars.EPPM*fs[6]-xcen);
       	json.value(B2DVars.EPPM*fs[8]-xcen);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*fs[1]-ycen);
       	json.value(B2DVars.EPPM*fs[3]-ycen);
       	json.value(B2DVars.EPPM*fs[5]-ycen);
       	json.value(B2DVars.EPPM*fs[7]-ycen);
       	json.value(B2DVars.EPPM*fs[9]-ycen);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the jewel body
		json.key("linearVelocity").value(0);
		json.key("name").value("Jewel"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(xcen);
		json.key("y").value(ycen);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Jewel
		return 1; // Return number of bodies added
	}

	public static void AddJewelDiamond(JSONStringer json, float[] fs, int cnt) throws JSONException {
		float cenAngle = MathUtils.radiansToDegrees*PolygonOperations.GetAngle(fs[0], fs[1], fs[4], fs[5]);
		float[] xyCoord = PolygonOperations.RotateCoordinate(0.0f, -2.3f, cenAngle, 0.0f, 0.0f);
		float xcen = B2DVars.EPPM*((fs[0]+fs[4])*0.5f + xyCoord[0]);
		float ycen = B2DVars.EPPM*((fs[1]+fs[5])*0.5f + xyCoord[1]);
		json.object(); // Start of Jewel
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        // Add some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("collect");
        json.key("string").value("Diamond");
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0);
    	json.key("restitution").value(0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_JEWEL);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(B2DVars.EPPM*fs[0]-xcen);
       	json.value(B2DVars.EPPM*fs[2]-xcen);
       	json.value(B2DVars.EPPM*fs[4]-xcen);
       	json.value(B2DVars.EPPM*fs[6]-xcen);
       	json.value(B2DVars.EPPM*fs[8]-xcen);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*fs[1]-ycen);
       	json.value(B2DVars.EPPM*fs[3]-ycen);
       	json.value(B2DVars.EPPM*fs[5]-ycen);
       	json.value(B2DVars.EPPM*fs[7]-ycen);
       	json.value(B2DVars.EPPM*fs[9]-ycen);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the jewel body
		json.key("linearVelocity").value(0);
		json.key("name").value("Diamond"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(xcen);
		json.key("y").value(ycen);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Diamond
	}

	public static int AddKey(JSONStringer json, float[] fs, int cnt, int keycolor) throws JSONException {
		float xcen = B2DVars.EPPM*(fs[0]+fs[4])*0.5f;
		float ycen = B2DVars.EPPM*(fs[1]+fs[5])*0.5f;
		json.object(); // Start of Key
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
		// Specify some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("collect");
        if (keycolor == ObjectVars.KeyRed) json.key("string").value("KeyRed");
        else if (keycolor == ObjectVars.KeyGreen) json.key("string").value("KeyGreen");
        else if (keycolor == ObjectVars.KeyBlue) json.key("string").value("KeyBlue");
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(0.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_KEY);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(B2DVars.EPPM*fs[0]-xcen);
       	json.value(B2DVars.EPPM*fs[2]-xcen);
       	json.value(B2DVars.EPPM*fs[4]-xcen);
       	json.value(B2DVars.EPPM*fs[6]-xcen);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*fs[1]-ycen);
       	json.value(B2DVars.EPPM*fs[3]-ycen);
       	json.value(B2DVars.EPPM*fs[5]-ycen);
       	json.value(B2DVars.EPPM*fs[7]-ycen);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the key body
		json.key("linearVelocity").value(0);
		json.key("name").value("Key"+cnt);
        // Set the position
		json.key("position");
		json.object();
		json.key("x").value(xcen);
		json.key("y").value(ycen);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Key
		return 1; // Return number of bodies added
	}

	public static int AddLog(JSONStringer json, float[] fs, int cnt) throws JSONException {
		json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(30.0);
    	json.key("friction").value(0.3);
    	json.key("restitution").value(0.5);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_SWITCH);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(B2DVars.EPPM*fs[2]);
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the boulder body
		json.key("linearVelocity").value(0);
		json.key("name").value("Log"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*fs[0]);
		json.key("y").value(B2DVars.EPPM*fs[1]);
		json.endObject();
		json.key("type").value(2);
		json.endObject();
		return 1; // Return number of bodies added
	}

	public static int AddNitrous(JSONStringer json, float[] fs, int cnt) throws JSONException {
		float xcen = B2DVars.EPPM*(fs[0]+fs[4])*0.5f;
		float ycen = B2DVars.EPPM*(fs[1]+fs[5])*0.5f;
		json.object(); // Start of Key
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
		// Specify some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("collect");
        json.key("string").value("Nitrous");
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(0.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_KEY);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(B2DVars.EPPM*fs[0]-xcen);
       	json.value(B2DVars.EPPM*fs[2]-xcen);
       	json.value(B2DVars.EPPM*fs[4]-xcen);
       	json.value(B2DVars.EPPM*fs[6]-xcen);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*fs[1]-ycen);
       	json.value(B2DVars.EPPM*fs[3]-ycen);
       	json.value(B2DVars.EPPM*fs[5]-ycen);
       	json.value(B2DVars.EPPM*fs[7]-ycen);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the key body
		json.key("linearVelocity").value(0);
		json.key("name").value("Nitrous"+cnt);
        // Set the position
		json.key("position");
		json.object();
		json.key("x").value(xcen);
		json.key("y").value(ycen);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Nitrous
		return 1; // Return number of bodies added
	}

public static int AddPendulum(JSONStringer json, float[] fs, int cnt) throws JSONException {
		// Body order: Ball, Anchor, Shaft
		json.object(); // Start of Ball body
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(2500.0);
    	json.key("friction").value(0.6);
    	json.key("restitution").value(1.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_SWITCH);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(B2DVars.EPPM*fs[2]);
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the boulder body
		json.key("linearVelocity").value(0);
		json.key("name").value("PendulumBoulder"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*fs[0]);
		json.key("y").value(B2DVars.EPPM*fs[1]);
		json.endObject();
		json.key("type").value(2);
		json.endObject(); // End of Ball
		json.object(); // Start of Anchor
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(2500.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(1.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_DOOR | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_SWITCH);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(-B2DVars.EPPM*fs[5]*0.5f);
       	json.value(B2DVars.EPPM*fs[5]*0.5f);
       	json.value(B2DVars.EPPM*fs[5]*0.5f);
       	json.value(-B2DVars.EPPM*fs[5]*0.5f);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(-B2DVars.EPPM*fs[6]*0.5f);
       	json.value(-B2DVars.EPPM*fs[6]*0.5f);
       	json.value(B2DVars.EPPM*fs[6]*0.5f);
       	json.value(B2DVars.EPPM*fs[6]*0.5f);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the anchor body
		json.key("linearVelocity").value(0);
		json.key("name").value("PendulumAnchor"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*(fs[3]+fs[5]*0.5f));
		json.key("y").value(B2DVars.EPPM*(fs[4]+fs[6]*0.5f));
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Anchor
		float shaftLength = B2DVars.EPPM * (float) Math.sqrt( Math.pow(fs[0]-(fs[3]+0.5f*fs[5]),2) + Math.pow(fs[1]-(fs[4]+0.5f*fs[6]),2) );
		float[] xshaft = {-0.03f, -0.03f, 0.03f, 0.03f};
		float[] yshaft = {-0.5f*shaftLength, 0.5f*shaftLength, 0.5f*shaftLength, -0.5f*shaftLength};
		float rotAngle;
		if (fs[0] == fs[3]+fs[5]*0.5f) {
			rotAngle = 0.0f; // No rotation needed
		} else if (fs[1] == fs[4]+fs[6]*0.5f) {
			rotAngle = 90.0f;
			PolygonOperations.RotateArray(xshaft,yshaft,rotAngle,0.0f,0.0f);
		} else {
			rotAngle = 90.0f + MathUtils.radiansToDegrees*(float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
			PolygonOperations.RotateArray(xshaft,yshaft,rotAngle,0.0f,0.0f);
		}
		json.object(); // Start of Shaft
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(2500.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(1.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
        json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
        for (int i = 0; i < xshaft.length; i++) json.value(xshaft[i]);
        json.endArray();
        json.key("y");
        json.array();
        for (int i = 0; i < yshaft.length; i++) json.value(yshaft[i]);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the shaft body
		json.key("linearVelocity").value(0);
		json.key("name").value("PendulumShaft"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(0.5f*B2DVars.EPPM*(fs[0]+(fs[3]+fs[5]*0.5f)));
		json.key("y").value(0.5f*B2DVars.EPPM*(fs[1]+(fs[4]+fs[6]*0.5f)));
		json.endObject();
		json.key("type").value(2);
		json.endObject(); // End of Shaft
		return 3; // Return number of bodies added
	}

	public static int AddSpike(JSONStringer json, float[] fs, int cnt) throws JSONException {
		json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("fixedRotation").value(true);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(0.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_SPIKE);
        json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
        json.key("circle");
        // Begin circle object
        json.object();
        // Specify the center of the circle
        json.key("center").value(0);
        // Specify the radius of the circle
        json.key("radius").value(B2DVars.EPPM*fs[2]);
        json.endObject(); // End circle object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the boulder body
		json.key("linearVelocity").value(0);
		json.key("name").value("Spike"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*fs[0]);
		json.key("y").value(B2DVars.EPPM*fs[1]);
		json.endObject();
		json.key("type").value(0);
		json.endObject();
		return 1; // Return number of bodies added
	}

	public static int AddTransport(JSONStringer json, float[] fs, int cnt, Vector2 gravityVec) throws JSONException {
		float xcenA = B2DVars.EPPM*(fs[0]+fs[4])*0.5f;
		float ycenA = B2DVars.EPPM*(fs[1]+fs[5])*0.5f;
		float xcenB = B2DVars.EPPM*(fs[8]+fs[12])*0.5f;
		float ycenB = B2DVars.EPPM*(fs[9]+fs[13])*0.5f;
		float rotAngleA = PolygonOperations.GetAngle(fs[0], fs[1], fs[2], fs[3]);
		float rotAngleB = PolygonOperations.GetAngle(fs[8], fs[9], fs[10], fs[11]);
		// Get the gravity vector
		float gravity = 0.0f;
        if (LevelVars.get(LevelVars.PROP_GRAVITY) == "Earth") gravity = B2DVars.GRAVITY_EARTH;
        else if (LevelVars.get(LevelVars.PROP_GRAVITY) == "Mars") gravity = B2DVars.GRAVITY_MARS;
        else if (LevelVars.get(LevelVars.PROP_GRAVITY) == "Moon") gravity = B2DVars.GRAVITY_MOON;
        else gravity = B2DVars.GRAVITY_EARTH;
        float xgrav = 0.0f, ygrav=-gravity;
		if (gravityVec != null) {
	        xgrav = gravityVec.x*gravity;
	        ygrav = gravityVec.y*gravity;
		}
		json.object(); // Start of Transport Entry A
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
		// Specify some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("transportXY");
        json.key("vec2");
        json.object();
        json.key("x").value(xcenB-xcenA);
        json.key("y").value(ycenB-ycenA);
        json.endObject();
        json.endObject();
        json.object();
        json.key("name").value("transportAngle");
        json.key("float").value(rotAngleB-rotAngleA);
        json.endObject();
        if (gravityVec != null) {
            json.object();
            json.key("name").value("gravityVector");
            json.key("vec2");
            json.object();
            json.key("x").value(xgrav);
            json.key("y").value(ygrav);
            json.endObject();
            json.endObject();
        }
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(0.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_TRANSPORT);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(B2DVars.EPPM*fs[0]-xcenA);
       	json.value(B2DVars.EPPM*fs[2]-xcenA);
       	json.value(B2DVars.EPPM*fs[4]-xcenA);
       	json.value(B2DVars.EPPM*fs[6]-xcenA);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*fs[1]-ycenA);
       	json.value(B2DVars.EPPM*fs[3]-ycenA);
       	json.value(B2DVars.EPPM*fs[5]-ycenA);
       	json.value(B2DVars.EPPM*fs[7]-ycenA);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the transport body A
		json.key("linearVelocity").value(0);
		json.key("name").value("Transport"+cnt+"_A");
		json.key("position");
		json.object();
		json.key("x").value(xcenA);
		json.key("y").value(ycenA);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Transport A
		// Now insert Transport B
		json.object(); // Start of Transport Entry B
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
		// Specify some user data
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("transportXY");
        json.key("vec2");
        json.object();
        json.key("x").value(xcenA-xcenB);
        json.key("y").value(ycenA-ycenB);
        json.endObject();
        json.endObject();
        json.object();
        json.key("name").value("transportAngle");
        json.key("float").value(rotAngleA-rotAngleB);
        json.endObject();
        json.endArray();
        json.key("fixedRotation").value(false);
        // Add the fixtures
        json.key("fixture");
        json.array();
        json.object();
        json.key("sensor").value(true);
    	json.key("density").value(1.0);
    	json.key("friction").value(0.0);
    	json.key("restitution").value(0.0);
        json.key("name").value("fixture8");
        json.key("filter-categoryBits").value(B2DVars.BIT_TRANSPORT);
        json.key("filter-maskBits").value(B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
		json.key("polygon");
        json.object(); // Begin polygon object
        json.key("vertices");
        json.object(); // Begin vertices object
        json.key("x");
        json.array();
       	json.value(B2DVars.EPPM*fs[8]-xcenB);
       	json.value(B2DVars.EPPM*fs[10]-xcenB);
       	json.value(B2DVars.EPPM*fs[12]-xcenB);
       	json.value(B2DVars.EPPM*fs[14]-xcenB);
        json.endArray();
        json.key("y");
        json.array();
       	json.value(B2DVars.EPPM*fs[9]-ycenB);
       	json.value(B2DVars.EPPM*fs[11]-ycenB);
       	json.value(B2DVars.EPPM*fs[13]-ycenB);
       	json.value(B2DVars.EPPM*fs[15]-ycenB);
       	json.endArray();
        json.endObject(); // End the vertices object
        json.endObject(); // End polygon object
        json.endObject(); // End this fixture
        json.endArray(); // End the array of fixtures
        // Add some final properties for the transport body B
		json.key("linearVelocity").value(0);
		json.key("name").value("Transport"+cnt+"_B");
		json.key("position");
		json.object();
		json.key("x").value(xcenB);
		json.key("y").value(ycenB);
		json.endObject();
		json.key("type").value(0);
		json.endObject(); // End of Transport B
		return 2; // Return number of bodies added
	}

	public static void AddBoundary(JSONStringer json, String textString) throws JSONException {
		float friction = 0.9f;
		float restitution = 0.2f;
		// Bottom Boundary
		json.object();
	    // Specify other properties of this fixture
		json.key("density").value(1);
		json.key("friction").value(friction);
		json.key("restitution").value(restitution);
	    json.key("name").value("fixture8");
	    json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
	    json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN);
	    // Set the (background) ground texture
	    json.key("customProperties");
	    json.array();
	    json.object();
	    json.key("name").value("TextureMask");
	    json.key("string").value(textString);
	    json.endObject();
	    json.endArray();
		json.key("polygon");
	    json.object(); // Begin polygon object
	    json.key("vertices");
	    json.object(); // Begin vertices object
	    json.key("x");
	    json.array();
	    json.value(1000.0f).value(1050.0f).value(-50.0f).value(0.0f);
	    json.endArray();
	    json.key("y");
	    json.array();
	    json.value(0.0f).value(-50.0f).value(-50.0f).value(0.0f);
	    json.endArray();
	    json.endObject(); // End the vertices object
	    json.endObject(); // End polygon object
	    json.endObject(); // End this fixture
	    // Left Boundary
		json.object();
	    // Specify other properties of this fixture
		json.key("density").value(1);
	    json.key("friction").value(friction);
	    json.key("restitution").value(restitution);
	    json.key("name").value("fixture8");
	    json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
	    json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN);
	    // Set the (background) ground texture
	    json.key("customProperties");
	    json.array();
	    json.object();
	    json.key("name").value("TextureMask");
	    json.key("string").value(textString);
	    json.endObject();
	    json.endArray();
		json.key("polygon");
	    json.object(); // Begin polygon object
	    json.key("vertices");
	    json.object(); // Begin vertices object
	    json.key("x");
	    json.array();
	    json.value(-50.0f).value(0.0f).value(0.0f).value(-50.0f);
	    json.endArray();
	    json.key("y");
	    json.array();
	    json.value(1050.0f).value(1000.0f).value(0.0f).value(-50.0f);
	    json.endArray();
	    json.endObject(); // End the vertices object
	    json.endObject(); // End polygon object
	    json.endObject(); // End this fixture
	    // Top Boundary
		json.object();
	    // Specify other properties of this fixture
		json.key("density").value(1);
	    json.key("friction").value(friction);
	    json.key("restitution").value(restitution);
	    json.key("name").value("fixture8");
	    json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
	    json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN);
	    // Set the (background) ground texture
	    json.key("customProperties");
	    json.array();
	    json.object();
	    json.key("name").value("TextureMask");
	    json.key("string").value(textString);
	    json.endObject();
	    json.endArray();
		json.key("polygon");
	    json.object(); // Begin polygon object
	    json.key("vertices");
	    json.object(); // Begin vertices object
	    json.key("x");
	    json.array();
	    json.value(0.0f).value(-50.0f).value(1050.0f).value(1000.0f);
	    json.endArray();
	    json.key("y");
	    json.array();
	    json.value(1000.0f).value(1050.0f).value(1050.0f).value(1000.0f);
	    json.endArray();
	    json.endObject(); // End the vertices object
	    json.endObject(); // End polygon object
	    json.endObject(); // End this fixture
		json.object();
	    // Specify other properties of this fixture
		json.key("density").value(1);
	    json.key("friction").value(friction);
	    json.key("restitution").value(restitution);
	    json.key("name").value("fixture8");
	    json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
	    json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);
	    // Set the (background) ground texture
	    json.key("customProperties");
	    json.array();
	    json.object();
	    json.key("name").value("TextureMask");
	    json.key("string").value(textString);
	    json.endObject();
	    json.endArray();
		json.key("polygon");
	    json.object(); // Begin polygon object
	    json.key("vertices");
	    json.object(); // Begin vertices object
	    json.key("x");
	    json.array();
	    json.value(1050.0f).value(1000.0f).value(1000.0f).value(1050.0f);
	    json.endArray();
	    json.key("y");
	    json.array();
	    json.value(-50.0f).value(0.0f).value(1000.0f).value(1050.0f);
	    json.endArray();
	    json.endObject(); // End the vertices object
	    json.endObject(); // End polygon object
	    json.endObject(); // End this fixture
	    return;
	}

	public static String AddFallingPolygon(JSONStringer json, float[] poly, float[] path, int pType, ArrayList<float[]> allDecors, ArrayList<Integer> allDecorTypes, ArrayList<Integer> allDecorPolys, String textString, String textGrass, float friction, float restitution, int cnt, int pNumb) throws JSONException {
		ArrayList<float[]> convexPolygons;
		ArrayList<ArrayList<Vector2>> convexVectorPolygons;
		ArrayList<Vector2> concaveVertices;
		// Begin the Falling Body definition
        json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("userData").value("GroundFall");
        json.key("customProperties");
        // Set GameInfo
        json.array();
        json.object();
        json.key("name").value("GameInfo");
        json.key("string").value("SURFACE");
        json.endObject();
        json.object();
        json.key("name").value("FallTime");
        json.key("float").value(path[0]);
        json.endObject();
        json.endArray();
        // Add the fixtures
        json.key("fixture");
        json.array();
		// Add fixtures
		if (pType==4) {
        	// Decompose each polygon into a series of convex polygons
			concaveVertices = PolygonOperations.MakeVertices(poly);
			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
			for (int k = 0; k<convexPolygons.size(); k++){
				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+pNumb+" P"; // A problem with the length^2 of a polygon
				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+pNumb+" P"; // polygon is not convex
            	json.object();
	            // Specify other properties of this fixture
	        	json.key("density").value(250);
	            json.key("friction").value(friction);
	            json.key("restitution").value(restitution);
	            json.key("name").value("fixture8");
	            json.key("userData").value("GroundFall");
	            if (textString.equalsIgnoreCase("images/ground_lava.png")) {
		            json.key("filter-categoryBits").value(B2DVars.BIT_SPIKE);
		            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            			            	
	            } else {
		            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
		            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            	
	            }
	            // Set the (background) ground texture
	            json.key("customProperties");
	            json.array();
	            json.object();
	            json.key("name").value("TextureMask");
	            json.key("string").value(textString);
	            json.endObject();
	            json.endArray();
    			json.key("polygon");
                json.object(); // Begin polygon object
                json.key("vertices");
                json.object(); // Begin vertices object
                json.key("x");
                json.array();
                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j]-poly[0]));
                json.endArray();
                json.key("y");
                json.array();
                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j+1]-poly[1]));
                json.endArray();
                json.endObject(); // End the vertices object
                json.endObject(); // End polygon object
                json.endObject(); // End this fixture
			}
			// Check if any grass needs to be added to this polygon
	        for (int i = 0; i<allDecors.size(); i++) {
	        	// Decompose each polygon into a series of convex polygons
	            if ((allDecorTypes.get(i) == DecorVars.Grass) & (allDecorPolys.get(i)==pNumb)) {
	    			concaveVertices = PolygonOperations.MakeVertices(allDecors.get(i));
	    			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
	    			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
	    			for (int k = 0; k<convexPolygons.size(); k++){
	    				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+pNumb+" G"; // A problem with the length^2 of a polygon
	    				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+pNumb+" G"; // polygon is not convex
	                	json.object();
			            // Specify other properties of this fixture
			        	json.key("density").value(1);
			            json.key("friction").value(0);
			            json.key("restitution").value(0);
			            json.key("name").value("fixture8");
			            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
			            json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
			            // Set the (background) ground texture
			            json.key("customProperties");
			            json.array();
			            json.object();
			            json.key("name").value("TextureMask");
			            json.key("string").value(textGrass);
			            json.endObject();
			            json.endArray();
		    			json.key("polygon");
		                json.object(); // Begin polygon object
		                json.key("vertices");
		                json.object(); // Begin vertices object
		                json.key("x");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j]-poly[0]));
		                json.endArray();
		                json.key("y");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j+1]-poly[1]));
		                json.endArray();
		                json.endObject(); // End the vertices object
		                json.endObject(); // End polygon object
		                json.endObject(); // End this fixture
	    			}
	            }
	        }
		} else if (pType==5) {
        	json.object();
            // Specify other properties of this fixture
        	json.key("density").value(1);
            json.key("friction").value(friction);
            json.key("restitution").value(restitution);
            json.key("name").value("fixture8");
            json.key("userData").value("GroundFall");
            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);
            // Set the (background) ground texture
            json.key("customProperties");
            json.array();
            json.object();
            json.key("name").value("TextureMask");
            json.key("string").value(textString);
            json.endObject();
            json.endArray();
            json.key("circle");
            // Begin circle object
            json.object();
            // Specify the center of the circle
            json.key("center");
            json.object();
            json.key("x").value(0);
            json.key("y").value(0);
            json.endObject();
            // Specify the radius of the circle
            json.key("radius").value(B2DVars.EPPM*poly[2]);
            json.endObject(); // End circle object
            json.endObject(); // End this fixture
		}
        json.endArray(); // End of the fixtures for this Falling body
        // Add some final properties for the ground body
        json.key("linearDamping").value(path[1]);
		json.key("linearVelocity").value(0);
		json.key("name").value("GroundF"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*poly[0]);
		json.key("y").value(B2DVars.EPPM*poly[1]);
		json.endObject();
		json.key("type").value(2);
        json.endObject(); // End of this falling body
        return "";
	}

	public static String AddTriggerPolygon(JSONStringer json, float[] poly, float[] path, int pType, ArrayList<float[]> allDecors, ArrayList<Integer> allDecorTypes, ArrayList<Integer> allDecorPolys, String textString, String textGrass, float friction, float restitution, int cnt, int pNumb) throws JSONException {
		ArrayList<float[]> convexPolygons;
		ArrayList<ArrayList<Vector2>> convexVectorPolygons;
		ArrayList<Vector2> concaveVertices;
		// Begin the Falling Body definition
        json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("customProperties");
        // Set GameInfo
        json.array();
        json.object();
        json.key("name").value("GameInfo");
        json.key("string").value("SURFACE");
        json.endObject();
        json.endArray();
        // Add the fixtures
        json.key("fixture");
        json.array();
		// Add fixtures
		if (pType==6) {
        	// Decompose each polygon into a series of convex polygons
			concaveVertices = PolygonOperations.MakeVertices(poly);
			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
			for (int k = 0; k<convexPolygons.size(); k++){
				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+pNumb+" P"; // A problem with the length^2 of a polygon
				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+pNumb+" P"; // polygon is not convex
            	json.object();
	            // Specify other properties of this fixture
	        	json.key("density").value(250);
	            json.key("friction").value(friction);
	            json.key("restitution").value(restitution);
	            json.key("name").value("fixture8");
	            if (textString.equalsIgnoreCase("images/ground_lava.png")) {
		            json.key("filter-categoryBits").value(B2DVars.BIT_SPIKE);
		            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            			            	
	            } else {
		            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
		            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            	
	            }
	            // Set the (background) ground texture
	            json.key("customProperties");
	            json.array();
	            json.object();
	            json.key("name").value("TextureMask");
	            json.key("string").value(textString);
	            json.endObject();
	            json.endArray();
    			json.key("polygon");
                json.object(); // Begin polygon object
                json.key("vertices");
                json.object(); // Begin vertices object
                json.key("x");
                json.array();
                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j]-poly[0]));
                json.endArray();
                json.key("y");
                json.array();
                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j+1]-poly[1]));
                json.endArray();
                json.endObject(); // End the vertices object
                json.endObject(); // End polygon object
                json.endObject(); // End this fixture
			}
			// Check if any grass needs to be added to this polygon
	        for (int i = 0; i<allDecors.size(); i++) {
	        	// Decompose each polygon into a series of convex polygons
	            if ((allDecorTypes.get(i) == DecorVars.Grass) & (allDecorPolys.get(i)==pNumb)) {
	    			concaveVertices = PolygonOperations.MakeVertices(allDecors.get(i));
	    			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
	    			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
	    			for (int k = 0; k<convexPolygons.size(); k++){
	    				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+pNumb+" G"; // A problem with the length^2 of a polygon
	    				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+pNumb+" G"; // polygon is not convex
	                	json.object();
			            // Specify other properties of this fixture
			        	json.key("density").value(1);
			            json.key("friction").value(0);
			            json.key("restitution").value(0);
			            json.key("name").value("fixture8");
			            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
			            json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
			            // Set the (background) ground texture
			            json.key("customProperties");
			            json.array();
			            json.object();
			            json.key("name").value("TextureMask");
			            json.key("string").value(textGrass);
			            json.endObject();
			            json.endArray();
		    			json.key("polygon");
		                json.object(); // Begin polygon object
		                json.key("vertices");
		                json.object(); // Begin vertices object
		                json.key("x");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j]-poly[0]));
		                json.endArray();
		                json.key("y");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j+1]-poly[1]));
		                json.endArray();
		                json.endObject(); // End the vertices object
		                json.endObject(); // End polygon object
		                json.endObject(); // End this fixture
	    			}
	            }
	        }
		} else if (pType==7) {
        	json.object();
            // Specify other properties of this fixture
        	json.key("density").value(1);
            json.key("friction").value(friction);
            json.key("restitution").value(restitution);
            json.key("name").value("fixture8");
            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);
            // Set the (background) ground texture
            json.key("customProperties");
            json.array();
            json.object();
            json.key("name").value("TextureMask");
            json.key("string").value(textString);
            json.endObject();
            json.endArray();
            json.key("circle");
            // Begin circle object
            json.object();
            // Specify the center of the circle
            json.key("center");
            json.object();
            json.key("x").value(0);
            json.key("y").value(0);
            json.endObject();
            // Specify the radius of the circle
            json.key("radius").value(B2DVars.EPPM*poly[2]);
            json.endObject(); // End circle object
            json.endObject(); // End this fixture
		}
		// Add the trigger - first make the poly
    	float[] triggerPoly = new float[] {path[2]-5.0f, path[3]-path[4]/2,
    			path[2]+5.0f, path[3]-path[4]/2,
    			path[2]+5.0f, path[3]+path[4]/2,
    			path[2]-5.0f, path[3]+path[4]/2};
    	PolygonOperations.RotateXYArray(triggerPoly, path[5], path[2], path[3]);
    	// Decompose each polygon into a series of convex polygons
		concaveVertices = PolygonOperations.MakeVertices(triggerPoly);
		convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
		convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
		for (int k = 0; k<convexPolygons.size(); k++){
			if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+pNumb+" P"; // A problem with the length^2 of a polygon
			//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+pNumb+" P"; // polygon is not convex
        	json.object();
            // Specify other properties of this fixture
            //json.key("sensor").value(true);
        	json.key("density").value(1.0);
        	json.key("friction").value(0.0);
        	json.key("restitution").value(1.0);
            json.key("name").value("fixture8");
            json.key("userData").value("GroundTrigger");
            json.key("filter-categoryBits").value(B2DVars.BIT_SWITCH);
            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL);
			json.key("polygon");
            json.object(); // Begin polygon object
            json.key("vertices");
            json.object(); // Begin vertices object
            json.key("x");
            json.array();
            for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j]-poly[0]));
            json.endArray();
            json.key("y");
            json.array();
            for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j+1]-poly[1]));
            json.endArray();
            json.endObject(); // End the vertices object
            json.endObject(); // End polygon object
            json.endObject(); // End this fixture
		}
		// End of the fixtures for this Trigger body
		json.endArray();
        // Add some final properties for the ground body
        //json.key("linearDamping").value(path[1]);
		json.key("linearVelocity").value(0);
		json.key("name").value("Trigger"+cnt);
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*poly[0]);
		json.key("y").value(B2DVars.EPPM*poly[1]);
		json.endObject();
		json.key("type").value(2);
        json.endObject(); // End of this trigger body
        return "";
	}

	public static String AddKinematicPolygon(JSONStringer json, float[] poly, float[] path, int pType, ArrayList<float[]> allDecors, ArrayList<Integer> allDecorTypes, ArrayList<Integer> allDecorPolys, String textString, String textGrass, float friction, float restitution, int pNumb) throws JSONException {
		ArrayList<float[]> convexPolygons;
		ArrayList<ArrayList<Vector2>> convexVectorPolygons;
		ArrayList<Vector2> concaveVertices;
		// Begin the Kinematic Body definition
        json.object();
        json.key("angle").value(0);
        if (path[2]==1) json.key("angularVelocity").value(MathUtils.degreesToRadians*path[0]);
        else json.key("angularVelocity").value(MathUtils.degreesToRadians*(path[0]-360));
        json.key("awake").value(true);
        json.key("customProperties");
        json.array();
        // CP: Set GameInfo
        json.object();
        json.key("name").value("GameInfo");
        json.key("string").value("SURFACE");
        json.endObject();
        // CP: Set the path
        float pathlength = -1.0f;
        if (path.length >= 10) {
        	json.object();
        	json.key("name").value("path");
	        json.key("vertices");
	        json.object(); // Begin vertices object
	        json.key("x");
	        json.array();
	        for (int j = 0; j<(path.length-6)/2; j++) json.value(B2DVars.EPPM*path[6+2*j]);
	        json.endArray();
	        json.key("y");
	        json.array();
	        for (int j = 0; j<(path.length-6)/2; j++) json.value(B2DVars.EPPM*path[6+2*j+1]);
	        json.endArray();
	        json.endObject(); // End the vertices object
	        json.endObject();
            pathlength = 0.0f;
	        for (int j = 0; j<((path.length-6)/2)-1; j++) pathlength += (float)Math.sqrt((path[6+2*j]-path[6+2*(j+1)])*(path[6+2*j]-path[6+2*(j+1)]) + (path[6+2*j+1]-path[6+2*(j+1)+1])*(path[6+2*j+1]-path[6+2*(j+1)+1]));
        }
        // CP: Set Velocity
        json.object();
        json.key("name").value("speed");
        json.key("float").value(B2DVars.EPPM*path[1]);
        json.endObject();
        // CP: Set Direction
        json.object();
        json.key("name").value("direction");
        json.key("int").value((int)path[3]);
        json.endObject();
        // CP: Set path length
        if (pathlength >= 0.0) {
        	json.object();
        	json.key("name").value("pathlength");
        	json.key("float").value(B2DVars.EPPM*pathlength);
        	json.endObject();
        }
        if (path.length >= 10) {
        	float bestval = -1.0f;
        	int bestidx = 0;
        	for (int j = 0; j<(path.length-6)/2; j++) {
        		if (j == 0) bestval = (float)Math.sqrt((path[6+2*j]-path[4])*(path[6+2*j]-path[4]) + (path[6+2*j+1]-path[5])*(path[6+2*j+1]-path[5]));
        		else if ((float)Math.sqrt((path[6+2*j]-path[4])*(path[6+2*j]-path[4]) + (path[6+2*j+1]-path[5])*(path[6+2*j+1]-path[5])) < bestval) {
        			bestval = (float)Math.sqrt((path[6+2*j]-path[4])*(path[6+2*j]-path[4]) + (path[6+2*j+1]-path[5])*(path[6+2*j+1]-path[5]));
        			bestidx = j;
        		}
        	}
        	json.object();
        	json.key("name").value("index");
        	json.key("int").value(bestidx);
        	json.endObject();        	
        } else {
        	json.object();
        	json.key("name").value("index");
        	json.key("int").value(0);
        	json.endObject();        	
        }
        json.endArray();
        // Add the fixtures
        json.key("fixture");
        json.array();
		// Add fixtures
		if (pType==2) {
        	// Decompose each polygon into a series of convex polygons
			concaveVertices = PolygonOperations.MakeVertices(poly);
			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
			for (int k = 0; k<convexPolygons.size(); k++){
				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+pNumb+" P"; // A problem with the length^2 of a polygon
				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+pNumb+" P"; // polygon is not convex
            	json.object();
	            // Specify other properties of this fixture
	        	json.key("density").value(1);
	            json.key("friction").value(friction);
	            json.key("restitution").value(restitution);
	            json.key("name").value("fixture8");
	            if (textString.equalsIgnoreCase("images/ground_lava.png")) {
		            json.key("filter-categoryBits").value(B2DVars.BIT_SPIKE);
		            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            			            	
	            } else {
		            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
		            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            	
	            }
	            // Set the (background) ground texture
	            json.key("customProperties");
	            json.array();
	            json.object();
	            json.key("name").value("TextureMask");
	            json.key("string").value(textString);
	            json.endObject();
	            json.endArray();
    			json.key("polygon");
                json.object(); // Begin polygon object
                json.key("vertices");
                json.object(); // Begin vertices object
                json.key("x");
                json.array();
                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j]-path[4]));
                json.endArray();
                json.key("y");
                json.array();
                for (int j = 0; j<convexPolygons.get(k).length/2; j++) json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j+1]-path[5]));
                json.endArray();
                json.endObject(); // End the vertices object
                json.endObject(); // End polygon object
                json.endObject(); // End this fixture
			}
			// Check if any grass needs to be added to this polygon
	        for (int i = 0; i<allDecors.size(); i++) {
	        	// Decompose each polygon into a series of convex polygons
	            if ((allDecorTypes.get(i) == DecorVars.Grass) & (allDecorPolys.get(i)==pNumb)) {
	    			concaveVertices = PolygonOperations.MakeVertices(allDecors.get(i));
	    			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
	    			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
	    			for (int k = 0; k<convexPolygons.size(); k++){
	    				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+pNumb+" G"; // A problem with the length^2 of a polygon
	    				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+pNumb+" G"; // polygon is not convex
	                	json.object();
			            // Specify other properties of this fixture
			        	json.key("density").value(1);
			            json.key("friction").value(0);
			            json.key("restitution").value(0);
			            json.key("name").value("fixture8");
			            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
			            json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
			            // Set the (background) ground texture
			            json.key("customProperties");
			            json.array();
			            json.object();
			            json.key("name").value("TextureMask");
			            json.key("string").value(textGrass);
			            json.endObject();
			            json.endArray();
		    			json.key("polygon");
		                json.object(); // Begin polygon object
		                json.key("vertices");
		                json.object(); // Begin vertices object
		                json.key("x");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
		                	json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j]-path[4]));
		                }
		                json.endArray();
		                json.key("y");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
		                	json.value(B2DVars.EPPM*(convexPolygons.get(k)[2*j+1]-path[5]));
		                }
		                json.endArray();
		                json.endObject(); // End the vertices object
		                json.endObject(); // End polygon object
		                json.endObject(); // End this fixture
	    			}
	            }
	        }
		} else if (pType==3) {
        	json.object();
            // Specify other properties of this fixture
        	json.key("density").value(1);
            json.key("friction").value(friction);
            json.key("restitution").value(restitution);
            json.key("name").value("fixture8");
            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);
            // Set the (background) ground texture
            json.key("customProperties");
            json.array();
            json.object();
            json.key("name").value("TextureMask");
            json.key("string").value(textString);
            json.endObject();
            json.endArray();
            json.key("circle");
            // Begin circle object
            json.object();
            // Specify the center of the circle
            json.key("center");
            json.object();
            json.key("x").value(B2DVars.EPPM*(poly[0]-path[4]));
            json.key("y").value(B2DVars.EPPM*(poly[1]-path[5]));
            json.endObject();
            // Specify the radius of the circle
            json.key("radius").value(B2DVars.EPPM*poly[2]);
            json.endObject(); // End circle object
            json.endObject(); // End this fixture
		}
        json.endArray(); // End of the fixtures for this Kinematic body
        // Add some final properties for the ground body
		json.key("linearVelocity").value(0);
		json.key("name").value("Ground");
		json.key("position");
		json.object();
		json.key("x").value(B2DVars.EPPM*path[4]);
		json.key("y").value(B2DVars.EPPM*path[5]);
		json.endObject();
		json.key("type").value(1);
        json.endObject(); // End of this kinematic body
        return "";
	}
}
