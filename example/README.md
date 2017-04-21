# Tabris.js Maps Example

## Prerequisites

To run the example it is recommended to use the [tabris-cli](https://www.npmjs.com/package/tabris-cli). The tabris-cli requires a local [Tabris.js platform](https://tabrisjs.com/download) which should be referenced by a corresponding environment variable.

### Android
Running the example on Android requires a valid Google Maps API key. The documentation for the Google Maps API explains how to [acquire an API key](https://developers.google.com/maps/documentation/android/signup). It can be configured in the `config.xml`:

```
<plugin name="tabris-plugin-maps" spec="<version-or-path>">
  <variable name="ANDROID_API_KEY" value="<google-maps-api-key>" />
</plugin>
```

## Running the Example

Using the tabris-cli the example can be started by running:

```sh
$ tabris run <platform>
```

When running this example on Android, the Google Maps API key can also be provided via a command line option. The tabris-cli will replace the placeholder in the config.xml with the given API key:

```sh
$ tabris run <platform> --variables ANDROID_API_KEY=<google-maps-api-key>
```