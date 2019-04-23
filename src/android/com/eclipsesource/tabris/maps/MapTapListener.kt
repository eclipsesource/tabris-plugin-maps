package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.maps.MapHolderView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class MapTapListener(private val scope: ActivityScope, private val mapHolderView: MapHolderView)
  : GoogleMap.OnMapClickListener {

  override fun onMapClick(latLng: LatLng) {
    val position = listOf(latLng.latitude, latLng.longitude)
    scope.remoteObject(mapHolderView)?.notify("tap", "position", position)
  }

}
