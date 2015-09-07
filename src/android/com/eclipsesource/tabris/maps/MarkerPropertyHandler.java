package com.eclipsesource.tabris.maps;

import android.app.Activity;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.google.android.gms.maps.model.Marker;

public class MarkerPropertyHandler<T extends Marker> implements IPropertyHandler<T> {

  private final Activity activity;
  private final TabrisContext context;

  public MarkerPropertyHandler( Activity activity, TabrisContext context ) {
    this.activity = activity;
    this.context = context;
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
