package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.VideoRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VideoPersistenceImpl implements VideoPersistence {

    private final VideoRepository repository;

    public VideoPersistenceImpl(VideoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Video create(Video video) {
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
    public List<Video> findAllByUserId(UUID userId) {
        Page<VideoEntity> page = repository.findAllByUserId(userId, 30,3 );
        List<VideoEntity> videos = page.items();
        return videos.stream().map(VideoEntity::toModel).toList();
    }
}
