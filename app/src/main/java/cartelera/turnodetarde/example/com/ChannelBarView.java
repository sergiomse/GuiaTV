package cartelera.turnodetarde.example.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sergiomse@gmail.com on 16/07/2015.
 */
public class ChannelBarView extends View {

    private List<Channel> channels;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        invalidate();
    }



    private final static int BAR_HEIGHT_DP = 72;


    private DisplayMetrics dm;
    private Paint pntLines = new Paint();
    private Paint pntTimeText = new Paint();
    private SimpleDateFormat sdfTimeText = new SimpleDateFormat("HH.mm");
    private int barHeightPx;


    public ChannelBarView(Context context) {
        super(context);
        init();
    }

    public ChannelBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChannelBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setWillNotDraw(false);
        dm = getContext().getResources().getDisplayMetrics();
        pntLines.setColor(Color.rgb(222, 222, 222));
        pntLines.setStyle(Paint.Style.STROKE);
        pntLines.setStrokeWidth(1);
        pntTimeText.setColor(Color.rgb(82, 157, 166));
        pntTimeText.setTextSize(16 * dm.density);
        Typeface typefaceTime = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        pntTimeText.setTypeface(typefaceTime);

        barHeightPx = (int) (BAR_HEIGHT_DP * dm.density);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = (int) (72 * dm.density * channels.size());

        long maxDiff = 0;
        for(Channel channel : channels) {
            Date startDate = channel.getPrograms().get(0).getStart();
            Date finishDate = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            long diff = finishDate.getTime() - startDate.getTime();
            if(diff > maxDiff) {
                maxDiff = diff;
            }
        }
        int width = (int) (100 * dm.density * maxDiff / 3600000);

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Date startDate = null;
        Date finishDate = null;
        long diff = 0;
        int width = 0;

        int height = -1;
        for(Channel channel : channels) {
            startDate = channel.getPrograms().get(0).getStart();
            finishDate = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            diff = finishDate.getTime() - startDate.getTime();
            width = (int) (100 * dm.density * diff / 3600000);
            canvas.drawRect(height, height, width, height + barHeightPx, pntLines);

            width = 0;
            int totalWidth = 0;
            for(Program program : channel.getPrograms()) {
                startDate = program.getStart();
                finishDate = program.getFinish();
                diff = finishDate.getTime() - startDate.getTime();
                width = (int) (100 * dm.density * diff / 3600000);


                canvas.drawText(sdfTimeText.format(startDate) + "ddddddd", totalWidth + 20, height + 40, pntTimeText);

                canvas.drawLine(totalWidth + width, height, totalWidth + width, height + barHeightPx, pntLines);

                totalWidth += width;
            }


            height += barHeightPx;
        }

//        int width = 0;
//        int totalWidth = 0;
//
//
//        for(Program program : channels.getPrograms()) {
//            Date startDate = program.getStart();
//            Date finishDate = program.getFinish();
//            long diff = finishDate.getTime() - startDate.getTime();
//            width = (int) (100 * dm.density * diff / 3600000);
//
//
//            canvas.drawText(sdfTimeText.format(startDate), totalWidth + 20, 40, pntTimeText);
//
//            canvas.drawLine(totalWidth + width, 0, totalWidth + width, getHeight(), pntLines);
//
//            totalWidth += width;
//        }

    }
}
