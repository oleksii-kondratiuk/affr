package com.ifelsecoders.queue.record;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.queue.BrokerException;
import com.ifelsecoders.queue.Consumer;

import java.util.concurrent.CountDownLatch;

public class RecordMessageConsumer extends Consumer<CsvRecordMessage, RecordBroker> {
    private ParsingResult parsingResult;
    private CountDownLatch countDownLatch;
    private RecordProcessor recordProcessor;

    public RecordMessageConsumer(RecordBroker broker, CountDownLatch countDownLatch, ParsingResult parsingResult,
                                 RecordProcessor recordProcessor) {
        this.messageBroker = broker;
        this.countDownLatch = countDownLatch;
        this.parsingResult = parsingResult;
        this.recordProcessor = recordProcessor;
    }

    @Override
    public void processMessage() {
        try {
            CsvRecordMessage message = null;

            while (messageBroker.getContinueProducing() || message != null) {
                message = messageBroker.get();
                if(message != null) {
                    recordProcessor.processRecord(message, parsingResult);
                }
            }
            countDownLatch.countDown();
        } catch (BrokerException ex) {
            ex.printStackTrace();
        }
    }
}
