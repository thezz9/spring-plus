package org.example.expert;

import jakarta.persistence.EntityManager;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class UserDataInitializerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    @Rollback(false)
    public void testGenerateUsers() {

        int batchSize = 20000;

        for (int i = 0; i < 1000000; i++) {
            String nickname = UUID.randomUUID().toString();
            User user = new User(nickname + "@example.com", nickname);
            userRepository.save(user);

            if (i % batchSize == 0) {
                userRepository.flush();
                em.clear();
                System.out.println("저장: " + i + "개");
            }
        }

        System.out.println("100만 건 데이터 생성 완료");
    }
}
