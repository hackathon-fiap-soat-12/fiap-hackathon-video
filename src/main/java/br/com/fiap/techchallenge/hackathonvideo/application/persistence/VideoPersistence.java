package br.com.fiap.techchallenge.hackathonvideo.application.persistence;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import java.util.Optional;
import java.util.UUID;

public interface VideoPersistence {

    Video save(Video video);

    Video update(Video video);

    Optional<Video> findById(UUID id);

    CustomPage findAllByUserId(UUID userId, Integer pageSize, String exclusiveStartKey);
}
