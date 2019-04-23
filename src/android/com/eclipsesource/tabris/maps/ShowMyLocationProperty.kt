package com.eclipsesource.tabris.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.android.BooleanProperty
import com.eclipsesource.tabris.maps.MapValidator.validateGoogleMap

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ShowMyLocationProperty(private val scope: ActivityScope) : BooleanProperty<MapHolderView>("showMyLocation") {

  override fun set(mapHolderView: MapHolderView, showMyLocation: Boolean?) {
    check(ContextCompat.checkSelfPermission(scope.activity, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      Manifest.permission.ACCESS_FINE_LOCATION + " not available"
    }
    mapHolderView.googleMap?.isMyLocationEnabled = showMyLocation ?: false
  }

  override fun get(mapHolderView: MapHolderView): Boolean {
    validateGoogleMap(mapHolderView.googleMap, "Only call get on map when it is ready.")
    return mapHolderView.googleMap?.isMyLocationEnabled ?: false
  }

}
