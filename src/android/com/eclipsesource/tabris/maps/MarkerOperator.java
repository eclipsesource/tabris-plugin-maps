package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.operator.AbstractAndroidOperator;
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

public class MarkerOperator extends AbstractAndroidOperator {

  private static final String WIDGET_TYPE = "_ESMarker";
  private static final String EVENT_TAP = "tap";
  private final MarkerPropertyHandler markerPropertyHandler;
  private String mapId;
  private TabrisActivity activity;

  public MarkerOperator( TabrisActivity activity ) {
    super( activity );
    this.activity = activity;
    markerPropertyHandler = new MarkerPropertyHandler( activity );
  }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @Override
  public void create( CreateOperation createOperation ) {
    super.create( createOperation );
    Properties properties = createOperation.getProperties();
    if( properties != null
        && properties.hasProperty( PROP_PARENT )
        && properties.hasProperty( "latLng" ) ) {
      mapId = properties.getString( PROP_PARENT );
      Marker marker = createMarker( properties.getList( "latLng", Double.class ) );
      getObjectRegistry().register( createOperation.getTarget(), marker, createOperation.getType() );
    }
  }

  private Marker createMarker( List<Double> latLng ) {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    if( mapHolderView != null ) {
      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position( new LatLng( latLng.get( 0 ), latLng.get( 1 ) ) );
      return mapHolderView.getGoogleMap().addMarker( markerOptions );
    } else {
      throw new RuntimeException( "Can not find map parent with id " + mapId + " for marker." );
    }
  }

  @Override
  public void listen( ListenOperation listenOperation ) {
    super.listen( listenOperation );
    validateListenOperation( listenOperation );
    Properties properties = listenOperation.getProperties();
    if( properties.hasProperty( EVENT_TAP ) ) {
      if( properties.getBoolean( EVENT_TAP ) ) {
        attachOnMarkerTapListener();
      } else {
        removeOnMarkerTapListener();
      }
    }
  }

  private void attachOnMarkerTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick( Marker marker ) {
        activity.getRemoteObject( marker ).notify( EVENT_TAP );
        return true;
      }
    } );
  }

  private void removeOnMarkerTapListener() {
    MapHolderView mapHolderView = getObjectRegistry().getObject( mapId, MapHolderView.class );
    GoogleMap googleMap = getGoogleMapSafely( mapHolderView );
    googleMap.setOnMarkerClickListener( null );
  }

  public GoogleMap getGoogleMapSafely( MapHolderView mapHolderView ) {
    GoogleMap googleMap = mapHolderView.getGoogleMap();
    MapValidator.validateGoogleMap( googleMap,
        "Markers can only be used after the Map's 'ready' event has been fired." );
    return googleMap;
  }

  @Override
  public Object get( String target, String property ) {
    return super.get( target, property );
  }

  @Override
  public void set( SetOperation operation ) {
    super.set( operation );
  }

}
