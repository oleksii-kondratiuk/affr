package com.ifelsecoders.queue.record;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DefaultRecordProcessor implements RecordProcessor {
    @Override
    public void processRecord(CsvRecordMessage message, ParsingResult parsingResult) {
        mapRecordToUser(message.getUserId(), message.getProfileName(), parsingResult.getActiveUsers());
        increaseReviewCountForProduct(message.getProductId(), parsingResult.getProductToReviewCountMap());
        increaseWordCountForProduct(message.getComment(), parsingResult.getUsedWordsCountMap());
    }

    void mapRecordToUser(String userId, String profileName, Map<String, User> users) {
        User user = new User(userId, profileName);
        users.putIfAbsent(userId, user);
        users.get(userId).getCommentsCount().incrementAndGet();
    }

    void increaseReviewCountForProduct(String productId, Map<String, AtomicLong> productToReviewCountMap) {
        productToReviewCountMap.putIfAbsent(productId, new AtomicLong(0l));
        productToReviewCountMap.get(productId).incrementAndGet();
    }

    void increaseWordCountForProduct(String comment, Map<String, AtomicLong> usedWordsCountMap) {
        Arrays.stream(comment.split("\\P{L}+")).forEach(word -> {
            if(word != null) {
                usedWordsCountMap.putIfAbsent(word, new AtomicLong(0l));
                usedWordsCountMap.get(word).incrementAndGet();
            }
        });
    }
}
