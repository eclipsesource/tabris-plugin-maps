/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.operator.AbstractWidgetOperator;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.client.core.operation.CallOperation;
import com.eclipsesource.tabris.client.core.operation.CreateOperation;
import com.eclipsesource.tabris.client.core.operation.DestroyOperation;
import com.eclipsesource.tabris.client.core.operation.ListenOperation;
import com.eclipsesource.tabris.client.core.operation.SetOperation;

import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateCreateOperation;

/**
 * This class handles all protocol operation for the Tabris maps custom widget.
 */
public class MapOperator extends AbstractWidgetOperator {

  private static final String LOG_TAG = "tabris.maps";
  public static final String WIDGET_TYPE = "com.eclipsesource.tabris.maps.map";

  private final MapPropertyHandler mapPropertyHandler;

  public MapOperator( TabrisActivity activity ) {
    super( activity );
    mapPropertyHandler = new MapPropertyHandler( activity );
  }

  @Override
  protected IPropertyHandler getPropertyHandler( Object object ) {
    return mapPropertyHandler;
  }

  @Override
  public String getType() {
    return WIDGET_TYPE;
  }

  @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR1 )
  @Override
  public void create( CreateOperation createOperation ) {
    Log.d( LOG_TAG, String.format( "Creating new map. Target: %s, Type: %s",
        createOperation.getTarget(), createOperation.getType() ) );
    validateCreateOperation( createOperation );
    MapHolderView mapHolderView = new MapHolderView( getActivity() );
    initiateNewView( createOperation, mapHolderView );
    mapHolderView.createMap();
  }

  @Override
  public void set( SetOperation setOperation ) {
    super.set( setOperation );
  }

  @Override
  public void listen( ListenOperation listenOperation ) {
    super.listen( listenOperation );
  }

  @Override
  public Object call( CallOperation callOperation ) {
    return null;
  }

  @Override
  public Object get( String s, String s1 ) {
    return null;
  }

  @Override
  public void destroy( DestroyOperation destroyOperation ) {
    super.destroy( destroyOperation );
  }

}