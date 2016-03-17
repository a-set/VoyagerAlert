package co.sethspace.voyageralert;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * This class registers the device and keeps track of a boolean shared preferences indicator
 * on the front-end
 * Created by Anirudh on 3/17/2016.
 */
public class VoyagerRegistrationIntentService extends IntentService {


    private static final String TAG = "RegIntentService";

    public VoyagerRegistrationIntentService(){
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG,"GCM Registration Token" + token);

            // TODO: Implement this method to send any registration to your app's servers.
            //sendRegistrationToServer(token);

            // Subscribe to topic channels
            //subscribeTopics(token);
            sharedPreferences.edit().putBoolean(getString(R.string.gcm_sentToken),true).apply();

            //Print on the console that the registration is complete
            Log.i(TAG,"GCM Registration is complete");

        } catch(Exception e){
            Log.d(TAG,"Failed to complete token refresh");
            sharedPreferences.edit().putBoolean(getString(R.string.gcm_sentToken), false).apply();
        }
    }
}
