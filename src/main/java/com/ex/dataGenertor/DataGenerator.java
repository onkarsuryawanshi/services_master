package com.ex.dataGenertor;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
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
        LocalDate dateTime = LocalDate.now();
        return dateTime;
    }

    public LocalTime getTime() {
        return LocalTime.now();
    }
}
