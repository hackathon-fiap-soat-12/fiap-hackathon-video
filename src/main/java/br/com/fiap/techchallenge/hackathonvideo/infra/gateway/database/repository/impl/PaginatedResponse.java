package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginatedResponse<T> {
    private final List<T> items;
    private final Map<String, AttributeValue> lastEvaluatedKey;

    private PaginatedResponse(Builder<T> builder) {
        this.items = builder.items;
        this.lastEvaluatedKey = builder.lastEvaluatedKey;
    }

    public List<T> getItems() {
        return items;
    }

    @JsonProperty("lastEvaluatedKey")
    public Map<String, Object> getLastEvaluatedKeyAsMap() {
        return convertLastEvaluatedKey(lastEvaluatedKey);
    }

    public Map<String, AttributeValue> getLastEvaluatedAttributeValue() {
        return lastEvaluatedKey;
    }

    private Map<String, Object> convertLastEvaluatedKey(Map<String, AttributeValue> exclusiveStartKey) {
        if (exclusiveStartKey == null) return null;

        Map<String, Object> convertedKey = new HashMap<>();
        for (Map.Entry<String, AttributeValue> entry : exclusiveStartKey.entrySet()) {
            convertedKey.put(entry.getKey(), convertAttributeValue(entry.getValue()));
        }
        return convertedKey;
    }

    private Object convertAttributeValue(AttributeValue attributeValue) {
        if (attributeValue.s() != null) return attributeValue.s();
        if (attributeValue.n() != null) return attributeValue.n();
        if (attributeValue.bool() != null) return attributeValue.bool();
        if (attributeValue.b() != null) return attributeValue.b().asByteArray();
        if (attributeValue.ss() != null) return attributeValue.ss();
        if (attributeValue.ns() != null) return attributeValue.ns();
        if (attributeValue.bs() != null) return attributeValue.bs();
        return null; // Outros tipos podem ser tratados conforme necessário
    }

    public static class Builder<T> {
        private List<T> items;
        private Map<String, AttributeValue> lastEvaluatedKey;

        public Builder<T> items(List<T> items) {
            this.items = items;
            return this;
        }

        public Builder<T> lastEvaluatedKey(Map<String, AttributeValue> lastEvaluatedKey) {
            this.lastEvaluatedKey = lastEvaluatedKey;
            return this;
        }

        public PaginatedResponse<T> build() {
            return new PaginatedResponse<>(this);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
}
