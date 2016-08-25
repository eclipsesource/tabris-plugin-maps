var positionPage = require("./pages/position");
var cameraPage = require("./pages/camera");
var regionPage = require("./pages/region");
var markerPage = require("./pages/marker");

var drawer = tabris.create("Drawer");
tabris.create("PageSelector").appendTo(drawer);

positionPage.create().open();
cameraPage.create();
regionPage.create();
markerPage.create();
