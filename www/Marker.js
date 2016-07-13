tabris.registerWidget("_ESMarker", {
  _type: "tabris.maps.marker",
  _properties: {
    latLng: {type: "array", nocache: true},
    title: {type: "string"},
    color: {type: "string"}
  },
  _events: {tap: true}
});
