package com.modsen.event.mapper.parser;

import java.util.List;

/**
 * @author Alexander Dudkin
 */
public interface JsonFieldParser {
    List<String> parse(String json);
}
