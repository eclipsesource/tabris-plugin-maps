/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisWidgetPropertyHandler;
import com.eclipsesource.tabris.android.internal.toolkit.property.ViewPropertyHandler;
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

  private Map<String, Integer> mapTypes;

  public MapPropertyHandler( Activity activity, TabrisContext context ) {
    super( activity, context );
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
    System.out.println( "set: " + properties );
    for( String key : properties.getAll().keySet() ) {
      setProperty( key, view.getGoogleMap(), properties );
    }
  }

  private void setProperty( String key, GoogleMap map, Properties properties ) {
    switch( key ) {
      case "center":
        setCenter( map, properties );
        break;
      case "zoom":
        setZoom( map, properties );
        break;
      case "mapType":
        setMapType( map, properties );
        break;
    }
  }

  private void setCenter( GoogleMap map, Properties properties ) {
    List<Double> coordinates = properties.getList( "center", Double.class );
    if( coordinates.size() != 2 ) {
      throw new IllegalArgumentException( "The center property has to be a 2 element tuple" );
    }
    LatLng latLng = new LatLng( coordinates.get( 0 ), coordinates.get( 1 ) );
    System.out.println( "move to: " + latLng );
    map.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
  }

  private void setZoom( GoogleMap map, Properties properties ) {
    Float zoom = properties.getFloat( "zoom" );
    if( zoom == null ) {
      throw new IllegalArgumentException( "The zoom property has to be a float value" );
    }
    System.out.println( "zoom to: " + zoom );
    map.moveCamera( CameraUpdateFactory.zoomTo( zoom ) );
  }

  private void setMapType( GoogleMap map, Properties properties ) {
    String mapTypeString = properties.getString( "mapType" );
    if( mapTypeString == null ) {
      throw new IllegalArgumentException( "The mapType property has to be a string value." );
    }
    Integer mapTypeInteger = mapTypes.get( mapTypeString );
    if( mapTypeInteger != null ) {
      map.setMapType( mapTypeInteger );
    }
  }

  @Override
  public Object get( MapHolderView mapHolderView, String property ) {
    switch( property ) {
      case "minZoomLevel":
        return getGoogleMapSafely( mapHolderView ).getMinZoomLevel();
      case "maxZoomLevel":
        return getGoogleMapSafely( mapHolderView ).getMaxZoomLevel();
      case "center":
        return getCenter( mapHolderView );
      case "zoom":
        return getZoom( mapHolderView );
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

}
