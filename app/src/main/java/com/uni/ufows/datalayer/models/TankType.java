package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sammy on 3/16/2017.
 */
@Table(name="TankType")
public class TankType extends Model implements Parcelable {
    @Column(name="tank_type_id", unique = true)
    String tankTypeId;
    @Column(name="tank_capacity")
    String tankCapacity;
    @Column(name="compartments_number")
    String compartmentsNumber;

    public TankType(String tankTypeId, String tankCapacity, String compartmentsNumber) {
        this.tankTypeId = tankTypeId;
        this.tankCapacity = tankCapacity;
        this.compartmentsNumber = compartmentsNumber;
    }

    public String getTankTypeId() {
        return tankTypeId;
    }

    public void setTankTypeId(String tankTypeId) {
        this.tankTypeId = tankTypeId;
    }

    public String getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(String tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public String getCompartmentsNumber() {
        return compartmentsNumber;
    }

    public void setCompartmentsNumber(String compartmentsNumber) {
        this.compartmentsNumber = compartmentsNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tankTypeId);
        dest.writeString(this.tankCapacity);
        dest.writeString(this.compartmentsNumber);
    }

    protected TankType(Parcel in) {
        this.tankTypeId = in.readString();
        this.tankCapacity = in.readString();
        this.compartmentsNumber = in.readString();
    }

    public static final Parcelable.Creator<TankType> CREATOR = new Parcelable.Creator<TankType>() {
        @Override
        public TankType createFromParcel(Parcel source) {
            return new TankType(source);
        }

        @Override
        public TankType[] newArray(int size) {
            return new TankType[size];
        }
    };

    @Override
    public String toString() {
        return "TankType{" +
                "tankTypeId='" + tankTypeId + '\'' +
                ", tankCapacity='" + tankCapacity + '\'' +
                ", compartmentsNumber='" + compartmentsNumber + '\'' +
                '}';
    }
}
