package com.example.android.aa.tabshop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.aa.R;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<ShopModel> mShopModel;
    private LayoutInflater layoutInflater;

    public ShopAdapter(Context context, List<ShopModel> mShopModel) {
        this.mShopModel = mShopModel;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        final ShopModel model = mShopModel.get(i);
        itemViewHolder.setData(model, i);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.list_shop, viewGroup, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public int getItemCount() {
        return mShopModel.size();
    }

    public void setFilter(List<ShopModel> countryModels) {
        mShopModel = new ArrayList<>();
        mShopModel.addAll(countryModels);
        notifyDataSetChanged();
    }
}