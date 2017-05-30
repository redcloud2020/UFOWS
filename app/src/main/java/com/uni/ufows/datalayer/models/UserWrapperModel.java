package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sammy on 3/17/2017.
 */

public class UserWrapperModel implements Parcelable {
    private ArrayList<User> DeletedData;
    private ArrayList<User> NewUpdatedData;

    public UserWrapperModel(ArrayList<User> deletedData, ArrayList<User> newUpdatedData) {
        DeletedData = deletedData;
        NewUpdatedData = newUpdatedData;
    }

    public ArrayList<User> getDeletedData() {
        return DeletedData;
    }

    public void setDeletedData(ArrayList<User> deletedData) {
        DeletedData = deletedData;
    }

    public ArrayList<User> getNewUpdatedData() {
        return NewUpdatedData;
    }

    public void setNewUpdatedData(ArrayList<User> newUpdatedData) {
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

    protected UserWrapperModel(Parcel in) {
        this.DeletedData = in.createTypedArrayList(User.CREATOR);
        this.NewUpdatedData = in.createTypedArrayList(User.CREATOR);
    }

    public static final Parcelable.Creator<UserWrapperModel> CREATOR = new Parcelable.Creator<UserWrapperModel>() {
        @Override
        public UserWrapperModel createFromParcel(Parcel source) {
            return new UserWrapperModel(source);
        }

        @Override
        public UserWrapperModel[] newArray(int size) {
            return new UserWrapperModel[size];
        }
    };
    public UserWrapperModel (){}

    @Override
    public String toString() {
        return "UserWrapperModel{" +
                "DeletedData=" + DeletedData +
                ", NewUpdatedData=" + NewUpdatedData +
                '}';
    }
}
