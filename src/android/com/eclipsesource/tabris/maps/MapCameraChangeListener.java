package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.List;

import static java.util.Arrays.asList;

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
    List<Double> latLng = asList( cameraPosition.target.latitude, cameraPosition.target.longitude );
    objectRegistry.getRemoteObjectForObject( mapHolderView ).notify( EVENT_CAMERACHANGE, "latLng", latLng );
  }
}
