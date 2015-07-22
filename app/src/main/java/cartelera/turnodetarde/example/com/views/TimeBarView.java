package cartelera.turnodetarde.example.com.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cartelera.turnodetarde.example.com.R;

/**
 * Created by turno de tarde on 15/07/2015.
 */
public class TimeBarView extends View {

    private static final String TAG = TimeBarView.class.getSimpleName();

    private Date initialDate;
    private Date finalDate;
    private int totalWidth;


    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(int totalWidth) {
        this.totalWidth = totalWidth;
    }





    private DisplayMetrics dm;
    private int leftPadding;

    private Paint pntBackground = new Paint();
    private Paint pntLines = new Paint();
    private Paint pntText = new Paint();

    private Drawable leftGradient;

    public TimeBarView(Context context) {
        super(context);
        init();
    }

    public TimeBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        dm = getContext().getResources().getDisplayMetrics();

        leftPadding = (int) (72 * dm.density - 1);

        pntBackground.setColor(Color.rgb(236, 236, 236));
        pntLines.setColor(Color.rgb(222, 222, 222));
        pntText.setColor(Color.BLACK);
        pntText.setTextSize(14 * dm.density);
        pntText.setAntiAlias(true);

        leftGradient = getResources().getDrawable(R.drawable.time_gradient);
        leftGradient.setBounds(0, 0, (int) (72 * dm.density), (int) (48 * dm.density));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int sx = getScrollX();

        //background
        canvas.drawRect(0, 0, sx + getWidth(), getHeight(), pntBackground);

        float x = leftPadding;
        Calendar cal = Calendar.getInstance();
        if(initialDate == null) {
            initialDate = new Date();
        }
        cal.setTime(initialDate);
        int initialHour = cal.get(Calendar.HOUR_OF_DAY);
        float increment = 100 * dm.density;

        boolean isDayChange = false;
        int dayChangePos = 0;

        while(x < getWidth() + sx + 100) {
            canvas.drawLine(x, (float) getHeight(), x, getHeight() - 5 * dm.density, pntLines);

            String initialHourString = String.valueOf(initialHour) + ":00";
            Rect bounds = new Rect();
            pntText.getTextBounds(initialHourString, 0, initialHourString.length(), bounds);

            int textPos = 0;
            if(sx + getWidth() < x - bounds.width() / 2) {
                pntText.setAlpha(0);

            } else if(sx + getWidth() > x - bounds.width() / 2 && sx + getWidth() < x + bounds.width() / 2) {
                pntText.setAlpha((int) (255 / bounds.width() * (sx + getWidth() - x + bounds.width() / 2)));
                textPos = sx +  getWidth() - bounds.width();

            } else {
                pntText.setAlpha(255);
                textPos = (int) (x - bounds.width() / 2);
            }
            canvas.drawText(initialHourString, textPos, getHeight() - 15 * dm.density, pntText);

            leftGradient.draw(canvas);

            initialHour++;
            if(initialHour == 25) {
                initialHour = 1;
                isDayChange = true;
                dayChangePos = (int) x;
            }

            x += increment;
        }

        canvas.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, pntLines);
    }
}
