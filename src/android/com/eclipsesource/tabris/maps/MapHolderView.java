/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.eclipsesource.tabris.android.TabrisContext;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * This class represents one instance of the Tabris maps custom widget in the view hierarchy.
 */
public class MapHolderView extends FrameLayout implements OnMapReadyCallback {

  public static final String EVENT_READY = "ready";

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private GoogleMap googleMap;
  private MapFragment mapFragment;
  private boolean changeUserInitiated;

  public MapHolderView( Activity activity, TabrisContext tabrisContext ) {
    super( activity );
    this.activity = activity;
    this.tabrisContext = tabrisContext;
  }

  public void createMap() {
    FrameLayout mapDummy = createMapDummy();
    mapFragment = new MapFragment();
    FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
    ft.replace( mapDummy.getId(), mapFragment ).commit();
  }

  @NonNull
  @TargetApi( Build.VERSION_CODES.JELLY_BEAN_MR1 )
  private FrameLayout createMapDummy() {
    FrameLayout mapDummy = new FrameLayout( activity );
    mapDummy.setId( View.generateViewId() + 1024 ); // TODO generate a clever unique ID
    addView( mapDummy, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
    return mapDummy;
  }

  public GoogleMap getGoogleMap() {
    return googleMap;
  }

  public void setOnMapReadyListener() {
    mapFragment.getMapAsync( this );
  }

  @Override
  public void onMapReady( GoogleMap googleMap ) {
    this.googleMap = googleMap;
    tabrisContext.getObjectRegistry().getRemoteObjectForObject( this ).notify( EVENT_READY );
  }

  public boolean isChangeUserInitiated() {
    return changeUserInitiated;
  }

  public void setChangeUserInitiated( boolean changeUserInitiated ) {
    this.changeUserInitiated = changeUserInitiated;
  }

  public void moveCamera( CameraUpdate cameraUpdate ) {
    setChangeUserInitiated( false );
    getGoogleMap().moveCamera( cameraUpdate );
  }

  public void animateCamera( CameraUpdate cameraUpdate ) {
    getGoogleMap().animateCamera( cameraUpdate, new UserInitiationCallback( this ) );
  }

  private static class UserInitiationCallback implements GoogleMap.CancelableCallback {

    private final MapHolderView mapHolderView;

    UserInitiationCallback( MapHolderView mapHolderView ) {
      this.mapHolderView = mapHolderView;
    }

    @Override
    public void onFinish() {
      mapHolderView.setChangeUserInitiated( true );
    }

    @Override
    public void onCancel() {
      mapHolderView.setChangeUserInitiated( false );
    }
  }
}
