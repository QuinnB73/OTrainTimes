package com.quinnbudan.otraintimesv2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.quinnbudan.otraintimesv2.R;

import com.quinnbudan.otraintimesv2.interfaces.TaskDelegate;
import com.quinnbudan.otraintimesv2.models.StationModel;
import com.quinnbudan.otraintimesv2.tools.Stations;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener, TaskDelegate {
    private static final String TAG = "MainActivity";
    private static final int NUM_TIMES_RETURNED_BY_API = 3;
    public static final String DIR1_END_STATION = "Greenboro";
    public static final String DIR2_END_STATION = "Bayview";

    private Stations stations = Stations.singletonInstance;
    private ArrayList<StationModel> stationsDir1;
    private ArrayList<StationModel> stationsDir2;
    private TextView dir1;
    private TextView dir2;
    private TableLayout dir1Sched;
    private TableLayout dir2Sched;

    /*
     * Callback for the creation of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get all views
        dir1 = (TextView)findViewById(R.id.dir1);
        dir2 = (TextView)findViewById(R.id.dir2);
        dir1Sched = (TableLayout)findViewById(R.id.dir1Sched);
        dir2Sched = (TableLayout)findViewById(R.id.dir2Sched);
        initStationLists();
        initTables();
    }

    /*
     * Callback to create the options menu for the app bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // inflate custom menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_options_menu, menu);
        return true;
    }

    /*
     * Callback for when an option from the options menu in the app bar is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // handle refresh button press in action bar
        switch(item.getItemId()){
            case R.id.refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Initialize the station lists used in this activity
     */
    private void initStationLists(){
        stationsDir1 = new ArrayList<>();
        stationsDir2 = new ArrayList<>();
        String[] stationNames = stations.getStationNames();

        for(int i = 0; i < stationNames.length; i++){
            stationsDir1.add(new StationModel(stationNames[i], "dir1", this, this.getApplicationContext()));
            stationsDir2.add(new StationModel(stationNames[i], "dir2", this, this.getApplicationContext()));
        }
        stationsDir1.remove(0);
        stationsDir2.remove(stationsDir2.size()-1);
    }

    /*
     * Initialize the tables and the text views above them
     */
    private void initTables(){
        Spanned spannedText = Html.fromHtml("<b>TOWARDS " + DIR1_END_STATION.toUpperCase() + " (<i>GPS</i>)</b>");
        dir1.setText(spannedText);
        spannedText = Html.fromHtml("<b>TOWARDS " + DIR2_END_STATION.toUpperCase() + " (<i>GPS</i>)</b>");
        dir2.setText(spannedText);

        for(int i = 0; i < stationsDir1.size(); i++){
            addRow(stationsDir1.get(i), dir1Sched, stationsDir1.get(i).getGpsSchedule());
            addRow(stationsDir2.get(i), dir2Sched, stationsDir1.get(i).getGpsSchedule());
        }
    }

    /*
     * Add a row to the tables based on the custom_table_row.xml layout
     */
    private void addRow(StationModel station, TableLayout table, ArrayList<String> gpsTimes){
        // setup
        View row;
        LayoutInflater inflater = getLayoutInflater();
        row = inflater.inflate(R.layout.custom_table_row, null, false);
        Spanned time = Html.fromHtml("<b>0:00</b>");

        // find all views and init buttons
        Button stationCell = (Button)row.findViewById(R.id.station);
        stationCell.setText(station.getName());
        stationCell.setTag(station.getDirection());
        stationCell.setOnClickListener(this);
        TextView timeCell1 = (TextView)row.findViewById(R.id.time1);
        TextView timeCell2 = (TextView)row.findViewById(R.id.time2);
        TextView timeCell3 = (TextView)row.findViewById(R.id.time3);

        // logic to determine what to populate the time fields with, everything will be 0:00 until
        // the app has loaded the response from the OC Transpo API
        if (gpsTimes == null || gpsTimes.size() < NUM_TIMES_RETURNED_BY_API) {
            timeCell1.setText(time);
            timeCell2.setText(time);
            timeCell3.setText(time);
        } else {
            ArrayList<Spanned> times = new ArrayList<>();
            for(int i = 0; i < gpsTimes.size(); i++){
                times.add(Html.fromHtml("<b>" + gpsTimes.get(i) + "</b>"));
            }
            timeCell1.setText(times.get(0));
            timeCell2.setText(times.get(1));
            timeCell3.setText(times.get(2));
        }

        table.addView(row);
    }

    /*
     * Refreshes the GPS schedule for each station
     */
    private void refresh(){
        Log.d(TAG, "refreshing");
        for(int i = 0; i < stationsDir1.size(); i++){
            stationsDir1.get(i).refreshGpsSchedule();
            stationsDir2.get(i).refreshGpsSchedule();
        }
    }

    /*
     * Takes the user to the activity that will display the full schedule for a station
     */
    @Override
    public void onClick(View view){
        Intent intent = new Intent(getApplicationContext(), StationActivity.class);
        Button button = (Button)view;
        String station = (String)button.getText();
        Log.d(TAG, station);
        intent.putExtra("STATION", station);
        intent.putExtra("DIRECTION", (String)button.getTag());
        startActivity(intent);
    }

    /*
     * Once the async Retrofit2 call is completed we update the text fields here
     */
    @Override
    public void taskCompletionResult(ArrayList<String> result){
        Log.d(TAG, "got it: " + result.toString());
        dir1Sched.removeAllViews();
        dir2Sched.removeAllViews();
        for(int i = 0; i < stationsDir1.size(); i++){
            addRow(stationsDir1.get(i), dir1Sched, stationsDir1.get(i).getGpsSchedule());
            addRow(stationsDir2.get(i), dir2Sched, stationsDir2.get(i).getGpsSchedule());
        }
    }
}
