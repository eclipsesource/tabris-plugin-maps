package com.eclipsesource.tabris.maps

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapMarker {

  var position: LatLng? = null
  var title: String? = null
  var icon: BitmapDescriptor? = null
  var subtitle: String? = null
  var marker: Marker? = null
  var mapId: String? = null

  fun updateMarker() {
    marker?.let {
      it.position = position
      it.title = title
      it.setIcon(icon)
      it.snippet = subtitle
    }
  }

}
