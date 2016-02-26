/**
 * Copyright (c) 2016 by Stefan Gross All Rights Reserved.
 * Licensed under the terms of the MIT License
 * 
 * Please see the LICENSE included with this distribution for details.
 * 
 * @author Stefan Gross https://github.com/stgrosshh
 *
 */
package ti.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiBaseActivity;
import org.appcelerator.titanium.TiC;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.ArraySet;

@Kroll.module(name = "Tipermissions", id = "ti.permissions")
public class TipermissionsModule extends KrollModule {

	// Standard Debugging variables
	private static final String LCAT = "TipermissionsModule";
	private static final boolean DBG = TiConfig.LOGD;

	// uncritical permission as of Android 6 Marshmallow

	static final List<String> simplePermissions = Arrays.asList(
			Manifest.permission.BLUETOOTH,
			Manifest.permission.BLUETOOTH_ADMIN,
			Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.ACCESS_NOTIFICATION_POLICY,
			Manifest.permission.ACCESS_WIFI_STATE,
			Manifest.permission.BROADCAST_STICKY,
			Manifest.permission.CHANGE_NETWORK_STATE,
			Manifest.permission.CHANGE_WIFI_STATE,
			Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
			Manifest.permission.DISABLE_KEYGUARD,
			Manifest.permission.EXPAND_STATUS_BAR,
			Manifest.permission.FLASHLIGHT,
			Manifest.permission.GET_ACCOUNTS,
			Manifest.permission.GET_PACKAGE_SIZE,
			Manifest.permission.INTERNET,
			Manifest.permission.KILL_BACKGROUND_PROCESSES,
			Manifest.permission.MODIFY_AUDIO_SETTINGS,
			Manifest.permission.NFC,
			Manifest.permission.READ_SYNC_SETTINGS,
			Manifest.permission.READ_SYNC_STATS,
			Manifest.permission.RECEIVE_BOOT_COMPLETED,
			Manifest.permission.REORDER_TASKS,
			Manifest.permission.REQUEST_INSTALL_PACKAGES,
			Manifest.permission.SET_TIME_ZONE,
			Manifest.permission.SET_WALLPAPER,
			Manifest.permission.SET_WALLPAPER_HINTS,
			Manifest.permission.TRANSMIT_IR,
			Manifest.permission.USE_FINGERPRINT,
			Manifest.permission.VIBRATE, Manifest.permission.WAKE_LOCK,
			Manifest.permission.WRITE_SYNC_SETTINGS,
			Manifest.permission.SET_ALARM,
			Manifest.permission.INSTALL_SHORTCUT,
			Manifest.permission.UNINSTALL_SHORTCUT);

	// critical permissions as of Android 6 Marshmallow
	static final List<String> permissions = Arrays.asList(
			Manifest.permission.READ_CALENDAR,
			Manifest.permission.WRITE_CALENDAR,

			Manifest.permission.READ_CONTACTS,
			Manifest.permission.WRITE_CONTACTS,
			Manifest.permission.GET_ACCOUNTS,

			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION,

			Manifest.permission.RECORD_AUDIO,

			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.CALL_PHONE,
			Manifest.permission.READ_CALL_LOG,
			Manifest.permission.WRITE_CALL_LOG,
			Manifest.permission.ADD_VOICEMAIL,
			Manifest.permission.USE_SIP,
			Manifest.permission.PROCESS_OUTGOING_CALLS,

			Manifest.permission.BODY_SENSORS,

			Manifest.permission.SEND_SMS,
			Manifest.permission.RECEIVE_SMS,
			Manifest.permission.READ_SMS,
			Manifest.permission.RECEIVE_WAP_PUSH,
			Manifest.permission.RECEIVE_MMS,
			//Manifest.permission.READ_CELL_BROADCASTS, // missing in level 23?

			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,

			Manifest.permission.BLUETOOTH_PRIVILEGED,
			Manifest.permission.CAMERA
			);


	/*
	 * Deprecated permissions?
	 * 
	 * android.permission.ACCESS_WIMAX_STATE - unknown
	 * android.permission.CHANGE_WIMAX_STATE - unknown
	 * android.permission.SUBSCRIBED_FEEDS_READ - unknown
	 */

	// requestCode key in callback result 
	@Kroll.constant 
	public static final String KEY_REQUEST_CODE = "requestCode";

	public TipermissionsModule() {
		super();
		Log.d(LCAT, "Ti Permissions Module ctor");
	}

	// TODO shall we implement convenience methods? -> would be a long list though?!

	/**
	 * check, if given permission is currently granted
	 * 
	 * @param requestedPermission - permission as defined in Manifest
	 * @return
	 */
	@Kroll.method
	public boolean hasPermission(@Kroll.argument() String requestedPermission) {
		Log.d(LCAT, "check for granted permission: " + requestedPermission);

		// TODO really depends on Build or Platform???
		if (Build.VERSION.SDK_INT < 23) {
			return true;
		}

		Activity currentActivity = TiApplication.getAppCurrentActivity();
		if (ContextCompat.checkSelfPermission(currentActivity,
				requestedPermission) != PackageManager.PERMISSION_GRANTED) {
			return false;
		}
		return true;
	}


	/**
	 * Request a permission and optionally register a callback for the current activity
	 * 
	 * @param requestedPermission permission as defined in Manifest
	 * @param requestCode - 8 Bit value to associate callback with request
	 * @param permissionCallback function called with result of permission prompt
	 * @return true in case of valid request, false if requested permission is not a valid one
	 */
	@Kroll.method
	public boolean requestPermission(String requestedPermission,
			@Kroll.argument(optional = true) Integer requestCode,
			@Kroll.argument(optional = true) KrollFunction permissionCallback) {

		Log.d(LCAT, "Requesting permission: " + requestedPermission);

		if (!isValidPermissionString(requestedPermission)) {
			Log.e(LCAT, "Requested permission is not supported :"
					+ requestedPermission);
			return false;
		}

		return handleRequest(new String[]{requestedPermission}, requestCode, permissionCallback);

	}

	/**
	 * Request a permission and optionally register a callback for the current activity
	 * 
	 * @param requestedPermissions Array of permissions as defined in Manifest
	 * @param requestCode - 8 Bit value to associate callback with request
	 * @param permissionCallback function called with result of permission prompt
	 * @return true in case of valid request, false if requested permission is not a valid one
	 */
	@Kroll.method
	public boolean requestPermissions(@Kroll.argument String[] requestedPerms,
			@Kroll.argument(optional = true) Integer requestCode,
			@Kroll.argument(optional = true) KrollFunction permissionCallback) {

//		String[] requestedPermissions = new String[]{requestedPerms};//(String[])requestedPerms;
		for(String permission:requestedPerms) {
			Log.d(LCAT, "Requesting permission: " + permission);

			if (!isValidPermissionString(permission)) {
				Log.e(LCAT, "Requested permission is not supported :"
						+ permission);
				return false;
			}
		}

		return handleRequest(requestedPerms, requestCode, permissionCallback);

	}		

	private boolean handleRequest(String[] permissions, Integer requestCode, KrollFunction permissionCallback) {
		Activity activity = TiApplication.getAppCurrentActivity();

		if (!(activity instanceof TiBaseActivity)) {
			Log.w(LCAT,	"Requesting permission from non-Titanium activity - not supported");
			return false;
		}

		TiBaseActivity currentActivity = (TiBaseActivity) activity;
		// Do we need a callback and request code in any case?
		if (requestCode == null) {
			Log.w(LCAT, "No request code given - this is not supported by Ti Permissions module");
			return false;
		}

		// register callback in current activity
		// TODO what is the exact purpose of the context? We should provide the Activity's Proxy, not the module object
		KrollObject context = currentActivity.getActivityProxy().getKrollObject();

		Log.d(LCAT, "Registering callback");
		currentActivity.registerPermissionRequestCallback(requestCode, 
				permissionCallback, context,permissions);

		Log.d(LCAT, "Calling permission request");
		ActivityCompat.requestPermissions(activity,	permissions, requestCode);
		return true;
	}

	private boolean isUncriticalPermissionString(String requestedPermission) {
		return simplePermissions.contains(requestedPermission);
	}

	private boolean isCriticalPermissionString(String requestedPermission) {
		return permissions.contains(requestedPermission);
	}

	private boolean isValidPermissionString(String requestedPermission) {
		return isCriticalPermissionString(requestedPermission) || isUncriticalPermissionString(requestedPermission);
	}

}
