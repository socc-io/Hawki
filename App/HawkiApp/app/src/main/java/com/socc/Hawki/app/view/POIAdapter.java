package com.socc.Hawki.app.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.socc.Hawki.app.R;
import com.socc.Hawki.app.listener.OnPoiClickListener;
import com.socc.Hawki.app.service.response.Poi;

import java.util.List;

/**
 * Created by kakaogames on 2017. 12. 14..
 */

public class POIAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnPoiClickListener{
    private Context context;
    private List<Poi> itemList;

    public POIAdapter(Context context, List<Poi> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        POIViewHolder vh = new POIViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi, parent, false), context);

        vh.setOnItemClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((POIViewHolder)holder).bindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void changeList(List<Poi> list){
        itemList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Poi poi = itemList.get(position);
        Log.i("OnItemClick", poi.toString());
        ((POISearchActivity)context).setResultToActivity(poi);
    }
}
