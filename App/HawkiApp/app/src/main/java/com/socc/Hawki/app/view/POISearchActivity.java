package com.socc.Hawki.app.view;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.socc.Hawki.app.R;
        import com.socc.Hawki.app.application.HawkiApplication;
        import com.socc.Hawki.app.listener.OnPoiClickListener;
        import com.socc.Hawki.app.service.SingleTonBuildingInfo;
        import com.socc.Hawki.app.service.network.HttpService;
        import com.socc.Hawki.app.service.response.Build;
        import com.socc.Hawki.app.service.response.GetBuildingInfoRes;
        import com.socc.Hawki.app.service.response.GetPoiListReq;
        import com.socc.Hawki.app.service.response.Poi;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

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
    @BindView(R.id.editText_search)
    EditText etSearch;

    private POIAdapter adapter;
    private List<Poi> itemList;
    private Map<Integer, String> idToCategory = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        ButterKnife.bind(this);

        idToCategory.put(R.id.imgBtn_beauty, "화장품");
        idToCategory.put(R.id.imgBtn_cafe, "카페");
        idToCategory.put(R.id.imgBtn_fashion, "옷집");
        idToCategory.put(R.id.imgBtn_food, "음식점");
        idToCategory.put(R.id.imgBtn_toilet, "화장실");
        idToCategory.put(R.id.imgBtn_etc, "편의");

        initPOIDataList();
    }

    private void initPOIDataList() {
        HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
        Call<GetPoiListReq> poiListReqCall = httpService.getPOIList("21160803");
        poiListReqCall.enqueue(new Callback<GetPoiListReq>() {
            @Override
            public void onResponse(Call<GetPoiListReq> call, Response<GetPoiListReq> response) {
                if (response.isSuccessful()) {
                    itemList = response.body().getPois();
                    initRecyclerView();
                    Log.d("itemList", itemList.toString());
                }
            }

            @Override
            public void onFailure(Call<GetPoiListReq> call, Throwable t) {

            }
        });
    }

    public void setResultToActivity(Poi poi){
        Intent intent = new Intent();
        ArrayList<Poi> list = new ArrayList<>();
        list.add(poi);
        intent.putExtra("poiList", list);
        setResult(POISearchActivity.RESULT_OK, intent);
        finish();
    }

    public void setResultToActivity(ArrayList<Poi> poiList){
        Intent intent = new Intent();

        intent.putExtra("poiList", poiList);
        setResult(POISearchActivity.RESULT_OK, intent);
        finish();
    }

    public void initRecyclerView(){
        poiRcv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        poiRcv.setLayoutManager(layoutManager);

        adapter = new POIAdapter(this, itemList);
        poiRcv.setAdapter(adapter);
    }

    @OnClick({R.id.imgBtn_beauty, R.id.imgBtn_cafe, R.id.imgBtn_fashion, R.id.imgBtn_food, R.id.imgBtn_toilet})
    public void categoryBtnClick(View v){
        String category = idToCategory.get(v.getId());
        ArrayList<Poi> substitute = new ArrayList<>();
        for(Poi poi : itemList) {
            if(poi.getCategory().equals(category)) {
                substitute.add(poi);
            }
        }

        adapter.changeList(substitute);
        setResultToActivity(substitute);
    }

    @OnClick(R.id.button_search)
    public void searchBtnClick(View v){
        String input = etSearch.getText().toString();
        etSearch.setText("");
        ArrayList<Poi> substitute = new ArrayList<>();
        for(Poi poi : itemList) {

            if(poi.getName().equals(input)) {
                substitute.add(poi);
            }
        }
        adapter.changeList(substitute);
        //setResultToActivity(substitute);
    }

}

