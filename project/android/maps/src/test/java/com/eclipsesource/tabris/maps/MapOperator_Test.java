package com.eclipsesource.tabris.maps;

import android.app.Activity;

import com.eclipsesource.tabris.android.PropertyHandler;
import com.eclipsesource.tabris.android.TabrisContext;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MapOperator_Test {

  @Test
  public void testGetType() {
    MapOperator operator = new MapOperator(mock(Activity.class), mock(TabrisContext.class));

    String type = operator.getType();

    assertEquals("com.eclipsesource.maps.Map", type);
  }

  @Test
  public void testGetPropertyHandler() {
    MapOperator operator = new MapOperator(mock(Activity.class), mock(TabrisContext.class));

    PropertyHandler propertyHandler = operator.getPropertyHandler(null);

    assertTrue(propertyHandler instanceof MapPropertyHandler);
  }

}