package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.nickname = :nickname")
    List<User> findByNicknameJPQL(@Param("nickname") String nickname);

    @Query(value = "SELECT * FROM users WHERE nickname = :nickname", nativeQuery = true)
    List<User> findByNicknameNative(@Param("nickname") String nickname);
}
