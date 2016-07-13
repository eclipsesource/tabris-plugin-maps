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
  animateCameraToPosition: function(position, radius, padding) {
    var southwest = computeOffset(position, radius * Math.sqrt(2.0), 225);
    var northeast = computeOffset(position, radius * Math.sqrt(2.0), 45);
    this.animateCameraToBoundingBox(northeast, southwest, padding);
  },
  animateCameraToBoundingBox: function(northEast, southWest, padding) {
    this._nativeCall("animateCameraToBoundingBox", {
      northEast: northEast,
      southWest: southWest,
      padding: padding
    });
  },
  animateCameraToPointGroup: function(latLngPointGroup) {
    // TODO check types
    this._nativeCall("animateCameraToPointGroup", {
      "latLngPointGroup": latLngPointGroup
    });
  },
  createMarker: function (mapOptions) {
    return tabris.create("_ESMarker", mapOptions).appendTo(this);
  },


});

var EARTH_RADIUS = 6371009;

// As described at http://williams.best.vwh.net/avform.htm#LL
function computeOffset(from, distance, heading) {
  var distanceOnEarth = distance / EARTH_RADIUS;
  var headingInRadians = toRadians(heading);
  var fromLat = toRadians(from[0]);
  var fromLng = toRadians(from[1]);
  var cosDistance = Math.cos(distanceOnEarth);
  var sinDistance = Math.sin(distanceOnEarth);
  var sinFromLat = Math.sin(fromLat);
  var cosFromLat = Math.cos(fromLat);
  var sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(headingInRadians);
  var dLng = Math.atan2(
    sinDistance * cosFromLat * Math.sin(headingInRadians),
    cosDistance - sinFromLat * sinLat);
  return [toDegrees(Math.asin(sinLat)), toDegrees(fromLng + dLng)];
}

function toDegrees(angrad) {
  return angrad * 180 / Math.PI;
}

function toRadians(angdeg) {
  return angdeg / 180 * Math.PI;
}