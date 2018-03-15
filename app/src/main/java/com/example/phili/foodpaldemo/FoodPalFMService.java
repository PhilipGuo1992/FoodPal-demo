package com.example.phili.foodpaldemo;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by yunfei on 2018-03-14.
 */

public class FoodPalFMService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String notifiTitle = null;
        String notifiBody = null;

        if(remoteMessage.getNotification() != null){
            notifiTitle = remoteMessage.getNotification().getTitle();
            notifiBody = remoteMessage.getNotification().getBody();

        }

    }


}
