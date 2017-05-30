package com.uni.ufows.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sammy on 2/28/2017.
 */
@Table(name="Role")
public class Role extends Model implements Parcelable {

    @Column(name="Role_Id")
    int roleId;
    @Column(name="job_title")
    String jobTitle;
    @Column(name="priviledge_id")
    String priviledgeId;

    public Role(int roleId, String jobTitle, String priviledgeId) {
        this.roleId = roleId;
        this.jobTitle = jobTitle;
        this.priviledgeId = priviledgeId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPriviledgeId() {
        return priviledgeId;
    }

    public void setPriviledgeId(String priviledgeId) {
        this.priviledgeId = priviledgeId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.roleId);
        dest.writeString(this.jobTitle);
        dest.writeString(this.priviledgeId);
    }

    protected Role(Parcel in) {
        this.roleId = in.readInt();
        this.jobTitle = in.readString();
        this.priviledgeId = in.readString();
    }

    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel source) {
            return new Role(source);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", jobTitle='" + jobTitle + '\'' +
                ", priviledgeId='" + priviledgeId + '\'' +
                '}';
    }
}
