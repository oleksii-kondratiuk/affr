package com.ifelsecoders.queue.record;

import com.ifelsecoders.model.CsvRecordMessage;
import com.ifelsecoders.model.ParsingResult;

public interface RecordProcessor {
    void processRecord(CsvRecordMessage message, ParsingResult parsingResult);
}
