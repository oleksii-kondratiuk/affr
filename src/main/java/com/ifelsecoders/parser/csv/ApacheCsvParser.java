package com.ifelsecoders.parser.csv;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import com.ifelsecoders.parser.Parser;
import com.ifelsecoders.parser.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ApacheCsvParser implements Parser {
    final String ID = "Id";
    final String PRODUCT_ID = "ProductId";
    final String USER_ID = "UserId";
    final String PROFILE_NAME = "ProfileName";
    final String HELPFULNESS_NUMERATOR = "HelpfulnessNumerator";
    final String HELPFULNESS_DENOMINATOR = "HelpfulnessDenominator";
    final String SCORE = "Score";
    final String TIME = "Time";
    final String SUMMARY = "Summary";
    final String TEXT = "Text";

    public ParsingResult parse(File file) throws ParserException {
        CSVParser csvRecords = parseFile(file);
        ConcurrentHashMap<String, User> activeUsers = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Long> productToReviewCountMap = new ConcurrentHashMap<>();

        try {
            csvRecords.getRecords().parallelStream().forEach(csvRecord -> {
                String userId = csvRecord.get(USER_ID);
                String profileName = csvRecord.get(PROFILE_NAME);
                String productId = csvRecord.get(PRODUCT_ID);

                mapRecordToUser(userId, profileName, activeUsers);
                increaseReviewCountForProduct(productId, productToReviewCountMap);
            });
        } catch (IOException e) {
            throw new ParserException("Could not get records for CSV file " + file.getName(), e);
        }

        return new ParsingResult(activeUsers, productToReviewCountMap);
    }

    void mapRecordToUser(String userId, String profileName, Map<String, User> users) {
        User user = new User(userId, profileName);
        users.merge(userId, user, (user1, user2) -> {
            user1.setCommentsCount(user1.getCommentsCount() + 1);
            return user1;
        });
    }

    void increaseReviewCountForProduct(String productId, Map<String, Long> productToReviewCountMap) {
        productToReviewCountMap.merge(productId, 1l, (previousCounter, currentCounter) -> previousCounter + 1);
    }

    CSVParser parseFile(File file) throws ParserException {
        try {
            Reader in = new FileReader(file);
            return CSVFormat.EXCEL.
                    withHeader(ID, PRODUCT_ID, USER_ID, PROFILE_NAME, HELPFULNESS_NUMERATOR,
                            HELPFULNESS_DENOMINATOR, SCORE, TIME, SUMMARY, TEXT)
                    .withFirstRecordAsHeader()
                    .withIgnoreEmptyLines().parse(in);
        } catch (IOException e) {
            throw new ParserException("Could not parse file " + file);
        }
    }
}
