package com.cremcash.eloan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    Context context;
    List<ImageModel> listdata;

    public ImageAdapter(Context context, List<ImageModel> listdata){
        this.context = context;
        this.listdata = listdata;
    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item_layout, null);
        }

        ImageView ivImage = view.findViewById(R.id.grid_item_image);

        byte[] imageBytes = Base64.decode(listdata.get(i).getImage(), Base64.DEFAULT);
        Bitmap decImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        ivImage.setImageBitmap(decImage);

        return view;
    }
}
