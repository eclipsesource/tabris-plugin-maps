var PLUGIN_ID = 'tabris-plugin-maps';

var sphericalUtil = cordova.require(PLUGIN_ID + '.sphericalutil');

var EVENT_TYPES = ['tap', 'longpress', 'ready', 'cameramove']

var Map = tabris.NativeObject.extend('com.eclipsesource.maps.Map', tabris.Widget);

Map.prototype._listen = function(name, listening) {
  if (EVENT_TYPES.indexOf(name) > -1) {
    this._nativeListen(name, listening);
  } else if (name === 'change:camera') {
    this._nativeListen('changecamera', listening);
  } else {
    tabris.Widget.prototype._listen.call(this, name, listening);
  }
};

Map.prototype._trigger = function(name, event) {
  if (name === 'changecamera') {
    this._triggerChangeEvent('camera', event);
  } else {
    tabris.Widget.prototype._trigger.call(this, name, event);
  }
};

Object.assign(Map.prototype, {

  _create: function() {
    tabris.NativeObject.prototype._create.apply(this, arguments);
    this._markers = [];
    return this;
  },

  moveToPosition: function(position, radius, options) {
    var southWest = sphericalUtil.computeOffset(position, radius * Math.sqrt(2.0), 225);
    var northEast = sphericalUtil.computeOffset(position, radius * Math.sqrt(2.0), 45);
    this.moveToRegion({northEast: northEast, southWest: southWest}, options);
  },

  moveToRegion: function(region, options) {
    this._nativeCall('moveToRegion', {
      region: region,
      options: options
    });
  },

  addMarker: function(marker) {
    if (marker._map) {
      throw new Error('Marker is already attached to a map');
    }
    marker._map = this;
    this._nativeCall('addMarker', {marker: marker.cid});
    this._markers.push(marker);
  },

  removeMarker: function(marker) {
    marker._map = null;
    var index = this._markers.indexOf(marker);
    if (index > -1) {
      this._nativeCall('removeMarker', {marker: marker.cid});
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

tabris.NativeObject.defineProperties(Map.prototype, {
  position: {type: 'array', nocache: true},
  region: {type: 'any', nocache: true},
  camera: {
    type: 'any', nocache: true,
    access: {
      set: function(name, camera) {
        this._nativeSet(name, camera);
      }
    }
  },
  showMyLocation: {type: 'boolean', default: false},
  showMyLocationButton: {type: 'boolean', default: false},
  myLocation: {type: 'array', nocache: true},
  mapType: {
    type: ['choice', ['none', 'hybrid', 'normal', 'satellite', 'terrain', 'satelliteflyover', 'hybridflyover']],
    nocache: true
  }
});

module.exports = Map;
