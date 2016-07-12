package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.HashMap;

import static java.util.Arrays.asList;

public class MapCameraChangeListener implements GoogleMap.OnCameraChangeListener {

  public static final String EVENT_CAMERA_MOVE = "cameramove";
  public static final String EVENT_CAMERA_MOVE_PROGRAMMATIC = "cameramoveprogrammatic";

  private final ObjectRegistry objectRegistry;
  private final MapHolderView mapHolderView;

  public MapCameraChangeListener( ObjectRegistry objectRegistry, MapHolderView mapHolderView ) {
    this.objectRegistry = objectRegistry;
    this.mapHolderView = mapHolderView;
  }

  @Override
  public void onCameraChange( CameraPosition cameraPosition ) {
    String event = EVENT_CAMERA_MOVE;
    if( !mapHolderView.isChangeUserInitiated() ) {
      event = EVENT_CAMERA_MOVE_PROGRAMMATIC;
      mapHolderView.setChangeUserInitiated( true );
    }
    HashMap<String, Object> arguments = new HashMap<>();
    arguments.put( "position", asList( cameraPosition.target.latitude, cameraPosition.target.longitude ) );
    objectRegistry.getRemoteObjectForObject( mapHolderView ).notify( event, arguments );
  }

}
