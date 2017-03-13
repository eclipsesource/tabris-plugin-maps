/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.ObjectRegistry.RegistryEntry;
import com.eclipsesource.tabris.client.core.OperatorRegistry;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.eclipsesource.tabris.maps.MapHolderView.EVENT_READY;
import static com.eclipsesource.tabris.maps.MapLongPressListener.EVENT_LONGPRESS;
import static com.eclipsesource.tabris.maps.MapTapListener.EVENT_TAP;
import static com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap;

public class MapOperator extends AbstractTabrisOperator<MapHolderView> {

  private static final String TYPE = "com.eclipsesource.maps.Map";
  private static final String METHOD_MOVE_TO_REGION = "moveToRegion";
  private static final String METHOD_ADD_MARKER = "addMarker";
  private static final String METHOD_REMOVE_MARKER = "removeMarker";
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
  public TabrisPropertyHandler<MapHolderView> getPropertyHandler( MapHolderView object ) {
    return mapPropertyHandler;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR1 )
  @Override
  public MapHolderView create( String id, Properties properties ) {
    return new MapHolderView( activity, tabrisContext );
  }

  @Override
  public void listen( String id, MapHolderView mapHolderView, String event, boolean listen ) {
    switch( event ) {
      case EVENT_READY:
        if( listen ) {
          mapHolderView.setOnMapReadyListener();
        } else {
          throw new IllegalStateException( "'ready' event listeners cannot be removed." );
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
    }
  }

  @Override
  public Object call( MapHolderView mapHolderView, String method, Properties properties ) {
    switch( method ) {
      case METHOD_MOVE_TO_REGION:
        moveCameraToRegion( mapHolderView, properties );
        break;
      case METHOD_ADD_MARKER:
        addMarker( mapHolderView, properties );
        break;
      case METHOD_REMOVE_MARKER:
        removeMarker( properties );
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

  private void addMarker( MapHolderView mapHolderView, Properties properties ) {
    String markerId = properties.getString( "marker" );
    if( markerId != null ) {
      MapMarker mapMarker = tabrisContext.getObjectRegistry().getObject( markerId, MapMarker.class );
      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position( mapMarker.getPosition() );
      Marker marker = mapHolderView.getGoogleMap().addMarker( markerOptions );
      mapMarker.setMarker( marker );
      mapMarker.setMapId( tabrisContext.getObjectRegistry().getRemoteObjectForObject( mapHolderView ).getId() );
      mapMarker.updateMarker();
    }
  }

  private void removeMarker( Properties properties ) {
    String markerId = properties.getString( "marker" );
    if( markerId != null ) {
      MapMarker mapMarker = tabrisContext.getObjectRegistry().getObject( markerId, MapMarker.class );
      mapMarker.getMarker().remove();
      mapMarker.setMarker( null );
      mapMarker.setMapId( null );
    }
  }

  @Override
  public void destroy( MapHolderView mapHolderView ) {
    disableLocationIndicator( mapHolderView );
    ( ( ViewGroup )mapHolderView.getParent() ).removeView( mapHolderView );
    destroyMarker( mapHolderView );
  }

  private void disableLocationIndicator( MapHolderView mapHolderView ) {
    if( ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION )
        == PackageManager.PERMISSION_GRANTED ) {
      mapHolderView.getGoogleMap().setMyLocationEnabled( false );
    }
  }

  private void destroyMarker( MapHolderView mapHolderView ) {
    String mapId = tabrisContext.getObjectRegistry().getRemoteObjectForObject( mapHolderView ).getId();
    for( RegistryEntry registryEntry : tabrisContext.getObjectRegistry().getEntries() ) {
      Object object = registryEntry.getObject();
      if( object instanceof MapMarker ) {
        MapMarker mapMarker = ( MapMarker )object;
        String markerMapId = mapMarker.getMapId();
        if( markerMapId != null && markerMapId.equals( mapId ) ) {
          OperatorRegistry operatorRegistry = ( ( TabrisActivity )activity ).getWidgetToolkit().getOperatorRegistry();
          ( ( MarkerOperator )operatorRegistry.get( MarkerOperator.TYPE ) ).destroy( mapMarker );
        }
      }
    }
  }

  private void attachOnMapClickListener( MapHolderView mapHolderView ) {
    getGoogleMapSafely( mapHolderView )
        .setOnMapClickListener( new MapTapListener( tabrisContext.getObjectRegistry(), mapHolderView ) );
  }

  private void removeOnMapClickListener( MapHolderView mapHolderView ) {
    getGoogleMapSafely( mapHolderView ).setOnMapClickListener( null );
  }

  private void attachOnMapLongClickListener( MapHolderView mapHolderView ) {
    getGoogleMapSafely( mapHolderView )
        .setOnMapLongClickListener( new MapLongPressListener( tabrisContext.getObjectRegistry(), mapHolderView ) );
  }

  private void removeOnMapLongClickListener( MapHolderView mapHolderView ) {
    getGoogleMapSafely( mapHolderView ).setOnMapLongClickListener( null );
  }

  private GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    validateGoogleMap( googleMap, "Can not get map before 'ready' event has fired." );
    return googleMap;
  }

}
