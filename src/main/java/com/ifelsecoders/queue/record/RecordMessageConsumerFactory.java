package com.ifelsecoders.queue.record;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import com.ifelsecoders.queue.ConsumerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RecordMessageConsumerFactory  implements ConsumerFactory<RecordMessageConsumer, RecordBroker> {

    @Autowired
    private RecordBroker recordBroker;

    ParsingResult parsingResult;
    CountDownLatch countDownLatch;

    @Autowired
    private RecordProcessor recordProcessor;

    @PostConstruct
    public void init() {
        parsingResult = new ParsingResult();

        Map<String, User> activeUsers = new ConcurrentHashMap<>();
        parsingResult.setActiveUsers(activeUsers);

        Map<String, AtomicLong> productToReviewCountMap = new ConcurrentHashMap<>();
        parsingResult.setProductToReviewCountMap(productToReviewCountMap);

        Map<String, AtomicLong> usedWordsCountMap = new ConcurrentHashMap<>();
        parsingResult.setUsedWordsCountMap(usedWordsCountMap);

        countDownLatch = new CountDownLatch(4);
    }

    @Override
    public RecordMessageConsumer getConsumerInstance(RecordBroker broker) {
        return new RecordMessageConsumer(recordBroker, countDownLatch, parsingResult, recordProcessor);
    }

    public ParsingResult getParsingResult() {
        return parsingResult;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
}
