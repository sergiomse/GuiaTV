package cartelera.turnodetarde.example.com;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
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
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private HorizontalScrollView scrollTimeBar;
    private LinearLayout iconsLayout;
    private HorizontalScrollView scrollProgramsView;
    private LinearLayout programsLayout;
    private TimeBarView timeBarView;
    private RecyclerView recyclerView;

    private List<Channel> channels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        for(Channel channel : channels) {

            MyRecyclerView recyclerView = new MyRecyclerView(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);

            LinearLayoutManager lManager = new LinearLayoutManager(this);
            lManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerView.setLayoutManager(lManager);
            ChannelAdapter adapter = new ChannelAdapter(this, channel);
            recyclerView.setAdapter(adapter);

            recyclerView.setOnTouchListener(onChannelTouchListener);
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


    private class OnChannelTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            int scrollX = ((MyRecyclerView) v).getHorizontalOffset();
            //get first visible item
            View firstVisibleItem = mLLM.findViewByPosition(mLLM.findFirstVisibleItemPosition());

            int leftScrollXCalculated = 0;
            if (firstItemPosition == 0){
                //if first item, get width of headerview (getLeft() < 0, that's why I Use Math.abs())
                leftScrollXCalculated = Math.abs(firstVisibleItem.getLeft());
            }
            else{

                //X-Position = Gap to the right + Number of cells * width - cell offset of current first visible item
                //(mHeaderItemWidth includes already width of one cell, that's why I have to subtract it again)
                leftScrollXCalculated = (mHeaderItemWidth - mCellWidth) + firstItemPosition  * mCellWidth + firstVisibleItem.getLeft();
            }
            timeBarView.setPosX(scrollX);
            return false;
        }
    }

    private OnChannelTouchListener onChannelTouchListener = new OnChannelTouchListener();

}
