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

    @Before
    public void init() {
        defaultParsingResultsProcessor = new DefaultParsingResultsProcessor();
    }

    @Test
    public void testGetMostActiveUsersInAlphabeticalOrder() throws Exception {
        Map<String, User> activeUsers = new HashMap();
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

        Map<String, Long> productToReviewCountMap = new HashMap<>();
        ParsingResult parsingResult = new ParsingResult(activeUsers, productToReviewCountMap);

        List<User> mostActiveUsers = defaultParsingResultsProcessor
                .getMostActiveUsersInAlphabeticalOrder(parsingResult, 3);

        assertEquals(3, mostActiveUsers.size());
        assertEquals(JOHN_PROFILENAME, mostActiveUsers.get(0).getProfileName());
        assertEquals(OLIVER_PROFILENAME, mostActiveUsers.get(1).getProfileName());
        assertEquals(PAUL_PROFILENAME, mostActiveUsers.get(2).getProfileName());
    }
}