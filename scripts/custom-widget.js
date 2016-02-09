#!/usr/bin/env node

var fs = require("fs"),
    path = require("path");

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

    var replace = function(path, to_replace, replace_with) {
      var data = fs.readFileSync(path, "utf8");
      var result = data.replace(to_replace, replace_with);
      fs.writeFileSync(path, result, "utf8");
    };

    var updateIOSAppDelegate = function() {
      var appDelegate = getProjectFile("ios", "Classes/AppDelegate.m");
      var projectName = cfg.name();
      var importReplace = "#import \"AppDelegate.h\"";
      var registerReplace = "self.client.delegate = self;"
      replace(appDelegate, importReplace, importReplace + "\n#import \"" + projectName + "-Swift.h\"");
      replace(appDelegate, registerReplace, "[self.client addRemoteObject:[ESMap class]];" + "\n\t" + "[self.client addRemoteObject:[ESMarker class]];" + "\n\t" + registerReplace);
    };

    updateIOSAppDelegate();
  };

}