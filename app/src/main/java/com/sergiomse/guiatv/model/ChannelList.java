package com.sergiomse.guiatv.model;

import com.sergiomse.guiatv.Constansts;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by sergiomse@gmail.com on 21/07/2015.
 */
public class ChannelList extends ArrayList<Channel> {

    private int maxWidthDp;
    private DateTime now;
    private DateTime maxDate = new DateTime();

    public int getMaxWidthDp() {
        return maxWidthDp;
    }

    public void setMaxWidthDp(int maxWidthDp) {
        this.maxWidthDp = maxWidthDp;
    }

    public DateTime getNow() {
        return now;
    }

    public void setNow(DateTime now) {
        this.now = now;
    }

    public DateTime getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(DateTime maxDate) {
        this.maxDate = maxDate;
    }


    public Map<Channel, ProgramComponentList> getProgramComponents() {
        Map<Channel, ProgramComponentList> map = new LinkedHashMap<Channel, ProgramComponentList>();
        maxWidthDp = maxWidthDp();

        for(Channel channel : this) {
            ProgramComponentList programComponentList = new ProgramComponentList();

            int offset = 0;
            int totalWidth = 0;

            int indexFirstProgram = getIndexFirstProgram(channel);

            DateTime roundedNow = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), now.getHourOfDay(), 0, 0, 0);
            offset = (int) (roundedNow.getMillis() / 3600000.0 * Constansts.DP_WIDTH_PER_HOUR);


            for(int i=indexFirstProgram; i<channel.getPrograms().size(); i++) {
                Program program = channel.getPrograms().get(i);

                int finish = (int) (program.getFinish().getMillis() / 3600000.0 * Constansts.DP_WIDTH_PER_HOUR);

                ProgramComponent programComponent = new ProgramComponent();
                programComponent.setId(program.getId());
                programComponent.setDpWidth(finish - offset);
                programComponent.setName(program.getName());
                programComponent.setTime(program.getStart().toString("HH.mm"));
//                programComponent.setTime(sdf.format(program.getStart()));

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

            DateTime lastDateChannel = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            if(lastDateChannel.compareTo(maxDate) > 0) {
                maxDate = lastDateChannel;
            }
        }

        return map;
    }

    private int getIndexFirstProgram(Channel channel) {
        for(int i=0; i<channel.getPrograms().size(); i++) {
            DateTime finish = channel.getPrograms().get(i).getFinish();
            DateTime roundedFinish = new DateTime(finish.getYear(), finish.getMonthOfYear(), finish.getDayOfMonth(), finish.getHourOfDay(), 59, 59, 999);
            if(roundedFinish.compareTo(now) > 0) {
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
        DateTime startDate = null;
        DateTime finishDate = null;
        for(Channel channel : this) {
            startDate = channel.getPrograms().get(0).getStart();
            finishDate = channel.getPrograms().get(channel.getPrograms().size() - 1).getFinish();
            diff = finishDate.getMillis() - startDate.getMillis();
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
