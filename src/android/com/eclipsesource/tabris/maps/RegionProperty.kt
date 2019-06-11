package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.V8ObjectProperty
import com.eclipsesource.tabris.android.internal.ktx.asList
import com.eclipsesource.v8.V8Object
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
object RegionProperty : V8ObjectProperty<MapHolderView>("region") {

  override fun set(mapHolderView: MapHolderView, region: V8Object?) {
    region?.let {
      val bounds = createBoundsFromBoundingBox(it)
      mapHolderView.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))
    }
  }

  override fun get(mapHolderView: MapHolderView): Any {
    val bounds = mapHolderView.googleMap.projection.visibleRegion.latLngBounds
    return mapOf(
        "southWest" to listOf(bounds.southwest.latitude, bounds.southwest.longitude),
        "northEast" to listOf(bounds.northeast.latitude, bounds.northeast.longitude)
    )
  }

  private fun createBoundsFromBoundingBox(region: V8Object): LatLngBounds {
    val southWest = region.getArray("southWest").asList<Double>()
    val northEast = region.getArray("northEast").asList<Double>()
    return LatLngBounds(
        LatLng(southWest[0], southWest[1]),
        LatLng(northEast[0], northEast[1]))
  }

}
