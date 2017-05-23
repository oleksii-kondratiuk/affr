package com.ifelsecoders.queue;

import com.ifelsecoders.model.MessageForTranslation;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class Broker {

    public LinkedBlockingQueue<MessageForTranslation> queue = new LinkedBlockingQueue<>();

    public void setContinueProducing(Boolean continueProducing) {
        this.continueProducing = continueProducing;
    }

    public Boolean getContinueProducing() {
        return continueProducing;
    }

    private Boolean continueProducing = Boolean.TRUE;

    public void put(MessageForTranslation message) throws BrokerException
    {
        try {
            this.queue.put(message);
        } catch (InterruptedException e) {
            throw new BrokerException("Could not put message " + message + " to queue.");
        }
    }

    public MessageForTranslation get() throws BrokerException
    {
        try {
            return this.queue.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new BrokerException("Could not poll message from queue.");
        }
    }
}
