package com.eclipsesource.tabris.maps;

import android.app.Activity;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.Marker;

public class MarkerOperator extends AbstractTabrisOperator<MapMarker> {

  public static final String TYPE = "com.eclipsesource.maps.Marker";

  private static final String EVENT_TAP = "tap";

  private final MarkerPropertyHandler<MapMarker> markerPropertyHandler;

  public MarkerOperator( Activity activity, TabrisContext tabrisContext ) {
    markerPropertyHandler = new MarkerPropertyHandler<>( activity, tabrisContext );
  }

  @Override
  public TabrisPropertyHandler<MapMarker> getPropertyHandler( MapMarker object ) {
    return markerPropertyHandler;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public MapMarker create( String id, Properties properties ) {
    return new MapMarker();
  }

  @Override
  public void destroy( MapMarker mapMarker ) {
    Marker marker = mapMarker.getMarker();
    if( marker != null ) {
      marker.remove();
    }
  }

}
