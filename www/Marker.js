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
  position: {type: 'array', nocache: true},
  image: {type: 'image', nocache: true, default: null}
});

module.exports = Marker;
