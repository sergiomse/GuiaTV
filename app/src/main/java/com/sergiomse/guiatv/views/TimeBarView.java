package com.sergiomse.guiatv.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.sergiomse.guiatv.Constansts;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by turno de tarde on 15/07/2015.
 */
public class TimeBarView extends View {

    private static final String TAG = TimeBarView.class.getSimpleName();

    private DateTime initialDate;
    private Date finalDate;
    private DateTime dayShownDate;
    private int totalWidth;


    public DateTime getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(DateTime initialDate) {
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
    private List<PositionedDate> positionedDatesList = new ArrayList<PositionedDate>();
    private DateTime nextShownDate;
    private String nextShownDateStr;
    private boolean isDayChange = false;

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

        //inicializamos en caso de null para que se vea la vista en el disenador de layouts
        if(initialDate == null) {
            initialDate = new DateTime();
            dayShownDate = initialDate;
        }

        //background
        canvas.drawRect(0, 0, sx + getWidth(), getHeight(), pntBackground);

        drawHourLines(canvas);

        drawHourText(canvas);



        int xx = 0;

        //dibujamos el dia
        xx = leftPadding;
        DateTime initial = initialDate;

        //calculamos la hora inicial y su posiciones
        while(xx < sx) {
            initial = initial.plusHours(1);
            xx += Constansts.getPxWidthPerHour(dm);
        }

        //calculamos si hay cambio de dia
        int xxx = xx;
        int x1 = 0;
        int x2 = 0;
        DateTime current = initial;
        boolean otherDayChange = false;

        while (xxx < getWidth() + sx) {

            if(current.getHourOfDay() == 0) {
                isDayChange = true;
                dayShownDate = initial;
//                int position = -1;
//                for(int i = 0; i < positionedDatesList.size(); i++) {
//                    if(positionedDatesList.get(i).getDateTime().equals(dayShownDate)) {
//                        position = i;
//                        break;
//                    }
//                }
//                if(position != -1) {
//                    x1 = positionedDatesList.get(position).getPosition();
//                } else {
//                    x1 = (int) ((xxx - sx - leftPadding) / 2.0 + sx + leftPadding);
//                    positionedDatesList.add(new PositionedDate(dayShownDate, x1));
//                }

                break;
            }

            current = current.plusHours(1);
            xxx += Constansts.getPxWidthPerHour(dm);
        }


        //dibujamos los dias

//        if(otherDayChange && ) {
//            isDayChange = true;
//
//        } else if(isDayChange){
//            dayShownDate = dayShownDate.plusDays(1);
//            nextShownDate = null;
//            isDayChange = false;
//        }

        //pintamos el dia en si
        if(!isDayChange) {
            drawNormalDay(canvas, dayShownDate);
        } else {
            drawMovingDay(canvas, dayShownDate, 0);
        }


        //dibujamos la linea horizontal inferior de separacion
        canvas.drawLine(0, getHeight() - 1, sx + getWidth(), getHeight() - 1, pntLines);
    }

    private void drawNormalDay(Canvas canvas, DateTime initialDay) {
        int sx = getScrollX();
        pntText.setAlpha(255);
        String day = initialDay.toString("EEEE d").toUpperCase();
        canvas.drawText(day, (getWidth() - leftPadding) / 2 + leftPadding + sx, getHeight() - 25 * dm.density, pntText);
    }

    private void drawMovingDay(Canvas canvas, DateTime initialDay, int changePos) {
        int sx = getScrollX();
        pntText.setAlpha(255);
        String day = initialDay.toString("EEEE d").toUpperCase();
        canvas.drawText(day, 100, getHeight() - 25 * dm.density, pntText);
        String day2 = initialDay.plusDays(1).toString("EEEE d").toUpperCase();
        canvas.drawText(day2, sx + getWidth() - 200, getHeight() - 25 * dm.density, pntText);
    }

    private void drawHourText(Canvas canvas) {
        //dibujamos las horas
        int sx = getScrollX();
        int xx = leftPadding;
        int initialHour = initialDate.getHourOfDay();
        while(xx < getWidth() + sx + 200) {
            if(initialHour == 24) {
                initialHour = 0;
            }

            String initialHourString = String.valueOf(initialHour) + ":00";
            Rect bounds = new Rect();
            pntText.getTextBounds(initialHourString, 0, initialHourString.length(), bounds);

            int textPos = 0;
            if(sx + getWidth() < xx - bounds.width() / 2) {
                pntText.setAlpha(0);

            } else if(sx + getWidth() > xx - bounds.width() / 2 && sx + getWidth() < xx + bounds.width() / 2) {
                pntText.setAlpha((int) (255 / bounds.width() * (sx + getWidth() - xx + bounds.width() / 2)));
                textPos = sx +  getWidth() - bounds.width() / 2;

            } else {
                pntText.setAlpha(255);
                textPos = (int) xx;
            }
            canvas.drawText(initialHourString, textPos, getHeight() - 10 * dm.density, pntText);
            initialHour++;
            xx += Constansts.getPxWidthPerHour(dm);
        }
    }

    private void drawHourLines(Canvas canvas) {
        //dibujamos las lineas
        int sx = getScrollX();
        int xx = leftPadding;
        while(xx < getWidth() + sx) {
            canvas.drawLine(xx, (float) getHeight(), xx, getHeight() - 5 * dm.density, pntLines);
            xx += Constansts.getPxWidthPerHour(dm);
        }
    }


    private class PositionedDate {

        private DateTime date;
        private int position;

        public PositionedDate(DateTime date, int position) {
            this.date = date;
            this.position = position;
        }

        public DateTime getDateTime() {
            return date;
        }

        public void setDateTime(DateTime date) {
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
