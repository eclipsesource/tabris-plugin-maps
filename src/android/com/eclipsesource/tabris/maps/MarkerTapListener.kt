package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.Scope
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.Marker

class MarkerTapListener(private val mapHolderView: MapHolderView, private val scope: Scope) : OnMarkerClickListener {

  override fun onMarkerClick(marker: Marker): Boolean {
    val mapId = scope.remoteObject(mapHolderView)?.id
    scope.objectRegistry.find<MapMarker>().forEach { mapMarker ->
      mapMarker.marker?.let {
        if (it.id == marker.id && mapMarker.mapId == mapId) {
          scope.remoteObject(mapMarker)?.notify("tap")
          return true
        }
      }
    }
    return true
  }

}
