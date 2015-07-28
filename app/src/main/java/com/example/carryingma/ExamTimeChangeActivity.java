package com.example.carryingma;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class ExamTimeChangeActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerUser,spinnerExamDate;
    private TextView averageTextView;
    private String[] user = {"A","B","C","D","E"};
    private String[] examDate = {"1","2","3","4","5","6","7"};
    private ArrayAdapter<String> userAdapter,examDateAdapter;
    private String uriAPI = "http://10.0.36.116/php/mode.php";
    static final int REFRESH_DATA = 0x00000001;
    private Handler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_time_change);
        findView();
        setSpinner();

        mhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    //receive data
                    case REFRESH_DATA:
                        String result = null;
                        if (msg.obj instanceof String)
                            result = (String) msg.obj;
                        if (result != null)
                            //toast out (display) data received
                            Toast.makeText(ExamTimeChangeActivity.this, result, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner =(Spinner) parent;
        if(spinner.getId() == R.id.spinnerUser) {
            Toast.makeText(this, "你選擇" + user[position], Toast.LENGTH_SHORT).show();
        }else if(spinner.getId() == R.id.spinnerExamDate) {
            Toast.makeText(this, "你選擇" + examDate[position], Toast.LENGTH_SHORT).show();
        }
        setData();
    }

    //Buttons action
    public void ChangeExamTimeBtn(View view) {
        sentExamDate();
    }

    private void setData() {
        //TODO: Set the data about user and examDate then prepare to sent data to server
    }

    private void sentExamDate() {
        //TODO: Find the user and update his examDate
            //start a thread. insert msg into a runnable
            Thread t = new Thread(new sendPostRunnable());
            //start thread
            t.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    private void findView() {
        spinnerUser = (Spinner)findViewById(R.id.spinnerUser);
        spinnerExamDate = (Spinner)findViewById(R.id.spinnerExamDate);
        averageTextView = (TextView)findViewById(R.id.averageTextView);
    }

    private void setSpinner() {
        userAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,user);
        examDateAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,examDate);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(userAdapter);
        spinnerExamDate.setAdapter(examDateAdapter);
        spinnerUser.setOnItemSelectedListener(this);
        spinnerExamDate.setOnItemSelectedListener(this);
    }

    private String sendPostDataToInternet() {
        //establish an http Post connection
        HttpPost httpRequest = new HttpPost(uriAPI);
        //Post connection need to be an ArrayList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("data", strTxt));

        try {
            //send http request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //receive http request
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            //if StatusCode is 200
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //dump string to strResult
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                //return string
                return strResult;
            }
        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage().toString(),Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        return null;
    }
    class sendPostRunnable implements Runnable {
        public void run() {
            String result = sendPostDataToInternet();
            mhandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
        }
    }
}
