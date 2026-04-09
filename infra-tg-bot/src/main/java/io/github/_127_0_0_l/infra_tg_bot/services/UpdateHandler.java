package io.github._127_0_0_l.infra_tg_bot.services;

import io.github._127_0_0_l.core.constants.ValidationConstants;
import io.github._127_0_0_l.core.ports.out.db.CitiesPort;
import io.github._127_0_0_l.core.ports.out.db.RegionsPort;
import io.github._127_0_0_l.core.ports.out.db.TgChatPort;
import io.github._127_0_0_l.infra_tg_bot.interfaces.TgBotMapper;
import io.github._127_0_0_l.infra_tg_bot.models.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UpdateHandler implements LongPollingUpdateConsumer {
    private final NotificationService notificationService;
    private final List<Region> regions;
    private final List<City> cities;
    private final TgChatPort tgChatPort;
    private final TgBotMapper mapper;
    private final int PAGINATION_SIZE = 5;
    private final int PAGE_SIZE = 10;

    public UpdateHandler(NotificationService notificationService,
        TgChatPort tgChatPort,
        TgBotMapper mapper,
        RegionsPort regionsPort,
        CitiesPort citiesPort
    ){
        this.notificationService = notificationService;
        this.tgChatPort = tgChatPort;
        this.mapper = mapper;

        regions = mapper.toBotRegions(regionsPort.getAll());
        cities = mapper.toBotCities(citiesPort.getAll());
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

            try {
                handleMessage(chatId, text);
            } catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
    }

    private void handleMessage(long chatId, String text){
        if (!tgChatPort.exists(chatId)){
            var newChat = new Chat(chatId, ChatState.INIT);
            var mapped = mapper.toCoreTgChat(newChat);
            tgChatPort.create(mapped);
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
        switch (text){
            case "/start":
                if (mapper.toBotChatState(tgChatPort.getState(chatId)) == ChatState.INIT) {
                    goToMainMenu(chatId, "welcome");
                } else {
                    goToMainMenu(chatId, "welcome back\n your last settings are:\n"
                        + mapper.toBotChat(tgChatPort.get(chatId).orElseThrow()).getFilters().toString());
                }
                break;
            default:
                break;
        }
    }

    private void handleIdleCase(long chatId, String text){
        switch (text) {
            case "run":
                goToNotifying(chatId, "start notifying");
                break;
            case "set filters":
                goToFilterMainMenu(chatId, "current filters:\n"
                    + mapper.toBotChat(tgChatPort.get(chatId).orElseThrow()).getFilters().toString()
                    + "\nchoose filter to set");
                break;
            case "/stop":
                goToInactive(chatId, "stopped");
                break;
            default:
                break;
        }
    }

    private void handleNotifyingCase(long chatId, String text){
        switch (text) {
            case "stop":
                goToMainMenu(chatId, "stop notifying");
                break;
            default:
                break;
        }
    }

    private void handleWaitingForFilterOptionCase(long chatId, String text){
        switch (text) {
            case "region":
                goToRegions(chatId, "chose regions");
                break;
            case "city":
                goToCities(chatId, "chose cities");
                break;
            case "price":
                goToPriceOptions(chatId, "chose price option");
                break;
            case "year":
                goToYearOptions(chatId, "chose year options");
                break;
            case "to main menu":
                goToMainMenu(chatId, "filters setted");
                break;
            default:
                break;
        }
    }

    private void handleWaitingForYearOptionCase(long chatId, String text){
        switch (text){
            case "year_from":
                goToYearFromEntering(chatId, "enter year_from");
                break;
            case "year_to":
                goToYearToEntering(chatId, "enter year_to");
                break;
            case "submit":
                goToFilterMainMenu(chatId, "current filters:\n"
                    + mapper.toBotChat(tgChatPort.get(chatId).orElseThrow()).getFilters().toString()
                    + "\nchoose filter to set");
                break;
        }
    }

    private void handleWaitingForYearFromCase(long chatId, String text){
        boolean isValid = false;

        try {
            int year = Integer.parseInt(text);
            Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
            if (year >= ValidationConstants.MIN_YEAR_VALUE && chat.getFilters().getYearTo() >= year){
                isValid = true;
                chat.getFilters().setYearFrom(year);
                tgChatPort.update(mapper.toCoreTgChat(chat));
            }
        } catch (Exception e) { }

        if (isValid){
            goToYearOptions(chatId, "set successfully\nchose year options");
        } else {
            goToYearOptions(chatId, "invalid value\nchose year options");
        }
    }

    private void handleWaitingForYearToCase(long chatId, String text){
        boolean isValid = false;

        try {
            int year = Integer.parseInt(text);
            Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
            if (year <= LocalDateTime.now().getYear() && chat.getFilters().getYearFrom() <= year){
                isValid = true;
                chat.getFilters().setYearTo(year);
                tgChatPort.update(mapper.toCoreTgChat(chat));
            }
        } catch (Exception e){}

        if (isValid){
            goToYearOptions(chatId, "set successfully\nchose year options");
        } else {
            goToYearOptions(chatId, "invalid value\nchose year options");
        }
    }

    private void handleWaitingForPriceOptionCase(long chatId, String text){
        switch (text){
            case "price_from":
                goToPriceFromEntering(chatId, "enter price_from");
                break;
            case "price_to":
                goToPriceToEntering(chatId, "enter price_to");
                break;
            case "submit":
                goToFilterMainMenu(chatId, "current filters:\n"
                    + mapper.toBotChat(tgChatPort.get(chatId).orElseThrow()).getFilters().toString()
                    + "\nchoose filter to set");
                break;
        }
    }

    private void handleWaitingForPriceFromCase(long chatId, String text){
        boolean isValid = false;

        try {
            int price = Integer.parseInt(text);
            Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
            if (price >= 0 && chat.getFilters().getPriceTo() >= price){
                isValid = true;
                chat.getFilters().setPriceFrom(price);
            }
            tgChatPort.update(mapper.toCoreTgChat(chat));
        } catch (Exception e) {}

        if (isValid){
            goToPriceOptions(chatId, "set successfully\nchose price options");
        } else {
            goToPriceOptions(chatId, "invalid value\nchose price options");
        }
    }

    private void handleWaitingForPriceToCase(long chatId, String text){
        boolean isValid = false;

        try {
            int price = Integer.parseInt(text);
            Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
            if (price >= 0 && chat.getFilters().getPriceFrom() <= price){
                isValid = true;
                chat.getFilters().setPriceTo(price);
            }
            tgChatPort.update(mapper.toCoreTgChat(chat));
        } catch (Exception e) {}

        if (isValid){
            goToPriceOptions(chatId, "set successfully\nchose price options");
        } else {
            goToPriceOptions(chatId, "invalid value\nchose price options");
        }
    }

    private void handleWaitingForRegionCase(long chatId, String text){
        if (text.equals("submit")) {
            goToFilterMainMenu(chatId, "current filters:\n"
                + mapper.toBotChat(tgChatPort.get(chatId).orElseThrow()).getFilters().toString()
                + "\nchoose filter to set");
            return;
        }

        goToRegions(chatId, text, "chose regions", true);
    }

    private void handleWaitingForCityCase(long chatId, String text){
        if (text.equals("submit")) {
            goToFilterMainMenu(chatId, "current filters:\n"
                + mapper.toBotChat(tgChatPort.get(chatId).orElseThrow()).getFilters().toString()
                + "\nchoose filter to set");
            return;
        }

        goToCities(chatId, text, "chose cities", true);
    }

    private void goToMainMenu(long chatId, String message){
        Queue<ChatKeyboardButton> row = new LinkedList<>();
        row.add(new ChatKeyboardButton("run"));
        row.add(new ChatKeyboardButton("set filters"));
        row.add(new ChatKeyboardButton("/stop"));

        List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row);

        int messageId = notificationService.notifyWithKeyboardButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.IDLE);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToFilterMainMenu (long chatId, String message) {
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("Region", "region"));
        row1.add(new ChatInlineButton("City", "city"));
        Queue<ChatInlineButton> row2 = new LinkedList<>();
        row2.add(new ChatInlineButton("Price", "price"));
        row2.add(new ChatInlineButton("Year", "year"));
        Queue<ChatInlineButton> row3 = new LinkedList<>();
        row3.add(new ChatInlineButton("to main menu", "to main menu"));

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        buttons.add(row1);
        buttons.add(row2);
        buttons.add(row3);

        int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.WAITING_FOR_FILTER_OPTION);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToNotifying (long chatId, String message) {
        Queue<ChatKeyboardButton> row = new LinkedList<>();
        row.add(new ChatKeyboardButton("stop"));

        List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row);

        int messageId = notificationService.notifyWithKeyboardButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.NOTIFYING);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToInactive (long chatId, String message) {
        Queue<ChatKeyboardButton> row = new LinkedList<>();
        row.add(new ChatKeyboardButton("/start"));

        List<Queue<ChatKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(row);

        int messageId = notificationService.notifyWithKeyboardButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.INACTIVE);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToRegions (long chatId, String message){
        goToRegions(chatId, "", message, false);
    }
    
    private void goToRegions (long chatId, String text, String message, boolean isUpdate) {
        var chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();

        if (isUpdate){
            Optional<Region> region = regions.stream().filter(r -> r.name().equals(text)).findFirst();
            if (region.isPresent()){
                chat.getFilters().toggleRegion(region.get());
            } else {
                return;
            }
        }

        for (Region region : regions){
            Queue<ChatInlineButton> row = new LinkedList<>();
            row.add(new ChatInlineButton(
                    (chat.getFilters().getRegions().contains(region) ? "🟢 " : "⭕️ ") + region.name(),
                    region.name()));
            buttons.add(row);
        }
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));
        buttons.add(row1);
        
        if (isUpdate){
            notificationService.editWithInlineButtons(chatId, chat.getLastMessageId(), message, buttons);
        } else {
            int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);
            chat.setLastMessageId(messageId);
            chat.setState(ChatState.WAITING_FOR_REGION);
        }

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToCities (long chatId, String message){
        goToCities(chatId, "", message, false);
    }

    private void goToCities (long chatId, String text, String message, boolean isUpdate) {
        var chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        List<City> currentCities = chat.getFilters().getRegions().stream()
            .flatMap(r -> r.cities().stream())
            .toList();

        boolean isDigit = text.matches("\\d+");

        if (isUpdate && !isDigit){
            Optional<City> city = currentCities.stream().filter(c -> c.name().equals(text)).findFirst();
            if (city.isPresent()){
                chat.getFilters().toggleCity(city.get());
                tgChatPort.update(mapper.toCoreTgChat(chat));
                chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
            } else {
                return;
            }
        }

        Queue<ChatInlineButton> pagination;
        int totalPages = currentCities.size() / PAGE_SIZE + ((currentCities.size() % PAGE_SIZE) > 0 ? 1 : 0);
        int currentPage;
        if (isUpdate){
            if (isDigit){
                currentPage = Integer.parseInt(text);
            } else {
                int currentItem = currentCities.indexOf(currentCities.stream().filter(c -> c.name().equals(text)).findFirst().get());
                currentPage = currentItem / PAGE_SIZE + ((currentItem % PAGE_SIZE) > 0 ? 1 : 0);
            }
        } else {
            currentPage = 1;
        }
        pagination = getPagination(totalPages, currentPage);

        for (City city : currentCities.stream().skip((currentPage - 1) * PAGE_SIZE).limit(PAGE_SIZE).toList()){
            Queue<ChatInlineButton> row = new LinkedList<>();
            row.add(new ChatInlineButton(
                    (chat.getFilters().getCities().contains(city) ? "🟢 " : "⭕️ ") + city.name(),
                    city.name()));
            buttons.add(row);
        }
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("submit", "submit"));

        buttons.add(pagination);
        buttons.add(row);

        if (isUpdate){
            notificationService.editWithInlineButtons(chatId, chat.getLastMessageId(), message, buttons);
        } else {
            int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);
            chat.setLastMessageId(messageId);
            chat.setState(ChatState.WAITING_FOR_CITY);
        }

        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private Queue<ChatInlineButton> getPagination(int totalPages, int page){
        Queue<ChatInlineButton> pagination = new LinkedList<>();
        int firstPage = totalPages <= PAGINATION_SIZE ? 1 : page - (PAGINATION_SIZE / 2 + Math.abs(PAGINATION_SIZE / 2 - (totalPages - page)));
        int lastPage = totalPages <= PAGINATION_SIZE ? totalPages : firstPage + PAGINATION_SIZE - 1;

        for (int i = firstPage; i <= lastPage; i++){
            if (i == page){
                pagination.add(new ChatInlineButton("[" + i + "]", String.valueOf(i)));
                continue;
            }

            if (i == firstPage && firstPage != 1){
                pagination.add(new ChatInlineButton("..." + i, String.valueOf(i)));
                continue;
            }

            if (i == lastPage && lastPage != totalPages){
                pagination.add(new ChatInlineButton(i + "...", String.valueOf(i)));
                continue;
            }

            pagination.add(new ChatInlineButton(String.valueOf(i), String.valueOf(i)));
        }

        return pagination;
    }

    private void goToPriceOptions (long chatId, String message) {
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("price from", "price_from"));
        row.add(new ChatInlineButton("price to", "price_to"));
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("submit", "submit"));

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        buttons.add(row);
        buttons.add(row1);

        int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);

        var chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.WAITING_FOR_PRICE_OPTION);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToYearOptions (long chatId, String message) {
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("year from", "year_from"));
        row1.add(new ChatInlineButton("year to", "year_to"));
        Queue<ChatInlineButton> row2 = new LinkedList<>();
        row2.add(new ChatInlineButton("submit", "submit"));

        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        buttons.add(row1);
        buttons.add(row2);

        int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);

        var chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.WAITING_FOR_YEAR_OPTION);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToYearFromEntering (long chatId, String message) {
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("back", "back"));
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        buttons.add(row);

        int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.WAITING_FOR_YEAR_FROM);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToYearToEntering (long chatId, String message) {
        Queue<ChatInlineButton> row1 = new LinkedList<>();
        row1.add(new ChatInlineButton("back", "back"));
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        buttons.add(row1);

        int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.WAITING_FOR_YEAR_TO);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToPriceFromEntering (long chatId, String message) {
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("back", "back"));
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        buttons.add(row);

        int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.WAITING_FOR_PRICE_FROM);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }

    private void goToPriceToEntering (long chatId, String message) {
        Queue<ChatInlineButton> row = new LinkedList<>();
        row.add(new ChatInlineButton("back", "back"));
        List<Queue<ChatInlineButton>> buttons = new ArrayList<>();
        buttons.add(row);

        int messageId = notificationService.notifyWithInlineButtons(chatId, message, buttons);

        Chat chat = mapper.toBotChat(tgChatPort.get(chatId).orElseThrow());
        chat.setState(ChatState.WAITING_FOR_PRICE_TO);
        chat.setLastMessageId(messageId);
        tgChatPort.update(mapper.toCoreTgChat(chat));
    }
}
