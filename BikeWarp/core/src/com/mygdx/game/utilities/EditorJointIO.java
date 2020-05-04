package com.mygdx.game.utilities;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.ObjectVars;
import com.mygdx.game.utilities.NewJoint;
import com.mygdx.game.utilities.json.JSONException;
import com.mygdx.game.utilities.json.JSONStringer;

public class EditorJointIO {

	public static void AddJoints(JSONStringer json, ArrayList<NewJoint> jointList)  throws JSONException {
		for (int i = 0; i<jointList.size(); i++){
			json.object();
			if (jointList.get(i).jointType == "revolute") {
				json.key("type").value("revolute");
				json.key("name").value("joint"+i);
				json.key("anchorA");
				json.object();
				json.key("x").value(jointList.get(i).anchorA.x);
				json.key("y").value(jointList.get(i).anchorA.y);
				json.endObject();
				json.key("anchorB");
				json.object();
				json.key("x").value(jointList.get(i).anchorB.x);
				json.key("y").value(jointList.get(i).anchorB.y);
				json.endObject();
	            json.key("bodyA").value(jointList.get(i).bodyA);
				json.key("bodyB").value(jointList.get(i).bodyB);
				json.key("collideConnected").value(true);
				json.key("enableLimit").value(false);
				json.key("enableMotor").value(false);
				json.key("jointSpeed").value(0);
				json.key("lowerLimit").value(0);
				json.key("maxMotorTorque").value(0);
				json.key("motorSpeed").value(0);
				json.key("refAngle").value(0);
				json.key("upperLimit").value(0);
			} else if (jointList.get(i).jointType == "distance") {
				json.key("type").value("distance");
				json.key("name").value("joint"+i);
				json.key("anchorA");
				json.object();
				json.key("x").value(jointList.get(i).anchorA.x);
				json.key("y").value(jointList.get(i).anchorA.y);
				json.endObject();
				json.key("anchorB");
				json.object();
				json.key("x").value(jointList.get(i).anchorB.x);
				json.key("y").value(jointList.get(i).anchorB.y);
				json.endObject();
	            json.key("bodyA").value(jointList.get(i).bodyA);
				json.key("bodyB").value(jointList.get(i).bodyB);
				json.key("collideConnected").value(true);
				json.key("dampingRatio").value(0);
				json.key("frequency").value(0);
				json.key("length").value(jointList.get(i).length);
			} else if (jointList.get(i).jointType == "rope") {
				json.key("type").value("rope");
	            json.key("name").value("joint"+i); 
	            json.key("anchorA");
	            json.object();
	            json.key("x").value(jointList.get(i).anchorA.x);
	            json.key("y").value(jointList.get(i).anchorA.y);
	            json.endObject();
	            json.key("anchorB");
	            json.object();
	            json.key("x").value(jointList.get(i).anchorB.x);
	            json.key("y").value(jointList.get(i).anchorB.y);
	            json.endObject();
	            json.key("bodyA").value(jointList.get(i).bodyA);
				json.key("bodyB").value(jointList.get(i).bodyB);
				json.key("collideConnected").value(true);
	            json.key("maxLength").value(jointList.get(i).length); 
			} else if (jointList.get(i).jointType == "weld") {
				json.key("type").value("weld");
	            json.key("name").value("joint"+i); 
	            json.key("anchorA");
	            json.object();
	            json.key("x").value(jointList.get(i).anchorA.x);
	            json.key("y").value(jointList.get(i).anchorA.y);
	            json.endObject();
	            json.key("anchorB");
	            json.object();
	            json.key("x").value(jointList.get(i).anchorB.x);
	            json.key("y").value(jointList.get(i).anchorB.y);
	            json.endObject();
	            json.key("bodyA").value(jointList.get(i).bodyA);
				json.key("bodyB").value(jointList.get(i).bodyB);
				json.key("collideConnected").value(true);
	            json.key("refAngle").value(jointList.get(i).length); 
			} else System.out.println("Joint type unknown:" + jointList.get(i).jointType);
			json.endObject();
		}
		return;
	}

	public static void JointBallChain(ArrayList<NewJoint> jointList, float[] fs, int bodyIndex) {
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
		float cxEven = 0.0f, cxOdd = 0.0f;
		float cyEven = -halfWidth*imageScale + halfWidth;
		float cyOdd = halfWidth*imageScale - halfWidth;
		float[] cCoord;
		// Determine the ball-anchor axis rotation
		if (fs[0] == fs[3]+fs[5]*0.5f) rotAngleBA = 0.0f; // No rotation needed
		else rotAngleBA = 90.0f + MathUtils.radiansToDegrees*(float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
		if (fs[0] < fs[3]+fs[5]*0.5f) rotAngleBA += 180.0f;
		// Rotate the even pivot coordinate
		cCoord = PolygonOperations.RotateCoordinate(cxOdd,cyOdd,rotAngle,0.0f,0.0f);
		cxOdd = cCoord[0];
		cyOdd = cCoord[1];
		cCoord = PolygonOperations.RotateCoordinate(cxOdd,cyOdd,rotAngleBA,0.0f,0.0f);
		cxOdd = cCoord[0];
		cyOdd = cCoord[1];
		// Rotate the odd pivot coordinate
		cCoord = PolygonOperations.RotateCoordinate(cxEven,cyEven,-rotAngle,0.0f,0.0f);
		cxEven = cCoord[0];
		cyEven = cCoord[1];
		cCoord = PolygonOperations.RotateCoordinate(cxEven,cyEven,rotAngleBA,0.0f,0.0f);
		cxEven = cCoord[0];
		cyEven = cCoord[1];
		// Setup the joint between the ball and the first link
		Vector2 vecA = new Vector2(0.0f, 0.0f);
		Vector2 vecB = new Vector2(cxEven, cyEven);
		jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex, bodyIndex+2, -1.0f));
		// Setup the joints between all of the links
		//float xcen, ycen;
		//float chainShift = (float) (2.0f*linkLength*Math.cos(MathUtils.degreesToRadians*rotAngle));
		for (int i = 0; i < nLinks-1; i++) {
			if (i%2 == 0) {
				vecA = new Vector2(-cxEven, -cyEven);
				vecB = new Vector2(-cxOdd, -cyOdd);
			} else {
				vecA = new Vector2(cxOdd, cyOdd);				
				vecB = new Vector2(cxEven, cyEven);
			}
			jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex+i+2, bodyIndex+i+3, -1.0f));
		}
		// Setup the join between the anchor and the last link
		vecA = new Vector2(0.0f, 0.0f);
		vecB = new Vector2(cxOdd,cyOdd);
		jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex+1, bodyIndex+2+nLinks-1, -1.0f));
		// Ensure that the maximum distance between the anchor and the ball is shaftMaxLength
		vecA = new Vector2(0.0f, 0.0f);
		vecB = new Vector2(0.0f, 0.0f);
		jointList.add(new NewJoint("rope", vecA, vecB, bodyIndex+1, bodyIndex, shaftMaxLength));
		return;
	}

	public static void JointBridge(ArrayList<NewJoint> jointList, float[] fs, int bodyIndex) {
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
		float cxEven = 0.0f, cxOdd = 0.0f;
		float cyEven = -halfWidth*imageScale + halfWidth;
		float cyOdd = halfWidth*imageScale - halfWidth;
		float[] cCoord;
		// Determine the ball-anchor axis rotation
		if (fs[0] == fs[3]+fs[5]*0.5f) rotAngleBA = 0.0f; // No rotation needed
		else rotAngleBA = 90.0f + MathUtils.radiansToDegrees*(float) Math.atan( (fs[1] - (fs[4]+fs[6]*0.5f))/(fs[0] - (fs[3]+fs[5]*0.5f)) );
		if (fs[0] < fs[3]+fs[5]*0.5f) rotAngleBA += 180.0f;
		// Rotate the even pivot coordinate
		cCoord = PolygonOperations.RotateCoordinate(cxOdd,cyOdd,rotAngle,0.0f,0.0f);
		cxOdd = cCoord[0];
		cyOdd = cCoord[1];
		cCoord = PolygonOperations.RotateCoordinate(cxOdd,cyOdd,rotAngleBA,0.0f,0.0f);
		cxOdd = cCoord[0];
		cyOdd = cCoord[1];
		// Rotate the odd pivot coordinate
		cCoord = PolygonOperations.RotateCoordinate(cxEven,cyEven,-rotAngle,0.0f,0.0f);
		cxEven = cCoord[0];
		cyEven = cCoord[1];
		cCoord = PolygonOperations.RotateCoordinate(cxEven,cyEven,rotAngleBA,0.0f,0.0f);
		cxEven = cCoord[0];
		cyEven = cCoord[1];
		// Setup the joint between the ball and the first link
		Vector2 vecA = new Vector2(0.0f, 0.0f);
		Vector2 vecB = new Vector2(cxEven, cyEven);
		jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex, bodyIndex+2, -1.0f));
		// Setup the joints between all of the links
		//float xcen, ycen;
		//float chainShift = (float) (2.0f*linkLength*Math.cos(MathUtils.degreesToRadians*rotAngle));
		for (int i = 0; i < nLinks-1; i++) {
			if (i%2 == 0) {
				vecA = new Vector2(-cxEven, -cyEven);
				vecB = new Vector2(-cxOdd, -cyOdd);
			} else {
				vecA = new Vector2(cxOdd, cyOdd);				
				vecB = new Vector2(cxEven, cyEven);
			}
			jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex+i+2, bodyIndex+i+3, -1.0f));
		}
		// Setup the join between the anchor and the last link
		vecA = new Vector2(0.0f, 0.0f);
		vecB = new Vector2(cxOdd,cyOdd);
		jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex+1, bodyIndex+2+nLinks-1, -1.0f));
		// Ensure that the maximum distance between the anchor and the ball is shaftMaxLength
		vecA = new Vector2(0.0f, 0.0f);
		vecB = new Vector2(0.0f, 0.0f);
		jointList.add(new NewJoint("rope", vecA, vecB, bodyIndex+1, bodyIndex, shaftMaxLength));
		return;
	}

	public static void JointPendulum(ArrayList<NewJoint> jointList, float[] fs, int bodyIndex) {
		float xcen = 0.5f*B2DVars.EPPM*(fs[0] + fs[3]+0.5f*fs[5]);
		float ycen = 0.5f*B2DVars.EPPM*(fs[1] + fs[4]+0.5f*fs[6]);
		// Ball-Shaft Joint
		Vector2 vecA = new Vector2(0.0f, 0.0f);
		Vector2 vecB = new Vector2(B2DVars.EPPM*fs[0] - xcen, B2DVars.EPPM*fs[1] - ycen);
		jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex, bodyIndex+2, -1.0f));
		// Anchor-Shaft Joint
		vecA = new Vector2(0.0f, 0.0f);
		vecB = new Vector2(B2DVars.EPPM*(fs[3]+0.5f*fs[5]) - xcen, B2DVars.EPPM*(fs[4]+0.5f*fs[6]) - ycen);
		jointList.add(new NewJoint("revolute", vecA, vecB, bodyIndex+1, bodyIndex+2, -1.0f));
		return;
	}

	public static void JointFalling(ArrayList<NewJoint> jointList, float[] fs, int bodyIndex) {
		// Set the weld joint for the falling body
		Vector2 vecA = new Vector2(B2DVars.EPPM*fs[0], B2DVars.EPPM*fs[1]);
		Vector2 vecB = new Vector2(0.0f, 0.0f);
		jointList.add(new NewJoint("weld", vecA, vecB, 0, bodyIndex, 0.0f));
		return;
	}

	public static void JointTrigger(ArrayList<NewJoint> jointList, float[] fs, int bodyIndex) {
		// Set the weld joint for the trigger body
		Vector2 vecA = new Vector2(B2DVars.EPPM*fs[0], B2DVars.EPPM*fs[1]);
		Vector2 vecB = new Vector2(0.0f, 0.0f);
		jointList.add(new NewJoint("weld", vecA, vecB, 0, bodyIndex, 0.0f));
		return;
	}
}
