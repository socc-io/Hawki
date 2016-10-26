package com.socc.Hawki.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.provider.SyncStateContract;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

import DataPacket.DataSource;
import DataPacket.Json;


public class BuildingFragment extends Fragment {

    private String TAG = BuildingFragment.class.getSimpleName();

    View rootView;
    EditText editText;
    Button inputButton;
    TextView editTextName, editTextId;
    ImageView mapView;

    private ProgressDialog pDialog;
    public static Data selectedData;

    private ListView listView;
    ArrayList<HashMap<String, String>> buildList;
    List<Data> datas = new ArrayList<>();

    public static Data getInstance() {
        return selectedData;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_building, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editTextName = (TextView)getActivity().findViewById(R.id.editText_buildingName);
        editTextId = (TextView)getActivity().findViewById(R.id.editText_buildingId);

        inputButton = (Button) rootView.findViewById(R.id.requestBuild);
        editText = (EditText) rootView.findViewById(R.id.nameEdit);
        buildList = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.list);
        mapView = (ImageView) rootView.findViewById(R.id.mapView);


        final Json layer = new Json();

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String buildName = editText.getText().toString();
                String requestBuildURL = DataSource.createRequestURL(DataSource.DATAFORMAT.BuildingInfo, 0, 0, 0, 0, buildName);
                try {

                    String result = new HttpHandler().execute(requestBuildURL,"GET").get();

                    if (result != null) {
                        try {

                            String convertStr = Json.convertStandardJSONString(result);
                            JSONObject jsonObj = new JSONObject(convertStr);
                            datas = layer.load(jsonObj, DataSource.DATAFORMAT.BuildingInfo);
                            buildList.clear();

                            for (int i = 0; i < datas.size(); i++) {

                                BuildingData buildMarker = (BuildingData) datas.get(i);

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


                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), buildList,
                        R.layout.list_item, new String[]{"name", "address",
                        "phone"}, new int[]{R.id.name,
                        R.id.email, R.id.mobile});

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(itemClickListener);

                listView.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);

            }

        });
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
        {

            String selectedBuildId;
            String selectedBuildName;

            selectedData = datas.get(pos);
            selectedBuildId = selectedData.getBuildId();
            selectedBuildName = selectedData.getTitle();

            Toast.makeText(
                    getActivity(),
                    selectedBuildId,
                    Toast.LENGTH_SHORT
            ).show();

            editTextName.setText(selectedBuildName);
            editTextId.setText(selectedBuildId);


        }
    };
}





