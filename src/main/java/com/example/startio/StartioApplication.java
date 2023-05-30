package com.example.startio;

import com.example.startio.service.FilesHandling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartioApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartioApplication.class, args);
        FilesHandling filesHandling = new FilesHandling();
        filesHandling.readFileAndInsertToDB("requests.csv");
        filesHandling.readFileAndInsertToDB("impressions.csv");
        filesHandling.readFileAndInsertToDB("clicks.csv");
    }

}
