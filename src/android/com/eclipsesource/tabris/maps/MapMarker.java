package com.eclipsesource.tabris.maps;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapMarker {

  private LatLng position;
  private String title;
  private BitmapDescriptor icon;
  private String subtitle;
  private Marker marker;
  private String mapId;

  public void setPosition(LatLng position) {
    this.position = position;
  }

  public LatLng getPosition() {
    return position;
  }

  public void setIcon(BitmapDescriptor icon) {
    this.icon = icon;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public void setMarker(Marker marker) {
    this.marker = marker;
  }

  public Marker getMarker() {
    return marker;
  }

  public void setMapId(String mapId) {
    this.mapId = mapId;
  }

  public String getMapId() {
    return mapId;
  }

  public void updateMarker() {
    if (marker != null) {
      marker.setPosition(position);
      marker.setTitle(title);
      marker.setIcon(icon);
      marker.setSnippet(subtitle);
    }
  }
}
