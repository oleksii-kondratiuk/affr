package com.ifelsecoders.parser.csv;

import com.ifelsecoders.model.CsvRecordMessage;
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

    @Before
    public void init() throws BrokerException {
        csvParser = new ApacheCsvParser();
        csvParser.setTranslateMessageBroker(new TranslateMessageBroker());
        csvParser.setRecordBroker(recordBroker);

        doNothing().when(recordBroker).put(any(CsvRecordMessage.class));
    }

    @Test
    public void testSimple10RowsCsv() throws Exception {
        File inputCsvFile = new File(getClass().getResource("/10_rows.csv").getFile());
        csvParser.parse(inputCsvFile);

        verify(recordBroker, times(10)).put(any(CsvRecordMessage.class));

    }
}