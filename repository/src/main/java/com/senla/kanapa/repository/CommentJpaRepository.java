package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    List<Comment> getByAdvertisement_Id(Long advertisementId);
}
