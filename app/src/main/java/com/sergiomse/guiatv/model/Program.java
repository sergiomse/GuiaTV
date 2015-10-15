package com.sergiomse.guiatv.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by turno de tarde on 07/07/2015.
 */
public class Program implements Parcelable, Comparable<Program> {

    private int id;
    private DateTime start;
    private DateTime finish;
    private String name;
    private String details;
    private Link[] links;
    private String channelName;


    public Program() {}

    public Program(Parcel source) {
        id = source.readInt();
        start = DateTime.parse(source.readString());
        finish = DateTime.parse(source.readString());
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

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getFinish() {
        return finish;
    }

    public void setFinish(DateTime finish) {
        this.finish = finish;
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
        dest.writeString(start.toString());
        dest.writeString(finish.toString());
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

    @Override
    public int compareTo(Program another) {
        return start.compareTo(another.start);
    }
}
