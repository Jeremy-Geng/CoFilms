package com.cognidius.cofilms.database;

public class LoggedUser {
    private static String USERNAME;
    private static String PASSWORD;
    private static String NICKNAME;
    private static String COUNTRY;
    private static String GENDER;
    private static String ORGANIZATION;
    private static String BIRTHOFDATE;

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getNICKNAME() {
        return NICKNAME;
    }

    public static String getCOUNTRY() {
        return COUNTRY;
    }

    public static String getGENDER() {
        return GENDER;
    }

    public static String getORGANIZATION() {
        return ORGANIZATION;
    }

    public static String getBIRTHOFDATE() {
        return BIRTHOFDATE;
    }

    public static void setUSERNAME(String USERNAME) {
        LoggedUser.USERNAME = USERNAME;
    }

    public static void setPASSWORD(String PASSWORD) {
        LoggedUser.PASSWORD = PASSWORD;
    }

    public static void setNICKNAME(String NICKNAME) {
        LoggedUser.NICKNAME = NICKNAME;
    }

    public static void setCOUNTRY(String COUNTRY) {
        LoggedUser.COUNTRY = COUNTRY;
    }

    public static void setGENDER(String GENDER) {
        LoggedUser.GENDER = GENDER;
    }

    public static void setORGANIZATION(String ORGANIZATION) {
        LoggedUser.ORGANIZATION = ORGANIZATION;
    }

    public static void setBIRTHOFDATE(String BIRTHOFDATE) {
        LoggedUser.BIRTHOFDATE = BIRTHOFDATE;
    }
}
