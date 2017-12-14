package com.socc.Hawki.app.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.socc.Hawki.app.R;
import com.socc.Hawki.app.service.response.Poi;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kakaogames on 2017. 12. 14..
 */

public class POIViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textView_poi_name)
    TextView tvPoiName;
    @BindView(R.id.textView_poi_category)
    TextView tvPoiCategory;

    private Context context;

    public POIViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    public void bindView(Poi poi){
        tvPoiName.setText(poi.getName());
        tvPoiCategory.setText(poi.getCategory());
    }
}
