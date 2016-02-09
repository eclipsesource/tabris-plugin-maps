#!/usr/bin/env node

var fs = require("fs"),
    path = require("path");
    util = require('util'),
    exec = require('child_process').exec

var rootdir = process.argv[2];
if (rootdir) {

  module.exports = function(context) {

    var cordova_util = context.requireCordovaModule("cordova-lib/src/cordova/util"),
        ConfigParser = context.requireCordovaModule("cordova-common").ConfigParser,
        projectRoot = cordova_util.isCordova(),
        xml = cordova_util.projectConfig(projectRoot),
        cfg = new ConfigParser(xml);

    // Cordova moved the platforms stuff; try both locations so we'll work for new and old file layouts.
    var platforms;
    try {
      platforms = context.requireCordovaModule('cordova-lib/src/cordova/platforms');
    } catch(e) {
      platforms = context.requireCordovaModule('cordova-lib/src/platforms/platforms');
    }

    var getProjectFile = function(platform, relPath) {
      return path.join(projectRoot, "platforms", platform, cfg.name(), relPath);
    };

    var getProjectName = function() {
      return cfg.name();
    }

    var updateExistingBridgingHeader = function() {
      var headerPath = getProjectFile("ios", "Bridging-Header.h");
      fs.appendFile(headerPath, "\n\n#import \"ESMaps-Bridging-Header.h\"");
    }

    updateExistingBridgingHeader();
  }
}