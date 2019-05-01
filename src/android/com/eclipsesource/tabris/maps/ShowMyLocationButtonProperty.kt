package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.BooleanProperty

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
object ShowMyLocationButtonProperty : BooleanProperty<MapHolderView>("showMyLocationButton") {

  override fun set(mapHolderView: MapHolderView, showMyLocation: Boolean?) {
    mapHolderView.googleMap?.uiSettings?.isMyLocationButtonEnabled = showMyLocation ?: false
  }

  override fun get(mapHolderView: MapHolderView): Boolean {
    val googleMap = requireNotNull(mapHolderView.googleMap) {
      "Google Map is not yet ready. Only call get on map when it is ready."
    }
    return googleMap.uiSettings?.isMyLocationButtonEnabled ?: false
  }

}
