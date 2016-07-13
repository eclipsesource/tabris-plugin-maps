/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.eclipsesource.tabris.client.core.OperatorRegistry;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import static com.eclipsesource.tabris.maps.MapCameraChangeListener.*;
import static com.eclipsesource.tabris.maps.MapClickListener.EVENT_TAP;
import static com.eclipsesource.tabris.maps.MapHolderView.EVENT_READY;
import static com.eclipsesource.tabris.maps.MapLongClickListener.EVENT_LONGPRESS;
import static com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap;

/**
 * This class handles all protocol operation for the Tabris maps custom widget.
 */
public class MapOperator extends AbstractTabrisOperator<MapHolderView> {

  public static final String WIDGET_TYPE = "tabris.Map";
  private static final String METHOD_MOVE_CAMERA_TO_REGION = "moveCameraToRegion";
  private static final String PROP_OPTIONS = "options";
  private static final String PROP_ANIMATE = "animate";
  private static final String PROP_PADDING = "padding";
  private static final String PROP_SOUTH_WEST = "southWest";
  private static final String PROP_NORTH_EAST = "northEast";
  private static final String PROP_REGION = "region";

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
      case EVENT_CAMERA_MOVE:
      case EVENT_CAMERA_MOVE_PROGRAMMATIC:
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
      case METHOD_MOVE_CAMERA_TO_REGION:
        moveCameraToRegion( mapHolderView, properties );
        break;
    }
    return null;
  }

  private void moveCameraToRegion( MapHolderView mapHolderView, Properties properties ) {
    LatLngBounds bounds = createBoundsFromRegion( properties );
    int padding = getPaddingFromOptions( properties );
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds( bounds, padding );
    if( getAnimateFromOptions( properties ) ) {
      mapHolderView.animateCamera( cameraUpdate );
    } else {
      mapHolderView.moveCamera( cameraUpdate );
    }
  }

  private boolean getAnimateFromOptions( Properties properties ) {
    Properties optionProperties = properties.getProperties( PROP_OPTIONS );
    if( optionProperties != null ) {
      return optionProperties.getBooleanSafe( PROP_ANIMATE );
    }
    return false;
  }

  private LatLngBounds createBoundsFromRegion( Properties properties ) {
    Properties region = properties.getProperties( PROP_REGION );
    List<Double> southWest = region.getList( PROP_SOUTH_WEST, Double.class );
    List<Double> northEast = region.getList( PROP_NORTH_EAST, Double.class );
    return new LatLngBounds(
        new LatLng( southWest.get( 0 ), southWest.get( 1 ) ),
        new LatLng( northEast.get( 0 ), northEast.get( 1 ) ) );
  }

  private int getPaddingFromOptions( Properties properties ) {
    return getScaledFloat( properties.getProperties( PROP_OPTIONS ), PROP_PADDING );
  }

  private int getScaledFloat( Properties properties, String key ) {
    if( properties != null ) {
      Float padding = properties.getFloat( key );
      if( padding != null ) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics( metrics );
        return Math.round( metrics.density * padding );
      }
    }
    return 0;
  }

  @Override
  public void destroy( MapHolderView mapHolderView ) {
    ( ( ViewGroup )mapHolderView.getParent() ).removeView( mapHolderView );
    String mapId = tabrisContext.getObjectRegistry().getRemoteObjectForObject( mapHolderView ).getId();
    for( ObjectRegistry.RegistryEntry registryEntry : tabrisContext.getObjectRegistry().getEntries() ) {
      Object object = registryEntry.getObject();
      if( object instanceof MapMarker ) {
        MapMarker mapMarker = ( MapMarker )object;
        if( mapMarker.getMapId().equals( mapId ) ) {
          OperatorRegistry operatorRegistry = ( ( TabrisActivity )activity ).getWidgetToolkit().getOperatorRegistry();
          MarkerOperator operator = ( MarkerOperator )operatorRegistry.get( MarkerOperator.TYPE );
          operator.destroy( mapMarker );
        }
      }
    }
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
