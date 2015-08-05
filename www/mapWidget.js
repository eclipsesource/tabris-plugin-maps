tabris.registerWidget("ESMap", {
  _properties: {
    liteMode: {type: "boolean", default: false},
    center: "array",
    zoom: "float",
    mapType: {type: ["choice", ["none", "hybrid", "normal", "satellite", "terrain"]]}
  },
  _events: {
    maptap: {
      trigger: function(event) {this.trigger("maptap", this, event.latLng);}
    },
    maplongpress: {
      trigger: function(event) {this.trigger("maplongpress", this, event.latLng);}
    },
    mapready: {
      trigger: function() {this.trigger("mapready", this);}
    },
    mappan: {
      trigger: function(event) {this.trigger("mappan", this, event.latLng);}
    }
  },
  animateCamera: function(centerLatLng, zoom) {
    // TODO check types
    this._nativeCall("animateCamera", {
      "centerLatLng": centerLatLng,
      "zoom": zoom
    });
  },
  animateCameraToBounds: function(northWestLatLng, southEastLatLng) {
    // TODO check types
    this._nativeCall("animateCameraToBounds", {
      "northWestLatLng": northWestLatLng,
      "southEastLatLng": southEastLatLng
    });
  },
  animateCameraToPointGroup: function(latLngPointGroup) {
    // TODO check types
    this._nativeCall("animateCameraToPointGroup", {
      "latLngPointGroup": latLngPointGroup
    });
  },
  getMinZoomLevel: function() {
    return this._nativeCall("getMinZoomLevel", {});
  },
  getMaxZoomLevel: function() {
    return this._nativeCall("getMaxZoomLevel", {});
  }
});
