package pl.visualnet.omomo.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationParcel implements Parcelable {

    public static final Creator<LocationParcel> CREATOR
            = new Creator<LocationParcel>() {

        public LocationParcel createFromParcel(Parcel in) {
            return new LocationParcel(in);
        }

        public LocationParcel[] newArray(int size) {
            return new LocationParcel[size];
        }
    };

    private String id;
    private String name;
    private String institutionName;
    private String street;
    private double latitude;
    private double longitude;

    public LocationParcel(String id, String name, String institutionName, String street, double latitude, double longitude) {

        this.id = id;
        this.name = name;
        this.institutionName = institutionName;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private LocationParcel(Parcel in) {

        String[] dataString = new String[4];
        double[] dataDouble = new double[2];

        in.readStringArray(dataString);
        in.readDoubleArray(dataDouble);

        this.id = dataString[0];
        this.name = dataString[1];
        this.institutionName = dataString[2];
        this.street = dataString[3];

        this.latitude = dataDouble[0];
        this.longitude = dataDouble[1];

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

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[]{this.id, this.name, this.institutionName, this.street});
        out.writeDoubleArray(new double[]{this.latitude, this.longitude});
    }
}
