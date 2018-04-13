package com.daemons.crecheapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daemons.crecheapp.R;
import com.daemons.crecheapp.utilities.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private TextView tv_forgotPass;
    private EditText et_uid, et_pass;
    public String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        if(prefs.getBoolean("login",false)==true){
            //c.token = obj.getString("token");
            Intent i =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }


        url = getString(R.string.BaseURL)+"/creche/logincreche/";
        Log.i("url",url);
        et_uid = (EditText) findViewById(R.id.et_user_id);
        et_pass = (EditText) findViewById(R.id.et_pass);
        loginButton = (Button) findViewById(R.id.bt_login);
        tv_forgotPass = (TextView) findViewById(R.id.tv_forgot_pass);

        // Set a click listener for button widget
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Empty the TextView
                // Initialize a new StringRequest
                final String editid = et_uid.getText().toString();
                final String editpass = et_pass.getText().toString();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Do something with response string
                               Log.i("tata1",response);
                               try{
                                   JSONObject obj=new JSONObject(response);
                                   //c.token = obj.getString("token");
                                   Intent i =new Intent(LoginActivity.this,MainActivity.class);
                                   SharedPreferences.Editor editor = getSharedPreferences("session", MODE_PRIVATE).edit();
                                   editor.putInt("crecheId", obj.getInt("crecheId"));
                                   editor.putBoolean("login", true);
                                   editor.apply();
                                   startActivity(i);
                                   finish();
                               }catch (Exception e){
                                   Toast.makeText(LoginActivity.this,"Some error occured",Toast.LENGTH_SHORT).show();
                                   Log.i("tata","Error");
                               }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this,"Wrong ID or Password",Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> d = new HashMap<String, String>();
                        JSONObject data = new JSONObject();
                        try {
                            data.put("crecheId", editid);
                            data.put("crechePassword", editpass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.put("data", data.toString());
                        return d;
                    }
                };




                // Add StringRequest to the RequestQueue
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String id = et_uid.getText().toString();
        String pass = et_pass.getText().toString();
//        if (id.equals("123") && pass.equals("123")) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        } else {
//            Toast.makeText(this, "User id or Password invalid!", Toast.LENGTH_SHORT).show();
//            et_uid.setText("");
//            et_pass.setText("");
//            et_pass.clearFocus();
//        }


    }

    public void forgotPass(View view) {
        Toast.makeText(this, "Yaad rakhna chaiye tha!!", Toast.LENGTH_SHORT).show();
    }
}
