package br.com.fiap.techchallenge.hackathonvideo.domain.models;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.fiap.techchallenge.hackathonvideo.domain.constants.Constants.*;

public class Video {

    private final UUID id;

    private final User user;

    private final String videoName;

    private final String videoKey;

    private final String framesKey;

    private ProcessStatus status;

    private final Audit audit;

    public Video(UUID id, User user, String videoKey, String framesKey, String videoName,
                 ProcessStatus status, Audit audit) {
        this.id = id;
        this.user = user;
        this.videoName = videoName;
        this.videoKey = videoKey;
        this.framesKey = framesKey;
        this.status = status;
        this.audit = audit;
    }

    public Video(String videoName, User user) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.videoName = videoName;
        this.videoKey = PATH_VIDEO + videoName;
        this.framesKey = PATH_FRAMES + videoName + ".zip";
        this.status = ProcessStatus.NEW;
        this.audit = new Audit(LocalDateTime.now(), LocalDateTime.now());
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

    public String getVideoName() {
        return videoName;
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
        audit.setUpdatedAt(LocalDateTime.now());
    }

    public LocalDateTime getCreatedAt() {
        return audit.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return audit.getUpdatedAt();
    }

    public void setReceived() {
        this.status = ProcessStatus.RECEIVED;
        audit.setUpdatedAt(LocalDateTime.now());
    }
}
