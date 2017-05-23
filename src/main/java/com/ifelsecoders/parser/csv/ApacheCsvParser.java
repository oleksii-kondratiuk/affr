package com.ifelsecoders.parser.csv;

import com.ifelsecoders.model.MessageForTranslation;
import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.model.User;
import com.ifelsecoders.parser.Parser;
import com.ifelsecoders.parser.ParserException;
import com.ifelsecoders.queue.BrokerException;
import com.ifelsecoders.queue.TranslateMessageBroker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
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

    private static final String INPUT_LANG = "en";
    private static final String OUTPUT_LANG = "fr";

    @Autowired
    public TranslateMessageBroker translateMessageBroker;

    public ParsingResult parse(File file) throws ParserException {
        CSVParser csvRecords = parseFile(file);
        Map<String, User> activeUsers = new HashMap<>();
        Map<String, AtomicLong> productToReviewCountMap = new HashMap<>();
        Map<String, AtomicLong> usedWordsCountMap = new HashMap<>();

        for (CSVRecord csvRecord : csvRecords) {
            processRecord(activeUsers, productToReviewCountMap, usedWordsCountMap, csvRecord);
        }
        return new ParsingResult(activeUsers, productToReviewCountMap, usedWordsCountMap);
    }

    private void processRecord(Map<String, User> activeUsers,
                               Map<String, AtomicLong> productToReviewCountMap,
                               Map<String, AtomicLong> usedWordsCountMap, CSVRecord csvRecord) {
        String userId = csvRecord.get(2);
        String profileName = csvRecord.get(3);
        String productId = csvRecord.get(1);
        String comment = csvRecord.get(9);


        mapRecordToUser(userId, profileName, activeUsers);
        increaseReviewCountForProduct(productId, productToReviewCountMap);
        increaseWordCountForProduct(comment, usedWordsCountMap);
        sendMessageForTranslation(comment);
    }

    private void sendMessageForTranslation(String comment) {
        MessageForTranslation messageForTranslation = new MessageForTranslation(INPUT_LANG, OUTPUT_LANG, comment);
        try {
            translateMessageBroker.put(messageForTranslation);
        } catch (BrokerException e) {
            log.error("Could not put message {} to queue", messageForTranslation);
        }
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
            usedWordsCountMap.putIfAbsent(word, new AtomicLong(0l));
            usedWordsCountMap.get(word).incrementAndGet();
        });
    }

    CSVParser parseFile(File file) throws ParserException {
        try {
            CSVFormat format = CSVFormat.DEFAULT.withHeader(ID, PRODUCT_ID, USER_ID,
                    PROFILE_NAME, HELPFULNESS_NUMERATOR,
                    HELPFULNESS_DENOMINATOR, SCORE, TIME, SUMMARY, TEXT).withFirstRecordAsHeader();
            return CSVParser.parse(file, Charset.defaultCharset(),
                    format);
        } catch (IOException e) {
            throw new ParserException("Could not parse file " + file);
        }
    }

    public void setTranslateMessageBroker(TranslateMessageBroker translateMessageBroker) {
        this.translateMessageBroker = translateMessageBroker;
    }
}
