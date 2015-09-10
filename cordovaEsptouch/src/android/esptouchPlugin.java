package com.icubespace.cordova_esptouch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.List;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class esptouchPlugin extends CordovaPlugin {
	
	CallbackContext receivingCallbackContext = null;
	IEsptouchTask mEsptouchTask;
	@Override
    public boolean execute(String action, final JSONArray args,final CallbackContext callbackContext) throws JSONException{
        if (action.equals("smartConfig")) {
			receivingCallbackContext = callbackContext;
            callbackContext.success("enter smartConfig");
            final String apSsid = args.getString(0);
			final String apBssid = args.getString(1);
			final String apPassword = args.getString(2);
			final String isSsidHiddenStr = args.getString(3);
			final String taskResultCountStr = args.getString(4);
			
			final Object mLock = new Object();
			cordova.getThreadPool().execute(
				new Runnable() {
					public void run() {
						receivingCallbackContext.success("thread runing");
						synchronized (mLock) {
							PluginResult result = new PluginResult(PluginResult.Status.OK, "thread runing");
                            result.setKeepCallback(true);           // keep callback after this call
                            receivingCallbackContext.sendPluginResult(result);
						boolean isSsidHidden = false;
						if (isSsidHiddenStr.equals("YES")) {
							isSsidHidden = true;
						}
						int taskResultCount = Integer.parseInt(taskResultCountStr);
						synchronized (mLock) {
							mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
							isSsidHidden, cordova.getActivity());
							mEsptouchTask.setEsptouchListener(myListener);
						}
						List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
						}
					}
				}
			);
						
			PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
			result.setKeepCallback(true); // keep callback in order to call it later
			receivingCallbackContext.sendPluginResult(result);
			return true;
		}else{
			callbackContext.error("function name error");
			return false;
		}
		
	}

	
	//after get result
	private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
				String text = result.getBssid() + " is connected to the wifi";
				receivingCallbackContext.success("enter onEsptoucResultAddedPerform"+text);//-----------------
				PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, text);
                pluginResult.setKeepCallback(true);           // keep callback after this call
				receivingCallbackContext.sendPluginResult(pluginResult);
				
	}
	//listener to get result
	private IEsptouchListener myListener = new IEsptouchListener() {

		@Override
		public void onEsptouchResultAdded(final IEsptouchResult result) {
			receivingCallbackContext.success("enter onEsptouchResultAdded");//----------------
			onEsptoucResultAddedPerform(result);
		}
	};
	
	
}