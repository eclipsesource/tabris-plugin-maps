/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.operator.AbstractWidgetOperator;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.eclipsesource.tabris.client.core.operation.CallOperation;
import com.eclipsesource.tabris.client.core.operation.CreateOperation;
import com.eclipsesource.tabris.client.core.operation.DestroyOperation;
import com.eclipsesource.tabris.client.core.operation.ListenOperation;
import com.eclipsesource.tabris.client.core.operation.SetOperation;
import com.google.android.gms.maps.GoogleMap;

import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateCallOperation;
import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateCreateOperation;
import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateListenOperation;
import static com.eclipsesource.tabris.maps.MapClickListener.EVENT_TAP;
import static com.eclipsesource.tabris.maps.MapHolderView.EVENT_READY;
import static com.eclipsesource.tabris.maps.MapLongClickListener.EVENT_LONGPRESS;
import static com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap;

/**
 * This class handles all protocol operation for the Tabris maps custom widget.
 */
public class MapOperator extends AbstractWidgetOperator {

  private static final String LOG_TAG = "tabris.maps";
  public static final String WIDGET_TYPE = "ESMap";

  private final MapPropertyHandler mapPropertyHandler;

  public MapOperator( TabrisActivity activity ) {
    super( activity );
    mapPropertyHandler = new MapPropertyHandler( activity );
  }

  @Override
  protected IPropertyHandler getPropertyHandler( Object object ) {
    return mapPropertyHandler;
  }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR1 )
  @Override
  public void create( CreateOperation createOperation ) {
    Log.d( LOG_TAG, String.format( "Creating new map. Target: %s, Type: %s",
        createOperation.getTarget(), createOperation.getType() ) );
    validateCreateOperation( createOperation );
    MapHolderView mapHolderView = new MapHolderView( getActivity() );
    initiateNewView( createOperation, mapHolderView );
    mapHolderView.createMap();
  }

  @Override
  public void set( SetOperation setOperation ) {
    super.set( setOperation );
  }

  @Override
  public void listen( ListenOperation listenOperation ) {
    super.listen( listenOperation );
    validateListenOperation( listenOperation );
    Properties properties = listenOperation.getProperties();
    if( properties.hasProperty( EVENT_READY ) ) {
      if( properties.getBoolean( EVENT_READY ) ) {
        attachOnMapReadyListener( listenOperation );
      } else {
        throw new IllegalStateException( "'mapReady' event listeners cannot be removed." );
      }
    }
    if( properties.hasProperty( EVENT_TAP ) ) {
      if( properties.getBoolean( EVENT_TAP ) ) {
        attachOnMapClickListener( listenOperation );
      } else {
        removeOnMapClickListener( listenOperation );
      }
    }
    if( properties.hasProperty( EVENT_LONGPRESS ) ) {
      if( properties.getBoolean( EVENT_LONGPRESS ) ) {
        attachOnMapLongClickListener( listenOperation );
      } else {
        removeOnMapLongClickListener( listenOperation );
      }
    }
  }

  private void attachOnMapReadyListener( ListenOperation listenOperation ) {
    MapHolderView mapHolderView = ( MapHolderView )findViewByTarget( listenOperation );
    mapHolderView.setOnMapReadyListener();
  }

  private void attachOnMapClickListener( ListenOperation listenOperation ) {
    MapHolderView mapHolderView = ( MapHolderView )findViewByTarget( listenOperation );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapClickListener( new MapClickListener( getActivity(), mapHolderView ) );
  }

  private void removeOnMapClickListener( ListenOperation listenOperation ) {
    MapHolderView mapHolderView = ( MapHolderView )findViewByTarget( listenOperation );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapClickListener( null );
  }

  private void attachOnMapLongClickListener( ListenOperation listenOperation ) {
    MapHolderView mapHolderView = ( MapHolderView )findViewByTarget( listenOperation );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapLongClickListener( new MapLongClickListener( getActivity(), mapHolderView ) );
  }

  private void removeOnMapLongClickListener( ListenOperation listenOperation ) {
    MapHolderView mapHolderView = ( MapHolderView )findViewByTarget( listenOperation );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMapLongClickListener( null );
  }

  private GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    validateGoogleMap( googleMap, "Listeners must be attached in the 'mapReady' event callback." );
    return googleMap;
  }

  @Override
  public Object call( CallOperation callOperation ) {
    validateCallOperation( callOperation );
    String method = callOperation.getMethod();
    switch( method ) {
      default:
        Log.d( LOG_TAG, String.format( "call to unknown method \"%s\"", method ) );
        break;
    }
    return null;
  }

  @Override
  public Object get( String target, String property ) {
    return super.get( target, property );
  }

  @Override
  public void destroy( DestroyOperation destroyOperation ) {
    super.destroy( destroyOperation );
  }

}