package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.StringProperty

object MarkerSubtitleProperty : StringProperty<MapMarker>("subtitle", {
  it?.let {
    subtitle = it
  }
})
