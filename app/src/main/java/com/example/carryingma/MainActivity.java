package com.example.carryingma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnClickListener
{
    private EditText txtMessage;
    private Button sendBtn;
    //SC server ip
    //private String uriAPI = "http://192.168.1.164/httpPostTest.php";
    //Tuna server ip
    private String uriAPI = "http://10.0.36.116/httpPostTest.php";

    //    ?????????
    protected static final int REFRESH_DATA = 0x00000001;
    //    ??UI Thread???Handler,?????Thread????
    Handler mhandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_DATA:
                    String result = null;
                    if (msg.obj instanceof String)
                        result = (String) msg.obj;
                    if(result != null)
                        Toast.makeText(MainActivity.this,result,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMessage = (EditText)findViewById(R.id.txt_message);
        sendBtn = (Button)findViewById(R.id.send_bnt);

        if (sendBtn != null)
            sendBtn.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        if(v == sendBtn)
        {
            if (txtMessage != null)
            {
//                ?????????
                String msg = txtMessage.getEditableText().toString();

//                ????Thread??????????Runnable???Thread??
                Thread t = new Thread(new sendPostRunnable(msg));
                t.start();
            }
        }
    }

    private String sendPostDataToInternet(String strTxt)
    {
        /*??HTTP post??*/
        HttpPost httpRequest = new HttpPost(uriAPI);
        /* Post?????????NameValuePair[]????*/
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data", strTxt));

        try
        {
//            ??HTTP Request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//            ??HTTP Request
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
//            ?????200 OK
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
//                ??????
                String strResult = EntityUtils.toString(httpResponse.getEntity());
//                ??
                return strResult;
            }
        }catch (Exception e)
        {
//            Toast.makeText(this, e.getMessage().toString(),Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        return null;
    }

    class sendPostRunnable implements Runnable
    {
        String strTxt = null;

        public sendPostRunnable(String strTxt) {
            this.strTxt = strTxt;
        }

        public void run()
        {
            String result = sendPostDataToInternet(strTxt);
            mhandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
        }
    }
}
