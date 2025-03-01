package com.modsen.event.mapper.parser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.event.exception.RepositoryException;
import com.modsen.event.mapper.parser.JsonFieldParser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Alexander Dudkin
 */
@Component("urlParser")
public class UrlFieldParser implements JsonFieldParser {

    private final ObjectMapper objectMapper;

    private static final String URL_FIELD = "url";

    public UrlFieldParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<String> parse(String json) {
        try {
            List<Map<String, String>> items = objectMapper.readValue(
                    json,
                    new TypeReference<>() {}
            );

            return items.stream()
                    .map(item -> item.get(URL_FIELD))
                    .filter(Objects::nonNull)
                    .toList();
        } catch (JsonProcessingException e) {
            throw new RepositoryException("Error parsing url field");
        }
    }

}
