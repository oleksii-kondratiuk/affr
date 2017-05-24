package com.ifelsecoders.queue;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class ConsumerThreadPool<T extends Consumer, M extends QueueMessage, B extends Broker<M>> {
    // Can be put to configuration or obtained from parameters of Jar execution
    private int threadPoolSize = 4;
    private ExecutorService executor;

    @Autowired
    private B broker;

    @Autowired
    private ConsumerFactory<T, B> consumerFactory;

    @PostConstruct
    public void postInit() throws InterruptedException {
        executor = Executors.newFixedThreadPool(threadPoolSize);
        for (int i = 0; i < threadPoolSize; i++) {
            Consumer consumer = consumerFactory.getConsumerInstance(broker);
            executor.execute(consumer);
        }
    }

    public void shutdown() throws InterruptedException {
        while(broker.queue.size() != 0) {
            Thread.sleep(1000);
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
