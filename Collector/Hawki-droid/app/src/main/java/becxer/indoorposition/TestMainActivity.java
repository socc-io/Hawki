package becxer.indoorposition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import becxer.indoorposition.activity.CollectorActivity;

public class TestMainActivity extends AppCompatActivity {

    TextView tv_predict_result = null;

    Button btn_goto_collecting = null;
    Button btn_fitting = null;
    Button btn_predict = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        tv_predict_result = (TextView)findViewById(R.id.tv_predict_result);

        btn_goto_collecting = (Button) findViewById(R.id.btn_goto_collecting);
        btn_goto_collecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMainActivity.this, CollectorActivity.class);
                startActivity(intent);
            }
        });

        btn_fitting = (Button) findViewById(R.id.btn_fitting);
        btn_fitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Implement fitting predictor
            }
        });

        btn_predict = (Button) findViewById(R.id.btn_predict);
        btn_predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Implement predicting
            }
        });
    }
}