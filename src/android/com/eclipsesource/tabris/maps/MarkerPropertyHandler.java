package com.eclipsesource.tabris.maps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.eclipsesource.tabris.android.Properties;
import com.eclipsesource.tabris.android.PropertyHandler;
import com.eclipsesource.tabris.android.TabrisContext;
import com.eclipsesource.tabris.android.internal.toolkit.ImageProviderListenerAdapter;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static com.eclipsesource.tabris.android.internal.ParamCheck.notNull;
import static java.util.Arrays.asList;

public class MarkerPropertyHandler<T extends MapMarker> implements PropertyHandler<T> {

  private static final String PROP_POSITION = "position";
  private static final String PROP_TITLE = "title";
  private static final String PROP_SUBTITLE = "subtitle";
  private static final String PROP_IMAGE = "image";

  private final TabrisContext tabrisContext;

  public MarkerPropertyHandler(TabrisContext tabrisContext) {
    this.tabrisContext = notNull(tabrisContext, TabrisContext.class);
  }

  @Override
  public void set(T marker, Properties properties) {
    setPosition(marker, properties);
    setTitle(marker, properties);
    setImage(marker, properties);
    setSubtitle(marker, properties);
    marker.updateMarker();
  }

  private void setPosition(T marker, Properties properties) {
    List<Double> position = properties.getList(PROP_POSITION, Double.class);
    if (position == null || position.size() != 2) {
      throw new IllegalArgumentException("The 'position' property has to be a 2 element tuple but is " + position);
    }
    marker.setPosition(new LatLng(position.get(0), position.get(1)));
  }

  private void setTitle(T marker, Properties properties) {
    if (properties.hasProperty(PROP_TITLE)) {
      marker.getMarker().setTitle(properties.getString(PROP_TITLE));
    }
  }

  private void setImage(final T marker, Properties properties) {
    if (properties.hasProperty(PROP_IMAGE)) {
      List<Object> image = properties.getList(PROP_IMAGE, Object.class);
      if (image == null) {
        marker.setIcon(null);
      } else {
        this.tabrisContext.getWidgetToolkit().getImageProvider().load(image, new ImageProviderListenerAdapter() {
            @Override
            public void onImageLoaded(Drawable drawable) {
              Bitmap bitmap = drawableToBitmap(drawable);
              marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
              marker.updateMarker();
            }
        });
      }
    }
  }

  private Bitmap drawableToBitmap(Drawable drawable) {
    Canvas canvas = new Canvas();
    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    canvas.setBitmap(bitmap);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  private void setSubtitle(T marker, Properties properties) {
    if (properties.hasProperty(PROP_SUBTITLE)) {
      marker.getMarker().setTitle(properties.getString(PROP_SUBTITLE));
    }
  }

  @Override
  public Object get(T marker, String property) {
    switch (property) {
      case PROP_POSITION:
        return getPosition(marker);
    }
    return null;
  }

  private Object getPosition(T marker) {
    LatLng position = marker.getPosition();
    if (position != null) {
      return asList(position.latitude, position.longitude);
    }
    return null;
  }

}
