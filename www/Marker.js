tabris.registerWidget("_ESMarker", {
  _type: "com.eclipsesource.maps.Marker",
  _properties: {
    latLng: {type: "array", nocache: true},
    title: {type: "string"},
    color: {type: "string"}
  },
  _events: {tap: true}
});
