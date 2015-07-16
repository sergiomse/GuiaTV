package cartelera.turnodetarde.example.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Date;

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

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
                        MeasureSpec.makeMeasureSpec((int) (72 * dm.density), MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, pntLines);
    }
}
