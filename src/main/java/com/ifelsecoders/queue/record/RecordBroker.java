package com.ifelsecoders.queue.record;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.queue.Broker;
import org.springframework.stereotype.Component;

@Component
public class RecordBroker extends Broker<CsvRecordMessage> {
}
