package edu.hm.cs.fs.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Fabio
 * 
 */
public final class NetworkUtils {
	private NetworkUtils() {
	}

	/**
	 * Checks the interent connection.
	 * 
	 * @param context
	 * @return <code>true</code> if the device is connected to the internet.
	 */
	public static boolean isConnected(final Context context) {
		final NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * Checks the interent connection.
	 * 
	 * @param context
	 * @return <code>true</code> if the device is connected to the internet.
	 */
	public static boolean isWifiConnected(final Context context) {
		final NetworkInfo networkInfoWifi = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return networkInfoWifi != null && networkInfoWifi.isConnected();
	}
}
