package com.example.android.aa.tabshop.TabFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.aa.HttpManager;
import com.example.android.aa.R;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.CheckInternetConnection;
import com.example.android.aa.repo.Const;
import com.example.android.aa.repo.mClickListener;
import com.example.android.aa.repo.myRecycler;
import com.example.android.aa.tabshop.ShopAdapter;
import com.example.android.aa.tabshop.ShopDetail;
import com.example.android.aa.tabshop.ShopModel;
import com.example.android.aa.tabshop.ShopParser;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MedicineFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerview;
    private List<ShopModel> mShopModel;
    private ShopAdapter adapter;
    Activity context;
    private ArrayList<String> values;
    ProgressBar pb;
    ImageView empty;
    CheckInternetConnection isOnline;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.shop_medicine_fragment, container, false);
        context = getActivity();
        recyclerview = (RecyclerView) view.findViewById(R.id.shop_medicine_recyclerview);
        pb = (ProgressBar) view.findViewById(R.id.progressBar_shop_medicine);
        empty = (ImageView) view.findViewById(android.R.id.empty);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerview.setLayoutManager(gridLayoutManager);



        if (CheckInternetConnection.getInstance(context).isOnline()) {
            requestData(Const.SHOP_URL);
        } else {
            pb.setVisibility(View.INVISIBLE);
            empty.setVisibility(View.VISIBLE);
        }


        recyclerview.addOnItemTouchListener(new myRecycler(context, recyclerview, new mClickListener() {
            @Override
            public void mClick(View v, int position) {
                ShopModel shopModel = mShopModel.get(position);
                Intent intent = new Intent(context, ShopDetail.class);
                values = new ArrayList<>();
                values.add(String.valueOf(shopModel.getProductId()));
                values.add(shopModel.getName());
                values.add(String.valueOf(shopModel.getPrice()));
                values.add(shopModel.getPhoto());
                values.add(String.valueOf(shopModel.getQuantity()));
                intent.putStringArrayListExtra("detail", values);
                startActivity(intent);
            }
        }));
        return view;
    }

    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("category", "m");

        MyShopMedicineAsynk task = new MyShopMedicineAsynk();
        task.execute(p);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mShopModel = new ArrayList<>();
        adapter = new ShopAdapter(context, mShopModel);
        recyclerview.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shop_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //searchView.getBackground();
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        adapter.setFilter(mShopModel);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<ShopModel> filteredModelList = filter(mShopModel, newText);

        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<ShopModel> filter(List<ShopModel> models, String query) {
        query = query.toLowerCase();
        final List<ShopModel> filteredModelList = new ArrayList<>();
        for (ShopModel model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private class MyShopMedicineAsynk extends AsyncTask<RequestPackage, String, List<ShopModel>> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }


        @Override
        protected List<ShopModel> doInBackground(RequestPackage... params) {

            String content = HttpManager.getData(params[0]);
            mShopModel = ShopParser.parseFeed(content);

            for (ShopModel food : mShopModel) {
                try {
                    String imageUrl = Const.IMAGE_BASE_URL_SHOP_IMAGE + food.getPhoto();
                    InputStream in = (InputStream) new URL(imageUrl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    food.setBitmap(bitmap);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return mShopModel;
        }

        @Override
        protected void onPostExecute(List<ShopModel> result) {
            pb.setVisibility(View.INVISIBLE);
            if (result == null) {
                Toast.makeText(context, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }
            mShopModel = result;
            updateDisplay();

        }

    }

    protected void updateDisplay() {
        adapter = new ShopAdapter(context, mShopModel);
        recyclerview.setAdapter(adapter);

    }
}
