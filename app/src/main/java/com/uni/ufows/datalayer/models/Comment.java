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
@Table(name = "Comments")
public class Comment extends Model implements Parcelable {
    @Expose
    @Column(name = "Comment_Id", unique = true)
    String Comment_Id;
    @Expose
    @Column(name="Timestamp")
    String Timestamp;
    @Expose
    @Column(name="Content")
    String Content;
    @Expose
    @Column(name="User_Id_by")
    String User_Id_by;
    @Expose
    @Column(name="User_Id_about")
    String User_Id_about;
    @Expose
    @Column(name="Tank_Id_about")
    String Tank_Id_about;
    public Comment(){}
    public static List<Comment> getAll(){
        return new Select().from(Comment.class).execute();
    }
    public static Comment selectById(String id){
        return new Select().from(Comment.class).where("Comment_Id = '"+id+"'").executeSingle();
    }
    public static List<Comment> selectAll(){
        return new Select().from(Comment.class).execute();
    }
    public Comment(String comment_Id, String timestamp, String content, String user_Id_by, String user_Id_about, String tank_Id_about) {
        Comment_Id = comment_Id;
        Timestamp = timestamp;
        Content = content;
        User_Id_by = user_Id_by;
        User_Id_about = user_Id_about;
        Tank_Id_about = tank_Id_about;
    }

    public String getComment_Id() {
        return Comment_Id;
    }

    public void setComment_Id(String comment_Id) {
        Comment_Id = comment_Id;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getUser_Id_by() {
        return User_Id_by;
    }

    public void setUser_Id_by(String user_Id_by) {
        User_Id_by = user_Id_by;
    }

    public String getUser_Id_about() {
        return User_Id_about;
    }

    public void setUser_Id_about(String user_Id_about) {
        User_Id_about = user_Id_about;
    }

    public String getTank_Id_about() {
        return Tank_Id_about;
    }

    public void setTank_Id_about(String tank_Id_about) {
        Tank_Id_about = tank_Id_about;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Comment_Id);
        dest.writeString(this.Timestamp);
        dest.writeString(this.Content);
        dest.writeString(this.User_Id_by);
        dest.writeString(this.User_Id_about);
        dest.writeString(this.Tank_Id_about);
    }

    protected Comment(Parcel in) {
        this.Comment_Id = in.readString();
        this.Timestamp = in.readString();
        this.Content = in.readString();
        this.User_Id_by = in.readString();
        this.User_Id_about = in.readString();
        this.Tank_Id_about = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public String toString() {
        return "Comment{" +
                "Comment_Id='" + Comment_Id + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", Content='" + Content + '\'' +
                ", User_Id_by='" + User_Id_by + '\'' +
                ", User_Id_about='" + User_Id_about + '\'' +
                ", Tank_Id_about='" + Tank_Id_about + '\'' +
                '}';
    }
}
