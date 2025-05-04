package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("""
        SELECT t FROM Todo t
        LEFT JOIN t.user
        WHERE (:weather IS NULL OR t.weather = :weather)
        AND (
            (:startDateTime IS NULL AND :endDateTime IS NULL) OR
            (:startDateTime IS NULL AND t.modifiedAt <= :endDateTime) OR
            (:endDateTime IS NULL AND t.modifiedAt >= :startDateTime) OR
            (t.modifiedAt BETWEEN :startDateTime AND :endDateTime)
        )
        ORDER BY t.modifiedAt DESC
    """)
    Page<Todo> findAllByDynamicCondition(
            Pageable pageable,
            @Param("weather") String weather,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
