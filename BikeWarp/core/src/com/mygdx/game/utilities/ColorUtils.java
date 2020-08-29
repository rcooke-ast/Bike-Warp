package com.mygdx.game.utilities;

public class ColorUtils {

	public static float[] hsbToRgb(float hue, float saturation, float value) {

	    int h = (int)(hue * 6);
	    float f = hue * 6 - h;
	    float p = value * (1 - saturation);
	    float q = value * (1 - f * saturation);
	    float t = value * (1 - (1 - f) * saturation);

	    switch (h) {
	      case 0: return rgbToArray(value, t, p);
	      case 1: return rgbToArray(q, value, p);
	      case 2: return rgbToArray(p, value, t);
	      case 3: return rgbToArray(p, q, value);
	      case 4: return rgbToArray(t, p, value);
	      case 5: return rgbToArray(value, p, q);
	      default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
	    }
	}

	public static float[] rgbToArray(float r, float g, float b) {
	    float[] arr = new float[3];
	    arr[0] = r;
	    arr[1] = g;
	    arr[2] = b;
	    return arr;
	}

	public static String rgbToString(float r, float g, float b) {
	    String rs = Integer.toHexString((int)(r * 256));
	    String gs = Integer.toHexString((int)(g * 256));
	    String bs = Integer.toHexString((int)(b * 256));
	    return rs + gs + bs;
	}

	public static float[] ConvertStringToColor(String colstr) {
		float[] retarr = new float[4];
		if (colstr.startsWith("COLOR_")) {
			String[] arr = colstr.split("_");
			retarr[0] = Float.parseFloat(arr[1]);
			retarr[1] = Float.parseFloat(arr[2]);
			retarr[2] = Float.parseFloat(arr[3]);
			retarr[3] = Float.parseFloat(arr[4]);
		}
		return retarr;
	}


}