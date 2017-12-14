package com.socc.Hawki.app.view;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.Toast;

        import com.socc.Hawki.app.R;
        import com.socc.Hawki.app.application.HawkiApplication;
        import com.socc.Hawki.app.service.SingleTonBuildingInfo;
        import com.socc.Hawki.app.service.network.HttpService;
        import com.socc.Hawki.app.service.response.Build;
        import com.socc.Hawki.app.service.response.GetBuildingInfoRes;
        import com.socc.Hawki.app.service.response.GetPoiListReq;
        import com.socc.Hawki.app.service.response.Poi;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.OnClick;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

/**
 * Created by jinwon on 2017-12-09.
 */

public class POISearchActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView poiRcv;

    private POIAdapter adapeter;
    private List<Poi> itemList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        ButterKnife.bind(this);

        initPOIDataList();
        initRecyclerView();
    }

    private void initPOIDataList() {

        HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
        Call<GetPoiListReq> poiListReqCall = httpService.getPOIList(SingleTonBuildingInfo.getInstance().getSelectedBuildId());
        poiListReqCall.enqueue(new Callback<GetPoiListReq>() {
            @Override
            public void onResponse(Call<GetPoiListReq> call, Response<GetPoiListReq> response) {
                if (response.isSuccessful()) {
                    itemList = response.body().getPois();
                    Log.d("itemList", itemList.toString());
                }
            }

            @Override
            public void onFailure(Call<GetPoiListReq> call, Throwable t) {

            }
        });
    }

    public void initRecyclerView(){
        poiRcv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        poiRcv.setLayoutManager(layoutManager);

        adapeter = new POIAdapter(this, itemList);
        poiRcv.setAdapter(adapeter);
    }

    @OnClick({R.id.imgBtn_beauty, R.id.imgBtn_cafe})
    public void categoryBtnClick(View v){
        switch (v.getId()){
            case R.id.imgBtn_beauty:
                Log.d("POISearchActivity", "imgBtn_beauty");
                break;
            case R.id.imgBtn_cafe:
                Log.d("POISearchActivity", "imgBtn_cafe");
                break;
        }
    }
}

