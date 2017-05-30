package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sammy on 2/28/2017.
 */

@Table(name = "Tank")
public class Tank extends Model implements Parcelable {
    @SerializedName("Tank_Id")
    @Column(name = "Tank_Id", unique = true)
    String tankId;
    @SerializedName("Tank_latitude")
    @Column(name = "Tank_latitude")
    String tankLatitude;
    @SerializedName("Tank_longitude")
    @Column(name = "Tank_longitude")
    String tankLongitude;
    @SerializedName("District")
    @Column(name = "District")
    String district;
    @SerializedName("Block")
    @Column(name = "Block")
    String block;
    @SerializedName("Tank_number")
    @Column(name = "Tank_number")
    String tankNumber;
    @SerializedName("tank_type")
    @Column(name = "tank_type")
    String tankType;
    @SerializedName("created_at")
    @Column(name = "created_at")
    String createdAt;
    @SerializedName("Timestamp")
    @Column(name = "Timestamp")
    String Timestamp;
    @SerializedName("Comment_Id")
    @Column(name = "Comment_Id")
    String commentId;

    public static List<Tank> getAll(){
        return new Select().from(Tank.class).execute();
    }
    public static List<Tank> select (){
        return new Select().from(Tank.class).execute();
    }
    public static Tank getOldDate(){
        return  new Select().from(Tank.class).orderBy("id DESC").executeSingle();
    }
    public static Tank getTankById(String id){
        return  new Select().from(Tank.class).where("Tank_Id = '"+id+"'").executeSingle();
    }
    public Tank() {
    }

    public Tank(String tankId, String tankLatitude, String tankLongitude, String district, String block, String tankNumber, String tankType, String createdAt, String timestamp, String commentId) {
        this.tankId = tankId;
        this.tankLatitude = tankLatitude;
        this.tankLongitude = tankLongitude;
        this.district = district;
        this.block = block;
        this.tankNumber = tankNumber;
        this.tankType = tankType;
        this.createdAt = createdAt;
        Timestamp = timestamp;
        this.commentId = commentId;
    }

    public String getTankId() {
        return tankId;
    }

    public void setTankId(String tankId) {
        this.tankId = tankId;
    }

    public String getTankLatitude() {
        return tankLatitude;
    }

    public void setTankLatitude(String tankLatitude) {
        this.tankLatitude = tankLatitude;
    }

    public String getTankLongitude() {
        return tankLongitude;
    }

    public void setTankLongitude(String tankLongitude) {
        this.tankLongitude = tankLongitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getTankNumber() {
        return tankNumber;
    }

    public void setTankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
    }

    public String getTankType() {
        return tankType;
    }

    public void setTankType(String tankType) {
        this.tankType = tankType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tankId);
        dest.writeString(this.tankLatitude);
        dest.writeString(this.tankLongitude);
        dest.writeString(this.district);
        dest.writeString(this.block);
        dest.writeString(this.tankNumber);
        dest.writeString(this.tankType);
        dest.writeString(this.createdAt);
        dest.writeString(this.Timestamp);
        dest.writeString(this.commentId);
    }

    protected Tank(Parcel in) {
        this.tankId = in.readString();
        this.tankLatitude = in.readString();
        this.tankLongitude = in.readString();
        this.district = in.readString();
        this.block = in.readString();
        this.tankNumber = in.readString();
        this.tankType = in.readString();
        this.createdAt = in.readString();
        this.Timestamp = in.readString();
        this.commentId = in.readString();
    }

    public static final Creator<Tank> CREATOR = new Creator<Tank>() {
        @Override
        public Tank createFromParcel(Parcel source) {
            return new Tank(source);
        }

        @Override
        public Tank[] newArray(int size) {
            return new Tank[size];
        }
    };

    @Override
    public String toString() {
        return "Tank{" +
                "tankId='" + tankId + '\'' +
                ", tankLatitude='" + tankLatitude + '\'' +
                ", tankLongitude='" + tankLongitude + '\'' +
                ", district='" + district + '\'' +
                ", block='" + block + '\'' +
                ", tankNumber='" + tankNumber + '\'' +
                ", tankType='" + tankType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", commentId='" + commentId + '\'' +
                '}';
    }
}
