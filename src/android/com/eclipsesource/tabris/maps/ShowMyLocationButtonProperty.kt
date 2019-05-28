package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.BooleanProperty

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
object ShowMyLocationButtonProperty : BooleanProperty<MapHolderView>("showMyLocationButton") {

  override fun set(mapHolderView: MapHolderView, showMyLocation: Boolean?) {
    mapHolderView.googleMap.uiSettings?.isMyLocationButtonEnabled = showMyLocation ?: false
  }

  override fun get(mapHolderView: MapHolderView) =
          mapHolderView.googleMap.uiSettings?.isMyLocationButtonEnabled ?: false

}
