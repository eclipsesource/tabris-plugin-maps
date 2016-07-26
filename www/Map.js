var PLUGIN_ID = "com.eclipsesource.tabris.maps";

var sphericalUtil = cordova.require(PLUGIN_ID + ".sphericalutil");

tabris.registerWidget("_EclipseSourceMapsMap", {
  _create: function () {
    this._super("_create", arguments);
    this._markers = [];
    return this;
  },
  _type: "com.eclipsesource.maps.Map",
  _properties: {
    position: {type: "array", nocache: true},
    region: {type: "any", nocache:true},
    camera: {type: "any", nocache: true},
    showMyLocation: {type: "boolean", default: false},
    showMyLocationButton: {type: "boolean", default: false},
    myLocation: {type: "array", nocache: true},
    mapType: {type: ["choice", ["none", "hybrid", "normal", "satellite", "terrain", "satelliteflyover", "hybridflyover"]], nocache: true}
  },
  _events: {
    tap: {
      trigger: function(event) {this.trigger("tap", this, event.latLng);}
    },
    longpress: {
      trigger: function(event) {this.trigger("longpress", this, event.latLng);}
    },
    ready: {
      trigger: function() {this.trigger("ready", this);}
    },
    cameramove: {
      trigger: function(event) {this.trigger("cameramove", this, event);}
    },
    "change:camera": {
      name: "cameramoveprogrammatic",
      trigger: function(event) {this.trigger("change:camera", this, event);}
    }
  },
  moveToPosition: function(position, radius, options) {
    var southWest = sphericalUtil.computeOffset(position, radius * Math.sqrt(2.0), 225);
    var northEast = sphericalUtil.computeOffset(position, radius * Math.sqrt(2.0), 45);
    this.moveToRegion({northEast: northEast, southWest: southWest}, options);
  },
  moveToRegion: function(region, options) {
    this._nativeCall("moveToRegion", {
      region: region,
      options: options
    });
  },
  addMarker: function (marker) {
    if (marker._map) {
      throw new Error("Marker is already attached to a map");
    }
    marker._map = this;
    this._nativeCall("addMarker", {marker: marker.cid});
    this._markers.push(marker);
  },
  removeMarker: function (marker) {
    marker._map = null;
    var index = this._markers.indexOf(marker);
    if (index > -1) {
      this._nativeCall("removeMarker", {marker: marker.cid});
      this._markers.splice(index, 1);
    }
  },
  getMarkers: function() {
    return this._markers;
  },
  dispose: function() {
    this._markers = [];
    this._dispose();
  }

});

module.exports = tabris._EclipseSourceMapsMap;
