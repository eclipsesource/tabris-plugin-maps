/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisWidgetPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap;
import static java.util.Arrays.asList;

public class MapPropertyHandler extends TabrisWidgetPropertyHandler<MapHolderView> {

  private static final String PROP_POSITION = "position";
  private static final String PROP_CAMERA = "camera";
  private static final String PROP_MIN_ZOOM_LEVEL = "minZoomLevel";
  private static final String PROP_MAX_ZOOM_LEVEL = "maxZoomLevel";
  private static final String PROP_ZOOM = "zoom";
  private static final String PROP_MAP_TYPE = "mapType";
  private static final String PROP_SHOW_MY_LOCATION = "showMyLocation";
  private static final String PROP_SHOW_MY_LOCATION_BUTTON = "showMyLocationButton";
  private static final String PROP_MY_LOCATION = "myLocation";

  private Map<String, Integer> mapTypes;
  private Activity activity;

  public MapPropertyHandler( Activity activity, TabrisContext context ) {
    super( activity, context );
    this.activity = activity;
    initMapTypesMap();
  }

  private Map<String, Integer> initMapTypesMap() {
    mapTypes = new HashMap<>();
    mapTypes.put( "none", GoogleMap.MAP_TYPE_NONE );
    mapTypes.put( "hybrid", GoogleMap.MAP_TYPE_HYBRID );
    mapTypes.put( "normal", GoogleMap.MAP_TYPE_NORMAL );
    mapTypes.put( "satellite", GoogleMap.MAP_TYPE_SATELLITE );
    mapTypes.put( "terrain", GoogleMap.MAP_TYPE_TERRAIN );
    return mapTypes;
  }

  @Override
  public void set( MapHolderView view, Properties properties ) {
    super.set( view, properties );
    for( String key : properties.getAll().keySet() ) {
      setProperty( key, view, properties );
    }
  }

  private void setProperty( String key, MapHolderView mapHolderView, Properties properties ) {
    switch( key ) {
      case PROP_POSITION:
        setPosition( mapHolderView, properties );
        break;
      case PROP_CAMERA:
        setCamera( mapHolderView, properties );
        break;
      case PROP_ZOOM:
        setZoom( mapHolderView.getGoogleMap(), properties );
        break;
      case PROP_MAP_TYPE:
        setMapType( mapHolderView.getGoogleMap(), properties );
        break;
      case PROP_SHOW_MY_LOCATION:
        setShowMyLocation( mapHolderView.getGoogleMap(), properties );
        break;
      case PROP_SHOW_MY_LOCATION_BUTTON:
        setShowMyLocationButton( mapHolderView.getGoogleMap(), properties );
        break;
    }
  }

  private void setCamera( MapHolderView mapHolderView, Properties properties ) {
    setPosition( mapHolderView, properties.getPropertiesSafe( PROP_CAMERA ) );
  }

  private void setPosition( MapHolderView mapHolderView, Properties properties ) {
    List<Double> position = properties.getList( PROP_POSITION, Double.class );
    if( position == null || position.size() != 2 ) {
      throw new IllegalArgumentException( "The 'position' property has to be a 2 element tuple but is " + position );
    }
    mapHolderView.setChangeUserInitiated( false );
    mapHolderView.getGoogleMap().moveCamera(
        CameraUpdateFactory.newLatLng( new LatLng( position.get( 0 ), position.get( 1 ) ) ) );
  }

  private void setZoom( GoogleMap map, Properties properties ) {
    if( properties.getFloat( PROP_ZOOM ) == null ) {
      throw new IllegalArgumentException( "The 'zoom' property has to be a number but is null" );
    }
    map.moveCamera( CameraUpdateFactory.zoomTo( properties.getFloat( PROP_ZOOM ) ) );
  }

  private void setMapType( GoogleMap map, Properties properties ) {
    String mapTypeString = properties.getString( PROP_MAP_TYPE );
    if( mapTypeString == null ) {
      throw new IllegalArgumentException( "The 'mapType' property has to be a string value." );
    }
    Integer mapTypeInteger = mapTypes.get( mapTypeString );
    if( mapTypeInteger != null ) {
      map.setMapType( mapTypeInteger );
    }
  }

  private void setShowMyLocation( GoogleMap map, Properties properties ) {
    if( ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION )
        != PackageManager.PERMISSION_GRANTED ) {
      throw new IllegalStateException( Manifest.permission.ACCESS_FINE_LOCATION + " not available" );
    }
    map.setMyLocationEnabled( properties.getBooleanSafe( PROP_SHOW_MY_LOCATION ) );
  }

  private void setShowMyLocationButton( GoogleMap map, Properties properties ) {
    map.getUiSettings().setMyLocationButtonEnabled( properties.getBooleanSafe( PROP_SHOW_MY_LOCATION_BUTTON ) );
  }

  @Override
  public Object get( MapHolderView mapHolderView, String property ) {
    switch( property ) {
      case PROP_MIN_ZOOM_LEVEL:
        return getGoogleMapSafely( mapHolderView ).getMinZoomLevel();
      case PROP_MAX_ZOOM_LEVEL:
        return getGoogleMapSafely( mapHolderView ).getMaxZoomLevel();
      case PROP_POSITION:
        return getPosition( mapHolderView );
      case PROP_CAMERA:
        return getCamera( mapHolderView );
      case PROP_ZOOM:
        return getZoom( mapHolderView );
      case PROP_SHOW_MY_LOCATION:
        return getShowMyLocation( mapHolderView );
      case PROP_SHOW_MY_LOCATION_BUTTON:
        return getShowMyLocationButton( mapHolderView );
      case PROP_MY_LOCATION:
        return getMyLocation( mapHolderView );
      default:
        return super.get( mapHolderView, property );
    }
  }

  private Object getMyLocation( MapHolderView mapHolderView ) {
    Location myLocation = mapHolderView.getGoogleMap().getMyLocation();
    if( myLocation != null ) {
      return asList( myLocation.getLatitude(), myLocation.getLongitude() );
    }
    return null;
  }

  private GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    validateGoogleMap( googleMap, "Only call get on map when it is ready." );
    return googleMap;
  }

  private float getZoom( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    return googleMap.getCameraPosition().zoom;
  }

  private List<Double> getPosition( MapHolderView mapHolderView ) {
    LatLng target = getGoogleMapSafely( mapHolderView ).getCameraPosition().target;
    return asList( target.latitude, target.longitude );
  }

  private Object getCamera( MapHolderView mapHolderView ) {
    LatLng target = getGoogleMapSafely( mapHolderView ).getCameraPosition().target;
    HashMap<String, Object> camera = new HashMap<>();
    camera.put( PROP_POSITION, asList( target.latitude, target.longitude ) );
    return camera;
  }

  private boolean getShowMyLocation( MapHolderView mapHolderView ) {
    return getGoogleMapSafely( mapHolderView ).isMyLocationEnabled();
  }

  private boolean getShowMyLocationButton( MapHolderView mapHolderView ) {
    return getGoogleMapSafely( mapHolderView ).getUiSettings().isMyLocationButtonEnabled();
  }

}
