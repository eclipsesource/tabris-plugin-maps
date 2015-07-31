/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.property.ViewPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPropertyHandler extends ViewPropertyHandler<MapHolderView> {

  private Map<String, Integer> mapTypes;

  public MapPropertyHandler(TabrisActivity activity) {
    super(activity);
    initMapTypesMap();
  }

  private Map<String, Integer> initMapTypesMap() {
    mapTypes = new HashMap<>();
    mapTypes.put("none", GoogleMap.MAP_TYPE_NONE);
    mapTypes.put("hybrid", GoogleMap.MAP_TYPE_HYBRID);
    mapTypes.put("normal", GoogleMap.MAP_TYPE_NORMAL);
    mapTypes.put("satellite", GoogleMap.MAP_TYPE_SATELLITE);
    mapTypes.put("terrain", GoogleMap.MAP_TYPE_TERRAIN);
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
      case "latLng":
        setLatLong( map, properties );
        break;
      case "zoom":
        setZoom( map, properties );
        break;
      case "mapType":
        setMapType( map, properties );
        break;
    }
  }

  private void setLatLong( GoogleMap map, Properties properties ) {
    List<Double> coordinates = properties.getList( "latLng", Double.class );
    if( coordinates.size() != 2 ) {
      throw new IllegalArgumentException( "The latLng property has to be a 2 element tuple" );
    }
    LatLng latLng = new LatLng( coordinates.get( 0 ), coordinates.get( 1 ) );
    System.out.println( "move to: " + latLng );
    map.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
  }

  private void setZoom( GoogleMap map, Properties properties ) {
    Integer zoom = properties.getInteger( "zoom" );
    if( zoom == null ) {
      throw new IllegalArgumentException( "The zoom property has to be an integer value" );
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
  public Object get(MapHolderView view, String property) {
    return super.get(view, property);
  }

}
