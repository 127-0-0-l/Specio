package io.github._127_0_0_l.infra_db.repositories;

import io.github._127_0_0_l.infra_db.entities.TgChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgChatRepository extends JpaRepository<TgChat, Long> {

}
