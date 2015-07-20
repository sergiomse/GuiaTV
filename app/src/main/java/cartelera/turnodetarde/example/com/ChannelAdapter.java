package cartelera.turnodetarde.example.com;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by turno de tarde on 17/07/2015.
 */
public class ChannelAdapter extends RecyclerView.Adapter {

    public static class ChannelHolder extends RecyclerView.ViewHolder {

        public TextView tvTime;
        public TextView tvName;

        public ChannelHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvName = (TextView) itemView.findViewById(R.id.tvProgramName);
        }
    }


    private Channel channel;
    private DisplayMetrics dm;
    private int widthExceeded;

    public ChannelAdapter(Context context, Channel channel) {
        this.channel = channel;
        dm = context.getResources().getDisplayMetrics();
    }


    @Override
    public int getItemViewType(int position) {
        return position < channel.getPrograms().size() ? position : -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == -1) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_empty, viewGroup, false);

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, (int) (72 * dm.density));
            v.setLayoutParams(params);
            return new ChannelHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_program, viewGroup, false);

            Date startDate = channel.getPrograms().get(viewType).getStart();
            Date finishDate = channel.getPrograms().get(viewType).getFinish();
            long diff = finishDate.getTime() - startDate.getTime();
            int width = (int) (100 * dm.density * diff / 3600000);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, (int) (72 * dm.density));
            v.setLayoutParams(params);
            return new ChannelHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ChannelHolder holder = (ChannelHolder) viewHolder;
        Calendar cal = Calendar.getInstance();
        cal.setTime(channel.getPrograms().get(i).getStart());
        holder.tvTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ".00");
        holder.tvName.setText(channel.getPrograms().get(i).getName());
    }

    @Override
    public int getItemCount() {
        return channel.getPrograms().size() + 1;
    }
}
