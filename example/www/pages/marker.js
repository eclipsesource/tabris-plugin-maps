var MARGIN = 16;

exports.create = function() {
  return new tabris.Page({
    title: "Marker",
    topLevel: true
  }).once("appear", createExample);
};

function createExample(page) {

  var controls = new tabris.Composite({
    left: 0, right: 0, bottom: 0,
    background: "white",
    elevation: 6
  }).appendTo(page);

  new esmaps.Map({
    left: 0, right: 0, top: 0, bottom: controls
  }).on("ready", function() {
    this.set("region", {southWest: [48.812059, 2.2495793], northEast: [48.910537, 2.4205543]});
    createMarker(this, [48.8560453, 2.3415578], "Notre-dame");
    createMarker(this, [48.8566333, 2.2975081], "Eifel tower");
  }).appendTo(page);

  var markerTextView = new tabris.TextView({
    layoutData: {left: MARGIN, right: MARGIN, top: MARGIN, bottom: MARGIN},
    markupEnabled: true,
    text: "Tap on marker..."
  }).appendTo(controls);

  function createMarker(map, position, title) {
    var marker = new esmaps.Marker({position: position});
    marker.on("tap", function() {
      markerTextView.set("text", "Tapped on <b>" + title + "</b>");
    });
    map.addMarker(marker);
  }

}
