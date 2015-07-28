package cartelera.turnodetarde.example.com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import cartelera.turnodetarde.example.com.R;
import cartelera.turnodetarde.example.com.model.Program;


public class DetailActivity extends Activity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private WebView webView;
    private ProgressDialog progressDialog;
    private Program program;

    private TextView tvTime;
    private TextView tvChannel;
    private TextView tvProgram;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        program = getIntent().getParcelableExtra("program");

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvChannel = (TextView) findViewById(R.id.tvChannel);
        tvProgram = (TextView) findViewById(R.id.tvProgram);

        tvTime.setText(sdf.format(program.getStart()));
        tvChannel.setText(program.getChannelName());
        tvProgram.setText(program.getName());

        webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        progressDialog = ProgressDialog.show(DetailActivity.this, "Loading Details", "Loading...");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(DetailActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
//                alertDialog.setTitle("Error");
//                alertDialog.setMessage(description);
//                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//                alertDialog.show();
            }
        });
        webView.loadUrl(program.getDetailsUrl());
    }

}
