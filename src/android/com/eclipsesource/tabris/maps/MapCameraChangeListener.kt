package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.ActivityScope
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.model.CameraPosition

class MapCameraChangeListener(private val mapHolderView: MapHolderView, private val scope: ActivityScope)
  : OnCameraMoveStartedListener, OnCameraIdleListener {

  private var reason: Int = 0

  override fun onCameraMoveStarted(reason: Int) {
    this.reason = reason
  }

  override fun onCameraIdle() {
    mapHolderView.googleMap?.cameraPosition?.let {
      notifyChangeCameraEvent(it)
      if (reason == OnCameraMoveStartedListener.REASON_GESTURE || reason == OnCameraMoveStartedListener.REASON_API_ANIMATION) {
        notifyCameraMoveEvent(it)
      }
    }
  }

  private fun notifyChangeCameraEvent(cameraPosition: CameraPosition) {
    val arguments = mapOf(
        "position" to listOf(cameraPosition.target.latitude, cameraPosition.target.longitude)
    )
    scope.remoteObject(mapHolderView)?.notify("changecamera", arguments)
  }

  private fun notifyCameraMoveEvent(cameraPosition: CameraPosition) {
    val arguments = mapOf(
        "position" to listOf(cameraPosition.target.latitude, cameraPosition.target.longitude)
    )
    scope.remoteObject(mapHolderView)?.notify("cameraMoved", "camera", arguments)
  }

}
