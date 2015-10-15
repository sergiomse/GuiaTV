package com.sergiomse.guiatv.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sergiomse.guiatv.alarm.AlarmAdapter;
import com.sergiomse.guiatv.CustomLinearLayoutManager;
import com.sergiomse.guiatv.R;
import com.sergiomse.guiatv.adapters.LinksAdapter;
import com.sergiomse.guiatv.database.GuiaDb;
import com.sergiomse.guiatv.model.Link;
import com.sergiomse.guiatv.model.Program;

import org.joda.time.DateTime;


public class DetailActivity extends Activity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private Program program;

    private TextView tvTime;
    private TextView tvChannel;
    private TextView tvProgram;
    private TextView tvContent;

    private ImageView ivAlarm;

    private RecyclerView linksRecyclerView;



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

        tvTime.setText(program.getStart().toString("HH.mm"));
        tvChannel.setText(program.getChannelName());
        tvProgram.setText(program.getName());
        tvContent.setText(program.getDetails());

        //is program already started
        if(program.getStart().minusMinutes(5).compareTo(DateTime.now()) > 0) {
            if (isAlarmSetForProgram()) {
                ivAlarm.setImageResource(R.drawable.ic_alarm_on);
            } else {
                ivAlarm.setImageResource(R.drawable.ic_alarm);
            }
        } else {
            ivAlarm.setVisibility(View.GONE);
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
        boolean isSet = db.existProgramInDb(program);
        db.cleanup();
        return isSet;
    }

    public void onClickAlarm(View v) {
        if(isAlarmSetForProgram()) {
            ivAlarm.setImageResource(R.drawable.ic_alarm);
            Toast.makeText(this, "Eliminado aviso para \"" + program.getName() + "\"", Toast.LENGTH_SHORT).show();

            AlarmAdapter alarmAdapter = new AlarmAdapter(this);
            alarmAdapter.removeAlarm(program);

        } else {
            ivAlarm.setImageResource(R.drawable.ic_alarm_on);
            Toast.makeText(this, "Activado aviso para \"" + program.getName() + "\"", Toast.LENGTH_SHORT).show();

            AlarmAdapter alarmAdapter = new AlarmAdapter(this);
            alarmAdapter.setAlarm(program);
        }
    }

}
