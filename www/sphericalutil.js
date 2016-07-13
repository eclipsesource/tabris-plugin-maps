/*
 * Partial port from https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/SphericalUtil.java
 */

var EARTH_RADIUS = 6371009;

module.exports = {

  // As described at http://williams.best.vwh.net/avform.htm#LL
  computeOffset: function(from, distance, heading) {
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

}

function toDegrees(angrad) {
  return angrad * 180 / Math.PI;
}

function toRadians(angdeg) {
  return angdeg / 180 * Math.PI;
}