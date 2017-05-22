package com.ifelsecoders.parser;

import com.ifelsecoders.model.ParsingResult;

import java.io.File;

/**
 * Parser which obtains file as an input and produces model which reflects CSV file format.
 */
public interface Parser {

    /**
     * Parses file into set of collections and returns them in a wrapper ParsingResult object
     * @param file
     * @return
     * @throws ParserException
     */
    ParsingResult parse(File file) throws ParserException;
}
