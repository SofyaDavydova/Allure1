package ru.netology.delivery;

import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int days, String pattern) {
        String date = LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
        return date;
    }

    public static String generateCity() {
        String[] cities = new String[]{"Горно-Алтайск", "Уфа", "Улан-Удэ", "Махачкала", "Элиста", " Самара", "Сыктывкар"};
        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateName(Faker faker) {
        String name = faker.name().lastName() + " " + faker.name().firstName();
        return name;
    }

    public static String generatePhone(Faker faker) {
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static class Registration {
        private static Faker faker;

        private Registration() {
        }

        @Step
        public static UserInfo generateUser(String locale) {
            faker = new Faker(new Locale(locale));
            UserInfo user = new UserInfo(generateCity(),
                    generateName(faker), generatePhone(faker));
            return user;
        }
    }

        @Value
        public static class UserInfo {
            String city;
            String name;
            String phone;

        }
}
