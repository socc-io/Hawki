package becxer.indoorposition.collector;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by becxer on 2016. 4. 2..
 */
public class IPcard {

    public static int WIFI_COUNT_THRESHOLD = 15;

    public JSONObject card_data = new JSONObject();
    /**
     *
     * Packet structure..
     *
     *  {
     *     "ip_id" : "room-A",
     *
     *     "wifi" : [
     *        {
     *          "bssid" : "00:23:22:32:0f:33",
     *          "rssi" : -81,
     *          "ssid" : ap_id
     *        }...
     *     ]
     *
     * }
     *
     */
    public IPcard(String ip_id){
        try{
            card_data.put("ip_id", ip_id);
            card_data.put("wifi", new JSONArray());
        }catch (Exception e){
            Log.d("IPcard", e.toString());
        }
    }

    public String toString(){
        String res = "";
        try{
            res += card_data.toString();
        }catch (Exception e){
            Log.d("IPcard", e.toString());
        }
        return res;
    }

    public void add_wifi(String bssid, int rssi, String ssid){
        try {
            JSONObject wifi_obj = new JSONObject();
            wifi_obj.put("bssid",bssid);
            wifi_obj.put("rssi",rssi);
            wifi_obj.put("ssid",ssid);
            JSONArray wifi_list = card_data.getJSONArray("wifi").put(wifi_obj);
            card_data.put("wifi",wifi_list);
        }catch (Exception e){
            Log.d("IPcard", e.toString());
        }
    }

    public int get_wifi_size(){
        int res = -1;
        try{
            JSONArray wifi_array = card_data.getJSONArray("wifi");
            res = wifi_array.length();
        }catch(Exception e){
            Log.d("IPcard", e.toString());
        }
        return res;
    }

    public String get_ip_id (){
        String res = "";
        try{
            res = card_data.getString("ip_id");
        }catch(Exception e){
            Log.d("IPcard", e.toString());
        }
        return res;
    }

    public boolean is_valid(){
        boolean res = true;
        try{
            if(card_data.getJSONArray("wifi").length() < WIFI_COUNT_THRESHOLD) {
                return false;
            }
        }catch(Exception e){
            Log.d("IPcard", e.toString());
        }
        return res;
    }
}
