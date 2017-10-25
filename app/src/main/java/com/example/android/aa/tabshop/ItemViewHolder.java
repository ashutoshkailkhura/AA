package com.example.android.aa.tabshop;

import android.content.Intent;
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

public class ItemViewHolder extends RecyclerView.ViewHolder {

    ImageView productImage;
    TextView productName, productPrise;
    int position;
    ShopModel shopModel;
    private LruCache<Integer, Bitmap> imageCache;

    public ItemViewHolder(View itemView) {
        super(itemView);

        productName = (TextView) itemView.findViewById(R.id.list_shop_name);
        productPrise = (TextView) itemView.findViewById(R.id.list_shop_prise);
        productImage = (ImageView) itemView.findViewById(R.id.list_shop_imageview);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }

    public void setData(ShopModel shopModel, int position) {
        this.productName.setText(shopModel.getName());
        this.productPrise.setText(String.valueOf(shopModel.getPrice()));
        //this.productImage.setImageBitmap(shopModel.getBitmap());
        this.position = position;
        this.shopModel = shopModel;

        Bitmap bitmap = imageCache.get(shopModel.hashCode());
        if (bitmap != null) {
            this.productImage.setImageBitmap(shopModel.getBitmap());
        } else {
            FlowerAndView container = new FlowerAndView();
            container.shopModel = shopModel;
            container.view = itemView;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }

    }

    class FlowerAndView {
        public ShopModel shopModel;
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
            ShopModel shopModel = container.shopModel;

            try {
                String Image_url = Const.IMAGE_BASE_URL_SHOP_IMAGE + shopModel.getPhoto();
                InputStream in = (InputStream) new URL(Image_url).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                shopModel.setBitmap(bitmap);
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
                productImage = (ImageView) result.view.findViewById(R.id.list_shop_imageview);
                productImage.setImageBitmap(result.bitmap);
                imageCache.put(result.shopModel.hashCode(), result.bitmap);
            } catch (Exception e) {
                productImage.setImageResource(R.drawable.logo);
            }

        }

    }


}