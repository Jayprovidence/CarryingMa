package com.example.carryingma;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class HTTPPostActivity extends Activity implements View.OnClickListener {
    private EditText txtMessage;
    private Button sendBtn;
    //SC server ip
    //private String uriAPI = "http://192.168.1.164/httpPostTest.php";
    //Tuna server ip
    private String uriAPI = "http://10.0.36.116/php/httpPostTest.php";

    //message serial for "refresh page"
    protected static final int REFRESH_DATA = 0x00000001;

    //create a handler for UI thread, used for receiving other thread messages
    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //receive data
                case REFRESH_DATA:
                    String result = null;
                    if (msg.obj instanceof String)
                        result = (String) msg.obj;
                    if (result != null)
                        //toast out (display) data received
                        Toast.makeText(HTTPPostActivity.this, result, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set default layout xml file
        setContentView(R.layout.activity_http_post);

        txtMessage = (EditText) findViewById(R.id.txt_message);
        sendBtn = (Button) findViewById(R.id.send_bnt);

        if (sendBtn != null)
            sendBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == sendBtn) {
            if (txtMessage != null) {
                //dump message entered in EditText/txtMessage to msg
                String msg = txtMessage.getEditableText().toString();

                //start a thread. insert msg into a runnable
                Thread t = new Thread(new sendPostRunnable(msg));
                //start thread
                t.start();
            }
        }
    }

    private String sendPostDataToInternet(String strTxt) {
        //establish an http Post connection
        HttpPost httpRequest = new HttpPost(uriAPI);
        //Post connection need to be an ArrayList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data", strTxt));

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
        String strTxt = null;

        //set what string is going to send
        public sendPostRunnable(String strTxt) {
            this.strTxt = strTxt;
        }

        public void run() {
            String result = sendPostDataToInternet(strTxt);
            mhandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
        }
    }
}