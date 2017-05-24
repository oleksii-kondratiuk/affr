package com.ifelsecoders.queue.record;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

public class DefaultRecordProcessorTest {

    private static final String JOHN_USER_ID = "1";
    private static final String JOHN_PROFILE_NAME = "John";
    private static final String PAUL_USER_ID = "2";
    private static final String PAUL_PROFILE_NAME = "Paul";

    private DefaultRecordProcessor defaultRecordProcessor;

    @Before
    public void init() {
        defaultRecordProcessor = new DefaultRecordProcessor();
    }

    @Test
    public void testMapRecordToUser() throws Exception {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

        defaultRecordProcessor.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);
        assertEquals(1, users.size());
        assertEquals(JOHN_USER_ID, users.get(JOHN_USER_ID).getUserId());
        assertEquals(JOHN_PROFILE_NAME, users.get(JOHN_USER_ID).getProfileName());
        assertEquals(1l, users.get(JOHN_USER_ID).getCommentsCount().get());
    }

    @Test
    public void testMapRecordForTwoDistinctUsers() throws Exception {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

        defaultRecordProcessor.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);
        defaultRecordProcessor.mapRecordToUser(PAUL_USER_ID, PAUL_PROFILE_NAME, users);

        assertEquals(2, users.size());
        assertEquals(JOHN_USER_ID, users.get(JOHN_USER_ID).getUserId());
        assertEquals(JOHN_PROFILE_NAME, users.get(JOHN_USER_ID).getProfileName());
        assertEquals(1l, users.get(JOHN_USER_ID).getCommentsCount().get());

        assertEquals(PAUL_USER_ID, users.get(PAUL_USER_ID).getUserId());
        assertEquals(PAUL_PROFILE_NAME, users.get(PAUL_USER_ID).getProfileName());
        assertEquals(1l, users.get(PAUL_USER_ID).getCommentsCount().get());
    }

    @Test
    public void testTwoMapRecordsForOneUser() throws Exception {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

        defaultRecordProcessor.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);
        defaultRecordProcessor.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);

        assertEquals(1, users.size());
        assertEquals(JOHN_USER_ID, users.get(JOHN_USER_ID).getUserId());
        assertEquals(JOHN_PROFILE_NAME, users.get(JOHN_USER_ID).getProfileName());
        assertEquals(2l, users.get(JOHN_USER_ID).getCommentsCount().get());
    }

    @Test
    public void testSimple10RowsCsv() throws Exception {
        Map<String, User> activeUsers = new HashMap<>();
        Map<String, AtomicLong> productToReviewCountMap = new HashMap<>();
        Map<String, AtomicLong> usedWordsCountMap = new HashMap<>();
        ParsingResult parsingResult = new ParsingResult(activeUsers, productToReviewCountMap, usedWordsCountMap);

        List<CsvRecordMessage> csvRecordMessageList = new ArrayList();
        csvRecordMessageList.add(new CsvRecordMessage("1", "delmartian", "B000UA0QIQ", "Lucky"));
        csvRecordMessageList.add(new CsvRecordMessage("1", "delmartian", "B00813GRG4", "AC/DC Rules"));
        csvRecordMessageList.add(new CsvRecordMessage("1", "delmartian", "B000LQOCH0", "Cool"));
        csvRecordMessageList.add(new CsvRecordMessage("1", "delmartian", "B000UA0QIQ", "Cool"));
        csvRecordMessageList.add(new CsvRecordMessage("2", "Twoapennything", "B006K2ZZ7K", "Cool, Lucky"));
        csvRecordMessageList.add(new CsvRecordMessage("2", "Twoapennything", "B006K2ZZ7K", "AC/DC Rules"));
        csvRecordMessageList.add(new CsvRecordMessage("3", "Twoapennything", "B006K2ZZ7K", "Lucky"));
        csvRecordMessageList.add(new CsvRecordMessage("4", "Twoapennything", "B006K2ZZ7K", "Lucky"));
        csvRecordMessageList.add(new CsvRecordMessage("5", "R. James", "B000UA0QIQ", "Lucky"));
        csvRecordMessageList.add(new CsvRecordMessage("5", "Carol", "B000LQOCH0", "Lucky"));


        for(CsvRecordMessage message:csvRecordMessageList) {
            defaultRecordProcessor.processRecord(message, parsingResult);
        }

        checkActiveUsersCount(parsingResult);
        checkProductToReviewCount(parsingResult);
        checkWordCount(parsingResult);
    }

    private void checkActiveUsersCount(ParsingResult parsingResult) {
        Map<String, User> activeUsers = parsingResult.getActiveUsers();
        assertEquals(5, activeUsers.size());
        assertEquals(4l, activeUsers.get("1").getCommentsCount().get());
        assertEquals(2l, activeUsers.get("2").getCommentsCount().get());
        assertEquals(1l, activeUsers.get("3").getCommentsCount().get());
        assertEquals(1l, activeUsers.get("4").getCommentsCount().get());
        assertEquals(2l, activeUsers.get("5").getCommentsCount().get());
    }

    private void checkProductToReviewCount(ParsingResult parsingResult) {
        Map<String, AtomicLong> productToReviewCountMap = parsingResult.getProductToReviewCountMap();
        assertEquals(4, productToReviewCountMap.size());
        assertEquals(3l, productToReviewCountMap.get("B000UA0QIQ").get());
        assertEquals(1l, productToReviewCountMap.get("B00813GRG4").get());
        assertEquals(2l, productToReviewCountMap.get("B000LQOCH0").get());
        assertEquals(4l, productToReviewCountMap.get("B006K2ZZ7K").get());
    }

    private void checkWordCount(ParsingResult parsingResult) {
        Map<String, AtomicLong> wordCountMap = parsingResult.getUsedWordsCountMap();
        assertEquals(5, wordCountMap.size());
        assertEquals(3l, wordCountMap.get("Cool").get());
        assertEquals(6, wordCountMap.get("Lucky").get());
        assertEquals(2l, wordCountMap.get("Rules").get());
        assertEquals(2l, wordCountMap.get("AC").get());
        assertEquals(2l, wordCountMap.get("DC").get());
    }
}