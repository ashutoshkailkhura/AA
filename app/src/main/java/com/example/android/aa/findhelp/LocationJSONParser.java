package com.example.android.aa.findhelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationJSONParser {

    public static List<PlaceModel> parseFeed(String content) {


        try {
            JSONObject obj = new JSONObject(content);
            JSONArray data = obj.getJSONArray("findhelp");
            List<PlaceModel> placelistModelList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonlocation = data.getJSONObject(i);
                PlaceModel placelistModel = new PlaceModel();

                placelistModel.setPlacename(jsonlocation.getString("placename"));
                placelistModel.setAddress(jsonlocation.getString("address"));
                placelistModel.setMobilenum(jsonlocation.getString("mobilenum"));
                placelistModel.setImage(jsonlocation.getString("image"));
                placelistModel.setLat(jsonlocation.getDouble("lat"));
                placelistModel.setLng(jsonlocation.getDouble("lng"));
                placelistModel.setAddress(jsonlocation.getString("discription"));

                placelistModelList.add(placelistModel);
            }

            return placelistModelList;
        } catch (JSONException e) {
            return null;

        }
    }
}

