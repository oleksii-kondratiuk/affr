package com.ifelsecoders.queue.record;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.queue.ConsumerThreadPool;
import org.springframework.stereotype.Component;

@Component(value = "recordMessageConsumerThreadPool")
public class RecordMessageConsumerThreadPool extends ConsumerThreadPool<RecordMessageConsumer, CsvRecordMessage, RecordBroker> {
}
