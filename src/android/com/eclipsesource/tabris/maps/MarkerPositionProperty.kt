package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.V8ArrayProperty
import com.eclipsesource.tabris.android.internal.ktx.asList
import com.eclipsesource.v8.V8Array
import com.google.android.gms.maps.model.LatLng

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class MarkerPositionProperty : V8ArrayProperty<MapMarker>("position") {

  override fun set(marker: MapMarker, property: V8Array?) {
    val position = property?.asList<Double>()
    require(position != null && position.size == 2) {
      "The 'position' property has to be a 2 element tuple but is $position"
    }
    marker.position = LatLng(position[0], position[1])
  }

  override fun get(marker: MapMarker): Any? =
      marker.position?.let { listOf(it.latitude, it.longitude) }

}
