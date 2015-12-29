package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapCameraChangeListener implements GoogleMap.OnCameraChangeListener {

  public static final String EVENT_CAMERACHANGE = "pan";

  private final ObjectRegistry objectRegistry;
  private final MapHolderView mapHolderView;

  public MapCameraChangeListener( ObjectRegistry objectRegistry, MapHolderView mapHolderView ) {
    this.objectRegistry = objectRegistry;
    this.mapHolderView = mapHolderView;
  }

  @Override
  public void onCameraChange( CameraPosition cameraPosition ) {
    List<Double> properties = new ArrayList<>();
    LatLng target = cameraPosition.target;
    properties.add( target.latitude );
    properties.add( target.longitude );
    objectRegistry.getRemoteObjectForObject( mapHolderView ).notify( EVENT_CAMERACHANGE, "latLng", properties );

  }
}
