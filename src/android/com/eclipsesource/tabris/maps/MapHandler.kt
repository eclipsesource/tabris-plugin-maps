package com.eclipsesource.tabris.maps

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.android.Property
import com.eclipsesource.tabris.android.internal.ktx.getFloat
import com.eclipsesource.tabris.android.internal.ktx.toList
import com.eclipsesource.tabris.android.internal.ktx.toPixel
import com.eclipsesource.tabris.android.internal.nativeobject.view.ViewHandler
import com.eclipsesource.v8.V8Object
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.roundToInt

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
open class MapHandler(private val scope: ActivityScope) : ViewHandler<MapHolderView>(scope) {

  override val type = "com.eclipsesource.maps.Map"

  override val properties: List<Property<MapHolderView, *>> = listOf(
      CameraProperty,
      MapTypeProperty,
      MyLocationProperty,
      PositionProperty,
      RegionProperty,
      ShowMyLocationButtonProperty,
      ShowMyLocationProperty(scope)
  )

  override fun create(id: String, properties: V8Object) = MapHolderView(scope)

  override fun listen(id: String, mapHolderView: MapHolderView, event: String, listen: Boolean) {
    super.listen(id, mapHolderView, event, listen)
    when (event) {
      "ready" -> if (listen) {
        mapHolderView.ensureOnMapReadyListener()
      } else {
        throw IllegalStateException("'ready' event listeners cannot be removed.")
      }
      "tap" -> if (listen) {
        attachOnMapClickListener(mapHolderView)
      } else {
        removeOnMapClickListener(mapHolderView)
      }
      "longpress" -> if (listen) {
        attachOnMapLongClickListener(mapHolderView)
      } else {
        removeOnMapLongClickListener(mapHolderView)
      }
    }
  }

  override fun call(mapHolderView: MapHolderView, method: String, properties: V8Object) = when (method) {
    "moveToRegion" -> moveCameraToRegion(mapHolderView, properties)
    "addMarker" -> addMarker(mapHolderView, properties)
    "removeMarker" -> removeMarker(properties)
    else -> null
  }

  private fun moveCameraToRegion(mapHolderView: MapHolderView, properties: V8Object) {
    val bounds = createBoundsFromRegion(properties)
    val padding = getPaddingFromOptions(properties)
    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
    if (getAnimateFromOptions(properties)) {
      mapHolderView.animateCamera(cameraUpdate)
    } else {
      mapHolderView.moveCamera(cameraUpdate)
    }
  }

  private fun getAnimateFromOptions(properties: V8Object): Boolean =
      properties.getObject("options")?.getBoolean("animate") ?: false

  private fun createBoundsFromRegion(properties: V8Object): LatLngBounds {
    val region = properties.getObject("region")
    val southWest = region.getArray("southWest").toList<Double>()
    val northEast = region.getArray("northEast").toList<Double>()
    return LatLngBounds(
        LatLng(southWest[0], southWest[1]),
        LatLng(northEast[0], northEast[1]))
  }

  private fun getPaddingFromOptions(properties: V8Object): Int =
      properties.getObject("options")?.getFloat("padding")?.toPixel(scope)?.roundToInt() ?: 0

  private fun addMarker(mapHolderView: MapHolderView, properties: V8Object) {
    properties.getString("marker")?.let {
      scope.objectRegistry.find<MapMarker>(it)?.let { mapMarker ->
        mapMarker.position?.let { markerPosition ->
          val markerOptions = MarkerOptions()
          markerOptions.position(markerPosition)
          with(mapMarker) {
            marker = mapHolderView.googleMap.addMarker(markerOptions)
            mapId = scope.remoteObject(mapHolderView)?.id
          }
        }
      }
    }
  }

  private fun removeMarker(properties: V8Object) {
    properties.getString("marker")?.let {
      scope.objectRegistry.find<MapMarker>(it)?.let { mapMarker ->
        with(mapMarker) {
          marker?.remove()
          marker = null
          mapId = null
        }
      }
    }
  }

  override fun destroy(mapHolderView: MapHolderView) {
    disableLocationIndicator(mapHolderView)
    destroyMarker(mapHolderView)
    super.destroy(mapHolderView)
  }

  private fun disableLocationIndicator(mapHolderView: MapHolderView) {
    if (ContextCompat.checkSelfPermission(scope.activity, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      mapHolderView.googleMap.isMyLocationEnabled = false
    }
  }

  private fun destroyMarker(mapHolderView: MapHolderView) {
    val mapId = scope.remoteObject(mapHolderView)?.id
    scope.objectRegistry.find<MapMarker>()
        .filter { it.mapId == mapId }
        .forEach { scope.bridge.destroy(it) }
  }

  private fun attachOnMapClickListener(mapHolderView: MapHolderView) {
    mapHolderView.googleMap.setOnMapClickListener(MapTapListener(scope, mapHolderView))
  }

  private fun removeOnMapClickListener(mapHolderView: MapHolderView) {
    mapHolderView.googleMap.setOnMapClickListener(null)
  }

  private fun attachOnMapLongClickListener(mapHolderView: MapHolderView) {
    mapHolderView.googleMap.setOnMapLongClickListener(MapLongPressListener(scope, mapHolderView))
  }

  private fun removeOnMapLongClickListener(mapHolderView: MapHolderView) {
    mapHolderView.googleMap.setOnMapLongClickListener(null)
  }

}
