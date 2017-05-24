package com.ifelsecoders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsingResult {
    Map<String, User> activeUsers;
    Map<String, AtomicLong> productToReviewCountMap;
    Map<String, AtomicLong> usedWordsCountMap;
}
