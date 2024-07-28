package com.example.demo.helpers;

import java.util.Random;

public class DataHelper {

    //Другая реализация - прокидывать сервисы в параметры в этом классе , тк данный класс ничего не знает про Spring
    /* static void prepareNewsWithUsers(final String categoryName) {
        final UserCreateResponse petrPetrov = userService.createUser(new UserCreateRequest("Petrov"));
        final UserCreateResponse userOther = userService.createUser(new UserCreateRequest("Callinial"));
        newsService.createNews(NewsDto.builder().title("test").content("test")
                .userId(petrPetrov.userId()).categoryName(categoryName).build());
        newsService.createNews(NewsDto.builder().title("testOther").content("testOther")
                .userId(userOther.userId()).categoryName(categoryName).build());
    }*/

    public static String getAlphabeticString(final int targetStringLength) {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        return new Random().ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public static String getNumeric(final int targetStringLength) {
        if (targetStringLength < 1) {
            throw new IllegalArgumentException("Wrong");
        }

        return "+" + new Random().nextInt((int) Math.pow(10, targetStringLength - 1),
            (int) Math.pow(10, targetStringLength) - 1);
    }

    public static Integer getNumber(final int targetStringLength) {
        if (targetStringLength < 1) {
            throw new IllegalArgumentException("Wrong");
        }

        return new Random().nextInt((int) Math.pow(10, targetStringLength - 1),
            (int) Math.pow(10, targetStringLength) - 1);
    }

    public static News.NewsBuilder<?, ?> prepareNews() {
        return News.builder().title("testNews").content("test");
    }

    public static Category.CategoryBuilder<?, ?> prepareCategory() {
        return Category.builder().name("test1");
    }

    public static Category.CategoryBuilder<?, ?> preparePlainCategory() {
        return Category.builder();
    }

    public static User.UserBuilder<?, ?> prepareUser() {
        return User.builder().username("Test");
    }

    //TODO добавить лист с комментариями
    public static User preparePetrPetrov() {
        return User.builder()
            .id(Long.valueOf(getNumeric(3)))
            .username("PetrPetrov")
            .build();
    }

}
