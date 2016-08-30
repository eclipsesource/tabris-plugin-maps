var MARGIN = 16;

exports.create = function() {
  return new tabris.Page({
    title: "Position",
    topLevel: true
  }).once("appear", createExample);
};

function createExample(page) {

  var controls = new tabris.Composite({
    left: 0, right: 0, bottom: 0,
    background: "white",
    elevation: 6
  }).appendTo(page);

  var map = new esmaps.Map({
    left: 0, right: 0, top: 0, bottom: controls
  }).on("ready", function() {
    this.set("position", [48.8644458, 2.3589976]);
    updatePositionTextView();
  }).appendTo(page);

  var paris = new tabris.Button({
    left: MARGIN, right: ["50%", 8], top: MARGIN,
    text: "Show Paris"
  }).on("select", function() {
    map.set("position", [48.8644458, 2.3589976]);
  }).appendTo(controls);

  new tabris.Button({
    left: ["50%", 8], right: MARGIN, top: MARGIN,
    text: "Show Sydney"
  }).on("select", function() {
    map.set("position", [-33.867, 151.206]);
  }).appendTo(controls);

  var updatePositionButton = new tabris.Button({
    text: "Get position",
    left: MARGIN, top: [paris, MARGIN], bottom: MARGIN
  }).on("select", updatePositionTextView)
    .appendTo(controls);

  var positionTextView = new tabris.TextView({
    left: [updatePositionButton, MARGIN], right: MARGIN, top: [paris, MARGIN], bottom: MARGIN,
    markupEnabled: true
  }).appendTo(controls);

  function updatePositionTextView() {
    var position = map.get("position");
    positionTextView.set("text", "Latitude: <b>" + truncate(position[0]) + "</b><br/>"
      + "Longitude: <b>" + truncate(position[1]) + "</b>");
  }

  function truncate(number) {
    var multiplier = Math.pow(10, 6),
      adjustedNum = number * multiplier,
      truncatedNum = Math[adjustedNum < 0 ? 'ceil' : 'floor'](adjustedNum);
    return truncatedNum / multiplier;
  }

}
