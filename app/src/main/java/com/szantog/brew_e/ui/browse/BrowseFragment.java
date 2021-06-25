package com.szantog.brew_e.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szantog.brew_e.R;
import com.szantog.brew_e.common.AppButtonIdCollection;
import com.szantog.brew_e.domain.CoffeeShop;
import com.szantog.brew_e.ui.MainController;
import com.szantog.brew_e.ui.MainViewModel;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class BrowseFragment extends Fragment implements Marker.OnMarkerClickListener {

    private MainViewModel mainViewModel;
    private BrowseViewModel browseViewModel;

    private MapView mapView;
    private List<CoffeeShop> coffeeShopList;
    private int selectedShopId = -1;

    private TextView shopNameTextView;
    private TextView shopAddressTextView;
    private TextView shopMottoTextView;

    public void addCoffeeShopsToMap() {
        for (int i = 0; i < coffeeShopList.size(); i++) {
            Marker marker = new Marker(mapView);
            try {
                Double Lat = Double.parseDouble(coffeeShopList.get(i).getLat_coord());
                Double Lon = Double.parseDouble(coffeeShopList.get(i).getLon_coord());
                marker.setPosition(new GeoPoint(Lat, Lon));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setInfoWindow(null);
                marker.setSubDescription(String.valueOf(coffeeShopList.get(i).getId()));
                mapView.getOverlays().add(marker);
                marker.setOnMarkerClickListener(this);
            } catch (NullPointerException e) {

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.browse_map_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopNameTextView = view.findViewById(R.id.browse_map_selected_shop_title);
        shopAddressTextView = view.findViewById(R.id.browse_map_selected_shop_details);
        shopMottoTextView = view.findViewById(R.id.browse_map_selected_shop_motto);

        view.findViewById(R.id.browse_map_drink_menu_text).setOnClickListener(this::buttonClicked);
        view.findViewById(R.id.browse_map_drink_menu_img).setOnClickListener(this::buttonClicked);
        view.findViewById(R.id.browse_map_blog_text).setOnClickListener(this::buttonClicked);
        view.findViewById(R.id.browse_map_blog_img).setOnClickListener(this::buttonClicked);

        mapView = view.findViewById(R.id.mapfragment);
        mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP);
        mapView.setMultiTouchControls(true);
        Double initZoom = 10d;
        Double budapestLatitude = 47.49801;
        Double budapestLongitude = 19.03991;
        mapView.getController().setZoom(initZoom);
        mapView.getController().setCenter(new GeoPoint(budapestLatitude, budapestLongitude));

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        browseViewModel = new ViewModelProvider((requireActivity())).get(BrowseViewModel.class);
        ((MainController)requireActivity()).showProgressDialog(true);
        browseViewModel.downloadShopList();
        ((MainController)requireActivity()).showProgressDialog(false);
        browseViewModel.getCoffeeShopList().observe(requireActivity(), new Observer<List<CoffeeShop>>() {
            @Override
            public void onChanged(List<CoffeeShop> coffeeShops) {
                if (coffeeShops != null) {
                    BrowseFragment.this.coffeeShopList = coffeeShops;
                    if (mapView != null)
                        addCoffeeShopsToMap();
                }
            }
        });
    }

    public void buttonClicked(View v) {
        if (selectedShopId > 0) {
            int buttonId = 0;
            if (v.getId() == R.id.browse_map_drink_menu_text || v.getId() == R.id.browse_map_drink_menu_img) {
                buttonId = AppButtonIdCollection.BROWSEFRAGMENT_MENU_BUTTON_ID;
            } else if (v.getId() == R.id.browse_map_blog_text || v.getId() == R.id.browse_map_blog_img) {
                buttonId = AppButtonIdCollection.BROWSEFRAGMENT_BLOG_BUTTON_ID;
            }
            mainViewModel.setClickedButtonId(buttonId);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        int selectedId = Integer.parseInt(marker.getSubDescription());
        selectedShopId = selectedId;
        ((MainController) requireActivity()).setSelectedShopId(selectedId);
        int index = 0;
        for (int i = 0; i < coffeeShopList.size(); i++) {
            if (coffeeShopList.get(i).getId() == selectedShopId) {
                index = i;
            }
        }
        CoffeeShop selectedShop = coffeeShopList.get(index);
        shopNameTextView.setText(selectedShop.getName());
        shopAddressTextView.setText(selectedShop.getCity() + " " + selectedShop.getPostalcode() + " " + selectedShop.getStreet());
        shopMottoTextView.setText(selectedShop.getDescription());
        return false;
    }
}
