package com.ifelsecoders.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    final String userId;
    final String profileName;
    long commentsCount = 1;
}
