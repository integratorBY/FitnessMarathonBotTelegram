package com.example.fitnessMarathonBot.regex;

public class RegexHandler {

    public static boolean checkDate(String date) {
        return date.matches("^(?:(?:31(\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
    }

    public static boolean checkUserAnswerOnDigit(String userAnswer) {
        return userAnswer.matches("[0-99]+");
    }

    public static boolean checkUserAnswerOnPeriod(String userAnswer) {
        return userAnswer.matches("[0-99]-[0-99]");
    }


}
