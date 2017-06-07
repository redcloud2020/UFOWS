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

@Table(name="Event")
public class Event extends Model implements Parcelable {
    @Expose
    @Column(name="Event_Id", unique = true)
    String Event_Id;
    @Expose
    @Column(name="Timestamp")
    String Timestamp;
    @Expose
    @Column(name="Cfw_user_Id")
    String Cfw_user_Id;
    @Expose
    @Column(name="Task_Id")
    String Task_Id;
    @Expose
    @Column(name="Tank_Id", unique = true)
    String Tank_Id;
    @Expose
    @Column(name="Measurement1_black")
    double Measurement1_black;
    @Expose
    @Column(name="Measurement2_black")
    double Measurement2_black;

    @Expose
    @Column(name="Measurement1_color")
    double Measurement1_color;

    @Expose
    @Column(name="Measurement2_color")
    double Measurement2_color;

    @Expose
    @Column(name="Comment_Id")
    String Comment_Id;

    @Expose
    @Column(name="Latitude")
    double Latitude;

    @Expose
    @Column(name="Longitude")
    double Longitude;

    @Expose
    @Column(name="Truck_Id")
    String Truck_Id;

    @Expose
    @Column(name="Driver_user_Id")
    String Driver_user_Id;

    @Expose
    @Column(name="Time_user")
    String Time_user;


    public Event(){}

    public static Event selectById(String id){
        return new Select().from(Event.class).where("Tank_Id = '"+id+"'").executeSingle();
    }
    public static Event getEventById(String id){
        return new Select().from(Event.class).where("Event_Id = '"+id+"'").executeSingle();
    }
    public static List<Event> getAll(){
        return new Select().from(Event.class).execute();
    }


    public Event(String event_Id, String timestamp, String cfw_user_Id, String task_Id, String tank_Id, double measurement1_black, double measurement2_black, double measurement1_color, double measurement2_color, String comment_Id, double latitude, double longitude, String truck_id, String driver_id) {
        Event_Id = event_Id;
        Timestamp = timestamp;
        Cfw_user_Id = cfw_user_Id;
        Task_Id = task_Id;
        Tank_Id = tank_Id;
        Measurement1_black = measurement1_black;
        Measurement2_black = measurement2_black;
        Measurement1_color = measurement1_color;
        Measurement2_color = measurement2_color;
        Comment_Id = comment_Id;
        Latitude = latitude;
        Longitude = longitude;

        Truck_Id = truck_id;
        Driver_user_Id = driver_id;
        Time_user = timestamp;
    }

    public String getEvent_Id() {
        return Event_Id;
    }

    public void setEvent_Id(String event_Id) {
        Event_Id = event_Id;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getCfw_user_Id() {
        return Cfw_user_Id;
    }

    public void setCfw_user_Id(String cfw_user_Id) {
        Cfw_user_Id = cfw_user_Id;
    }

    public String getTask_Id() {
        return Task_Id;
    }

    public void setTask_Id(String task_Id) {
        Task_Id = task_Id;
    }

    public String getTank_Id() {
        return Tank_Id;
    }

    public void setTank_Id(String tank_Id) {
        Tank_Id = tank_Id;
    }

    public double getMeasurement1_black() {
        return Measurement1_black;
    }

    public void setMeasurement1_black(double measurement1_black) {
        Measurement1_black = measurement1_black;
    }

    public double getMeasurement2_black() {
        return Measurement2_black;
    }

    public void setMeasurement2_black(double measurement2_black) {
        Measurement2_black = measurement2_black;
    }

    public double getMeasurement1_color() {
        return Measurement1_color;
    }

    public void setMeasurement1_color(double measurement1_color) {
        Measurement1_color = measurement1_color;
    }

    public double getMeasurement2_color() {
        return Measurement2_color;
    }

    public void setMeasurement2_color(double measurement2_color) {
        Measurement2_color = measurement2_color;
    }

    public String getComment_Id() {
        return Comment_Id;
    }

    public void setComment_Id(String comment_Id) {
        Comment_Id = comment_Id;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Event_Id);
        dest.writeString(this.Timestamp);
        dest.writeString(this.Cfw_user_Id);
        dest.writeString(this.Task_Id);
        dest.writeString(this.Tank_Id);
        dest.writeDouble(this.Measurement1_black);
        dest.writeDouble(this.Measurement2_black);
        dest.writeDouble(this.Measurement1_color);
        dest.writeDouble(this.Measurement2_color);
        dest.writeString(this.Comment_Id);
        dest.writeDouble(this.Latitude);
        dest.writeDouble(this.Longitude);
        dest.writeString(this.Truck_Id);
        dest.writeString(this.Driver_user_Id);
        dest.writeString(this.Time_user);
    }

    protected Event(Parcel in) {
        this.Event_Id = in.readString();
        this.Timestamp = in.readString();
        this.Cfw_user_Id = in.readString();
        this.Task_Id = in.readString();
        this.Tank_Id = in.readString();
        this.Measurement1_black = in.readDouble();
        this.Measurement2_black = in.readDouble();
        this.Measurement1_color = in.readDouble();
        this.Measurement2_color = in.readDouble();
        this.Comment_Id = in.readString();
        this.Latitude = in.readDouble();
        this.Longitude = in.readDouble();
        this.Truck_Id = in.readString();
        this.Driver_user_Id = in.readString();
        this.Time_user = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public String toString() {
        return "Event{" +
                "Event_Id='" + Event_Id + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", Cfw_user_Id='" + Cfw_user_Id + '\'' +
                ", Task_Id='" + Task_Id + '\'' +
                ", Tank_Id='" + Tank_Id + '\'' +
                ", Measurement1_black=" + Measurement1_black +
                ", Measurement2_black=" + Measurement2_black +
                ", Measurement1_color=" + Measurement1_color +
                ", Measurement2_color=" + Measurement2_color +
                ", Comment_Id='" + Comment_Id + '\'' +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", Truck_Id=" + Truck_Id +
                ", Driver_user_Id=" + Driver_user_Id +
                ", Time_user=" + Time_user +
                '}';
    }
}
