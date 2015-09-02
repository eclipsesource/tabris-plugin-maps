# tabris-maps
tabris-maps is a custom widget for [Tabris.js](https://tabrisjs.com).
tabris-maps comes in form of a Cordova plugin.

This maps widget for use in the native UI (iOS and Android) of your Tabris.js projects leverages the respective native map implementations.

It provides access to features available through the [Map Kit framework](https://developer.apple.com/maps/) on iOS and the [Google Maps API](https://developers.google.com/maps/) on Android.



## Use the Plugin
The Tabris.js website provides documentation on [how to use custom widgets](https://tabrisjs.com/documentation/1.2/custom-widgets) in your Tabris.js projects. It also explains [how to use Cordova plugins](https://tabrisjs.com/documentation/latest/cordova) in your projects.

### Add the Plugin to your Project
Add it to your Cordova project like this:
```bash
cordova plugin add ../tabris-maps --variable API_KEY_FOR_ANDROID="your-android-maps-api-key-here"
```

### Example Project
We prepare an [example Tabris.js project](https://github.com/eclipsesource/tabris-maps-example) that uses tabris-maps as a custom widget.

## Requirements
### Android
To use this custom widget on Android devices you have to obtain an API Key from Google.
The documentation for the Google Maps API explains [how to aquire API keys](https://developers.google.com/maps/documentation/android/signup).

### iOS
tbd

## Compatibility
Compatible with:
 * [Tabris.js 1.1.0](https://github.com/eclipsesource/tabris-js/releases/tag/v1.1.0)

### Supported API
This sections gives a brief overview of the subsets of the native maps APIs supported by this custom widget.
#### Android
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

#### iOS
 tbd
