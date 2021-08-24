package com.mindhub.homebanking.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CardUtils {
    private CardUtils() {
    }

    public static int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max-min)) + min);
    }

    public static String getNumber() {
        return getRandomNumber(4000,9999) + "-" + getRandomNumber(3000,9999) +"-"+ getRandomNumber(2000,9999) +"-"+ getRandomNumber(1000,9999);
    }

    public static String getDateFormat(){
        LocalDateTime today= LocalDateTime.now();

        DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return today.format(todayFormat);
    }

    public static String getDateFormatCard(){
        LocalDate todayCard= LocalDate.now();

        DateTimeFormatter todayFormatCard = DateTimeFormatter.ofPattern("MM-yy");

        return todayCard.format(todayFormatCard);
    }


}
