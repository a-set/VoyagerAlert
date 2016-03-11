package co.sethspace.voyageralert;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * A placeholder fragment containing a simple view.
 */
public class VoyagerMainActivityFragment extends Fragment implements OnMapReadyCallback,
        LocationListener{

    private GoogleMap mMap;
    public VoyagerMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_voyager_main, container, false);

        //Set the toolbar
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.voyager_toolbar);
        toolbar.setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Load the map
        ((SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.voyager_map))
                .getMapAsync(this);



        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Getting current location and enabling my location button
        mMap.setMyLocationEnabled(true);

        //Get current location
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(android.app.Activity.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        //If location is not available toast it
        if (location != null)
            onLocationChanged(location);
        else
            Toast.makeText(getActivity(), R.string.voyager_no_location, Toast.LENGTH_SHORT).show();

        locationManager.requestLocationUpdates(provider, 20000, 5, this);
        //Set map to get user location
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
