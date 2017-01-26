var Marker = tabris.Widget.extend({

  _type: 'com.eclipsesource.maps.Marker',

  _name: 'Marker',

  _properties: {
    position: {type: 'array', nocache: true}
  },

  _events: {
    tap: true
  }

});

Marker.prototype.dispose = function() {
  if (this._map) {
    this._map.removeMarker(this);
  }
  this._dispose();
}

module.exports = Marker;
