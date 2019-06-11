package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.StringProperty

object MarkerSnippetProperty : StringProperty<MapMarker>("snippet", {
  if (it != null) {
    snippet = it
  }
})
