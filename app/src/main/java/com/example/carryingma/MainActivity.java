package com.example.carryingma;
//package com.android.wecarry;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.carryingma.GCM.QuickstartPreferences;
import com.example.carryingma.GCM.RegistrationIntentService;
import com.example.carryingma.GcmExample.GCMLaunchActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //Buttons action
    public void httpPostBtn (View view){
        Intent intent = new Intent(this, HTTPPostActivity.class);
        startActivity(intent);
    }

    public void GCMlaunchBtn (View view){
        Intent intent = new Intent(this, GCMLaunchActivity.class);
        startActivity(intent);
    }
    public void ExamTimeChangeBtn(View view){
        Intent intent = new Intent(this, ExamTimeChangeActivity.class);
        startActivity(intent);
    }
    public void LoginBtn(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void UserExamBtn(View view){
        Intent intent = new Intent(this, UserExamActivity.class);
        startActivity(intent);
    }
    public void GcmBtn(View view){
        Intent intent = new Intent(this, GcmActivity.class);
        startActivity(intent);
    }


}
