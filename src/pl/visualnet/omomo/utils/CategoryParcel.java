package pl.visualnet.omomo.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryParcel implements Parcelable {

    public static final Creator<CategoryParcel> CREATOR
            = new Creator<CategoryParcel>() {

        public CategoryParcel createFromParcel(Parcel in) {
            return new CategoryParcel(in);
        }

        public CategoryParcel[] newArray(int size) {
            return new CategoryParcel[size];
        }
    };
    private String id;
    private String name;

    public CategoryParcel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private CategoryParcel(Parcel in) {

        String[] data = new String[2];
        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];

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

    @Override
    public String toString() {
        return "" + this.id + " " + this.name;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[]{this.id, this.name});

    }
}
