package cartelera.turnodetarde.example.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cartelera.turnodetarde.example.com.OnProgramClickListener;
import cartelera.turnodetarde.example.com.R;
import cartelera.turnodetarde.example.com.model.EmptyProgramComponent;
import cartelera.turnodetarde.example.com.model.Link;
import cartelera.turnodetarde.example.com.model.ProgramComponent;
import cartelera.turnodetarde.example.com.model.ProgramComponentBase;

/**
 * Created by turno de tarde on 17/07/2015.
 */
public class LinksAdapter extends RecyclerView.Adapter {

    public static class LinksHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public LinksHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }


    private Link[] links;
    private DisplayMetrics dm;
    private OnProgramClickListener onProgramClickListener;

    public LinksAdapter(Context context, Link[] links) {
        this.links = links;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_link, viewGroup, false);

        return new LinksHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        LinksHolder holder = (LinksHolder) viewHolder;
        holder.textView.setText(links[position].getText());
    }

    @Override
    public int getItemCount() {
        return links.length;
    }
}
