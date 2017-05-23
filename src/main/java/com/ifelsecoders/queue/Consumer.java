package com.ifelsecoders.queue;

import com.ifelsecoders.model.GoogleTranslateResponse;
import com.ifelsecoders.model.MessageForTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private Broker broker;

    public Consumer(Broker broker) {
        this.broker = broker;
    }


    @Override
    public void run() {
        List<MessageForTranslation> messageAggregator = new ArrayList<>();
        AtomicInteger messageLength = new AtomicInteger(0);
        try {
            MessageForTranslation messageForTranslation = null;

            while (broker.getContinueProducing() || messageForTranslation != null) {
                messageForTranslation = broker.get();
                if(messageForTranslation == null || messageForTranslation.getText() == null) {
                    continue;
                }
                if(messageLength.get() + messageForTranslation.getText().length() > 100) {
                    sendForTranslation();
                    messageAggregator = new ArrayList<>();
                    messageLength.set(0);
                }

                messageAggregator.add(messageForTranslation);
                messageLength.getAndAdd(messageForTranslation.getText().length());
            }

            if(messageAggregator.size() > 0) {
                sendForTranslation();
            }
        } catch (BrokerException ex) {
            ex.printStackTrace();
        }
    }

    private GoogleTranslateResponse sendForTranslation() {
        // Lets` assume here is mocked call to Google Translate API
        // As an option we can implement putting response message to another queue to make asynchronous
        // processing of responses.
        return null;
    }
}
