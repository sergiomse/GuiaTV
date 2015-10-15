package com.sergiomse.guiatv.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.transition.ChangeClipBounds;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.sergiomse.guiatv.Constansts;
import com.sergiomse.guiatv.ProgramSerializer;
import com.sergiomse.guiatv.R;
import com.sergiomse.guiatv.adapters.ProgramsAdapter;
import com.sergiomse.guiatv.model.Channel;
import com.sergiomse.guiatv.model.ChannelList;
import com.sergiomse.guiatv.model.Program;
import com.sergiomse.guiatv.model.ProgramComponentList;
import com.sergiomse.guiatv.views.ProgramsRecyclerView;
import com.sergiomse.guiatv.views.TimeBarView;

import org.joda.time.DateTime;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

    private LinearLayout iconsLayout;
    private HorizontalScrollView scrollProgramsView;
    private LinearLayout programsLayout;
    private TimeBarView timeBarView;
    private RecyclerView recyclerView;
    private LinearLayoutManager lManager;
    private ImageView timeLine;

    private int totalXScroll = 0;
    private DisplayMetrics dm;
    private DateTime now;

    private ChannelList channels = new ChannelList();

    public ChannelList getChannels() {
        return channels;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // set an exit transition
            getWindow().setExitTransition(new ChangeClipBounds());
        }
        setContentView(R.layout.activity_main);

        now = new DateTime();

        dm = getResources().getDisplayMetrics();

        timeBarView = (TimeBarView) findViewById(R.id.timeBarView);
        timeBarView.setInitialDate(now);

        iconsLayout = (LinearLayout) findViewById(R.id.iconsLayout);
        programsLayout = (LinearLayout) findViewById(R.id.programsLayout);
        timeLine = (ImageView) findViewById(R.id.timeLine);


        receiveData();
//        readData();

    }



    private void receiveData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://jbossas-semise.rhcloud.com/Guia";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new GsonBuilder().registerTypeAdapter(Program.class, new ProgramSerializer()).create();
                        channels = gson.fromJson(response, new TypeToken<ChannelList>(){}.getType());
                        for(Channel channel : channels) {
                            for(Program program : channel.getPrograms()) {
                                program.setChannelName(channel.getName());
                            }

                            Collections.sort(channel.getPrograms());
                        }
                        Collections.sort(channels);

                        drawData();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        timeLine.setVisibility(View.GONE);

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "Error " + error, Toast.LENGTH_LONG).show();
                    }
                });

        progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Loading...");
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    private void drawData() {

        channels.setNow(now);
        Map<Channel, ProgramComponentList> map = channels.getProgramComponents();

        for(Map.Entry<Channel, ProgramComponentList> entry : map.entrySet()) {
            Channel channel = entry.getKey();
            ProgramComponentList programComponentList = entry.getValue();

            ProgramsRecyclerView recyclerView = new ProgramsRecyclerView(this);
            recyclerView.setTag(channel.getName());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);

            lManager = new LinearLayoutManager(this);
            lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(lManager);

            ProgramsAdapter adapter = new ProgramsAdapter(this, programComponentList);
            recyclerView.setAdapter(adapter);

            recyclerView.setOnScrollListener(onChannelScrollListener);
            programsLayout.addView(recyclerView);
        }

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) timeLine.getLayoutParams();
        params.leftMargin = (int) (now.getMinuteOfHour() / 60.0 * Constansts.DP_WIDTH_PER_HOUR * dm.density);
        timeLine.setVisibility(View.VISIBLE);

    }



//    private void readData() {
//        String line = null;
//        StringBuilder json = new StringBuilder();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.test)));
//
//        try {
//
//            line = reader.readLine();
//            while (line != null) {
//                json.append(line);
//                json.append("\n");
//                line = reader.readLine();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    reader = null;
//                }
//            }
//        }
//
//        Gson gson = new Gson();
//        channels = gson.fromJson(json.toString(), new TypeToken<ChannelList>(){}.getType());
//
//        for(Channel channel : channels) {
//            for(Program program : channel.getPrograms()) {
//                program.setChannelName(channel.getName());
//            }
//        }
//        Collections.sort(channels);
//    }



    private class OnChannelScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            totalXScroll = totalXScroll + dx;

            timeBarView.scrollTo(totalXScroll, 0);

            int childCount = programsLayout.getChildCount();
//            for(int i=0; i<childCount; i++) {
//                View v = programsLayout.getChildAt(i);
//                if (v instanceof ProgramsRecyclerView) {
//                    ProgramsRecyclerView rv = (ProgramsRecyclerView) v;
//                    rv.removeOnScrollListener(onChannelScrollListener);
//                }
//            }
            for(int i=0; i<childCount; i++) {
                View v = programsLayout.getChildAt(i);
                if(v instanceof ProgramsRecyclerView) {
                    ProgramsRecyclerView rv = (ProgramsRecyclerView) v;
                    if(rv != recyclerView) {
                        rv.setOnScrollListener(null);
                        rv.scrollBy(dx, 0);
                        rv.setOnScrollListener(onChannelScrollListener);

                    }
                }
            }
//            for(int i=0; i<childCount; i++) {
//                View v = programsLayout.getChildAt(i);
//                if (v instanceof ProgramsRecyclerView) {
//                    ProgramsRecyclerView rv = (ProgramsRecyclerView) v;
//                    rv.addOnScrollListener(onChannelScrollListener);
//                }
//            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) timeLine.getLayoutParams();
            params.leftMargin = params.leftMargin - dx;
            timeLine.setLayoutParams(params);
        }
    }

    private OnChannelScrollListener onChannelScrollListener = new OnChannelScrollListener();

}
