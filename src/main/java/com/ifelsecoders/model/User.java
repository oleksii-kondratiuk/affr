package com.ifelsecoders.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"commentsCount"})
public class User {
    final String userId;
    final String profileName;
    Long commentsCount = 1l;
}
