package io.github._127_0_0_l.infra_tg_bot.services;

import io.github._127_0_0_l.infra_tg_bot.models.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDateTime;
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
            case ChatState.INACTIVE -> handleInitCase(chatId, text);
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
        int messageId;
        switch (text){
            case "/start":
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("run"));
                row.add(new ChatKeyboardButton("set filters"));
                row.add(new ChatKeyboardButton("/stop"));
                buttons.add(row);

                if (chats.get(chatId).getState() == ChatState.INIT) {
                    messageId = notificationService.notifyWithKeyboardButtons(chatId, "welcome", buttons);
                } else {
                    messageId = notificationService.notifyWithKeyboardButtons(
                            chatId,
                            "welcome back\n your last settings are:" + chats.get(chatId).getFilters().toString(),
                            buttons);
                }
                chats.get(chatId).setState(ChatState.IDLE);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            default:
                break;
        }
    }

    private void handleIdleCase(long chatId, String text){
        int messageId;
        switch (text) {
            case "run":
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("stop"));
                buttons.add(row);
                messageId = notificationService.notifyWithKeyboardButtons(chatId, "start notifying", buttons);
                chats.get(chatId).setState(ChatState.NOTIFYING);
                chats.get(chatId).setLastMessageId(messageId);
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
                messageId = notificationService.notifyWithInlineButtons(
                        chatId, "current filters:\nchoose filter to set", buttons1);
                chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            case "/stop":
                List<Queue<ChatKeyboardButton>> buttons2 = new ArrayList<>();
                Queue<ChatKeyboardButton> row3 = new LinkedList<>();
                row3.add(new ChatKeyboardButton("/start"));
                buttons2.add(row3);
                messageId = notificationService.notifyWithKeyboardButtons(chatId, "stopped", buttons2);
                chats.get(chatId).setState(ChatState.INACTIVE);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            default:
                break;
        }
    }

    private void handleNotifyingCase(long chatId, String text){
        switch (text) {
            case "stop":
                // deactivate
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("run"));
                row.add(new ChatKeyboardButton("set filters"));
                row.add(new ChatKeyboardButton("/stop"));
                buttons.add(row);
                int mId = notificationService.notifyWithKeyboardButtons(chatId, "stop nitifying", buttons);
                chats.get(chatId).setState(ChatState.IDLE);
                chats.get(chatId).setLastMessageId(mId);
                break;
            default:
                break;
        }
    }

    private void handleWaitingForFilterOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        int messageId;
        var chat = chats.get(chatId);

        switch (text) {
            case "region":
                for (String region : regions){
                    Queue<ChatInlineButton> row = new LinkedList<>();
                    row.add(new ChatInlineButton(
                            (chat.getFilters().getRegions().contains(region) ? "🟢 " : "⭕️ ") + region,
                            region));
                    buttons.add(row);
                }
                Queue<ChatInlineButton> row5 = new LinkedList<>();
                row5.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row5);
                messageId = notificationService.notifyWithInlineButtons(chatId, "chose regions", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_REGION);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            case "city":
                for (String city : cities){
                    Queue<ChatInlineButton> row = new LinkedList<>();
                    row.add(new ChatInlineButton(
                            (chat.getFilters().getCities().contains(city) ? "🟢 " : "⭕️ ") + city,
                            city));
                    buttons.add(row);
                }
                Queue<ChatInlineButton> row6 = new LinkedList<>();
                row6.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row6);
                messageId = notificationService.notifyWithInlineButtons(chatId, "chose city", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_CITY);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            case "price":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("price from", "price_from"));
                row.add(new ChatInlineButton("price to", "price_to"));
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row);
                buttons.add(row1);
                messageId = notificationService.notifyWithInlineButtons(chatId, "chose price option", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_OPTION);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            case "year":
                Queue<ChatInlineButton> row3 = new LinkedList<>();
                row3.add(new ChatInlineButton("year from", "year_from"));
                row3.add(new ChatInlineButton("year to", "year_to"));
                Queue<ChatInlineButton> row4 = new LinkedList<>();
                row4.add(new ChatInlineButton("submit", "submit"));
                buttons.add(row3);
                buttons.add(row4);
                messageId = notificationService.notifyWithInlineButtons(chatId, "chose year options", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_OPTION);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            case "back":
                List<Queue<ChatKeyboardButton>> buttons1 = new ArrayList<>();
                Queue<ChatKeyboardButton> row2 = new LinkedList<>();
                row2.add(new ChatKeyboardButton("run"));
                row2.add(new ChatKeyboardButton("set filters"));
                buttons1.add(row2);
                messageId = notificationService.notifyWithKeyboardButtons(chatId, "", buttons1);
                chats.get(chatId).setState(ChatState.IDLE);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            default:
                break;
        }
    }

    private void handleWaitingForYearOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        int messageId;

        switch (text){
            case "year_from":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("back", "back"));
                buttons.add(row);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter year_from", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_FROM);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            case "year_to":
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("back", "back"));
                buttons.add(row1);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter year_to", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_TO);
                chats.get(chatId).setLastMessageId(messageId);
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
                messageId = notificationService.notifyWithInlineButtons(
                        chatId, "current filters:\nchoose filter to set", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
                chats.get(chatId).setLastMessageId(messageId);
                break;
        }
    }

    private void handleWaitingForYearFromCase(long chatId, String text){
        var chat = chats.get(chatId);
        boolean valid;
        int year;
        int messageId;

        try {
            year = Integer.parseInt(text);
            if (year >= 1900 && chat.getFilters().getYearTo() >= year){
                valid = true;
                chat.getFilters().setYearFrom(year);
            } else {
                valid = false;
            }
        } catch (Exception e){
            valid = false;
        }

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("year from", "year_from"));
        row.add(new ChatInlineButton("year to", "year_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            messageId = notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose year options", buttons);
        } else {
            messageId = notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose year options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_OPTION);
        chats.get(chatId).setLastMessageId(messageId);
    }

    private void handleWaitingForYearToCase(long chatId, String text){
        var chat = chats.get(chatId);
        boolean valid = false;
        int messageId;
        int year;

        try {
            year = Integer.parseInt(text);
            if (year <= LocalDateTime.now().getYear() && chat.getFilters().getYearFrom() <= year){
                valid = true;
                chat.getFilters().setYearTo(year);
            }
        } catch (Exception e){}

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("year from", "year_from"));
        row.add(new ChatInlineButton("year to", "year_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            messageId = notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose year options", buttons);
        } else {
            messageId = notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose year options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_YEAR_OPTION);
        chats.get(chatId).setLastMessageId(messageId);
    }

    private void handleWaitingForPriceOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        int messageId;

        switch (text){
            case "price_from":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("back", "back"));
                buttons.add(row);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter price_from", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_FROM);
                chats.get(chatId).setLastMessageId(messageId);
                break;
            case "price_to":
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("back", "back"));
                buttons.add(row1);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter price_to", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_TO);
                chats.get(chatId).setLastMessageId(messageId);
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
                messageId = notificationService.notifyWithInlineButtons(
                        chatId, "current filters:\nchoose filter to set", buttons);
                chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
                chats.get(chatId).setLastMessageId(messageId);
                break;
        }
    }

    private void handleWaitingForPriceFromCase(long chatId, String text){
        var chat = chats.get(chatId);
        boolean valid = false;
        int messageId;
        int price;

        try {
            price = Integer.parseInt(text);
            if (price >= 0 && chat.getFilters().getPriceTo() >= price){
                valid = true;
                chat.getFilters().setPriceFrom(price);
            }
        } catch (Exception e) {}

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("price from", "price_from"));
        row.add(new ChatInlineButton("price to", "price_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            messageId = notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose price options", buttons);
        } else {
            messageId = notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose price options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_OPTION);
        chats.get(chatId).setLastMessageId(messageId);
    }

    private void handleWaitingForPriceToCase(long chatId, String text){
        var chat = chats.get(chatId);
        boolean valid = false;
        int messageId;
        int price;

        try {
            price = Integer.parseInt(text);
            if (price >= 0 && chat.getFilters().getPriceFrom() <= price){
                valid = true;
                chat.getFilters().setPriceFrom(price);
            }
        } catch (Exception e) {}

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("price from", "price_from"));
        row.add(new ChatInlineButton("price to", "price_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row);
        buttons.add(row1);

        if (valid){
            messageId = notificationService.notifyWithInlineButtons(chatId, "set successfully\nchose price options", buttons);
        } else {
            messageId = notificationService.notifyWithInlineButtons(chatId, "invalid value\nchose price options", buttons);
        }

        chats.get(chatId).setState(ChatState.WAITING_FOR_PRICE_OPTION);
        chats.get(chatId).setLastMessageId(messageId);
    }

    private void handleWaitingForRegionCase(long chatId, String text){
        var chat = chats.get(chatId);
        int messageId;

        if (text.equals("submit")) {
            List<Queue<ChatInlineButton>> buttons1 = new ArrayList<>();
            Queue<ChatInlineButton> row1 = new LinkedList<>();
            row1.add(new ChatInlineButton("Region", "region"));
            row1.add(new ChatInlineButton("City", "city"));
            Queue<ChatInlineButton> row2 = new LinkedList<>();
            row2.add(new ChatInlineButton("Price", "price"));
            row2.add(new ChatInlineButton("Year", "year"));
            buttons1.add(row1);
            buttons1.add(row2);

            messageId = notificationService.notifyWithInlineButtons(
                    chatId, "current filters:\nchoose filter to set", buttons1);
            chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
            chats.get(chatId).setLastMessageId(messageId);
            return;
        }

        if (regions.contains(text)){
            chat.getFilters().toggleRegion(text);
            List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
            for (String region : regions){
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton(
                        (chat.getFilters().getRegions().contains(region) ? "🟢 " : "⭕️ ") + region,
                        region));
                buttons.add(row);
            }
            Queue<ChatInlineButton> row2 = new LinkedList<>();
            row2.add(new ChatInlineButton("submit", "submit"));
            buttons.add(row2);
            notificationService.editWithInlineButtons(chatId, chat.getLastMessageId(), "chose regions", buttons);
        }
    }

    private void handleWaitingForCityCase(long chatId, String text){
        var chat = chats.get(chatId);
        int messageId;

        if (text.equals("submit")) {
            List<Queue<ChatInlineButton>> buttons1 = new ArrayList<>();
            Queue<ChatInlineButton> row1 = new LinkedList<>();
            row1.add(new ChatInlineButton("Region", "region"));
            row1.add(new ChatInlineButton("City", "city"));
            Queue<ChatInlineButton> row2 = new LinkedList<>();
            row2.add(new ChatInlineButton("Price", "price"));
            row2.add(new ChatInlineButton("Year", "year"));
            buttons1.add(row1);
            buttons1.add(row2);

            messageId = notificationService.notifyWithInlineButtons(
                    chatId, "current filters:\nchoose filter to set", buttons1);
            chats.get(chatId).setState(ChatState.WAITING_FOR_FILTER_OPTION);
            chats.get(chatId).setLastMessageId(messageId);
            return;
        }

        if (cities.contains(text)){
            chat.getFilters().toggleCity(text);
            List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
            for (String city : cities){
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton(
                        (chat.getFilters().getCities().contains(city) ? "🟢 " : "⭕️ ") + city,
                        city));
                buttons.add(row);
            }
            Queue<ChatInlineButton> row2 = new LinkedList<>();
            row2.add(new ChatInlineButton("submit", "submit"));
            buttons.add(row2);
            notificationService.editWithInlineButtons(chatId, chat.getLastMessageId(), "chose cities", buttons);
        }
    }
}
