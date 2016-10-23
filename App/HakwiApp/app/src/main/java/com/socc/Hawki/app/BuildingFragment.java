package com.socc.Hawki.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.DataSource;
import data.Json;


public class BuildingFragment extends Fragment {
    private String TAG = BuildingFragment.class.getSimpleName();

    View rootView;
    EditText editText;
    Button inputButton;
    EditText editTextName, editTextId;

    private ProgressDialog pDialog;
    public static Marker selectedMarker;

    private ListView listView;
    ArrayList<HashMap<String, String>> buildList;
    List<Marker> markers = new ArrayList<>();

    public static Marker getInstance() {
        return selectedMarker;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_building, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editTextName = (EditText)getActivity().findViewById(R.id.editText_buildingName);
        editTextId = (EditText)getActivity().findViewById(R.id.editText_buildingId);

        inputButton = (Button) rootView.findViewById(R.id.requestBuild);
        editText = (EditText) rootView.findViewById(R.id.nameEdit);
        buildList = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.list);

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String buildName = editText.getText().toString();
                String makeURL = DataSource.createRequestURL(DataSource.DATAFORMAT.BuildingInfo, 0, 0, 0, 0, buildName);
                new GetContacts().execute(makeURL, "GET");

            }

        });
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
        {

            String selectedBuildId;
            String selectedBuildName;
            String toastMessage;

            selectedMarker = markers.get(pos);
            selectedBuildId = selectedMarker.getBuildId();
            selectedBuildName = selectedMarker.getTitle();

            Toast.makeText(
                    getActivity(),
                    selectedBuildId,
                    Toast.LENGTH_SHORT
            ).show();

            editTextName.setText(selectedBuildName);
            editTextId.setText(selectedBuildId);

        }
    };


    private class GetContacts extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("로딩 중입니다...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();
            String url = params[0];
            //String method = params[1]; 일단 지금은 필요없을듯!
            Json layer = new Json();

            String jsonStr = sh.httpGet(url);//get

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    String convertStr = Json.convertStandardJSONString(jsonStr);
                    JSONObject jsonObj = new JSONObject(convertStr);
                    markers = layer.load(jsonObj, DataSource.DATAFORMAT.BuildingInfo);

                    // Getting JSON Array node
                    //JSONArray contacts = jsonObj.getJSONArray("Build");
                    buildList.clear();

                    // looping through All Contacts
                    for (int i = 0; i < markers.size(); i++) {

                        BuildingMarker buildMarkerTemp = (BuildingMarker) markers.get(i);

                        // tmp hash map for single contact
                        HashMap<String, String> build = new HashMap<>();
                        build.put("name", buildMarkerTemp.getTitle());
                        build.put("address", buildMarkerTemp.getAddress());
                        build.put("phone", buildMarkerTemp.getPhoneNumber());

                        buildList.add(build);
                        // adding contact to contact list

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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), buildList,
                    R.layout.list_item, new String[]{"name", "address",
                    "phone"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});


            listView.setAdapter(adapter);
            listView.setOnItemClickListener(itemClickListener);
        }

    }





}