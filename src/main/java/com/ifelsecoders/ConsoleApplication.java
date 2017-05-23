package com.ifelsecoders;

import com.ifelsecoders.model.MessageForTranslation;
import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.parser.Parser;
import com.ifelsecoders.queue.ConsumerThreadPool;
import com.ifelsecoders.queue.TranslateMessageBroker;
import com.ifelsecoders.queue.TranslateMessageConsumer;
import com.ifelsecoders.service.ParsingResultsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class ConsoleApplication implements CommandLineRunner {

    @Autowired
    private Parser parser;

    @Autowired
    private ParsingResultsProcessor processor;

    @Autowired
    private ConsumerThreadPool<TranslateMessageConsumer, MessageForTranslation,
            TranslateMessageBroker> messageForTranslationConsumerThreadPool;

    @Override
    public void run(String... args) throws Exception {
        File file = new File(args[0]);

        ParsingResult parsingResult = parser.parse(file);

        System.out.println("Most active users: ");
        processor.getMostActiveUsersInAlphabeticalOrder(parsingResult, 1000)
                .stream()
                .forEach(user -> System.out.println(user.getProfileName()));

        System.out.println();

        System.out.println("Most commented food items: ");
        processor.getMostCommentedFoodItems(parsingResult, 1000)
                .stream()
                .forEach(itemId -> System.out.println(itemId));

        System.out.println();

        System.out.println("Most used words: ");
        processor.getMostUsedWords(parsingResult, 1000)
                .stream()
                .forEach(word -> System.out.println(word));

        messageForTranslationConsumerThreadPool.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ConsoleApplication.class);
        app.run(args);
    }
}
