package com.szantog.brew_e;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

interface BrowseFragmentCallback {
    void browseFragmentButtonClicked(int buttonId, int shopId);
}

public class BrowseFragment extends Fragment implements Marker.OnMarkerClickListener {

    public static final int MENU_BUTTON_ID = 200;
    public static final int BLOG_BUTTON_ID = 201;

    private BrowseFragmentCallback browseFragmentCallback;

    private int selectedShopId = 0;

    public BrowseFragment(BrowseFragmentCallback browseFragmentCallback) {
        this.browseFragmentCallback = browseFragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.browse_map_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.browse_map_drink_menu_text).setOnClickListener(this::buttonClicked);
        view.findViewById(R.id.browse_map_drink_menu_img).setOnClickListener(this::buttonClicked);
        view.findViewById(R.id.browse_map_blog_text).setOnClickListener(this::buttonClicked);
        view.findViewById(R.id.browse_map_blog_img).setOnClickListener(this::buttonClicked);

        MapView mapView = view.findViewById(R.id.mapfragment);
        mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP);
        mapView.setMultiTouchControls(true);
        Double initZoom = 10d;
        Double budapestLatitude = 47.49801;
        Double budapestLongitude = 19.03991;
        mapView.getController().setZoom(initZoom);
        mapView.getController().setCenter(new GeoPoint(budapestLatitude, budapestLongitude));

        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(budapestLatitude + random.nextDouble() / 10, budapestLongitude + random.nextDouble() / 10));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setInfoWindow(null);
            marker.setSubDescription(String.valueOf(i));
            mapView.getOverlays().add(marker);
            marker.setOnMarkerClickListener(this);
        }
    }

    public void buttonClicked(View v) {
        int buttonId = 0;
        if (v.getId() == R.id.browse_map_drink_menu_text || v.getId() == R.id.browse_map_drink_menu_img) {
            buttonId = MENU_BUTTON_ID;
        } else if (v.getId() == R.id.browse_map_blog_text || v.getId() == R.id.browse_map_blog_img) {
            buttonId = BLOG_BUTTON_ID;
        }
        if (browseFragmentCallback != null) {
            browseFragmentCallback.browseFragmentButtonClicked(buttonId, selectedShopId);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        Log.e("alt-long", "alt: " + String.valueOf(marker.getPosition().getAltitude()) + ", long: " +
                String.valueOf(marker.getPosition().getLongitude()));
        Log.e("subdescr", marker.getSubDescription());
        return false;
    }
}
