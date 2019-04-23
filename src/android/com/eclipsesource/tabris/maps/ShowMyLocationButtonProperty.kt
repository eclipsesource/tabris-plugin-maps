package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.BooleanProperty
import com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ShowMyLocationButtonProperty : BooleanProperty<MapHolderView>("showMyLocationButton") {

  override fun set(mapHolderView: MapHolderView, showMyLocation: Boolean?) {
    mapHolderView.googleMap?.uiSettings?.isMyLocationButtonEnabled = showMyLocation ?: false
  }

  override fun get(mapHolderView: MapHolderView): Boolean {
    validateGoogleMap(mapHolderView.googleMap, "Only call get on map when it is ready.")
    return mapHolderView.googleMap?.uiSettings?.isMyLocationButtonEnabled ?: false
  }

}
