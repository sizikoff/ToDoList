package ru.tihomirov.todolist.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import ru.tihomirov.todolist.R;
import ru.tihomirov.todolist.activities.TaskActivity;

import static ru.tihomirov.todolist.Constants.CONTENT_TEXT;
import static ru.tihomirov.todolist.Constants.CONTENT_TITLE;
import static ru.tihomirov.todolist.Constants.ID;
import static ru.tihomirov.todolist.Constants.TICKER;

public class RebootService extends BroadcastReceiver {

    Notification myNotication;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Интент для активити, которую мы хотим запускать при нажатии на уведомление
        Intent intentTL = new Intent(context, TaskActivity.class);
        intentTL.putExtra(ID, intent.getIntExtra(ID, 0));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
                intentTL, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setAutoCancel(false);
        builder.setTicker(intent.getStringExtra(TICKER));
        builder.setContentTitle(intent.getStringExtra(CONTENT_TITLE));
        builder.setContentText(intent.getStringExtra(CONTENT_TEXT));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false);
//        builder.setSubText("This is subtext...");
        builder.build();
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 500, 110, 500, 110}, -1);

        myNotication = builder.getNotification();
        nm.notify(1, myNotication);
    }
}
