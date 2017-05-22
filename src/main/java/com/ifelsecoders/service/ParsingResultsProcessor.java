package com.ifelsecoders.service;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;

import java.util.Collection;

public interface ParsingResultsProcessor {

    Collection<User> getMostActiveUsersInAlphabeticalOrder(ParsingResult parsingResult, int limit);

    Collection<String> getMostCommentedFoodItems(ParsingResult parsingResult, int limit);

    Collection<String> getMostUsedWords(ParsingResult parsingResult, int limit);
}
