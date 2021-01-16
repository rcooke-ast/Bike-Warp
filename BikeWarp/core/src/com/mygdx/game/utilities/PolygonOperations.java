package com.mygdx.game.utilities;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gushikustudios.rube.PolySpatial;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.DecorVars;

public class PolygonOperations {

	public static float[] CalculateIntersection(float a, float b, float c, float d, float p, float q, float r, float s, boolean nbr) {
		float[] itsct = new float[2];
		float[] medArr = new float[4];
		float ga, gb, ia, ib, tst;
		if (c==a) { // First set is a vertical segment
			if (p==r) { // Both segments are vertical
				if (c==p) { //Both segments are at an identical x coordinate
					if (((b<q)&(b<s)&(d<q)&(d<s)) | ((b>q)&(b>s)&(d>q)&(d>s))) return null; //Segments are separate
					else {
						itsct[0] = a;
						medArr[0] = b;
						medArr[1] = d;
						medArr[2] = q;
						medArr[3] = s;
						Arrays.sort(medArr);
						itsct[1] = 0.5f*(medArr[1]+medArr[2]);
					}
				} else return null;
			} else { // The first segment is vertical, the second is not
				if (Intersects(a,b,c,d,p,q,r,s)) {
					gb = (q-s)/(p-r);
					ib = q-p*gb;
					tst = a*gb+ib;
					itsct[0]=a;
					if (b<d) {
						if ((tst>b)&(tst<d)&(!nbr)) itsct[1]=tst;
						else return null;
					} else {
						if ((tst>d)&(tst<b)&(!nbr)) itsct[1]=tst;
						else return null;
					}
				} else return null;
			}
		} else if (p==r) { // Second set is a vertical segment, but the first is not
			if (Intersects(a,b,c,d,p,q,r,s)) {
				ga = (d-b)/(c-a);
				ia = b-a*ga;
				tst = p*ga+ia;
				itsct[0]=p;
				if (q<s) {
					if ((q<tst)&(tst<s)&(!nbr)) itsct[1]=tst;
					else return null;
				} else {
					if ((tst>s)&(tst<q)&(!nbr)) itsct[1]=tst;
					else return null;
				}
			} else return null;
		} else if ((b==d)&(q==s)) { // Both segments are horizontal
			if (b==q) { // Segments have the same horizontal value
				if (((a<p)&(a<r)&(c<p)&(c<r)) | ((a>p)&(a>r)&(c>p)&(c>r))) return null; //Segments are separate
				else {
					itsct[1] = b;
					medArr[0] = a;
					medArr[1] = c;
					medArr[2] = p;
					medArr[3] = r;
					Arrays.sort(medArr);
					itsct[0] = 0.5f*(medArr[1]+medArr[2]);
				}
			} else return null; // Segments are separated
		} else {
			ga = (d-b)/(c-a);
			ia = b-a*ga;
			gb = (q-s)/(p-r);
			ib = q-p*gb;
			if (ga==gb) { // Segments are parallel
				if (ia==ib) { // Segments fall on an identical line
					if (((a<p)&(a<r)&(c<p)&(c<r)) | ((a>p)&(a>r)&(c>p)&(c>r))) return null; //Segments are separate
					else {
						medArr[0] = a;
						medArr[1] = c;
						medArr[2] = p;
						medArr[3] = r;
						Arrays.sort(medArr);
						itsct[0] = 0.5f*(medArr[1]+medArr[2]);
						itsct[1] = 0.5f*ga*(medArr[1]+medArr[2]) + ia;
					}
				} else return null;
			} else { // Calculate the intersection
				tst = (ib-ia)/(ga-gb); // Yes, this is correct -- the x-value of the intersection
				if ( (((a<tst)&(tst<c))|((a>tst)&(tst>c))) & (((p<tst)&(tst<r))|((p>tst)&(tst>r))) & (!nbr) & (Intersects(a,b,c,d,p,q,r,s))) {
					itsct[0] = tst;
					itsct[1] = ga*tst+ia;
				} else return null;
			}
		}
		return itsct;
	}

	public static boolean CheckConvexHull(float[] poly) {
		int lenpoly = poly.length/2;
		int i1, i2;
		Vector2 edge, r;
		for (int i=0; i<lenpoly; i++) {
			i1 = i;
			if (i==lenpoly-1) i2=0;
			else i2 = i+1;
			edge = new Vector2(poly[2*i2]-poly[2*i1],poly[2*i2+1]-poly[2*i1+1]);
			for (int j=0; j<lenpoly; j++) {
				if ((j==i1)|(j==i2)){}
				else {
					r = new Vector2(poly[2*j]-poly[2*i1],poly[2*j+1]-poly[2*i1+1]);
					if (edge.x*r.y - edge.y*r.x < 0.0f) return true;
				}
			}
		}
		return false;
	}

	public static float[] CheckIntersections(ArrayList<float[]> polys, ArrayList<Integer> ptypes,
											 ArrayList<float[]> decors, ArrayList<Integer> dtypes) {
		float[] itsct = new float[2];
		float a, b, c, d, p, q, r, s;
		int plen;
		boolean nbr;
		// Check for intersecting lines with the polygons
		for (int i=0; i<polys.size(); i++) {
			if (ptypes.get(i)%2==0) {
				plen = polys.get(i).length;
				for (int j=0; j<plen/2; j++) {
					a = polys.get(i)[2*j];
					b = polys.get(i)[2*j+1];
					if (j==plen/2 - 1) {
						c = polys.get(i)[0];
						d = polys.get(i)[1];
					} else {
						c = polys.get(i)[2*j+2];
						d = polys.get(i)[2*j+3];
					}
					for (int k=0; k<polys.get(i).length/2; k++) {
						if (j!=k) {
							p = polys.get(i)[2*k];
							q = polys.get(i)[2*k+1];
							if (k==plen/2 - 1) {
								r = polys.get(i)[0];
								s = polys.get(i)[1];
							} else {
								r = polys.get(i)[2*k+2];
								s = polys.get(i)[2*k+3];
							}
							if ((j-k==1)|(k-j==1)|(j-k==plen-1)|(k-j==plen-1)) nbr = true;
							else nbr = false;
							itsct = CalculateIntersection(a,b,c,d,p,q,r,s,nbr);
							if (itsct != null) return itsct;
						}
					}
				}
			}// else it's a circle
		}
		// Check for intersecting lines with the grass
		for (int i=0; i<decors.size(); i++) {
			if (dtypes.get(i)==DecorVars.Grass) {
				plen = decors.get(i).length;
				for (int j=0; j<plen/2; j++) {
					a = decors.get(i)[2*j];
					b = decors.get(i)[2*j+1];
					if (j==plen/2 - 1) {
						c = decors.get(i)[0];
						d = decors.get(i)[1];
					} else {
						c = decors.get(i)[2*j+2];
						d = decors.get(i)[2*j+3];
					}
					for (int k=0; k<decors.get(i).length/2; k++) {
						if (j!=k) {
							p = decors.get(i)[2*k];
							q = decors.get(i)[2*k+1];
							if (k==plen/2 - 1) {
								r = decors.get(i)[0];
								s = decors.get(i)[1];
							} else {
								r = decors.get(i)[2*k+2];
								s = decors.get(i)[2*k+3];
							}
							if ((j-k==1)|(k-j==1)|(j-k==plen-1)|(k-j==plen-1)) nbr = true;
							else nbr = false;
							itsct = CalculateIntersection(a,b,c,d,p,q,r,s,nbr);
							if (itsct != null) return itsct;
						}
					}
				}
			}// else it's another decoration
		}
		return null;
	}

	public static boolean CheckUnique(float[] poly) {
		int lenpoly = poly.length/2;
		float failval = 0.0025f; // This is a fixed value in the box2d code, no length^2 can be less than this value.
		int i1, i2;
		float tst = 0.0f;
		for (int i=0; i<lenpoly; i++) {
			i1 = i;
			if (i==lenpoly-1) i2=0;
			else i2 = i+1;
			tst = B2DVars.EPPM*B2DVars.EPPM*(new Vector2(poly[2*i2]-poly[2*i1],poly[2*i2+1]-poly[2*i1+1])).len2();
			if (tst < failval) return true;
		}
		return false;
	}

	public static boolean CheckAreas(float[] poly) {
		float failval = 1.0E-7f; // This is a fixed value in the box2d code, no length^2 can be less than this value.
		int lenpoly = poly.length/2;
		int i1, i2;
		float tst = 0.0f;
		float e1x, e1y, e2x, e2y;
		float xcen=0.0f, ycen=0.0f;
		// Compute the area
		for (int i=0; i<lenpoly; i++) {
			i1 = i;
			if (i==lenpoly-1) i2=0;
			else i2 = i+1;
			e1x = poly[2*i1];
			e1y = poly[2*i1+1];
			e2x = poly[2*i2];
			e2y = poly[2*i2+1];
			tst += e1x * e2y - e1y * e2x;
			xcen += poly[2*i1];;
			ycen += poly[2*i1+1];
		}
		xcen /= lenpoly;
		ycen /= lenpoly;
		float dist = (float) Math.sqrt(xcen*xcen + ycen*ycen);
		tst *= 0.5f * (B2DVars.EPPM * B2DVars.EPPM);// * (PolySpatial.PIXELS_PER_METER*PolySpatial.PIXELS_PER_METER);
//		System.out.println(tst/dist);
		if (tst/dist < failval) return true;
		return false;
	}

//	public static boolean CheckAreas(float[] poly) {
//		int lenpoly = poly.length/2;
//		float failval = 0.0038f; // This limit was found empirically.
//		int i1, i2;
//		float tst = 0.0f;
//		for (int i=0; i<lenpoly; i++) {
//			i1 = i;
//			if (i==lenpoly-1) i2=0;
//			else i2 = i+1;
//			tst += poly[2*i2+1]*poly[2*i1] - poly[2*i2]*poly[2*i1+1];
//		}
//		tst *= 0.5f * (B2DVars.EPPM * B2DVars.EPPM);// * (PolySpatial.PIXELS_PER_METER*PolySpatial.PIXELS_PER_METER/2pi);
//		System.out.println(tst);
//		if (tst < failval) return true;
//		return false;
//	}

	public static float[] CheckVertexSizes(ArrayList<float[]> polys, ArrayList<Integer> ptypes,
			 ArrayList<float[]> decors, ArrayList<Integer> dtypes) {
		float[] itsct = new float[2];
		itsct[0] = 0.0f;
		itsct[1] = 0.0f;
		for (int k = 0; k<polys.size(); k++) {
			if (ptypes.get(k)%2 == 0) {
				if (CheckUnique(polys.get(k).clone())) { // A problem with the length^2 of a polygon
					for (int j=0; j<polys.get(k).length/2; j++) {
						itsct[0] += polys.get(k)[2*j];
						itsct[1] += polys.get(k)[2*j+1];
					}
					itsct[0] /= polys.get(k).length/2;
					itsct[1] /= polys.get(k).length/2;
					return itsct;
				}
			}
		}
		for (int k = 0; k<decors.size(); k++) {
			if (dtypes.get(k) == DecorVars.Grass) {
				if (CheckUnique(decors.get(k).clone())) { // A problem with the length^2 of a polygon
					for (int j=0; j<decors.get(k).length/2; j++) {
						itsct[0] += decors.get(k)[2*j];
						itsct[1] += decors.get(k)[2*j+1];
					}
					itsct[0] /= decors.get(k).length/2;
					itsct[1] /= decors.get(k).length/2;
					return itsct;
				}
			}
		}
		return null;
	}

	public static float[] CheckVertexSizesDecompose(ArrayList<float[]> polys, ArrayList<Integer> ptypes,
			 ArrayList<float[]> decors, ArrayList<Integer> dtypes) {
		ArrayList<float[]> convexPolygons;
		ArrayList<ArrayList<Vector2>> convexVectorPolygons;
		ArrayList<Vector2> concaveVertices;
		float[] itsct = new float[2];
		itsct[0] = 0.0f;
		itsct[1] = 0.0f;
		for (int i = 0; i<polys.size(); i++) {
			if (ptypes.get(i)%2 == 0) {
				concaveVertices = PolygonOperations.MakeVertices(polys.get(i));
				convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
				convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
				for (int k = 0; k<convexPolygons.size(); k++){
					if (CheckUnique(convexPolygons.get(k).clone())) { // A problem with the length^2 of a polygon
						for (int j=0; j<convexPolygons.get(k).length/2; j++) {
							itsct[0] += convexPolygons.get(k)[2*j];
							itsct[1] += convexPolygons.get(k)[2*j+1];
						}
						itsct[0] /= convexPolygons.get(k).length/2;
						itsct[1] /= convexPolygons.get(k).length/2;
						return itsct;
					}
				}
			}
		}
		for (int i = 0; i<decors.size(); i++) {
			if (dtypes.get(i) == DecorVars.Grass) {
				concaveVertices = PolygonOperations.MakeVertices(decors.get(i));
				convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
				convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
				for (int k = 0; k<convexPolygons.size(); k++){
					if (CheckUnique(convexPolygons.get(k).clone())) { // A problem with the length^2 of a polygon
						for (int j=0; j<convexPolygons.get(k).length/2; j++) {
							itsct[0] += convexPolygons.get(k)[2*j];
							itsct[1] += convexPolygons.get(k)[2*j+1];
						}
						itsct[0] /= convexPolygons.get(k).length/2;
						itsct[1] /= convexPolygons.get(k).length/2;
						return itsct;
					}
				}
			}
		}
		return null;
	}

	public static boolean Intersects(float a, float b, float c, float d, float p, float q, float r, float s) {
		float det, gamma, lambda;
		det = (c - a) * (s - q) - (r - p) * (d - b);
		if (det == 0.0f) {
			return false;
		} else {
			lambda = ((s - q) * (r - a) + (p - r) * (s - b)) / det;
			gamma = ((b - d) * (r - a) + (c - a) * (s - b)) / det;
			return (0.000001 < lambda && lambda < 0.999999) && (0.000001 < gamma && gamma < 0.999999);
		}
	}

	public static ArrayList<float[]> MakeConvexPolygon(ArrayList<ArrayList<Vector2>> aaVector) {
    	ArrayList<float[]> cvxPolygons = new ArrayList<float[]>();
    	float[] newCvxPoly;
    	for (int i = 0; i<aaVector.size(); i++){
    		newCvxPoly = new float[2*aaVector.get(i).size()];
    		for (int j = 0; j<aaVector.get(i).size(); j++){
    			newCvxPoly[2*j] = aaVector.get(i).get(j).x;
    			newCvxPoly[2*j+1] = aaVector.get(i).get(j).y;
    		}
    		cvxPolygons.add(newCvxPoly.clone());
    	}
    	return cvxPolygons;
	}

    public static ArrayList<Vector2> MakeVertices(float[] fs) {
    	ArrayList<Vector2> vertices = new ArrayList<Vector2>();
    	for (int i = 0; i<fs.length/2; i++){
        	vertices.add(new Vector2(fs[2*i],fs[2*i+1]));
    	}
		return vertices;
	}

    public static float OpenDoorDirection(float xcen, float ycen, float xpnt, float ypnt, float angle) {
    	float dirc = 1.0f;
    	if (angle == 0.0f) {
    		if (xpnt > xcen) dirc = -1.0f;
    		else dirc = 1.0f;
    	} else if (angle == Math.PI) {
    		if (xpnt > xcen) dirc = 1.0f;
    		else dirc = -1.0f;    		
    	} else if ((angle >= 0.0f) & (angle <= Math.PI)) {
    		if ((ypnt-ycen) > Math.tan(angle+Math.PI/2)*(xpnt-xcen)) dirc = -1.0f;
    		else dirc = 1.0f;
    	} else if ((angle > Math.PI) & (angle < 2*Math.PI)) {
    		if ((ypnt-ycen) > Math.tan(angle+Math.PI/2)*(xpnt-xcen)) dirc = 1.0f;
    		else dirc = -1.0f;
    	}
    	return dirc;
    }

    public static void RotateArray(float[] xarray, float[] yarray, float angle, float cx, float cy) {
    	float xtmp, ytmp;
    	angle *= MathUtils.degreesToRadians;
    	for (int i = 0; i<xarray.length; i++){
    		xtmp = xarray[i];
    		ytmp = yarray[i];
    		xarray[i] = cx + (xtmp-cx)*(float) Math.cos(angle) - (ytmp-cy)*(float) Math.sin(angle);
    		yarray[i] = cy + (xtmp-cx)*(float) Math.sin(angle) + (ytmp-cy)*(float) Math.cos(angle);
    	}
    	return;
    }

    public static void RotateXYArray(float[] xyarray, float angle, float cx, float cy) {
    	// Note: angle is assumed to be in radians
    	float xtmp, ytmp;
    	for (int i = 0; i<xyarray.length/2; i++){
    		xtmp = xyarray[2*i];
    		ytmp = xyarray[2*i+1];
    		xyarray[2*i] = cx + (xtmp-cx)*(float) Math.cos(angle) - (ytmp-cy)*(float) Math.sin(angle);
    		xyarray[2*i+1] = cy + (xtmp-cx)*(float) Math.sin(angle) + (ytmp-cy)*(float) Math.cos(angle);
    	}
    	return;
    }

    public static float[] RotateCoordinate(float xcoord, float ycoord, float angle, float cx, float cy) {
    	float[] retarr = new float[2];
    	angle *= MathUtils.degreesToRadians;
    	retarr[0]= cx + (xcoord-cx)*(float) Math.cos(angle) - (ycoord-cy)*(float) Math.sin(angle);
    	retarr[1] = cy + (xcoord-cx)*(float) Math.sin(angle) + (ycoord-cy)*(float) Math.cos(angle);
    	return retarr;
    }

    public static Vector2 RotateCoordinateVector(float xcoord, float ycoord, float angle, float cx, float cy) {
    	Vector2 retarr = new Vector2();
    	angle *= MathUtils.degreesToRadians;
    	retarr.x= cx + (xcoord-cx)*(float) Math.cos(angle) - (ycoord-cy)*(float) Math.sin(angle);
    	retarr.y = cy + (xcoord-cx)*(float) Math.sin(angle) + (ycoord-cy)*(float) Math.cos(angle);
    	return retarr;
    }

    public static boolean SwitchOnOff(float xcen, float ycen, float xpnt, float ypnt, float angle, float switchLR) {
    	boolean switchit = false;
    	if (angle == 0.0f) {
    		if (xpnt > xcen) {
    			if (switchLR < 0.0f) switchit = true;
    		} else if (switchLR > 0.0f) switchit = true;
    	} else if (angle == Math.PI) {
    		if (xpnt > xcen) {
    			if (switchLR > 0.0f) switchit = true;
    		} else if (switchLR < 0.0f) switchit = true;    		
    	} else if ((angle >= 0.0f) & (angle <= Math.PI)) {
    		if ((ypnt-ycen) > Math.tan(angle+Math.PI/2)*(xpnt-xcen)) {
    			if (switchLR < 0.0f) switchit = true;
    		} else if (switchLR > 0.0f) switchit = true;
    	} else if ((angle > Math.PI) & (angle < 2*Math.PI)) {
    		if ((ypnt-ycen) > Math.tan(angle+Math.PI/2)*(xpnt-xcen)) {
    			if (switchLR > 0.0f) switchit = true;
    		} else if (switchLR < 0.0f) switchit = true;
    	}
    	return switchit;
    }

    public static float GetAngle(float xcen, float ycen, float xpnt, float ypnt) {
    	float angle;
        if (ypnt > ycen) {
     	   if (xpnt > xcen) angle = (float) Math.atan( (ypnt-ycen)/(xpnt-xcen) );
     	   else angle = (float) (Math.PI - Math.atan( (ypnt-ycen)/(xcen-xpnt) ) );
        } else if (ypnt == ycen) {
     	   if (xpnt > xcen) angle = 0.0f;
     	   else angle = (float) Math.PI;
        } else if (xpnt == xcen) {
     	   if (ypnt > ycen) angle = (float) Math.PI/2.0f;
     	   else angle = 1.5f * (float) Math.PI;
        } else {
     	   if (xpnt > xcen) angle = (float) (2.0*Math.PI - Math.atan( (ycen-ypnt)/(xpnt-xcen) ) );
     	   else angle = (float) (Math.PI + Math.atan( (ycen-ypnt)/(xcen-xpnt) ) );
        }
    	return angle;
    }

    public static float GetLength(float xcen, float ycen, float xpnt, float ypnt) {
		return (float) Math.sqrt( (xcen-xpnt)*(xcen-xpnt) + (ycen-ypnt)*(ycen-ypnt) );
	}

	public static boolean PointInPolygon(float[] poly, float px, float py) {
		int szp;
		boolean inside = false;
		szp = poly.length/2;

		if (szp < 3) return false;

		float x2 = poly[2*(szp-1)];
		float y2 = poly[2*(szp-1)+1];
		float x1, y1;
		for (int i = 0; i < szp; x2 = x1, y2 = y1, ++i) {
			x1 = poly[2*i];
			y1 = poly[2*i+1];
			if (((y1 < py) && (y2 >= py))
					|| (y1 >= py) && (y2 < py)) {
				if ((py - y1) / (y2 - y1)
						* (x2 - x1) < (px - x1))
					inside = !inside;
		         }
		      }
		return inside;
	}

	public static float[] MeanXY(float[] poly) {
		float[] meanvals = new float[2];
		meanvals[0] = 0.0f;
		meanvals[1] = 0.0f;
		int szp = poly.length/2;
		for (int i = 0; i < szp; i++) {
			meanvals[0] += poly[2*i];
			meanvals[1] += poly[2*i+1];
		}
		meanvals[0] /= szp;
		meanvals[1] /= szp;		
		return meanvals;
	}

    public static float SideLength(float xa, float ya, float xb, float yb) {
    	float retv = (float) Math.sqrt((xa-xb)*(xa-xb) + (ya-yb)*(ya-yb));
    	return retv;
    }
}
