package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.VideoRepository;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.paginators.ScanIterable;

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

    public List<VideoEntity> findAllByUserId(UUID userId) {
        ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
                .filterExpression(Expression.builder()
                        .expression("userId = :userIdVal")
                        .expressionValues(Map.of(":userIdVal",
                                AttributeValue.builder().s(userId.toString()).build()))
                        .build())
                .build();

        return dynamoDbTemplate.scan(scanEnhancedRequest, VideoEntity.class).items().stream().toList();
    }

    public PaginatedResponse<VideoEntity> findAllByUserId(UUID userId, int pageSize, Map<String, AttributeValue> exclusiveStartKey) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(k -> k.partitionValue(userId.toString()));

        QueryEnhancedRequest.Builder queryRequestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)  // 🔥 Ordena do mais novo para o mais antigo
                .limit(pageSize);

        if (exclusiveStartKey != null && !exclusiveStartKey.isEmpty()) {
            queryRequestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        PageIterable<VideoEntity> queryResult = dynamoDbTemplate.query(queryRequestBuilder.build(), VideoEntity.class, "UserCreatedAtIndex");

        var page = queryResult.stream().findFirst().orElse(null);
        List<VideoEntity> items = (page != null) ? page.items() : List.of();
        Map<String, AttributeValue> lastEvaluatedKey = (page != null) ? page.lastEvaluatedKey() : null;

        return PaginatedResponse.<VideoEntity>builder()
                .items(items)
                .lastEvaluatedKey(lastEvaluatedKey)
                .build();
    }

    public PaginatedResponse<VideoEntity> findAllByUserId(String userId, int pageSize, Map<String, AttributeValue> exclusiveStartKey) {
        Map<String, AttributeValue> expressionValues = Map.of(
                ":userIdVal", AttributeValue.builder().s(userId.toString()).build()
        );

        Expression filterExpression = Expression.builder()
                .expression("userId = :userIdVal")
                .expressionValues(expressionValues)
                .build();

        ScanEnhancedRequest.Builder scanRequestBuilder = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression)
                .limit(pageSize);

        if (exclusiveStartKey != null && !exclusiveStartKey.isEmpty()) {
            scanRequestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        PageIterable<VideoEntity> scanResult = dynamoDbTemplate.scan(scanRequestBuilder.build(), VideoEntity.class);

        var page = scanResult.stream().findFirst().orElse(null);
        List<VideoEntity> items = (page != null) ? page.items() : List.of();
        Map<String, AttributeValue> lastEvaluatedKey = (page != null) ? page.lastEvaluatedKey() : null;

        return PaginatedResponse.<VideoEntity>builder()
                .items(items)
                .lastEvaluatedKey(lastEvaluatedKey)
                .build();
    }


    @Override
    public Page<VideoEntity> findAllByUserId(UUID userId, int pageSize, int pageNumber) {
        Map<String, AttributeValue> lastEvaluatedKey = null;

        for (int i = 0; i < pageNumber; i++) {
            ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                    .filterExpression(Expression.builder()
                            .expression("userId = :userId")
                            .expressionValues(Map.of(":userId",
                                    AttributeValue.builder().s(userId.toString()).build()))
                            .build())
                    .limit(pageSize)
                    .exclusiveStartKey(lastEvaluatedKey) // Ponto de partida para a página
                    .build();

            PageIterable<VideoEntity> pages = dynamoDbTemplate.scan(scanRequest, VideoEntity.class);
            Page<VideoEntity> page = pages.iterator().next(); // Obtém a página atual

            if (i == pageNumber - 1) { // Retorna a página desejada
                return page;
            }

            lastEvaluatedKey = page.lastEvaluatedKey(); // Avança para a próxima página
            if (lastEvaluatedKey == null) {
                break; // Se não há mais páginas, retorna a última
            }
        }


        return Page.builder(VideoEntity.class).build();  // Retorna uma página vazia se não houver mais resultados
    }
}
