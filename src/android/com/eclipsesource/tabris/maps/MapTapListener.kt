package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.Scope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class MapTapListener(private val scope: Scope, private val mapHolderView: MapHolderView)
  : GoogleMap.OnMapClickListener {

  override fun onMapClick(latLng: LatLng) {
    val position = listOf(latLng.latitude, latLng.longitude)
    scope.remoteObject(mapHolderView)?.notify("tap", "position", position)
  }

}
