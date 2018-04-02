'use strict'

const functions = require('firebase-functions');
const admin  = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendNotification = functions.database.ref('/notifications/{userId}/{notificationId}').onWrite(event =>{

  const user_id = event.params.userId;
  const notification = event.params.notification;

  console.log('we have a notification to send to: ', user_id);

  if(!event.data.val()){

    return console.log('A Notification has been deleted from the database: ', notification_id);

  }

  const fromUser = admin.database.ref(`/notifications/${user_id}/${notification_id}`).once(`value`);
  return fromUser.then(fromUserResult =>{

    const from_user_id = fromUserResult.val().from;

    console.log('You have a new Notification from : ', from_user_id);

    const userQuery  = admin.database().ref(`users/${from_user_id}/userName`).once(`value`);
    return userQuery.then(userResult =>{

      const userName = userResult.val();
      const deviceToken = admin.database().ref(`/users/${user_id}/device_token`).once('value');

      return deviceToken.then(result => {

        const token_id = result.val();

        const payload = {
          notification:{
            title:"Joining the group",
            body:`${userName} has joined the group`,
            icon:"default",
            click_action:"android.intent.action.notification"
          }

      };
      return admin.messaging().sendToDevice(token_id, payload);

      });


    });



  });



});
