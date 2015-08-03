package com.sergiomse.guiatv;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.sergiomse.guiatv.database.ProgramAlarm;


public class AlarmNotificationService extends Service {

    public AlarmNotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ProgramAlarm programAlarm = intent.getParcelableExtra("programAlarm");

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_television)
                        .setContentTitle("Tu Guia TV")
                        .setContentText("El programa \"" + programAlarm.getName() + "\" va a empezar en breve.")
                        .setAutoCancel(true);

        nm.notify(1231231, mBuilder.build());

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
