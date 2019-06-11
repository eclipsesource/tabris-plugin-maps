package com.eclipsesource.tabris.maps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.android.V8ArrayProperty
import com.eclipsesource.tabris.android.internal.toolkit.Image
import com.eclipsesource.tabris.android.internal.toolkit.ImageProviderListenerAdapter
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MarkerImageProperty(private val scope: ActivityScope) : V8ArrayProperty<MapMarker>("image", {
  if (it == null) {
    icon = null
  } else {
    val imageList = Image.fromV8Array(it).toList()
    scope.imageProvider.load(imageList, object : ImageProviderListenerAdapter() {
      override fun onImageLoaded(drawable: Drawable) {
        icon = BitmapDescriptorFactory.fromBitmap(drawableToBitmap(drawable))
      }
    })
  }
})

private fun drawableToBitmap(drawable: Drawable): Bitmap {
  val canvas = Canvas()
  val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
  canvas.setBitmap(bitmap)
  with(drawable) {
    setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    draw(canvas)
  }
  return bitmap
}
