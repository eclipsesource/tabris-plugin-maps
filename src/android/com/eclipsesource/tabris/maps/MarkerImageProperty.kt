package com.eclipsesource.tabris.maps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.android.AnyProperty
import com.eclipsesource.tabris.android.internal.image.Image
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MarkerImageProperty(private val scope: ActivityScope) : AnyProperty<MapMarker>("image", {
  if (it == null) {
    icon = null
  } else {
    scope.imageLoader.load(Image(scope, it)) { drawable: Drawable? ->
      icon = when {
        drawable is BitmapDrawable -> BitmapDescriptorFactory.fromBitmap(drawable.bitmap)
        drawable != null -> BitmapDescriptorFactory.fromBitmap(drawable.toBitmap())
        else -> null
      }
    }
  }
})

private fun Drawable.toBitmap(): Bitmap {
  val canvas = Canvas()
  val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
  canvas.setBitmap(bitmap)
  setBounds(0, 0, intrinsicWidth, intrinsicHeight)
  draw(canvas)
  return bitmap
}
