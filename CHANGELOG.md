Change Log
==========

## Version 4.0.1

* Fix marker image scaling on Android.

## Version 4.0.0

* Update tabris-plugin-maps to Tabris.js 2.2 custom widget API. This version is not compatible with Tabris.js 2.0 and 2.1 anymore.
* Support customizing the marker appearance by applying a custom image.

## Version 3.0.0

* Migrate tabris-plugin-maps to Tabris.js 2 API (breaks compatibility with Tabris.js 1.x)
  * fire events with a single callback argument (see [Tabris.js events documentation](https://tabrisjs.com/documentation/2.0/widget-basics.html#events))
  * rename "change:camera" event to "cameraChanged"
  * rename "cameramove" event to "cameraMoved"
* pressing the "show my location" button now also triggers "cameraMoved"

## Version 2.0.1

_2016-08-30_

* Restructure project to be more friendly on npmjs.com

## Version 2.0.0

_2016-08-30_

  *  Complete rewrite of public js API
      * New namespace `esmaps` for `Map` and `Marker`
      * New properties `position`, `camera` and `region`
      * New ways of positioning the camera via `moveToPosition` and `moveToRegion`
      * New Marker API when creating and adding markers
      * Allow to get users location from `Map`
      * Allow to show and move to users location on `Map`
  * Ensure same behavior on Android and iOS for all documented features
