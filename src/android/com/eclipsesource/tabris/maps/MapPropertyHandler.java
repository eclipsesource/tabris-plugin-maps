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

import java.util.List;

public class MapPropertyHandler extends ViewPropertyHandler<MapHolderView> {

  public MapPropertyHandler(TabrisActivity activity) {
    super(activity);
  }

  @Override
  public void set(MapHolderView view, Properties properties) {
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

  @Override
  public Object get(MapHolderView view, String property) {
    return super.get(view, property);
  }

}
