#!/usr/bin/env node

var fs = require("fs"),
    path = require("path");
    util = require('util'),
    exec = require('child_process').exec

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
      var regex = new RegExp(to_replace, 'g');
      var result = data.replace(regex, replace_with);
      fs.writeFileSync(path, result, "utf8");
    };

    var walkSync = function(dir, filelist) {
      var fs = fs || require('fs'),
          files = fs.readdirSync(dir);
      filelist = filelist || [];
      files.forEach(function(file) {
        if (fs.statSync(dir + '/' + file).isDirectory()) {
          filelist = walkSync(dir + '/' + file, filelist);
        }
        else {
          filelist.push(dir + '/' + file);
        }
      });
      return filelist;
    };

    var checkForBridgingHeader = function() {
      var contents = walkSync(getProjectFile("ios", ""), []);
      contents.forEach(function(fileName) {
        if (fileName.indexOf("-Bridging-Header.h") > -1) {
          return true;
        };
      });
      return false;
    };

    var getBridgingHeaderPath = function() {
      var contents = walkSync(getProjectFile("ios", ""), []);
      contents.forEach(function(fileName) {
        if (fileName.indexOf("-Bridging-Header.h") > -1) {
          return fileName;
        };
      });
    };

    var updateProjectSettings = function() {
      var projectName = getProjectName();
      var headerName = projectName + "-Bridging-Header.h";
      var file = getProjectFile("ios", "") + ".xcodeproj/project.pbxproj";
      var headerReplace = "__PROJECT_NAME__-Bridging-Header.h";
      var bridgeReplace = "SWIFT_OBJC_BRIDGING_HEADER = \"\";"
      replace(file, headerReplace, headerName);
      replace(file, bridgeReplace, "SWIFT_OBJC_BRIDGING_HEADER = \""+ projectName + "/Plugins/com.eclipsesource.tabris.maps/" + headerName + "\";");
    };

    var setupBridgingHeader = function() {
      var headerName = getProjectName() + "-Bridging-Header.h";
      var headerReplace = "__PROJECT_NAME__-Bridging-Header.h";
      var folder = getProjectFile("ios", "Plugins/com.eclipsesource.tabris.maps/");
      fs.rename(folder + headerReplace, folder + headerName);
    }

    var updateExistingBridgingHeader = function() {
      var headerPath = getBridgingHeaderPath();
      fs.appendFile(headerPath, "\n\n#import \"AppDelegate.h\"\n#import <Tabris/BasicObject.h>\n#import <Tabris/BasicWidget.h>\n#import <Tabris/ClientLayout.h>\n#import <Tabris/DelegatingView.h>\n#import <Tabris/JSBinding.h>\n#import <Tabris/Message.h>\n#import <Tabris/Notifications.h>\n#import <Tabris/NotificationsProtocols.h>\n#import <Tabris/RemoteObject.h>\n#import <Tabris/TabrisClient.h>\n#import <Tabris/TabrisClientDelegates.h>\n#import <Tabris/TabrisHTTPClient.h>\n");
    }

    if (checkForBridgingHeader()) {
      updateExistingBridgingHeader();
    } else {
      setupBridgingHeader();
      updateProjectSettings();
    }
  }
}