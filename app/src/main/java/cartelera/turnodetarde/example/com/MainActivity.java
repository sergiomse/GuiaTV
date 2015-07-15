package cartelera.turnodetarde.example.com;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
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

        scrollTimeBar = (HorizontalScrollView) findViewById(R.id.scrollTimeBar);
        iconsLayout = (LinearLayout) findViewById(R.id.iconsLayout);
        scrollProgramsView = (HorizontalScrollView) findViewById(R.id.scrollProgramsView);
        programsLayout = (LinearLayout) findViewById(R.id.programsLayout);

        scrollProgramsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int scrollX = scrollProgramsView.getScrollX();
                int scrollY = scrollProgramsView.getScrollY();
                scrollTimeBar.scrollTo(scrollX, scrollY);
                return false;
            }
        });


        readData();
        drawData();
    }

    private void drawData() {
        for(Channel channel : channels) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 70);
            linearLayout.setLayoutParams(params);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
