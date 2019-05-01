package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.AnyProperty

object MyLocationProperty : AnyProperty<MapHolderView>("myLocation", getter = {
  googleMap?.myLocation?.let { listOf(it.latitude, it.longitude) }
})
