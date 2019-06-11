package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.Scope
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.Marker

class MarkerTapListener(private val mapHolderView: MapHolderView, private val scope: Scope) : OnMarkerClickListener {

  override fun onMarkerClick(marker: Marker): Boolean {
    val mapId = scope.remoteObject(mapHolderView)?.id
    scope.objectRegistry.find<MapMarker>()
        .find { it.marker?.id == marker.id && it.mapId == mapId }
        .let { scope.remoteObject(it)?.notify("tap") }
    return true
  }

}
