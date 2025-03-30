package br.com.fiap.techchallenge.hackathonvideo.domain.models;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.fiap.techchallenge.hackathonvideo.domain.constants.Constants.*;

public class Video {

    private UUID id;

    private final User user;

    private final String videoKey;

    private final String framesKey;

    private ProcessStatus status;

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Video(UUID id, User user, String videoKey, String framesKey,
                 ProcessStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.videoKey = videoKey;
        this.framesKey = framesKey;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Video(String fileName, User user) {
        this.user = user;
        this.videoKey = user.getId() + PATH_VIDEO + fileName;
        this.framesKey = user.getId() + PATH_FRAMES + fileName;
        this.status = ProcessStatus.NEW;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return this.user.getId();
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public String getBucketName() {
        return BUCKET_NAME;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public String getFramesKey() {
        return framesKey;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setReceived() {
        this.status = ProcessStatus.RECEIVED;
        this.updatedAt = LocalDateTime.now();
    }
}
