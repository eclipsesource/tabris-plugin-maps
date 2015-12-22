package com.eclipsesource.tabris.maps;

import android.app.Activity;
import android.util.Log;

import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

public class MarkerPropertyHandler<T extends Marker> implements TabrisPropertyHandler<T> {

  private static final String LOG_TAG = "marker.properties";
  private static final String PROPERTY_TITLE = "title";
  private static final String PROPERTY_COLOR = "color";
  private static final Map<String, Float> COLOR_MAP = new HashMap<String, Float>();

  static {
    COLOR_MAP.put( "azure", BitmapDescriptorFactory.HUE_AZURE );
    COLOR_MAP.put( "cyan", BitmapDescriptorFactory.HUE_CYAN );
    COLOR_MAP.put( "blue", BitmapDescriptorFactory.HUE_BLUE );
    COLOR_MAP.put( "green", BitmapDescriptorFactory.HUE_GREEN );
    COLOR_MAP.put( "magenta", BitmapDescriptorFactory.HUE_MAGENTA );
    COLOR_MAP.put( "orange", BitmapDescriptorFactory.HUE_ORANGE );
    COLOR_MAP.put( "red", BitmapDescriptorFactory.HUE_RED );
    COLOR_MAP.put( "rose", BitmapDescriptorFactory.HUE_ROSE );
    COLOR_MAP.put( "violet", BitmapDescriptorFactory.HUE_VIOLET );
    COLOR_MAP.put( "yellow", BitmapDescriptorFactory.HUE_YELLOW );
  }

  private final Activity activity;
  private final TabrisContext context;

  public MarkerPropertyHandler( Activity activity, TabrisContext context ) {
    this.activity = activity;
    this.context = context;
  }

  @Override
  public Object get( T marker, String property ) {
    Log.d( LOG_TAG, String.format( "get \"%s\" from %s", property, marker ) );
    switch( property ) {
      case "title":
        return marker.getTitle();
    }
    return null;
  }

  @Override
  public void set( T marker, Properties properties ) {
    Log.d( LOG_TAG, String.format( "set \"%s\" on %s", properties, marker ) );
    setTitle( marker, properties );
    setMarkerColor( marker, properties );
  }

  private void setMarkerColor( T marker, Properties properties ) {
    if( properties.hasProperty( PROPERTY_COLOR ) ) {
      String markerColorName = properties.getString( PROPERTY_COLOR );
      Float markerColor = COLOR_MAP.get( markerColorName );
      if( markerColor == null ) {
        Log.e( LOG_TAG, String.format( "Color \"%s\" not available!", markerColorName ) );
      } else {
        marker.setIcon( BitmapDescriptorFactory.defaultMarker( markerColor ) );
      }
    }
  }

  private void setTitle( T marker, Properties properties ) {
    if( properties.hasProperty( PROPERTY_TITLE ) ) {
      marker.setTitle( properties.getString( PROPERTY_TITLE ) );
    }
  }

}
