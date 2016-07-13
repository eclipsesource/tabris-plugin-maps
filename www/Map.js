var PLUGIN_ID = "com.eclipsesource.tabris.maps";

var sphericalUtil = cordova.require(PLUGIN_ID + ".sphericalutil");

tabris.registerWidget("ESMap", {
  _type: "com.eclipsesource.maps.Map",
  _supportsChildren: true,
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
  createMarker: function (mapOptions) {
    return tabris.create("_ESMarker", mapOptions).appendTo(this);
  },

});
