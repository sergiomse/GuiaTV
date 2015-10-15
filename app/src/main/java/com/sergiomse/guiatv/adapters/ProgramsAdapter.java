package com.sergiomse.guiatv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sergiomse.guiatv.OnProgramClickListener;
import com.sergiomse.guiatv.R;
import com.sergiomse.guiatv.model.EmptyProgramComponent;
import com.sergiomse.guiatv.model.ProgramComponent;
import com.sergiomse.guiatv.model.ProgramComponentBase;
import com.sergiomse.guiatv.model.ProgramComponentList;

/**
 * Created by turno de tarde on 17/07/2015.
 */
public class ProgramsAdapter extends RecyclerView.Adapter {

    public static class ChannelHolder extends RecyclerView.ViewHolder {

        public LinearLayout layoutProgramContainer;
        public TextView tvTime;
        public TextView tvName;

        public ChannelHolder(View itemView) {
            super(itemView);
            layoutProgramContainer = (LinearLayout) itemView.findViewById(R.id.layoutProgramContainer);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvName = (TextView) itemView.findViewById(R.id.tvProgramName);
        }
    }


    private ProgramComponentList programComponentList;
    private DisplayMetrics dm;
    private OnProgramClickListener onProgramClickListener;

    public ProgramsAdapter(Context context, ProgramComponentList programComponentList) {
        this.programComponentList = programComponentList;
        dm = context.getResources().getDisplayMetrics();
        onProgramClickListener = new OnProgramClickListener();
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

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (programComponentBase.getDpWidth() * dm.density),
                (int) (72 * dm.density));
        v.setLayoutParams(params);
        return new ChannelHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ChannelHolder holder = (ChannelHolder) viewHolder;

        ProgramComponentBase programComponentBase = programComponentList.get(position);
        if(programComponentBase instanceof ProgramComponent) {
            holder.layoutProgramContainer.setTag(((ProgramComponent) programComponentList.get(position)).getId());
            holder.layoutProgramContainer.setOnClickListener(onProgramClickListener);
            holder.tvTime.setText(((ProgramComponent) programComponentList.get(position)).getTime());
            holder.tvName.setText(((ProgramComponent) programComponentList.get(position)).getName());
        }
    }

    @Override
    public int getItemCount() {
        return programComponentList.size();
    }
}
