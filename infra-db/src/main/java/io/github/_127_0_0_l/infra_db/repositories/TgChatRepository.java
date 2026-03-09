package io.github._127_0_0_l.infra_db.repositories;

import io.github._127_0_0_l.infra_db.entities.TgChat;
import io.github._127_0_0_l.infra_db.repositories.interfaces.TgChatState;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgChatRepository extends JpaRepository<TgChat, Long> {
    Optional<TgChatState> findChatStateById(Long chatId);

    List<TgChat> findAllByState(String state);
}
