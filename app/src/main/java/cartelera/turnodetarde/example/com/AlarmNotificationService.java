package cartelera.turnodetarde.example.com;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmNotificationService extends Service {
    public AlarmNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
