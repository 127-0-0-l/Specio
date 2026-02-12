package io.github._127_0_0_l.infra_tg_bot.services;

import io.github._127_0_0_l.infra_tg_bot.models.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

@Service
public class UpdateHandler implements LongPollingUpdateConsumer {
    private final TelegramClient telegramClient;
    private final NotificationService notificationService;
    private final List<String> regions = List.of("Minsk region", "Brest region", "Grodno region", "Vitebsk region",
            "Gomel region", "Mogilev region");
    private final List<String> cities = List.of("Minsk", "Brest", "Bobruisk", "Visokaye", "Kamenets", "Baranovichi");
    private Map<Long, Chat> chats = new HashMap<>();

    public UpdateHandler(TelegramClientProvider telegramClientProvider,
                         NotificationService notificationService){
        telegramClient = telegramClientProvider.getTelegramClient();
        this.notificationService = notificationService;
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            long chatId;
            String text;
            int lastMessageId;

            if (update.hasMessage() && update.getMessage().hasText()) {
                chatId = update.getMessage().getChatId();
                text = update.getMessage().getText();
            } else if (update.hasCallbackQuery()){
                chatId = update.getCallbackQuery().getMessage().getChatId();
                text = update.getCallbackQuery().getData();
                lastMessageId = update.getCallbackQuery().getMessage().getMessageId();
            } else {
                continue;
            }

            handleMessage(chatId, text);
        }
    }

    private void handleMessage(long chatId, String text){
        chats.putIfAbsent(chatId, new Chat(chatId, ChatState.INIT, new Filters()));
        ChatState state = chats.get(chatId).getState();

        switch (state){
            case ChatState.INIT -> handleInitCase(chatId, text);
            case ChatState.IDLE -> handleIdleCase(chatId, text);
            case ChatState.NOTIFYING -> handleNotifyingCase(chatId, text);
            case ChatState.WAITING_FOR_FILTER_OPTION -> handleWaitingForFilterOptionCase(chatId, text);
            case ChatState.WAITING_FOR_YEAR_OPTION -> handleWaitingForYearOptionCase(chatId, text);
            case ChatState.WAITING_FOR_YEAR_FROM -> handleWaitingForYearFromCase(chatId, text);
            case ChatState.WAITING_FOR_YEAR_TO -> handleWaitingForYearToCase(chatId, text);
            case ChatState.WAITING_FOR_PRICE_OPTION -> handleWaitingForPriceOptionCase(chatId, text);
            case ChatState.WAITING_FOR_PRICE_FROM -> handleWaitingForPriceFromCase(chatId, text);
            case ChatState.WAITING_FOR_PRICE_TO -> handleWaitingForPriceToCase(chatId, text);
            case ChatState.WAITING_FOR_REGION -> handleWaitingForRegionCase(chatId, text);
            case ChatState.WAITING_FOR_CITY -> handleWaitingForCityCase(chatId, text);
        }
    }

    private void handleInitCase(long chatId, String text){
        switch (text){
            case "/start":
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("run"));
                row.add(new ChatKeyboardButton("set filters"));
                buttons.add(row);

                boolean exists = true;
                if (exists) {
                    notificationService.notifyWithKeyboardButtons(
                            chatId, "welcome back\n your last settings are:", buttons);
                } else {
                    notificationService.notifyWithKeyboardButtons(chatId, "welcome", buttons);
                }
                chats.get(chatId).setState(ChatState.IDLE);
                break;
            default:
                break;
        }
    }

    private void handleIdleCase(long chatId, String text){
        switch (text) {
            case "run":
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("stop"));
                buttons.add(row);
                notificationService.notifyWithKeyboardButtons(chatId, "start notifying", buttons);
                // activate
                chats.get(chatId).setState(ChatState.NOTIFYING);
                break;
            case "set filters":
                List<Queue<ChatInlineButton>> buttons1 = new ArrayList<>();
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("Region", "region"));
                row1.add(new ChatInlineButton("City", "city"));
                Queue<ChatInlineButton> row2 = new LinkedList<>();
                row2.add(new ChatInlineButton("Price", "price"));
                row2.add(new ChatInlineButton("Year", "year"));
                buttons1.add(row1);
                buttons1.add(row2);
                chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
                notificationService.notifyWithInlineButtons(
                        chatId, "current filters:\nchoose filter to set", buttons1);
                break;
            default:
                break;
        }
    }

    private void handleNotifyingCase(long chatId, String text){
        switch (text) {
            case "stop":
                // deactivate
                chats.get(chatId).setState(ChatState.IDLE);
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("run"));
                row.add(new ChatKeyboardButton("set filters"));
                buttons.add(row);
                notificationService.notifyWithKeyboardButtons(chatId, "stopped", buttons);
                break;
            default:
                break;
        }
    }

    private void handleWaitingForFilterOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();

        switch (text) {
            case "region":
                for (String region : regions){
                    Queue<ChatInlineButton> row = new LinkedList<>();
                    row.add(new ChatInlineButton(region, region));
                    buttons.add(row);
                }
                Queue<ChatInlineButton> row5 = new LinkedList<>();
                row5.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row5);
                notificationService.notifyWithInlineButtons(chatId, "chose regions", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_REGION);
                break;
            case "city":
                for (String city : cities){
                    Queue<ChatInlineButton> row = new LinkedList<>();
                    row.add(new ChatInlineButton(city, city));
                    buttons.add(row);
                }
                Queue<ChatInlineButton> row6 = new LinkedList<>();
                row6.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row6);
                chats.get(chatId).setState(ChatState.WAITING_FOR_CITY);
                notificationService.notifyWithInlineButtons(chatId, "chose city", buttons);
                break;
            case "price":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("price from", "price_from"));
                row.add(new ChatInlineButton("price to", "price_to"));
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row);
                buttons.add(row1);
                chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_OPTION);
                notificationService.notifyWithInlineButtons(chatId, "chose price option", buttons);
                break;
            case "year":
                Queue<ChatInlineButton> row3 = new LinkedList<>();
                row3.add(new ChatInlineButton("year from", "year_from"));
                row3.add(new ChatInlineButton("year to", "year_to"));
                Queue<ChatInlineButton> row4 = new LinkedList<>();
                row4.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row3);
                buttons.add(row4);
                chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_OPTION);
                notificationService.notifyWithInlineButtons(chatId, "chose year options", buttons);
                break;
            case "back":
                List<Queue<ChatKeyboardButton>> buttons1 = new ArrayList<>();
                Queue<ChatKeyboardButton> row2 = new LinkedList<>();
                row2.add(new ChatKeyboardButton("run"));
                row2.add(new ChatKeyboardButton("set filters"));
                buttons1.add(row2);
                chats.get(chatId).setState(ChatState.IDLE);
                notificationService.notifyWithKeyboardButtons(chatId, "", buttons1);
                break;
            default:
                break;
        }
    }

    private void handleWaitingForYearOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();

        switch (text){
            case "year_from":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("back", "back"));
                buttons.add(row);
                notificationService.notifyWithInlineButtons(chatId, "enter year_from", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_FROM);
                break;
            case "year_to":
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("back", "back"));
                buttons.add(row1);
                notificationService.notifyWithInlineButtons(chatId, "enter year_to", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_TO);
                break;
            case "submit":
                Queue<ChatInlineButton> row2 = new LinkedList<>();
                row2.add(new ChatInlineButton("Region", "region"));
                row2.add(new ChatInlineButton("City", "city"));
                Queue<ChatInlineButton> row3 = new LinkedList<>();
                row3.add(new ChatInlineButton("Price", "price"));
                row3.add(new ChatInlineButton("Year", "year"));
                buttons.add(row2);
                buttons.add(row3);
                chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
                notificationService.notifyWithInlineButtons(
                        chatId, "current filters:\nchoose filter to set", buttons);
                break;
        }
    }

    private void handleWaitingForYearFromCase(long chatId, String text){
        boolean valid = true;

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("year from", "year_from"));
        row.add(new ChatInlineButton("year to", "year_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose year options", buttons);
        } else {
            notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose year options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_OPTION);
    }

    private void handleWaitingForYearToCase(long chatId, String text){
        boolean valid = false;

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("year from", "year_from"));
        row.add(new ChatInlineButton("year to", "year_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose year options", buttons);
        } else {
            notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose year options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_OPTION);
    }

    private void handleWaitingForPriceOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();

        switch (text){
            case "price_from":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("back", "back"));
                buttons.add(row);
                notificationService.notifyWithInlineButtons(chatId, "enter price_from", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_FROM);
                break;
            case "price_to":
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("back", "back"));
                buttons.add(row1);
                notificationService.notifyWithInlineButtons(chatId, "enter price_to", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_TO);
                break;
            case "submit":
                Queue<ChatInlineButton> row2 = new LinkedList<>();
                row2.add(new ChatInlineButton("Region", "region"));
                row2.add(new ChatInlineButton("City", "city"));
                Queue<ChatInlineButton> row3 = new LinkedList<>();
                row3.add(new ChatInlineButton("Price", "price"));
                row3.add(new ChatInlineButton("Year", "year"));
                buttons.add(row2);
                buttons.add(row3);
                chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
                notificationService.notifyWithInlineButtons(
                        chatId, "current filters:\nchoose filter to set", buttons);
                break;
        }
    }

    private void handleWaitingForPriceFromCase(long chatId, String text){
        boolean valid = true;

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("price from", "price_from"));
        row.add(new ChatInlineButton("price to", "price_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose price options", buttons);
        } else {
            notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose price options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_OPTION);
    }

    private void handleWaitingForPriceToCase(long chatId, String text){
        boolean valid = true;

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("price from", "price_from"));
        row.add(new ChatInlineButton("price to", "price_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose price options", buttons);
        } else {
            notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose price options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_OPTION);
    }

    private void handleWaitingForRegionCase(long chatId, String text){
        if (text.equals("submit")) {
            chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);

            List<Queue<ChatInlineButton>> buttons1 = new ArrayList<>();
            Queue<ChatInlineButton> row1 = new LinkedList<>();
            row1.add(new ChatInlineButton("Region", "region"));
            row1.add(new ChatInlineButton("City", "city"));
            Queue<ChatInlineButton> row2 = new LinkedList<>();
            row2.add(new ChatInlineButton("Price", "price"));
            row2.add(new ChatInlineButton("Year", "year"));
            buttons1.add(row1);
            buttons1.add(row2);

            notificationService.notifyWithInlineButtons(
                    chatId, "current filters:\nchoose filter to set", buttons1);
            return;
        }

        if (regions.contains(text)){
            notificationService.notify(chatId, text + " region toggled");
            // toggle button
        }
    }

    private void handleWaitingForCityCase(long chatId, String text){
        if (text.equals("submit")) {
            chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);

            List<Queue<ChatInlineButton>> buttons1 = new ArrayList<>();
            Queue<ChatInlineButton> row1 = new LinkedList<>();
            row1.add(new ChatInlineButton("Region", "region"));
            row1.add(new ChatInlineButton("City", "city"));
            Queue<ChatInlineButton> row2 = new LinkedList<>();
            row2.add(new ChatInlineButton("Price", "price"));
            row2.add(new ChatInlineButton("Year", "year"));
            buttons1.add(row1);
            buttons1.add(row2);

            notificationService.notifyWithInlineButtons(
                    chatId, "current filters:\nchoose filter to set", buttons1);
            return;
        }

        if (cities.contains(text)){
            notificationService.notify(chatId, text + " city toggled");
            // toggle button
        }
    }
}
