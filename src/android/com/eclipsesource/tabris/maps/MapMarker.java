package com.eclipsesource.tabris.maps;

import com.google.android.gms.maps.model.Marker;

public class MapMarker {

  private String mapId;
  private Marker marker;

  public MapMarker( Marker marker, String mapId ) {
    this.mapId = mapId;
    this.marker = marker;
  }

  public String getMapId() {
    return mapId;
  }

  public Marker getMarker() {
    return marker;
  }
}
