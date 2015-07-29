package cartelera.turnodetarde.example.com.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cartelera.turnodetarde.example.com.AlarmNotificationService;
import cartelera.turnodetarde.example.com.CustomLinearLayoutManager;
import cartelera.turnodetarde.example.com.R;
import cartelera.turnodetarde.example.com.adapters.LinksAdapter;
import cartelera.turnodetarde.example.com.database.GuiaDb;
import cartelera.turnodetarde.example.com.database.ProgramAlarm;
import cartelera.turnodetarde.example.com.model.Link;
import cartelera.turnodetarde.example.com.model.Program;


public class DetailActivity extends Activity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private WebView webView;
    private ProgressDialog progressDialog;
    private Program program;

    private TextView tvTime;
    private TextView tvChannel;
    private TextView tvProgram;
    private TextView tvContent;

    private ImageView ivAlarm;

    private RecyclerView linksRecyclerView;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        program = getIntent().getParcelableExtra("program");

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvChannel = (TextView) findViewById(R.id.tvChannel);
        tvProgram = (TextView) findViewById(R.id.tvProgram);
        tvContent = (TextView) findViewById(R.id.tvContent);

        ivAlarm = (ImageView) findViewById(R.id.ivAlarm);

        tvTime.setText(sdf.format(program.getStart()));
        tvChannel.setText(program.getChannelName());
        tvProgram.setText(program.getName());
        tvContent.setText(program.getDetails());

        if(isAlarmSetForProgram()) {
            ivAlarm.setImageResource(R.drawable.ic_alarm_on);
        } else {
            ivAlarm.setImageResource(R.drawable.ic_alarm);
        }

        linksRecyclerView = (RecyclerView) findViewById(R.id.linksRecyclerView);

        CustomLinearLayoutManager lManager = new CustomLinearLayoutManager(this);
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        linksRecyclerView.setLayoutManager(lManager);

        Link[] links = program.getLinks();
        if(links == null) {
            links = new Link[0];
        }

        LinksAdapter linksAdapter = new LinksAdapter(this,linksRecyclerView, links);
        linksRecyclerView.setAdapter(linksAdapter);

    }

    private boolean isAlarmSetForProgram() {
        GuiaDb db = new GuiaDb(this);
        ProgramAlarm p = db.getProgramAlarmById(program.getId());
        db.cleanup();
        return p != null;
    }

    public void onClickAlarm(View v) {
        if(isAlarmSetForProgram()) {
            GuiaDb db = new GuiaDb(this);
            db.deleteProgramAlarmById(program.getId());
            db.cleanup();
            ivAlarm.setImageResource(R.drawable.ic_alarm);
            Toast.makeText(this, "Eliminado aviso para \"" + program.getName() + "\"", Toast.LENGTH_SHORT).show();

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmNotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            alarmManager.cancel(pendingIntent);

        } else {
            GuiaDb db = new GuiaDb(this);
            ProgramAlarm programAlarm = new ProgramAlarm();
            programAlarm.setId(program.getId());
            Date alarm = new Date(program.getStart().getTime() - 5L * 60L * 1000L);
            programAlarm.setAlarm(alarm);
            programAlarm.setStart(program.getStart());
            programAlarm.setName(program.getName());
            programAlarm.setChannel(program.getChannelName());
            db.insertProgramAlarm(programAlarm);
            db.cleanup();
            ivAlarm.setImageResource(R.drawable.ic_alarm_on);
            Toast.makeText(this, "Activado aviso para \"" + program.getName() + "\"", Toast.LENGTH_SHORT).show();

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmNotificationService.class);
            intent.putExtra("programAlarm", programAlarm);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, programAlarm.getAlarm().getTime(), pendingIntent);
        }
    }

}
