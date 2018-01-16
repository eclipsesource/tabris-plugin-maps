package com.eclipsesource.tabris.maps;

import android.app.Activity;

import com.eclipsesource.tabris.android.AbstractOperator;
import com.eclipsesource.tabris.android.Properties;
import com.eclipsesource.tabris.android.PropertyHandler;
import com.eclipsesource.tabris.android.TabrisContext;
import com.google.android.gms.maps.model.Marker;

public class MarkerOperator extends AbstractOperator<MapMarker> {

  public static final String TYPE = "com.eclipsesource.maps.Marker";

  private final MarkerPropertyHandler<MapMarker> markerPropertyHandler;

  public MarkerOperator(Activity activity, TabrisContext tabrisContext) {
    markerPropertyHandler = new MarkerPropertyHandler<>(tabrisContext);
  }

  @Override
  public PropertyHandler<MapMarker> getPropertyHandler(MapMarker object) {
    return markerPropertyHandler;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public MapMarker create(String id, Properties properties) {
    return new MapMarker();
  }

  @Override
  public void destroy(MapMarker mapMarker) {
    Marker marker = mapMarker.getMarker();
    if (marker != null) {
      marker.remove();
    }
  }

}
