package com.ifelsecoders.model;

import com.ifelsecoders.queue.QueueMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CsvRecordMessage implements QueueMessage {
    final private String userId;
    final private String profileName;
    final private String productId;
    final private String comment;
}
