package com.example.abac;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = customView.class.getSimpleName();

    private customView grid;
    private final dbHelper dbHelper = new dbHelper(MainActivity.this);
    private SQLiteDatabase db;
    private int curPolicy;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = findViewById(R.id.grid);
        db = dbHelper.getWritableDatabase();
        final FloatingActionButton button_add = findViewById(R.id.add_policy);
        Button button_del = findViewById(R.id.delButton);
        Button button_deploy = findViewById(R.id.deploy_policy);


        updateSpinner();
        spinner.setOnItemSelectedListener(this);

        // Check if the table is empty. If so, create a 20x20 table
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM Policies", null);

        if (cur!=null){
            cur.moveToFirst();
            if (cur.getInt(0)==0){
                PolicyModel policyModel = new PolicyModel(-1, "Example Policy", 20);
                dbHelper.addPolicy(policyModel);
                dbHelper.initMatrix(1,20);
                curPolicy=1;
                grid.initGrid(20,1);
                updateSpinner();
            }else{
                loadFirstPolicy();
            }
        }
        grid.invalidate();


        if (cur != null) {
            cur.close();
        }

        // Add policy button switches to different activity.
        button_add.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddPolicy.class)));

        // Functionality for deleting the current policy.
        button_del.setOnClickListener(view -> {
            CharSequence text;

            // Check that there is more than one policy left
            Cursor cur1 = db.rawQuery("SELECT Count(PolicyID) FROM POLICIES",null);
            cur1.moveToFirst();
            if(cur1.getInt(0) != 1){
                // Given that there is more than one policy left, display to the user that the policy has been deleted and remove it from the database.
                cur1 = db.rawQuery("SELECT PolicyName FROM POLICIES WHERE PolicyID=" + curPolicy,null);
                cur1.moveToFirst();

                text = "Deleted Policy: " + cur1.getString(0);
                dbHelper.deletePolicy(curPolicy);
                loadFirstPolicy();
                grid.invalidate();
                updateSpinner();
            }else{
                // Don't allow the user to delete the last policy.
                 text = "Error: Cannot delete last policy";
            }
            // Display the appropriate toast.
            Toast toast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
            toast.show();
            cur1.close();
        });

        // When clicking 'deploy', generate the JSON and send it to the server
        button_deploy.setOnClickListener(view -> {
            JSONArray jsonArray = matrixJSON();
            sendData(jsonArray);
        });
    }

    // Load the first policy. Used on initial load and when deleting the current policy.
    public void loadFirstPolicy(){
        // Select the first policyID in the table
        Cursor cur = db.rawQuery("SELECT MIN(PolicyID) FROM Policies",null);
        cur.moveToFirst();
        curPolicy = cur.getInt(0);
        cur.close();

        // Select the size from the specified policyID so that we may initialize the grid.
        cur = db.rawQuery("SELECT size FROM Policies WHERE PolicyID=" + curPolicy,null);
        cur.moveToFirst();
        int size = cur.getInt(0);

        grid.initGrid(size,curPolicy);
        cur.close();
    }

    // Load all policy names into the spinner and associate the id with it.
    public void updateSpinner(){
        // Select the policyID under _id so that we may use it in the spinner
        // Select Policy names so that we may display it in the spinner.
        Cursor cur = db.rawQuery("SELECT PolicyID as _id, PolicyName FROM Policies",null);
        String[] orig = new String[]{"PolicyName"};
        int[] dest = new int[]{android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,cur,orig,dest,0);
        spinner = this.findViewById(R.id.spinner);
        spinner.setAdapter(sca);
    }

    // Used when selecting a policy on the Spinner.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        curPolicy = (int) id;
        Cursor cur = db.rawQuery("SELECT size FROM Policies WHERE PolicyID=" + curPolicy,null);
        cur.moveToFirst();
        int size = cur.getInt(0);
        cur.close();

        grid.initGrid(size,curPolicy);
        grid.invalidate();
    }

    // Likely will not be used, but required as we implemented AdapterView.OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Log.d(TAG,"Nothing selected.");
    }

    // Generates a JSON Array from the policy that is selected.
    public JSONArray matrixJSON(){
        // Only select the zones that are red or yellow as those are the necessary values for the path planning.
        Cursor cursor = db.rawQuery("SELECT columnID,rowID,value FROM Matrix WHERE PolicyID=" + curPolicy +
                " AND value!=1",null);

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        // For each row in the table, put each column into a JSON Object.
        // Each Object gets placed into the array
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        //Log.d(TAG, e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    // Sends the JSONArray to the PHP server so that it may update the SQL tables with proper information.
    public void sendData(JSONArray data) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:80/insertmatrix.php";

        // Switch it to a string so that it can be sent to the server as a POST request.
        String json = data.toString();

        // Generate the request
        StringRequest dataReq = new StringRequest(Request.Method.POST,url,response -> Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("data",json);

                return params;
            }

        };
        dataReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        // Add the request
        queue.add(dataReq);
    }
}