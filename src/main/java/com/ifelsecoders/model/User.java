package com.ifelsecoders.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"commentsCount"})
public class User {
    final String userId;
    final String profileName;
    AtomicLong commentsCount = new AtomicLong(0l);
}
