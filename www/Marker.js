tabris.registerWidget("_EclipseSourceMapsMarker", {
  _type: "com.eclipsesource.maps.Marker",
  _properties: {
    latLng: {type: "array", nocache: true},
    title: {type: "string"},
    color: {type: "string"}
  },
  _events: {
    tap: true
  },
  dispose: function() {
    if (this._map) {
      this._map.removeMarker(this); 
    }
    this._dispose();
  }
});

module.exports = tabris._EclipseSourceMapsMarker;
