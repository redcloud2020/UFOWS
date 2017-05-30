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
 * Created by sammy on 3/16/2017.
 */
@Table(name="Truck")
public class Truck extends Model implements Parcelable {
    @Expose
    @Column(name="Truck_Id", unique = true)
    String Truck_Id;
    @Expose
    @Column(name="Truck_number")
    String Truck_number;
    @Expose
    @Column(name="Employer_Id")
    String Employer_Id;
    @Expose
    @Column(name ="Green_plate_number")
    String Green_plate_number;
    @Expose
    @Column(name="Yellow_plate_number")
    String Yellow_plate_number;
    @Expose
    @Column(name="Year_of_make")
    String Year_of_make;
    @Expose
    @Column(name="Truck_total_capacity")
    String Truck_total_capacity;
    @Expose
    @Column(name="Hose_length")
    String Hose_length;
    @Expose
    @Column(name="isDeleted")
    String isDeleted;
    @Expose
    @Column(name="created_at")
    String created_at;
    @Expose
    @Column(name="updated_at")
    String updated_at;

    public Truck(String truck_Id, String Truck_number, String employer_Id, String green_plate_number, String yellow_plate_number, String year_of_make, String truck_total_capacity, String hose_length, String isDeleted, String created_at, String updated_at) {
        Truck_Id = truck_Id;
        this.Truck_number = Truck_number;
        Employer_Id = employer_Id;
        Green_plate_number = green_plate_number;
        Yellow_plate_number = yellow_plate_number;
        Year_of_make = year_of_make;
        Truck_total_capacity = truck_total_capacity;
        Hose_length = hose_length;
        this.isDeleted = isDeleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    public Truck(){}

    public static List<Truck> getAll(){
        return new Select().from(Truck.class).execute();
    }

    public String getTruck_Id() {
        return Truck_Id;
    }

    public void setTruck_Id(String truck_Id) {
        Truck_Id = truck_Id;
    }

    public String getTruck_number() {
        return Truck_number;
    }

    public void setTruck_number(String Truck_number) {
        Truck_number = Truck_number;
    }

    public String getEmployer_Id() {
        return Employer_Id;
    }

    public void setEmployer_Id(String employer_Id) {
        Employer_Id = employer_Id;
    }

    public String getGreen_plate_number() {
        return Green_plate_number;
    }

    public void setGreen_plate_number(String green_plate_number) {
        Green_plate_number = green_plate_number;
    }

    public String getYellow_plate_number() {
        return Yellow_plate_number;
    }

    public void setYellow_plate_number(String yellow_plate_number) {
        Yellow_plate_number = yellow_plate_number;
    }

    public String getYear_of_make() {
        return Year_of_make;
    }

    public void setYear_of_make(String year_of_make) {
        Year_of_make = year_of_make;
    }

    public String getTruck_total_capacity() {
        return Truck_total_capacity;
    }

    public void setTruck_total_capacity(String truck_total_capacity) {
        Truck_total_capacity = truck_total_capacity;
    }

    public String getHose_length() {
        return Hose_length;
    }

    public void setHose_length(String hose_length) {
        Hose_length = hose_length;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Truck_Id);
        dest.writeString(this.Truck_number);
        dest.writeString(this.Employer_Id);
        dest.writeString(this.Green_plate_number);
        dest.writeString(this.Yellow_plate_number);
        dest.writeString(this.Year_of_make);
        dest.writeString(this.Truck_total_capacity);
        dest.writeString(this.Hose_length);
        dest.writeString(this.isDeleted);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    protected Truck(Parcel in) {
        this.Truck_Id = in.readString();
        this.Truck_number = in.readString();
        this.Employer_Id = in.readString();
        this.Green_plate_number = in.readString();
        this.Yellow_plate_number = in.readString();
        this.Year_of_make = in.readString();
        this.Truck_total_capacity = in.readString();
        this.Hose_length = in.readString();
        this.isDeleted = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<Truck> CREATOR = new Creator<Truck>() {
        @Override
        public Truck createFromParcel(Parcel source) {
            return new Truck(source);
        }

        @Override
        public Truck[] newArray(int size) {
            return new Truck[size];
        }
    };

    @Override
    public String toString() {
        return "Truck{" +
                "Truck_Id='" + Truck_Id + '\'' +
                ", Truck_number='" + Truck_number + '\'' +
                ", Employer_Id='" + Employer_Id + '\'' +
                ", Green_plate_number='" + Green_plate_number + '\'' +
                ", Yellow_plate_number='" + Yellow_plate_number + '\'' +
                ", Year_of_make='" + Year_of_make + '\'' +
                ", Truck_total_capacity='" + Truck_total_capacity + '\'' +
                ", Hose_length='" + Hose_length + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
    public static List<Truck> select (){
        return new Select().from(Truck.class).execute();
    }
    public static Truck getOldDate(){
        return  new Select().from(Truck.class).orderBy("id DESC").executeSingle();
    }
    public static Truck getTruckById(String ids){
        return  new Select().from(Truck.class).where("Truck_Id = ?", ids).executeSingle();
    }
    public static Truck getTruckByTruckNumber(String number){
        return  new Select().from(Truck.class).where("Truck_number ='"+number+"'").executeSingle();
    }
}
