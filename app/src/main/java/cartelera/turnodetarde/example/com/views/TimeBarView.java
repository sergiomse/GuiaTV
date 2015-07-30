package cartelera.turnodetarde.example.com.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cartelera.turnodetarde.example.com.Constansts;

/**
 * Created by turno de tarde on 15/07/2015.
 */
public class TimeBarView extends View {

    private static final String TAG = TimeBarView.class.getSimpleName();

    private Date initialDate;
    private Date finalDate;
    private Date dayShownDate;
    private int totalWidth;


    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
        this.dayShownDate = initialDate;
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


    private SimpleDateFormat daySdf = new SimpleDateFormat("EEEE d");
    private long oneDay = 24 * 60 * 60 * 1000;
    private List<PositionedDate> positionedDatesList = new ArrayList<>();
    private Date nextShownDate;
    private String nextShownDateStr;

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
//        pntLines.setColor(Color.rgb(222, 222, 222));
        pntText.setColor(Color.BLACK);
        pntLines.setColor(Color.BLACK);
        pntText.setTextSize(14 * dm.density);
        pntText.setAntiAlias(true);
        pntText.setTextAlign(Paint.Align.CENTER);

    }


    //FIXME Modificar para organizarlo mejor y solucionar algunos problemas
    @Override
    protected void onDraw(Canvas canvas) {
        int sx = getScrollX();

        //background
        canvas.drawRect(0, 0, sx + getWidth(), getHeight(), pntBackground);

        float x = leftPadding;
        Calendar cal = Calendar.getInstance();
        if(initialDate == null) {
            initialDate = new Date();
            dayShownDate = initialDate;
        }
        cal.setTime(initialDate);
        int initialHour = cal.get(Calendar.HOUR_OF_DAY);
        float increment = Constansts.DP_WIDTH_PER_HOUR * dm.density;

        boolean isDayChange = false;
        int x1 = 0;
        int x2 = 0;
        int alpha = 255;

        while(x < getWidth() + sx + 100) {

            if(initialHour == 24) {
                initialHour = 0;
                if(x < getWidth() + sx) {
                    isDayChange = true;

                    int position = -1;
                    for(int i = 0; i < positionedDatesList.size(); i++) {
                        if(positionedDatesList.get(i).getDate().equals(dayShownDate)) {
                            position = i;
                            break;
                        }
                    }
                    if(position != -1) {
                        x1 = positionedDatesList.get(position).getPosition();
                    } else {
                        x1 = (int) ((x - sx - leftPadding) / 2 + sx + leftPadding);
                        positionedDatesList.add(new PositionedDate(dayShownDate, x1));
                    }

                    if(nextShownDate == null) {
                        nextShownDate = new Date(dayShownDate.getTime() + oneDay);
                    }

                    nextShownDateStr = nextShownDate != null ? daySdf.format(nextShownDate).toUpperCase() : "";

                    Rect bounds = new Rect();
                    pntText.getTextBounds(nextShownDateStr, 0, nextShownDateStr.length(), bounds);

                    if( (sx + getWidth() + x + bounds.width()) / 2 > sx + getWidth()) {
                        x2 = sx + getWidth() - bounds.width() / 2;
                        alpha = (int) (255 / bounds.width() * (sx + getWidth() - x + bounds.width() / 2));
                    } else {
                        x2 = (int) ((sx + getWidth() + x) / 2.0);
                    }

                    if(x2 < (getWidth() - leftPadding) / 2 + sx + leftPadding) {
                        x2 = (getWidth() - leftPadding) / 2 + leftPadding + sx;
                    }
                }
            }

            canvas.drawLine(x, (float) getHeight(), x, getHeight() - 5 * dm.density, pntLines);

            String initialHourString = String.valueOf(initialHour) + ":00";
            Rect bounds = new Rect();
            pntText.getTextBounds(initialHourString, 0, initialHourString.length(), bounds);

            int textPos = 0;
            if(sx + getWidth() < x - bounds.width() / 2) {
                pntText.setAlpha(0);

            } else if(sx + getWidth() > x - bounds.width() / 2 && sx + getWidth() < x + bounds.width() / 2) {
                pntText.setAlpha((int) (255 / bounds.width() * (sx + getWidth() - x + bounds.width() / 2)));
                textPos = sx +  getWidth() - bounds.width() / 2;

            } else {
                pntText.setAlpha(255);
                textPos = (int) x;
            }
            canvas.drawText(initialHourString, textPos, getHeight() - 10 * dm.density, pntText);

            initialHour++;
            x += increment;
        }


        String day = daySdf.format(dayShownDate).toUpperCase();
        pntText.setAlpha(255);
        if(!isDayChange) {
            canvas.drawText(day, (getWidth() - leftPadding) / 2 + leftPadding + sx, getHeight() - 25 * dm.density, pntText);
        } else {

            canvas.drawText(day, x1, getHeight() - 25 * dm.density, pntText);
            pntText.setAlpha(alpha);
            canvas.drawText(nextShownDateStr, x2, getHeight() - 25 * dm.density, pntText);
        }

        canvas.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, pntLines);
    }




    private class PositionedDate {

        private Date date;
        private int position;

        public PositionedDate(Date date, int position) {
            this.date = date;
            this.position = position;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }


        @Override
        public boolean equals(Object o) {
            if(o == null) {
                return false;
            }

            if(!(o instanceof PositionedDate)) {
                return false;
            }

            PositionedDate pd = (PositionedDate) o;
            if(date.equals(pd.date)) {
                return true;
            }

            return false;
        }
    }
}
