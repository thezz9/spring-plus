package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodoSearchRequest {

    private final String title;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    private final String nickname;
}
