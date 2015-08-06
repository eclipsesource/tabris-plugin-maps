package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.Marker;

public class MarkerPropertyHandler<T extends Marker> implements IPropertyHandler<T> {

  private TabrisActivity activity;

  public MarkerPropertyHandler( TabrisActivity activity ) {
    this.activity = activity;
  }

  @Override
  public Object get( T marker, String property ) {
    System.out.println( "get on " + marker + ": " + property );
    return null;
  }

  @Override
  public void set( T marker, Properties properties ) {
    System.out.println( "set on " + marker + ": " + properties );
  }
}
