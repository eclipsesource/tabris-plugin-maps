package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.HashMap;

import static java.util.Arrays.asList;

public class MapCameraChangeListener implements GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

  public static final String EVENT_CAMERA_MOVE = "cameramove";
  public static final String EVENT_CHANGE_CAMERA = "changecamera";

  private final ObjectRegistry objectRegistry;
  private final MapHolderView mapHolderView;
  private int reason;

  public MapCameraChangeListener( ObjectRegistry objectRegistry, MapHolderView mapHolderView ) {
    this.objectRegistry = objectRegistry;
    this.mapHolderView = mapHolderView;
  }

  @Override
  public void onCameraMoveStarted( int reason ) {
    this.reason = reason;
  }

  @Override
  public void onCameraIdle() {
    CameraPosition cameraPosition = mapHolderView.getGoogleMap().getCameraPosition();
    notifyChangeCameraEvent( cameraPosition );
    if( reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE ) {
      notifyCameraMoveEvent( cameraPosition );
    }
  }
  private void notifyChangeCameraEvent( CameraPosition cameraPosition ) {
    HashMap<String, Object> arguments = new HashMap<>();
    arguments.put( "position", asList( cameraPosition.target.latitude, cameraPosition.target.longitude ) );
    objectRegistry.getRemoteObjectForObject( mapHolderView ).notify( EVENT_CHANGE_CAMERA, arguments );
  }

  private void notifyCameraMoveEvent( CameraPosition cameraPosition ) {
    HashMap<String, Object> arguments = new HashMap<>();
    arguments.put( "position", asList( cameraPosition.target.latitude, cameraPosition.target.longitude ) );
    objectRegistry.getRemoteObjectForObject( mapHolderView ).notify( EVENT_CAMERA_MOVE, "camera", arguments );
  }

}
