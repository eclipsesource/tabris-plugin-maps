/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import static com.eclipsesource.tabris.maps.MapCameraChangeListener.EVENT_CAMERACHANGE;
import static com.eclipsesource.tabris.maps.MapClickListener.EVENT_TAP;
import static com.eclipsesource.tabris.maps.MapHolderView.EVENT_READY;
import static com.eclipsesource.tabris.maps.MapLongClickListener.EVENT_LONGPRESS;
import static com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap;

/**
 * This class handles all protocol operation for the Tabris maps custom widget.
 */
public class MapOperator extends AbstractTabrisOperator<MapHolderView> {

  public static final String WIDGET_TYPE = "tabris.Map";
  private static final String LOG_TAG = WIDGET_TYPE;
  private static final int DEFAULT_CAMERA_PADDING = 32;

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
    MapHolderView mapHolderView = new MapHolderView( activity, tabrisContext );
    mapHolderView.createMap();
    return mapHolderView;
  }

  @Override
  public void listen( final MapHolderView mapHolderView, String event, boolean listen ) {
    switch( event ) {
      case EVENT_READY:
        if( listen ) {
          mapHolderView.setOnMapReadyListener();
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
      case "animateCameraToPointGroup":
        animateCameraToPointGroup( mapHolderView, properties );
        break;
      default:
        Log.d( LOG_TAG, String.format( "Call to unknown method \"%s\". Properties: %s", method, properties ) );
        break;
    }
    return null;
  }

  private void animateCameraToPointGroup( MapHolderView mapHolderView, Properties properties ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    List<Double> latLngPointGroup = properties.getList( "latLngPointGroup", Double.class );
    for( int i = 0; i < latLngPointGroup.size(); i += 2 ) {
      builder.include( new LatLng( latLngPointGroup.get( i ), latLngPointGroup.get( i + 1 ) ) );
    }
    googleMap.animateCamera( CameraUpdateFactory.newLatLngBounds( builder.build(), getCameraPadding() ) );
  }

  private int getCameraPadding() {
    DisplayMetrics metrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics( metrics );
    return Math.round( metrics.density * DEFAULT_CAMERA_PADDING );
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
