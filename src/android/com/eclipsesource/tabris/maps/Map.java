package com.eclipsesource.tabris.maps;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.eclipsesource.tabris.android.TabrisActivity;
import com.eclipsesource.tabris.android.internal.toolkit.operator.AbstractWidgetOperator;
import com.eclipsesource.tabris.android.internal.toolkit.property.IPropertyHandler;
import com.eclipsesource.tabris.android.internal.toolkit.property.ViewPropertyHandler;
import com.eclipsesource.tabris.client.core.OperatorRegistry;
import com.eclipsesource.tabris.client.core.model.Properties;
import com.eclipsesource.tabris.client.core.operation.CallOperation;
import com.eclipsesource.tabris.client.core.operation.CreateOperation;
import com.eclipsesource.tabris.client.core.operation.DestroyOperation;
import com.eclipsesource.tabris.client.core.operation.ListenOperation;
import com.eclipsesource.tabris.client.core.operation.SetOperation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import static com.eclipsesource.tabris.client.core.util.ValidationUtil.validateCreateOperation;

/**
 * This class echoes a string called from JavaScript.
 */
public class Map extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        String message = args.getString(0);
        System.out.println(String.format("Execute executed. Action: %s args: %s", action, args));
        return true;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        TabrisActivity activity = (TabrisActivity)cordova.getActivity();
        OperatorRegistry operatorRegistry = activity.getWidgetToolkit().getOperatorRegistry();
        operatorRegistry.register("Map", new MapOperator(activity));
        System.out.println("Initialize " + operatorRegistry);
    }

    private class MapOperator extends AbstractWidgetOperator implements OnMapReadyCallback {
        private final MapPropertyHandler mapPropertyHandler;

        public MapOperator(TabrisActivity activity) {
            super(activity);
            mapPropertyHandler = new MapPropertyHandler(activity);
        }

        @Override
        protected IPropertyHandler getPropertyHandler(Object object) {
            return mapPropertyHandler;
        }

        @Override
        public String getType() {
            return "Map";
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void create(CreateOperation createOperation) {
            System.out.println("Create: " + createOperation.getTarget() + " " + createOperation.getType());
            validateCreateOperation(createOperation);

            FrameLayout frameLayout = new FrameLayout(getActivity());
            initiateNewView(createOperation, frameLayout);
            frameLayout.setBackgroundColor(Color.RED);

            FrameLayout mapLayout = new FrameLayout(getActivity());
            mapLayout.setId(View.generateViewId());
            frameLayout.addView(mapLayout);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mapLayout.setLayoutParams(layoutParams);

            MapFragment mapFragment = new MapFragment();
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(mapLayout.getId(), mapFragment).commit();
            mapFragment.getMapAsync(this);
        }

        @Override
        public void set(SetOperation setOperation) {

        }

        @Override
        public void listen(ListenOperation listenOperation) {

        }

        @Override
        public Object call(CallOperation callOperation) {
            return null;
        }

        @Override
        public Object get(String s, String s1) {
            return null;
        }

        @Override
        public void destroy(DestroyOperation destroyOperation) {

        }

        @Override
        public void onMapReady(GoogleMap map) {
            System.out.println("Map showing");
            LatLng sydney = new LatLng(-33.867, 151.206);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        }

        private class MapPropertyHandler extends ViewPropertyHandler<FrameLayout> {

            public MapPropertyHandler(TabrisActivity activity) {
                super(activity);
            }

            @Override
            public void set(FrameLayout view, Properties properties) {
                super.set(view, properties);
            }

            @Override
            public Object get(FrameLayout view, String property) {
                return super.get(view, property);
            }

        }
    }
}