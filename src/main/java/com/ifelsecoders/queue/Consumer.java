package com.ifelsecoders.queue;

public abstract class Consumer<T extends QueueMessage> implements Runnable {
    protected TranslateMessageBroker translateMessageBroker;

    @Override
    public void run() {
        processMessage();
    }

    abstract void processMessage();
}
