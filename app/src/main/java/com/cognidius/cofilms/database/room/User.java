package com.cognidius.cofilms.database.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    @NonNull
    public String userName;

    @ColumnInfo(name = "passWord" )
    @NonNull
    public String passWord;

    @ColumnInfo(name = "nickName" )
    public String nickName;

    @ColumnInfo(name = "country" )
    public String country;

    @ColumnInfo(name = "gender" )
    public String gender;

    @ColumnInfo(name = "community" )
    public String community;

    @ColumnInfo(name = "birthOfDate" )
    public String birthOfDate;



    public User(@NonNull String userName, @NonNull String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(@NonNull String passWord) {
        this.passWord = passWord;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrganization() {
        return community;
    }

    public void setOrganization(String organization) {
        this.community = organization;
    }

    public String getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(String birthOfDate) {
        this.birthOfDate = birthOfDate;
    }

}
