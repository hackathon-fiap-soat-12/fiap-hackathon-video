package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;
import java.util.UUID;

@DynamoDbBean
public class VideoEntity {

    private UUID id;

    private UUID userId;

    private String userEmail;

    private String bucketName;

    private String videoName;

    private String videoKey;

    private String framesKey;

    private ProcessStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public VideoEntity() {
    }

    public VideoEntity(Video video) {
        this.assignItems(video);
    }

    public VideoEntity update(Video video) {
        this.assignItems(video);
        return this;
    }

    private void assignItems(Video video){
        this.id = video.getId();
        this.userId = video.getUserId();
        this.userEmail = video.getUserEmail();
        this.bucketName = video.getBucketName();
        this.videoName = video.getVideoName();
        this.videoKey = video.getVideoKey();
        this.framesKey = video.getFramesKey();
        this.status = video.getStatus();
        this.createdAt = video.getCreatedAt();
        this.updatedAt = video.getUpdatedAt();
    }

    @DynamoDbPartitionKey
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

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getVideoName() {
        return this.videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
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

    public Video toModel() {
        return new Video(
                this.id,
                new User(this.userId, this.userEmail),
                this.videoKey,
                this.framesKey,
                this.videoName,
                this.status,
                new Audit(this.createdAt, this.updatedAt)
        );
    }
}
