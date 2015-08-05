package com.eclipsesource.tabris.maps;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MapOperator_Test {

  @Test
  public void testGetType() {
    MapOperator operator = new MapOperator( mock( TabrisActivity.class ) );

    String type = operator.getType();

    assertEquals( "com.eclipsesource.tabris.maps.map", type );
  }

  @Test
  public void testGetPropertyHandler() {
    MapOperator operator = new MapOperator( mock( TabrisActivity.class ) );

    IPropertyHandler propertyHandler = operator.getPropertyHandler( null );

    assertTrue( propertyHandler instanceof MapPropertyHandler );
  }

}