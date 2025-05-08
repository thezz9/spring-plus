package org.example.expert.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserResponse> findByNicknameQueryDSL(String nickname) {
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
