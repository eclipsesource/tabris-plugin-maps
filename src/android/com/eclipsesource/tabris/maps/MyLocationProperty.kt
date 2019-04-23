package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.AnyProperty
import com.eclipsesource.tabris.maps.MapHolderView

object MyLocationProperty : AnyProperty<MapHolderView>("myLocation", getter = {
  val myLocation = googleMap?.myLocation
  if (myLocation != null) {
    listOf(myLocation.latitude, myLocation.longitude)
  } else null
})
