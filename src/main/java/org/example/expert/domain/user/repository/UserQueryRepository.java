package org.example.expert.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<UserResponse> findByNickname(String nickname) {
        QUser user = QUser.user;

        return queryFactory
                .select(Projections.constructor(UserResponse.class,
                        user.id,
                        user.email))
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetch();
    }
}
