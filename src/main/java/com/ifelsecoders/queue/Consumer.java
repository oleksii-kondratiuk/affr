package com.ifelsecoders.queue;

public abstract class Consumer<T extends QueueMessage, B extends Broker> implements Runnable {
    protected B messageBroker;

    @Override
    public void run() {
        processMessage();
    }

    protected abstract void processMessage();
}
