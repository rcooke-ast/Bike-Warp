package com.mygdx.game.utilities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.ObjectVars;
import com.mygdx.game.utilities.json.JSONException;
import com.mygdx.game.utilities.json.JSONStringer;

public class EditorImageIO {

	public static int ImageBallChain(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float rd = fs[2]*B2DVars.EPPM;
		float brd = 0.2f*rd;
		float xv = 0.5f*fs[5]*B2DVars.EPPM;
		float yv = 0.5f*fs[6]*B2DVars.EPPM;
		json.object();
		json.key("name").value("BallChainBall"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/boulder.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject(); // End of Pendulum Ball
		json.object(); // Start of Pendulum Anchor
		json.key("name").value("BallChainAnchor"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex+1);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-xv).value(xv).value(xv).value(-xv).endArray();
		json.key("y").array().value(-yv).value(-yv).value(yv).value(yv).endArray();
		json.endObject();
		json.key("file").value("images/metalplate.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-xv).value(-yv).value(xv).value(-yv).value(xv).value(yv).value(-xv).value(yv).endArray();
		json.endObject(); // End of Pendulum Anchor
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
		// Rotate the odd array
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngle,0.0f,0.0f);
		// Rotate the even array
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,-rotAngle,0.0f,0.0f);
		// Determine the ball-anchor axis rotation
		if (fs[0] == fs[3]+fs[5]*0.5f) rotAngleBA = 0.0f; // No rotation needed
		else rotAngleBA = 90.0f + MathUtils.radiansToDegrees*(float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
		if (fs[0] < fs[3]+fs[5]*0.5f) rotAngleBA += 180.0f; 
		// Rotate both arrays to lie on the Ball-Anchor axis
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngleBA,0.0f,0.0f);
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,rotAngleBA,0.0f,0.0f);
		float[] xshaft = new float[4], yshaft = new float[4];
		float setAngle;
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
				// Set the angle of this link
				setAngle = rotAngleBA-rotAngle;
			} else {
				xshaft[0] = xshaftOdd[0];
				xshaft[1] = xshaftOdd[1];
				xshaft[2] = xshaftOdd[2];
				xshaft[3] = xshaftOdd[3];
				yshaft[0] = yshaftOdd[0];
				yshaft[1] = yshaftOdd[1];
				yshaft[2] = yshaftOdd[2];
				yshaft[3] = yshaftOdd[3];
				// Set the angle of this link
				setAngle = rotAngleBA+rotAngle;
			}
			// Add this chain link to the json object
			json.object(); // Start of Link
			json.key("name").value("BallChain"+cnt+"_link"+i);
			json.key("opacity").value(1);
			json.key("renderOrder").value(1);
			json.key("scale").value(1);
			json.key("aspectScale").value(1);
			json.key("angle").value(MathUtils.degreesToRadians*(setAngle+90.0f));
			json.key("body").value(bodyIndex+2+i);
			json.key("center");
			json.object();
			json.key("x").value(0);
			json.key("y").value(0);
			json.endObject();
			json.key("corners");
			json.object();
			json.key("x").array().value(xshaft[0]).value(xshaft[1]).value(xshaft[2]).value(xshaft[3]).endArray();
			json.key("y").array().value(yshaft[0]).value(yshaft[1]).value(yshaft[2]).value(yshaft[3]).endArray();
			json.endObject();
			json.key("file").value("images/chain_link.png");
			json.key("filter").value(1);
			json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
			json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
			json.key("glVertexPointer").array().value(-0.5f*bodyLength).value(halfWidth).value(0.5f*bodyLength).value(halfWidth).value(0.5f*bodyLength).value(-halfWidth).value(-0.5f*bodyLength).value(-halfWidth).endArray();
			json.endObject(); // End of Ball Chain link
		}
		json.object(); // Start of Ball Chain Bolt
		json.key("name").value("BallChainBallBolt"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(2);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-brd).value(brd).value(brd).value(-brd).endArray();
		json.key("y").array().value(-brd).value(-brd).value(brd).value(brd).endArray();
		json.endObject();
		json.key("file").value("images/bolt.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-brd).value(-brd).value(brd).value(-brd).value(brd).value(brd).value(-brd).value(brd).endArray();
		json.endObject(); // End of Ball Chain Ball Bolt
		json.object(); // Start of Ball Chain Anchor Bolt
		json.key("name").value("BallChainAnchorBolt"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(2);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex+1);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-brd).value(brd).value(brd).value(-brd).endArray();
		json.key("y").array().value(-brd).value(-brd).value(brd).value(brd).endArray();
		json.endObject();
		json.key("file").value("images/bolt.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-brd).value(-brd).value(brd).value(-brd).value(brd).value(brd).value(-brd).value(brd).endArray();
		json.endObject(); // End of Ball Chain Anchor Bolt
		return nLinks+2;
	}

	public static int ImageBoulder(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float rd = fs[2]*B2DVars.EPPM;
		json.object();
		json.key("name").value("Boulder"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/boulder.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject();
		return 1;
	}

	public static int ImageBridge(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float shaftMinLength = B2DVars.EPPM * (float) Math.sqrt( Math.pow(0.5f*(fs[8]+fs[12])-0.5f*(fs[0]+fs[4]),2) + Math.pow(0.5f*(fs[9]+fs[13])-0.5f*(fs[1]+fs[5]),2) );
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
		// Rotate the odd array
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngle,0.0f,0.0f);
		// Rotate the even array
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,-rotAngle,0.0f,0.0f);
		// Determine the ball-anchor axis rotation
		if (fs[0] == fs[3]+fs[5]*0.5f) rotAngleBA = 0.0f; // No rotation needed
		else rotAngleBA = 90.0f + MathUtils.radiansToDegrees*(float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
		if (fs[0] < fs[3]+fs[5]*0.5f) rotAngleBA += 180.0f; 
		// Rotate both arrays to lie on the Ball-Anchor axis
		PolygonOperations.RotateArray(xshaftOdd,yshaftOdd,rotAngleBA,0.0f,0.0f);
		PolygonOperations.RotateArray(xshaftEven,yshaftEven,rotAngleBA,0.0f,0.0f);
		float[] xshaft = new float[4], yshaft = new float[4];
		float setAngle;
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
				// Set the angle of this link
				setAngle = rotAngleBA-rotAngle;
			} else {
				xshaft[0] = xshaftOdd[0];
				xshaft[1] = xshaftOdd[1];
				xshaft[2] = xshaftOdd[2];
				xshaft[3] = xshaftOdd[3];
				yshaft[0] = yshaftOdd[0];
				yshaft[1] = yshaftOdd[1];
				yshaft[2] = yshaftOdd[2];
				yshaft[3] = yshaftOdd[3];
				// Set the angle of this link
				setAngle = rotAngleBA+rotAngle;
			}
			// Add this chain link to the json object
			json.object(); // Start of Link
			json.key("name").value("Bridge"+cnt+"_link"+i);
			json.key("opacity").value(1);
			json.key("renderOrder").value(1);
			json.key("scale").value(1);
			json.key("aspectScale").value(1);
			json.key("angle").value(MathUtils.degreesToRadians*(setAngle+90.0f));
			json.key("body").value(bodyIndex+2+i);
			json.key("center");
			json.object();
			json.key("x").value(0);
			json.key("y").value(0);
			json.endObject();
			json.key("corners");
			json.object();
			json.key("x").array().value(xshaft[0]).value(xshaft[1]).value(xshaft[2]).value(xshaft[3]).endArray();
			json.key("y").array().value(yshaft[0]).value(yshaft[1]).value(yshaft[2]).value(yshaft[3]).endArray();
			json.endObject();
			json.key("file").value("images/wood_link.png");
			json.key("filter").value(1);
			json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
			json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
			json.key("glVertexPointer").array().value(-0.5f*bodyLength).value(halfWidth).value(0.5f*bodyLength).value(halfWidth).value(0.5f*bodyLength).value(-halfWidth).value(-0.5f*bodyLength).value(-halfWidth).endArray();
			json.endObject(); // End of Ball Chain link
		}
		return nLinks+2;
	}

	public static int ImageCrate(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float xcen = 0.5f*B2DVars.EPPM*(fs[0]+fs[4]);
		float ycen = 0.5f*B2DVars.EPPM*(fs[1]+fs[5]);
		float rotAngle;
		if (fs[0] == fs[6]) {
			rotAngle = 0.0f; // No rotation needed
		} else if (fs[1] == fs[7]) {
			rotAngle = 90.0f;
		} else {
			rotAngle = 90.0f + MathUtils.radiansToDegrees * (float) Math.atan( (fs[1] - fs[7])/(fs[0] - fs[6]) );
		}
		json.object(); // Start of Crate
		json.key("name").value("Crate"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(MathUtils.degreesToRadians*rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(B2DVars.EPPM*fs[0]-xcen).value(B2DVars.EPPM*fs[2]-xcen).value(B2DVars.EPPM*fs[4]-xcen).value(B2DVars.EPPM*fs[6]-xcen).endArray();
		json.key("y").array().value(B2DVars.EPPM*fs[1]-ycen).value(B2DVars.EPPM*fs[3]-ycen).value(B2DVars.EPPM*fs[5]-ycen).value(B2DVars.EPPM*fs[7]-ycen).endArray();
		json.endObject();
		json.key("file").value("images/crate2.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(B2DVars.EPPM*ObjectVars.objectCrate[0]).value(B2DVars.EPPM*ObjectVars.objectCrate[1]).value(B2DVars.EPPM*ObjectVars.objectCrate[2]).value(B2DVars.EPPM*ObjectVars.objectCrate[3]).value(B2DVars.EPPM*ObjectVars.objectCrate[4]).value(B2DVars.EPPM*ObjectVars.objectCrate[5]).value(B2DVars.EPPM*ObjectVars.objectCrate[6]).value(B2DVars.EPPM*ObjectVars.objectCrate[7]).endArray();
		json.endObject(); // End of Crate
		return 1;
	}

	public static int ImageDoor(JSONStringer json, float[] fs, int bodyIndex, int cnt, int doorcolor) throws JSONException {
		float xcen = B2DVars.EPPM*0.5f*(fs[0]+fs[4]);
		float ycen = B2DVars.EPPM*0.5f*(fs[1]+fs[5]);
		float pSize = B2DVars.EPPM*ObjectVars.objectPadlock;
		float rotAngle = PolygonOperations.GetAngle(fs[0],fs[1],fs[2],fs[3]);
		json.object(); // Start of Gate
		json.key("name").value("Gate"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(1);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(B2DVars.EPPM*fs[0]-xcen).value(B2DVars.EPPM*fs[2]-xcen).value(B2DVars.EPPM*fs[4]-xcen).value(B2DVars.EPPM*fs[6]-xcen).endArray();
		json.key("y").array().value(B2DVars.EPPM*fs[1]-ycen).value(B2DVars.EPPM*fs[3]-ycen).value(B2DVars.EPPM*fs[5]-ycen).value(B2DVars.EPPM*fs[7]-ycen).endArray();
		json.endObject();
		json.key("file").value("images/metal_pole_1x16.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(B2DVars.EPPM*ObjectVars.objectDoor[0]).value(B2DVars.EPPM*ObjectVars.objectDoor[1]).value(B2DVars.EPPM*ObjectVars.objectDoor[2]).value(B2DVars.EPPM*ObjectVars.objectDoor[3]).value(B2DVars.EPPM*ObjectVars.objectDoor[4]).value(B2DVars.EPPM*ObjectVars.objectDoor[5]).value(B2DVars.EPPM*ObjectVars.objectDoor[6]).value(B2DVars.EPPM*ObjectVars.objectDoor[7]).endArray();
		json.endObject(); // End of Gate
		json.object(); // Start of Padlock
		json.key("name").value("Padlock"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(1);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-pSize).value(pSize).value(pSize).value(-pSize).endArray();
		json.key("y").array().value(-pSize).value(-pSize).value(pSize).value(pSize).endArray();
		json.endObject();
		if (doorcolor == ObjectVars.DoorRed) json.key("file").value("images/padlock_red.png");
		else if (doorcolor == ObjectVars.DoorGreen) json.key("file").value("images/padlock_green.png");
		else if (doorcolor == ObjectVars.DoorBlue) json.key("file").value("images/padlock_blue.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-pSize).value(-pSize).value(pSize).value(-pSize).value(pSize).value(pSize).value(-pSize).value(pSize).endArray();
		json.endObject(); // End of Padlock
		return 1;
	}

	public static int ImageFinish(JSONStringer json, float[] fs, int bodyIndex) throws JSONException {
		float rd = ObjectVars.objectFinishBall[2]*B2DVars.EPPM;
		json.object();
		json.key("name").value("Finish");
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/finish_ball.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject();
		return 1;
	}

	public static int ImageJewel(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float rotAngle = PolygonOperations.GetAngle(fs[0], fs[1], fs[4], fs[5]);
		float jWidth = B2DVars.EPPM*ObjectVars.objectJewel[4];
		float[] xarr = {-jWidth, jWidth, jWidth, -jWidth};
		float[] yarr = {-jWidth, -jWidth, jWidth, jWidth};
		PolygonOperations.RotateArray(xarr, yarr, rotAngle, 0.0f, 0.0f);
		json.object(); // Start of Jewel
		json.key("name").value("Jewel"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(xarr[0]).value(xarr[1]).value(xarr[2]).value(xarr[3]).endArray();
		json.key("y").array().value(yarr[0]).value(yarr[1]).value(yarr[2]).value(yarr[3]).endArray();
		json.endObject();
		json.key("file").value("images/gem_gold.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		//json.key("glTexCoordPointer").array().value(0).value(0).value(0.25f).value(0).value(0.25f).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-jWidth).value(-jWidth).value(jWidth).value(-jWidth).value(jWidth).value(jWidth).value(-jWidth).value(jWidth).endArray();
		json.endObject(); // End of Jewel
		return 1;
	}

	public static int ImageJewelDiamond(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float rotAngle = PolygonOperations.GetAngle(fs[0], fs[1], fs[4], fs[5]);
		float jWidth = B2DVars.EPPM*ObjectVars.objectJewel[4];
		float[] xarr = {-jWidth, jWidth, jWidth, -jWidth};
		float[] yarr = {-jWidth, -jWidth, jWidth, jWidth};
		PolygonOperations.RotateArray(xarr, yarr, rotAngle, 0.0f, 0.0f);
		json.object(); // Start of Jewel
		json.key("name").value("Diamond"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(xarr[0]).value(xarr[1]).value(xarr[2]).value(xarr[3]).endArray();
		json.key("y").array().value(yarr[0]).value(yarr[1]).value(yarr[2]).value(yarr[3]).endArray();
		json.endObject();
		json.key("file").value("images/gem_diamond.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-jWidth).value(-jWidth).value(jWidth).value(-jWidth).value(jWidth).value(jWidth).value(-jWidth).value(jWidth).endArray();
		json.endObject(); // End of Jewel
		return 1;
	}

	public static int ImageGravity(JSONStringer json, float[] fs, int bodyIndex, int cnt, Vector2 gravityVec) throws JSONException {
		float rd = 20.3f*B2DVars.EPPM;
		float rotAngle = PolygonOperations.GetAngle(0.0f, 0.0f, gravityVec.x, gravityVec.y) + (float)Math.PI/2;
		json.object();
		json.key("name").value("Gravity"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/gravity.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject();
		return 1;
	}

	public static int ImageKey(JSONStringer json, float[] fs, int bodyIndex, int cnt, int keycolor) throws JSONException {
		float xcen = 0.5f*B2DVars.EPPM*(fs[0]+fs[4]);
		float ycen = 0.5f*B2DVars.EPPM*(fs[1]+fs[5]);
		float rotAngle = PolygonOperations.GetAngle(fs[0], fs[1], fs[2], fs[3]);
		json.object(); // Start of Key
		json.key("name").value("Key"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(B2DVars.EPPM*fs[0]-xcen).value(B2DVars.EPPM*fs[2]-xcen).value(B2DVars.EPPM*fs[4]-xcen).value(B2DVars.EPPM*fs[6]-xcen).endArray();
		json.key("y").array().value(B2DVars.EPPM*fs[1]-ycen).value(B2DVars.EPPM*fs[3]-ycen).value(B2DVars.EPPM*fs[5]-ycen).value(B2DVars.EPPM*fs[7]-ycen).endArray();
		json.endObject();
		if (keycolor == ObjectVars.KeyRed) json.key("file").value("images/key_red.png");
		else if (keycolor == ObjectVars.KeyGreen) json.key("file").value("images/key_green.png");
		else if (keycolor == ObjectVars.KeyBlue) json.key("file").value("images/key_blue.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(B2DVars.EPPM*ObjectVars.objectKey[0]).value(B2DVars.EPPM*ObjectVars.objectKey[1]).value(B2DVars.EPPM*ObjectVars.objectKey[2]).value(B2DVars.EPPM*ObjectVars.objectKey[3]).value(B2DVars.EPPM*ObjectVars.objectKey[4]).value(B2DVars.EPPM*ObjectVars.objectKey[5]).value(B2DVars.EPPM*ObjectVars.objectKey[6]).value(B2DVars.EPPM*ObjectVars.objectKey[7]).endArray();
		json.endObject(); // End of Key
		return 1;
	}

	public static int ImageLog(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float rd = fs[2]*B2DVars.EPPM;
		json.object();
		json.key("name").value("Log"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/log.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject();
		return 1;
	}

	public static int ImageNitrous(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float xcen = 0.5f*B2DVars.EPPM*(fs[0]+fs[4]);
		float ycen = 0.5f*B2DVars.EPPM*(fs[1]+fs[5]);
		float rotAngle = PolygonOperations.GetAngle(fs[0], fs[1], fs[2], fs[3]);
		json.object(); // Start of Nitrous
		json.key("name").value("Nitrous"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(B2DVars.EPPM*fs[0]-xcen).value(B2DVars.EPPM*fs[2]-xcen).value(B2DVars.EPPM*fs[4]-xcen).value(B2DVars.EPPM*fs[6]-xcen).endArray();
		json.key("y").array().value(B2DVars.EPPM*fs[1]-ycen).value(B2DVars.EPPM*fs[3]-ycen).value(B2DVars.EPPM*fs[5]-ycen).value(B2DVars.EPPM*fs[7]-ycen).endArray();
		json.endObject();
		json.key("file").value("images/nitrous.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(B2DVars.EPPM*ObjectVars.objectNitrous[0]).value(B2DVars.EPPM*ObjectVars.objectNitrous[1]).value(B2DVars.EPPM*ObjectVars.objectNitrous[2]).value(B2DVars.EPPM*ObjectVars.objectNitrous[3]).value(B2DVars.EPPM*ObjectVars.objectNitrous[4]).value(B2DVars.EPPM*ObjectVars.objectNitrous[5]).value(B2DVars.EPPM*ObjectVars.objectNitrous[6]).value(B2DVars.EPPM*ObjectVars.objectNitrous[7]).endArray();
		json.endObject(); // End of Nitrous
		return 1;
	}

	public static int ImagePendulum(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float rd = fs[2]*B2DVars.EPPM;
		float brd = 0.2f*rd;
		float xv = 0.5f*fs[5]*B2DVars.EPPM;
		float yv = 0.5f*fs[6]*B2DVars.EPPM;
		json.object();
		json.key("name").value("PendulumBall"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/boulder.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject(); // End of Pendulum Ball
		json.object(); // Start of Pendulum Anchor
		json.key("name").value("PendulumAnchor"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex+1);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-xv).value(xv).value(xv).value(-xv).endArray();
		json.key("y").array().value(-yv).value(-yv).value(yv).value(yv).endArray();
		json.endObject();
		json.key("file").value("images/metalplate.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-xv).value(-yv).value(xv).value(-yv).value(xv).value(yv).value(-xv).value(yv).endArray();
		json.endObject(); // End of Pendulum Anchor
		float shaftLength = B2DVars.EPPM * (float) Math.sqrt( Math.pow(fs[0]-(fs[3]+0.5f*fs[5]),2) + Math.pow(fs[1]-(fs[4]+0.5f*fs[6]),2) );
		float[] xshaft = {-0.03f, 0.03f, 0.03f, -0.03f};
		float[] yshaft = {-0.5f*shaftLength, -0.5f*shaftLength, 0.5f*shaftLength, 0.5f*shaftLength};
		float rotAngle;
		if (fs[0] == fs[3]+fs[5]*0.5f) {
			rotAngle = 0.0f;
		} else if (fs[1] == fs[4]+fs[6]*0.5f) {
			rotAngle = 90.0f; // No rotation needed
			PolygonOperations.RotateArray(xshaft,yshaft,rotAngle,0.0f,0.0f);
		} else {
			rotAngle = 90.0f + MathUtils.radiansToDegrees * (float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
			PolygonOperations.RotateArray(xshaft,yshaft,rotAngle,0.0f,0.0f);
		}
		json.object(); // Start of Pendulum Shaft
		json.key("name").value("PendulumShaft"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(1);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(MathUtils.degreesToRadians*rotAngle);
		json.key("body").value(bodyIndex+2);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(xshaft[0]).value(xshaft[1]).value(xshaft[2]).value(xshaft[3]).endArray();
		json.key("y").array().value(yshaft[0]).value(yshaft[1]).value(yshaft[2]).value(yshaft[3]).endArray();
		json.endObject();
		json.key("file").value("images/metal_pole_1x16.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		//json.key("glVertexPointer").array().value(xshaft[0]).value(yshaft[0]).value(xshaft[1]).value(yshaft[1]).value(xshaft[2]).value(yshaft[2]).value(xshaft[3]).value(yshaft[3]).endArray();
		//json.key("glVertexPointer").array().value(-0.03f).value(-0.5f*shaftLength).value(0.03f).value(-0.5f*shaftLength).value(0.03f).value(0.05f*shaftLength).value(-0.03f).value(0.5f*shaftLength).endArray();
		json.key("glVertexPointer").array().value(-0.5f*shaftLength).value(-0.03f).value(0.5f*shaftLength).value(-0.03f).value(0.5f*shaftLength).value(0.03f).value(-0.5f*shaftLength).value(0.03f).endArray();
		json.endObject(); // End of Pendulum Shaft
		json.object(); // Start of Pendulum Ball Bolt
		json.key("name").value("PendulumBallBolt"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(2);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-brd).value(brd).value(brd).value(-brd).endArray();
		json.key("y").array().value(-brd).value(-brd).value(brd).value(brd).endArray();
		json.endObject();
		json.key("file").value("images/bolt.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-brd).value(-brd).value(brd).value(-brd).value(brd).value(brd).value(-brd).value(brd).endArray();
		json.endObject(); // End of Pendulum Ball Bolt
		json.object(); // Start of Pendulum Anchor Bolt
		json.key("name").value("PendulumAnchorBolt"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(2);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex+1);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-brd).value(brd).value(brd).value(-brd).endArray();
		json.key("y").array().value(-brd).value(-brd).value(brd).value(brd).endArray();
		json.endObject();
		json.key("file").value("images/bolt.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-brd).value(-brd).value(brd).value(-brd).value(brd).value(brd).value(-brd).value(brd).endArray();
		json.endObject(); // End of Pendulum Anchor Bolt
		return 3;
	}

	public static int ImagePlanet(JSONStringer json, float[] fs, int bodyIndex, int cnt, int dType) throws JSONException {
		String img_fname = "";
		if (dType==ObjectVars.PlanetMercury) img_fname="images/planet_mercury.png";
		else if (dType==ObjectVars.PlanetVenus) img_fname="images/planet_venus.png";
		else if (dType==ObjectVars.PlanetEarth) img_fname="images/planet_earth.png";
		else if (dType==ObjectVars.PlanetMars) img_fname="images/planet_mars.png";
		else if (dType==ObjectVars.PlanetJupiter) img_fname="images/planet_jupiter.png";
		else if (dType==ObjectVars.PlanetSaturn) img_fname="images/planet_saturn.png";
		else if (dType==ObjectVars.PlanetUranus) img_fname="images/planet_uranus.png";
		else if (dType==ObjectVars.PlanetNeptune) img_fname="images/planet_neptune.png";
		else if (dType==ObjectVars.PlanetSun) img_fname="images/planet_sun.png";
		int retval = 1;
		if (dType==ObjectVars.PlanetSaturn) {
			retval = ImagePlanetPolygon(json, fs, bodyIndex, cnt, img_fname);
		} else {
			if (dType==ObjectVars.PlanetSun) retval = ImagePlanetCircle(json, fs, bodyIndex, cnt, img_fname, 1.35211f);
			else retval = ImagePlanetCircle(json, fs, bodyIndex, cnt, img_fname, 1.0f);
		}
		return retval;
	}

	public static int ImagePlanetPolygon(JSONStringer json, float[] fs, int bodyIndex, int cnt, String image_fname) throws JSONException {
		// Get the center, xmin, xmax, ymin, ymax of the planet
		float xcen = 0.0f;
		float ycen = 0.0f;
		float xmin=0, ymin=0, xmax=0, ymax=0;
		for (int pp=0; pp<fs.length/2; pp++) {
			xcen += fs[2*pp];
			ycen += fs[2*pp+1];
			// x extremes
			if ((fs[2*pp] < xmin) || (pp == 0)) {
				xmin = fs[2*pp];
			}
			if ((fs[2*pp] > xmax) || (pp == 0)) {
				xmax = fs[2*pp];
			}
			// y extremes
			if ((fs[2*pp+1] < ymin) || (pp == 0)) {
				ymin = fs[2*pp+1];
			}
			if ((fs[2*pp+1] > ymax) || (pp == 0)) {
				ymax = fs[2*pp+1];
			}
		}
		xcen *= B2DVars.EPPM/(fs.length/2);
		ycen *= B2DVars.EPPM/(fs.length/2);
		xmin *= B2DVars.EPPM;
		ymin *= B2DVars.EPPM;
		xmax *= B2DVars.EPPM;
		ymax *= B2DVars.EPPM;
		//
		float rotAngle = 0.0f;
		json.object(); // Start of Nitrous
		json.key("name").value("Planet"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(xmin-xcen).value(xmax-xcen).value(xmax-xcen).value(xmin-xcen).endArray();
		json.key("y").array().value(ymin-ycen).value(ymin-ycen).value(ymax-ycen).value(ymax-ycen).endArray();
		json.endObject();
		json.key("file").value(image_fname);
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		//json.key("glVertexPointer").array().value(B2DVars.EPPM*ObjectVars.objectNitrous[0]).value(B2DVars.EPPM*ObjectVars.objectNitrous[1]).value(B2DVars.EPPM*ObjectVars.objectNitrous[2]).value(B2DVars.EPPM*ObjectVars.objectNitrous[3]).value(B2DVars.EPPM*ObjectVars.objectNitrous[4]).value(B2DVars.EPPM*ObjectVars.objectNitrous[5]).value(B2DVars.EPPM*ObjectVars.objectNitrous[6]).value(B2DVars.EPPM*ObjectVars.objectNitrous[7]).endArray();
		// This probably needs to change if the rotation changes...
		json.key("glVertexPointer").array().value(xmin-xcen).value(ymin-ycen).value(xmax-xcen).value(ymin-ycen).value(xmax-xcen).value(ymax-ycen).value(xmin-xcen).value(ymax-ycen).endArray();
		json.endObject(); // End of Planet
		return 1;
	}

	public static int ImagePlanetCircle(JSONStringer json, float[] fs, int bodyIndex, int cnt, String image_fname, float scale) throws JSONException {
		float rd = scale*fs[2]*B2DVars.EPPM;
		json.object();
		json.key("name").value("Planet"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value(image_fname);
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject();
		return 1;
	}

	public static int ImageSpike(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float rd = fs[2]*B2DVars.EPPM;
		json.object();
		json.key("name").value("Spike"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/spike.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject();
		return 1;
	}

	public static int ImageTransport(JSONStringer json, float[] fs, int bodyIndex, int cnt) throws JSONException {
		float xcen = 0.5f*B2DVars.EPPM*(fs[0]+fs[4]);
		float ycen = 0.5f*B2DVars.EPPM*(fs[1]+fs[5]);
		float rotAngle = PolygonOperations.GetAngle(fs[0], fs[1], fs[2], fs[3]);
		float xp = 0.42f*B2DVars.EPPM*ObjectVars.objectTransport[5]*(float)Math.cos(rotAngle+Math.PI/2);
		float yp = 0.42f*B2DVars.EPPM*ObjectVars.objectTransport[5]*(float)Math.sin(rotAngle+Math.PI/2);
		json.object(); // Start of Transport A
		json.key("name").value("TransportA"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(B2DVars.EPPM*fs[0]-xcen-xp).value(B2DVars.EPPM*fs[2]-xcen-xp).value(B2DVars.EPPM*fs[4]-xcen+xp).value(B2DVars.EPPM*fs[6]-xcen+xp).endArray();
		json.key("y").array().value(B2DVars.EPPM*fs[1]-ycen-yp).value(B2DVars.EPPM*fs[3]-ycen-yp).value(B2DVars.EPPM*fs[5]-ycen+yp).value(B2DVars.EPPM*fs[7]-ycen+yp).endArray();
		json.endObject();
		json.key("file").value("images/transport_spiral.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(B2DVars.EPPM*ObjectVars.objectTransport[0]).value(B2DVars.EPPM*ObjectVars.objectTransport[1]).value(B2DVars.EPPM*ObjectVars.objectTransport[2]).value(B2DVars.EPPM*ObjectVars.objectTransport[3]).value(B2DVars.EPPM*ObjectVars.objectTransport[4]).value(B2DVars.EPPM*ObjectVars.objectTransport[5]).value(B2DVars.EPPM*ObjectVars.objectTransport[6]).value(B2DVars.EPPM*ObjectVars.objectTransport[7]).endArray();
		json.endObject(); // End of Transport A
		xcen = 0.5f*B2DVars.EPPM*(fs[8]+fs[12]);
		ycen = 0.5f*B2DVars.EPPM*(fs[9]+fs[13]);
		rotAngle = PolygonOperations.GetAngle(fs[8], fs[9], fs[10], fs[11]);
		xp = 0.42f*B2DVars.EPPM*ObjectVars.objectTransport[5]*(float)Math.cos(rotAngle+Math.PI/2);
		yp = 0.42f*B2DVars.EPPM*ObjectVars.objectTransport[5]*(float)Math.sin(rotAngle+Math.PI/2);
		json.object(); // Start of Transport B
		json.key("name").value("TransportB"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("body").value(bodyIndex+1);
		json.key("center");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(B2DVars.EPPM*fs[8]-xcen-xp).value(B2DVars.EPPM*fs[10]-xcen-xp).value(B2DVars.EPPM*fs[12]-xcen+xp).value(B2DVars.EPPM*fs[14]-xcen+xp).endArray();
		json.key("y").array().value(B2DVars.EPPM*fs[9]-ycen-yp).value(B2DVars.EPPM*fs[11]-ycen-yp).value(B2DVars.EPPM*fs[13]-ycen+yp).value(B2DVars.EPPM*fs[15]-ycen+yp).endArray();
		json.endObject();
		json.key("file").value("images/transport_spiral.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(B2DVars.EPPM*ObjectVars.objectTransport[0]).value(B2DVars.EPPM*ObjectVars.objectTransport[1]).value(B2DVars.EPPM*ObjectVars.objectTransport[2]).value(B2DVars.EPPM*ObjectVars.objectTransport[3]).value(B2DVars.EPPM*ObjectVars.objectTransport[4]).value(B2DVars.EPPM*ObjectVars.objectTransport[5]).value(B2DVars.EPPM*ObjectVars.objectTransport[6]).value(B2DVars.EPPM*ObjectVars.objectTransport[7]).endArray();
		json.endObject(); // End of Transport B
		return 2;
	}

	public static int ImageFallingSign(JSONStringer json, float[] fpo, float[] fpa, int bodyIndex, int cnt) throws JSONException {
		float rd = 30.0f*B2DVars.EPPM;
		float shaftLength = 5.0f*rd;
		float[] xshaft = {-0.03f, 0.03f, 0.03f, -0.03f};
		float[] yshaft = {-shaftLength/2.0f, -shaftLength/2.0f, shaftLength/2.0f, shaftLength/2.0f};
		json.object(); // Start of Road Sign Post
		json.key("name").value("FallingSign"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(1);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0.0f);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value((fpa[2]-fpo[0])*B2DVars.EPPM);
		json.key("y").value((fpa[3]-fpo[1])*B2DVars.EPPM +0.5f*shaftLength);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(xshaft[0]).value(xshaft[1]).value(xshaft[2]).value(xshaft[3]).endArray();
		json.key("y").array().value(yshaft[0]).value(yshaft[1]).value(yshaft[2]).value(yshaft[3]).endArray();
		json.endObject();
		json.key("file").value("images/metal_pole_1x16.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-0.03f).value(-0.5f*shaftLength).value(0.03f).value(-0.5f*shaftLength).value(0.03f).value(0.5f*shaftLength).value(0.03f).value(0.5f*shaftLength).endArray();
		json.endObject(); // End of Road Sign Post
		json.object();
		json.key("name").value("FallingSign"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(0);
		json.key("body").value(bodyIndex);
		json.key("center");
		json.object();
		json.key("x").value((fpa[2]-fpo[0])*B2DVars.EPPM);
		json.key("y").value((fpa[3]-fpo[1])*B2DVars.EPPM + shaftLength);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value("images/RS_exclamation.png");
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject();
		return 1;
	}
}
