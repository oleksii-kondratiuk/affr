package com.ifelsecoders.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class ConsumerThreadPool {
    // Can be put to configuration or obtained from parameters of Jar execution
    private int threadPoolSize = 4;
    private ExecutorService executor;

    @Autowired
    private Broker broker;

    @PostConstruct
    public void postInit() throws InterruptedException {
        executor = Executors.newFixedThreadPool(threadPoolSize);
        for (int i = 0; i < threadPoolSize; i++) {
            Consumer consumer = new Consumer(broker);
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

    /*@PreDestroy
    public void preDestroy() throws InterruptedException {
        executor.awaitTermination(10, TimeUnit.SECONDS);
        executor.shutdown();
    }*/
}
