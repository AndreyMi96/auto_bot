package com.example.auto_bot.controller;




import com.example.auto_bot.config.BotConfig;
import com.example.auto_bot.state.BotState;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Slf4j
@EnableScheduling
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private Map<Long, BotState> userStateMap = new HashMap<>();
    
    private String nameAuto;
    private int saleAuto;
    private int enganeAuto;
    private int sbor;

    

    public TelegramBot(BotConfig config) throws GeneralSecurityException, IOException {
        this.config = config;

        try {
            List<BotCommand> listOfCommands = new ArrayList<>();
            listOfCommands.add(new BotCommand("/start", "Запуск"));
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            processMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery());
        }
    }

    private void processMessage(Message message) throws GeneralSecurityException, IOException {
        String messageText = message.getText();
        long chatId = message.getChatId();
        BotState botState = userStateMap.getOrDefault(chatId, BotState.USER);

        switch (messageText) {
            case "/start":
//                startCommandReceived(chatId, message.getChat().getFirstName());
                sendMessage(chatId, "Приветсвую", Keyboard.getAdsKeyboard());
                break;

            case "Расчитать стоймость":
                sendMessage(chatId, "Возраст авто", Keyboard.getAdsKeyboardCity());
                userStateMap.put(chatId, BotState.INFO1);
                break;


            default:
                if (botState == BotState.INFO1) {
                    nameAuto = messageText;
                    sendMessage(chatId, "Укажите стоймость авто в €", null);
                    userStateMap.put(chatId, BotState.INFO2);
                } else if (botState == BotState.INFO2) {
                    saleAuto = Integer.parseInt(messageText);
                    sendMessage(chatId, "Укажите объем см³", null);
                    userStateMap.put(chatId, BotState.INFO3);
                } else if (botState == BotState.INFO3) {
                    enganeAuto = Integer.parseInt(messageText);
                    int a = getAuto(nameAuto, saleAuto, enganeAuto);
                    String itogo = "Результаты расчета: " + "\n\n" +
                            "Срок авто - " + nameAuto + "\n" +
                            "Стоймость авто - " + saleAuto + " €" + "\n" +
                            "Объем двигателя - " + enganeAuto + " см³" + "\n\n" +
                            "Таможенная пошлина = " + a + " €" + "\n" +
                            "Льготная пошлина = " + a/2 + " €" + "\n\n" +
                            "Таможенный сбор = 120 руб." + "\n" +
                            "Утиль сбор = 116 руб." + "\n" +
                            "Услуги декларанта = 100 - 150 руб.";
                    sendMessage(chatId, itogo, null);
                    userStateMap.put(chatId, BotState.USER);
                }

                break;
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) throws GeneralSecurityException, IOException {
        long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();
        long messageId = callbackQuery.getMessage().getMessageId();
        String messageText = callbackQuery.getMessage().getText();
    }




    //Приветсвие
    private void startCommandReceived(long chatId, String firstName) {
        String welcomeMessage = "Привет, " + firstName + " кучерявого настроения.";
        sendMessage(chatId, welcomeMessage, null);
    }


    //Отправка нового сообщения
    private void sendMessage(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);
        executeMessage(message);

    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private int getEnganeAuto1(int saleAuto, int enganeAuto) {

        double sales1 = 0;
        double sales2 = 0;

        if (saleAuto <= 8500) {
            sales1 = saleAuto * 0.54;
        } else {
            sales1 = saleAuto * 0.48;
        }

        if (saleAuto <= 8500) {
            sales1 = saleAuto * 0.54;
            sales2 = enganeAuto * 2.5;
        } else if (saleAuto <= 16700) {
            sales1 = saleAuto * 0.48;
            sales2 = enganeAuto * 3.5;
        } else if (saleAuto <= 42300) {
            sales1 = saleAuto * 0.48;
            sales2 = enganeAuto * 5.5;
        } else if (saleAuto <= 84500) {
            sales1 = saleAuto * 0.48;
            sales2 = 7.5;
        } else if (saleAuto <= 169000) {
            sales1 = saleAuto * 0.48;
            sales2 = 15;
        } else if (saleAuto > 16900) {
            sales1 = saleAuto * 0.48;
            sales2 = 20;
        }


        int a = (int) (sales1 >= sales2 ? sales1: sales2);

        return a;
    }


    private int getEnganeAuto2(int enganeAuto) {
        double abc = 0;

        if (enganeAuto <= 1000) {
            abc = 1.5;
        } else if (enganeAuto <= 1500) {
            abc = 1.7;
        } else if (enganeAuto <= 1800) {
            abc = 2.5;
        } else if (enganeAuto <= 2300) {
            abc = 2.7;
        } else if (enganeAuto <= 3000) {
            abc = 3;
        } else if (enganeAuto > 3000) {
            abc = 3.6;
        }


        int a = (int) (enganeAuto * abc);

        return a;
    }

    private int getEnganeAuto3(int enganeAuto) {
        double abc = 0;

        if (enganeAuto <= 1000) {
            abc = 3;
        } else if (enganeAuto <= 1500) {
            abc = 3.2;
        } else if (enganeAuto <= 1800) {
            abc = 3.5;
        } else if (enganeAuto <= 2300) {
            abc = 4.8;
        } else if (enganeAuto <= 3000) {
            abc = 5;
        } else if (enganeAuto > 3000) {
            abc = 5.7;
        }


        int a = (int) (enganeAuto * abc);
        System.out.println(a);

        return a;
    }

    private int getAuto(String nameAuto, int saleAuto, int enganeAuto) {

        int abc = 0;
        switch (nameAuto) {
            case "Менее 3-х лет":
                abc = getEnganeAuto1(saleAuto, enganeAuto);
                break;
            case "От 3-х до 5 лет":
                abc = getEnganeAuto2(enganeAuto);
                break;
            case "Более 5 лет":
                abc = getEnganeAuto3(enganeAuto);
                break;
            case "Более 7 лет":
                abc = getEnganeAuto3(enganeAuto);
                break;
        }
        return abc;
    }




}
