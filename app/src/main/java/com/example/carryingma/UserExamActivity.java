package com.example.carryingma;

import android.app.Activity;
import android.content.Context;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class UserExamActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerExamDate;
    private TextView modeTextView;
    private String[] user = {"A","B","C","D","E","F","G"};  //for testing, it will be that a member login and we will get his data from mysql
    private String[] examDate = {"1","2","3","4","5","6","7"}; //for testing
    private ArrayAdapter<String> examDateAdapter;
    private String uriAPI = "http://60.251.49.213:8888/php/updateExamDate.php";
    private String uriAPIMode = "http://60.251.49.213:8888/php/mode.php";
    private String uriAPIGetMode = "http://60.251.49.213:8888/php/get_mode.php";
    static final int REFRESH_DATA = 0x00000001;
    private Handler mhandler, mhandler2;
    private String userNameString, examDateString;  //data for sent
    private static Toast toast[] = new Toast[2];    //this activity has 2 toast to show

    private String modeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exam);
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
                            makeTextAndShow(0, UserExamActivity.this, result, Toast.LENGTH_SHORT);
                        break;
                }
            }
        };
        mhandler2 = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    //receive data
                    case REFRESH_DATA:
                        String modeValue = null;
                        if (msg.obj instanceof String)
                            modeValue = (String) msg.obj;
                        if (modeValue != null){
                            //toast out (display) data received
                            modeTextView.setText(modeValue);
                            Definition.MODE_EAXM_DATE  = modeValue;
                        }
                        break;
                }
            }
        };


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner =(Spinner) parent;
        if(spinner.getId() == R.id.spinnerExamDate) {
            //Set the data about user and examDate then prepare to sent data to server
            examDateString = examDate[position];
        }
        if( examDateString != null) {
            makeTextAndShow(1, UserExamActivity.this, "User: " + userNameString + ", Date: " + examDateString, Toast.LENGTH_SHORT);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    //Buttons action
    public void changeExamTimeBtn(View view) {
        //start a thread. insert msg into a runnable
        Thread t = new Thread(new sendPostRunnable(userNameString, examDateString));
        //start thread
        t.start();
        Definition.USER_NAME = userNameString;
        Definition.USER_EXAM_DATE = examDateString;
    }

    private void findView() {
        spinnerExamDate = (Spinner)findViewById(R.id.spinnerExamDate);
        modeTextView = (TextView)findViewById(R.id.modeTextView);
    }

    private void setSpinner() {
        examDateAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,examDate);
        spinnerExamDate.setAdapter(examDateAdapter);
        spinnerExamDate.setOnItemSelectedListener(this);
    }

    private String sendPostDataToInternet(String userName, String examDate) {
        //establish an http Post connection
        HttpPost httpRequest = new HttpPost(uriAPI);
        HttpPost httpMode = new HttpPost(uriAPIMode);
        HttpPost httpGetMode = new HttpPost(uriAPIGetMode);
        //Post connection need to be an ArrayList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("examDate", examDate));

        try {
            //send http request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //receive http request
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            HttpResponse httpRMode = new DefaultHttpClient().execute(httpMode);
            HttpResponse httpRGetMode = new DefaultHttpClient().execute(httpGetMode);
            //if StatusCode is 200
            if (httpRGetMode.getStatusLine().getStatusCode() == 200){
                //dump string
                modeValue = EntityUtils.toString(httpRGetMode.getEntity());
            }
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
        String userName = null;
        String examDate = null;

        //set what string is going to send
        public sendPostRunnable(String userName, String examDate) {
            this.userName = userName;
            this.examDate = examDate;
        }
        public void run() {
            String result = sendPostDataToInternet(userName, examDate);
            mhandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
            mhandler2.obtainMessage(REFRESH_DATA, modeValue).sendToTarget();
        }
    }
    private static void makeTextAndShow(int i,  final Context context, final String text, final int duration) {
        if(i<=toast.length && i>=0) {
            if(toast[i] == null) {
                toast[i] = android.widget.Toast.makeText(context, text, duration);
            }else {
                toast[i].setText(text);
                toast[i].setDuration(duration);
            }
            toast[i].show();
        }
    }

}
