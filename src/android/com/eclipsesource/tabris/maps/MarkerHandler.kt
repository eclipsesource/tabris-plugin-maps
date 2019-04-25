package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.android.ObjectHandler
import com.eclipsesource.tabris.android.Property
import com.eclipsesource.v8.V8Object

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class MarkerHandler(scope: ActivityScope) : ObjectHandler<MapMarker> {

  override val type = TYPE

  override val properties: List<Property<MapMarker, *>> = listOf(
      MarkerImageProperty(scope),
      MarkerPositionProperty(),
      MarkerSubtitleProperty,
      MarkerTitleProperty
  )

  override fun create(id: String, properties: V8Object) = MapMarker()

  override fun destroy(mapMarker: MapMarker) {
    mapMarker.marker?.remove()
  }

  companion object {
    const val TYPE = "com.eclipsesource.maps.Marker"
  }

}
