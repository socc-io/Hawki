package com.socc.Hawki.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.socc.Hawki.app.R;
import com.socc.Hawki.app.service.HawkAPI;
import com.socc.Hawki.app.service.SingleTonBuildingInfo;
import com.socc.Hawki.app.service.response.GetBuildingInfoRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by young on 2017-06-02.
 */


public class BuildingSearchActivity extends AppCompatActivity {

    public static GetBuildingInfoRes selectedRecvData;

    private EditText buildingNameEdit;
    private Button getBuildInfoButton;
    private ListView listView;
    private List<HashMap<String, String>> buildList;
    private List<GetBuildingInfoRes> recvBuildingData = new ArrayList<>();
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_search);

        listView = (ListView) findViewById(R.id.listView_building);
        buildingNameEdit = (EditText) findViewById(R.id.nameEdit);
        getBuildInfoButton = (Button) findViewById(R.id.requestBuild);
        buildList = new ArrayList<>();

        Intent intent = getIntent();
        type = intent.getStringExtra("TYPE");

        getBuildInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String buildName = buildingNameEdit.getText().toString();
                if (buildName.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "건물 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                HawkAPI api = HawkAPI.getInstance(); // get API Instance
                recvBuildingData = api.getBuildingInfo(buildName); // fetch data
                if (recvBuildingData == null) {
                    Toast.makeText(getApplicationContext(), "실패했습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                buildList.clear();
                for (GetBuildingInfoRes data : recvBuildingData) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("name", data.getTitle());
                    hm.put("address", data.getAddress());
                    hm.put("phone", data.getPhone());
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


