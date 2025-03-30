package br.com.fiap.techchallenge.hackathonvideo.domain.models;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class PresignedFile {

    private final UUID id;
    private final String url;
    private final PresignedMethods method;
    private final Instant expiresIn;

    public PresignedFile(UUID id, String url, PresignedMethods method, Instant expiresIn) {
        this.id = id;
        this.url = url;
        this.method = method;
        this.expiresIn = expiresIn;
    }

    public String getUrl() {
        return url;
    }

    public Integer getSecondsToExpire() {
        return (int) Duration.between(Instant.now(), this.expiresIn).toSeconds();
    }

    public UUID getId() {
        return id;
    }

    public PresignedMethods getMethod() {
        return method;
    }
}
