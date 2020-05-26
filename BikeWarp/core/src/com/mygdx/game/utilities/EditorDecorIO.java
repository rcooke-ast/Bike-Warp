package com.mygdx.game.utilities;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.DecorVars;
import com.mygdx.game.handlers.ObjectVars;
import com.mygdx.game.utilities.json.JSONException;
import com.mygdx.game.utilities.json.JSONStringer;

public class EditorDecorIO {

	public static void AddDecorations(JSONStringer json, ArrayList<float[]> decors, ArrayList<Integer> decorTypes) throws JSONException {
        for (int i = 0; i<decors.size(); i++){
        	if (decorTypes.get(i) == DecorVars.RoadSign_Stop) {
        		ImageRoadSign(json, decors.get(i), "images/RS_stop.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_DoNotEnter) {
        		ImageRoadSign(json, decors.get(i), "images/RS_donotenter.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_Bumps) {
        		ImageRoadSign(json, decors.get(i), "images/RS_bumps.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_Exclamation) {
        		ImageRoadSign(json, decors.get(i), "images/RS_exclamation.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_Motorbike) {
        		ImageRoadSign(json, decors.get(i), "images/RS_motorbike.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_NoMotorbike) {
        		ImageRoadSign(json, decors.get(i), "images/RS_nomotorbike.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_RampAhead) {
        		ImageRoadSign(json, decors.get(i), "images/RS_rampahead.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_ReduceSpeed) {
        		ImageRoadSign(json, decors.get(i), "images/RS_reducespeed.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_10) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_10.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_20) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_20.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_30) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_30.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_40) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_40.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_50) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_50.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_60) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_60.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_80) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_80.png", i);
        	} else if (decorTypes.get(i) == DecorVars.RoadSign_100) {
        		ImageRoadSign(json, decors.get(i), "images/RS_speed_100.png", i);
//        	} else if (decorTypes.get(i) == DecorVars.Waterfall) {
//        		ImageWaterfall(json, decors.get(i), "images/waterfall.png", i);
        	}
        }
		return;
	}

	public static void ImageRoadSign(JSONStringer json, float[] fs, String imageFile, int cnt) throws JSONException {
		float rd = fs[2]*B2DVars.EPPM;
		float shaftLength = 5.0f*rd;
		float[] xshaft = {-0.03f, 0.03f, 0.03f, -0.03f};
		float[] yshaft = {-shaftLength/2.0f, -shaftLength/2.0f, shaftLength/2.0f, shaftLength/2.0f};
		PolygonOperations.RotateArray(xshaft,yshaft,MathUtils.radiansToDegrees*fs[3],0.0f,0.0f);
		float[] cenv = PolygonOperations.RotateCoordinate(fs[0], fs[1]-0.5f*shaftLength/B2DVars.EPPM, MathUtils.radiansToDegrees*fs[3], fs[0], fs[1]);
		json.object(); // Start of Road Sign Post
		json.key("name").value("Decor"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(1);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(fs[3]);
		json.key("center");
		json.object();
		json.key("x").value(cenv[0]*B2DVars.EPPM);
		json.key("y").value(cenv[1]*B2DVars.EPPM);
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
//		json.key("glVertexPointer").array().value(-0.5f*shaftLength).value(-0.03f).value(0.5f*shaftLength).value(-0.03f).value(0.5f*shaftLength).value(0.03f).value(-0.5f*shaftLength).value(0.03f).endArray();
		json.key("glVertexPointer").array().value(-0.03f).value(-0.5f*shaftLength).value(0.03f).value(-0.5f*shaftLength).value(0.03f).value(0.5f*shaftLength).value(0.03f).value(0.5f*shaftLength).endArray();
		json.endObject(); // End of Road Sign Post
		json.object(); // Start of Road Sign
		json.key("name").value("Decor"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(fs[3]);
		json.key("center");
		json.object();
		json.key("x").value(fs[0]*B2DVars.EPPM);
		json.key("y").value(fs[1]*B2DVars.EPPM);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(-rd).value(rd).value(rd).value(-rd).endArray();
		json.key("y").array().value(-rd).value(-rd).value(rd).value(rd).endArray();
		json.endObject();
		json.key("file").value(imageFile);
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
		json.key("glVertexPointer").array().value(-rd).value(-rd).value(rd).value(-rd).value(rd).value(rd).value(-rd).value(rd).endArray();
		json.endObject(); // End of Road Sign
		return;
	}

	
//	public static void ImageWaterfall(JSONStringer json, float[] fs, String imageFile, int cnt) throws JSONException {
//		// Define some variables
//		ArrayList<Vector2> concaveVertices = PolygonOperations.MakeVertices(fs);
//		ArrayList<ArrayList<Vector2>> convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
//		ArrayList<float[]> convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
//		for (int k = 0; k<convexPolygons.size(); k++) {
//         	json.object();
//            // Specify other properties of this fixture
//        	json.key("density").value(1);
//            json.key("friction").value(0);
//            json.key("restitution").value(0);
//            json.key("name").value("fixture8");
//            json.key("userData").value("Waterfall");
//            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
//            json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
//            // Set the (background) ground texture
//            json.key("customProperties");
//            json.array();
//            json.object();
//            json.key("name").value("TextureMask");
//            json.key("string").value(imageFile);
//            json.endObject();
//            json.endArray();
//			json.key("polygon");
//            json.object(); // Begin polygon object
//            json.key("vertices");
//            json.object(); // Begin vertices object
//            json.key("x");
//            json.array();
//            for (int j = 0; j<convexPolygons.get(k).length/2; j++){
//            	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j]);
//            }
//            json.endArray();
//            json.key("y");
//            json.array();
//            for (int j = 0; j<convexPolygons.get(k).length/2; j++){
//            	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j+1]);
//            }
//            json.endArray();
//            json.endObject(); // End the vertices object
//            json.endObject(); // End polygon object
//            json.endObject(); // End this fixture
//		}
//	}
	
	public static void ImageThisDoesNothing(JSONStringer json, float[] fs, int bodyIndex, int cnt, int doorcolor) throws JSONException {
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
		return;
	}

}
