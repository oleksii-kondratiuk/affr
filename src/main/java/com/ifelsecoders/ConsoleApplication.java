package com.ifelsecoders;

import com.ifelsecoders.model.ParsingResult;
import com.ifelsecoders.parser.Parser;
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

    @Override
    public void run(String... args) throws Exception {
        File file = new File(args[0]);

        ParsingResult parsingResult = parser.parse(file);

        System.out.println("Most active users: ");
        processor.getMostActiveUsersInAlphabeticalOrder(parsingResult, 1000)
                .stream()
                .forEach(user -> System.out.println(user.getProfileName()));

        System.out.println("Most commented food items: ");
        processor.getMostCommentedFoodItems(parsingResult, 1000)
                .stream()
                .forEach(itemId -> System.out.println(itemId));
    }

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ConsoleApplication.class);
        app.run(args);
    }
}
