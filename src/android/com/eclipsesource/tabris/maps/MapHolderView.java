/*
 * Copyright(c) 2015 EclipseSource. All Rights Reserved.
 */

package com.eclipsesource.tabris.maps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.eclipsesource.tabris.android.ObjectRegistry;
import com.eclipsesource.tabris.android.TabrisContext;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

@SuppressLint("ViewConstructor")
public class MapHolderView extends FrameLayout implements OnMapReadyCallback {

  public static final String EVENT_READY = "ready";

  private final Activity activity;
  private final TabrisContext tabrisContext;
  private GoogleMap googleMap;
  private SupportMapFragment mapFragment;

  public MapHolderView(Activity activity, TabrisContext tabrisContext) {
    super(activity);
    this.activity = activity;
    this.tabrisContext = tabrisContext;
    setId(View.generateViewId());
    createMap();
  }

  public void createMap() {
    mapFragment = new SupportMapFragment();
    if (activity instanceof AppCompatActivity) {
      AppCompatActivity appCompatActivity = (AppCompatActivity)activity;
      appCompatActivity.getSupportFragmentManager()
          .beginTransaction()
          .add(getId(), mapFragment)
          .commit();
    } else {
      throw new RuntimeException("Maps plugin requires " + AppCompatActivity.class.getSimpleName());
    }
  }

  public GoogleMap getGoogleMap() {
    return googleMap;
  }

  public void setOnMapReadyListener() {
    mapFragment.getMapAsync(this);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    this.googleMap = googleMap;
    ObjectRegistry objectRegistry = tabrisContext.getObjectRegistry();
    googleMap.setOnMarkerClickListener(new MarkerTapListener(this, objectRegistry));
    MapCameraChangeListener cameraChangeListener = new MapCameraChangeListener(this, objectRegistry);
    googleMap.setOnCameraMoveStartedListener(cameraChangeListener);
    googleMap.setOnCameraIdleListener(cameraChangeListener);
    objectRegistry.getRemoteObjectForObject(this).notify(EVENT_READY);
  }

  public void moveCamera(CameraUpdate cameraUpdate) {
    getGoogleMap().moveCamera(cameraUpdate);
  }

  public void animateCamera(CameraUpdate cameraUpdate) {
    getGoogleMap().animateCamera(cameraUpdate);
  }

}