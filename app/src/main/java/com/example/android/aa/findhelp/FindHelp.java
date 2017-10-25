package com.example.android.aa.findhelp;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.aa.HttpManager;
import com.example.android.aa.R;
import com.example.android.aa.RequestPackage;
import com.example.android.aa.repo.CheckInternetConnection;
import com.example.android.aa.repo.Const;
import com.example.android.aa.repo.mClickListener;
import com.example.android.aa.repo.myRecycler;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FindHelp extends AppCompatActivity {

    private ProgressBar pb;
    private List<PlaceModel> placeModelList;
    private ImageView empty;
    RecyclerView recyclerview;
    private ArrayList<String> values;
    private Toolbar toolbar;
    private LocationAdapter locationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_help);

        setUpToolBar();
        pb = (ProgressBar) findViewById(R.id.progressBar_findhelp);
        empty = (ImageView) findViewById(android.R.id.empty);
        recyclerview = (RecyclerView) findViewById(R.id.find_help_recycler);
        placeModelList = new ArrayList<>();
        locationAdapter = new LocationAdapter(placeModelList, this);
        recyclerview.setAdapter(locationAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.addOnItemTouchListener(new myRecycler(getApplicationContext(), recyclerview, new mClickListener() {
            @Override
            public void mClick(View v, int position) {
                PlaceModel placelistModel = placeModelList.get(position);
                Intent intent = new Intent(FindHelp.this, DetailFindHelpActivity.class);
                values = new ArrayList<>();
                values.add(placelistModel.getAddress());
                values.add(placelistModel.getPlacename());
                values.add(placelistModel.getImage());
                values.add(placelistModel.getMobilenum());
                values.add(String.valueOf(placelistModel.getLat()));
                values.add(String.valueOf(placelistModel.getLng()));
                values.add(placelistModel.getDiscription());
                intent.putStringArrayListExtra("detail", values);
                startActivity(intent);
            }
        }));
        if (CheckInternetConnection.getInstance(this).isOnline()) {
            requestData(Const.FIND_HELP);
        } else {
            pb.setVisibility(View.INVISIBLE);
            empty.setVisibility(View.VISIBLE);
        }

    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.findhelptoolbar);
        toolbar.setTitle("Find Help");
        toolbar.inflateMenu(R.menu.menu_help);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_help_seeall) {
                    startActivity(new Intent(FindHelp.this, ExploreAll.class));
                }
                if (item.getItemId() == R.id.menu_help_add) {
                    startActivity(new Intent(FindHelp.this, AddFindHelp.class));
                }
                if (item.getItemId() == R.id.menu_find_help) {

                    MenuItemCompat.setOnActionExpandListener(item,
                            new MenuItemCompat.OnActionExpandListener() {
                                @Override
                                public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                                    locationAdapter.setFilter(placeModelList);
                                    return true; // Return true to collapse action view
                                }

                                @Override
                                public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                                    return true; // Return true to expand action view
                                }
                            });

                    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                    //searchView.getBackground();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            final List<PlaceModel> filteredModelList = filter(placeModelList, newText);
                            locationAdapter.setFilter(filteredModelList);
                            return true;
                        }
                    });
                }
                if (item.getItemId() == R.id.menu_help_seeall) {
                    //startActivity(new Intent(this,SeeAllHelp.class));
                }
                return false;
            }
        });

    }


    private List<PlaceModel> filter(List<PlaceModel> models, String query) {
        query = query.toLowerCase();
        final List<PlaceModel> filteredModelList = new ArrayList<>();
        for (PlaceModel model : models) {
            final String text = model.getPlacename().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void requestData(String uri) {
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        AsyncFindHelp task = new AsyncFindHelp();
        task.execute(p);
    }

    private void updateDisplay() {
        LocationAdapter adapter = new LocationAdapter(placeModelList, this);
        recyclerview.setAdapter(adapter);
    }


    class AsyncFindHelp extends AsyncTask<RequestPackage, String, List<PlaceModel>> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);


        }

        @Override
        protected List<PlaceModel> doInBackground(RequestPackage... params) {


            String content = HttpManager.getData(params[0]);
            placeModelList = LocationJSONParser.parseFeed(content);

            return placeModelList;
        }

        @Override
        protected void onPostExecute(List<PlaceModel> result) {

            pb.setVisibility(View.GONE);
            updateDisplay();
        }

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }


}