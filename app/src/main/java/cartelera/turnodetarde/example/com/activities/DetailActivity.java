package cartelera.turnodetarde.example.com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import cartelera.turnodetarde.example.com.CustomLinearLayoutManager;
import cartelera.turnodetarde.example.com.R;
import cartelera.turnodetarde.example.com.adapters.LinksAdapter;
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

        tvTime.setText(sdf.format(program.getStart()));
        tvChannel.setText(program.getChannelName());
        tvProgram.setText(program.getName());
        tvContent.setText(program.getDetails());

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


    public void onClickAlarm(View v) {

    }

}
