package becxer.indoorposition.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import becxer.indoorposition.R;
import becxer.indoorposition.collector.IPcard;
import becxer.indoorposition.collector.IPcardCollector;
import becxer.indoorposition.utils.FileReadWriter;

public class CollectorActivity extends AppCompatActivity {

    ListView listview_ipcard = null;
    ArrayList<IPcardCollector> ipcard_collector_list = null;

    Button btn_clear = null;
    Button btn_load = null;
    Button btn_save = null;
    Button btn_add_new = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);
        ipcard_collector_list = new ArrayList<>();
        listview_ipcard = (ListView)findViewById(R.id.listview_ipcard);
        listview_ipcard.setAdapter(
                new CollectorArrayAdapter(
                        this,R.layout.activity_collector_row,ipcard_collector_list));

        btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipcard_collector_list.clear();
                listview_ipcard.deferNotifyDataSetChanged();
            }
        });

        btn_load = (Button)findViewById(R.id.btn_load);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        File sel = new File(dir, filename);
                        return filename.contains(".dat") && sel.isFile();
                    }

                };
                final String[] filelist = FileReadWriter.checkExist("ipcard",null).list(filter);
                AlertDialog.Builder alert = new AlertDialog.Builder(CollectorActivity.this);
                alert.setTitle("Load from file");
                alert.setItems(filelist, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String chosenFile = filelist[which];
                        String content = FileReadWriter.readContent("ipcard", chosenFile);
                        String[] splitted_content = content.split("\n");

                        String label = "";
                        IPcardCollector iPcardCollector = null;

                        for(String ipcard_str : splitted_content){
                            try {
                                JSONObject card_data = new JSONObject(ipcard_str);
                                IPcard iPcard = new IPcard("");
                                iPcard.card_data = card_data;
                                if(!label.equals(iPcard.get_ip_id())){
                                    iPcardCollector = new IPcardCollector(CollectorActivity.this);
                                    iPcardCollector.setIPcardLabel(iPcard.get_ip_id());
                                }
                                iPcardCollector.addIPcard(iPcard);
                            }catch (Exception e){
                                Log.d("load", e.toString());
                            }
                        }

                    }
                });
                alert.show();
            }
        });

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CollectorActivity.this);
                alert.setTitle("Save to file");
                final EditText input = new EditText(CollectorActivity.this); input.setHint("Azone");
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String filename = input.getText().toString() + ".dat";
                        String content = "";
                        for(IPcardCollector iPcardCollector : ipcard_collector_list){
                            for(IPcard ipcard : iPcardCollector.getIPcardList()){
                                content += ipcard.toString() + "\n";
                            }
                        }
                        FileReadWriter.writeContent("ipcard",filename,content);
                    }
                });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {}});
                alert.show();
            }
        });

        btn_add_new = (Button)findViewById(R.id.btn_add_new);
        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CollectorActivity.this);
                alert.setTitle("Add New Collector");
                final EditText input = new EditText(CollectorActivity.this); input.setHint("Azone");
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String label = input.getText().toString();
                        IPcardCollector iPcardCollector = new IPcardCollector(CollectorActivity.this);
                        iPcardCollector.setIPcardLabel(label);
                        ipcard_collector_list.add(iPcardCollector);
                    }
                });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {}});
                alert.show();
            }
        });

    }
}
