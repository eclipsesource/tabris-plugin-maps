package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.ActivityScope
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.Marker

class MarkerTapListener(private val mapHolderView: MapHolderView, private val scope: ActivityScope) : OnMarkerClickListener {

  override fun onMarkerClick(marker: Marker): Boolean {
    val mapId = scope.remoteObject(mapHolderView)?.id
    val mapMarkerList = scope.objectRegistry.find<MapMarker>()
     for (index in 0 until mapMarkerList.size) {
      val mapMarker = mapMarkerList[index]
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
