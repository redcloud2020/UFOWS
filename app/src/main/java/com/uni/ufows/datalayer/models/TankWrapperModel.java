package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sammy on 3/17/2017.
 */

public class TankWrapperModel implements Parcelable {
    private ArrayList<Tank> DeletedData;
    private ArrayList<Tank> NewUpdatedData;

    public TankWrapperModel(ArrayList<Tank> deletedData, ArrayList<Tank> newUpdatedData) {
        DeletedData = deletedData;
        NewUpdatedData = newUpdatedData;
    }

    public ArrayList<Tank> getDeletedData() {
        return DeletedData;
    }

    public void setDeletedData(ArrayList<Tank> deletedData) {
        DeletedData = deletedData;
    }

    public ArrayList<Tank> getNewUpdatedData() {
        return NewUpdatedData;
    }

    public void setNewUpdatedData(ArrayList<Tank> newUpdatedData) {
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

    protected TankWrapperModel(Parcel in) {
        this.DeletedData = in.createTypedArrayList(Tank.CREATOR);
        this.NewUpdatedData = in.createTypedArrayList(Tank.CREATOR);
    }

    public static final Parcelable.Creator<TankWrapperModel> CREATOR = new Parcelable.Creator<TankWrapperModel>() {
        @Override
        public TankWrapperModel createFromParcel(Parcel source) {
            return new TankWrapperModel(source);
        }

        @Override
        public TankWrapperModel[] newArray(int size) {
            return new TankWrapperModel[size];
        }
    };

    @Override
    public String toString() {
        return "TankWrapperModel{" +
                "DeletedData=" + DeletedData +
                ", NewUpdatedData=" + NewUpdatedData +
                '}';
    }
}
