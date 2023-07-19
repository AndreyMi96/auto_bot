package com.example.auto_bot.controller;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    //Клавиатура
    public static ReplyKeyboardMarkup getAdsKeyboardCity() {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setResizeKeyboard(true); // Установка параметра resize_keyboard в true

        // Создание двух строк строки кнопок
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add("Менее 3-х лет");
        row1.add("От 3-х до 5 лет");
        row2.add("Более 5 лет");
        row2.add("Более 7 лет");


        // Добавление строк кнопок в клавиатуру
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(row1);
        keyboardRows.add(row2);

        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }

    //Клавиатура
    public static ReplyKeyboardMarkup getAdsKeyboard() {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setResizeKeyboard(true); // Установка параметра resize_keyboard в true

        // Создание двух строк строки кнопок
        KeyboardRow row1 = new KeyboardRow();


        row1.add("Расчитать стоймость");

        // Добавление строк кнопок в клавиатуру
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(row1);

        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }
}
