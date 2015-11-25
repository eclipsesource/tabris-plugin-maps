#!/usr/bin/env node

var fs = require("fs"),
    path = require("path");

var rootdir = process.argv[2];
if (rootdir) {

  module.exports = function(context) {

    var cordova_util = context.requireCordovaModule("cordova-lib/src/cordova/util"),
        ConfigParser = context.requireCordovaModule("cordova-lib/src/configparser/ConfigParser"),
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
      var platform_path = path.join(projectRoot, "platforms", platform);
      var parser = new platforms[platform].parser(platform_path);
      if (typeof parser.cordovaproj !== "undefined") {
        return path.join(parser.cordovaproj, relPath);
      }
      return path.join(parser.path, relPath);
    };

    var getProjectName = function() {
      var platform_path = path.join(projectRoot, "platforms", "ios");
      var parser = new platforms["ios"].parser(platform_path);
      return parser.cordovaproj.replace(/^.*[\\\/]/, '');
    }

    var replace = function(path, to_replace, replace_with) {
      var data = fs.readFileSync(path, "utf8");
      var result = data.replace(to_replace, replace_with);
      fs.writeFileSync(path, result, "utf8");
    };

    var updateIOSAppDelegate = function() {
      var appDelegate = getProjectFile("ios", "Classes/AppDelegate.m");
      var plugins = cfg.getPlatformPreference("tabris-plugins", "ios")
      var projectName = getProjectName();
      var importReplace = "#import \"AppDelegate.h\"";
      var registerReplace = "self.client.delegate = self;"
      replace(appDelegate, importReplace, importReplace + "\n#import \"" + projectName + "-Swift.h\"");
      replace(appDelegate, registerReplace, "[self.client addRemoteObject:[ESMap class]];" + "\n\t" + "[self.client addRemoteObject:[ESMarker class]];" + "\n\t" + registerReplace);
    };

    updateIOSAppDelegate();
  };

}