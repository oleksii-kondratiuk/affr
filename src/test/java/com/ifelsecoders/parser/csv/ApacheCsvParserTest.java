package com.ifelsecoders.parser.csv;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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
        assertEquals(5, parsingResult.getActiveUsers().size());
        assertEquals(new Long(4l), parsingResult.getActiveUsers().get("1").getCommentsCount());
        assertEquals(new Long(2l), parsingResult.getActiveUsers().get("2").getCommentsCount());
        assertEquals(new Long(1l), parsingResult.getActiveUsers().get("3").getCommentsCount());
        assertEquals(new Long(1l), parsingResult.getActiveUsers().get("4").getCommentsCount());
        assertEquals(new Long(2l), parsingResult.getActiveUsers().get("5").getCommentsCount());

        assertEquals(4, parsingResult.getProductToReviewCountMap().size());
        assertEquals(new Long(3l), parsingResult.getProductToReviewCountMap().get("B000UA0QIQ"));
        assertEquals(new Long(1l), parsingResult.getProductToReviewCountMap().get("B00813GRG4"));
        assertEquals(new Long(2l), parsingResult.getProductToReviewCountMap().get("B000LQOCH0"));
        assertEquals(new Long(4l), parsingResult.getProductToReviewCountMap().get("B006K2ZZ7K"));
    }
}