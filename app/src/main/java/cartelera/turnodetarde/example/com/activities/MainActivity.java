package cartelera.turnodetarde.example.com.activities
        ;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;

import cartelera.turnodetarde.example.com.R;
import cartelera.turnodetarde.example.com.adapters.ProgramsAdapter;
import cartelera.turnodetarde.example.com.model.Channel;
import cartelera.turnodetarde.example.com.model.ChannelList;
import cartelera.turnodetarde.example.com.model.ProgramComponentList;
import cartelera.turnodetarde.example.com.views.ProgramsRecyclerView;
import cartelera.turnodetarde.example.com.views.TimeBarView;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LinearLayout iconsLayout;
    private HorizontalScrollView scrollProgramsView;
    private LinearLayout programsLayout;
    private TimeBarView timeBarView;
    private RecyclerView recyclerView;
    private LinearLayoutManager lManager;

    private int overallXScroll = 0;
    private DisplayMetrics dm;

    private ChannelList channels = new ChannelList();

    public ChannelList getChannels() {
        return channels;
    }

//    public void setChannels(ChannelList channels) {
//        this.channels = channels;
//    }

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

        iconsLayout = (LinearLayout) findViewById(R.id.iconsLayout);
        programsLayout = (LinearLayout) findViewById(R.id.programsLayout);



        readData();
        drawData();
    }

    private void drawData() {

        Map<Channel, ProgramComponentList> map = channels.getProgramComponents();

        timeBarView.setTotalWidth((int) ((channels.getMaxWidthDp() + 72) * dm.density));

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
        channels = gson.fromJson(json.toString(), new TypeToken<ChannelList>(){}.getType());
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
                if(v instanceof ProgramsRecyclerView) {
                    ProgramsRecyclerView rv = (ProgramsRecyclerView) v;
                    if(rv != recyclerView) {
                        rv.setOnScrollListener(null);
                        rv.scrollBy(dx, 0);
                        rv.setOnScrollListener(onChannelScrollListener);
                    }
                }
            }
        }

    }

    private OnChannelScrollListener onChannelScrollListener = new OnChannelScrollListener();

}
