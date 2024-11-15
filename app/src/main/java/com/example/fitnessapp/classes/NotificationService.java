package com.example.fitnessapp.classes;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.fitnessapp.R;
import com.example.fitnessapp.activitys.MainActivity;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "MyNotificationChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("ACTION_TRIGGER_NOTIFICATION")) {
                // When the service receives the broadcast to trigger the notification,
                // create and show the notification
                long timeInMillis = intent.getLongExtra("timeInMillis", 0);
                createNotification(getApplicationContext(), timeInMillis);
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification(Context context, long timeInMillis) {
        // Ensure the notification channel is created
        createNotificationChannel(context);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.dumbbells)
                .setContentTitle("Training Reminder")
                .setContentText("Don't forget to do your training!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create a PendingIntent to launch the app when the notification is tapped
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // This method is called to schedule the notification at a specific time
    public static void scheduleNotification(Context context, long timeInMillis) {
        // Create an intent to trigger the notification
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction("ACTION_TRIGGER_NOTIFICATION");
        intent.putExtra("timeInMillis", timeInMillis);

        // Create a PendingIntent for the intent
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager and schedule the PendingIntent at the specified time
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
}
