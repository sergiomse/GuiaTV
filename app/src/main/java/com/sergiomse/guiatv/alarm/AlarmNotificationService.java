package com.sergiomse.guiatv.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.sergiomse.guiatv.R;
import com.sergiomse.guiatv.activities.DetailActivity;
import com.sergiomse.guiatv.database.GuiaDb;
import com.sergiomse.guiatv.database.ProgramAlarm;
import com.sergiomse.guiatv.model.Program;


public class AlarmNotificationService extends Service {

    public AlarmNotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Program program = intent.getParcelableExtra("program");
        Intent intentDetails = new Intent(this, DetailActivity.class);
        intentDetails.putExtra("program", program);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentDetails, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmAdapter alarmAdapter = new AlarmAdapter(this);
        alarmAdapter.removeAlarm(program);

        RemoteViews notifView = new RemoteViews(getPackageName(), R.layout.notification);
        notifView.setTextViewText(R.id.tvNotificationProgram, program.getStart().toString("HH.mm") + " " + program.getName());

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setContent(notifView)
                        .setSmallIcon(R.drawable.ic_notification_small_tv)
//                        .setLargeIcon(bitmapTelevision)
//                        .setContentTitle("Tu Guia TV")
//                        .setContentText("El programa \"" + program.getName() + "\" va a empezar en breve.")
                        .setAutoCancel(true)
                        .setVibrate(new long[]{100, 400, 300, 400, 800})
                        .setLights(Color.rgb(255, 193, 7), 200, 1000)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        nm.notify(1231231, mBuilder.build());

        stopSelf(startId);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
