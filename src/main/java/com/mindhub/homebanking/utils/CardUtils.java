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
        String number = getRandomNumber(4000,9999) + "-" + getRandomNumber(3000,9999) +"-"+ getRandomNumber(2000,9999) +"-"+ getRandomNumber(1000,9999);
        return number;
    }

    public static String getDateFormat(){
        LocalDateTime today= LocalDateTime.now();

        DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(todayFormat);

        return formattedDate;
    }

    public static String getDateFormatCard(){
        LocalDate todayCard= LocalDate.now();

        DateTimeFormatter todayFormatCard = DateTimeFormatter.ofPattern("MM-yy");
        String formattedDateCard = todayCard.format(todayFormatCard);

        return formattedDateCard;
    }


}
