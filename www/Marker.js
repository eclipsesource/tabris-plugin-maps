var Marker = tabris.NativeObject.extend('com.eclipsesource.maps.Marker', tabris.Widget);

Marker.prototype._listen = function(name, listening) {
  if (name === 'tap') {
    this._nativeListen(name, listening);
  } else {
    tabris.Widget.prototype._listen.call(this, name, listening);
  }
};

Marker.prototype.dispose = function() {
  if (this._map) {
    this._map.removeMarker(this);
  }
  this._dispose();
};

tabris.NativeObject.defineProperties(Marker.prototype, {
  position: {
    type: {
      convert(value) {
        if (!Array.isArray(value)) {
          throw new Error(value + ' is not an array');
        }
        return Object.freeze(value);
      }
    },
    nocache: true
  },
  image: {type: 'ImageValue', default: null}
});

module.exports = Marker;
