package cartelera.turnodetarde.example.com.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by turno de tarde on 17/07/2015.
 */
public class ProgramsRecyclerView extends RecyclerView {

    private int maxWidth;
    private int widthExceede;

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }




    private DisplayMetrics dm;


    public ProgramsRecyclerView(Context context) {
        super(context);
        init();
    }

    public ProgramsRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgramsRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        dm = getResources().getDisplayMetrics();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int height = (int) (72 * dm.density);
        heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

//        widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
        int width = MeasureSpec.getSize(widthSpec);

        setMeasuredDimension(widthSpec, heightSpec);
    }
}
