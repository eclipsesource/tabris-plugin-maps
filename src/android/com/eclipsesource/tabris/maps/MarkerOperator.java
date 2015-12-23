package com.eclipsesource.tabris.maps;

import android.app.Activity;
import android.util.Log;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.eclipsesource.tabris.client.core.operation.CreateOperation;
import com.eclipsesource.tabris.client.core.operation.ListenOperation;
import com.eclipsesource.tabris.client.core.operation.SetOperation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.eclipsesource.tabris.client.core.ProtocolConstants.PROP_PARENT;
import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateListenOperation;

public class MarkerOperator extends AbstractTabrisOperator<Marker> {

  private static final String WIDGET_TYPE = "tabris.maps.marker";
  private static final String EVENT_TAP = "tap";
  private static final String LOG_TAG = WIDGET_TYPE;

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private final MarkerPropertyHandler markerPropertyHandler;
  private String mapId;

  public MarkerOperator( Activity activity, TabrisContext tabrisContext ) {
    this.activity = activity;
    this.tabrisContext = tabrisContext;
    markerPropertyHandler = new MarkerPropertyHandler( activity, tabrisContext );
  }

  @Override
  public TabrisPropertyHandler<Marker> getPropertyHandler() {
    return markerPropertyHandler;
  }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @Override
  public Marker create( Properties properties ) {
    Log.d( LOG_TAG, String.format( "Creating new marker. %s", properties ) );
    if( properties != null
        && properties.hasProperty( PROP_PARENT )
        && properties.hasProperty( "latLng" ) ) {
      mapId = properties.getString( PROP_PARENT );
      Marker marker = createMarker( properties.getList( "latLng", Double.class ) );
      Log.d( LOG_TAG, String.format( "New marker is: %s (id: %s)", marker, marker.getId() ) );
      return marker;
    } else {
      throw new RuntimeException( "Invalid marker properties: " + properties );
    }
  }

  @Override
  public void destroy( Marker marker ) {
    Log.d( LOG_TAG, String.format( "Removing marker: %s (id: %s)", marker, marker.getId() ) );
    marker.remove();
  }

  @Override
  public void listen( final Marker marker, String event, boolean listen ) {
    switch( event ) {
      case EVENT_TAP:
        if( listen ) {
          attachOnMarkerTapListener();
        } else {
          removeOnMarkerTapListener();
        }
        break;
    }
  }

  public GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    MapValidator.validateGoogleMap( googleMap,
        "Markers can only be used after the Map's 'ready' event has been fired." );
    return googleMap;
  }

  private Marker createMarker( List<Double> latLng ) {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    if( mapHolderView != null ) {
      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position( new LatLng( latLng.get( 0 ), latLng.get( 1 ) ) );
      return mapHolderView.getGoogleMap().addMarker( markerOptions );
    } else {
      throw new RuntimeException( "Can not find parent map for marker with id: " + mapId );
    }
  }

  private void attachOnMarkerTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    Log.d( LOG_TAG, String.format( "Attaching listener to: %s", googleMap ) );
    googleMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick( Marker marker ) {
        Log.d( LOG_TAG, String.format( "Marker %s with id %s was tapped.", marker, marker.getId() ) );
        getObjectRegistry().getRemoteObjectForObject( marker ).notify( EVENT_TAP );
        return true;
      }
    } );
  }

  private void removeOnMarkerTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMarkerClickListener( null );
  }

  private ObjectRegistry getObjectRegistry() {
    return tabrisContext.getObjectRegistry();
  }

}
