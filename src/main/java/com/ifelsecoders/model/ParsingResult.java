package com.ifelsecoders.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ParsingResult {
    Map<String, User> activeUsers;
}
