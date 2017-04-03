package com.component.independent.voicecommandintegration1;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView result,led;
    Button command;
    String led_com;
    String url_on ="https://api.thingspeak.com/update?api_key=6S9LM0BJ63OYT5CP&field1=1";
    String url_off ="https://api.thingspeak.com/update?api_key=6S9LM0BJ63OYT5CP&field1=0";

    private final int req_code= 143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.textView2);
        led = (TextView) findViewById(R.id.textView3);
        command = (Button)findViewById(R.id.button);

        command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeCommand();

            }
        });
    }
    private void executeCommand(){

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Your Wish is my Command");

        try{
            startActivityForResult(i,req_code);
        }catch(ActivityNotFoundException e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case req_code :
                if (requestCode == req_code && data != null) {
                ArrayList<String> arr = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                result.setText(arr.get(0));
                    led_com = result.getText().toString();
                    toggleLed(led_com);
            }
            break;
        }


    }

    public void toggleLed(String toggle){
       RequestQueue req = Volley.newRequestQueue(MainActivity.this);
        StringRequest on = new StringRequest(Request.Method.GET, url_on, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        StringRequest off = new StringRequest(Request.Method.GET, url_off, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this,response.toString() , Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        if(toggle.equals("on")){
            led.setText("LED turned on");
            req.add(on);

        }
        else if(toggle.equals("of")){
            led.setText("LED turned off");
            req.add(off);
        }
    }
}
