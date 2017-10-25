package com.example.android.aa.findhelp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.android.aa.R;
import com.example.android.aa.repo.Const;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<HPViewHolder> {

    private List<PlaceModel> mPlaceModel;
    private LayoutInflater layoutInflater;


    public LocationAdapter(List<PlaceModel> mPlaceModel, Context ctx) {
        this.mPlaceModel = mPlaceModel;
        this.layoutInflater = LayoutInflater.from(ctx);


    }

    @Override
    public HPViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.list_helpplace, viewGroup, false);
        HPViewHolder Holder = new HPViewHolder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(HPViewHolder Holder, int i) {
        PlaceModel model = mPlaceModel.get(i);
        Holder.setData(model, i);
    }

    @Override
    public int getItemCount() {
        return mPlaceModel.size();
    }

    public void setFilter(List<PlaceModel> placeModels) {
        mPlaceModel = new ArrayList<>();
        mPlaceModel.addAll(placeModels);
        notifyDataSetChanged();
    }



}
