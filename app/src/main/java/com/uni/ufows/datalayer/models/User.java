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

/**
 * Created by sammy on 2/28/2017.
 */
@Table(name = "User")
public class User extends Model implements Parcelable {
    @SerializedName("Id")
    @Column(name = "User_Id", unique = true)
    String userId;
    @SerializedName("First_name_ar")
    @Column(name = "First_name_ar")
    String firstNameAr;
    @SerializedName("Last_name_ar")
    @Column(name = "Last_name_ar")
    String lastNameAr;
    @SerializedName("First_name_en")
    @Column(name = "First_name_en")
    String firstNameEn;
    @SerializedName("Last_name_en")
    @Column(name = "Last_name_en")
    String lastNameEn;
    @SerializedName("Phone_number")
    @Column(name = "Phone_number")
    String phoneNumber;
    @SerializedName("Username")
    @Column(name = "Username")
    String userName;
    @SerializedName("Password")
    @Column(name = "Password")
    String password;
    @SerializedName("Employer_Id")
    @Column(name = "Employer_Id")
    String employerId;
    @SerializedName("Rating")
    @Column(name = "Rating")
    String rating;
    @SerializedName("created_at")
    @Column(name = "created_at")
    String createdAt;
    @SerializedName("updated_at")
    @Column(name = "updated_at")
    String updatedAt;
    @SerializedName("isDeleted")
    @Column(name = "isDeleted")
    String isDeleted;
    @SerializedName("Role_Id")
    @Column(name = "Role_Id")
    String roleId;
    @SerializedName("ufow_app")
    @Column(name = "ufow_app")
    String ufow_app;


    public static User getUserById(String driverId){
        return  new Select().from(User.class).where("User_Id = '"+driverId+"'").executeSingle();
    }
    public static User getUserByName(String driverId){
        return  new Select().from(User.class).where("Username = '"+driverId+"'").executeSingle();
    }
    public static List<User> getAll(){
        return new Select().from(User.class).execute();
    }
    public static User getOldDate(){
        return  new Select().from(User.class).orderBy("id DESC").executeSingle();
    }
    public static User login(String username, String password){
        return  new Select().from(User.class).where("Username = '"+username+"' AND Password= '"+password+"'").executeSingle();
    }

    public User(String userId, String firstNameAr, String lastNameAr, String firstNameEn, String lastNameEn, String phoneNumber, String userName, String password, String employerId, String rating, String createdAt, String updatedAt, String isDeleted, String roleId, String ufow_app) {
        this.userId = userId;
        this.firstNameAr = firstNameAr;
        this.lastNameAr = lastNameAr;
        this.firstNameEn = firstNameEn;
        this.lastNameEn = lastNameEn;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.password = password;
        this.employerId = employerId;
        this.rating = rating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.roleId = roleId;
        this.ufow_app = ufow_app;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstNameAr() {
        return firstNameAr;
    }

    public void setFirstNameAr(String firstNameAr) {
        this.firstNameAr = firstNameAr;
    }

    public String getLastNameAr() {
        return lastNameAr;
    }

    public void setLastNameAr(String lastNameAr) {
        this.lastNameAr = lastNameAr;
    }

    public String getFirstNameEn() {
        return firstNameEn;
    }

    public void setFirstNameEn(String firstNameEn) {
        this.firstNameEn = firstNameEn;
    }

    public String getLastNameEn() {
        return lastNameEn;
    }

    public void setLastNameEn(String lastNameEn) {
        this.lastNameEn = lastNameEn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUfow_app() {
        return ufow_app;
    }

    public void setUfow_app(String ufow_app) {
        this.ufow_app = ufow_app;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.firstNameAr);
        dest.writeString(this.lastNameAr);
        dest.writeString(this.firstNameEn);
        dest.writeString(this.lastNameEn);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.employerId);
        dest.writeString(this.rating);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.isDeleted);
        dest.writeString(this.roleId);
        dest.writeString(this.ufow_app);
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.firstNameAr = in.readString();
        this.lastNameAr = in.readString();
        this.firstNameEn = in.readString();
        this.lastNameEn = in.readString();
        this.phoneNumber = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.employerId = in.readString();
        this.rating = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.isDeleted = in.readString();
        this.roleId = in.readString();
        this.ufow_app = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstNameAr='" + firstNameAr + '\'' +
                ", lastNameAr='" + lastNameAr + '\'' +
                ", firstNameEn='" + firstNameEn + '\'' +
                ", lastNameEn='" + lastNameEn + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", employerId='" + employerId + '\'' +
                ", rating='" + rating + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", roleId='" + roleId + '\'' +
                ", ufow_app='" + ufow_app + '\'' +
                '}';
    }
    public User(){}
}

