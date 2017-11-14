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
 * Created by young on 2017-06-02.
 */


public class BuildingSearchActivity extends AppCompatActivity {

    public static Build selectedRecvData;

    private EditText buildingNameEdit;
    private ImageButton getBuildInfoButton;
    private ListView listView;
    private List<HashMap<String, String>> buildList;
    private List<Build> recvBuildingData = new ArrayList<>();
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_search);

        listView = (ListView) findViewById(R.id.listView_building);
        buildingNameEdit = (EditText) findViewById(R.id.nameEdit);
        getBuildInfoButton = (ImageButton) findViewById(R.id.requestBuild);
        buildList = new ArrayList<>();

        Intent intent = getIntent();
        type = intent.getStringExtra("TYPE");

        getBuildInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String buildName = buildingNameEdit.getText().toString();
                if (buildName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "건물 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpService httpService = HawkiApplication.getRetrofit().create(HttpService.class);
                Call<GetBuildingInfoRes> call = httpService.getBuildingInfo(buildName);
                call.enqueue(new Callback<GetBuildingInfoRes>() {
                    @Override
                    public void onResponse(Call<GetBuildingInfoRes> call, Response<GetBuildingInfoRes> response) {
                        GetBuildingInfoRes res = response.body();
                        recvBuildingData = res.getBuildList();

                        buildList.clear();
                        for (Build data : recvBuildingData) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("name", data.getTitle());
                            hm.put("address", data.getAddress());
                            hm.put("phone", data.getPhone());

                            Log.d("SUNO", data.getTitle() +","+data.getAddress()+","+data.getPhone());
                            buildList.add(hm);
                        }

                        ListAdapter adapter = new SimpleAdapter(
                                getApplicationContext(), buildList,
                                R.layout.list_item, new String[]{"name", "address",
                                "phone"}, new int[]{R.id.name,
                                R.id.email, R.id.mobile});

                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(itemClickListener);
                    }

                    @Override
                    public void onFailure(Call<GetBuildingInfoRes> call, Throwable t) {
                        Toast.makeText(BuildingSearchActivity.this, "건물 검색 실패 :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });

            }

        });
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id) {
            selectedRecvData = recvBuildingData.get(pos);
            SingleTonBuildingInfo.getInstance().setSelectedBuildId(selectedRecvData.getId());
            SingleTonBuildingInfo.getInstance().setSelectedBuildName(selectedRecvData.getTitle());
            Intent intent;

            if(type.equals("COLLECTOR")) {
                intent = new Intent(BuildingSearchActivity.this, CollectorActivity.class);
            }

            else {
                intent = new Intent(BuildingSearchActivity.this, FinderActivity.class);
            }
            startActivity(intent);

        }
    };

}


