tabris.registerWidget("_EclipseSourceMapsMarker", {
  _type: "com.eclipsesource.maps.Marker",
  _properties: {
    position: {type: "array", nocache: true}
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
