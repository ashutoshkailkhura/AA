package com.example.android.aa.findhelp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.aa.R;
import com.example.android.aa.repo.Const;

import java.io.InputStream;
import java.net.URL;


public class HPViewHolder extends RecyclerView.ViewHolder {

    ImageView placeImage;
    TextView placename;
    int position;
    PlaceModel placeModel;
    private LruCache<Integer, Bitmap> imageCache;

    public HPViewHolder(View itemView) {
        super(itemView);
        placename = (TextView) itemView.findViewById(R.id.location_list_textview);
        placeImage = (ImageView) itemView.findViewById(R.id.location_list_imageview);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }

    public void setData(PlaceModel placeModel, int position) {
        this.placename.setText(placeModel.getPlacename());
        //this.placeImage.setImageBitmap(placeModel.getBitmap());
        this.position = position;
        this.placeModel = placeModel;

        Bitmap bitmap = imageCache.get(placeModel.hashCode());
        if (bitmap != null) {
            this.placeImage.setImageBitmap(placeModel.getBitmap());
        } else {
            FlowerAndView container = new FlowerAndView();
            container.placeModel = placeModel;
            container.view = itemView;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }

    }

    class FlowerAndView {
        public PlaceModel placeModel;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {

            FlowerAndView container = params[0];
            PlaceModel placeModel = container.placeModel;

            try {
                String Image_url = Const.IMAGE_BASE_URL_FIND_HELP + placeModel.getImage();
                InputStream in = (InputStream) new URL(Image_url).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                placeModel.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView result) {
            try {
                placeImage = (ImageView) result.view.findViewById(R.id.location_list_imageview);
                placeImage.setImageBitmap(result.bitmap);
                imageCache.put(result.placeModel.hashCode(), result.bitmap);
            } catch (Exception e) {
                placeImage.setImageResource(R.drawable.logo);
            }

        }

    }


}