package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.maps.MapHolderView
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.model.LatLng

class MapLongPressListener(private val scope: ActivityScope, private val mapHolderView: MapHolderView)
  : OnMapLongClickListener {

  override fun onMapLongClick(latLng: LatLng) {
    val position = listOf(latLng.latitude, latLng.longitude)
    scope.remoteObject(mapHolderView)?.notify("longpress", "position", position)
  }

}
