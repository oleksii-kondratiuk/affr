package com.ifelsecoders.queue;

import com.ifelsecoders.model.MessageForTranslation;
import org.springframework.stereotype.Component;

@Component(value = "translateMessageConsumerThreadPool")
public class TranslateMessageConsumerThreadPool extends ConsumerThreadPool<TranslateMessageConsumer, MessageForTranslation, TranslateMessageBroker> {
}
