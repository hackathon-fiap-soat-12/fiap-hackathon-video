package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.VideoRepository;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
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

    public PageIterable<VideoEntity> findAllByUserId(UUID userId) {
        ScanEnhancedRequest scanEnhancedRequest = ScanEnhancedRequest.builder()
                .filterExpression(Expression.builder()
                        .expression("userId = :userIdVal")
                        .expressionValues(Map.of(":userIdVal",
                                AttributeValue.builder().s(userId.toString()).build()))
                        .build())
                .build();

        return dynamoDbTemplate.scan(scanEnhancedRequest, VideoEntity.class);
    }

    @Override
    public Page<VideoEntity> findAllByUserId(UUID userId, int pageSize, int pageNumber) {
        Map<String, AttributeValue> lastEvaluatedKey = null;

        for (int i = 0; i < pageNumber; i++) {
            ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
                    .filterExpression(Expression.builder()
                            .expression("userId = :userId")
                            .expressionValues(Map.of(":userId", AttributeValue.builder().s(userId.toString()).build()))
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
