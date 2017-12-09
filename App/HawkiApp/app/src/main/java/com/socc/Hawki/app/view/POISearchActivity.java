package com.socc.Hawki.app.view;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ImageButton;
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

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

/**
 * Created by jinwon on 2017-12-09.
 */

public class POISearchActivity extends AppCompatActivity {

    public static Build selectedRecvData;

    private EditText poiNameEdit;
    private ImageButton getPoiListButton;
    private ListView listView;
    private List<HashMap<String, String>> poiList;
    private List<Build> recvPoiData = new ArrayList<>();
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);

        listView = (ListView) findViewById(R.id.listView_poi);
        poiNameEdit = (EditText) findViewById(R.id.nameEdit);
        getPoiListButton = (ImageButton) findViewById(R.id.requestPoi);
        poiList = new ArrayList<>();

        Intent intent = getIntent();
        type = intent.getStringExtra("TYPE");

        getPoiListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String poiName = poiNameEdit.getText().toString();
                if (poiName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "실내 장소명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
                Call<GetBuildingInfoRes> call = httpService.getBuildingInfo(poiName);
                call.enqueue(new Callback<GetBuildingInfoRes>() {
                    @Override
                    public void onResponse(Call<GetBuildingInfoRes> call, Response<GetBuildingInfoRes> response) {
                        GetBuildingInfoRes res = response.body();
                        recvPoiData = res.getBuildList();

                        poiList.clear();
                        for (Build data : recvPoiData) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("name", data.getTitle());
                            hm.put("address", data.getAddress());
                            hm.put("phone", data.getPhone());

                            Log.d("SUNO", data.getTitle() + "," + data.getAddress() + "," + data.getPhone());
                            poiList.add(hm);
                        }

                        ListAdapter adapter = new SimpleAdapter(
                                getApplicationContext(), poiList,
                                R.layout.list_item, new String[]{"name", "address",
                                "phone"}, new int[]{R.id.name,
                                R.id.email, R.id.mobile});

                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(itemClickListener);
                    }

                    @Override
                    public void onFailure(Call<GetBuildingInfoRes> call, Throwable t) {
                        Toast.makeText(POISearchActivity.this, "건물 검색 실패 :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });

            }

        });
    }

    ///
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id) {
            selectedRecvData = recvPoiData.get(pos);
            SingleTonBuildingInfo.getInstance().setSelectedBuildId(selectedRecvData.getId());
            SingleTonBuildingInfo.getInstance().setSelectedBuildName(selectedRecvData.getTitle());
            Intent intent;

            if (type.equals("COLLECTOR")) {
                intent = new Intent(POISearchActivity.this, CollectorActivity.class);
            } else {
                intent = new Intent(POISearchActivity.this, FinderActivity.class);
            }
            startActivity(intent);

        }
    };
}

