/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.GoogleMap;

import static com.eclipsesource.tabris.maps.MapClickListener.EVENT_TAP;
import static com.eclipsesource.tabris.maps.MapHolderView.EVENT_READY;
import static com.eclipsesource.tabris.maps.MapLongClickListener.EVENT_LONGPRESS;
import static com.eclipsesource.tabris.maps.MapCameraChangeListener.EVENT_CAMERACHANGE;
import static com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap;

/**
 * This class handles all protocol operation for the Tabris maps custom widget.
 */
public class MapOperator extends AbstractTabrisOperator<MapHolderView> {

  public static final String WIDGET_TYPE = "tabris.Map";
  private static final String LOG_TAG = WIDGET_TYPE;

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private final TabrisPropertyHandler<MapHolderView> mapPropertyHandler;

  public MapOperator( Activity activity, TabrisContext tabrisContext ) {
    this.activity = activity;
    this.tabrisContext = tabrisContext;
    mapPropertyHandler = new MapPropertyHandler( activity, tabrisContext );
  }

  @Override
  public TabrisPropertyHandler<MapHolderView> getPropertyHandler() {
    return mapPropertyHandler;
  }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR1 )
  @Override
  public MapHolderView create( Properties properties ) {
    Log.d( LOG_TAG, String.format( "Creating new map holder. %s", properties ) );
    final MapHolderView mapHolderView = new MapHolderView( activity, tabrisContext );
    mapHolderView.post( new Runnable() {
      public void run() {
        mapHolderView.createMap();
      }
    } );
    return mapHolderView;
  }

  @Override
  public void listen( final MapHolderView mapHolderView, String event, boolean listen ) {
    switch( event ) {
      case EVENT_READY:
        if( listen ) {
          mapHolderView.post( new Runnable() {
            public void run() {
              mapHolderView.setOnMapReadyListener();
            }
          } );
        } else {
          throw new IllegalStateException( "'mapReady' event listeners cannot be removed." );
        }
        break;
      case EVENT_TAP:
        if( listen ) {
          attachOnMapClickListener( mapHolderView );
        } else {
          removeOnMapClickListener( mapHolderView );
        }
        break;
      case EVENT_LONGPRESS:
        if( listen ) {
          attachOnMapLongClickListener( mapHolderView );
        } else {
          removeOnMapLongClickListener( mapHolderView );
        }
        break;
      case EVENT_CAMERACHANGE:
        if( listen ) {
          attachOnCameraChangeListener( mapHolderView );
        } else {
          removeOnCameraChangeListener( mapHolderView );
        }
        break;
    }
  }

  @Override
  public Object call( MapHolderView mapHolderView, String method, Properties properties ) {
    switch( method ) {
      default:
        Log.d( LOG_TAG, String.format( "Call to unknown method \"%s\". Properties: %s", method, properties ) );
        break;
    }
    return null;
  }

  @Override
  public void destroy( MapHolderView mapHolderView ) {
    ( ( ViewGroup )mapHolderView.getParent() ).removeView( mapHolderView );
  }

  private void attachOnMapClickListener( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapClickListener( new MapClickListener( tabrisContext.getObjectRegistry(), mapHolderView ) );
  }

  private void removeOnMapClickListener( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapClickListener( null );
  }

  private void attachOnMapLongClickListener( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapLongClickListener( new MapLongClickListener( tabrisContext.getObjectRegistry(), mapHolderView ) );
  }

  private void removeOnMapLongClickListener( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapLongClickListener( null );
  }

  private void attachOnCameraChangeListener( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnCameraChangeListener( new MapCameraChangeListener( tabrisContext.getObjectRegistry(), mapHolderView ) );
  }

  private void removeOnCameraChangeListener( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnCameraChangeListener( null );
  }

  private GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    validateGoogleMap( googleMap, "Listeners must be attached in the 'mapReady' event callback." );
    return googleMap;
  }

}
