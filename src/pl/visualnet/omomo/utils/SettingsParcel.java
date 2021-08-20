package pl.visualnet.omomo.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class SettingsParcel implements Parcelable {

    public static final Creator<SettingsParcel> CREATOR
            = new Creator<SettingsParcel>() {

        public SettingsParcel createFromParcel(Parcel in) {
            return new SettingsParcel(in);
        }

        public SettingsParcel[] newArray(int size) {
            return new SettingsParcel[size];
        }
    };
    private int distance;
    private boolean isDrawCircle;
    private boolean isActualPosition;

    public SettingsParcel(int distance, boolean isDrawCircle, boolean isActualPosition) {
        this.distance = distance;
        this.isDrawCircle = isDrawCircle;
        this.isActualPosition = isActualPosition;
    }

    private SettingsParcel(Parcel in) {

        distance = in.readInt();

        boolean[] data = new boolean[2];
        in.readBooleanArray(data);

        isDrawCircle = data[0];
        isActualPosition = data[1];

    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isDrawCircle() {
        return isDrawCircle;
    }

    public void setDrawCircle(boolean isDrawCircle) {
        this.isDrawCircle = isDrawCircle;
    }

    public boolean isActualPosition() {
        return isActualPosition;
    }

    public void setActualPosition(boolean isActualPosition) {
        this.isActualPosition = isActualPosition;
    }

    @Override
    public String toString() {
        return "SettingsParcel{" +
                ", distance=" + distance +
                ", isDrawCircle=" + isDrawCircle +
                '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(distance);
        out.writeBooleanArray(new boolean[]{isDrawCircle, isActualPosition});

    }
}
