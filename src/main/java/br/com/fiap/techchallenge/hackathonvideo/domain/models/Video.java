package br.com.fiap.techchallenge.hackathonvideo.domain.models;

import br.com.fiap.techchallenge.hackathonvideo.domain.constants.Constants;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Video {

    private UUID id;

    private UUID userId;

    private String userEmail;

    private String bucketName = Constants.BUCKET_NAME;

    private String videoKey;

    private String framesKey;

    private ProcessStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Video(UUID id, UUID userId, String userEmail, String bucketName, String videoKey, String framesKey,
                 ProcessStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.bucketName = bucketName;
        this.videoKey = videoKey;
        this.framesKey = framesKey;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Video(String fileName, UUID userId, String userEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.videoKey = fileName;
        this.status = ProcessStatus.NEW;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getFramesKey() {
        return framesKey;
    }

    public void setFramesKey(String framesKey) {
        this.framesKey = framesKey;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
