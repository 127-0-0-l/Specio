package io.github._127_0_0_l.infra_db.adapters.out;

import io.github._127_0_0_l.core.models.*;
import io.github._127_0_0_l.core.ports.out.db.TgChatPort;
import io.github._127_0_0_l.infra_db.interfaces.DBMapper;
import io.github._127_0_0_l.infra_db.repositories.TgChatRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TgChatAdapter implements TgChatPort {
    private final TgChatRepository tgChatRepository;
    private final DBMapper mapper;

    public TgChatAdapter(TgChatRepository thChatRepository,
        DBMapper mapper
    ){
        this.tgChatRepository = thChatRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Long> create(TgChat chat) {
        try{
            var entity = mapper.toDBTgChat(chat);
            var saved = tgChatRepository.save(entity);
            return Optional.of(saved.getId());
        } catch (IllegalArgumentException e){
            log.error("Failed to create tg chat: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public boolean update(TgChat chat) {
        try{
            if (!tgChatRepository.existsById(chat.id())){
                throw new IllegalArgumentException("tg chat with id=" + chat.id() + " doesn't exists");
            }
            var entity = mapper.toDBTgChat(chat);
            tgChatRepository.save(entity);
            return true;
        } catch (IllegalArgumentException e){
            log.error("Failed to update tg chat: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        if (tgChatRepository.existsById(id)){
            tgChatRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<TgChat> get(Long id) {
        var chat = tgChatRepository.findById(id);
        if (chat.isPresent()){
            var mapped = mapper.toCoreTgChat(chat.get());
            return Optional.of(mapped);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<TgChat> get(List<Long> ids) {
        var chats = tgChatRepository.findAllById(ids);
        if (chats.size() != 0){
            return mapper.toCoreTgChats(chats);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<TgChat> getAll() {
        var chats = tgChatRepository.findAll();
        if (chats.size() != 0){
            return mapper.toCoreTgChats(chats);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ChatState getState(Long chatId) {
        var state = tgChatRepository.findChatStateById(chatId);
        if (state.isPresent()){
            var mapped = mapper.toCoreTgChatState(state.get().getState());
            return mapped;
        } else {
            return ChatState.INACTIVE;
        }
    }

    @Override
    public List<TgChat> getByState(ChatState state) {
        var chats = tgChatRepository.findAllByState(state.toString());
        if (chats.size() != 0){
            return mapper.toCoreTgChats(chats);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean toggleChatActivation(Long id) {
        var entity = tgChatRepository.findById(id);
        if (entity.isPresent()){
            entity.get().setState(
                entity.get().getState() == ChatState.INACTIVE.toString()
                ? ChatState.IDLE.toString()
                : ChatState.INACTIVE.toString());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateFilters(Long chatId, Filters filters) {
        var entity = tgChatRepository.findById(chatId);
        if (entity.isPresent()){
            entity.get().setFilters(mapper.toDBFilters(filters));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean exists(Long id) {
        return tgChatRepository.existsById(id);
    }
}
