package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.Scope
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.CameraPosition

class MapCameraChangeListener(private val mapHolderView: MapHolderView, private val scope: Scope)
  : OnCameraMoveStartedListener, OnCameraIdleListener {

  private var reason: Int = 0

  override fun onCameraMoveStarted(reason: Int) {
    this.reason = reason
  }

  override fun onCameraIdle() {
    mapHolderView.googleMap.cameraPosition?.let {
      notifyChangeCameraEvent(it)
      when (reason) {
        REASON_GESTURE, REASON_API_ANIMATION -> notifyCameraMoveEvent(it)
      }
    }
  }

  private fun notifyChangeCameraEvent(cameraPosition: CameraPosition) {
    scope.remoteObject(mapHolderView)?.notify("changeCamera", mapOf(
        "position" to listOf(cameraPosition.target.latitude, cameraPosition.target.longitude)
    ))
  }

  private fun notifyCameraMoveEvent(cameraPosition: CameraPosition) {
    scope.remoteObject(mapHolderView)?.notify("cameraMoved", "camera", mapOf(
        "position" to listOf(cameraPosition.target.latitude, cameraPosition.target.longitude)
    ))
  }

}
