package com.ifelsecoders.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
public class ParsingResult {
    Map<String, User> activeUsers;
    Map<String, AtomicLong> productToReviewCountMap;
    Map<String, AtomicLong> usedWordsCountMap;
}
