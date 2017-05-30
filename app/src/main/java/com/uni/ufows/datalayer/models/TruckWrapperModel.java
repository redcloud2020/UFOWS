package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sammy on 4/6/2017.
 */

public class TruckWrapperModel implements Parcelable {
    private ArrayList<Truck> DeletedData;
    private ArrayList<Truck> NewUpdatedData;

    public TruckWrapperModel(ArrayList<Truck> deletedData, ArrayList<Truck> newUpdatedData) {
        DeletedData = deletedData;
        NewUpdatedData = newUpdatedData;
    }

    public ArrayList<Truck> getDeletedData() {
        return DeletedData;
    }

    public void setDeletedData(ArrayList<Truck> deletedData) {
        DeletedData = deletedData;
    }

    public ArrayList<Truck> getNewUpdatedData() {
        return NewUpdatedData;
    }

    public void setNewUpdatedData(ArrayList<Truck> newUpdatedData) {
        NewUpdatedData = newUpdatedData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.DeletedData);
        dest.writeTypedList(this.NewUpdatedData);
    }

    protected TruckWrapperModel(Parcel in) {
        this.DeletedData = in.createTypedArrayList(Truck.CREATOR);
        this.NewUpdatedData = in.createTypedArrayList(Truck.CREATOR);
    }

    public static final Creator<TruckWrapperModel> CREATOR = new Creator<TruckWrapperModel>() {
        @Override
        public TruckWrapperModel createFromParcel(Parcel source) {
            return new TruckWrapperModel(source);
        }

        @Override
        public TruckWrapperModel[] newArray(int size) {
            return new TruckWrapperModel[size];
        }
    };

    @Override
    public String toString() {
        return "TruckWrapperModel{" +
                "DeletedData=" + DeletedData +
                ", NewUpdatedData=" + NewUpdatedData +
                '}';
    }
}
