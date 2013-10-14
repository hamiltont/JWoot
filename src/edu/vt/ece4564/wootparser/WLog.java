package edu.vt.ece4564.wootparser;

import android.util.Log;

public class WLog {
	/**
	 * Enable or disable the log. Disabling typically cuts runtime by 50%
	 */
	public static final boolean DEBUG = true;
	public static final String TAG = "Woot Parser";

	public static void i(Object... logs) {
		println(TAG, Log.INFO, logs);
	}

	public static void e(Object... logs) {
		println(TAG, Log.ERROR, logs);
	}

	public static void d(Object... logs) {
		println(TAG, Log.DEBUG, logs);
	}

	public static void v(Object... logs) {
		println(TAG, Log.VERBOSE, logs);
	}

	public static void w(Object... logs) {
		println(TAG, Log.WARN, logs);
	}

	private static void println(String tag, int priority, Object... logs) {
		if (!DEBUG)
			return;

		StringBuilder builder = new StringBuilder();
		for (Object log : logs)
			builder.append(log.toString()).append(' ');

		Log.println(priority, tag, builder.toString());
	}
}
