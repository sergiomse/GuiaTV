package cartelera.turnodetarde.example.com.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by turno de tarde on 29/07/2015.
 */
public class ProgramAlarm implements Parcelable {

    private int id;
    private Date alarm;
    private Date start;
    private String name;
    private String channel;



    public ProgramAlarm() {}

    public ProgramAlarm(Parcel source) {
        id = source.readInt();
        alarm = new Date(source.readLong());
        start = new Date(source.readLong());
        name = source.readString();
        channel = source.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getAlarm() {
        return alarm;
    }

    public void setAlarm(Date alarm) {
        this.alarm = alarm;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(alarm.getTime());
        dest.writeLong(start.getTime());
        dest.writeString(name);
        dest.writeString(channel);
    }

    public final static Parcelable.Creator<ProgramAlarm> CREATOR = new Creator<ProgramAlarm>() {
        @Override
        public ProgramAlarm createFromParcel(Parcel source) {
            return new ProgramAlarm(source);
        }

        @Override
        public ProgramAlarm[] newArray(int size) {
            return new ProgramAlarm[size];
        }
    };
}
