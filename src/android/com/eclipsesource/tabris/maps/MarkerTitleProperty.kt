package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.StringProperty

object MarkerTitleProperty : StringProperty<MapMarker>("title", {
  if (it != null) {
    title = it
  }
})
