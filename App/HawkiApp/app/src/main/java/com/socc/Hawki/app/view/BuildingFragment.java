package com.socc.Hawki.app.view;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.socc.Hawki.app.R;
import com.socc.Hawki.app.service.HawkAPI;
import com.socc.Hawki.app.service.response.GetBuildingInfoRes;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BuildingFragment extends Fragment {

    private String TAG = BuildingFragment.class.getSimpleName();

    View rootView;
    EditText buildingNameEdit;
    Button getBuildInfoButton;
    TextView nameTextView, idTextView;
    ImageView mapView;

    private Bitmap mapViewBitmap;

    public static GetBuildingInfoRes selectedRecvData;

    private ListView listView;
    List<HashMap<String, String>> buildList;
    List<GetBuildingInfoRes> recvBuildingData = new ArrayList<>();

    public static GetBuildingInfoRes getInstance() {
        return selectedRecvData;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_building, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapView = (ImageView)rootView.findViewById(R.id.mapView);
        listView = (ListView)rootView.findViewById(R.id.listView_building);
        nameTextView = (TextView) getActivity().findViewById(R.id.editText_buildingName);
        idTextView = (TextView) getActivity().findViewById(R.id.editText_buildingId);
        buildingNameEdit = (EditText) rootView.findViewById(R.id.nameEdit);

        buildList = new ArrayList<>();

        getBuildInfoButton = (Button) rootView.findViewById(R.id.requestBuild);
        getBuildInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("hawki", "getBuildInfoButton Pressed!");

                mapView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                String buildName = buildingNameEdit.getText().toString();
                if(buildName.length() <= 0) {
                    Toast.makeText(getActivity(), "건물 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                HawkAPI api = HawkAPI.getInstance(); // get API Instance

                recvBuildingData = api.getBuildingInfo(buildName); // fetch data
                if(recvBuildingData == null) {
                    Toast.makeText(getActivity(), "실패했습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // convert List<BuildingData> to List<HashMap<String, String>>
                buildList.clear();
                for(GetBuildingInfoRes data: recvBuildingData) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("name", data.getTitle());
                    hm.put("address", data.getAddress());
                    hm.put("phone", data.getPhone());
                    buildList.add(hm);
                }

                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), buildList,
                        R.layout.list_item, new String[]{"name", "address",
                        "phone"}, new int[]{R.id.name,
                        R.id.email, R.id.mobile});

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(itemClickListener);

            }

        });
    }

        private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
            {
                selectedRecvData = recvBuildingData.get(pos);

                String selectedBuildId = selectedRecvData.getId();
                String selectedBuildName = selectedRecvData.getTitle();

                // apply data to TextView
                idTextView.setText(selectedBuildId);
                nameTextView.setText(selectedBuildName);

                Toast.makeText(
                        getActivity(),
                        selectedBuildId,
                        Toast.LENGTH_SHORT
                ).show();

                HawkAPI api = HawkAPI.getInstance();

                String mapImageUrl = api.getMapImageURL(selectedBuildId);

                Picasso.with(getActivity()).load(mapImageUrl).resize(mapView.getWidth(),mapView.getHeight()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mapViewBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        mapView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Bitmap noImagebitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.nomapimage);
                        mapView.setImageBitmap(noImagebitmap);
                        mapView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);

                        mapViewBitmap = noImagebitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Toast.makeText(getActivity(),"지도를 등록해주세요!!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        };

    public Bitmap getMapViewBitmap() {
        return mapViewBitmap;
    }

}







