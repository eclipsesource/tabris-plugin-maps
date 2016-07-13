var PLUGIN_ID = "com.eclipsesource.tabris.maps";

var sphericalUtil = cordova.require(PLUGIN_ID + ".sphericalutil");

tabris.registerWidget("ESMap", {
  _type: "tabris.Map",
  _supportsChildren: true,
  _properties: {
    liteMode: {type: "boolean", default: false},
    position: {type: "array", nocache: true},
    camera: {type: "any", nocache: true},
    zoom: {type: "any", nocache: true},
    minZoomLevel: {type: "any", nocache: true},
    maxZoomLevel: {type: "any", nocache: true},
    region: {type: "array", nocache: true},
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
  moveCameraToPosition: function(position, radius, options) {
    var southwest = sphericalUtil.computeOffset(position, radius * Math.sqrt(2.0), 225);
    var northeast = sphericalUtil.computeOffset(position, radius * Math.sqrt(2.0), 45);
    this.moveCameraToBoundingBox(northeast, southwest, options);
  },
  moveCameraToBoundingBox: function(northEast, southWest, options) {
    this._nativeCall("moveCameraToBoundingBox", {
      northEast: northEast,
      southWest: southWest,
      options: options
    });
  },
  createMarker: function (mapOptions) {
    return tabris.create("_ESMarker", mapOptions).appendTo(this);
  },


});
