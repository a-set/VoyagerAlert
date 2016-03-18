package co.sethspace.voyageralert;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class VoyagerMainActivityFragment extends Fragment implements OnMapReadyCallback,
        LocationListener{

    private static final String TAG = "VoyagerMainFragment";
    private GoogleMap mMap;
    private static OkHttpClient client = new OkHttpClient();
    List<VoyagerHistory> histories;
    private VoyagerHistoryAdapter voyagerHistoryAdapter;

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

        //Set the Recycler View Adapter
        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.voyager_history_list);



        //TODO: Make a call to the server to get the List of historical threats
        histories = VoyagerHistory.createDummyList(10);

        //Instantiate the adapter
        voyagerHistoryAdapter = new VoyagerHistoryAdapter(histories);

        //Set the adapter
        recyclerView.setAdapter(voyagerHistoryAdapter);

        //Set the Linear Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //set a default item animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Add Item Decoration
        RecyclerView.ItemDecoration itemDecoration = new
                VoyagerDividerItemDecoration(getActivity(), VoyagerDividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);



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

        //Make a call to fetch threats
        updateListOfThreats(latitude,longitude);

    }

    public void updateListOfThreats(double latitude, double longitude){
        //Build the url
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("uci.alware.org")
                .addPathSegment("history.php")
                .addQueryParameter("id","fAactxgd5Xo:APA91bHjyJImJ5hscECMsyhHURLS0uJE0SrsCUypHY_rniMoesJvwNBkg9tAu7YSjEOf-X_CCCmp2OCsOMI16OuFO8YZFgAu1FtgWIjlkQbzIy7gOTjf3-VZlkderqysHDSMvjaczQSm")
                .addQueryParameter("time", Long.toString(1457891414l))
                .addQueryParameter("long", Double.toString(longitude))
                .addQueryParameter("lat", Double.toString(latitude))
                .addQueryParameter("level",Integer.toString(100)).build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) throw new IOException("Unexpected Code" + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.i("TEST",responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                try {
                    JSONObject responseJson = new JSONObject(response.body().string());
                    if(responseJson.getString("status").equals("OK")) {
                        histories = new ArrayList<VoyagerHistory>();
                        JSONArray historyJson = responseJson.getJSONArray("history");
                        for (int i = 0; i < historyJson.length(); i++) {
                            JSONObject jsonObject = historyJson.getJSONObject(i);
                            histories.add(new VoyagerHistory(jsonObject.getInt("level"),
                                    jsonObject.getString("text"), jsonObject.getInt("type")));
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    voyagerHistoryAdapter.addAllItems(histories);
                                    voyagerHistoryAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
