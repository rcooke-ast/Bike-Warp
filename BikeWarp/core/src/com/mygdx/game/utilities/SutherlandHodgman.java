package com.mygdx.game.utilities;

import java.util.*;
 
public class SutherlandHodgman {
 
    @SuppressWarnings("unchecked")
	public SutherlandHodgman(ArrayList<float[]> subject, ArrayList<float[]> clipper) {
    	ArrayList<float[]> result  = (ArrayList<float[]>) subject.clone();
        int len = clipper.size();

        for (int i = 0; i < len; i++) {
 
            int len2 = result.size();
            ArrayList<float[]> input = result;
            result = new ArrayList<float[]>(len2);
 
            float[] A = clipper.get((i + len - 1) % len);
            float[] B = clipper.get(i);
 
            for (int j = 0; j < len2; j++) {
 
                float[] P = input.get((j + len2 - 1) % len2);
                float[] Q = input.get(j);
 
                if (isInside(A, B, Q)) {
                    if (!isInside(A, B, P))
                        result.add(intersection(A, B, P, Q));
                    result.add(Q);
                } else if (isInside(A, B, P))
                    result.add(intersection(A, B, P, Q));
            }
        }
    }
 
    private boolean isInside(float[] a, float[] b, float[] c) {
        return (a[0] - c[0]) * (b[1] - c[1]) > (a[1] - c[1]) * (b[0] - c[0]);
    }
 
    private float[] intersection(float[] a, float[] b, float[] p, float[] q) {
        float A1 = b[1] - a[1];
        float B1 = a[0] - b[0];
        float C1 = A1 * a[0] + B1 * a[1];
 
        float A2 = q[1] - p[1];
        float B2 = p[0] - q[0];
        float C2 = A2 * p[0] + B2 * p[1];
 
        float det = A1 * B2 - A2 * B1;
        float x = (B2 * C1 - B1 * C2) / det;
        float y = (A1 * C2 - A2 * C1) / det;
 
        return new float[]{x, y};
    }
}