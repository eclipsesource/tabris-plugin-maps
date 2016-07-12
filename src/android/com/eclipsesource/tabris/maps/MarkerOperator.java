package com.eclipsesource.tabris.maps;

import android.app.Activity;

import com.eclipsesource.tabris.android.AbstractTabrisOperator;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.client.core.ObjectRegistry;
import com.eclipsesource.tabris.client.core.ObjectRegistry.RegistryEntry;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.eclipsesource.tabris.client.core.ProtocolConstants.PROP_PARENT;

public class MarkerOperator extends AbstractTabrisOperator<MapMarker> {

  public static final String TYPE = "tabris.maps.marker";

  private static final String PROP_LAT_LNG = "latLng";
  private static final String EVENT_TAP = "tap";

  private final TabrisContext tabrisContext;
  private final MarkerPropertyHandler markerPropertyHandler;

  public MarkerOperator( Activity activity, TabrisContext tabrisContext ) {
    this.tabrisContext = tabrisContext;
    markerPropertyHandler = new MarkerPropertyHandler( activity, tabrisContext );
  }

  @Override
  public TabrisPropertyHandler<MapMarker> getPropertyHandler() {
    return markerPropertyHandler;
  }

  @Override
  public String getType() {
    return TYPE;
  }

  @Override
  public MapMarker create( Properties properties ) {
    String mapId = properties.getStringChecked( PROP_PARENT );
    List<Double> latLng = properties.getList( PROP_LAT_LNG, Double.class );
    Marker marker = createMarker( latLng, mapId );
    return new MapMarker( marker, mapId );
  }

  @Override
  public void destroy( MapMarker marker ) {
    marker.getMarker().remove();
  }

  @Override
  public void listen( final MapMarker marker, String event, boolean listen ) {
    switch( event ) {
      case EVENT_TAP:
        if( listen ) {
          attachOnMarkerTapListener( marker );
        } else {
          removeOnMarkerTapListener( marker );
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

  private Marker createMarker( List<Double> latLng, String mapId ) {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    if( mapHolderView != null ) {
      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position( new LatLng( latLng.get( 0 ), latLng.get( 1 ) ) );
      return mapHolderView.getGoogleMap().addMarker( markerOptions );
    } else {
      throw new RuntimeException( "Can not find parent map for marker with id: " + mapId );
    }
  }

  private void attachOnMarkerTapListener( final MapMarker mapMarker ) {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapMarker.getMapId(), MapHolderView.class );
    getGoogleMapSafely( mapHolderView ).setOnMarkerClickListener( new OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick( Marker marker ) {
        for( RegistryEntry entry : getObjectRegistry().getEntries() ) {
          Object object = entry.getObject();
          if( object instanceof MapMarker ) {
            MapMarker mapMarkerCandidate = ( MapMarker )object;
            if( mapMarkerCandidate.getMapId().equals( mapMarker.getMapId() )
                && mapMarkerCandidate.getMarker().getId().equals( marker.getId() ) ) {
              getObjectRegistry().getRemoteObjectForObject( mapMarkerCandidate ).notify( EVENT_TAP );
              break;
            }
          }
        }
        return true;
      }
    } );
  }

  private void removeOnMarkerTapListener( MapMarker marker ) {
    MapHolderView mapHolderView = getObjectRegistry().getObject( marker.getMapId(), MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMarkerClickListener( null );
  }

  private ObjectRegistry getObjectRegistry() {
    return tabrisContext.getObjectRegistry();
  }

}
