package becxer.indoorposition.collector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.net.wifi.WifiManager.*;

/**
 * Created by becxer on 2016. 4. 2..
 */
public class IPcardCollector {
    Context context;
    WifiManager wm;

    String collecting_ipcard_label;
    ArrayList<IPcard> collecting_ipcard_list = new ArrayList<>();
    boolean now_collecting = false;
    IPcardCollectorListener iPcardCollectorListener;

    public void startCollect(ArrayList<IPcard> ipcard_list){
        if(now_collecting) {
            Log.d("IPcardCollector", "Now in collecting");
            return;
        }

        this.collecting_ipcard_list = ipcard_list;
        this.now_collecting = true;
        wm.startScan();
    }

    public void stopCollect(){
        now_collecting = false;
    }
    public boolean isNowCollecting(){
        return now_collecting;
    }
    public ArrayList<IPcard> getIPcardList(){ return collecting_ipcard_list;}
    public void setIPcardLabel(String label){ this.collecting_ipcard_label = label;}
    public void addIPcard(IPcard ipcard){ collecting_ipcard_list.add(ipcard);}

    public IPcardCollector(Context context){
        this.context = context;
        wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        final IntentFilter filter = new IntentFilter(SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(wifiReceiver, filter);
        Log.d("IPcardCollector", "Setup WIfiManager getSystemService");
        if (wm.isWifiEnabled() == false)
            wm.setWifiEnabled(true);
    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(SCAN_RESULTS_AVAILABLE_ACTION)) {
                makeIPcard(wm.getScanResults());
                if (now_collecting) wm.startScan(); // refresh wifi manager
            } else if (action.equals(NETWORK_STATE_CHANGED_ACTION)) {
                context.sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
            }
        }
    };

    private void makeIPcard(List apList) {
        if (apList != null) {
            int size = apList.size();
            IPcard ipcard = new IPcard(collecting_ipcard_label);
            for (int i = 0; i < size; i++) {
                ScanResult scanResult = (ScanResult) apList.get(i);
                ipcard.add_wifi(scanResult.BSSID, scanResult.level, scanResult.SSID);
            }
            if(ipcard.is_valid()){
                addIPcard(ipcard);
            }
        }
    }

    public void setOnIPcardCollectorListener(IPcardCollectorListener ipcl){
        this.iPcardCollectorListener = ipcl;
    }

    public interface IPcardCollectorListener{
        public void onCollect(IPcard ipcard);
    }
}
