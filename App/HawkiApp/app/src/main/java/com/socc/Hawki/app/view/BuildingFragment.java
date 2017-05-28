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

import com.socc.Hawki.app.DataPacket.Json;
import com.socc.Hawki.app.R;
import com.socc.Hawki.app.model.BuildingData;
import com.socc.Hawki.app.model.RecvData;
import com.socc.Hawki.app.util.HttpHandler;
import com.socc.Hawki.app.util.URLMaker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class BuildingFragment extends Fragment {

    private String TAG = BuildingFragment.class.getSimpleName();

    View rootView;
    EditText editText;
    Button getBuildInfoButton;
    TextView nameTextView, idTextView;
    ImageView mapView;

    private Bitmap mapViewBitmap;

    public static RecvData selectedRecvData;

    private ListView listView;
    ArrayList<HashMap<String, String>> buildList;
    List<RecvData> recvDatas = new ArrayList<>();

    public static RecvData getInstance() {
        return selectedRecvData;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_building, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nameTextView = (TextView) getActivity().findViewById(R.id.editText_buildingName);
        idTextView = (TextView) getActivity().findViewById(R.id.editText_buildingId);
        editText = (EditText) rootView.findViewById(R.id.nameEdit);

        getBuildInfoButton = (Button) rootView.findViewById(R.id.requestBuild);
        getBuildInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mapView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                String buildName = editText.getText().toString();
                String requestBuildURL = URLMaker.createRequestURL(URLMaker.DATAFORMAT.BuildingInfo, 0, 0, 0, 0, buildName);

                final Json layer = new Json();
                try {
                    String result = new HttpHandler().execute(requestBuildURL, "GET").get();

                    if (result != null) {
                        try {
                            String convertStr = Json.convertStandardJSONString(result);
                            JSONObject jsonObj = new JSONObject(convertStr);
                            recvDatas = layer.load(jsonObj, URLMaker.DATAFORMAT.BuildingInfo);
                            buildList.clear();

                            for (int i = 0; i < recvDatas.size(); i++) {

                                BuildingData buildMarker = (BuildingData) recvDatas.get(i);

                                HashMap<String, String> build = new HashMap<>();
                                build.put("name", buildMarker.getTitle());
                                build.put("address", buildMarker.getAddress());
                                build.put("phone", buildMarker.getPhoneNumber());
                                buildList.add(build);

                            }
                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }
                    } else {
                        Log.e(TAG, "Couldn't get json from server.");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                listView = (ListView) rootView.findViewById(R.id.listView_building);
                buildList = new ArrayList<>();

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
                String selectedBuildId;
                String selectedBuildName;

                selectedRecvData = recvDatas.get(pos);
                selectedBuildId = selectedRecvData.getBuildId();
                selectedBuildName = selectedRecvData.getTitle();

                idTextView.setText(selectedBuildId);
                nameTextView.setText(selectedBuildName);

                Toast.makeText(
                        getActivity(),
                        selectedBuildId,
                        Toast.LENGTH_SHORT
                ).show();

                String mapImageUrl = "http://beaver.hp100.net:4000/static/map/" + selectedBuildId + ".jpg";

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







