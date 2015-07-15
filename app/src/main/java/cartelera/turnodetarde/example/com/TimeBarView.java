package cartelera.turnodetarde.example.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by turno de tarde on 15/07/2015.
 */
public class TimeBarView extends View {


    private Date initialDate;
    private Date finalDate;

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



    DisplayMetrics dm;

    private Paint pntBackground = new Paint();
    private Paint pntLines = new Paint();

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

        pntBackground.setColor(Color.rgb(236, 236, 236));
        pntLines.setColor(Color.rgb(222, 222, 222));
//        pntLines.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(initialDate != null && finalDate != null) {
            long millisDiff = finalDate.getTime() - initialDate.getTime();
            double hoursDiff = millisDiff / 3600000;
            int widthPixels = (int) (hoursDiff * 100 * dm.density);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthPixels, MeasureSpec.getMode(widthMeasureSpec));
        } else {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.getMode(widthMeasureSpec));
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), pntBackground);

        float x = 0;
        float increment = 100 * dm.density;
        while(x<getWidth()) {
            canvas.drawLine(x, (float) getHeight(), x, getHeight() - 5 * dm.density, pntLines);
            x += increment;
        }
    }
}
