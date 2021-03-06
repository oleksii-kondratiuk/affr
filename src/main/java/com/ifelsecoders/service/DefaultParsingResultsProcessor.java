package com.ifelsecoders.service;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultParsingResultsProcessor implements ParsingResultsProcessor {
    public List<User> getMostActiveUsersInAlphabeticalOrder(ParsingResult parsingResult, int limit) {
        return parsingResult.getActiveUsers().entrySet().stream()
                .sorted((e1, e2) -> Long.valueOf(e2.getValue().getCommentsCount().get())
                        .compareTo(Long.valueOf(e1.getValue().getCommentsCount().get())))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())
                .stream()
                .limit(limit)
                .sorted((e1, e2) -> e1.getProfileName().toLowerCase().compareTo(e2.getProfileName().toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMostCommentedFoodItems(ParsingResult parsingResult, int limit) {
        return parsingResult.getProductToReviewCountMap().entrySet().stream()
                .sorted((e1, e2) -> Long.valueOf(e2.getValue().get()).compareTo(Long.valueOf(e1.getValue().get())))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMostUsedWords(ParsingResult parsingResult, int limit) {
        return parsingResult.getUsedWordsCountMap().entrySet().stream()
                .sorted((e1, e2) -> Long.valueOf(e2.getValue().get()).compareTo(Long.valueOf(e1.getValue().get())))
                .map(Map.Entry::getKey)
                .limit(limit)
                .sorted()
                .collect(Collectors.toList());
    }
}
