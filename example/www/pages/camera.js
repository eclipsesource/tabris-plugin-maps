var MARGIN = 16;

exports.create = function() {
  return new tabris.Page({
    title: "Camera",
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
    this.on("change:camera", function() {
      updateCameraTextView("Camera changed programmatic");
    }).on("cameramove", function() {
      updateCameraTextView("Camera changed by user");
    });
    this.set("camera", {position: [48.8644458, 2.3589976]});
  }).appendTo(page);

  var paris = new tabris.Button({
    left: MARGIN, right: ["50%", 8], top: MARGIN,
    text: "Show Paris"
  }).on("select", function() {
    map.set("camera", {position: [48.8644458, 2.3589976]});
  }).appendTo(controls);

  new tabris.Button({
    left: ["50%", 8], right: MARGIN, top: MARGIN,
    text: "Show Sydney"
  }).on("select", function() {
    map.set("camera", {position: [-33.867, 151.206]});
  }).appendTo(controls);

  var cameraTextView = new tabris.TextView({
    left: MARGIN, right: MARGIN, top: [paris, MARGIN], bottom: MARGIN,
    lineSpacing: 1.2,
    markupEnabled: true
  }).appendTo(controls);

  function updateCameraTextView(source) {
    var text;
    if (source) {
      text = "<b>" + source + "</b><br/>";
    }
    var position = map.get("camera").position;
    text += "Position: [ " + truncate(position[0]) + ", " + truncate(position[1]) + " ]";
    cameraTextView.set("text", text);
  }

  function truncate(number) {
    var multiplier = Math.pow(10, 6),
      adjustedNum = number * multiplier,
      truncatedNum = Math[adjustedNum < 0 ? 'ceil' : 'floor'](adjustedNum);
    return truncatedNum / multiplier;
  }

}
