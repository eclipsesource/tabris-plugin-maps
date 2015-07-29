# tabris-maps
Tabris Maps Plugin

This is a custom maps widget for use in the native UI of your [Tabris.js](https://tabrisjs.com) and [Tabris](http://developer.eclipsesource.com/tabris/) projects.

It provides access to features available through the [Google Maps API](https://developers.google.com/maps/) for Android and iOS.

## Use the Plugin

The Tabris.js project provides documentation on [how to use Cordova plugins](https://tabrisjs.com/documentation/latest/cordova) in your projects.

### Obtain an API Key

The documentation for the Google Maps API explains how to aquire API keys for:
 * [Android](https://developers.google.com/maps/documentation/android/signup)
 * [iOS](https://developers.google.com/maps/documentation/ios/intro)

### Add the Plugin to your Project
Add it to your Cordova project like this:
```bash
cordova plugin add ../tabris-maps --variable API_KEY_FOR_ANDROID="your-android-maps-api-key-here"
```

## Compatibility
Compatible with:
 * [Tabris.js 1.1.0](https://github.com/eclipsesource/tabris-js/releases/tag/v1.1.0)
 
### Supported Maps API

 * Lite Mode (create)
 * GoogleMap
   * mapType
   * moveCamera
   * latLong
   * zoom
   * myLocationEnabled
   * zoomControlsEnabled
   * rotateGesturesEnabled
   * zoomGesturesEnabled
   * compassEnabled
   * tiltGesturesEnabled
   * myLocationButtonEnabled
   * addMarker()
   * removeMarker()
 * Listener
   * onMapClick
   * onMapLongClick
   * onMarkerClick
