# firebase-notifications
This app will create a caller notifications in notification drawer when we trigger a notifications from FCM V1. The caller notifications will appear irrespective of the app state like foreground, background, terminated.

Attached the postman curl to trigger fcm notification with data. Let me know, I can trigger the notification with my credentials.
curl --location
'https://fcm.googleapis.com/v1/projects/fir-notification-b7775/messages:send'
\
--header 'Authorization: Bearer
FCM_V1_okta_token'
\
--header 'Content-Type: application/json' \
--data '{
"message": {
"token": <Fcm token of device>,
"data": {
"caller_name": "Venkatesh"
}
}
}'