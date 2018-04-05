package com.example.phili.foodpaldemo.Function;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.phili.foodpaldemo.MainHomeActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;


/**
 * Created by wzb on 2018/4/5.
 */

public class NotificationOpenedHandler extends AppCompatActivity
        implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.

    //https://documentation.onesignal.com/docs/android-native-sdk#section--notificationopenedhandler-
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

        Intent intent = new Intent(getApplicationContext(), MainHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}