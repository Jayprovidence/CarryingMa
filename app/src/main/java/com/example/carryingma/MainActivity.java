package com.example.carryingma;
//package com.android.wecarry;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import java.io.IOException;

import static com.example.carryingma.GCM.RegistrationIntentService.getDefaults;


public class MainActivity extends Activity  implements View.OnClickListener/*implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener*/{

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 9001;

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "GcmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();*/

        //button click listeners
        findViewById(R.id.httppost_button).setOnClickListener(this);
        findViewById(R.id.gcmlaunch_button).setOnClickListener(this);
        findViewById(R.id.examtimechange_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.userexam_button).setOnClickListener(this);
        findViewById(R.id.gcm_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);


        //display progress bar for registering gcm instance id and token's progress status
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        //check if google play service is available on the device
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        //display current user
        String user = getDefaults(getString(R.string.current_user), MainActivity.this);
        Toast.makeText(MainActivity.this, user, Toast.LENGTH_LONG).show();
    }

    //Check if the device supports Google Play Services APK
    //This check should be used in MainActivity's onCreate() and onResume()
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    //
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
            /*case R.id.sign_in_button:
                // User clicked the sign-in button, so begin the sign-in process and automatically
                // attempt to resolve any errors that occur.
                // [START sign_in_clicked]
                mShouldResolve = true;
                mGoogleApiClient.connect();
                // Show a message to the user that we are signing in.
                mStatusTextView.setText(R.string.signing_in);
                // [END sign_in_clicked]
                break;*/

        }
    }

    /*//reset state of flags
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }
    //successful signed in,
    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        // Show the signed-in UI
        showSignedInUI();
    }
    //connect to google login service
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            showSignedOutUI();
        }
    }*/

    //Activity lifecycle
    @Override
    protected void onStart() {
        super.onStart();

        //load current user
        String user = getDefaults(getString(R.string.current_user), MainActivity.this);
        //if the current user is Admin(default), bring the user back to the login page
        if (user=="Admin"){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
