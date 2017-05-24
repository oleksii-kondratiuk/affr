package com.ifelsecoders.parser;

import java.io.File;

/**
 * Parser which obtains file as an input and produces model which reflects CSV file format.
 */
public interface Parser {

    /**
     * Parses file into set of collections and returns them in a wrapper ParsingResult object.
     * If translate param is set to true then records will translated using Google API.
     * @param file
     * @param translate
     * @return
     * @throws ParserException
     */
    void parse(File file, Boolean translate) throws ParserException;
}
