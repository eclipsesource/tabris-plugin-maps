package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.V8ObjectProperty
import com.eclipsesource.tabris.android.internal.ktx.asList
import com.eclipsesource.v8.V8Object
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import java.util.Arrays.asList

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
object CameraProperty : V8ObjectProperty<MapHolderView>("camera") {

  override fun set(mapHolderView: MapHolderView, property: V8Object?) {
    val position = property?.getArray("position")?.asList<Double>()
    require(position != null && position.size == 2) {
      "The 'position' property has to be a 2 element tuple but is $position"
    }
    val latLng = LatLng(position[0], position[1])
    mapHolderView.moveCamera(CameraUpdateFactory.newLatLng(latLng))
  }

  override fun get(mapHolderView: MapHolderView): Any? {
    mapHolderView.googleMap.cameraPosition?.target?.let {
      return mapOf("position" to asList(it.latitude, it.longitude))
    }
    return null
  }

}