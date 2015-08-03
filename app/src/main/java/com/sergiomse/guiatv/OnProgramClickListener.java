package com.sergiomse.guiatv;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import com.sergiomse.guiatv.activities.DetailActivity;
import com.sergiomse.guiatv.activities.MainActivity;
import com.sergiomse.guiatv.model.ChannelList;
import com.sergiomse.guiatv.model.Program;

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

            if(Build.VERSION.SDK_INT >= 21) {
                activity.startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext()).toBundle());
            } else {
                activity.startActivity(intent);
            }
        }
    }

}
