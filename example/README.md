# Tabris.js Maps Example

## Prerequisites

Download and extract a current [Tabris.js platform](https://tabrisjs.com/download).

### Android
Running the example on Android requires a valid Google Maps API key. The documentation for the Google Maps API explains how to [acquire an API key](https://developers.google.com/maps/documentation/android/signup). It can be configured in the `config.xml`:

```
<plugin name="tabris-plugin-maps" spec="3.0.0">
  <variable name="ANDROID_API_KEY" value="Your Google Maps API key" />
</plugin>
```

## Running the Example

```sh
$ cd www
$ npm install
$ cordova platform add PLATFORM_PATH
$ cordova run
```
