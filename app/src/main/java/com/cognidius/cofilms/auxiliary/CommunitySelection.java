package com.cognidius.cofilms.auxiliary;

public class CommunitySelection {
    private static boolean isEducation = false, isEntertainment = false, isSports = false, isPolitics = false;

    public static void touchEducation(){
        isEducation = !isEducation;
    }

    public static void touchEntertainment(){
        isEntertainment = !isEntertainment;
    }

    public static void touchSports(){
        isSports = !isSports;
    }

    public static void touchPolitics(){
        isPolitics = !isPolitics;
    }


    public static boolean isIsEducation() {
        return isEducation;
    }

    public static boolean isIsEntertainment() {
        return isEntertainment;
    }

    public static boolean isIsSports() {
        return isSports;
    }

    public static boolean isIsPolitics() {
        return isPolitics;
    }
}
