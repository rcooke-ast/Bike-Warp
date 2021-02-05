package com.mygdx.game.utilities;

public class FileUtils {
	private static final int NOT_FOUND = -1;
	private static final char UNIX_SEPARATOR = '/';
	private static final char WINDOWS_SEPARATOR = '\\';
	public static final char EXTENSION_SEPARATOR = '.';

	public static int indexOfLastSeparator(final String filename) {
		if (filename == null) {
			return NOT_FOUND;
		}
		final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	public static int indexOfExtension(final String filename) {
		if (filename == null) {
			return NOT_FOUND;
		}
		final int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
		final int lastSeparator = indexOfLastSeparator(filename);
		return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
	}

	public static String getName(final String filename) {
	    if (filename == null) {
	    	return null;
	    }
	    failIfNullBytePresent(filename);

	    final int index = indexOfLastSeparator(filename);
	    return filename.substring(index + 1);
	}

	public static String getBaseName(String filename) {
		return removeExtension(getName(filename));
    }

	public static String removeExtension(final String filename) {
		if (filename == null) {
			return null;
		}
		failIfNullBytePresent(filename);
	
		final int index = indexOfExtension(filename);
		if (index == NOT_FOUND) {
			return filename;
		} else {
	        return filename.substring(0, index);
	    }
	}

	private static void failIfNullBytePresent(String path) {
		int len = path.length();
	    for (int i = 0; i < len; i++) {
	    	if (path.charAt(i) == 0) {
	    		throw new IllegalArgumentException("Null byte present in file/path name. There are no " +
		                        "known legitimate use cases for such data, but several injection attacks may use it");
		    }
		}
	}

	public static int[][] deepCopyIntArray(int[][] input) {
		if (input == null)
			return null;
		int[][] result = new int[input.length][];
		for (int r = 0; r < input.length; r++) {
			result[r] = input[r].clone();
		}
		return result;
	}

	public static float[][][] deepCopyFloatArray(float[][][] input) {
		if (input == null)
			return null;
		float[][][] result = new float[input.length][input[0].length][];
		for (int r = 0; r < input.length; r++) {
			for (int s=0; s<input[0].length; s++) {
				result[r][s] = input[r][s].clone();
			}
		}
		return result;
	}
}