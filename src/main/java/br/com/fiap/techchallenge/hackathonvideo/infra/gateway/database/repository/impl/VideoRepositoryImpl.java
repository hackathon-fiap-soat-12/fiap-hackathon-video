package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.VideoRepository;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@Repository
public class VideoRepositoryImpl implements VideoRepository {

    private final DynamoDbTemplate dynamoDbTemplate;

    public VideoRepositoryImpl(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public VideoEntity save(VideoEntity videoEntity) {
        return dynamoDbTemplate.save(videoEntity);
    }

    @Override
    public Optional<VideoEntity> findById(UUID id) {
        var key = Key.builder()
                .partitionValue(id.toString())
                .build();

        return Optional.ofNullable(dynamoDbTemplate.load(key, VideoEntity.class));
    }

    @Override
    public PaginatedResponse<VideoEntity> findAllByUserId(UUID userId, int pageSize, Map<String, AttributeValue> exclusiveStartKey) {
        var queryConditional = QueryConditional
                .keyEqualTo(k -> k.partitionValue(userId.toString()));

        QueryEnhancedRequest.Builder queryRequestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .limit(pageSize);

        if (exclusiveStartKey != null && !exclusiveStartKey.isEmpty()) {
            queryRequestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        PageIterable<VideoEntity> queryResult = dynamoDbTemplate.query(queryRequestBuilder.build(), VideoEntity.class, "UserCreatedAtIndex");

        var page = queryResult.stream().findFirst().orElse(null);

        return PaginatedResponse.<VideoEntity>builder()
                .items((page != null) ? page.items() : List.of())
                .lastEvaluatedKey((page != null) ? page.lastEvaluatedKey() : null)
                .build();
    }
}
