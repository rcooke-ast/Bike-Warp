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
        	if (DecorVars.IsRoadSign(decorTypes.get(i))) {
        		String imageFile = DecorVars.GetRoadSignFilename(decorTypes.get(i));
        		ImageRoadSign(json, decors.get(i), imageFile, i);
			} else if (DecorVars.IsRect(decorTypes.get(i))) {
        		ImageRect(json, decors.get(i), decorTypes.get(i), i);
        	} else if (decorTypes.get(i) == DecorVars.Track) {
        		ImageTrack(json, decors.get(i), i);
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
		json.key("renderOrder").value(0);
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

	public static void ImageTrack(JSONStringer json, float[] fs, int cnt) throws JSONException {
		float trackLength = B2DVars.EPPM*DecorVars.trackLength;
		float trackHeight = B2DVars.EPPM*DecorVars.trackLength/6.0f;  // This is actually half the height
		float offset;
		for (int tt=0; tt<(int)fs[3]; tt++) {
			offset = tt*trackLength;
			float[] xshaft = {-trackLength/2.0f, trackLength/2.0f, trackLength/2.0f, -trackLength/2.0f};
			float[] yshaft = {-trackHeight, -trackHeight, trackHeight, trackHeight};
			PolygonOperations.RotateArray(xshaft,yshaft,MathUtils.radiansToDegrees*fs[2],0.0f,0.0f);
			float[] cenv = PolygonOperations.RotateCoordinate(fs[0]+(offset+0.5f*trackLength)/B2DVars.EPPM, fs[1], MathUtils.radiansToDegrees*fs[2], fs[0], fs[1]);
			json.object(); // Start of Track
			json.key("name").value("Decor"+cnt);
			json.key("opacity").value(1);
			json.key("renderOrder").value(0);
			json.key("scale").value(1);
			json.key("aspectScale").value(1);
			json.key("angle").value(fs[2]);
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
			json.key("file").value("images/track.png");
			json.key("filter").value(1);
			json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
			json.key("glTexCoordPointer").array().value(0).value(1).value(1).value(1).value(1).value(0).value(0).value(0).endArray();
			float unittrack = B2DVars.EPPM*DecorVars.trackLength;
			json.key("glVertexPointer").array().value(-trackHeight).value(-0.5f*unittrack).value(trackHeight).value(-0.5f*unittrack).value(trackHeight).value(0.5f*unittrack).value(trackHeight).value(0.5f*unittrack).endArray();
			json.endObject(); // End of Track
		}
		return;
	}

//	public static void ImageTrack(JSONStringer json, float[] fs, int cnt) throws JSONException {
//		float trackLength = B2DVars.EPPM*DecorVars.trackLength*fs[3];
//		float trackHeight = B2DVars.EPPM*DecorVars.trackLength/6.0f;  // This is actually half the height
//		float[] xshaft = {-trackLength/2.0f, trackLength/2.0f, trackLength/2.0f, -trackLength/2.0f};
//		float[] yshaft = {-trackHeight, -trackHeight, trackHeight, trackHeight};
//		PolygonOperations.RotateArray(xshaft,yshaft,MathUtils.radiansToDegrees*fs[2],0.0f,0.0f);
//		float[] cenv = PolygonOperations.RotateCoordinate(fs[0]+0.5f*trackLength/B2DVars.EPPM, fs[1], MathUtils.radiansToDegrees*fs[2], fs[0], fs[1]);
//		json.object(); // Start of Track
//		json.key("name").value("Decor"+cnt);
//		json.key("opacity").value(1);
//		json.key("renderOrder").value(0);
//		json.key("scale").value(1);
//		json.key("aspectScale").value(1);
//		json.key("angle").value(fs[2]);
//		json.key("center");
//		json.object();
//		json.key("x").value(cenv[0]*B2DVars.EPPM);
//		json.key("y").value(cenv[1]*B2DVars.EPPM);
//		json.endObject();
//		json.key("corners");
//		json.object();
//		json.key("x").array().value(xshaft[0]).value(xshaft[1]).value(xshaft[2]).value(xshaft[3]).endArray();
//		json.key("y").array().value(yshaft[0]).value(yshaft[1]).value(yshaft[2]).value(yshaft[3]).endArray();
//		json.endObject();
//		json.key("file").value("images/track.png");
//		json.key("filter").value(1);
//		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
//		json.key("glTexCoordPointer").array().value(0).value(1).value(1).value(1).value(1).value(0).value(0).value(0).endArray();
//		float unittrack = B2DVars.EPPM*DecorVars.trackLength;
//		json.key("glVertexPointer").array().value(-trackHeight).value(-0.5f*unittrack).value(trackHeight).value(-0.5f*unittrack).value(trackHeight).value(0.5f*unittrack).value(trackHeight).value(0.5f*unittrack).endArray();
//		json.endObject(); // End of Track
//		return;
//	}

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
	
	public static void ImageRect(JSONStringer json, float[] fs, int decorID, int cnt) throws JSONException {
		// This routine can be used for any image that is the shape of a rectangle
		float xcen = 0.5f*B2DVars.EPPM*(fs[0]+fs[4]);
		float ycen = 0.5f*B2DVars.EPPM*(fs[1]+fs[5]);
		float rotAngle = PolygonOperations.GetAngle(fs[0], fs[1], fs[2], fs[3]);
		// Set the Image properties
		String imageFile = DecorVars.GetImageRect(decorID, (int) fs[8]);
		float[] coord = DecorVars.GetCoordRect(decorID, (int) fs[8]);
		// Add image
		json.object(); // Start of Rectangular Image
		json.key("name").value("Decor"+cnt);
		json.key("opacity").value(1);
		json.key("renderOrder").value(0);
		json.key("scale").value(1);
		json.key("aspectScale").value(1);
		json.key("angle").value(rotAngle);
		json.key("center");
		json.object();
		json.key("x").value(xcen);
		json.key("y").value(ycen);
		json.endObject();
		json.key("corners");
		json.object();
		json.key("x").array().value(B2DVars.EPPM*fs[0]).value(B2DVars.EPPM*fs[2]).value(B2DVars.EPPM*fs[4]).value(B2DVars.EPPM*fs[6]).endArray();
		json.key("y").array().value(B2DVars.EPPM*fs[1]).value(B2DVars.EPPM*fs[3]).value(B2DVars.EPPM*fs[5]).value(B2DVars.EPPM*fs[7]).endArray();
		json.endObject();
		json.key("file").value(imageFile);
		json.key("filter").value(1);
		json.key("glDrawElements").array().value(0).value(1).value(2).value(2).value(3).value(0).endArray();
		json.key("glTexCoordPointer").array().value(0).value(0).value(1).value(0).value(1).value(1).value(0).value(1).endArray();
//		json.key("glVertexPointer").array().value(-0.5f*shaftLength).value(-0.03f).value(0.5f*shaftLength).value(-0.03f).value(0.5f*shaftLength).value(0.03f).value(-0.5f*shaftLength).value(0.03f).endArray();
		json.key("glVertexPointer").array().value(coord[0]).value(coord[1]).value(coord[2]).value(coord[3]).value(coord[4]).value(coord[5]).value(coord[6]).value(coord[7]).endArray();
		json.endObject(); // End of decoration
	}

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
