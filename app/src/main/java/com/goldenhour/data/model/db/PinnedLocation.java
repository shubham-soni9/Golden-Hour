package com.goldenhour.data.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Table of saving location into the table pinned_location
 */
@Entity(tableName = "pinned_location", primaryKeys = {"lat", "lng", "current_date"})
public class PinnedLocation implements Parcelable {

    @Expose
    @SerializedName("sunrise")
    @ColumnInfo(name = "sunrise")
    public String sunrise;

    @Expose
    @SerializedName("sunset")
    @ColumnInfo(name = "sunset")
    public String sunset;

    @Expose
    @SerializedName("moonrise")
    @ColumnInfo(name = "moonrise")
    public String moonrise;

    @Expose
    @SerializedName("moonset")
    @ColumnInfo(name = "moonset")
    public String moonset;

    @Expose
    @SerializedName("address")
    @ColumnInfo(name = "address")
    public String address;

    @Expose
    @SerializedName("lat")
    @ColumnInfo(name = "lat")
    public double lat;

    @Expose
    @SerializedName("lng")
    @ColumnInfo(name = "lng")
    public double lng;

    @Expose
    @NonNull
    @SerializedName("current_date")
    @ColumnInfo(name = "current_date")
    public String currentDate = "";

    public PinnedLocation() {
    }

    protected PinnedLocation(Parcel in) {
        sunrise = in.readString();
        sunset = in.readString();
        moonrise = in.readString();
        moonset = in.readString();
        address = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        currentDate = in.readString();
    }

    public static final Creator<PinnedLocation> CREATOR = new Creator<PinnedLocation>() {
        @Override
        public PinnedLocation createFromParcel(Parcel in) {
            return new PinnedLocation(in);
        }

        @Override
        public PinnedLocation[] newArray(int size) {
            return new PinnedLocation[size];
        }
    };

    public void setLocation(LatLng location) {
        this.lat = location.latitude;
        this.lng = location.longitude;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sunrise);
        dest.writeString(sunset);
        dest.writeString(moonrise);
        dest.writeString(moonset);
        dest.writeString(address);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(currentDate);
    }
}
