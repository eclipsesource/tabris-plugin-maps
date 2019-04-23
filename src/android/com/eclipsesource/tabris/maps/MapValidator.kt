package com.eclipsesource.tabris.maps

import com.google.android.gms.maps.GoogleMap

object MapValidator {

  fun validateGoogleMap(map: GoogleMap?, message: String) {
    check(map != null) {
      "Google Map is not yet ready. $message"
    }
  }

}
