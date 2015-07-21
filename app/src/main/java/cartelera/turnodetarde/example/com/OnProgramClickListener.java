package cartelera.turnodetarde.example.com;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import cartelera.turnodetarde.example.com.activities.DetailActivity;
import cartelera.turnodetarde.example.com.activities.MainActivity;
import cartelera.turnodetarde.example.com.model.ChannelList;
import cartelera.turnodetarde.example.com.model.Program;

/**
 * Created by turno de tarde on 21/07/2015.
 */
public class OnProgramClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if(v instanceof LinearLayout && v.getTag() instanceof Integer) {
            int id = (Integer) v.getTag();
            MainActivity activity = (MainActivity) v.getContext();
            ChannelList channels = activity.getChannels();
            Program program = channels.getProgramById(id);

            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra("program", program);
            activity.startActivity(intent);
        }
    }

}
