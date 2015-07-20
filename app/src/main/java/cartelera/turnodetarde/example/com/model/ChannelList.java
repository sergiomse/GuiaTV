package cartelera.turnodetarde.example.com.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sergiomse@gmail.com on 21/07/2015.
 */
public class ChannelList extends ArrayList<Channel> {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");

    public Map<Channel, ProgramComponentList> getProgramComponents() {

        Map<Channel, ProgramComponentList> map = new HashMap<>();

        int maxWidthDp = maxWidthDp();

        for(Channel channel : this) {
            ProgramComponentList programComponentList = new ProgramComponentList();

            int finish = 0;
            int start = 0;

            Date startDate = channel.getPrograms().get(0).getStart();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.set(Calendar.MINUTE, 0);
            start = (int) (cal.getTime().getTime() / 3600000);

            ProgramComponent programComponent = null;

            for(Program program : channel.getPrograms()) {

                finish = (int) (program.getFinish().getTime() / 3600000);

                programComponent = new ProgramComponent();
                programComponent.setDpWidth(finish - start);
                programComponent.setName(program.getName());
                programComponent.setTime(sdf.format(startDate));

                programComponentList.add(programComponent);
                start = finish;
            }

            if(finish < maxWidthDp) {
                EmptyProgramComponent emptyProgramComponent = new EmptyProgramComponent();
                emptyProgramComponent.setDpWidth(maxWidthDp - finish);
                programComponentList.add(emptyProgramComponent);
            }

            map.put(channel, programComponentList);
        }
        return map;
    }

    private int maxWidthDp() {
        int maxWidthDp = 0;
        int width = 0;
        long diff = 0;
        Date startDate = null;
        Date finishDate = null;
        for(Channel channel : this) {
            startDate = channel.getPrograms().get(0).getStart();
            finishDate = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            diff = finishDate.getTime() - startDate.getTime();
            width = (int) (100 * diff / 3600000);
            if(width > maxWidthDp) {
                maxWidthDp = width;
            }
        }

        return maxWidthDp;
    }
}
