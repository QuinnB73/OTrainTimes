package com.quinnbudan.otraintimesv2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.quinnbudan.otraintimesv2.R;

/**
 * Created by quinnbudan on 2016-12-28.
 */
public class StationActivity extends AppCompatActivity {
    private static final String TAG = "StationActivity";
    private TextView stationText;
    private TableLayout table;
    private Button backButton;

    /*
        Callback for the creation of the activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        // get views
        stationText = (TextView)findViewById(R.id.stationName);
        table = (TableLayout)findViewById(R.id.fullSchedule);
        backButton = (Button)findViewById(R.id.backButton);

        // get STATION and DIRECTION property from intent extras
        String station = getIntent().getStringExtra("STATION");
        String direction = getIntent().getStringExtra("DIRECTION");
        Spanned spanned;
        if (station != null && direction != null){
            String directionText;
            if(direction.equals("dir1")){
                directionText = MainActivity.DIR1_END_STATION;
            } else {
                directionText = MainActivity.DIR2_END_STATION;
            }
            spanned = Html.fromHtml("<b>" + station.toUpperCase() + " towards " +
                    directionText + "</b>");
        } else {
            spanned = Html.fromHtml("<b>STATION towards DIRECTION</b>");
        }
        stationText.setText(spanned);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for(int i = 0; i < 10; i++){
            addRow();
        }
    }

    /*
        Add row the the table
     */
    private void addRow(){
        View row;
        LayoutInflater inflater = getLayoutInflater();
        row = inflater.inflate(R.layout.custom_table_row2, null, false);
        Spanned time = Html.fromHtml("<b>0:00</b>");


        TextView time1 = (TextView)row.findViewById(R.id.fullScheduleTime1);
        time1.setText(time);
        TextView time2 = (TextView)row.findViewById(R.id.fullScheduleTime2);
        time2.setText(time);
        TextView time3 = (TextView)row.findViewById(R.id.fullScheduleTime3);
        time3.setText(time);
        TextView time4 = (TextView)row.findViewById(R.id.fullScheduleTime4);
        time4.setText(time);
        TextView time5 = (TextView)row.findViewById(R.id.fullScheduleTime5);
        time5.setText(time);

        table.addView(row);
    }
}
