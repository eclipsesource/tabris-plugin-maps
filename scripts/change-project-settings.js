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

    var getProjectName = function() {
      return cfg.name();
    }

    var replace = function(path, to_replace, replace_with) {
      var data = fs.readFileSync(path, "utf8");
      var regex = new RegExp(to_replace, 'g');
      var result = data.replace(regex, replace_with);
      fs.writeFileSync(path, result, "utf8");
    };

    var updateProjectSettings = function() {
      var projectName = getProjectName();
      var file = getProjectFile("ios", "") + ".xcodeproj/project.pbxproj";
      var definesModuleReplace = "DEFINES_MODULE = NO";
      var clangReplace = "CLANG_ENABLE_MODULES = NO";
      var containsSwiftReplace = "EMBEDDED_CONTENT_CONTAINS_SWIFT = NO";
      var runpathReplace = "LD_RUNPATH_SEARCH_PATHS = \"\"";
      replace(file, definesModuleReplace, "DEFINES_MODULE = YES");
      replace(file, clangReplace, "CLANG_ENABLE_MODULES = YES");
      replace(file, containsSwiftReplace, "EMBEDDED_CONTENT_CONTAINS_SWIFT = YES");
      replace(file, runpathReplace, "LD_RUNPATH_SEARCH_PATHS = \"@executable_path/Frameworks\"");
    };

    updateProjectSettings();
  }
}