package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageJpaRepository extends JpaRepository<Message, Long> {

    @Query(value = "select * from messages m where (m.recipient_id = :recipient and m.sender_id = :sender) " +
            "or (m.sender_id = :recipient and m.recipient_id = :sender) order by m.date", nativeQuery = true)
    List<Message> getTalk(Long sender, Long recipient);
}
