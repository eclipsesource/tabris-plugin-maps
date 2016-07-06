/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisWidgetPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap;

public class MapPropertyHandler extends TabrisWidgetPropertyHandler<MapHolderView> {

  private static final String PROP_CENTER = "center";
  private static final String PROP_MIN_ZOOM_LEVEL = "minZoomLevel";
  private static final String PROP_MAX_ZOOM_LEVEL = "maxZoomLevel";
  private static final String PROP_ZOOM = "zoom";
  private static final String PROP_MAP_TYPE = "mapType";
  private static final String PROP_SHOW_MY_LOCATION = "showMyLocation";
  private static final String PROP_SHOW_MY_LOCATION_BUTTON = "showMyLocationButton";

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
      setProperty( key, view.getGoogleMap(), properties );
    }
  }

  private void setProperty( String key, GoogleMap map, Properties properties ) {
    switch( key ) {
      case PROP_CENTER:
        setCenter( map, properties );
        break;
      case PROP_ZOOM:
        setZoom( map, properties );
        break;
      case PROP_MAP_TYPE:
        setMapType( map, properties );
        break;
      case PROP_SHOW_MY_LOCATION:
        setShowMyLocation( map, properties );
        break;
      case PROP_SHOW_MY_LOCATION_BUTTON:
        setShowMyLocationButton( map, properties );
        break;
    }
  }

  private void setCenter( GoogleMap map, Properties properties ) {
    List<Double> coordinates = properties.getList( PROP_CENTER, Double.class );
    if( coordinates.size() != 2 ) {
      throw new IllegalArgumentException( "The center property has to be a 2 element tuple" );
    }
    LatLng latLng = new LatLng( coordinates.get( 0 ), coordinates.get( 1 ) );
    System.out.println( "move to: " + latLng );
    map.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
  }

  private void setZoom( GoogleMap map, Properties properties ) {
    Float zoom = properties.getFloat( PROP_ZOOM );
    if( zoom == null ) {
      throw new IllegalArgumentException( "The zoom property has to be a float value" );
    }
    System.out.println( "zoom to: " + zoom );
    map.moveCamera( CameraUpdateFactory.zoomTo( zoom ) );
  }

  private void setMapType( GoogleMap map, Properties properties ) {
    String mapTypeString = properties.getString( PROP_MAP_TYPE );
    if( mapTypeString == null ) {
      throw new IllegalArgumentException( "The mapType property has to be a string value." );
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
      case PROP_CENTER:
        return getCenter( mapHolderView );
      case PROP_ZOOM:
        return getZoom( mapHolderView );
      case PROP_SHOW_MY_LOCATION:
        return getShowMyLocation( mapHolderView );
      case PROP_SHOW_MY_LOCATION_BUTTON:
        return getShowMyLocationButton( mapHolderView );
      default:
        return super.get( mapHolderView, property );
    }
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

  @NonNull
  private List<Double> getCenter( MapHolderView mapHolderView ) {
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    LatLng target = googleMap.getCameraPosition().target;
    Double[] position = { target.latitude, target.longitude };
    return Arrays.asList( position );
  }

  private boolean getShowMyLocation( MapHolderView mapHolderView ) {
    return getGoogleMapSafely( mapHolderView ).isMyLocationEnabled();
  }

  private boolean getShowMyLocationButton( MapHolderView mapHolderView ) {
    return getGoogleMapSafely( mapHolderView ).getUiSettings().isMyLocationButtonEnabled();
  }

}
