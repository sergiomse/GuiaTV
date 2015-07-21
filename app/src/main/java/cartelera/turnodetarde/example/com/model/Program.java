package cartelera.turnodetarde.example.com.model;

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
    private String name;
    private String detailsUrl;

    public Program(Parcel source) {
        id = source.readInt();
        start = new Date(source.readLong());
        finish = new Date(source.readLong());
        name = source.readString();
        detailsUrl = source.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
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
        dest.writeString(name);
        dest.writeString(detailsUrl);
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
