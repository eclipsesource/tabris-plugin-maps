/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapLongClickListener implements GoogleMap.OnMapLongClickListener {

  public static final String EVENT_LONGPRESS = "longpress";

  private final ObjectRegistry objectRegistry;
  private final MapHolderView mapHolderView;

  public MapLongClickListener( ObjectRegistry objectRegistry, MapHolderView mapHolderView ) {
    this.objectRegistry = objectRegistry;
    this.mapHolderView = mapHolderView;
  }

  @Override
  public void onMapLongClick( LatLng latLng ) {
    List<Double> properties = new ArrayList<>();
    properties.add( latLng.latitude );
    properties.add( latLng.longitude );
    objectRegistry.getRemoteObjectForObject( mapHolderView ).notify( EVENT_LONGPRESS, "latLng", properties );
  }
}
