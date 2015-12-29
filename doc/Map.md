# Map
A widget to display a map. Based on Google Maps.
Includes [Widget API](https://tabrisjs.com/documentation/latest/api/Widget)

## Properties
### mapType
Type: *string*, supported values: `none`, `hybrid`, `normal`, `satellite`, `terrain`, default: `normal`

Allows setting the type of the map. It cannot be changed after widget creation.

### center
Type: *[LatLng](LatLng.md)*

Allows setting the current camera position.

### zoom
Type: *number*

The zoom level of the map, ranging from 2.0 to 21.0.

### liteMode
Type: *boolean*

When `liteMode` is switched to *true*, a non-interactive bitmap version of the map is shown. Using maps with `liteMode` enabled can improve performance in the case that many maps are displayed, e.g. in a collection view.

### maxZoomLevel
Type: *float*

The maximum zoom level for the current camera position and map type. Read only.

### minZoomLevel
Type: *float*

Returns the minimum zoom level for the current map type and device used. Read only.

## Methods

### addMarker(latLng)

**Parameters:**

- latLng: *[LatLng](LatLng.md)*

Add a marker on the map at the given coordinates.

### removeMarker(marker)

**Parameters:**

- marker: *[Marker](Marker.md)*

Remove the referenced marker from the map.

## Events

### "ready" (widget)

**Parameters:**

- widget: *Map*

The map is ready to be used. Map event listeners can only be attached after the 'ready' event has been fired.

### "tap" (widget, latLng)

**Parameters:**

- widget: *Map*
- latLng: *[LatLng](LatLng.md)* The location on the map where the touch occurred.

Fired once when a finger briefly touches the map.

### "longpress" (widget, latLng)

**Parameters:**

- widget: *Map*
- latLng: *[LatLng](LatLng.md)* The location on the map where the touch occurred.

Fired after pressing the map for a specific amount of time (about a second), and again on lifting the finger.

### "pan" (widget, latLng)

**Parameters:**

- widget: *Map*
- latLng: *[LatLng](LatLng.md)*

Fired continuously while the map is dragged with a finger.
