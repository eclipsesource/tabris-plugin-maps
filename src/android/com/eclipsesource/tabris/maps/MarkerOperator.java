package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.operator.AbstractAndroidOperator;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.eclipsesource.tabris.client.core.operation.CreateOperation;
import com.eclipsesource.tabris.client.core.operation.SetOperation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.eclipsesource.tabris.client.core.ProtocolConstants.PROP_PARENT;

public class MarkerOperator extends AbstractAndroidOperator {

  private static final String WIDGET_TYPE = "_ESMarker";
  private final MarkerPropertyHandler markerPropertyHandler;

  public MarkerOperator( TabrisActivity activity ) {
    super( activity );
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
      Marker marker = createMarker( properties.getString( PROP_PARENT ),
          properties.getList( "latLng", Double.class ) );
      getObjectRegistry().register( createOperation.getTarget(), marker, createOperation.getType() );
    }
  }

  private Marker createMarker( String parentId, List<Double> latLng ) {
    MapHolderView mapHolderView = getObjectRegistry().getObject( parentId, MapHolderView.class );
    if( mapHolderView != null ) {
      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position( new LatLng( latLng.get( 0 ), latLng.get( 1 ) ) );
      return mapHolderView.getGoogleMap().addMarker( markerOptions );
    } else {
      throw new RuntimeException( "Can not find map parent with id " + parentId + " for marker.");
    }
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
