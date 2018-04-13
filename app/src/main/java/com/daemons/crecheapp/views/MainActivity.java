package com.daemons.crecheapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daemons.crecheapp.R;
import com.daemons.crecheapp.utilities.Child;
import com.daemons.crecheapp.utilities.CustomAdapter;
import com.daemons.crecheapp.utilities.MySingleton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    private TextView textViewName, textViewAddress, textViewchildId, textViewResult,a;
    JSONArray responseList;
    ArrayList<Child> childArrayList;
    private ProgressBar progressBar;
    CustomAdapter childAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.loading_spinner);
        childArrayList = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        Log.i("lkj1",""+prefs.getInt("crecheId",0));
        String url = getString(R.string.BaseURL)+"/child/getchildbycreche/"+prefs.getInt("crecheId",0);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        // Do something with response string
                        Log.i("lkjj",response);
                        try{
                            responseList=new JSONArray(response);
                            for(int i=0;i<responseList.length();i++){
                                Child child = new Child();
                                JSONObject obj = responseList.getJSONObject(i);
                                child.childId = obj.getInt("childId");
                                child.workerId = obj.getInt("workerId");
                                child.primaryContact = String.valueOf(obj.getLong("childPrimaryContact"));
                                child.dob = obj.getString("childDOB").substring(0,10);
                                child.parentId = obj.getInt("parentId");
                                child.childName = obj.getString("childFirstName")+" "+obj.getString("childLastName");
                                childArrayList.add(child);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        childAdapter.notifyDataSetChanged();
                        //setProgressBarIndeterminateVisibility(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                        Log.i("tata","Error");
                    }
                }) {
        };
        // Add StringRequest to the RequestQueue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);

        textViewName = (TextView) findViewById(R.id.name);
        textViewAddress = (TextView) findViewById(R.id.address);
        textViewchildId = (TextView) findViewById(R.id.childId);
        textViewResult = (TextView) findViewById(R.id.result);
        a = (TextView) findViewById(R.id.add);

        Button buttonQRScan = (Button) findViewById(R.id.qr_scan);
        buttonQRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });

        ListView childListView = (ListView) findViewById(R.id.child_list);
        childAdapter = new CustomAdapter(this,0,childArrayList);
        childListView.setAdapter(childAdapter);

        childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ChildDetailsActivity.class);
                intent.putExtra("childId",  childArrayList.get(i).childId);
                intent.putExtra("priContact",  childArrayList.get(i).primaryContact);
                intent.putExtra("dob",  childArrayList.get(i).dob);
                intent.putExtra("workerId", childArrayList.get(i).workerId);
                intent.putExtra("childName",  childArrayList.get(i).childName);
                intent.putExtra("parentid", childArrayList.get(i).parentId);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.i("mkj",result.toString());
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "QR Code donot contain data", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                textViewResult.setText("Result");
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewName.setText(obj.getString("name"));
                   textViewAddress.setText(obj.getString("timestamp"));
                   textViewchildId.setText(obj.getString("childId"));
                   a.setText(obj.getString("address"));
                   //Code change
                    String date = obj.getString("timestamp");
                    long currentmm = System.currentTimeMillis();
                    Calendar c = Calendar.getInstance();
                    int currentdate = c.get(Calendar.DATE);
                    int currentMonth = c.get(Calendar.MONTH);
                    int currentyear = c.get(Calendar.YEAR);
                    Log.i("datemera", String.valueOf(currentdate));
                    String QRdate = obj.getString("timestamp").substring(8,11);
                    Log.i("QRdate",QRdate);
                    int QRintdate = Integer.parseInt(QRdate.trim());
                    Log.i("QRintdate",QRintdate+"");
                    if(QRintdate == currentdate)
                    {
                        Toast.makeText(this, "YOU CAN TAKE CHILD", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    textViewName.setText(result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_options, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                SharedPreferences.Editor editor = getSharedPreferences("session", MODE_PRIVATE).edit();
                editor.putInt("crecheId", 0);
                editor.putBoolean("login", false);
                editor.apply();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                //Log.i("asdfgh", "");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
