package com.ifelsecoders.queue;

public interface ConsumerFactory<T extends Consumer, B extends Broker> {
    T getConsumerInstance(B broker);
}
