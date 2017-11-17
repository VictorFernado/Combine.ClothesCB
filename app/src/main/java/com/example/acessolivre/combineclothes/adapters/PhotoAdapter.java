package com.example.acessolivre.combineclothes.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.acessolivre.combineclothes.model.Photo;

import java.util.List;

/**
 * Created by jandersonlemos on 16/11/17.
 */

public class PhotoAdapter extends ArrayAdapter<Photo> {
    private List<Photo> list;
    public PhotoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Photo> photos) {
        super(context, resource, photos);
        this.list = photos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ((TextView)view.findViewById(android.R.id.text1)).setText(list.get(position).getPostDate().toString());
        return view;
    }
}
