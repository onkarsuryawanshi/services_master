package com.service_3.dataGenertor;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class DataGenerator {

    public String getAverageSpeedOfDevice(){
        Random random = new Random();
        return (random.nextFloat() *100) + "Mbps";
    }

    public int getDataUsed(){
        Random random = new Random();
        return random.nextInt(1000);
    }

    public LocalDate getDate() {
        LocalDate randomDate = createRandomDate(2020, 2022);
        return randomDate;
    }
    private LocalDate createRandomDate(int startYear, int endYear) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(startYear, endYear);
        return LocalDate.of(year, month, day);
    }
    public static int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public LocalTime getTime() {
        return LocalTime.now();
    }
}
