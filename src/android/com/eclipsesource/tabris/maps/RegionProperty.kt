package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.V8ObjectProperty
import com.eclipsesource.tabris.android.internal.ktx.asList
import com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap
import com.eclipsesource.v8.V8Object
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class RegionProperty : V8ObjectProperty<MapHolderView>("position") {

  override fun set(mapHolderView: MapHolderView, v8Object: V8Object?) {
    v8Object?.let {
      val bounds = createBoundsFromBoundingBox(it.getObject("region"))
      mapHolderView.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))
    }
  }

  override fun get(mapHolderView: MapHolderView): Any {
    validateGoogleMap(mapHolderView.googleMap, "Only call get on map when it is ready.")
    val bounds = (mapHolderView.googleMap as GoogleMap).projection.visibleRegion.latLngBounds
    return mapOf(
        "southWest" to listOf(bounds.southwest.latitude, bounds.southwest.longitude),
        "northEast" to listOf(bounds.northeast.latitude, bounds.northeast.longitude)
    )
  }

  private fun createBoundsFromBoundingBox(v8Object: V8Object): LatLngBounds {
    val southWest = v8Object.getArray("southWest").asList<Double>()
    val northEast = v8Object.getArray("northEast").asList<Double>()
    return LatLngBounds(
        LatLng(southWest[0], southWest[1]),
        LatLng(northEast[0], northEast[1]))
  }

}
