var MARGIN = 16;

exports.create = function() {
  return new tabris.Page({
    title: "Region",
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
    this.on("change:camera", updateRegionTextView)
      .on("cameramove", updateRegionTextView)
      .set("region", {southWest: [48.812059, 2.2495793], northEast: [48.910537, 2.4205543]});
  }).appendTo(page);

  var paris = new tabris.Button({
    left: MARGIN, right: ["50%", 8], top: MARGIN,
    text: "Show Paris"
  }).on("select", function() {
    map.set("region", {southWest: [48.812059, 2.2495793], northEast: [48.910537, 2.4205543]});
  }).appendTo(controls);

  new tabris.Button({
    left: ["50%", 8], right: MARGIN, top: MARGIN,
    text: "Show Sydney"
  }).on("select", function() {
    map.set("region", {southWest: [-33.912452, 151.1260233], northEast: [-33.785166, 151.2875383]});
  }).appendTo(controls);

  var regionTextView = new tabris.TextView({
    left: MARGIN, right: MARGIN, top: [paris, MARGIN], bottom: MARGIN,
    lineSpacing: 1.2,
    markupEnabled: true
  }).appendTo(controls);

  function updateRegionTextView(source) {
    var text = "<b>Region</b><br/>";
    var region = map.get("region");
    text += "southWest: [ " + truncate(region.southWest[0]) + ", " + truncate(region.southWest[1]) + " ]<br/>";
    text += "northEast: [ " + truncate(region.northEast[0]) + ", " + truncate(region.northEast[1]) + " ]";
    regionTextView.set("text", text);
  }

  function truncate(number) {
    var multiplier = Math.pow(10, 6),
      adjustedNum = number * multiplier,
      truncatedNum = Math[adjustedNum < 0 ? 'ceil' : 'floor'](adjustedNum);
    return truncatedNum / multiplier;
  }

}
