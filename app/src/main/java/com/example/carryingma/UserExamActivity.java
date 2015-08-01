package com.example.carryingma;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public abstract class UserExamActivity extends Activity implements AdapterView.OnItemSelectedListener
{
    private TextView UserTextView,DateTextView,modeTextView;
    private String[] user = {"A","B","C","D","E","F","G"};  //for testing, it will be that a member login and we will get his data from mysql
    private String[] examDate = {"1","2","3","4","5","6","7"}; //for testing
    private ArrayAdapter<String> userAdapter,examDateAdapter;
    private String uriAPI = "http://10.0.36.116/php/updateExamDate.php";
    private String uriAPIMod = "http://10.0.36.116/php/mode.php";
    static final int REFRESH_DATA = 0x00000001;
    private Handler mhandler;
    private String userNameString, examDateString;  //data for sent
    private static Toast toast[] = new Toast[2];    //this activity has 2 toast to show

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exam);
        findView();

        mhandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    //receive data
                    case REFRESH_DATA:
                        String result = null;
                        if (msg.obj instanceof String)
                            result = (String) msg.obj;
                        if (result != null)
                            //toast out (display) data received
                            //makeTextAndShow(0, UserExamActivity.this, result, Toast.LENGTH_SHORT);
                        UserTextView.setText(result);
                        break;
                }
            }
        };
    }
    private void findView()
    {

        UserTextView = (TextView)findViewById(R.id.userTextView);
        DateTextView = (TextView)findViewById(R.id.dateTextView);
        modeTextView = (TextView)findViewById(R.id.modeTextView);
    }
    private String sendPostDataToInternet(String userName, String examDate) {
        //establish an http Post connection
        HttpPost httpRequest = new HttpPost(uriAPI);
        HttpPost httpMode = new HttpPost(uriAPIMod);
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


}

