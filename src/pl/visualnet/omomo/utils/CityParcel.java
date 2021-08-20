package pl.visualnet.omomo.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class CityParcel implements Parcelable {

    public static final Parcelable.Creator<CityParcel> CREATOR
            = new Parcelable.Creator<CityParcel>() {

        public CityParcel createFromParcel(Parcel in) {
            return new CityParcel(in);
        }

        public CityParcel[] newArray(int size) {
            return new CityParcel[size];
        }
    };
    private String id;
    private String name;
    private String zip;

    public CityParcel(String id, String name, String zip) {
        this.id = id;
        this.name = name;
        this.zip = zip;
    }

    private CityParcel(Parcel in) {

        String[] data = new String[3];
        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.zip = data[2];

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "" + this.id + " " + this.name;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[]{this.id, this.name, this.zip});

    }
}
