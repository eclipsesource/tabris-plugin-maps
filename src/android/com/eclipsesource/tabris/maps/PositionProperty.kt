package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.V8ArrayProperty
import com.eclipsesource.tabris.android.internal.ktx.asList
import com.eclipsesource.v8.V8Array
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
object PositionProperty : V8ArrayProperty<MapHolderView>("position") {

  override fun set(mapHolderView: MapHolderView, position: V8Array?) {
    val positionList = position?.asList<Double>()
    require(positionList != null && positionList.size == 2) {
      "The 'position' property has to be a 2 element tuple but is $positionList"
    }
    val latLng = LatLng(positionList[0], positionList[1])
    mapHolderView.moveCamera(CameraUpdateFactory.newLatLng(latLng))
  }

  override fun get(mapHolderView: MapHolderView): List<Double>? {
    val googleMap = requireNotNull(mapHolderView.googleMap) {
      "Google Map is not yet ready. Only call get on map when it is ready."
    }
    googleMap.cameraPosition?.target?.let {
      return listOf(it.latitude, it.longitude)
    }
    return null
  }

}
