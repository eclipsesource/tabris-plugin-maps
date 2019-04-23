package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.StringProperty
import com.google.android.gms.maps.GoogleMap

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class MapTypeProperty : StringProperty<MapHolderView>("mapType") {

  private val mapTypes = mapOf(
      "none" to GoogleMap.MAP_TYPE_NONE,
      "hybrid" to GoogleMap.MAP_TYPE_HYBRID,
      "normal" to GoogleMap.MAP_TYPE_NORMAL,
      "satellite" to GoogleMap.MAP_TYPE_SATELLITE,
      "terrain" to GoogleMap.MAP_TYPE_TERRAIN
  )

  override fun set(mapHolderView: MapHolderView, mapType: String?) {
    require(mapType != null) { "The 'mapType' property has to be a string value." }
    mapTypes[mapType]?.let {
      mapHolderView.googleMap?.setMapType(it)
    }
  }

  override fun get(mapHolderView: MapHolderView): Any? {
    val mapType = mapHolderView.googleMap?.mapType
    for ((key, value) in mapTypes) {
      if (value == mapType) {
        return key
      }
    }
    return null
  }

}
