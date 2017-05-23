package com.ifelsecoders.queue;

import org.springframework.stereotype.Component;

@Component
public class TranslateMessageConsumerFactory implements ConsumerFactory<TranslateMessageConsumer, TranslateMessageBroker> {
    @Override
    public TranslateMessageConsumer getConsumerInstance(TranslateMessageBroker broker) {
        return new TranslateMessageConsumer(broker);
    }
}
