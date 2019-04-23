package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.V8ArrayProperty
import com.eclipsesource.tabris.android.internal.ktx.asList
import com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap
import com.eclipsesource.v8.V8Array
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class PositionProperty : V8ArrayProperty<MapHolderView>("position") {

  override fun set(mapHolderView: MapHolderView, property: V8Array?) {
    val position = property?.asList<Double>()
    require(position != null && position.size == 2) {
      "The 'position' property has to be a 2 element tuple but is $position"
    }
    val latLng = LatLng(position[0], position[1])
    mapHolderView.moveCamera(CameraUpdateFactory.newLatLng(latLng))
  }

  override fun get(mapHolderView: MapHolderView): List<Double> {
    validateGoogleMap(mapHolderView.googleMap, "Only call get on map when it is ready.")
    val target = mapHolderView.googleMap?.cameraPosition?.target
    return listOf(target!!.latitude, target.longitude)
  }

}
