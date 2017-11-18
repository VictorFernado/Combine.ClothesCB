package com.example.acessolivre.combineclothes.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.acessolivre.combineclothes.R;
import com.example.acessolivre.combineclothes.model.Photo;
import com.example.acessolivre.combineclothes.network.VolleySingleton;

import java.util.List;

/**
 * Created by jandersonlemos on 16/11/17.
 */

public class PhotoAdapter extends BaseAdapter{
    private Context context;
    private List<Photo> list;

    public PhotoAdapter(Context context, List<Photo> photos){
        this.context = context;
        this.list = photos;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
        NetworkImageView avatar = (NetworkImageView) view.findViewById(R.id.niv);
        avatar.setImageUrl(list.get(position).getUrlImage(), VolleySingleton.getInstance().getImageLoader());
        ((TextView) view.findViewById(R.id.tx_nota_media)).setText(list.get(position).getNota().toString());
        return view;
    }


}
