# Tabris.js Maps Example

## Prerequisites
To run the example it is recommended to use the [Tabris.js CLI](https://www.npmjs.com/package/tabris-cli).

### Android
Running the example on Android requires a valid Google Maps API key. The documentation for the Google Maps API explains how to [acquire an API key](https://developers.google.com/maps/documentation/android/signup).

## Running the Example

Using the Tabris.js CLI the example can be started by running:

```sh
$ tabris run <platform>
```

When running this example on Android, the Google Maps API key can also be provided by setting an environment variable. The Tabris.js CLI will replace the placeholder in the config.xml with the given API key:

```sh
$ ANDROID_API_KEY=<google-maps-api-key> tabris run android
```
