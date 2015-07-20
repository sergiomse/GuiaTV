package cartelera.turnodetarde.example.com.activities
        ;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cartelera.turnodetarde.example.com.Channel;
import cartelera.turnodetarde.example.com.views.ChannelAdapter;
import cartelera.turnodetarde.example.com.views.MyRecyclerView;
import cartelera.turnodetarde.example.com.R;
import cartelera.turnodetarde.example.com.views.TimeBarView;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

//    private HorizontalScrollView scrollTimeBar;
    private LinearLayout iconsLayout;
    private HorizontalScrollView scrollProgramsView;
    private LinearLayout programsLayout;
    private TimeBarView timeBarView;
    private RecyclerView recyclerView;
    private LinearLayoutManager lManager;

    private int overallXScroll = 0;
    private DisplayMetrics dm;

    private List<Channel> channels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm = getResources().getDisplayMetrics();

        timeBarView = (TimeBarView) findViewById(R.id.timeBarView);
        Calendar calendar = new GregorianCalendar(2015, 6, 15, 20, 0);
        timeBarView.setInitialDate(calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, 4);
        timeBarView.setFinalDate(calendar.getTime());
        timeBarView.invalidate();

//        scrollTimeBar = (HorizontalScrollView) findViewById(R.id.scrollTimeBar);
        iconsLayout = (LinearLayout) findViewById(R.id.iconsLayout);
//        scrollProgramsView = (HorizontalScrollView) findViewById(R.id.scrollProgramsView);
        programsLayout = (LinearLayout) findViewById(R.id.programsLayout);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerTest);

//        scrollProgramsView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int scrollX = scrollProgramsView.getScrollX();
//                timeBarView.setPosX(scrollX);
//                return false;
//            }
//        });


        readData();
        drawData();
    }

    private void drawData() {

        //calculate max width between all the RecyclerView's
        long maxDiff = 0;
        for(Channel channel : channels) {
            Date startDate = channel.getPrograms().get(0).getStart();
            Date finishDate = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            long diff = finishDate.getTime() - startDate.getTime();
            if(diff > maxDiff) {
                maxDiff = diff;
            }
        }
        int maxWidth = (int) (100 * dm.density * maxDiff / 3600000);

        for(Channel channel : channels) {

            MyRecyclerView recyclerView = new MyRecyclerView(this);
            recyclerView.setMaxWidth(maxWidth);
            recyclerView.setTag(channel.getName());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);

            lManager = new LinearLayoutManager(this);
            lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(lManager);

            ChannelAdapter adapter = new ChannelAdapter(this, channel);
            recyclerView.setAdapter(adapter);

            recyclerView.setOnScrollListener(onChannelScrollListener);
            programsLayout.addView(recyclerView);

        }
    }

    private void readData() {
        String line = null;
        StringBuilder json = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.test)));

        try {

            line = reader.readLine();
            while (line != null) {
                json.append(line);
                json.append("\n");
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    reader = null;
                }
            }
        }

        Gson gson = new Gson();
        channels = gson.fromJson(json.toString(), new TypeToken<List<Channel>>(){}.getType());
        Collections.sort(channels);
    }


    private class OnChannelScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            overallXScroll = overallXScroll + dx;

            timeBarView.scrollTo(overallXScroll, 0);

            int childCount = programsLayout.getChildCount();
            for(int i=0; i<childCount; i++) {
                View v = programsLayout.getChildAt(i);
                if(v instanceof MyRecyclerView) {
                    MyRecyclerView rv = (MyRecyclerView) v;
                    if(rv != recyclerView) {
                        rv.scrollBy(dx, 0);
                    }
                }
            }
        }

    }

    private OnChannelScrollListener onChannelScrollListener = new OnChannelScrollListener();

}
