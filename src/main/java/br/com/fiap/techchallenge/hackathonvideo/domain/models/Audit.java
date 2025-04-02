package br.com.fiap.techchallenge.hackathonvideo.domain.models;

import java.time.LocalDateTime;

public class Audit {
    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Audit(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime now) {
        this.updatedAt = now;
    }
}
