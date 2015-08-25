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


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    //google login stuff
    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 9001;
    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";
    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus, personImageUrl, personProfileUrl, personEmail;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    //gcm stuff
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

        // Restore from saved instance state
        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }

        //button click listeners
        findViewById(R.id.httppost_button).setOnClickListener(this);
        findViewById(R.id.gcmlaunch_button).setOnClickListener(this);
        findViewById(R.id.examtimechange_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.userexam_button).setOnClickListener(this);
        findViewById(R.id.gcm_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // Start with sign-in button disabled until sign-in either succeeds or fails
        findViewById(R.id.sign_in_button).setEnabled(false);

        // Set up view instances
        mStatus = (TextView) findViewById(R.id.status);
        personImageUrl = (TextView) findViewById(R.id.person_photo_url);
        personProfileUrl = (TextView) findViewById(R.id.person_profile_url);
        personEmail = (TextView) findViewById(R.id.person_email);

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
        user = "User: "+ user;
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
            case R.id.sign_in_button:
                // User clicked the sign-in button, so begin the sign-in process and automatically
                // attempt to resolve any errors that occur.
                mStatus.setText(R.string.signing_in);
                // [START sign_in_clicked]
                mShouldResolve = true;
                mGoogleApiClient.connect();
                // [END sign_in_clicked]
                break;
            case R.id.sign_out_button:
                // Clear the default account so that GoogleApiClient will not automatically
                // connect in the future.
                // [START sign_out_clicked]
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                // [END sign_out_clicked]
                updateUI(false);
                break;
            case R.id.disconnect_button:
                // Revoke all granted permissions and clear the default account.  The user will have
                // to pass the consent screen to sign in again.
                // [START disconnect_clicked]
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                // [END disconnect_clicked]
                updateUI(false);
                break;

        }
    }

    //[START google_sign_in_UI]
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            // Show signed-in user's name
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String name = currentPerson.getDisplayName();
            String photo = currentPerson.getImage().getUrl();
            String profile = currentPerson.getUrl();
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

            mStatus.setText(getString(R.string.signed_in_fmt, name));
            personImageUrl.setText(getString(R.string.person_photo_url_fmt, photo));
            personProfileUrl.setText(getString(R.string.person_profile_url_fmt, profile));
            personEmail.setText(getString(R.string.person_email, email));

            // Set button visibility
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            // Show signed-out message
            mStatus.setText(R.string.signed_out);

            // Set button visibility
            findViewById(R.id.sign_in_button).setEnabled(true);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    //[END google_sign_in_UI]

    // [START on_save_instance_state]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }
    // [END on_save_instance_state]

    // [START on_activity_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further errors.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }
    // [END on_activity_result]

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);

        // Show the signed-in UI
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Log.w(TAG, "onConnectionSuspended:" + i);
    }

    // [START on_connection_failed]
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
            updateUI(false);
        }
    }
    // [END on_connection_failed]

    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Show the default Google Play services error dialog which may still start an intent
            // on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;
                            updateUI(false);
                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a message to the user.
            String errorString = getString(R.string.play_services_error_fmt, errorCode);
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

            mShouldResolve = false;
            updateUI(false);
        }
    }

    //[START Activity_Life_Cycle]
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

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check-register gcm
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        //unregister gcm
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    //[END Activity_Life_Cycle]
}
