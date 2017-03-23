/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static java.util.Arrays.asList;

public class MapTapListener implements OnMapClickListener {

  public static final String EVENT_TAP = "tap";

  private final ObjectRegistry objectRegistry;
  private final MapHolderView mapHolderView;

  public MapTapListener( ObjectRegistry objectRegistry, MapHolderView mapHolderView ) {
    this.objectRegistry = objectRegistry;
    this.mapHolderView = mapHolderView;
  }

  @Override
  public void onMapClick( LatLng latLng ) {
    List<Double> position = asList( latLng.latitude, latLng.longitude );
    objectRegistry.getRemoteObjectForObject( mapHolderView ).notify( EVENT_TAP, "position", position );
  }
}
