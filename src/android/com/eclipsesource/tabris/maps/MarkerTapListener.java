package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.ObjectRegistry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerTapListener implements GoogleMap.OnMarkerClickListener {

  private static final java.lang.String EVENT_TAP = "tap";

  private final MapHolderView mapHolderView;
  private final ObjectRegistry objectRegistry;

  public MarkerTapListener(MapHolderView mapHolderView, ObjectRegistry objectRegistry) {
    this.mapHolderView = mapHolderView;
    this.objectRegistry = objectRegistry;
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    String mapId = objectRegistry.getRemoteObjectForObject(mapHolderView).getId();
    for (ObjectRegistry.RegistryEntry entry : objectRegistry.getEntries()) {
      Object object = entry.getObject();
      if (object instanceof MapMarker) {
        MapMarker mapMarker = (MapMarker)object;
        if (mapMarker.getMarker() != null && mapMarker.getMarker().getId().equals(marker.getId())
            && mapMarker.getMapId().equals(mapId)) {
          objectRegistry.getRemoteObjectForObject(object).notify(EVENT_TAP);
          break;
        }
      }
    }
    return true;
  }

}