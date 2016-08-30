package com.eclipsesource.tabris.maps;

import com.google.android.gms.maps.GoogleMap;

public class MapValidator {

  public static void validateGoogleMap( GoogleMap map, String message ) {
    if( map == null ) {
      throw new IllegalStateException( "Google Map is not yet ready. " + message );
    }
  }

}
