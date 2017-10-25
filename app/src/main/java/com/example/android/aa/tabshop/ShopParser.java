package com.example.android.aa.tabshop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopParser {

    static double n;

    public static List<ShopModel> parseFeed(String content) {

        try {
            List<ShopModel> shopModelList = new ArrayList<>();
            JSONObject obj = new JSONObject(content);
            JSONArray data = obj.getJSONArray("shop");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonlocation = data.getJSONObject(i);
                ShopModel food = new ShopModel();

                food.setProductId(jsonlocation.getInt("productId"));
                food.setName(jsonlocation.getString("name"));
                food.setCategory(jsonlocation.getString("category"));
                food.setQuantity(jsonlocation.getInt("quantity"));
                food.setPhoto(jsonlocation.getString("photo"));
                food.setPrice(jsonlocation.getDouble("price"));

                shopModelList.add(food);
            }

            return shopModelList;
        } catch (JSONException e) {
            return null;
        }
    }
}