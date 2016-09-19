package com.example.jeong.httpclient;

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
import android.widget.TextView;
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
    TextView textViewName, textViewId;

    private ProgressDialog pDialog;
    public static Marker selectedMarker;
    private String selectedBuildId;
    private String selectedBuildName;
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
        textViewName = (TextView)getActivity().findViewById(R.id.textView_buildingName);
        textViewId = (TextView)getActivity().findViewById(R.id.textView_buildingId);

        inputButton = (Button) rootView.findViewById(R.id.requestBuild);
        editText = (EditText) rootView.findViewById(R.id.nameEdit);
        buildList = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.list);

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2016. 9. 10. buildname 예외처리해야됨
                String buildName = editText.getText().toString();

                String makeURL = DataSource.createRequestURL(DataSource.DATAFORMAT.BuildingInfo, 0, 0, 0, 0, buildName);
                new GetContacts().execute(makeURL, "GET"); // 이러면 겟방식으로감

            }

        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
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
            String method = params[1];
            Json layer = new Json();

            // Making a request to url and getting response
            // TODO: 2016. 9. 17. 여기서  method 가 get 인지 post인지 makeserviceCall 에서 하는 행동 바꿔야 되지 않을가생각 
            String jsonStr = sh.httpGet(url);//get

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    // JSONObject jsonObj = new JSONObject(jsonStr);
                    jsonStr = "{\"Build\": [{\"address\": \"\\uc81c\\uc8fc\\ud2b9\\ubcc4\\uc790\\uce58\\ub3c4 \\uc81c\\uc8fc\\uc2dc \\uc601\\ud3c9\\ub3d9 2181\", \"id\": \"18059921\", \"phone\": \"02-6718-1501\", \"title\": \"\\uce74\\uce74\\uc624 \\uc2a4\\ud398\\uc774\\uc2a4\\ub2f7\\uc6d0\"}, {\"address\": \"\\uc81c\\uc8fc\\ud2b9\\ubcc4\\uc790\\uce58\\ub3c4 \\uc81c\\uc8fc\\uc2dc \\uc601\\ud3c9\\ub3d9 2184\", \"id\": \"22251293\", \"phone\": \"\", \"title\": \"\\uce74\\uce74\\uc624 \\uc2a4\\ud398\\uc774\\uc2a4\\ub2f7\\ud22c\"}, {\"address\": \"\\uacbd\\uae30 \\uc131\\ub0a8\\uc2dc \\ubd84\\ub2f9\\uad6c \\uc0bc\\ud3c9\\ub3d9 681 \\uc5d0\\uc774\\uce58\\uc2a4\\ud018\\uc5b4 N\\ub3d9 7\\uce35\", \"id\": \"18577297\", \"phone\": \"070-7492-1300\", \"title\": \"\\uce74\\uce74\\uc624 \\ud310\\uad50\\uc624\\ud53c\\uc2a4\"}, {\"address\": \"\\uc81c\\uc8fc\\ud2b9\\ubcc4\\uc790\\uce58\\ub3c4 \\uc81c\\uc8fc\\uc2dc \\uc601\\ud3c9\\ub3d9 2179-2\", \"id\": \"22251288\", \"phone\": \"064-727-5331\", \"title\": \"\\uce74\\uce74\\uc624 \\uc2a4\\ud398\\uc774\\uc2a4\\ub2f7\\ud0a4\\uc988\"}, {\"address\": \"\\uc81c\\uc8fc\\ud2b9\\ubcc4\\uc790\\uce58\\ub3c4 \\uc81c\\uc8fc\\uc2dc \\uc601\\ud3c9\\ub3d9 2274\", \"id\": \"22251292\", \"phone\": \"\", \"title\": \"\\uce74\\uce74\\uc624\\uc5b4\\ub9b0\\uc774\\uc9d1 \\uc8fc\\ucc28\\uc7a5\"}, {\"address\": \"\\uc81c\\uc8fc\\ud2b9\\ubcc4\\uc790\\uce58\\ub3c4 \\uc81c\\uc8fc\\uc2dc \\uc601\\ud3c9\\ub3d9 2179-2\", \"id\": \"22251290\", \"phone\": \"\", \"title\": \"\\uce74\\uce74\\uc624\\uc5b4\\ub9b0\\uc774\\uc9d1 \\ub180\\uc774\\ud130\"}, {\"address\": \"\\uc81c\\uc8fc\\ud2b9\\ubcc4\\uc790\\uce58\\ub3c4 \\uc81c\\uc8fc\\uc2dc \\uc5f0\\ub3d9 1371-4\", \"id\": \"12795506\", \"phone\": \"064-745-6689\", \"title\": \"\\uce74\\uce74\\uc624\"}, {\"address\": \"\\uc81c\\uc8fc\\ud2b9\\ubcc4\\uc790\\uce58\\ub3c4 \\uc81c\\uc8fc\\uc2dc \\uc77c\\ub3c41\\ub3d9 1454-2 2~3\\uce35\", \"id\": \"27539636\", \"phone\": \"064-755-0690\", \"title\": \"\\uce74\\uce74\\uc624\\uce69 \\uc81c\\uc8fc\\uc77c\\ub3c4\\uc9c0\\ud558\\uc0c1\\uac00\\uc810\"}, {\"address\": \"\\uc11c\\uc6b8 \\uc911\\uad6c \\uba85\\ub3d92\\uac00 51-3 3\\uce35\", \"id\": \"13010750\", \"phone\": \"02-3789-3102\", \"title\": \"\\uce74\\uce74\\uc624\\uadf8\\ub9b0\"}, {\"address\": \"\\uc11c\\uc6b8 \\ub9c8\\ud3ec\\uad6c \\uc11c\\uad50\\ub3d9 337-21\", \"id\": \"9492313\", \"phone\": \"02-3141-4663\", \"title\": \"\\uce74\\uce74\\uc624\\ubd04\"}, {\"address\": \"\\ubd80\\uc0b0 \\ud574\\uc6b4\\ub300\\uad6c \\uc6b0\\ub3d9 1514 \\uc13c\\ud140\\ub9ac\\ub354\\uc2a4\\ub9c8\\ud06c 2707\\ud638\", \"id\": \"11495307\", \"phone\": \"051-745-8787\", \"title\": \"\\uce74\\uce74\\uc624 \\ubd80\\uc0b0\\uacbd\\ub0a8\\uc13c\\ud130\"}, {\"address\": \"\\uc11c\\uc6b8 \\uc11c\\ucd08\\uad6c \\uc11c\\ucd08\\ub3d9 1305-7 \\uc720\\ucc3d\\ube4c\\ub529 1~3\\uce35\", \"id\": \"653245473\", \"phone\": \"02-6494-1100\", \"title\": \"\\uce74\\uce74\\uc624\\ud504\\ub80c\\uc988 \\uac15\\ub0a8\\ud50c\\ub798\\uadf8\\uc2ed\\uc2a4\\ud1a0\\uc5b4\"}, {\"address\": \"\\uc11c\\uc6b8 \\uac15\\ub0a8\\uad6c \\uc0bc\\uc131\\ub3d9 159 \\ucf54\\uc5d1\\uc2a4\\ubab0 \\uc9c0\\ud5582\\uce35 G209\\ud638\", \"id\": \"26338954\", \"phone\": \"02-6002-1880\", \"title\": \"\\uce74\\uce74\\uc624\\ud504\\ub80c\\uc988 \\ucf54\\uc5d1\\uc2a4\\uc810\"}, {\"address\": \"\\uc11c\\uc6b8 \\uc11c\\ub300\\ubb38\\uad6c \\ucc3d\\ucc9c\\ub3d9 30-33 \\ud604\\ub300\\ubc31\\ud654\\uc810 \\uc2e0\\ucd0c\\uc810 \\ubcf8\\uad00 \\uc9c0\\ud5582\\uce35\", \"id\": \"26029863\", \"phone\": \"02-3145-1722\", \"title\": \"\\uce74\\uce74\\uc624\\ud504\\ub80c\\uc988 \\ud604\\ub300\\ubc31\\ud654\\uc810\\uc2e0\\ucd0c\\uc810\"}, {\"address\": \"\\uad11\\uc8fc \\ubd81\\uad6c \\uc6a9\\ubd09\\ub3d9 1129-4 2\\uce35\", \"id\": \"11829864\", \"phone\": \"062-515-8096\", \"title\": \"\\uce74\\uce74\\uc624 \\ud638\\ub0a8\\uc81c\\uc8fc\\uc13c\\ud130\"}]}";
                    String temp = "안녕";
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    markers = layer.load(jsonObj, DataSource.DATAFORMAT.BuildingInfo);
                    System.out.println(markers.toString());

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

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
        {
        //TODO: itemClickListener 발생시 CollectorActivity의 textView값 update 필요!

            selectedMarker = markers.get(pos);
            selectedBuildId = markers.get(pos).getBuildId();
            selectedBuildName = markers.get(pos).getTitle();

            String toastMessage = selectedBuildId;

            Toast.makeText(
                    getActivity(),
                    toastMessage,
                    Toast.LENGTH_SHORT
            ).show();

            if(getActivity().getClass().getSimpleName().equals(CollectorActivity.class.getSimpleName())) {
                Log.d(TAG, "----COLLECTORACTICITY!-------");
            }else if(getActivity().getClass().getSimpleName().equals(LocalizationActivity.class.getSimpleName())){
                Log.d(TAG, "----LOCALIZATIONACTIVITY!!-------");
            }
            else{
                Log.d(TAG, "------ERROR!-------");
            }
            textViewName.setText(selectedBuildName);
            textViewId.setText(selectedBuildId);

        }
    };

}