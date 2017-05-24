package com.ifelsecoders.parser.csv;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.model.MessageForTranslation;
import com.ifelsecoders.parser.Parser;
import com.ifelsecoders.parser.ParserException;
import com.ifelsecoders.queue.BrokerException;
import com.ifelsecoders.queue.TranslateMessageBroker;
import com.ifelsecoders.queue.record.RecordBroker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
@Component
public class ApacheCsvParser implements Parser {
    public static final String ID = "Id";
    public static final String PRODUCT_ID = "ProductId";
    public static final String USER_ID = "UserId";
    public static final String PROFILE_NAME = "ProfileName";
    public static final String HELPFULNESS_NUMERATOR = "HelpfulnessNumerator";
    public static final String HELPFULNESS_DENOMINATOR = "HelpfulnessDenominator";
    public static final String SCORE = "Score";
    public static final String TIME = "Time";
    public static final String SUMMARY = "Summary";
    public static final String TEXT = "Text";

    private static final String INPUT_LANG = "en";
    private static final String OUTPUT_LANG = "fr";

    @Autowired
    public TranslateMessageBroker translateMessageBroker;

    @Autowired
    public RecordBroker recordBroker;

    public void parse(File file) throws ParserException {
        CSVParser csvRecords = parseFile(file);

        for (CSVRecord csvRecord : csvRecords) {
            processRecord(csvRecord);
        }
        recordBroker.setContinueProducing(Boolean.FALSE);
        translateMessageBroker.setContinueProducing(Boolean.FALSE);
    }

    private void processRecord(CSVRecord csvRecord) throws ParserException {
        String userId = csvRecord.get(USER_ID);
        String profileName = csvRecord.get(PROFILE_NAME);
        String productId = csvRecord.get(PRODUCT_ID);
        String comment = csvRecord.get(TEXT);
        CsvRecordMessage csvRecordMessage = new CsvRecordMessage(userId, profileName, productId, comment);
        try {
            recordBroker.put(csvRecordMessage);
        } catch (BrokerException e) {
            throw new ParserException("Could not put message " + csvRecordMessage + " to the record broker", e);
        }
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

    public void setRecordBroker(RecordBroker recordBroker) {
        this.recordBroker = recordBroker;
    }
}
