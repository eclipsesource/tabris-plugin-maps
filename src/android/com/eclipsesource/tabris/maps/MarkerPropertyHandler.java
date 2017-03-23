package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.Properties;
import com.eclipsesource.tabris.android.PropertyHandler;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static java.util.Arrays.asList;

public class MarkerPropertyHandler<T extends MapMarker> implements PropertyHandler<T> {

  private static final String PROP_POSITION = "position";
  private static final String PROP_TITLE = "title";
  private static final String PROP_SUBTITLE = "subtitle";

  @Override
  public void set( T marker, Properties properties ) {
    setPosition( marker, properties );
    setTitle( marker, properties );
    setSubtitle( marker, properties );
    marker.updateMarker();
  }

  private void setPosition( T marker, Properties properties ) {
    List<Double> position = properties.getList( PROP_POSITION, Double.class );
    if( position == null || position.size() != 2 ) {
      throw new IllegalArgumentException( "The 'position' property has to be a 2 element tuple but is " + position );
    }
    marker.setPosition( new LatLng( position.get( 0 ), position.get( 1 ) ) );
  }

  private void setTitle( T marker, Properties properties ) {
    if( properties.hasProperty( PROP_TITLE ) ) {
      marker.getMarker().setTitle( properties.getString( PROP_TITLE ) );
    }
  }

  private void setSubtitle( T marker, Properties properties ) {
    if( properties.hasProperty( PROP_SUBTITLE ) ) {
      marker.getMarker().setTitle( properties.getString( PROP_SUBTITLE ) );
    }
  }

  @Override
  public Object get( T marker, String property ) {
    switch( property ) {
      case PROP_POSITION:
        return getPosition( marker );
    }
    return null;
  }

  private Object getPosition( T marker ) {
    LatLng position = marker.getPosition();
    if( position != null ) {
      return asList( position.latitude, position.longitude );
    }
    return null;
  }

}
