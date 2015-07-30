/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.util.Log;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.client.core.OperatorRegistry;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class is the main entry point of the Tabris maps custom widget.
 */
public class Map extends CordovaPlugin {

  private static final String LOG_TAG = "tabris.maps";

  @Override
  public boolean execute( String action, JSONArray args, CallbackContext callbackContext ) throws JSONException {
    Log.d( LOG_TAG, String.format( "Execute action: %s args: %s", action, args ) );
    return true;
  }

  @Override
  public void initialize( CordovaInterface cordova, CordovaWebView webView ) {
    super.initialize( cordova, webView );
    TabrisActivity activity = ( TabrisActivity )cordova.getActivity();
    OperatorRegistry operatorRegistry = activity.getWidgetToolkit().getOperatorRegistry();
    operatorRegistry.register( MapOperator.WIDGET_TYPE, new MapOperator( activity ) );
    Log.d( LOG_TAG, "initialized" );
  }

}