package becxer.indoorposition.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import becxer.indoorposition.R;
import becxer.indoorposition.collector.IPcard;
import becxer.indoorposition.collector.IPcardCollector;

/**
 * Created by becxer on 2016. 4. 2..
 */
public class CollectorArrayAdapter extends ArrayAdapter<IPcardCollector> {

    ArrayList<IPcardCollector> list;
    Context context;

    public CollectorArrayAdapter(Context context, int resource, ArrayList<IPcardCollector> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.activity_collector_row, null);
        }
        final IPcardCollector ipcardCollector = list.get(position);

        if(ipcardCollector != null){
            TextView tv_ipcard_label = (TextView) v.findViewById(R.id.tv_ipcard_label);
            TextView tv_ipcard_sample = (TextView) v.findViewById(R.id.tv_ipcard_sample);
            final Button btn_toggle_collect = (Button) v.findViewById(R.id.btn_toggle_collect);
            final Button btn_delete = (Button) v.findViewById(R.id.btn_delete);

            final ArrayList<IPcard> ipcardList = ipcardCollector.getIPcardList();
            if(ipcardList.size() > 0) {
                IPcard ipcard_last = ipcardList.get(ipcardList.size() - 1);
                tv_ipcard_label.setText(ipcard_last.get_ip_id());
                tv_ipcard_sample.setText(ipcard_last.get_wifi_size());
            }
            btn_toggle_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ipcardCollector.isNowCollecting()){
                        ipcardCollector.startCollect(ipcardList);
                        btn_toggle_collect.setText("STOP");
                    }else {
                        ipcardCollector.stopCollect();
                        btn_toggle_collect.setText("START");
                    }
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ipcardCollector.stopCollect();
                    list.remove(ipcardCollector);
                }
            });

        }
        return v;
    }
}
