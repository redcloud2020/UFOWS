package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sammy on 3/16/2017.
 */
@Table(name="Priviledge")
public class Priviledge extends Model implements Parcelable {
    @Column(name="priviledge_id", unique = true)
    String priviledgeId;
    @Column(name="description")
    String description;

    public Priviledge(String priviledgeId, String description) {
        this.priviledgeId = priviledgeId;
        this.description = description;
    }

    public String getPriviledgeId() {
        return priviledgeId;
    }

    public void setPriviledgeId(String priviledgeId) {
        this.priviledgeId = priviledgeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.priviledgeId);
        dest.writeString(this.description);
    }

    protected Priviledge(Parcel in) {
        this.priviledgeId = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Priviledge> CREATOR = new Parcelable.Creator<Priviledge>() {
        @Override
        public Priviledge createFromParcel(Parcel source) {
            return new Priviledge(source);
        }

        @Override
        public Priviledge[] newArray(int size) {
            return new Priviledge[size];
        }
    };

    @Override
    public String toString() {
        return "Priviledge{" +
                "priviledgeId='" + priviledgeId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
