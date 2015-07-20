package cartelera.turnodetarde.example.com.views;

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

import cartelera.turnodetarde.example.com.model.Channel;
import cartelera.turnodetarde.example.com.model.Program;

/**
 * Created by sergiomse@gmail.com on 16/07/2015.
 */
public class ChannelBarView extends View {

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
        invalidate();
    }




    private DisplayMetrics dm;
    private Paint pntLines = new Paint();
    private Paint pntTimeText = new Paint();
    private SimpleDateFormat sdfTimeText = new SimpleDateFormat("HH.mm");

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
        pntTimeText.setColor(Color.rgb(82, 157, 166));
        pntTimeText.setTextSize(18 * dm.density);
        Typeface typefaceTime = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        pntTimeText.setTypeface(typefaceTime);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        if(!channel.getPrograms().isEmpty()) {
            Date startDate = channel.getPrograms().get(0).getStart();
            Date finishDate = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            long diff = finishDate.getTime() - startDate.getTime();
            width = (int) (100 * dm.density * diff / 3600000);
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (72 * dm.density), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = 0;
        int totalWidth = 0;
        for(Program program : channel.getPrograms()) {
            Date startDate = program.getStart();
            Date finishDate = program.getFinish();
            long diff = finishDate.getTime() - startDate.getTime();
            width = (int) (100 * dm.density * diff / 3600000);


            canvas.drawText(sdfTimeText.format(startDate), totalWidth + 20, 40, pntTimeText);

            canvas.drawLine(totalWidth + width, 0, totalWidth + width, getHeight(), pntLines);

            totalWidth += width;
        }


        canvas.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, pntLines);
    }
}
