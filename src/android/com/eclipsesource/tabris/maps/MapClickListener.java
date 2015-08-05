/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapClickListener implements GoogleMap.OnMapClickListener {

  public static final String EVENT_MAP_TAP = "maptap";

  private TabrisActivity activity;
  private MapHolderView mapHolderView;

  public MapClickListener( TabrisActivity activity, MapHolderView mapHolderView ) {
    this.activity = activity;
    this.mapHolderView = mapHolderView;
  }

  @Override
  public void onMapClick( LatLng latLng ) {
    List<Double> properties = new ArrayList<>();
    properties.add( latLng.latitude );
    properties.add( latLng.longitude );
    activity.getRemoteObject( mapHolderView ).notify( EVENT_MAP_TAP, "latLng", properties );
  }
}
