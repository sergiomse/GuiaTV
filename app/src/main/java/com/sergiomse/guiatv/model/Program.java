package com.sergiomse.guiatv.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by turno de tarde on 07/07/2015.
 */
public class Program implements Parcelable{

    private int id;
    private Date start;
    private Date finish;
    private Date alarm;
    private String name;
    private String details;
    private Link[] links;
    private transient String channelName;


    public Program() {}

    public Program(Parcel source) {
        id = source.readInt();
        start = new Date(source.readLong());
        finish = new Date(source.readLong());
        byte isNull = source.readByte();
        if(isNull == 1) {
            alarm = new Date(source.readLong());
        }
        name = source.readString();
        details = source.readString();
        links = source.createTypedArray(Link.CREATOR);
        channelName = source.readString();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public Date getAlarm() {
        return alarm;
    }

    public void setAlarm(Date alarm) {
        this.alarm = alarm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Link[] getLinks() {
        return links;
    }

    public void setLinks(Link[] links) {
        this.links = links;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(start.getTime());
        dest.writeLong(finish.getTime());
        if(alarm != null) {
            dest.writeByte((byte)1);
            dest.writeLong(alarm.getTime());
        } else {
            dest.writeByte((byte)0);
        }
        dest.writeString(name);
        dest.writeString(details);
        dest.writeTypedArray(links, flags);
        dest.writeString(channelName);
    }


    public static final Parcelable.Creator<Program> CREATOR = new Creator<Program>() {
        @Override
        public Program createFromParcel(Parcel source) {
            return new Program(source);
        }

        @Override
        public Program[] newArray(int size) {
            return new Program[size];
        }
    };
}
