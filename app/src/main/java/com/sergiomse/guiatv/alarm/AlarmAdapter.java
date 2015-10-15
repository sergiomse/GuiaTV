package com.sergiomse.guiatv.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sergiomse.guiatv.database.GuiaDb;
import com.sergiomse.guiatv.model.Program;

import org.joda.time.DateTime;

/**
 * Created by sergiomse@gmail.com on 07/08/2015.
 */
public class AlarmAdapter {

    private Context context;

    public AlarmAdapter(Context context) {
        this.context = context;
    }

    public void setAlarm(Program program) {
        GuiaDb db = new GuiaDb(context);
        db.insertProgramAlarm(program);
        Program firstAlarm = db.getFirstProgramAlarm();
        db.cleanup();

        putInAlarmManager(firstAlarm);

    }

    public void removeAlarm(Program program) {
        GuiaDb db = new GuiaDb(context);
        db.deleteProgramAlarm(program);
        Program firstAlarm = db.getFirstProgramAlarm();
        db.cleanup();

        cancelInAlarmManager(program);
        putInAlarmManager(firstAlarm);
    }

    private void cancelInAlarmManager(Program program) {
        if(program != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmNotificationService.class);
            intent.putExtra("program", program);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }


    private void putInAlarmManager(Program program) {
        if(program != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmNotificationService.class);
            intent.putExtra("program", program);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
//            alarmManager.set(AlarmManager.RTC, program.getStart().minusMinutes(5).getMillis(), pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, DateTime.now().plusMinutes(1).getMillis(), pendingIntent);
        }
    }
}
