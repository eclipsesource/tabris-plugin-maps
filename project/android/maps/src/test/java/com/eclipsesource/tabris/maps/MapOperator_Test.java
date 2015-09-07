package com.eclipsesource.tabris.maps;

import android.app.Activity;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.TabrisPropertyHandler;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MapOperator_Test {

  @Test
  public void testGetType() {
    MapOperator operator = new MapOperator( mock( Activity.class ), mock( TabrisContext.class) );

    String type = operator.getType();

    assertEquals( "ESMap", type );
  }

  @Test
  public void testGetPropertyHandler() {
    MapOperator operator = new MapOperator( mock( Activity.class ), mock( TabrisContext.class)  );

    TabrisPropertyHandler propertyHandler = operator.getPropertyHandler();

    assertTrue( propertyHandler instanceof MapPropertyHandler );
  }

}