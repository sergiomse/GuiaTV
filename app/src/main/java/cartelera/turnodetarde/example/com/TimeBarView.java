package cartelera.turnodetarde.example.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by turno de tarde on 15/07/2015.
 */
public class TimeBarView extends View {

    private int posX;
    private Date initialDate;
    private Date finalDate;

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = -posX;
        invalidate();
    }

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
//        pntLines.setColor(Color.BLACK);
        pntText.setColor(Color.BLACK);
        pntText.setTextSize(14 * dm.density);
        pntText.setAntiAlias(true);

        leftGradient = getResources().getDrawable(R.drawable.time_gradient);
        leftGradient.setBounds(0, 0, (int) (72 * dm.density), (int) (48 * dm.density));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        if(initialDate != null && finalDate != null) {
//            long millisDiff = finalDate.getTime() - initialDate.getTime();
//            double hoursDiff = millisDiff / 3600000;
//            totalWidth = (int) (hoursDiff * 100 * dm.density);
//        } else {
//            totalWidth = 0;
//        }


        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), pntBackground);

        float x = posX;
        Calendar cal = Calendar.getInstance();
        if(initialDate == null) {
            initialDate = new Date();
        }
        cal.setTime(initialDate);
        int initialHour = cal.get(Calendar.HOUR_OF_DAY);
        float increment = 100 * dm.density;
        while(x < getWidth()) {
            canvas.drawLine(x + leftPadding, (float) getHeight(), x + leftPadding, getHeight() - 5 * dm.density, pntLines);

            String initialHourString = String.valueOf(initialHour) + ":00";
            Rect bounds = new Rect();
            pntText.getTextBounds(initialHourString, 0, initialHourString.length(), bounds);

//            if(posX > x - bounds.width() / 2 && posX < x + bounds.width() / 2) {
//                pntText.setAlpha((int) (255 * (1 - (posX - x) / bounds.width())));
//                canvas.drawText(initialHourString, posX, getHeight() - 15 * dm.density, pntText);
//            } else {
//                pntText.setAlpha(255);
//                canvas.drawText(initialHourString, x - bounds.width() / 2, getHeight() - 15 * dm.density, pntText);
//            }

            pntText.setAlpha(255);
            canvas.drawText(initialHourString, x + leftPadding - bounds.width() / 2, getHeight() - 15 * dm.density, pntText);

            leftGradient.draw(canvas);

            initialHour++;
            x += increment;
        }

        canvas.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, pntLines);
    }
}
