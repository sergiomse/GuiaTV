package cartelera.turnodetarde.example.com.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import cartelera.turnodetarde.example.com.R;
import cartelera.turnodetarde.example.com.model.Channel;
import cartelera.turnodetarde.example.com.model.EmptyProgramComponent;
import cartelera.turnodetarde.example.com.model.ProgramComponent;
import cartelera.turnodetarde.example.com.model.ProgramComponentBase;
import cartelera.turnodetarde.example.com.model.ProgramComponentList;

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


    private ProgramComponentList programComponentList;
    private DisplayMetrics dm;

    public ChannelAdapter(Context context, ProgramComponentList programComponentList) {
        this.programComponentList = programComponentList;
        dm = context.getResources().getDisplayMetrics();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ProgramComponentBase programComponentBase = programComponentList.get(viewType);

        View v = null;
        if(programComponentBase instanceof ProgramComponent) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_program, viewGroup, false);

        } else if(programComponentBase instanceof EmptyProgramComponent) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_empty, viewGroup, false);
        }

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (programComponentList.get(viewType).getDpWidth() * dm.density),
                (int) (72 * dm.density));
        v.setLayoutParams(params);
        return new ChannelHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ChannelHolder holder = (ChannelHolder) viewHolder;

        ProgramComponentBase programComponentBase = programComponentList.get(position);
        if(programComponentBase instanceof ProgramComponent) {
            holder.tvTime.setText(((ProgramComponent) programComponentList.get(position)).getTime());
            holder.tvName.setText(((ProgramComponent) programComponentList.get(position)).getName());
        }
    }

    @Override
    public int getItemCount() {
        return programComponentList.size();
    }
}
