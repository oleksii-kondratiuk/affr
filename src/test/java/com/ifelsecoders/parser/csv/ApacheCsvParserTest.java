package com.ifelsecoders.parser.csv;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class ApacheCsvParserTest {

    private static final String JOHN_USER_ID = "1";
    private static final String JOHN_PROFILE_NAME = "John";
    private static final String PAUL_USER_ID = "2";
    private static final String PAUL_PROFILE_NAME = "Paul";

    ApacheCsvParser csvParser;

    @Before
    public void init() {
        csvParser = new ApacheCsvParser();
    }

    @Test
    public void testMapRecordToUser() throws Exception {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

        csvParser.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);
        assertEquals(1, users.size());
        assertEquals(JOHN_USER_ID, users.get(JOHN_USER_ID).getUserId());
        assertEquals(JOHN_PROFILE_NAME, users.get(JOHN_USER_ID).getProfileName());
        assertEquals(new Long(1l), users.get(JOHN_USER_ID).getCommentsCount());
    }

    @Test
    public void testMapRecordForTwoDistinctUsers() throws Exception {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

        csvParser.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);
        csvParser.mapRecordToUser(PAUL_USER_ID, PAUL_PROFILE_NAME, users);

        assertEquals(2, users.size());
        assertEquals(JOHN_USER_ID, users.get(JOHN_USER_ID).getUserId());
        assertEquals(JOHN_PROFILE_NAME, users.get(JOHN_USER_ID).getProfileName());
        assertEquals(new Long(1l), users.get(JOHN_USER_ID).getCommentsCount());

        assertEquals(PAUL_USER_ID, users.get(PAUL_USER_ID).getUserId());
        assertEquals(PAUL_PROFILE_NAME, users.get(PAUL_USER_ID).getProfileName());
        assertEquals(new Long(1l), users.get(PAUL_USER_ID).getCommentsCount());
    }

    @Test
    public void testTwoMapRecordsForOneUser() throws Exception {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

        csvParser.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);
        csvParser.mapRecordToUser(JOHN_USER_ID, JOHN_PROFILE_NAME, users);

        assertEquals(1, users.size());
        assertEquals(JOHN_USER_ID, users.get(JOHN_USER_ID).getUserId());
        assertEquals(JOHN_PROFILE_NAME, users.get(JOHN_USER_ID).getProfileName());
        assertEquals(new Long(2l), users.get(JOHN_USER_ID).getCommentsCount());
    }

    @Test
    public void testSimple10RowsCsv() throws Exception {
        File inputCsvFile = new File(getClass().getResource("/10_rows.csv").getFile());
        ParsingResult parsingResult = csvParser.parse(inputCsvFile);

        checkActiveUsersCount(parsingResult);
        checkProductToReviewCount(parsingResult);
        checkWordCount(parsingResult);
    }

    private void checkActiveUsersCount(ParsingResult parsingResult) {
        Map<String, User> activeUsers = parsingResult.getActiveUsers();
        assertEquals(5, activeUsers.size());
        assertEquals(new Long(4l), activeUsers.get("1").getCommentsCount());
        assertEquals(new Long(2l), activeUsers.get("2").getCommentsCount());
        assertEquals(new Long(1l), activeUsers.get("3").getCommentsCount());
        assertEquals(new Long(1l), activeUsers.get("4").getCommentsCount());
        assertEquals(new Long(2l), activeUsers.get("5").getCommentsCount());
    }

    private void checkProductToReviewCount(ParsingResult parsingResult) {
        Map<String, Long> productToReviewCountMap = parsingResult.getProductToReviewCountMap();
        assertEquals(4, productToReviewCountMap.size());
        assertEquals(new Long(3l), productToReviewCountMap.get("B000UA0QIQ"));
        assertEquals(new Long(1l), productToReviewCountMap.get("B00813GRG4"));
        assertEquals(new Long(2l), productToReviewCountMap.get("B000LQOCH0"));
        assertEquals(new Long(4l), productToReviewCountMap.get("B006K2ZZ7K"));
    }

    private void checkWordCount(ParsingResult parsingResult) {
        Map<String, Long> wordCountMap = parsingResult.getUsedWordsCountMap();
        assertEquals(4, wordCountMap.size());
        assertEquals(new Long(3l), wordCountMap.get("Cool"));
        assertEquals(new Long(5l), wordCountMap.get("Lucky"));
        assertEquals(new Long(2l), wordCountMap.get("Rules"));
        assertEquals(new Long(2l), wordCountMap.get("AC/DC"));
    }
}