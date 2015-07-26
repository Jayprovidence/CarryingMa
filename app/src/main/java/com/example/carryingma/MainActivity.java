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
    private String uriAPI = "http://0.0.0.0/httpPostTest.php";
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
            String msg = null;
            if (txtMessage != null)
            {
                msg = txtMessage.getEditableText().toString();
                String result = sendPostDataToInternet(msg);
                if (result != null)
                    Toast.makeText(this, result,Toast.LENGTH_LONG).show();
            }
        }
    }

    private String sendPostDataToInternet(String strTxt)
    {
        /*�إ�HTTP post�s�u*/
        HttpPost httpRequest = new HttpPost(uriAPI);
        /* Post�B�@�ǰe�ܼƥ�����NameValuePair[]�}�C�x�s*/
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data", strTxt));

        try
        {
//            �o�XHTTP Request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//            ���oHTTP Request
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
//            �Y���A�X��200 OK
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
//                ���X�^���r��
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                return strResult;
            }
        }catch (ClientProtocolException e)
        {
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch (IOException e)
        {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT);
            e.printStackTrace();
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage().toString(),Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
