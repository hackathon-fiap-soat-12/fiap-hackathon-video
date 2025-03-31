package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.PaginatedResponse;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepository {
    VideoEntity save(VideoEntity videoEntity);

    Optional<VideoEntity> findById(UUID id);

    List<VideoEntity> findAllByUserId(UUID userId);

    PaginatedResponse<VideoEntity> findAllByUserId(UUID userId, int pageSize, Map<String, AttributeValue> exclusiveStartKey);

    Page<VideoEntity> findAllByUserId(UUID userId, int pageSize, int pageNumber);
}
