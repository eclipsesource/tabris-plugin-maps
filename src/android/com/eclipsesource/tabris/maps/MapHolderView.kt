package com.eclipsesource.tabris.maps

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import com.eclipsesource.tabris.android.ActivityScope
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

@SuppressLint("ViewConstructor")
class MapHolderView(private val scope: ActivityScope) : FrameLayout(scope.activity), OnMapReadyCallback {

  private var mapFragment: SupportMapFragment? = null
  private var _googleMap: GoogleMap? = null
  private var mapReadyListenerRequired = false

  var googleMap: GoogleMap
    set(value) {
      _googleMap = value
    }
    get() = requireNotNull(_googleMap) {
      "Google Map is not yet ready. Only call get on map when it is ready."
    }

  init {
    id = View.generateViewId()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    createMap()
    if (mapReadyListenerRequired) {
      attachOnMapReadyListener()
    }
  }

  private fun createMap() {
    mapFragment = SupportMapFragment().apply {
      scope.activity
          .supportFragmentManager
          .beginTransaction()
          .add(id, this)
          .commit()
    }
  }

  fun ensureOnMapReadyListener() {
    mapReadyListenerRequired = true
    attachOnMapReadyListener()
  }

  private fun attachOnMapReadyListener() {
    mapFragment?.let {
      it.getMapAsync(this)
      mapReadyListenerRequired = false
    }
  }

  override fun onMapReady(googleMap: GoogleMap) {
    this.googleMap = googleMap
    with(googleMap) {
      setOnMarkerClickListener(MarkerTapListener(this@MapHolderView, scope))
      val cameraChangeListener = MapCameraChangeListener(this@MapHolderView, scope)
      setOnCameraMoveStartedListener(cameraChangeListener)
      setOnCameraIdleListener(cameraChangeListener)
    }
    scope.remoteObject(this)?.notify("ready")
  }

  fun moveCamera(cameraUpdate: CameraUpdate) {
    googleMap.moveCamera(cameraUpdate)
  }

  fun animateCamera(cameraUpdate: CameraUpdate) {
    googleMap.animateCamera(cameraUpdate)
  }

}
