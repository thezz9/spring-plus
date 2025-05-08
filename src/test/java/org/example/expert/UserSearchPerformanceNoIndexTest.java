package org.example.expert;

import org.example.expert.config.QuerydslConfig;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("검색 성능 테스트 (no index)")
public class UserSearchPerformanceNoIndexTest {

    @Autowired
    private UserRepository userRepository;

    private final String sampleNickname = "9592f583-f189-42cc-905b-e30223476732";

    private long averageExecutionTime(Runnable task) {
        long total = 0;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            total += (end - start);
        }
        return total / 10 / 1000000;
    }

    @Test
    @DisplayName("JPA 성능 테스트")
    @Transactional(readOnly = true)
    public void testJpaMethodSearch() {
        long avg = averageExecutionTime(() -> userRepository.findByNickname(sampleNickname));
        System.out.println("[JPA 평균 검색 시간] " + avg + " ms");
    }

    @Test
    @DisplayName("JPQL 성능 테스트")
    @Transactional(readOnly = true)
    public void testJpaJPQLSearch() {
        long avg = averageExecutionTime(() -> userRepository.findByNicknameJPQL(sampleNickname));
        System.out.println("[JPQL 평균 검색 시간] " + avg + " ms");
    }

    @Test
    @DisplayName("QueryDSL 성능 테스트")
    @Transactional(readOnly = true)
    public void testQueryDslSearch() {
        long avg = averageExecutionTime(() -> userRepository.findByNickname(sampleNickname));
        System.out.println("[QueryDSL 평균 검색 시간] " + avg + " ms");
    }

    @Test
    @DisplayName("Native SQL 성능 테스트")
    @Transactional(readOnly = true)
    public void testNativeQuerySearch() {
        long avg = averageExecutionTime( () -> userRepository.findByNicknameNative(sampleNickname));
        System.out.println("[Native SQL 평균 검색 시간] " + avg + " ms");
    }

}
