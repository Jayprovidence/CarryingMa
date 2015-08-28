package com.example.carryingma;
//package com.android.wecarry;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.carryingma.GCM.QuickstartPreferences;
import com.example.carryingma.GCM.RegistrationIntentService;
import com.example.carryingma.GcmExample.GCMLaunchActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;

import static com.example.carryingma.GCM.RegistrationIntentService.getDefaults;


public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button click listeners
        findViewById(R.id.httppost_button).setOnClickListener(this);
        findViewById(R.id.gcmlaunch_button).setOnClickListener(this);
        findViewById(R.id.examtimechange_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.userexam_button).setOnClickListener(this);
        findViewById(R.id.gcm_button).setOnClickListener(this);
        findViewById(R.id.google_login_button).setOnClickListener(this);
        findViewById(R.id.fb_login_button).setOnClickListener(this);

        //display current user
        String user = getDefaults(getString(R.string.current_user), MainActivity.this);
        String userTxt = "User: "+ user;
        Toast.makeText(MainActivity.this, userTxt, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.httppost_button:
                intent = new Intent(this, HTTPPostActivity.class);
                startActivity(intent);
                break;
            case R.id.gcmlaunch_button:
                intent = new Intent(this, GCMLaunchActivity.class);
                startActivity(intent);
                break;
            case R.id.examtimechange_button:
                intent = new Intent(this, ExamTimeChangeActivity.class);
                startActivity(intent);
                break;
            case R.id.login_button:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.userexam_button:
                intent = new Intent(this, UserExamActivity.class);
                startActivity(intent);
                break;
            case R.id.gcm_button:
                intent = new Intent(this, GcmActivity.class);
                startActivity(intent);
                break;
            case R.id.google_login_button:
                intent = new Intent(this, GoogleLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.fb_login_button:
                intent = new Intent(this, FBLogin.class);
                startActivity(intent);
                break;
        }
    }

    //[START Activity_Life_Cycle]
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    //[END Activity_Life_Cycle]
}
