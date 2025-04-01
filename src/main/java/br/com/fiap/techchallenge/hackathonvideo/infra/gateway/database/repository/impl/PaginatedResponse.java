package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

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

    public Map<String, AttributeValue> getLastEvaluatedAttributeValue() {
        return lastEvaluatedKey;
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
