package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
public class TodoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TodoQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;

        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Page<TodoSearchResponse> findTodosByDynamicCondition(TodoSearchRequest request, Pageable pageable) {
        QTodo todo = QTodo.todo;

        QManager manager = QManager.manager;
        QComment comment = QComment.comment;

        JPQLQuery<TodoSearchResponse> query = queryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title,
                        JPAExpressions.select(manager.count())
                                .from(manager)
                                .where(manager.todo.id.eq(todo.id)),
                        JPAExpressions.select(comment.count())
                                .from(comment)
                                .where(comment.todo.id.eq(todo.id))
                ))
                .from(todo)
                .where(
                        titleContains(request.getTitle()),
                        createdAtBetween(request.getStartDateTime(), request.getEndDateTime()),
                        nicknameContains(request.getNickname())
                )
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? todo.title.contains(title) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        if (startDateTime != null && endDateTime != null) {
            return todo.createdAt.between(startDateTime, endDateTime);
        } else if (startDateTime != null) {
            return todo.createdAt.goe(startDateTime);
        } else if (endDateTime != null) {
            return todo.createdAt.loe(endDateTime);
        }
        return null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return StringUtils.hasText(nickname) ? todo.managers.any().user.nickname.contains(nickname) : null;
    }
}
