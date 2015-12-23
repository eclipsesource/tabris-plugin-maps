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
The [tabris-maps-example](https://github.com/eclipsesource/tabris-maps-example) project provides a Tabris.js project that demonstrates different features of the tabris-maps custom widget.

## Requirements
### Android
To use this custom widget on Android devices you have to obtain an API Key from Google.
The documentation for the Google Maps API explains [how to aquire API keys](https://developers.google.com/maps/documentation/android/signup).

### iOS
iOS uses MapKit to privde maps services. Maps plugin has been written in Swift. For further information regarding MapKit you should check [Apple MapKit documentation](https://developer.apple.com/library/ios/documentation/MapKit/Reference/MapKit_Framework_Reference/index.html#//apple_ref/doc/uid/TP40008210). iOS implementation does not require any API key.

## API documentation
The tabris-maps custom widget provides this API:
* [Map](https://github.com/eclipsesource/tabris-maps/blob/master/doc/Map.md)
* [Marker](https://github.com/eclipsesource/tabris-maps/blob/master/doc/Marker.md)
* [LatLong](https://github.com/eclipsesource/tabris-maps/blob/master/doc/LatLng.md)

## Compatibility
Compatible with:
 * Android - [Tabris.js 1.1.0](https://github.com/eclipsesource/tabris-js/releases/tag/v1.1.0)
 * iOS - [Tabris.js 1.5.0](https://github.com/eclipsesource/tabris-js/releases/tag/v1.5.0)

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
 * Marker
   * latLong
   * title
   * color
 * Listener
   * onMapClick
   * onMapLongClick
   * onMarkerClick

#### iOS
 * MapKit
   * mapType
   * center
   * region
   * addMarker
   * removeMarker
 * Listener
   * tap
   * pan
   * ready
