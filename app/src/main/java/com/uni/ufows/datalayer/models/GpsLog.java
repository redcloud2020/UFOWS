package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by sammy on 2/28/2017.
 */
@Table(name="GPSLog")
public class GpsLog extends Model implements Parcelable {
    @Expose
    @Column(name = "Log_Id")
    String Log_Id;
    @Expose
    @Column(name = "Timestamp")
    String Timestamp;
    @Expose
    @Column(name = "Truck_Id")
    String Truck_Id;
    @Expose
    @Column(name = "Driver_user_Id")
    String Driver_user_Id;
    @Expose
    @Column(name = "Cfw_user_Id")
    String Cfw_user_Id;
    @Expose
    @Column(name = "Latitude")
    double Latitude;

    @Expose
    @Column(name = "Longitude")
    double Longitude;

    public GpsLog (){}
    public static List<GpsLog> selectAll(){
        return new Select().from(GpsLog.class).execute();
    }

    public GpsLog(String log_Id, String timestamp, String truck_Id, String driver_user_Id, String cfw_user_Id, double latitude, double longitude) {
        Log_Id = log_Id;
        Timestamp = timestamp;
        Truck_Id = truck_Id;
        Driver_user_Id = driver_user_Id;
        Cfw_user_Id = cfw_user_Id;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getLog_Id() {
        return Log_Id;
    }

    public void setLog_Id(String log_Id) {
        Log_Id = log_Id;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getTruck_Id() {
        return Truck_Id;
    }

    public void setTruck_Id(String truck_Id) {
        Truck_Id = truck_Id;
    }

    public String getDriver_user_Id() {
        return Driver_user_Id;
    }

    public void setDriver_user_Id(String driver_user_Id) {
        Driver_user_Id = driver_user_Id;
    }

    public String getCfw_user_Id() {
        return Cfw_user_Id;
    }

    public void setCfw_user_Id(String cfw_user_Id) {
        Cfw_user_Id = cfw_user_Id;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Log_Id);
        dest.writeString(this.Timestamp);
        dest.writeString(this.Truck_Id);
        dest.writeString(this.Driver_user_Id);
        dest.writeString(this.Cfw_user_Id);
        dest.writeDouble(this.Latitude);
        dest.writeDouble(this.Longitude);
    }

    protected GpsLog(Parcel in) {
        this.Log_Id = in.readString();
        this.Timestamp = in.readString();
        this.Truck_Id = in.readString();
        this.Driver_user_Id = in.readString();
        this.Cfw_user_Id = in.readString();
        this.Latitude = in.readDouble();
        this.Longitude = in.readDouble();
    }

    public static final Creator<GpsLog> CREATOR = new Creator<GpsLog>() {
        @Override
        public GpsLog createFromParcel(Parcel source) {
            return new GpsLog(source);
        }

        @Override
        public GpsLog[] newArray(int size) {
            return new GpsLog[size];
        }
    };

    @Override
    public String toString() {
        return "GpsLog{" +
                "Log_Id='" + Log_Id + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", Truck_Id=" + Truck_Id +
                ", Driver_user_Id=" + Driver_user_Id +
                ", Cfw_user_Id=" + Cfw_user_Id +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                '}';
    }
}