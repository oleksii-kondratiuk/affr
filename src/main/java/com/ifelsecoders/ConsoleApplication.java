package com.ifelsecoders;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.parser.Parser;
import com.ifelsecoders.queue.TranslateMessageConsumerThreadPool;
import com.ifelsecoders.queue.record.RecordMessageConsumerFactory;
import com.ifelsecoders.queue.record.RecordMessageConsumerThreadPool;
import com.ifelsecoders.service.ParsingResultsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ConsoleApplication implements CommandLineRunner {
    // TODO make it configurable
    private static final int CSV_PROCESSING_TIMEOUT = 600;

    @Autowired
    private Parser parser;

    @Autowired
    private ParsingResultsProcessor processor;

    @Autowired
    RecordMessageConsumerFactory recordMessageConsumerFactory;

    @Autowired
    private TranslateMessageConsumerThreadPool messageForTranslationConsumerThreadPool;

    @Autowired
    private RecordMessageConsumerThreadPool recordMessageConsumerThreadPool;

    @Override
    public void run(String... args) throws Exception {
        File file = new File(args[0]);

        Boolean translate = false;
        // TODO Here should be proper arguments parsing implemented.
        if(args.length == 2 && "translate=true".equalsIgnoreCase(args[1])) {
            translate = true;
        }

        // TODO proper error handling in case if parsing or any other component failed
        parser.parse(file, translate);

        boolean finishedInTime = recordMessageConsumerFactory.getCountDownLatch()
                .await(CSV_PROCESSING_TIMEOUT, TimeUnit.SECONDS);
        if(!finishedInTime) {
            System.out.println("Warning! CSV file processing took more than: " + CSV_PROCESSING_TIMEOUT + " "
                    + TimeUnit.SECONDS);
        }

        ParsingResult parsingResult = recordMessageConsumerFactory.getParsingResult();

        System.out.println("Most active users: ");
        processor.getMostActiveUsersInAlphabeticalOrder(parsingResult, 1000)
                .stream()
                .forEach(user -> System.out.print(user.getProfileName() + ","));

        System.out.println();

        System.out.println("Most commented food items: ");
        processor.getMostCommentedFoodItems(parsingResult, 1000)
                .stream()
                .forEach(itemId -> System.out.print(itemId + ","));

        System.out.println();

        System.out.println("Most used words: ");
        processor.getMostUsedWords(parsingResult, 1000)
                .stream()
                .forEach(word -> System.out.print(word + ","));

        // TODO hook to spring context destroy
        messageForTranslationConsumerThreadPool.shutdown();
        recordMessageConsumerThreadPool.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ConsoleApplication.class);
        app.run(args);
    }
}
