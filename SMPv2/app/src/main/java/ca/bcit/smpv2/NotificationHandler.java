package ca.bcit.smpv2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import java.util.ArrayList;
import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class NotificationHandler {

    private static int NOTIFICATION_ID = 0;
    private static int SHOW_NOTIFICATION_ID = 1;

    static public void showNotification(String title, String text, PendingIntent pendingIntent, Context callingContext, int privacyLevel) {
        if(LoginActivity.user != null) {
            UserSetting us = (UserSetting) LoginActivity.user.getSetting(SHOW_NOTIFICATION_ID);
            int usersPrivacySetting = Integer.parseInt(us.getValue());
            if (usersPrivacySetting >= privacyLevel) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(callingContext, "MyChannel")
                        .setSmallIcon(R.drawable.aptimg)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) callingContext.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "Channel_Name";
                    String description = "Channel_Description";
                    NotificationChannel channel = new NotificationChannel("MyChannel", name, IMPORTANCE_HIGH);
                    channel.setDescription(description);
                    channel.setLightColor(Color.RED);
                    channel.enableVibration(true);
                    if (channel != null) {
                        notificationManager.createNotificationChannel(channel);
                        notificationManager.notify(++NOTIFICATION_ID, builder.build());
                    }
                }
            }
        }
    }
}
