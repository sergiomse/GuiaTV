package cartelera.turnodetarde.example.com.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sergiomse@gmail.com on 25/07/2015.
 */
public class Link implements Parcelable {

    private String text;
    private String url;

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
