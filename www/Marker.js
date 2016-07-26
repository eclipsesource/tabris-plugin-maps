tabris.registerWidget("_EclipseSourceMapsMarker", {
  _type: "com.eclipsesource.maps.Marker",
  _properties: {
    latLng: {type: "array", nocache: true},
    title: {type: "string"},
    color: {type: "string"}
  },
  _events: {tap: true}
});

module.exports = tabris._EclipseSourceMapsMarker;
