/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static java.util.Arrays.asList;

public class MapLongPressListener implements OnMapLongClickListener {

  public static final String EVENT_LONGPRESS = "longpress";

  private final ObjectRegistry objectRegistry;
  private final MapHolderView mapHolderView;

  public MapLongPressListener(ObjectRegistry objectRegistry, MapHolderView mapHolderView) {
    this.objectRegistry = objectRegistry;
    this.mapHolderView = mapHolderView;
  }

  @Override
  public void onMapLongClick(LatLng latLng) {
    List<Double> position = asList(latLng.latitude, latLng.longitude);
    objectRegistry.getRemoteObjectForObject(mapHolderView).notify(EVENT_LONGPRESS, "position", position);
  }
}
