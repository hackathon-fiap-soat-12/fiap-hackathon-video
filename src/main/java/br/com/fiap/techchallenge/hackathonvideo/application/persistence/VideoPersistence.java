package br.com.fiap.techchallenge.hackathonvideo.application.persistence;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoPersistence {

    Video create(Video video);

    Video update(Video video);

    Optional<Video> findById(UUID id);

    List<Video> findAllByUserId(UUID userId);
}
