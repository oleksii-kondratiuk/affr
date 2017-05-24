package com.ifelsecoders.parser.csv;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.model.MessageForTranslation;
import com.ifelsecoders.queue.BrokerException;
import com.ifelsecoders.queue.TranslateMessageBroker;
import com.ifelsecoders.queue.record.RecordBroker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApacheCsvParserTest {
    ApacheCsvParser csvParser;

    @Mock
    private RecordBroker recordBroker;

    @Mock
    private TranslateMessageBroker translateMessageBroker;

    @Before
    public void init() throws BrokerException {
        csvParser = new ApacheCsvParser();
        csvParser.setTranslateMessageBroker(new TranslateMessageBroker());
        csvParser.setRecordBroker(recordBroker);
        csvParser.setTranslateMessageBroker(translateMessageBroker);

        doNothing().when(recordBroker).put(any(CsvRecordMessage.class));
        doNothing().when(translateMessageBroker).put(any(MessageForTranslation.class));
    }

    @Test
    public void testSimple10RowsCsvWithNoTranslation() throws Exception {
        File inputCsvFile = new File(getClass().getResource("/10_rows.csv").getFile());
        csvParser.parse(inputCsvFile, false);

        verify(recordBroker, times(10)).put(any(CsvRecordMessage.class));
        verify(translateMessageBroker, times(0)).put(any(MessageForTranslation.class));
    }

    @Test
    public void testSimple10RowsCsvWithTranslation() throws Exception {
        File inputCsvFile = new File(getClass().getResource("/10_rows.csv").getFile());
        csvParser.parse(inputCsvFile, true);

        verify(recordBroker, times(10)).put(any(CsvRecordMessage.class));
        verify(translateMessageBroker, times(10)).put(any(MessageForTranslation.class));
    }
}