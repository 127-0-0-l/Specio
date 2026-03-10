package io.github._127_0_0_l.infra_tg_bot.services;

import io.github._127_0_0_l.core.ports.out.db.TgChatPort;
import io.github._127_0_0_l.infra_tg_bot.interfaces.TgBotMapper;
import io.github._127_0_0_l.infra_tg_bot.models.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UpdateHandler implements LongPollingUpdateConsumer {
    private final NotificationService notificationService;
    private final List<String> regions = List.of("Minsk region", "Brest region", "Grodno region", "Vitebsk region",
            "Gomel region", "Mogilev region");
    private final List<String> cities = List.of("Minsk", "Brest", "Bobruisk", "Visokaye", "Kamenets", "Baranovichi");
    private final TgChatPort tgChatPort;
    private final TgBotMapper mapper;

    public UpdateHandler(NotificationService notificationService,
        TgChatPort tgChatPort,
        TgBotMapper mapper
    ){
        this.notificationService = notificationService;
        this.tgChatPort = tgChatPort;
        this.mapper = mapper;
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            long chatId;
            String text;

            if (update.hasMessage() && update.getMessage().hasText()) {
                chatId = update.getMessage().getChatId();
                text = update.getMessage().getText();
            } else if (update.hasCallbackQuery()){
                chatId = update.getCallbackQuery().getMessage().getChatId();
                text = update.getCallbackQuery().getData();
            } else {
                continue;
            }

            handleMessage(chatId, text);
        }
    }

    private void handleMessage(long chatId, String text){
        if (!tgChatPort.exists(chatId)){
            var newChat = new Chat(chatId, ChatState.INIT, new Filters());
            tgChatPort.create(mapper.toCoreTgChat(newChat));
        }
        ChatState state = mapper.toBotChatState(tgChatPort.getState(chatId));

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

                if (mapper.toBotChatState(tgChatPort.getState(chatId)) == ChatState.INIT) {
                    messageId = notificationService.notifyWithKeyboardButtons(chatId, "welcome", buttons);
                } else {
                    messageId = notificationService.notifyWithKeyboardButtons(
                            chatId,
                            "welcome back\n your last settings are:\n" + mapper.toBotChat(tgChatPort.get(chatId)).getFilters().toString(),
                            buttons);
                }
                Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
                chat.setState(ChatState.IDLE);
                chat.setLastMessageId(messageId);
                tgChatPort.update(mapper.toCoreTgChat(chat));
                break;
            default:
                break;
        }
    }

    private void handleIdleCase(long chatId, String text){
        int messageId;
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
        switch (text) {
            case "run":
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("stop"));
                buttons.add(row);
                messageId = notificationService.notifyWithKeyboardButtons(chatId, "start notifying", buttons);
                chat.setState(ChatState.NOTIFYING);
                chat.setLastMessageId(messageId);
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
                chat.setState(ChatState.WAITING_FOR_FILTER_OPTION);
                chat.setLastMessageId(messageId);
                break;
            case "/stop":
                List<Queue<ChatKeyboardButton>> buttons2 = new ArrayList<>();
                Queue<ChatKeyboardButton> row3 = new LinkedList<>();
                row3.add(new ChatKeyboardButton("/start"));
                buttons2.add(row3);
                messageId = notificationService.notifyWithKeyboardButtons(chatId, "stopped", buttons2);
                chat.setState(ChatState.INACTIVE);
                chat.setLastMessageId(messageId);
                break;
            default:
                break;
        }

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleNotifyingCase(long chatId, String text){
        switch (text) {
            case "stop":
                List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
                Queue<ChatKeyboardButton> row = new LinkedList<>();
                row.add(new ChatKeyboardButton("run"));
                row.add(new ChatKeyboardButton("set filters"));
                row.add(new ChatKeyboardButton("/stop"));
                buttons.add(row);
                int mId = notificationService.notifyWithKeyboardButtons(chatId, "stop nitifying", buttons);
                Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
                chat.setState(ChatState.IDLE);
                chat.setLastMessageId(mId);
                tgChatPort.update(mapper.toCoreTgChat(chat));
                break;
            default:
                break;
        }
    }

    private void handleWaitingForFilterOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        int messageId;
        var chat = mapper.toBotChat(tgChatPort.get(chatId));

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
                chat.setState(ChatState.WAITING_FOR_REGION);
                chat.setLastMessageId(messageId);
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
                chat.setState(ChatState.WAITING_FOR_CITY);
                chat.setLastMessageId(messageId);
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
                chat.setState(ChatState.WAITING_FOR_PRICE_OPTION);
                chat.setLastMessageId(messageId);
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
                chat.setState(ChatState.WAITING_FOR_YEAR_OPTION);
                chat.setLastMessageId(messageId);
                break;
            case "back":
                List<Queue<ChatKeyboardButton>> buttons1 = new ArrayList<>();
                Queue<ChatKeyboardButton> row2 = new LinkedList<>();
                row2.add(new ChatKeyboardButton("run"));
                row2.add(new ChatKeyboardButton("set filters"));
                buttons1.add(row2);
                messageId = notificationService.notifyWithKeyboardButtons(chatId, "", buttons1);
                chat.setState(ChatState.IDLE);
                chat.setLastMessageId(messageId);
                break;
            default:
                break;
        }

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForYearOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        int messageId;
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));

        switch (text){
            case "year_from":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("back", "back"));
                buttons.add(row);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter year_from", buttons);
                chat.setState(ChatState.WAITING_FOR_YEAR_FROM);
                chat.setLastMessageId(messageId);
                break;
            case "year_to":
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("back", "back"));
                buttons.add(row1);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter year_to", buttons);
                chat.setState(ChatState.WAITING_FOR_YEAR_TO);
                chat.setLastMessageId(messageId);
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
                chat.setState(ChatState.WAITING_FOR_FILTER_OPTION);
                chat.setLastMessageId(messageId);
                break;
        }

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForYearFromCase(long chatId, String text){
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
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

        chat.setState(ChatState.WAITING_FOR_YEAR_OPTION);
        chat.setLastMessageId(messageId);

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForYearToCase(long chatId, String text){
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
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

        chat.setState(ChatState.WAITING_FOR_YEAR_OPTION);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForPriceOptionCase(long chatId, String text){
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        int messageId;
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));

        switch (text){
            case "price_from":
                Queue<ChatInlineButton> row = new LinkedList<>();
                row.add(new ChatInlineButton("back", "back"));
                buttons.add(row);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter price_from", buttons);
                chat.setState(ChatState.WAITING_FOR_PRICE_FROM);
                chat.setLastMessageId(messageId);
                break;
            case "price_to":
                Queue<ChatInlineButton> row1 = new LinkedList<>();
                row1.add(new ChatInlineButton("back", "back"));
                buttons.add(row1);
                messageId = notificationService.notifyWithInlineButtons(chatId, "enter price_to", buttons);
                chat.setState(ChatState.WAITING_FOR_PRICE_TO);
                chat.setLastMessageId(messageId);
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
                chat.setState(ChatState.WAITING_FOR_FILTER_OPTION);
                chat.setLastMessageId(messageId);
                break;
        }

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForPriceFromCase(long chatId, String text){
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
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

        chat.setState(ChatState.WAITING_FOR_PRICE_OPTION);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForPriceToCase(long chatId, String text){
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
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

        chat.setState(ChatState.WAITING_FOR_PRICE_OPTION);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForRegionCase(long chatId, String text){
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
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
            chat.setState(ChatState.WAITING_FOR_FILTER_OPTION);
            chat.setLastMessageId(messageId);
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

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void handleWaitingForCityCase(long chatId, String text){
        Chat chat = mapper.toBotChat(tgChatPort.get(chatId));
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
            chat.setState(ChatState.WAITING_FOR_FILTER_OPTION);
            chat.setLastMessageId(messageId);
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

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }
}
