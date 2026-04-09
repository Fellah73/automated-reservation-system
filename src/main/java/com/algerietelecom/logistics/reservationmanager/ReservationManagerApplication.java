package com.algerietelecom.logistics.reservationmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ReservationManagerApplication {

    public static void main(String[] args) {
        // Charge le fichier .env
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Injecte les variables dans les propriétés système de Java
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(ReservationManagerApplication.class, args);
    }

}
