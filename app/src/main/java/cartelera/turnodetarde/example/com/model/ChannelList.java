package cartelera.turnodetarde.example.com.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import cartelera.turnodetarde.example.com.Constansts;

/**
 * Created by sergiomse@gmail.com on 21/07/2015.
 */
public class ChannelList extends ArrayList<Channel> {

    private int maxWidthDp;
    private Date maxDate = new Date();

    public int getMaxWidthDp() {
        return maxWidthDp;
    }

    public void setMaxWidthDp(int maxWidthDp) {
        this.maxWidthDp = maxWidthDp;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }






    private final static SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");

//    private final static int DP_WIDTH_PER_HOUR = 100;

    public Map<Channel, ProgramComponentList> getProgramComponents() {

        Map<Channel, ProgramComponentList> map = new LinkedHashMap<>();

        maxWidthDp = maxWidthDp();

        for(Channel channel : this) {
            ProgramComponentList programComponentList = new ProgramComponentList();

            int offset = 0;
            int totalWidth = 0;

            int indexFirstProgram = getIndexFirstProgram(channel);

            Date startDate = channel.getPrograms().get(indexFirstProgram).getStart();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.set(Calendar.MINUTE, 0);
            offset = (int) (cal.getTime().getTime() / 3600000.0 * Constansts.DP_WIDTH_PER_HOUR);


            for(int i=indexFirstProgram; i<channel.getPrograms().size(); i++) {
                Program program = channel.getPrograms().get(i);

                int finish = (int) (program.getFinish().getTime() / 3600000.0 * Constansts.DP_WIDTH_PER_HOUR);

                ProgramComponent programComponent = new ProgramComponent();
                programComponent.setId(program.getId());
                programComponent.setDpWidth(finish - offset);
                programComponent.setName(program.getName());
                programComponent.setTime(sdf.format(program.getStart()));

                totalWidth += finish - offset;
                programComponentList.add(programComponent);
                offset = finish;
            }

            if(totalWidth < maxWidthDp) {
                EmptyProgramComponent emptyProgramComponent = new EmptyProgramComponent();
                emptyProgramComponent.setDpWidth(maxWidthDp - totalWidth);
                programComponentList.add(emptyProgramComponent);
            }

            map.put(channel, programComponentList);

            Date lastDateChannel = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            if(lastDateChannel.compareTo(maxDate) > 0) {
                maxDate = lastDateChannel;
            }
        }

        return map;
    }

    private int getIndexFirstProgram(Channel channel) {
        Date now = new Date();
        for(int i=0; i<channel.getPrograms().size(); i++) {
            Date finish = channel.getPrograms().get(i).getFinish();
            if(finish.compareTo(now) > 0) {
                return i;
            }
        }
        //TODO ver si no hay coincidencias debemos devolver 0 u otra cosa
        return 0;
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
            width = (int) (Constansts.DP_WIDTH_PER_HOUR * diff / 3600000.0);
            if(width > maxWidthDp) {
                maxWidthDp = width;
            }
        }

        return maxWidthDp;
    }


    public Program getProgramById(int id) {
        for(Channel channel : this) {
            for(Program program : channel.getPrograms()) {
                if(program.getId() == id) {
                    return program;
                }
            }
        }

        return null;
    }
}
