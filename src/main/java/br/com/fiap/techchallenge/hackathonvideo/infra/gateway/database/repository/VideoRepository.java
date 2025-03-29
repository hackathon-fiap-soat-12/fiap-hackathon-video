package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.Optional;
import java.util.UUID;

public interface VideoRepository {
    VideoEntity save(VideoEntity videoEntity);

    Optional<VideoEntity> findById(UUID id);

    Page<VideoEntity> findAllByUserId(UUID userId, int pageSize, int pageNumber);
}
