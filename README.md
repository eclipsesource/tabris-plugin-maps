# Maps plugin for Tabris.js

The `tabris-maps` plugin project provides a [Tabris.js](https://tabrisjs.com) API to show and interact with a map widget. The plugin currently suports Android (via [Google Maps](https://developers.google.com/maps/)) and iOS (via [Map Kit](https://developer.apple.com/maps/)).

![Tabris maps on iOS and Android](assets/screenshots/maps.png)

## Example

The following snippet shows how the `tabris-maps` plugin can be used in a Tabris.js app:

```javascript
var page =  new tabris.Page( {
  title: "Tabris Maps",
  topLevel: true
});

new tabris.ESMap({
  left: 0, right: 0, top: 0, bottom: 0
}).on("ready", function() {
  // show paris with a radius of 2000 meters
  this.moveToPosition([48.8644458, 2.3589976], 2000);
}).appendTo(page);

page.open();
```

The [tabris-maps-example](https://github.com/eclipsesource/tabris-maps-example) project provides a Tabris.js project that demonstrates different features of the `tabris-maps` custom widget.

## Integrating the plugin
Using the plugin follows the standard cordova plugin mechanism. The Tabris.js website provides detailed information on how to [integrate custom widgets](https://tabrisjs.com/documentation/latest/build#adding-plugins) in your Tabris.js based app.

### Add the plugin to your project

The plugin can be added like any other cordova plugin. Either via the `cordova plugin add` command or as an entry in the apps `config.xml` (recommended):

```xml
<plugin name=`com.eclipsesource.tabris.maps` spec=`https://github.com/eclipsesource/tabris-maps.git` />
```

#### Android

On Android a Google Maps API key has to be provided when adding the plugin to your Cordova project. The documentation for the Google Maps API explains how to [aquire an API key](https://developers.google.com/maps/documentation/android/signup).

The API key can be configured inside your apps `config.xml`:

```xml
<plugin name=`com.eclipsesource.tabris.maps` spec=`https://github.com/eclipsesource/tabris-maps.git`>
  <variable name=`API_KEY_FOR_ANDROID` value=`your-android-maps-api-key` />
</plugin>
```

Alternatively the API key can be added during the `cordova plugin add` command:

```bash
cordova plugin add <tabris-maps-plugin> --variable API_KEY_FOR_ANDROID=`your-android-maps-api-key`
```

## API documentation

The API consists of two objects: The `ESMap` and the `Marker` that can be created on an `ESMap`. Note that the properties and functions of the `ESMap` *can only be accessed _after_ the [`ready`](#ready) callback has fired*.

### `ESMap`

#### Properties

The following properties can be applied on top of the [common Tabris.js properties](https://tabrisjs.com/documentation/latest/api/Widget#properties). All properties can be read and written unless noted otherwise.

##### `position` : _number[]_

* The current center position of the map. The array has to be a two element tuple consisting of latitude and longitude.
* Example: `[48.8644458, 2.3589976]`
  
##### `region` : _object_

* Describes the currently visible rectangle of the map. A region object has the two properties `southEast` and `northWest` to span the visible region of the map. In case the map is tilted the region is represented by the smallest possible rectangle that could contain the trapezoid representing the visible region.
* Example: `{southWest: [48.8644458, 2.3589976], northEast: [48.8821597,2.3856527]}`.

##### `camera` : _object_

* Represents the orientation of the camera. The camera currently only holds the one property `position`. It is the same property as the top level property `position`.
* Example: `{position: [48.8644458, 2.3589976]}`

##### `showMyLocation` : _boolean_, default: `false`

  * Displays the current location of the user on the map. To activate the option the app has to have the necessary permissions to retrieve the users location. Be aware that the position has to be obtained by the device so the effect might no be visible immediately.

##### `showMyLocationButton` : _boolean_, default: `false`

* Displays a button to animate the camera to the current location of the user when the location is available. Can only be activated when `showMyLocation` is enabled. The property is only available on Android but a similar effect can be achieved with the [`moveTo`](#functions) functions.

##### `myLocation` : _number[]_

* Retrieves the current location of the user. The returned array is a `position` array consisting of latitude and longitude. Can only be used when `showMyLocation` is enabled. Since the location obtained by `showMyLocation` is not available immediately this call can return `null` or `undefined`. The property is _read only_.
* Example: `[48.8644458, 2.3589976]`

##### `mapType` : _string_, supported values: `none`, `hybrid`, `normal`, `satellite`, `terrain`, `satelliteflyover`, `hybridflyover`, default: `normal`

* Defines how the map is visualized. The most common properties are `normal` and `sattelite`. Not all variants are supported on each platform. `hybrid`, `normal`, `satellite` are supported on Android and iOS whereas `none`, and `terrain` is only available on Android and `satelliteflyover` and `hybridflyover` is only available on iOS.
  
#### Events

##### `ready`

* The `ready` event is fired when the map is fully initialized and ready for user interaction. The [properties](#properties) and [functions](#functions) of the widget can only be accessed after the `ready` event has fired.

###### Parameter:

* `widget` : _ESMap_
  * The `ESMap` widget that is ready

##### `tap`

* The `tap` event is fired when a tap on a map is detected. 

###### Parameter:

* `widget` : _ESMap_
  * The `ESMap` widget the tap occurred on
* `position` : _number[]_
  * A position array consisting of latitude and longitude: E.g. `[48.8644458, 2.3589976]`

##### `longpress`

* The `longpress` event is fired when a long press on a map is detected. 

###### Parameter:

* `widget` : _ESMap_
  * The `ESMap` widget the long press occurred on
* `position` : _number[]_
  * A position array consisting of latitude and longitude: E.g. `[48.8644458, 2.3589976]`

##### `cameramove`

* The `cameramove` event is fired when the user changed the position of the map by interacting with the map via touch. The callback is not guaranteed to fire continuously but rather when the map has reached a resting position.

###### Parameter:

* `widget` : _ESMap_
  * The `ESMap` widget whose the camera position has changed
* `camera` : _object_
  * The `camera` object consists of the same properties as the [`camera`](#camera) property and contains the new orientation of the camera

##### `change:camera`

* The `change:camera` event is fired when the position of the map has reached its final destination by a programmatic camera change. E.g. by setting the [`position`](#position) or using one of the [`moveTo`](#functions) functions.

###### Parameter:

* `widget` : _ESMap_
  * The `ESMap` widget whose the camera position has changed
* `camera` : _object_
  * The `camera` object consists of the same properties as the [`camera`](#camera) property and contains the new orientation of the camera
  
  
### Functions

#### `moveToPosition(position, radius, [options])`

The function `moveToPosition()` updates the cameras center to a new position and makes sure the given radius is fully contained in the visible region. This function resets the maps tilt.

Example:
```js
map.moveToPosition([48.8644458, 2.3589976], 500, {padding: 16, animate: true});
```
##### Parameter

* `position` : _number[]_
  * The target center [`position`](#position) of the map as an array of latitude and longitude. E.g. `[48.8644458, 2.3589976]`.
* `radius` : _number_
  * The radius in meters that should be visible on the map. Effectively this parameter controls the "zoom" level of the map.
* `options` : _object_
  * The optional options object contains additional information for the camera placement:
    * `padding` : _number_
      * A padding placed around the given `radius` in device independent pixel. This allows to give some inset to the region that we want to point the map to.
    * `animate` : _boolean_
      * Whether to place the camera directly at the given position or to move the camera in an animate fashion.

#### `moveToRegion(region, [options])`

The function `moveToPosition()` sets the cameras center to a new position and makes sure the given radius is fully contained in the visible region. This function resets the maps tilt.

Example:
```js
map.moveToRegion({southWest: [48.8644458, 2.3589976], northEast: [48.8821597,2.3856527]},
                 {padding: 16, animate: true});
```

##### Parameter

* `region` : _object_
  * The visible area to show on the map. The format of the region parameter is the same as for the [`region`](#region) property. It consists of two properties `southEast` and `northWest` to span the visible region of the map. E.g. `{southWest: [48.8644458, 2.3589976], northEast: [48.8821597,2.3856527]}`
* `options` : _object_
  * The optional options object contains additional information for the camera placement:
    * `padding` : _number_
      * A padding placed around the given `radius` in screen pixel. This allows to provide some inset to the region to be shown on the map.
    * `animate` : _boolean_
      * Whether to place the camera directly at the given position or to move the camera in an animated fashion.

#### `Marker createMarker(options)`

The function `createMarker()` creates a marker at the given position contained in the options object.

##### Parameter

* `options` : _Object_
  * Contains a property `latLng` to indicate the position of the marker.

### `Marker`

A `Marker` is used to indicate places and positions on the Map.

The `Marker` API is preliminary and subject to change.

#### Properties

##### `title` : _string_

* A title to associate with the marker.

#### Events

##### `tap`

* The `tap` event is fired when the user taps on a marker.

#### Functions

##### dispose()

* Remove a marker from the map

## Compatibility

Compatible with [Tabris.js 1.8.0](https://github.com/eclipsesource/tabris-js/releases/tag/v1.8.0)

x## Development of the widget

While not required by the consumer of the widget, this repository provides Android specific development artifacts. These artifacts allow to more easily consume the native source code when developing the native parts of the widget.

### Android

The project provides a gradle based build configuration, which also allows to import the project into Android Studio.

In order to reference the Tabris.js specific APIs, the environment variable `TABRIS_ANDROID_CORDOVA_PLATFORM` has to point to the Tabris.js Android Cordova platform root directory.

```bash
export TABRIS_ANDROID_CORDOVA_PLATFORM=/home/user/tabris-android-cordova
```
 The environment variable is consumed in the gradle projects [build.gradle](project/android/build.gradle) file.

## Copyright

Published under the terms of the [BSD 3-Clause License](LICENSE).
