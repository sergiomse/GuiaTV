package com.sergiomse.guiatv.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sergiomse@gmail.com on 25/07/2015.
 */
public class Link implements Parcelable {

    private String text;
    private String url;

    public Link() {}

    public Link(Parcel source) {
        text = source.readString();
        url = source.readString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Link[] arrayFromString(String str) {
        if(str.isEmpty()) {
            return null;
        }

        String[] linkStr = str.split("$\\|$");
        Link[] links = new Link[linkStr.length];
        for(int i=0; i<linkStr.length; i++) {
            links[i] = new Link();
            String fields[] = linkStr[i].split("\\|");
            links[i].setText(fields[0]);
            links[i].setUrl(fields[1]);
        }
        return links;
    }

    public static String toArrayString(Link[] links) {
        if(links == null) {
            return "";
        }

        if(links.length == 0) {
            return "";
        }

        String result = "";
        for(int i=0; i<links.length; i++) {
            if(i != 0) {
                result += "$|$";
            }

            result += links[i].getText();
            result += "|";
            result += links[i].getUrl();

        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(url);
    }


    public final static Parcelable.Creator<Link> CREATOR = new Creator<Link>() {
        @Override
        public Link createFromParcel(Parcel source) {
            return new Link(source);
        }

        @Override
        public Link[] newArray(int size) {
            return new Link[size];
        }
    };
}
