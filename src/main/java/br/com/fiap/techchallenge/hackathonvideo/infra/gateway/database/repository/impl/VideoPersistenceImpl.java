package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.VideoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class VideoPersistenceImpl implements VideoPersistence {

    private final ObjectMapper objectMapper;
    private final VideoRepository repository;

    public VideoPersistenceImpl(ObjectMapper objectMapper, VideoRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @Override
    public Video save(Video video) {
        var videoSaved = repository.save(new VideoEntity(video));
        return videoSaved.toModel();
    }

    @Override
    public Video update(Video video) {
        var videoSaved = repository.save(new VideoEntity().update(video));
        return videoSaved.toModel();
    }

    @Override
    public Optional<Video> findById(UUID id) {
        var videoFound = repository.findById(id);
        return videoFound.map(VideoEntity::toModel);
    }

    @Override
    public CustomPage findAllByUserId(UUID userId, Integer pageSize, String exclusiveStartKey) {
        Map<String, AttributeValue> exclusiveStartKeyMap = null;

        if (exclusiveStartKey != null && !exclusiveStartKey.isEmpty()) {
            exclusiveStartKeyMap = this.parseExclusiveStartKey(exclusiveStartKey);
        }

        var paginatedResponse = repository.findAllByUserId(userId, pageSize, exclusiveStartKeyMap);
        var items = paginatedResponse.getItems().stream().map(VideoEntity::toModel).toList();
        var lastEvaluatedKeyAsMap = this.convertLastEvaluatedKeyToString(paginatedResponse.getLastEvaluatedAttributeValue());


        return new CustomPage(items, lastEvaluatedKeyAsMap);
    }

    private Map<String, AttributeValue> parseExclusiveStartKey(String exclusiveStartKeyJson) {
        try {
            Map<String, String> stringMap = objectMapper.readValue(exclusiveStartKeyJson, new TypeReference<Map<String, String>>() {});

            return stringMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> AttributeValue.builder().s(entry.getValue()).build()
                    ));
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao converter exclusiveStartKey para Map<String, AttributeValue>", e);
        }
    }

    public String convertLastEvaluatedKeyToString(Map<String, AttributeValue> lastEvaluatedKey) {
        if (lastEvaluatedKey == null || lastEvaluatedKey.isEmpty()) {
            return null;
        }

        try {
            // Converte Map<String, AttributeValue> para Map<String, String>
            Map<String, String> keyAsStringMap = lastEvaluatedKey.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().s() != null ? entry.getValue().s() : entry.getValue().n()
                    ));

            // Retorna a string JSON ao invés de um objeto JSON
            return objectMapper.writeValueAsString(keyAsStringMap);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter LastEvaluatedKey para String", e);
        }
    }
}

//        List<VideoEntity> videos = response.getItems();
//        Map<String, AttributeValue> nextKey = response.getLastEvaluatedKey();
//
//// Buscar a próxima página se houver mais registros
//        if (nextKey != null) {
//            PaginatedResponse<VideoEntity> nextPage = repository.findAllByUserId(userId, 10, nextKey);
//            List<VideoEntity> nextVideos = nextPage.getItems();
//        }
//
//
//
//
////        Page<VideoEntity> page = repository.findAllByUserId(userId, 30,3 );
////        List<VideoEntity> videos = page.items();
//
//        var paged = repository.findAllByUserId(userId);
//
//        return paged.stream().map(VideoEntity::toModel).toList();
