package com.example.carryingma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    private EditText txtUsr,txtPwd;
    private Button loginBtn;
    private String uriAPI = "http://192.168.1.5/php/login.php";

    //message serial for "refresh page"
    protected static final int REFRESH_DATA = 0x00000001;

    //create a handler for UI thread, used for receiving other thread messages
    public Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //receive data
                case REFRESH_DATA:
                    String result = null;
                    if (msg.obj instanceof String)
                        result = (String) msg.obj;
                    if (result != null)
                        //toast out (display) data received
                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                    //break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsr = (EditText) findViewById(R.id.email);
        txtPwd = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.LoginBtn);

        if (loginBtn != null)
            loginBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == loginBtn) {
            if (txtUsr != null) {
                if(txtPwd !=null){
                    //dump message entered in EditText/txtMessage to msg
                    String msg = txtUsr.getEditableText().toString();
                    String msg2 = txtPwd.getEditableText().toString();

                    //start a thread. insert msg into a runnable
                    Thread t = new Thread(new sendPostUsr(msg,msg2));
                    t.start();
                }
            }
        }
    }

    private String sendPostDataToInternet(String usr, String pwd) {
        //establish an http Post connection
        HttpPost httpRequestLogin = new HttpPost(uriAPI);
        //Post connection need to be an ArrayList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("usr", usr));
        params.add(new BasicNameValuePair("pwd", pwd));

        try {
            //send http request
            httpRequestLogin.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //receive http request
            HttpResponse httpResponseLogin = new DefaultHttpClient().execute(httpRequestLogin);
            //if StatusCode is 200
            if (httpResponseLogin.getStatusLine().getStatusCode() == 200) {
                //dump string to strResult
                String strResult = EntityUtils.toString(httpResponseLogin.getEntity());
                //return string
                return strResult;
            }
        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage().toString(),Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        return null;
    }

    class sendPostUsr implements Runnable {
        String usr = null;
        String pwd = null;

        //set what string is going to send
        public sendPostUsr(String usr, String pwd) {
            this.usr = usr;
            this.pwd = pwd;
        }

        public void run() {
            String result = sendPostDataToInternet(usr, pwd);
            mhandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
            String a = "Login success";
            if (result.equals(a)){
                //TODO: replace MainActivity to desired activity
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }


}
