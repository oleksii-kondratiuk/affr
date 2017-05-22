package com.ifelsecoders.service;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DefaultParsingResultsProcessorTest {

    private static final String JOHN_PROFILENAME = "John";
    private static final String PAUL_PROFILENAME = "Paul";
    private static final String OLIVER_PROFILENAME = "Oliver";
    private static final String MARK_PROFILENAME = "Mark";
    private static final String JOHN_USER_ID = "1";
    private static final String PAUL_USER_ID = "2";
    private static final String OLIVER_USER_ID = "3";
    private static final String MARK_USER_ID = "4";

    DefaultParsingResultsProcessor defaultParsingResultsProcessor;
    Map<String, User> activeUsers;
    Map<String, Long> productToReviewCountMap;
    ParsingResult parsingResult;

    @Before
    public void init() {
        defaultParsingResultsProcessor = new DefaultParsingResultsProcessor();
        activeUsers = new HashMap();
        productToReviewCountMap = new HashMap<>();
        parsingResult = new ParsingResult(activeUsers, productToReviewCountMap);
    }

    @Test
    public void testGetMostActiveUsersInAlphabeticalOrder() throws Exception {
        User john = new User(JOHN_USER_ID, JOHN_PROFILENAME);
        john.setCommentsCount(2l);
        activeUsers.put(JOHN_USER_ID, john);
        User paul = new User(PAUL_USER_ID, PAUL_PROFILENAME);
        paul.setCommentsCount(5l);
        activeUsers.put(PAUL_USER_ID, paul);
        User oliver = new User(OLIVER_USER_ID, OLIVER_PROFILENAME);
        oliver.setCommentsCount(5l);
        activeUsers.put(OLIVER_USER_ID, oliver);
        User mark = new User(MARK_USER_ID, MARK_PROFILENAME);
        mark.setCommentsCount(1l);
        activeUsers.put(MARK_USER_ID, mark);

        List<User> mostActiveUsers = defaultParsingResultsProcessor
                .getMostActiveUsersInAlphabeticalOrder(parsingResult, 3);

        assertEquals(3, mostActiveUsers.size());
        assertEquals(JOHN_PROFILENAME, mostActiveUsers.get(0).getProfileName());
        assertEquals(OLIVER_PROFILENAME, mostActiveUsers.get(1).getProfileName());
        assertEquals(PAUL_PROFILENAME, mostActiveUsers.get(2).getProfileName());
    }

    @Test
    public void getMostCommentedFoodItems() {
        productToReviewCountMap.put("A", 1l);
        productToReviewCountMap.put("B", 10l);
        productToReviewCountMap.put("C", 15l);
        productToReviewCountMap.put("D", 20l);
        productToReviewCountMap.put("E", 8l);
        productToReviewCountMap.put("F", 12l);

        List<String> mostCommentedFoodItems = defaultParsingResultsProcessor
                .getMostCommentedFoodItems(parsingResult, 5);

        assertEquals(5, mostCommentedFoodItems.size());
        assertEquals("D", mostCommentedFoodItems.get(0));
        assertEquals("C", mostCommentedFoodItems.get(1));
        assertEquals("F", mostCommentedFoodItems.get(2));
        assertEquals("B", mostCommentedFoodItems.get(3));
        assertEquals("E", mostCommentedFoodItems.get(4));
    }
}