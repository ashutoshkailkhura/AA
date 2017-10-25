package com.example.android.aa.findhelp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.android.aa.HttpManager;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.CheckInternetConnection;
import com.example.android.aa.repo.Const;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.android.aa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExploreAll extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<String> values;
    private ArrayList<Double> lat;
    private ArrayList<Double> lng;
    GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_all);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        // if (CheckInternetConnection.getInstance(this).isOnline()) {
        requestData(Const.EXPLORE_ALL);

    }


    private void addMarker(ArrayList<String> values, ArrayList<Double> lat, ArrayList<Double> lng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        for (int i = 0; i < values.size(); i++) {
            double mlat = lat.get(i);
            double mlng = lng.get(i);
            LatLng latLng = new LatLng(mlat, mlng);
            markerOptions.title(values.get(i));
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions);
        }

    }

    private void requestData(String uri) {
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        explaoreAsynk task = new explaoreAsynk();
        task.execute(p);
    }

    class explaoreAsynk extends AsyncTask<RequestPackage, Void, List<PlaceModel>> {


        @Override
        protected List<PlaceModel> doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            try {
                JSONObject obj = new JSONObject(content);
                JSONArray data = obj.getJSONArray("explore");
                List<PlaceModel> placelistModelList = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonlocation = data.getJSONObject(i);
                    PlaceModel placelistModel = new PlaceModel();
                    placelistModel.setPlacename(jsonlocation.getString("placename"));
                    placelistModel.setLat(jsonlocation.getDouble("lat"));
                    placelistModel.setLng(jsonlocation.getDouble("lng"));
                    placelistModelList.add(placelistModel);

                }


                return placelistModelList;
            } catch (JSONException e) {
                return null;

            }

        }

        @Override
        protected void onPostExecute(List<PlaceModel> placeModels) {
            values = new ArrayList<>();
            lat = new ArrayList<>();
            lng = new ArrayList<>();
            for (PlaceModel location : placeModels) {
                values.add(location.getPlacename());
                lat.add(location.getLat());
                lng.add(location.getLng());
            }

            addMarker(values, lat, lng);

        }
    }


}
